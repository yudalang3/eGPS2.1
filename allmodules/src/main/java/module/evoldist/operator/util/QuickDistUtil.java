package module.evoldist.operator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import module.evoldist.operator.pairdist.DistParameterPareparerABEqualPos;
import module.evoldist.operator.pairdist.SimpleObj;

import java.util.Set;

public class QuickDistUtil {

	/**
	 * <pre>
	 * 
	 *  ElegantJTable    65    65    10
	 *  B    66    66    11
	 *  C    67    67    12
	 *  D    68    68    13
	 *  G    71    71    16
	 *  H    72    72    17
	 *  K    75    75    20
	 *  M    77    77    22
	 *  -    45    45    -1
	 *  N    78    78    23
	 *  R    82    82    27
	 *  S    83    83    28
	 *  T    84    84    29
	 *  V    86    86    31
	 *  W    87    87    32
	 *  X    88    88    33
	 *  Y    89    89    34
	 *  The minimun is : 65	89
	 *  Size is : 17
	 * 
	 * </pre>
	 */

	private final static int[][] nucleotidesHashSet;

	private static DistParameterPareparerABEqualPos calculatorWithAB = new DistParameterPareparerABEqualPos();

	static final HashMap<Character, Set<Character>> oneChar2presentationsMap;

	static {
		oneChar2presentationsMap = new HashMap<Character, Set<Character>>();
		oneChar2presentationsMap.put('A', new HashSet<>(Arrays.asList('A')));
		oneChar2presentationsMap.put('G', new HashSet<>(Arrays.asList('G')));
		oneChar2presentationsMap.put('C', new HashSet<>(Arrays.asList('C')));
		oneChar2presentationsMap.put('T', new HashSet<>(Arrays.asList('T')));
		oneChar2presentationsMap.put('Y', new HashSet<>(Arrays.asList('C', 'T')));
		oneChar2presentationsMap.put('R', new HashSet<>(Arrays.asList('A', 'G')));
		oneChar2presentationsMap.put('W', new HashSet<>(Arrays.asList('A', 'T')));
		oneChar2presentationsMap.put('S', new HashSet<>(Arrays.asList('G', 'C')));
		oneChar2presentationsMap.put('K', new HashSet<>(Arrays.asList('T', 'G')));
		oneChar2presentationsMap.put('M', new HashSet<>(Arrays.asList('A', 'C')));
		oneChar2presentationsMap.put('D', new HashSet<>(Arrays.asList('A', 'G', 'T')));
		oneChar2presentationsMap.put('V', new HashSet<>(Arrays.asList('A', 'G', 'C')));
		oneChar2presentationsMap.put('H', new HashSet<>(Arrays.asList('A', 'T', 'C')));
		oneChar2presentationsMap.put('B', new HashSet<>(Arrays.asList('T', 'G', 'C')));
		oneChar2presentationsMap.put('N', new HashSet<>(Arrays.asList('A', 'G', 'C', 'T')));
		oneChar2presentationsMap.put('X', new HashSet<>(Arrays.asList('A', 'G', 'C', 'T')));
		oneChar2presentationsMap.put('-', new HashSet<>(Arrays.asList('-')));

		Set<Entry<Character, Set<Character>>> entrySet = oneChar2presentationsMap.entrySet();

		List<Entry<Character, Set<Character>>> list = new ArrayList<>(entrySet);

		list.sort(new Comparator<Entry<Character, Set<Character>>>() {
			@Override
			public int compare(Entry<Character, Set<Character>> o1, Entry<Character, Set<Character>> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});

		// 就是所有碱基的ascii码，直接给这个大小覆盖它全部
		final int MAX_NUMBUR_SIZE = 90;
		nucleotidesHashSet = new int[MAX_NUMBUR_SIZE][];

//		System.out.println("-------------------------------");
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Entry<Character, Set<Character>> entry = list.get(i);

			char key = entry.getKey().charValue();
			List<Character> tempList = new ArrayList<>(entry.getValue());
			Collections.sort(tempList);
			int size2 = tempList.size();

			int[] array = new int[size2];

			for (int j = 0; j < size2; j++) {
				char charValue = tempList.get(j).charValue();
				array[j] = charValue;
			}
			nucleotidesHashSet[key] = array;
		}
//		for (Entry<Character, Set<Character>> entry : list) {
//			System.out.println(entry);
//			
//		}
//		System.out.println("-------------------------------");

//		for (int[] entry : nucleotidesHashSet) {
//			System.out.println(Arrays.toString(entry));
//		}
//		System.out.println("-------------------------------");
	}
	
