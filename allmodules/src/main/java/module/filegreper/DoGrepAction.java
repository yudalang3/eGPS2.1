package module.filegreper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import utils.EGPSFileUtil;
import analysis.AbstractAnalysisAction;
import egps2.modulei.RunningTask;

public class DoGrepAction extends AbstractAnalysisAction implements RunningTask {

	protected String inputFilePath;
	protected int followingLineNumber = 0;
	protected boolean exportHeader = false;
	String[] searchKeys;

	int searchedResultsLimitation = Integer.MAX_VALUE;

	private List<BoyerMoore> bms;
	protected List<String> outputList = new ArrayList<>();

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public void setFollowingLineNumber(int followingLineNumber) {
		this.followingLineNumber = followingLineNumber;
	}

	public void setExportHeader(boolean exportHeader) {
		this.exportHeader = exportHeader;
	}

	public void setSearchKeys(String[] searchKeys) {
		this.searchKeys = searchKeys;
	}

	public void setSearchedResultsLimitation(int searchedResultsLimitation) {
		this.searchedResultsLimitation = searchedResultsLimitation;
	}

	@Override
	public boolean isTimeCanEstimate() {
		return false;
	}

	@Override
	public int processNext() throws Exception {
		doIt();
		return PROGRESS_FINSHED;
	}

	public List<String> getOutputList() {

		return outputList;
	}

	@Override
	public void doIt() throws Exception {
		Objects.requireNonNull(searchKeys);
		outputList.clear();
		// 创建一个固定大小为3的队列
		String headerLine = null;


		int searchedLineCount = 0;

		bms = Lists.newArrayList();
		for (String boyerMoore : searchKeys) {
			bms.add(new BoyerMoore(boyerMoore));
		}

		searchWork(headerLine, searchedLineCount);

	}

	protected void searchWork(String headerLine, int searchedLineCount) throws IOException {
		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(inputFilePath);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {

				if (headerLine == null) {
					headerLine = readLine;
				}

				if (matchLine(readLine)) {
					searchedLineCount++;
					outputList.add(readLine);

					int tempOutputIndex = followingLineNumber;
					// 注意这个顺序
					while (tempOutputIndex > 0 && (readLine = br.readLine()) != null) {
						outputList.add(readLine);
						tempOutputIndex--;
					}

					if (searchedLineCount == searchedResultsLimitation) {
						break;
					}
				}

			}
		}

		if (exportHeader) {
			outputList.add(0, headerLine);
		}
	}

	protected boolean matchLine(String readLine) {
		for (BoyerMoore boyerMoore : bms) {
			boolean search = boyerMoore.search(readLine) > -1;
			if (search) {
				return true;
			}

		}
		return false;
	}

}
