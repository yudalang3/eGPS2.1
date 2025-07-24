package utils.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableInt;

public class StringCounter {

	private Map<String, MutableInt> counterMap = new HashMap<>();

	public void printWithOriginalOrder() {
		for (Entry<String, MutableInt> entry : counterMap.entrySet()) {
			System.out.println(entry);
		}
	}
	public void printSortedResults() {
		List<Entry<String, MutableInt>> list = new ArrayList<>(counterMap.entrySet());
		
		Collections.sort(list, new Comparator<Entry<String, MutableInt>>() {
			@Override
			public int compare(Entry<String, MutableInt> o1, Entry<String, MutableInt> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		for (Entry<String, MutableInt> entry : list) {
			System.out.println(entry);
		}
	}

	public void addOneEntry(String str) {
		MutableInt mutableInt = counterMap.get(str);
		if (mutableInt == null) {
			MutableInt mutableInt2 = new MutableInt(1);
			counterMap.put(str, mutableInt2);
		} else {
			mutableInt.increment();
		}
	}
	
	public void clear() {
		counterMap.clear();
	}
	
	public Map<String, MutableInt> getCounterMap() {
		return counterMap;
	}

	public static void main(String[] args) {
		StringCounter stringCounter = new StringCounter();
		
		for (int i = 0; i < 10; i++) {
			String ss = String.valueOf(i);
			
			stringCounter.addOneEntry(ss);
		}
		
		stringCounter.printWithOriginalOrder();
	}
}
