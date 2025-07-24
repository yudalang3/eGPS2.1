package module.correlation4wnt;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;

import tsv.io.TSVReader;
import module.correlation4wnt.io.ParaModel;
import graphic.engine.DefaultLinerColorMapper;
import graphic.engine.colors.EGPSColors;

public class DataModel {

	// data need to import by users
	TreeBasedTable<String, String, Double> table = TreeBasedTable.create();

	List<HashBasedTable<String, String, Double>> connectingLineRelationships = Lists.newArrayList();
	List<Triple<String, String, Color>> listOfRightAnnotation;

	List<String> names = new ArrayList<>();

	DefaultLinerColorMapper mapper;

	private double max;
	private double min;

	public DataModel(String tsvPath, String jsonPara) throws IOException {
		ParaModel jsonData = JSON.parseObject(jsonPara, ParaModel.class);
		parsing(tsvPath, jsonData);
	}

	public DataModel(String tsvPath, ParaModel jsonData) throws IOException {
		parsing(tsvPath, jsonData);
	}

	public void parsing(String tsvPath, ParaModel jsonData) throws IOException {
		final String nameColKey = jsonData.getExpMatrixCol().get(0);
		List<String> cat1Cols = jsonData.getCat1Col();
		List<String> cat2Cols = jsonData.getCat2Col();

		Map<String, List<String>> colNames2dataMap = TSVReader.readAsKey2ListMap(tsvPath);
		names = colNames2dataMap.remove(nameColKey);
		int recordSize = names.size();

		{
			// interconnecting lines
			List<String> list1 = colNames2dataMap.remove(cat2Cols.get(1));
			List<String> list2 = names;
			List<String> list3 = colNames2dataMap.remove(cat2Cols.get(2));

			HashBasedTable<String, String, Double> table1 = HashBasedTable.create();

			IntStream.range(0, list1.size()).forEach(i -> {
				String item1 = list1.get(i);
				String item2 = list2.get(i);
				// 处理 item1 和 item2
				String string = list3.get(i);
				table1.put(item1, item2, new Double(string));
			});
			connectingLineRelationships.add(table1);
		}

		listOfRightAnnotation = Lists.newArrayList();


		List<String> categories = colNames2dataMap.remove(cat1Cols.get(1));
		{
			HashMap<String, Color> mapItemsColor = EGPSColors.getMapItemsColor(categories);

			if (categories.size() == 3) {
				List<String> temp = colNames2dataMap.remove(cat1Cols.get(2));
				int i = 0;
				for (String string : temp) {
					String name = names.get(i);
					Color color = EGPSColors.parseColor(string);
					mapItemsColor.put(name, color);
					i++;
				}

			}
			for (int i = 0; i < recordSize; i++) {
				String name = names.get(i);
				String category = categories.get(i);
				listOfRightAnnotation.add(Triple.of(name, category, mapItemsColor.get(category)));
			}

		}

		Map<String, List<Double>> rowName2valuesMap = Maps.newHashMap();
		for (int j = 0; j < recordSize; j++) {
			String name = names.get(j);

			ArrayList<Double> newArrayList = Lists.newArrayList();
			for (Entry<String, List<String>> entry : colNames2dataMap.entrySet()) {
				List<String> values = entry.getValue();
				String string = values.get(j);
				newArrayList.add(new Double(string));
			}
			rowName2valuesMap.put(name, newArrayList);
		}

		for (int i = 0; i < recordSize; i++) {
			String name1 = names.get(i);
			for (int j = 0; j < i; j++) {
				String name2 = names.get(j);
				List<Double> list1 = rowName2valuesMap.get(name1);
				List<Double> list2 = rowName2valuesMap.get(name2);
				double correlation = calculateCorrelation(list1, list2);
				table.put(name1, name2, correlation);
			}
		}

		double min = 1000000;
		double max = -10000;
		for (Cell<String, String, Double> cellSet : table.cellSet()) {
			double value = cellSet.getValue();
			if (value < min) {
				min = value;
			}
			if (value > max) {
				max = value;
			}
		}

		mapper = new DefaultLinerColorMapper(-1, 1);
		this.min = min;
		this.max = max;

	}

	public DataModel() {

	}

	private double calculateCorrelation(List<Double> list1, List<Double> list2) {
		if (list1 == null || list2 == null || list1.size() != list2.size() || list1.isEmpty()) {
			throw new IllegalArgumentException("列表不能为空且长度必须相等");
		}

		// 将 List 转换为 double[]
		double[] array1 = list1.stream().mapToDouble(Double::doubleValue).toArray();
		double[] array2 = list2.stream().mapToDouble(Double::doubleValue).toArray();

		// 使用 Apache Commons Math 计算相关系数
		PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
		return pearsonsCorrelation.correlation(array1, array2);
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\yudal\\Desktop\\temp.txt";
		String jsonString = "{\"expMatrixCol\":[\"name\"],\"cat1Col\":[\"name\",\"cat1\"],\"cat2Col\":[\"name\",\"cat2\",\"value\"]}";

		DataModel dataModel = new DataModel(path, jsonString);
		System.out.println(dataModel);
	}
}
