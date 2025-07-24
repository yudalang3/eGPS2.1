package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableInt;

public class EGPSObjectCounter<T> {

	private Map<T, MutableInt> counterMap = new LinkedHashMap<>();

	public void printWithOriginalOrder() {
		for (Entry<T, MutableInt> entry : counterMap.entrySet()) {
			System.out.println(entry);
		}
	}
	public void printSortedResults() {
		List<Entry<T, MutableInt>> list = new ArrayList<>(counterMap.entrySet());
		
		Collections.sort(list, new Comparator<Entry<T, MutableInt>>() {
			@Override
			public int compare(Entry<T, MutableInt> o1, Entry<T, MutableInt> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		
		for (Entry<T, MutableInt> entry : list) {
			System.out.println(entry);
		}
	}

	public void addOneEntry(T str) {
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
	
	public Map<T, MutableInt> getCounterMap() {
		return counterMap;
	}

	public static void main(String[] args) {
		EGPSObjectCounter<String> stringCounter = new EGPSObjectCounter<>();
		
		for (int i = 0; i < 10; i++) {
			String ss = String.valueOf(i);
			
			stringCounter.addOneEntry(ss);
		}
		
		stringCounter.printWithOriginalOrder();
	}
}
