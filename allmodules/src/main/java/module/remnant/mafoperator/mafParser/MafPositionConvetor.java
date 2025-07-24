package module.remnant.mafoperator.mafParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MafPositionConvetor {

	public static void main(String[] args) {
		MafPositionConvetor mafPositionConvetor = new MafPositionConvetor();
		mafPositionConvetor.runProcess(args);
	}

	public void runProcess(String[] args) {

		if (args == null || args.length < 3) {
			System.err.println("Usage:" + ""
					+ "java -cp maf_postion_converter.jar egps.core.popGen.mafParser.MafPositionConvetor 0 maf_file location_file	-v"
					+ "or "
					+ "java -cp maf_postion_converter.jar egps.core.popGen.mafParser.MafPositionConvetor 0 maf_file location_file");
		}

		// System.out.println(Arrays.toString(args));
//		File file = new File("/Users/evolgene/Desktop/target.maf");
		File file = new File(args[1]);
		// 0 is STRCA.chrZ ; 1 is Ggal.chr2
		int inputIndex = Integer.parseInt(args[0]);
		File inputLocation = new File(args[2]);
//		File inputLocation = new File("/Users/evolgene/Desktop/target.maf.inputLocation.txt");

		boolean verbose = false;
		if (args.length == 4 && args[3].equals("-v")) {
			verbose = true;
		}

		List<QueryRecord> queryRecords = getInputQueryMap(inputLocation);
		Map<String, ChromosomePositionHandler> queryChrMap = new HashMap<String, ChromosomePositionHandler>();
		List<ResultRecord> resultRecords = new ArrayList<ResultRecord>();

		for (QueryRecord queryRecord : queryRecords) {
			String chr = queryRecord.getChr();
			ChromosomePositionHandler chromosomePositionHandler = queryChrMap.get(chr);
			if (chromosomePositionHandler == null) {
				chromosomePositionHandler = new ChromosomePositionHandler(chr, resultRecords);
				ArrayList<Integer> arrayList = new ArrayList<>();
				arrayList.add(queryRecord.getStartPos());
				chromosomePositionHandler.setQueryLocations(arrayList);
				queryChrMap.put(chr, chromosomePositionHandler);
			} else {
				chromosomePositionHandler.getQueryLocations().add(queryRecord.getStartPos());
			}
		}

		MafFastaParser mfp = new MafFastaParser(file);

		int numOfLines = 0;

		while (mfp.parseBlocks()) {
			MafBlock block = mfp.getBlock();
			List<MafLine> lines = block.getLines();

			MafLine mafLine = lines.get(inputIndex);

			ChromosomePositionHandler chromosomePositionHandler = queryChrMap.get(mafLine.getRefSrc());
			if (chromosomePositionHandler != null) {
				chromosomePositionHandler.handleOneMafBlock(lines, inputIndex);
			}

//        	numOfLines ++;
//        	
//        	if (numOfLines > 10 ) {
//				break;
//			}

		}
		mfp.close();

		// One query record may have 2 or more results
		Map<QueryRecord, List<ResultRecord>> mapResults = new HashMap<>();
		for (ResultRecord resultRecord : resultRecords) {
			String queryChr = resultRecord.getQueryChr();
			int queryPos = resultRecord.getQueryPos();

			QueryRecord queryRecord = new QueryRecord();
			queryRecord.setChr(queryChr);
			queryRecord.setStartPos(queryPos);
			
			List<ResultRecord> list = mapResults.get(queryRecord);
			if (list == null) {
				list = new ArrayList<ResultRecord>();
				list.add(resultRecord);
				mapResults.put(queryRecord, list);
			}else {
				list.add(resultRecord);
			}
			
		}
		System.out.println("query\tposition\ttarget\tposition");
		for (QueryRecord resultRecord : queryRecords) {

			List<ResultRecord> resultRecords1 = mapResults.get(resultRecord);
			if (resultRecords1 == null) {
				System.out.println(resultRecord.getChr() + "\t" + resultRecord.getStartPos() + "\tNull");
			} else {
				for (ResultRecord tmpResultRecord : resultRecords1) {
					System.out.println(tmpResultRecord);
				}
			}

		}

		if (!verbose) {

			System.out.println("\n\n");

			for (QueryRecord resultRecord : queryRecords) {

				List<ResultRecord> resultRecords1 = mapResults.get(resultRecord);
				if (resultRecords1 == null) {
					System.out.println(resultRecord.getChr() + "\t" + resultRecord.getStartPos() + "\tNull");
				} else {
					for (ResultRecord tmpResultRecord : resultRecords1) {
						System.out.println(tmpResultRecord);
						
						List<MafLine> mafLines = tmpResultRecord.getMafLines();
						for (MafLine ll : mafLines) {
							System.out.println(ll);
						}
					}
				}
				

			}
			
			

		}
	}

	private List<QueryRecord> getInputQueryMap(File inputLocation) {
		List<QueryRecord> ret = new ArrayList<QueryRecord>();

		try (BufferedReader br = new BufferedReader(new FileReader(inputLocation));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split("\t");
				QueryRecord queryRecord = new QueryRecord();
				queryRecord.setChr(split[0]);
				// 1 based to 0 based!
				queryRecord.setStartPos(Integer.parseInt(split[1]) - 1);
				ret.add(queryRecord);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

}

class ChromosomePositionHandler {
	final static char GAP_CHR = '-';
	List<Integer> queryLocations;
	private List<ResultRecord> resultRecords;
	private String queryChr;

	/** 当调用getPosition方法后，这个变量会被赋值！*/
	private int blockIndexWithGaps = 0;
	private String targetMafLineSrcString;

	public ChromosomePositionHandler(String chr, List<ResultRecord> resultRecords) {
		this.resultRecords = resultRecords;
		this.queryChr = chr;
	}

	public void setQueryLocations(List<Integer> queryLocations) {
		this.queryLocations = queryLocations;
	}

	public boolean handleOneMafBlock(List<MafLine> lines, int inputIndex) {
		boolean hasMatch = false;

		MafLine mafLine = lines.get(inputIndex);
		int start = mafLine.getStart();
		int size = mafLine.getSize();

		for (int location : queryLocations) {

			if (location >= start && location < start + size) {
				ResultRecord resultRecord = new ResultRecord();
				resultRecord.setMafLines(lines);
				resultRecord.setQueryChr(queryChr);
				resultRecord.setQueryPos(location);
				resultRecord.setTargetPos(getPosition(inputIndex, lines, location - start));
				resultRecord.setTargetChr(targetMafLineSrcString);

				String ss = "";
				for (MafLine mafLine2 : lines) {
					ss += mafLine2.getSequence().charAt(blockIndexWithGaps);
				}

				resultRecord.setMatchedChars(ss);
				resultRecords.add(resultRecord);
				hasMatch = true;
			}
		}

		return hasMatch;
	}

	private int getPosition(int inputIndex, List<MafLine> lines, int passedNuclWithoutGap) {
		int positionInMafBlockWithoutGapsInQuery = -1;

		MafLine mafLine = lines.get(inputIndex);
		String sequence = mafLine.getSequence();
		int length = sequence.length();

		for (int i = 0; i < length; i++) {
			char charAt = sequence.charAt(i);
			if (charAt != GAP_CHR) {
				positionInMafBlockWithoutGapsInQuery++;

				if (positionInMafBlockWithoutGapsInQuery == passedNuclWithoutGap) {
					blockIndexWithGaps = i;
					break;
				}
			}

		}

		// another mafLine
		int anotherIndex = inputIndex == 0 ? 1 : 0;
		MafLine mafLine2 = lines.get(anotherIndex);
		String sequence2 = mafLine2.getSequence();
		String headerString = sequence2.substring(0, blockIndexWithGaps);
		targetMafLineSrcString = mafLine2.getRefSrc();

		int positionInTargetStrWithoutGap = 0;
		int length2 = headerString.length();
		for (int i = 0; i < length2; i++) {
			char charAt = headerString.charAt(i);
			if (charAt != GAP_CHR) {
				positionInTargetStrWithoutGap++;
			}
		}

		if (positionInMafBlockWithoutGapsInQuery == -1) {
			System.err.println("This is an internal err! " + getClass());
		}

		return mafLine2.getStart() + positionInTargetStrWithoutGap;
	}

	public List<Integer> getQueryLocations() {
		return queryLocations;
	}

}
