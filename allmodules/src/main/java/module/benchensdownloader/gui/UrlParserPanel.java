/**
 * 
 */
package module.benchensdownloader.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import tsv.io.KitTable;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class UrlParserPanel extends DIYToolSubTabModuleFace {

	private String targetSuffix;
	private String targetMoleculeType;
	private Pattern targetFileNamePattern;
	private List<String> outputStringKept = new ArrayList<>();

	public UrlParserPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.download.txt", "",
				"The line start with # will be ignored.\n # The contents to download, input the file path, tsv file format, Necessary.");
		mapProducer.addKeyValueEntryBean("has.header", "F",
				"Whether the tsv file has hader line, i.e. skip the first line");
		mapProducer.addKeyValueEntryBean("target.column", "1",
				"The column used to download target file, first column is 1, default is 1.");
		mapProducer.addKeyValueEntryBean("output.summary.file", "", "The output dir to store the results.");
		mapProducer.addKeyValueEntryBean("target.suffix", "all.fa.gz",
				"There are several files listed, only download the target files with suffix.");

		Set<String> moleculeType = new HashSet<>(
				Arrays.asList("cdna", "cds", "dna", "ncrna", "pep", "dna_index"));
		mapProducer.addKeyValueEntryBean("molecule.type", "pep",
				"From the Ensembl website, the possible values are: ".concat(moleculeType.toString()));


	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {

		String inputURLPath = null;
		String outputDir = null;
		int targetColumn = 0;
		boolean hasHeader = false;

		String string = para.get("$input.download.txt");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException(
					"# The content start with # will be ignored.\nPlese input the download file path.");
		} else {
			inputURLPath = string;
		}

		string = para.get("$target.column");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the target.column.");
		} else {
			targetColumn = Integer.parseInt(string);
		}

		string = para.get("$output.summary.file");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the output.dir.file");
		} else {
			outputDir = string;
		}
		string = para.get("$target.suffix");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the target.suffix");
		} else {
			targetSuffix = string;
		}
		string = para.get("$molecule.type");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the target.suffix");
		} else {
			targetMoleculeType = string;
		}
		string = para.get("$has.header");
		if (Strings.isNullOrEmpty(string)) {

		} else {
			hasHeader = BooleanUtils.toBoolean(string);
		}

		run(inputURLPath, targetColumn - 1, new File(outputDir), hasHeader);
	}

	private void run(String inputURLPath, int targetColumn, File outputDir, boolean hasHeader) throws IOException {
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		outputStringKept.clear();

		targetFileNamePattern = Pattern.compile("<a href=\"(.*?" + targetSuffix + ")\">");

		KitTable tsvTextFile = TSVReader.readTsvTextFile(inputURLPath, hasHeader);

		List<List<String>> contents = tsvTextFile.getContents();
		int size = contents.size();

		int curr = 0;

		List<String> summaryOutput = new ArrayList<>();

		StringBuilder sJ = new StringBuilder();
		for (List<String> list : contents) {
			curr++;

			String downloadURL = list.get(targetColumn);
			sJ.setLength(0);
			sJ.append(downloadURL);
			sJ.append("/").append(targetMoleculeType).append("/");

			String string = sJ.toString();
			
			appendText2Console("> Current is: ".concat(string));

			String matchedElement = null;
			try {
				matchedElement = extractPathOfFastaGZ(string);
			} catch (FileNotFoundException e) {
				outputStringKept.add(string.concat("\tURL not found."));
				continue;
			}


			List<String> outputs = new LinkedList<>();
			outputs.addAll(outputStringKept);
			outputs.add("Processed is: ".concat(string));
			outputs.add("Processed is: " + curr + "  /  " + size + "  matchedElement is : " + matchedElement);

			setText4Console(outputs);

			long ms = (long) (Math.random() * 1000L);
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			sJ.append(matchedElement);
			summaryOutput.add(sJ.toString());
		}

		FileUtils.writeLines(new File(outputDir, "summary.tsv"), summaryOutput);

	}


	private String extractPathOfFastaGZ(String urlPath) throws IOException {
		
		URL url = new URL(urlPath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));


		String ret = null;

		String line;
		// 读取每一行，寻找文件夹链接
		while ((line = reader.readLine()) != null) {
			Matcher matcher = targetFileNamePattern.matcher(line);
			if (matcher.find()) {
				String dirName = matcher.group(1); // 获取文件夹名称
				// 要得到 all.fa.gz的 文件
				// <a href="Acanthochromis_polyacanthus.ASM210954v1.cdna.all.fa.gz">
				ret = dirName;
				break;
			}

		}

		reader.close();

		return ret;
	}

}