	public static Set<Character> getNucleotideSetOfTarget(Character cha) {
		Set<Character> candidateStates = oneChar2presentationsMap.get(cha);
		return candidateStates;
	}

	/**
	 * 
	 * Deal with ambiguous base with equal possiblities.
	 * 
	 * @title getTwoSNPCharDifferenceWithAmbiguousBase
	 * @createdDate 2020-11-06 10:35
	 * @lastModifiedDate 2020-11-06 10:35
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param a
	 * @param b
	 * @return double
	 */
	public static double getTwoSNPCharDifferenceWithAmbiguousBase(char a, char b) {
		SimpleObj ret = calculatorWithAB.simpleParseTwoCharSNP2GetDiff(a, b);
		return ret.getDifference();
	}

	/**
	 * 
	 * Deal with ambiguous base with including relationship.
	 * 
	 * For example: Y means C/T, so the distance of Y and C is 0.
	 * 
	 * @title getTwoSNPCharDifferenceWithAmbiguousBase2
	 * @createdDate 2020-11-06 10:35
	 * @lastModifiedDate 2020-11-06 10:35
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param a
	 * @param b
	 * @return double : 0 is equal.
	 */
	public static double getTwoSNPCharDifferenceWithAmbiguousBase2(char a, char b) {
		char upperA = Character.toUpperCase(a);
		char upperB = Character.toUpperCase(b);

		Set<Character> set1 = oneChar2presentationsMap.get(upperA);
		Set<Character> set2 = oneChar2presentationsMap.get(upperB);

		if (set1 == null || set2 == null) {
			System.out.println("Error in " + upperA + "\t" + upperB);
			return 1;
		}

		if (set1.containsAll(set2) || set2.containsAll(set1)) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * @title judgeTwoAllelesIdentities
	 * @createdDate 2020-11-23 13:57
	 * @lastModifiedDate 2020-11-23 13:57
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param a
	 * @param b
	 * @return boolean : true if all chars in <code>a</code> and <code>b</code>
	 *         equals or contains.
	 */
	public static boolean judgeTwoAllelesIdentities(String a, String b) {
		int length = a.length();
		if (b.length() != length) {
			return false;
		}

		for (int i = 0; i < length; i++) {

			double res = getTwoSNPCharDifferenceWithAmbiguousBaseAccording2IntArray(a.charAt(i), b.charAt(i));
			if (res != 0) {
				return false;
			}
		}

		return true;
	}

	public static boolean hasAmbiguousNucleotide(String destination) {

		char[] charArray = destination.toCharArray();
		for (char c : charArray) {
			int size = oneChar2presentationsMap.get(c).size();
			if (size > 1) {
				return true;
			}
		}

		return false;
	}

	public static boolean hasAmbiguousNucleotide(char c) {

		int size = oneChar2presentationsMap.get(c).size();
		if (size > 1) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Deal with ambiguous base with including relationship.
	 * 
	 * For example: Y means C/T, so the distance of Y and C is 0.
	 * 
	 * 这是加强版为了快速运算的一个版本：有如下的省略 1. 输入的char必须要大写
	 * 
	 * 技术细节: 1. 没有用到hashset，而是直接从碱基得到一个下标，以这个下标作为key，直接用一个数组实现了哈希。 2.
	 * containsAll方法没有再用一个哈希了，因为这个元素最多也就四五个，所以直接一个循环判定是差不多的。
	 * 
	 * @title getTwoSNPCharDifferenceWithAmbiguousBase2
	 * @createdDate 2020-11-06 10:35
	 * @lastModifiedDate 2020-11-06 10:35
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param a
	 * @param b
	 * @return double : 0 is equal.
	 */
	public static double getTwoSNPCharDifferenceWithAmbiguousBaseAccording2IntArray(char a, char b) {

		int[] setA = nucleotidesHashSet[a];
		int[] setB = nucleotidesHashSet[b];
		if (containsAll(setA, setB) || containsAll(setB, setA)) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 判断 setA是否包含了setB的所有元素
	 * 
	 * @param setA
	 * @param setB
	 * @return
	 */
	private static boolean containsAll(int[] setA, int[] setB) {
		for (int i : setB) {
			boolean notContains = true;
			for (int j : setA) {
				if (j == i) {
					notContains = false;
				}
			}
			if (notContains) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		String a = "C",b = "N";
		boolean ret = judgeTwoAllelesIdentities(b, a);
		
		System.out.println(ret);
	}

}
