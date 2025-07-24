package module.brutemapping.algo;

/**
 * 我有如下需求：我有一条很长的字符串 String，叫做subject。还有一条相对比较短的序列加做query。
 * 我的目的是找到subject上和query完全匹配的序列位置，例如 100. 请你帮我写一个JAVA程序，要求速度快。
 * 
 * 解答：
 * 为了满足您的需求，可以考虑使用KMP算法(Knuth-Moris-Pratt算法)，这是一种改进的字符串搜索算法，可以在较短的时间内高效地完成子串搜索任务。
 */
public class KMPMatcher {

	private final String subject;

	public KMPMatcher(String subject) {
		this.subject = subject;
	}

    // 计算临时数组的部分匹配表（失配表）
	private int[] computeTemporaryArray(String pattern) {
        int[] lps = new int[pattern.length()];
        int index = 0; // index for pattern[]

        for (int i = 1; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(index)) {
                lps[i++] = ++index;
            } else {
                if (index != 0) index = lps[index - 1];
                else lps[i++] = index;
            }
        }
        return lps;
    }

    // KMP算法核心实现
	public int kmpSearch(String query, int startIndex4subject) {
		int length = subject.length();
		int[] lps = computeTemporaryArray(query);

		int j = 0; // index for query[]
		for (int i = startIndex4subject; i < length;) {
            if (subject.charAt(i) == query.charAt(j)) {
                i++;
                j++;
            }
            if (j == query.length()) {
                return i - j; // Match found, return index in subject
			} else if (i < length && subject.charAt(i) != query.charAt(j)) {
                if (j != 0) j = lps[j - 1];
                else i++;
            }
        }

        return -1; // No match found
    }

    public static void main(String[] args) {
		String subject = "HqwriThisisjfewahfhihhcihewiabc";
		String query = "abc";
		KMPMatcher kmpMatcher = new KMPMatcher(subject);
		int index = kmpMatcher.kmpSearch(query, 10);
        if (index != -1) {
            System.out.println("查询序列在主体字符串中的起始位置是: " + index);
        } else {
            System.out.println("查询序列不在主体字符串中。");
        }
    }
}