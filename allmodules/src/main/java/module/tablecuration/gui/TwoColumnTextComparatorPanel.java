/**
 * 
 */
package module.tablecuration.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import utils.string.EGPSStringUtil;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class TwoColumnTextComparatorPanel extends DIYToolSubTabModuleFace {

	public TwoColumnTextComparatorPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.table1.file", "",
				"Input data file, usually the tsv file. As the reference. Necessary");
		mapProducer.addKeyValueEntryBean("table1.target.column", "", "Target column for key.");
		mapProducer.addKeyValueEntryBean("input.table2.file", "",
				"Input data file, usually the tsv file. As the search query. Need to be the bigger one. Necessary");
		mapProducer.addKeyValueEntryBean("table2.target.column", "", "Target column for key.");
		mapProducer.addKeyValueEntryBean("compare.algorithm", "LevenshteinDistance",
				"Currently, supports two algorithm: JaccardSimilarity or LevenshteinDistance");
		mapProducer.addKeyValueEntryBean("output.file.path", "", "Output file path, tsv usually, Necessary.");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputFile1 = quickParseString("input.table1.file");
		String table1TargetCol = quickParseString("table1.target.column");
		String inputFile2 = quickParseString("input.table2.file");
		String table2TargetCol = quickParseString("table2.target.column");
		String outputFilePath = quickParseString("output.file.path");

		String compareAlgo = quickParseString("compare.algorithm");
		boolean isCompareWithLevenshteinDistance = true;
		if (Objects.equal(compareAlgo, "LevenshteinDistance")) {

		} else if (Objects.equal(compareAlgo, "JaccardSimilarity")) {
			isCompareWithLevenshteinDistance = false;
		} else {
			throw new InputMismatchException(
					"Sorry, your input compare algorithm is not supported, ".concat(compareAlgo));
		}

		Stopwatch stopwatch = Stopwatch.createStarted();
		List<String> outputs = perform(inputFile1, table1TargetCol, inputFile2, table2TargetCol,
				isCompareWithLevenshteinDistance,
				outputFilePath);

		stopwatch.stop();
		setText4Console(outputs);
		long millis = stopwatch.elapsed().toMillis();

		appendText2Console("Take time of  " + (millis) + " milliseconds to execute.");
	}

	/**
	 * 执行文件比较操作
	 *
	 * 该方法主要用于比较两个输入文件中的指定列，并生成一个输出文件，其中包含匹配的结果
	 * 它可以根据设定的是否使用Levenshtein距离来比较字符串相似度
	 *
	 * @param inputFile1 第一个输入文件路径
	 * @param table1TargetCol 第一个文件中的目标列标识
	 * @param inputFile2 第二个输入文件路径
	 * @param table2TargetCol 第二个文件中的目标列标识
	 * @param isCompareWithLevenshteinDistance 是否使用Levenshtein距离进行比较
	 * @param outputFilePath 输出文件路径
	 * @return 返回一个包含执行结果的字符串列表
	 * @throws IOException 如果在读写文件过程中发生错误
	 */
	private List<String> perform(String inputFile1, String table1TargetCol, String inputFile2, String table2TargetCol,
			boolean isCompareWithLevenshteinDistance, String outputFilePath) throws IOException {
		List<String> ret = new LinkedList<>();

		Map<String, List<String>> table1 = TSVReader.readAsKey2ListMap(inputFile1);
		Map<String, List<String>> table2 = TSVReader.readAsKey2ListMap(inputFile2);

		List<String> tab1Column = table1.get(table1TargetCol);
		List<String> tab2Column = table2.get(table2TargetCol);

		if (tab2Column.size() < tab1Column.size()) {
			throw new IllegalArgumentException(
					"The column of table 2 is less than column of table1, Please change the order.");
		}

		ArrayList<String> arrayList = new ArrayList<>(tab2Column);

		List<Pair<String, String>> loopQueryResults = Lists.newArrayList();
		while (true) {
			if (tab2Column.isEmpty() || tab1Column.isEmpty()) {
				break;
			}

			Pair<String, String> entry = oneEntrySearch(tab1Column, tab2Column, isCompareWithLevenshteinDistance);
			loopQueryResults.add(entry);
		}

		List<String> outputs = new LinkedList<>();
		
		String header = EGPSStringUtil.format("table1.{}\ttable2.{}", table1TargetCol, table2TargetCol);

		outputs.add(header);
		for (String string : arrayList) {
			String pairedOne = null;
			for (Pair<String, String> pair : loopQueryResults) {
				if (Objects.equal(pair.getRight(), string)) {
					pairedOne = pair.getLeft();
					break;
				}
			}

			if (pairedOne == null) {
				outputs.add("\t" + string);
			} else {
				outputs.add(pairedOne + "\t" + string);
			}
		}

		FileUtils.writeLines(new File(outputFilePath), outputs);

		if (tab1Column.isEmpty() && tab2Column.isEmpty()) {
			ret.add("Finish the one-to-one entry match");
		} else if (tab1Column.isEmpty()) {
			ret.add("Table 1 is matched to the table 2.");
		} else {
			ret.add("Error happened: table1 is not empty.");
		}

		return ret;
	}

	private Pair<String, String> oneEntrySearch(List<String> tab1Column, List<String> tab2Column,
			boolean isCompareWithLevenshteinDistance) {
		Pair<String, String> ret = null;
		double min = 10000000;
		for (String str1 : tab1Column) {
			for (String str2 : tab2Column) {
				double diff = 0;
				if (isCompareWithLevenshteinDistance) {
					diff = computeLevenshteinDistance(str1, str2);
				} else {
					diff = computeJaccardSimilarity(str1, str2);
				}
				if (diff < min) {
					min = diff;
					ret = Pair.of(str1, str2);
				}
			}
		}

		tab1Column.remove(ret.getKey());
		tab2Column.remove(ret.getValue());

		return ret;
	}

	public int computeLevenshteinDistance(String str1, String str2) {
		int[][] dp = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++) {
			for (int j = 0; j <= str2.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
					dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
				}
			}
		}
		return dp[str1.length()][str2.length()];
	}

	public double computeJaccardSimilarity(String str1, String str2) {
		Set<Character> set1 = new HashSet<>();
		Set<Character> set2 = new HashSet<>();

		for (char c : str1.toCharArray()) {
			set1.add(c);
		}

		for (char c : str2.toCharArray()) {
			set2.add(c);
		}

		Set<Character> intersection = new HashSet<>(set1);
		intersection.retainAll(set2); // 交集

		Set<Character> union = new HashSet<>(set1);
		union.addAll(set2); // 并集

		return (double) intersection.size() / union.size();
	}

}
