package utils.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CombinatoricsUtil {

	public static void main(String[] args) {

		List<String> input = Arrays.asList("a", "b", "c", "d");

		for (int i = 1; i <= input.size(); i++) {
			List<List<String>> combination = combination(input, i);
			System.out.println(combination.size());
			for (List<String> list : combination) {
				System.out.println(list);
			}
		}

	}

	/**
	 * 这里计算的是集合的组合数有多少。 例如：上面的例子，abcd四个元素，任意取出其中一个有多少种组合数。
	 * 
	 * @param <T>
	 * @param values
	 * @param size
	 * @return
	 */
	public static <T> List<List<T>> combination(List<T> values, int size) {

		if (0 == size) {
			return Collections.singletonList(Collections.<T>emptyList());
		}

		if (values.isEmpty()) {
			return Collections.emptyList();
		}

		List<List<T>> combination = new LinkedList<List<T>>();

		T actual = values.iterator().next();

		List<T> subSet = new LinkedList<T>(values);
		subSet.remove(actual);

		List<List<T>> subSetCombination = combination(subSet, size - 1);

		for (List<T> set : subSetCombination) {
			List<T> newSet = new LinkedList<T>(set);
			newSet.add(0, actual);
			combination.add(newSet);
		}

		combination.addAll(combination(subSet, size));

		return combination;
	}
}
