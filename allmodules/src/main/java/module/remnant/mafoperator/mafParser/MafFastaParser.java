package module.remnant.mafoperator.mafParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * * The MAFparser will parse a MAF file obtained from UCSC in either text of
 * gzipped format. The format should be in the following form:<br>
 * <br>
 * <code>
 * ##maf version=1 scoring=autoMZ.v1<br>
 * a score=15057.000000<br>
 * s hg19.chrM                   0 187 + 16571 GATCACAGGTCTATCACCCT...<br>
 * s gorGor1.Supercontig_0439211 0 184 +   616 GATCACAGGTCTATCACCCT...<br>
 * q gorGor1.Supercontig_0439211               57889999999999999999...<br>
 * i gorGor1.Supercontig_0439211 N 0 I 21<br>
 * </code>
 * 
 * @author mjaeger
 *
 */
public class MafFastaParser implements AutoCloseable {

	private File inputFile;
	private MafBlock block;
	private boolean isGziped;
//	private boolean isFirstBlock = true;
	private BufferedReader bufferedReader;
	private List<String> mafLines;

	/*
	 * private Pattern mafBlockPat = Pattern.compile(
	 * "^a\\sscore=([\\-\\d.])+$\n(^s.+\n)((^s.+\n)(^q.+\n)?(^i.+\n)?)+(e.+\n)*");
	 * private Pattern fastaBlockPat = Pattern.
	 * compile("^>(uc[a-z0-9\\.]+)_([ElegantJTable-Za-z0-9]+)[_\\d]+( [\\d]+){3} (([^\\s]+:[\\d]+-[\\d]+)([\\+-]))*$"
	 * ); private Pattern coordinatsPat =
	 * Pattern.compile("([^\\s]+):([\\d]+)-([\\d]+)$"); private Matcher matcher;
	 */

	public MafFastaParser(String filename) {
		this.inputFile = new File(filename);
		init();
	}

	public MafFastaParser(File file) {
		this.inputFile = file;
		init();
	}

	/**
	 * This will return the {@link MafBlock} containing the information
	 * 
	 * @return
	 */
	public MafBlock getBlock() {
		return block;
	}

	private void init() {
		if (inputFile.getName().endsWith(".gz"))
			this.isGziped = true;
		try {
			if (this.isGziped)
				this.bufferedReader = new BufferedReader(
						new InputStreamReader(new GZIPInputStream(new FileInputStream(this.inputFile))));
			else
				this.bufferedReader = new BufferedReader(new FileReader(this.inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			this.bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected List<String> readBlock() throws IOException {
		String line;

		mafLines = new ArrayList<>(50);
		while ((line = bufferedReader.readLine()) != null) {

			if (line.equals("")) {
				return mafLines;
			} else if (line.startsWith("#")) {
				continue;
			} else {
				mafLines.add(line);
			}
		}

		if (mafLines.size() > 0) {
			return mafLines;
		} else {
			return null;
		}
	}

	/**
	 * Irately parse each block
	 * 
	 * @author yudalang
	 * @date 2018-11-2
	 * @return true if has next block, false if come to end
	 */
	public boolean parseBlocks() {
		block = new MafBlock();
		List<String> blockUnit;
		try {
			if ((blockUnit = readBlock()) != null) {
				
				for (String str : blockUnit) {
					if (str.startsWith("a")) {

						String[] split = str.split(" ");

						for (String string : split) {
							if (string.startsWith("score=")) {
								Double dt = Double.parseDouble(string.substring(6));
								block.setScore(dt.intValue());
								break;
							}
						}

					} else if (str.startsWith("s")) {
						block.addLine(new MafLine(str));
					}
				}

			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}


}
