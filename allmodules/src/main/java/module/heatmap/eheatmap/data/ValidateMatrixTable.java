package module.heatmap.eheatmap.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import egps2.utils.common.math.MatrixElementUtil;
import egps2.utils.common.tablelike.LineSparator;
import egps2.utils.common.tablelike.MatrixTableContentBean;

public class ValidateMatrixTable {

	private MatrixTableContentBean bean;

	public void getFileFormat(File inputFile) throws IOException {
		List<String> readLines = FileUtils.readLines(inputFile,StandardCharsets.UTF_8);

		getFileFormat(readLines);
	}

	public void getFileFormat(List<String> inputLines) {
		
		List<String> readLines = Lists.newArrayList();
		for (String string : inputLines) {
			if (string.isEmpty()  || string.charAt(0) == '#') {
				continue;
			}
			readLines.add(string);
		}
		
		int regExpIndex = 0, tempMaxNumOfEles = Integer.MIN_VALUE;
		String regExp = null;
		boolean hasRowName = true, hasColumnName = false;
		String[] columnNames = null;

		String[] elementsAfterSuitableSplit = null;

		String firstLine = readLines.get(0);
		// 用空格/，/\t/ | /分别去split，得到数量最多的即为分隔符
		for (LineSparator lFeeder : LineSparator.values()) {
			String regularExp = lFeeder.getRegularExp();
			String[] splits = firstLine.split(regularExp);
			if (splits.length > tempMaxNumOfEles) {
				tempMaxNumOfEles = splits.length;
				regExpIndex = lFeeder.getIndexInCombox();
				elementsAfterSuitableSplit = splits;
				regExp = regularExp;
			}
		}

		int numOfTotalRows = readLines.size();

		int maxNumOfSplitedColumns = elementsAfterSuitableSplit.length;

		String[][] tableElements = null;

		if (maxNumOfSplitedColumns < 2) {
			throw new InputMismatchException("The number of line elements less than 2, please check your format");
		}

		// 若分割之后的第2，3，。。。到最后的元素都是double，则认为没有header
		for (int i = 1; i < maxNumOfSplitedColumns; i++) {
			boolean isDouble = MatrixElementUtil.isDoubleCompiledRegex(elementsAfterSuitableSplit[i]);
			if (!isDouble) {
				hasColumnName = true;
			}
		}

		// 判断有无rowName，应该是第二行，先得到第二行第一个元素
		String[] split = readLines.get(1).split(regExp);
		hasRowName = !MatrixElementUtil.isDoubleCompiledRegex(split[0]);

		int numOfNAs = 0;
		int maxNumOfRowsToShow = 0;
		if (hasColumnName) {
			columnNames = elementsAfterSuitableSplit;

			maxNumOfRowsToShow = numOfTotalRows - 1;
			tableElements = new String[maxNumOfRowsToShow][];

			for (int j = 1; j < numOfTotalRows; j++) {
				String[] splits = readLines.get(j).split(regExp);
				if (splits.length != maxNumOfSplitedColumns) {
					throw new InputMismatchException("The number of line elements not consist with header line.");
				}
				tableElements[j - 1] = splits;
				for (String string : splits) {
					if ("NA".equalsIgnoreCase(string)) {
						numOfNAs++;
					}
				}
			}

		} else {
			// columnNames = Arrays.stream(elementsAfterSuitableSplit).ma
			Object[] array = IntStream.range(0, maxNumOfSplitedColumns).mapToObj(i -> "ColName " + i).toArray();

			columnNames = new String[array.length];
			for (int i = 0; i < array.length; i++) {
				columnNames[i] = array[i].toString();
			}

			maxNumOfRowsToShow = numOfTotalRows;
			tableElements = new String[maxNumOfRowsToShow][];

			for (int j = 0; j < numOfTotalRows; j++) {
				String[] splits = readLines.get(j).split(regExp);
				if (splits.length != maxNumOfSplitedColumns) {
					throw new InputMismatchException("The number of line elements not consist with header line.");
				}
				tableElements[j] = splits;
				for (String string : splits) {
					if ("NA".equalsIgnoreCase(string)) {
						numOfNAs++;
					}
				}
			}

		}

		// System.out.println(getClass());
		// System.out.println(hasRowName+"\t"+hasColumnName+"\t"+regExp
		// +"\t"+regExpIndex);
		bean = new MatrixTableContentBean();
		bean.setColumnNames(columnNames);
		bean.setHasColumn(hasColumnName);
		bean.setHasRows(hasRowName);
		bean.setTableElements(tableElements);
		bean.setNumOfNAs(numOfNAs);
		bean.setNumOfRows(readLines.size());
		bean.setNumOfColumns(tempMaxNumOfEles);
		bean.setLineSeparetorIndex(regExpIndex);
		bean.setHasRows(hasRowName);
		bean.setHasColumn(hasColumnName);

		return;
	}

	public Optional<MatrixTableContentBean> getBean() {
		return Optional.ofNullable(bean);
	}

	public boolean detectFormat(List<String> strings) {
		// 首先验证文件是否大于 2 行
		if (strings.size() < 2) {
			return false;
		}

		int tempMaxNumOfEles = Integer.MIN_VALUE;
		String regExp = null;
		String firstLine = strings.get(0);
		// 用空格/，/\t/ | /分别去split，得到数量最多的即为分隔符
		for (LineSparator lFeeder : LineSparator.values()) {
			String regularExp = lFeeder.getRegularExp();
			String[] splits = firstLine.split(regularExp);
			if (splits.length > tempMaxNumOfEles) {
				tempMaxNumOfEles = splits.length;
				regExp = regularExp;
			}
		}

		// 第二行被分割后，要有相同的数量！
		String secondLine = strings.get(1);
		String[] splits = secondLine.split(regExp);
		int newLen = splits.length;

		if (tempMaxNumOfEles != newLen) {
			return false;
		}

		// 若分割之后的第2，3，。。。到最后的元素都是double，则认为没有header
		for (int i = 1; i < newLen; i++) {
			boolean isDouble = MatrixElementUtil.isDoubleCompiledRegex(splits[i]);
			if (!isDouble) {
				return false;
			}
		}

		return true;
	}

}
