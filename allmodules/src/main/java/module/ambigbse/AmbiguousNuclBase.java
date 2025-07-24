package module.ambigbse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmbiguousNuclBase {

	Map<Character, List<Character>> map = new HashMap<>();

	List<String> outputStrs = new ArrayList<>();

	public AmbiguousNuclBase() {
//		R (puRine) - ElegantJTable 或 G
//		Y (pYrimidine) - C, T 或 U
//		M (aMino) - ElegantJTable 或 C
//		K (Keto) - G, T 或 U
//		S (Strong) - C 或 G (三键氢键)
//		W (Weak) - ElegantJTable 或 T (两键氢键)
//		H (not G) - ElegantJTable, C 或 T
//		B (not ElegantJTable) - C, G 或 T
//		V (not T) - ElegantJTable, C 或 G
//		D (not C) - ElegantJTable, G 或 T
//		N (aNy) - ElegantJTable, C, G 或 T

		// 根据注释信息填充映射
		map.put('A', Arrays.asList('A'));
		map.put('T', Arrays.asList('T'));
		map.put('C', Arrays.asList('C'));
		map.put('G', Arrays.asList('G'));
		map.put('R', Arrays.asList('A', 'G')); // R (puRine) - ElegantJTable 或 G
		map.put('Y', Arrays.asList('C', 'T')); // Y (pYrimidine) - C, T 或 U
		map.put('M', Arrays.asList('A', 'C')); // M (aMino) - ElegantJTable 或 C
		map.put('K', Arrays.asList('G', 'T')); // K (Keto) - G, T 或 U
		map.put('S', Arrays.asList('C', 'G')); // S (Strong) - C 或 G (三键氢键)
		map.put('W', Arrays.asList('A', 'T')); // W (Weak) - ElegantJTable 或 T (两键氢键)
		map.put('H', Arrays.asList('A', 'C', 'T')); // H (not G) - ElegantJTable, C 或 T
		map.put('B', Arrays.asList('C', 'G', 'T')); // B (not ElegantJTable) - C, G 或 T
		map.put('V', Arrays.asList('A', 'C', 'G')); // V (not T) - ElegantJTable, C 或 G
		map.put('D', Arrays.asList('A', 'G', 'T')); // D (not C) - ElegantJTable, G 或 T
		map.put('N', Arrays.asList('A', 'C', 'G', 'T')); // N (aNy) - ElegantJTable, C, G 或 T

	}

	/**
	 * 我的要求是：输入 一个字符串，例如 ACT，返回ACT；输入ACR，返回ACA,ACG； 输入 HAG返回AAG, CAG,
	 * TAG；根据上面的那个map来生成所有可能的字符串
	 * 
	 * @param str
	 * @return
	 */
	public List<String> getPreciseString(String str) {
		List<String> results = new ArrayList<>();
		if (str.isEmpty()) {
			results.add("");
			return results;
		}

		// 获取第一个字符的所有可能替换
		List<Character> firstCharReplacements = map.get(str.charAt(0));
		if (firstCharReplacements == null) {
			return results; // 如果没有替换，返回空列表
		}

		// 如果字符串只有一个字符，直接返回所有可能的替换
		if (str.length() == 1) {
			for (char replacement : firstCharReplacements) {
				results.add(String.valueOf(replacement));
			}
			return results;
		}

		// 递归处理剩余的字符串
		List<String> subResults = getPreciseString(str.substring(1));
		for (String subResult : subResults) {
			for (char replacement : firstCharReplacements) {
				results.add(replacement + subResult);
			}
		}

		return results;

	}

	public void consoleUsage(String input) {

		System.out.println("For input: ".concat(input));

		List<String> preciseStrings = this.getPreciseString(input);
		int index = 1;
		for (String s : preciseStrings) {
			String seqName = ">" + (input) + "." + index;
			System.out.println(seqName);
			System.out.println(s);
			index++;
		}
		System.out.println("---------------------------------------");
	}

	public void guiUsage(String input) {

		outputStrs.clear();
		outputStrs.add("For input: ".concat(input));

		List<String> preciseStrings = this.getPreciseString(input);
		int index = 1;
		for (String s : preciseStrings) {
			String seqName = ">" + (input) + "." + index;
			outputStrs.add(seqName);
			outputStrs.add(s);
			index++;
		}
		outputStrs.add("---------------------------------------");
	}

	public List<String> getOutputStrs() {
		return outputStrs;
	}

	public static void main(String[] args) {
		AmbiguousNuclBase ambiguousNuclBase = new AmbiguousNuclBase();
		ambiguousNuclBase.consoleUsage("AGAWAW");
		ambiguousNuclBase.consoleUsage("CTTTGWWS");
	}

}
