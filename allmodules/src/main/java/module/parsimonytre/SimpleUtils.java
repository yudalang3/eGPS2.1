package module.parsimonytre;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.base.Stopwatch;

import tsv.io.TSVReader;

public class SimpleUtils {

	public static void main(String[] args) throws IOException {
		// 创建一个 Stopwatch 实例
		Stopwatch stopwatch = Stopwatch.createStarted();

		getTiedNumbers();

		// 停止 Stopwatch 并获取经过的时间
		stopwatch.stop();
		// 打印经过的时间
		System.out.println("执行耗时: " + stopwatch.elapsed(TimeUnit.SECONDS) + " 秒");

	}

	private static void getTiedNumbers() throws IOException {
//		String path = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\phmmer_search_324Animal\\2.annotateGeneWithSpecies\\NEE2013_all_species\\Inferring Internal Node States with Parsimony\\out.states.file.tsv";
		String path = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Main_Vertebrate_WntComp_formation_enrichment\\stage2\\before20250106_run\\2.annotateGeneWithSpecies\\TreeFromTimeTree\\Eukaryote\\out_tree_with_node_anno_results.tsv";
		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(path);
		List<String> list = asKey2ListMap.get("rootParsimonyScore");
		int totalCount = 0;
		for (String string : list) {
			List<Integer> listOfInteger = convertStringToList(string);
			boolean isTie = false;
			if (listOfInteger.size() < 2) {
				continue;
			} else {
				isTie = hasMultipleMinValues(listOfInteger);
			}

			if (isTie) {
				System.out.println(string);
				totalCount++;
			}
		}

		System.out.println("All values is: " + totalCount);

	}

	private static boolean hasMultipleMinValues(List<Integer> listOfInteger) {
		if (listOfInteger == null || listOfInteger.isEmpty()) {
			// 如果列表为空或为null，返回false
			return false;
		}

		// 找到列表中的最小值
		OptionalInt minOptional = listOfInteger.stream().filter(num -> num != null).mapToInt(Integer::intValue).min();

		if (!minOptional.isPresent()) {
			// 如果列表中没有有效的整数，返回false
			return false;
		}

		int minValue = minOptional.getAsInt();
		// 统计最小值出现的次数
		long occurrences = listOfInteger.stream().filter(num -> num != null && num.equals(minValue)).count();

		// 如果最小值出现的次数大于1，则存在多个相同的最小值
		return occurrences > 1;
	}

	private static List<Integer> convertStringToList(String str) {
        // 去除方括号
		String trimmedStr = str.replaceAll("[\\[\\]\"]", "");
        // 分割字符串并转换为 Integer 流，最后收集到 List<Integer>
        return Arrays.stream(trimmedStr.split(",\\s*"))
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

}
