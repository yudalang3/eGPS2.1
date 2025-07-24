package module.filegreper;
import java.util.Arrays;

public class BoyerMoore {

	private static final int ALPHABET_SIZE = 256; // ASCII字符集大小
	private int[] badChar;
	private int[] goodSuffix;
	private String pattern;

	// 构造函数，提前处理pattern
	public BoyerMoore(String pattern) {
		this.pattern = pattern;
		this.badChar = badCharacterHeuristic(pattern);
		this.goodSuffix = goodSuffixHeuristic(pattern);
	}

	// 生成坏字符表
	private int[] badCharacterHeuristic(String pattern) {
		int[] badChar = new int[ALPHABET_SIZE];
		Arrays.fill(badChar, -1);

		for (int i = 0; i < pattern.length(); i++) {
			badChar[pattern.charAt(i) % ALPHABET_SIZE] = i; // 处理字符索引超出范围的情况
		}

		return badChar;
    }

	// 生成好后缀表
	private int[] goodSuffixHeuristic(String pattern) {
		int m = pattern.length();
		int[] goodSuffix = new int[m + 1];
		int[] borderPosition = new int[m + 1];

		int i = m;
		int j = m + 1;
		borderPosition[i] = j;

		while (i > 0) {
			while (j <= m && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
				if (goodSuffix[j] == 0) {
					goodSuffix[j] = j - i;
                }
				j = borderPosition[j];
            }
			i--;
			j--;
			borderPosition[i] = j;
		}

		j = borderPosition[0];
		for (i = 0; i <= m; i++) {
			if (goodSuffix[i] == 0) {
				goodSuffix[i] = j;
			}
			if (i == j) {
				j = borderPosition[j];
			}
        }

		return goodSuffix;
    }

	// Boyer-Moore匹配算法，返回第一个匹配的index，未匹配返回-1
	public int search(String text) {
		int n = text.length();
		int m = pattern.length();

		if (m == 0) {
			return 0; // 空字符串匹配任意位置
		}

		int s = 0; // 起始位置

		while (s <= n - m) {
			int j = m - 1;

			// 从后往前匹配
			while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
				j--;
			}

			if (j < 0) {
				return s; // 匹配成功，返回起始位置
			} else {
				// 处理 badChar 访问时的数组越界问题
				char currentChar = text.charAt(s + j);
				int badCharShift = badChar[currentChar % ALPHABET_SIZE]; // 使用模运算避免越界
				s += Math.max(goodSuffix[j + 1], j - badCharShift);
			}
		}

		return -1; // 未找到匹配，返回-1
	}

    public static void main(String[] args) {
		String pattern = "ABC";
		BoyerMoore bm = new BoyerMoore(pattern);
		String text = "ABAAABCD";
		int result = bm.search(text);

		if (result != -1) {
			System.out.println("Pattern found at index " + result);
		} else {
			System.out.println("Pattern not found");
		}
    }
}
