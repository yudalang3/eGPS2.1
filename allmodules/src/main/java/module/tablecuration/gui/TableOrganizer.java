package module.tablecuration.gui;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.*;
import tsv.io.IntTable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import utils.string.EGPSStringUtil;
import org.apache.commons.lang3.mutable.MutableInt;

public class TableOrganizer {

	public IntTable countRowEntries(String inputFile, List<String> dim1, String dim2) throws IOException {
		Table<String, String, MutableInt> counter = TreeBasedTable.create();

		try (LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile), "UTF-8")) {
			// 解析表头
			Map<String, Integer> headerMap = parseHeader(lineIterator.next(), dim1, dim2);
			final int dim2Index = headerMap.get(dim2);
			List<Integer> dim1Indexes = dim1.stream()
					.map(headerMap::get)
					.collect(Collectors.toList());

			// 统计行数据
			StringBuilder sBuilder = new StringBuilder();
			while (lineIterator.hasNext()) {
				String[] lineContent = EGPSStringUtil.split(lineIterator.next(), '\t');
				sBuilder.setLength(0);
				for (int i = 0; i < dim1.size(); i++) {
					if (i > 0) {
						sBuilder.append("\t");
					}
					sBuilder.append(lineContent[dim1Indexes.get(i)]);
				}
				String key1 = sBuilder.toString();
				String key2 = lineContent[dim2Index];

				MutableInt mutableInt = counter.get(key1, key2);
				if (mutableInt == null) {
					counter.put(key1, key2, new MutableInt(1));
				} else {
					mutableInt.increment();
				}
			}
		}

		IntTable lists = convertTableToList(counter, dim1);
		return lists;
	}

	private Map<String, Integer> parseHeader(String headerLine, List<String> dim1, String dim2) {
		String[] headers = EGPSStringUtil.split(headerLine, '\t');
		Map<String, Integer> headerMap = Maps.newHashMap();
		for (int i = 0; i < headers.length; i++) {
			headerMap.put(headers[i], i);
		}

		// 检查 dim2 是否存在
		if (!headerMap.containsKey(dim2)) {
			throw new IllegalArgumentException("The dim2 key does not exist in the map");
		}

		// 检查 dim1 中的每个键是否存在
		for (String key : dim1) {
			if (!headerMap.containsKey(key)) {
				throw new IllegalArgumentException("The dim1 key does not exist in the map: " + key);
			}
		}

		return headerMap;
	}

	private IntTable convertTableToList(Table<String, String, MutableInt> table, List<String> dim1ColumnNames) {
		// 获取所有的行键和列键
		Set<String> rowKeys = table.rowKeySet();
		Set<String> columnKeys = table.columnKeySet();

		List<String> rowNames = Lists.newArrayList(rowKeys);
		List<String> colNames = Lists.newArrayList(columnKeys);

		String colNameOfRowNames = String.join("\t", dim1ColumnNames);
		int colSize = colNames.size();
		int[][] intTable = new int[rowNames.size()][colSize];

		int rowIndex = 0;
		for (String rowKey : rowNames) {
			int[] oneRow = new int[colSize];
			for (int i = 0; i < colSize; i++) {
				MutableInt mutableInt = table.get(rowKey, colNames.get(i));
				if (mutableInt == null){
					oneRow[i] = 0;
				}else {
					oneRow[i] = mutableInt.intValue();
				}
			}
			intTable[rowIndex] =oneRow;
			rowIndex ++;
		}

		IntTable ret = new IntTable(intTable, rowNames, colNames);
		ret.setColNameOfRowNames(colNameOfRowNames);

		return ret;
	}

	// 计数函数，自动更新 Map 中的计数
	/**
	 * 我有一个表格类型的数据，存储的结构是 一个 Map<String, List<String>> asKey2ListMap
	 * Key是列名，Value是一个列表，Value都是等长的。
	 * 
	 * 请你帮我写这个方法 extractTargetRecords 我的目的是根据
	 * targetCol中把entries，对asKey2ListMap进行过滤。保留entries中的记录。 返回值仍然是 Map<String,
	 * List<String>> 类型。
	 * 
	 * @param asKey2ListMap 存储表格数据的Map，Key是列名，Value是对应列的所有值。
	 * @param targetCol     目标列，用于根据entries筛选记录。
	 * @param entries       要保留的目标列中的值。
	 * @return 过滤后的Map，保留符合条件的记录。
	 * 
	 */
	public Map<String, List<String>> extractTargetRecords(Map<String, List<String>> asKey2ListMap, String targetCol,
			List<String> entries) {
		// 检查targetCol是否存在于asKey2ListMap
		if (!asKey2ListMap.containsKey(targetCol)) {
			throw new IllegalArgumentException("Target column not found in the map.");
		}

		// 获取目标列的数据
		List<String> targetColumnData = asKey2ListMap.get(targetCol);

		// 找到符合entries条件的行索引
		List<Integer> theEntryIndexIntargetColumn = new ArrayList<>();
		int entrySize = entries.size();

		for (int i = 0; i < entrySize; i++) {
			String entry = entries.get(i);

			int indexOf = targetColumnData.indexOf(entry);
			if (indexOf > -1) {
				theEntryIndexIntargetColumn.add(indexOf);
			}
		}

		// 创建一个新的Map，存储过滤后的数据
		Map<String, List<String>> filteredMap = new LinkedHashMap<>();

		// 遍历asKey2ListMap中的每一列，并根据targetIndices保留对应的行
		for (Map.Entry<String, List<String>> entry : asKey2ListMap.entrySet()) {
			String colName = entry.getKey();
			List<String> colData = entry.getValue();

			// 为当前列创建新的列表，存储符合条件的行
			List<String> filteredColData = new ArrayList<>();
			for (Integer index : theEntryIndexIntargetColumn) {
				filteredColData.add(colData.get(index));
			}

			// 将过滤后的列数据放入新的Map
			filteredMap.put(colName, filteredColData);
		}

		return filteredMap;
	}

	/**
	 * 我有一个表格类型的数据，存储的结构是 一个 Map<String, List<String>> asKey2ListMap
	 * Key是列名，Value是一个列表，Value都是等长的。
	 * 
	 * 请你帮我写这个方法 removeDuplicatedEnties(Map<String, List<String>> asKey2ListMap,
	 * List<String> targetCols) 我的目的是根据 targetCols，去除重复的行，保留第一个行。
	 * 
	 * @param asKey2ListMap
	 * @return
	 */
	public List<List<String>> removeDuplicatedEnties(Map<String, List<String>> asKey2ListMap, List<String> targetCols) {
		if (asKey2ListMap == null || asKey2ListMap.isEmpty() || targetCols == null || targetCols.isEmpty()) {
			return Collections.emptyList();
		}

		// 获取所有列名
		List<String> allColumns = new ArrayList<>(asKey2ListMap.keySet());
		// 假设所有列长度一致，获取任意一列的长度
		int numRows = asKey2ListMap.get(allColumns.get(0)).size();

		// Set 用于存储已经遇到的行标识符
		Set<String> seenRows = new HashSet<>();
		// List 用于存储保留的行索引
		List<Integer> rowsToKeep = new ArrayList<>();

		// 遍历每一行
		for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
			StringBuilder rowIdentifier = new StringBuilder();
			// 构建当前行的标识符
			for (String col : targetCols) {
				rowIdentifier.append(asKey2ListMap.get(col).get(rowIndex)).append(",");
			}
			String rowKey = rowIdentifier.toString();

			// 检查标识符是否已经存在
			if (!seenRows.contains(rowKey)) {
				seenRows.add(rowKey);
				rowsToKeep.add(rowIndex);
			}
		}

		// 创建新的 List<List<String>>
		List<List<String>> result = Lists.newLinkedList();
		result.add(allColumns);

		for (int rowIndex : rowsToKeep) {
			List<String> row = new ArrayList<>();
			for (String col : allColumns) {
				row.add(asKey2ListMap.get(col).get(rowIndex));
			}
			result.add(row);
		}

		return result;
	}

	public void writeToFile(List<List<String>> data, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (List<String> row : data) {
				String line = String.join("\t", row);
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
