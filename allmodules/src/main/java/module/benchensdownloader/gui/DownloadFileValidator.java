/**
 * 
 */
package module.benchensdownloader.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import utils.EGPSFileUtil;
import tsv.io.KitTable;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class DownloadFileValidator extends DIYToolSubTabModuleFace {

	public DownloadFileValidator(ComputationalModuleFace cmf) {
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

		mapProducer.addKeyValueEntryBean("results.file.name", "file_validate_reports.tsv",
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


		long timeMillis = System.currentTimeMillis();

		boolean allSuccess = true;
		int size = contents.size();
		for (int i = 0; i < size; i++) {
			List<String> list = contents.get(i);
			curr++;

			String downloadURL = list.get(targetColumn);

			String additionalLine = "";
			// #success
			// fail
			String filename = downloadURL.substring(downloadURL.lastIndexOf('/') + 1);
			File fileInDir = new File(downloadDir, filename);

			boolean result = validateFile(fileInDir);
			if (result) {
				additionalLine = "#success";
			} else {
				additionalLine = "fail";
				allSuccess = false;
			}

			list.add(0, additionalLine);

			List<String> outputs = new LinkedList<>();
			outputs.add("Current is: ".concat(downloadURL));
			outputs.add("Current is: " + curr + "  /  " + size);
			setText4Console(outputs);
		}

		if (hasHeader) {
			List<String> headerNames = tsvTextFile.getHeaderNames();
			headerNames.add(0, "validateColumn");
		}
		tsvTextFile.save2file(outputDir.getAbsolutePath());

		List<String> outputs = new LinkedList<>();
		outputs.add(
				"Take time of  " + (System.currentTimeMillis() - timeMillis) + " milliseconds to process files.");

		if (allSuccess) {
			outputs.add("Congratulations on successful validation of all files");
		} else {
			outputs.add("Sorry, some files maybe broken, please cheke the ");
		}
		setText4Console(outputs);


	}

	private boolean validateFile(File fileInDir) {
		if (!fileInDir.exists()) {
			return false;
		}

		try {
			InputStream inputStream = EGPSFileUtil
					.getInputStreamFromOneFileMaybeCompressed(fileInDir.getAbsolutePath());

			final int readLineLimit = 100000;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				String readLine = null;
				int count = 0;
				while ((readLine = br.readLine()) != null) {
					count++;
					if (count > readLineLimit) {
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
