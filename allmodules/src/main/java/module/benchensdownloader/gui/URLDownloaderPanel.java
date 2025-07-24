/**
 * 
 */
package module.benchensdownloader.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import tsv.io.KitTable;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import module.benchensdownloader.URLDirectDownloader;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class URLDownloaderPanel extends DIYToolSubTabModuleFace {

	public URLDownloaderPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	private List<String> outputStringKept = new ArrayList<>();

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.download.txt", "",
				"The line start with # will be ignored.\n# The contents to download, input the file path, tsv file format, Necessary.");
		mapProducer.addKeyValueEntryBean("target.column", "1",
				"The column used to download target file, first column is 1, default is 1.");
		mapProducer.addKeyValueEntryBean("output.dir.file", "", "The output dir to store the results.");
		mapProducer.addKeyValueEntryBean("has.header", "F",
				"Whether the tsv file has hader line, i.e. skip the first lien");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {

		String inputURLPath = null;
		String outputFilePath = null;
		int targetColumn = 0;
		boolean hasHeader = false;

		String string = para.get("$input.download.txt");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException(
					"Plese input the download file path.");
		} else {
			inputURLPath = string;
		}

		string = para.get("$target.column");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the target.column.");
		} else {
			targetColumn = Integer.parseInt(string);
		}

		string = para.get("$output.dir.file");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the output.dir.file");
		} else {
			outputFilePath = string;
		}
		string = para.get("$has.header");
		if (Strings.isNullOrEmpty(string)) {

		} else {
			hasHeader = BooleanUtils.toBoolean(string);
		}

		run(inputURLPath, targetColumn - 1, new File(outputFilePath), hasHeader);
	}

	private void run(String inputURLPath, int targetColumn, File outputDir, boolean hasHeader) throws IOException {
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		outputStringKept.clear();

		KitTable tsvTextFile = TSVReader.readTsvTextFile(inputURLPath, hasHeader);

		List<List<String>> contents = tsvTextFile.getContents();

		Iterator<List<String>> iterator = contents.iterator();
		while (iterator.hasNext()) {
			List<String> next = iterator.next();
			String string = next.get(0);
			if (string.length() > 0 && string.charAt(0) == '#') {
				iterator.remove();
			}

		}

		int size = contents.size();

		int curr = 0;

		URLDirectDownloader urlDirectDownloader = new URLDirectDownloader();
		urlDirectDownloader.setOutputDir(outputDir);

		for (List<String> list : contents) {
			curr++;

			String downloadURL = list.get(targetColumn);

			List<String> outputs = new LinkedList<>();
			outputs.addAll(outputStringKept);

			long timeMillis = System.currentTimeMillis();

			urlDirectDownloader.downloadContent(computationalModuleFace, downloadURL);

			outputs.add("Current is: ".concat(downloadURL));
			outputs.add("Current is: " + curr + "  /  " + size);

			long ms = (long) (Math.random() * 1000L);
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long thisTimeMillis = System.currentTimeMillis();

			outputs.add("Take time of  " + (thisTimeMillis - timeMillis) + " milliseconds to download the file.");
			setText4Console(outputs);
		}

	}

}
