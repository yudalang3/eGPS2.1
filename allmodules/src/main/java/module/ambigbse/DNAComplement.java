package module.ambigbse;

import java.util.HashMap;
import java.util.Map;

public class DNAComplement {

    // 定义一个映射，用于查找核苷酸的互补碱基
	private final Map<Character, Character> complementMap = new HashMap<>();

	public DNAComplement() {
        complementMap.put('A', 'T');
        complementMap.put('T', 'A');
        complementMap.put('C', 'G');
        complementMap.put('G', 'C');
        complementMap.put('N', 'N'); // N代表任意核苷酸
    }

    /**
     * 获取给定DNA序列的反向互补链。
     *
     * @param dnaSequence DNA序列，只包含大写字母A, T, C, G和N。
     * @return DNA序列的反向互补链。
     */
	public String getReverseComplement(String dnaSequence) {
        StringBuilder complement = new StringBuilder();
        for (int i = dnaSequence.length() - 1; i >= 0; i--) {
            char nucleotide = dnaSequence.charAt(i);
            complement.append(complementMap.get(nucleotide));
        }
        return complement.toString();
    }

}