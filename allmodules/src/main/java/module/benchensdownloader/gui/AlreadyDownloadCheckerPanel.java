/**
 * 
 */
package module.benchensdownloader.gui;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
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
public class AlreadyDownloadCheckerPanel extends DIYToolSubTabModuleFace {

	public AlreadyDownloadCheckerPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.download.txt", "",
				"The line start with # will be ignored.\n# The contents to download, input the file path, tsv file format, Necessary.");
		mapProducer.addKeyValueEntryBean("target.column", "1",
				"The column used to download target file, first column is 1, default is 1.");
		mapProducer.addKeyValueEntryBean("download.dir.file", "", "The output dir that has already downloaded files.");
		mapProducer.addKeyValueEntryBean("has.header", "F",
				"Whether the tsv file has hader line, i.e. skip the first line");

		mapProducer.addKeyValueEntryBean("results.file.name", "file_stilToDownload.tsv",
				"The output file name to store the results. The directory of file path is same as the input file.");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {

		String inputURLPath = null;
		String outputFileName = null;
		String queryAlreadyDownloadDirPath = null;
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

		string = para.get("$results.file.name");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the output file name");
		} else {
			outputFileName = string;
		}
		string = para.get("$has.header");
		if (Strings.isNullOrEmpty(string)) {

		} else {
			hasHeader = BooleanUtils.toBoolean(string);
		}

		string = para.get("$download.dir.file");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the output.dir.file");
		} else {
			queryAlreadyDownloadDirPath = string;
		}

		File inputURLParentFile = new File(inputURLPath).getParentFile();
		run(inputURLPath, targetColumn - 1, new File(inputURLParentFile, outputFileName), hasHeader,
				new File(queryAlreadyDownloadDirPath));
	}

	private void run(String inputURLPath, int targetColumn, File outputDir, boolean hasHeader, File downloadDir)
			throws IOException {

		KitTable tsvTextFile = TSVReader.readTsvTextFile(inputURLPath, hasHeader);

		List<List<String>> contents = tsvTextFile.getContents();
		int curr = 0;

		URLDirectDownloader urlDirectDownloader = new URLDirectDownloader();
		urlDirectDownloader.setOutputDir(outputDir);

		String[] list2 = downloadDir.list();
		HashSet<String> alreadDownloadFileNames = new HashSet<>();
		for (String string : list2) {
			String filename = string.substring(string.lastIndexOf('/') + 1);
			alreadDownloadFileNames.add(filename);
		}

		long timeMillis = System.currentTimeMillis();

		int size = contents.size();
		for (int i = 0; i < size; i++) {
			List<String> list = contents.get(i);
			curr++;

			String downloadURL = list.get(targetColumn);

			String additionalLine = "";

			String filename = downloadURL.substring(downloadURL.lastIndexOf('/') + 1);
			if (alreadDownloadFileNames.contains(filename)) {
				additionalLine = "#";
			}

			list.add(0, additionalLine);


		}

		if (hasHeader) {
			List<String> headerNames = tsvTextFile.getHeaderNames();
			headerNames.add(0, "checkColumn");
		}
		tsvTextFile.save2file(outputDir.getAbsolutePath());

		List<String> outputs = new LinkedList<>();
		outputs.add(
				"Take time of  " + (System.currentTimeMillis() - timeMillis) + " milliseconds to process files.");
		setText4Console(outputs);

	}

}
