package module.benchensdownloader.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import org.apache.commons.io.FileUtils;

import tsv.io.KitTable;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

@SuppressWarnings("serial")
public class UrlParserPanelOnlyOneLevel extends DockableTabModuleFaceOfVoice {

	private String targetSuffix;
	private Pattern targetFileNamePattern;
	private List<String> outputStringKept = new ArrayList<>();

	public UrlParserPanelOnlyOneLevel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	public String getShortDescription() {
		return new String(
				"By manually comparing the rules of the website, some url (like gff3 file) do not need the $molecular.type parameters");
	}

	@Override
	public String getTabName() {
		return new String("2.2 URL Batch Parser with one level");
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.download.txt", "",
				"The line start with # will be ignored.\n # The contents to download, input the file path, tsv file format, Necessary.");
		mapProducer.addKeyValueEntryBean("has.header", "F",
				"Whether the tsv file has hader line, i.e. skip the first line");
		mapProducer.addKeyValueEntryBean("target.column", "1",
				"The column used to download target file, first column is 1, default is 1.");
		mapProducer.addKeyValueEntryBean("output.summary.file", "", "The output **dir** to store the results.");
		mapProducer.addKeyValueEntryBean("target.suffix", "112.gff3.gz",
				"There are several files listed, only download the target files with suffix.");

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		String inputURLPath = o.getSimplifiedString("input.download.txt");
		int targetColumn = o.getSimplifiedInt("target.column");
		boolean hasHeader = o.getSimplifiedBool("has.header");

		String outputDir = o.getSimplifiedString("output.summary.file");

		targetSuffix = o.getSimplifiedString("target.suffix");

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
			sJ.append("/");

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
