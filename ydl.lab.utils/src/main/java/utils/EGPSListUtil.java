package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;

import utils.string.StringCounter;

/**
 * 一些对list的处理操作
 * 
 * @title EGPSListUtil
 * @createdDate 2021-11-15 16:00
 * @lastModifiedDate 2021-11-15 16:00
 * @author yjn
 * @since 1.7
 * 
 */
public class EGPSListUtil {

	/**
	 * 
	 * 统计string list中每个元素出现的次数
	 * 
	 * @title CountSameComponents
	 * @createdDate 2021-11-15 10:14
	 * @lastModifiedDate 2021-11-15 10:14
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param inputList
	 * @return
	 * @return Map<String,Integer>
	 */
	public static Map<String, Integer> countSameComponents(List<String> inputList) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String str : inputList) {
			Integer i = 1; // 定义一个计数器，用来记录重复数据的个数
			if (map.get(str) != null) {
				i = map.get(str) + 1;
			}
			map.put(str, i);
		}
		return map;
	}

	/**
	 * 
	 * 也是统计string list中每个元素出现的次数，在千万级别时会更快一些，可参见test
	 * 
	 * @title countString
	 * @createdDate 2021-11-15 16:28
	 * @lastModifiedDate 2021-11-15 16:28
	 * @author ydl
	 * @since 1.7
	 * 
	 * @param inputList
	 * @return
	 * @return Map<String,MutableInt>
	 */
	public static Map<String, MutableInt> countString(List<String> inputList) {
		StringCounter stringCounter = new StringCounter();
		for (String string : inputList) {
			stringCounter.addOneEntry(string);
		}
		return stringCounter.getCounterMap();
	}

	/**
	 * Returns consecutive sublists of a list, each of the same size (the final list
	 * may be smaller). For example, partitioning a list containing [a, b, c, d, e]
	 * with a partition size of 3 yields [[a, b, c], [d, e]] -- an outer list
	 * containing two inner lists of three and two elements, all in the original
	 * order.
	 * 
	 * @param <T>
	 * @param bigList
	 * @return
	 */
	public static <T> List<List<T>> partition(List<T> bigList, int numOfParts) {

		if (numOfParts < 1) {
			throw new IllegalArgumentException("The numOfParts should greater than 0");
		}
		List<List<T>> chunks = new ArrayList<>(numOfParts);

		int size = bigList.size();
		int sizeOfEachChunk = (int) Math.ceil(size / (double) numOfParts);

		for (int i = 0; i < size; i += sizeOfEachChunk) {
			int toIndex = i + sizeOfEachChunk;
			if (toIndex > size) {
				toIndex = size;
			}
			List<T> subList = bigList.subList(i, toIndex);
			List<T> chunk = new ArrayList<>(subList);
			chunks.add(chunk);
		}

		return chunks;
	}

	/**
	 * 
	 * 判定list2是否包含list1 >=percentThreshold的元素
	 * 
	 * @title containsPercent
	 * @createdDate 2023-06-27 10:06
	 * @lastModifiedDate 2023-06-27 10:06
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param <T>
	 * @param list1
	 * @param list2
	 * @param percentThreshold
	 * @return
	 * @return boolean
	 */
	public static <T> boolean containsPercent(List<T> list1, List<T> list2, double percentThreshold) {
		int count = 0;

		for (T num : list1) {
			if (list2.contains(num)) {
				count++;
			}
		}

		double percentage = (double) count / list1.size();

		return percentage >= percentThreshold;
	}

	/**
	 * 
	 * 获得list1和list2的差集。返回list1有，list2没有的元素。
	 * 
	 * @title calculateDifference
	 * @createdDate 2023-08-21 16:50
	 * @lastModifiedDate 2023-08-21 16:50
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param <T>
	 * @param list1
	 * @param list2
	 * @return
	 * @return List<T>
	 */
	public static <T> List<T> calculateDifference(List<T> list1, List<T> list2) {
		return list1.stream().filter(element -> !list2.contains(element)).collect(Collectors.toList());
	}

	/**
	 * 
	 * 计算两个List的交集
	 * 
	 * @title calculateIntersection
	 * @createdDate 2023-08-21 18:12
	 * @lastModifiedDate 2023-08-21 18:12
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param <T>
	 * @param list1
	 * @param list2
	 * @return
	 * @return List<T>
	 */
	public static <T> List<T> calculateIntersection(List<T> list1, List<T> list2) {
		return list1.stream().filter(element -> list2.contains(element)).collect(Collectors.toList());
	}

	
	public static <T> List<T> calculateSubtract(List<T> list1, List<T> list2) {
		return list1.stream().filter(element -> !list2.contains(element)).collect(Collectors.toList());
	}
	
	public static void main(String[] args) {
		List<String> list = Arrays.asList("a", "b", "c", "d", "e");
		List<String> list2 = Arrays.asList("a", "b", "c","d","e");
		List<List<String>> partition = partition(list, 78);
		System.out.println(calculateSubtract(list, list2).size());

//		for (List<String> l : partition) {
//			System.out.println(l);
//		}
	}

}
