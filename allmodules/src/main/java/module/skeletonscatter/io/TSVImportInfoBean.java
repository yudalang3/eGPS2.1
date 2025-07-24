package module.skeletonscatter.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import utils.string.EGPSStringUtil;

public class TSVImportInfoBean {

	String filePath = "";

	boolean hasHeader = true;

	/**
	 * y axis column, 0-based
	 */
	int targetColumn = 1;
	
	Optional<Integer> xAxisIndex = Optional.empty();

	public TSVImportInfoBean() {
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public int getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(int targetColumn) {
		this.targetColumn = targetColumn;
	}

	public void setXAxisIndex(int targetColumn) {
		this.xAxisIndex = Optional.of(targetColumn);
	}

	public Optional<Integer> getxAxisIndex() {
		return xAxisIndex;
	}

	public Pair<List<Double>, List<Double>> loadData() {

		List<String> readLines = null;
		try {
			readLines = FileUtils.readLines(new File(getFilePath()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int lineNumber = readLines.size();
		int startIndex = 0;
		if (this.isHasHeader()) {
			startIndex = 1;
		}

		List<Double> data = new ArrayList<>(lineNumber);

		int columnIndex = getTargetColumn() - 1;
		for (int i = startIndex; i < lineNumber; i++) {
			String string = readLines.get(i);
			String[] split = EGPSStringUtil.split(string, '\t');
			String doubleStr = split[columnIndex];
			data.add(Double.parseDouble(doubleStr));
		}

		List<Double> xAxisData = new ArrayList<>();
		if (xAxisIndex.isPresent()) {
			int xColumnIndex = xAxisIndex.get() - 1;
			for (int i = startIndex; i < lineNumber; i++) {
				String string = readLines.get(i);
				String[] split = EGPSStringUtil.split(string, '\t');
				String doubleStr = split[xColumnIndex];
				xAxisData.add(Double.parseDouble(doubleStr));
			}
		} else {
			int size = data.size();
			for (int i = 1; i <= size; i++) {
				xAxisData.add((double) i);
			}
		}
		return Pair.of(xAxisData, data);
	}

}
