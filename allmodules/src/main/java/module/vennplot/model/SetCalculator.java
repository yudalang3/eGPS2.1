package module.vennplot.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetCalculator {
	
	
	public IntersectionRegionDataModel calculate(SetItem[] setItems) {
		IntersectionRegionDataModel intersectionRegionDataModel = new IntersectionRegionDataModel();

		int highestValue = 0;
		int numOfSets = setItems.length;
		int numOfIntersectionItems = getNumOfIntersectionItems(numOfSets);
		for (int i = 0; i < numOfIntersectionItems; i++) {

			IntersectionRegionElement intersectionRegionElement = new IntersectionRegionElement();

			String bin = Integer.toBinaryString(numOfIntersectionItems - i);
			while (bin.length() < numOfSets) {
				bin = "0" + bin;
			}
			byte[] bbs = new byte[numOfSets];
			for (int j = 0; j < numOfSets; j++) {
				char charAt = bin.charAt(j);
				bbs[j] = (byte) (charAt - '0');
			}

			intersectionRegionElement.setBooleans(bbs);
			intersectionRegionElement.setFilledColor(Color.black);

			Element[] elementNames = getSetAfterIntersectAndComplement(bbs, setItems);
			int length = elementNames.length;
			intersectionRegionElement.setCount(length);
			intersectionRegionElement.setElements(elementNames);
			if (length > highestValue) {
				highestValue = length;
			}

			intersectionRegionDataModel.getEles().add(intersectionRegionElement);
		}

		intersectionRegionDataModel.setHigestNumber(highestValue);
		int expendHigestNumber = getExpendHigestNumber(highestValue);
		intersectionRegionDataModel.setExpendHigestNumber(expendHigestNumber);
		// TO get highest value and set ratio
		for (int i = 0; i < numOfIntersectionItems; i++) {
			IntersectionRegionElement intersectionRegionElement = intersectionRegionDataModel.getEles().get(i);
			int count = intersectionRegionElement.getCount();
			intersectionRegionElement.setRatio_this2highest(count / (float) expendHigestNumber);
		}
		
		Collections.sort(intersectionRegionDataModel.getEles());
		
		return intersectionRegionDataModel;
	}

	public int getNumOfIntersectionItems(int numOfSets) {
		return (int) Math.pow(2, numOfSets) - 1;
	}
	
	public final Element[] getSetAfterIntersectAndComplement(byte[] bbs, SetItem[] setItems) {
		/**
		 * First step is to get the index of sets which should to complement and which
		 * should be intersected!
		 */
		List<Integer> setIndexesForIntersection = new ArrayList<Integer>();
		List<Integer> setIndexesForComplement = new ArrayList<Integer>();
		int length = bbs.length;
		for (int i = 0; i < length; i++) {
			if (bbs[i] == 1) {
				setIndexesForIntersection.add(i);
			} else if (bbs[i] == 0) {
				setIndexesForComplement.add(i);
			}
		}

		int interSize = setIndexesForIntersection.size();

		Set<Element> intersectionSet = new HashSet<>(4096);
		if (interSize == 1) {
			SetItem setItem = setItems[setIndexesForIntersection.get(0)];

			for (Element element : setItem.getSetElements()) {
				intersectionSet.add(element);
			}
		} else {
			/**
			 * Find the one which has minimal elements
			 */
			int tempSize = Integer.MAX_VALUE;
			int tmpIndex = 0;
			for (int i = 0; i < interSize; i++) {
				Integer tempInteger = setIndexesForIntersection.get(i);
				int tt = setItems[tempInteger].getSetElements().length;
				if (tt < tempSize) {
					tempSize = tt;
					tmpIndex = tempInteger;
				}
			}

			Element[] minSetStrings = setItems[tmpIndex].getSetElements();

			Set<Element>[] otherSets = new Set[interSize - 1];
			int tt = 0;
			for (Integer ttt : setIndexesForIntersection) {
				if (ttt != tmpIndex) {
					Element[] setLists = setItems[ttt].getSetElements();
					Set<Element> aSet = new HashSet<>(setLists.length);
					for (Element element : setLists) {
						aSet.add(element);
					}
					otherSets[tt] = aSet;
					tt++;
				}

			}

			intersectionSet = getinterSetCore(minSetStrings, otherSets);
		}

		// complement

		int size = setIndexesForComplement.size();
		Set<Element>[] otherSets = new Set[size];
		for (int i = 0; i < size; i++) {
			HashSet<Element> hashSet = new HashSet<>();
			Element[] setLists = setItems[setIndexesForComplement.get(i)].getSetElements();

			for (Element t : setLists) {
				hashSet.add(t);
			}
			otherSets[i] = hashSet;
		}

		return aggregate2getComplemet(intersectionSet, otherSets);
	}

	private final Element[] aggregate2getComplemet(Set<Element> intersectionSet, Set<Element>[] otherSets) {
		Set<Element> ret = new HashSet<>(intersectionSet.size());

		int setIndex = 0;
		int interSize = otherSets.length;
		for (Element t : intersectionSet) {
			setIndex = 0;
			while (setIndex < interSize) {
				if (otherSets[setIndex].contains(t)) {
					break;
				}
				setIndex++;
			}

			if (setIndex == interSize) {
				ret.add(t);
			}
		}
		return ret.toArray(new Element[0]);
	}

	private final Set<Element> getinterSetCore(Element[] minSetStrings, Set<Element>[] otherSets) {

		Set<Element> intersectionSet = new HashSet<>(minSetStrings.length);

		int setIndex = 0;
		int interSize = otherSets.length;
		for (Element ele : minSetStrings) {
			setIndex = 0;
			while (setIndex < interSize) {
				if (!otherSets[setIndex].contains(ele)) {
					break;
				}
				setIndex++;
			}

			if (setIndex == interSize) {
				intersectionSet.add(ele);
			}
		}
		return intersectionSet;
	}
	
	
	private final int getExpendHigestNumber(int highest) {
		/**
		 * Temporary we see the result!
		 */
		int ret = (int) (1.1 * highest);
		if (ret == highest) {
			ret++;
		}

		return ret;
	}
}
