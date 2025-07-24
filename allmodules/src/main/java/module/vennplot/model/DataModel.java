package module.vennplot.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DataModel {

	private SetItem[] setItems;
	
	/**
	 * For left pie patings
	 */
	private int numOfAllUniqueElements;
	private int[][] patintingMatrix;

	public SetItem[] getSetItems() {
		return setItems;
	}
	public void setSetItems(SetItem[] setItems) {
		this.setItems = setItems;
	}
	
	public int getNumOfSets() {
		return setItems.length;
	}
	public int getNumOfIntersectionItems() {
		int length = setItems.length;
		return (int) Math.pow(2, length) - 1;
	}
	
	public void calculateDataForLeftPie() {
		LinkedHashSet<Element> temSet = getUnionOfAllSet(setItems);
		Map<Element, Integer>  map = new HashMap<Element, Integer>(temSet.size());
		int index = 0;
		for (Element element : temSet) {
			map.put(element, index);
			index ++;
		}
		
		numOfAllUniqueElements = index;
		int length = setItems.length;
		patintingMatrix = new int[length][];
		for (int i = 0; i < length; i++) {
			SetItem setItem = setItems[i];
			Element[] setElements = setItem.getSetElements();
			int ll = setElements.length;
			int[] array = new int[ll];
			for (int j = 0; j < ll; j++) {
				array[j] = map.get(setElements[j]);
			}
			patintingMatrix[i] = array;
		}
		
	}
	
	public int getNumOfAllUniqueElements() {
		return numOfAllUniqueElements;
	}
	
	public int[][] getPatintingMatrix() {
		return patintingMatrix;
	}
	
	private LinkedHashSet<Element> getUnionOfAllSet(SetItem[] setItems2) {
		int tt = 0;
		for (SetItem setItem : setItems2) {
			tt += setItem.getSetElements().length;
		}
		LinkedHashSet<Element> ret = new LinkedHashSet<>(tt);
		for (SetItem setItem : setItems2) {
			Element[] setElements = setItem.getSetElements();
			for (Element element : setElements) {
				ret.add(element);
			}
		}
		return ret;
	}
	public final Set<String> getIntersection(Set<String> set1, Set<String> set2) {
		Set<String> a;
		Set<String> b;
		Set<String> res = new HashSet<String>();
		if (set1.size() <= set2.size()) {
			a = set1;
			b = set2;
		} else {
			a = set2;
			b = set1;
		}
		for (String e : a) {
			if (b.contains(e)) {
				res.add(e);
			}
		}
		return res;
	}

	/**
	 * Get elements existed in set1 but not existed in set2
	 * 
	 * @param set1
	 * @param set2
	 * @return
	 */
	public final Set<String> getComplement(Set<String> set1, Set<String> set2) {
		Set<String> res = new HashSet<String>();
		for (String e : set1) {
			if (!set2.contains(e)) {
				res.add(e);
			}
		}
		return res;
	}
}
