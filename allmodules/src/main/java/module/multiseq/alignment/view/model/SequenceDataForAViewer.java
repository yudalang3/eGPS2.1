package module.multiseq.alignment.view.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import msaoperator.alignment.sequence.SequenceComponentRatio;
import org.apache.commons.lang3.StringUtils;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;


/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName SequenceDataForAViewer
 * 
 * @author mhl
 * 
 * @Date Created on:2019-12-20 17:23
 * 
 */
public class SequenceDataForAViewer extends BasicSequenceData {

	private List<SequenceI> currentPaintSequences = new ArrayList<SequenceI>();

	private List<SequenceComponentRatio> ratio = new ArrayList<SequenceComponentRatio>();

	public SequenceDataForAViewer(List<SequenceI> dataSequences) {
		super(dataSequences);
	}

	public List<SequenceI> getPaintSequences() {
		return currentPaintSequences;
	}

	public List<SequenceComponentRatio> getRatio() {

		return ratio;
	}

	public void initializeCurrentPaintSequcences(int startSeq, int endSeq, int startRes, int endRes) {
		currentPaintSequences.clear();

		for (int i = startSeq; i < endSeq; i++) {
			SequenceI sequenceI = dataSequences.get(i);

			SequenceI sequence = sequenceI.getSequenceI(startRes, endRes);

			currentPaintSequences.add(sequence);
		}
	}

	/**
	 *
	 * Maximum statistic for each column of Seqs
	 * 
	 *
	 * @param startRes,endRes -Specify the starting position of the Seq index
	 * 
	 * @author mhl
	 *
	 * @Date Created on:2019-07-19 10:21
	 */
	public void calculateStaticsOfSpecificColumns(int startRes, int endRes) {

		ratio.clear();

		int sequenceSize = dataSequences.size();

		if (sequenceSize == 0) {
			return;
		}

		for (int i = startRes; i < endRes; i++) {

			List<Character> columnSequence = getColumnSequence(i, sequenceSize);
			// Character[] columnSequence = getColumnSequence(i, sequenceSize);

			SequenceComponentRatio sequenceRatio = getSequenceRatio(columnSequence, sequenceSize);

			ratio.add(sequenceRatio);
		}
	}

	public List<Character> getColumnSequence(int index, int sequenceSize) {

		List<Character> collect = dataSequences.stream().map(e -> e.getCharAt(index)).collect(Collectors.toList());
// 		Character[] c = new Character[sequenceSize];
//		for (int i = 0; i < sequenceSize; i++) {
//			SequenceI sequenceI = dataSequences.get(i);
//			c[i] = sequenceI.getCharAt(index);
//		}
// 		return c;

		// return collect.toArray(c);
		return collect;
	}

	public SequenceComponentRatio getSequenceRatio(List<Character> chars, int sequenceSize) {

		List<Character> lists = chars.stream().filter(character -> character != '-').collect(Collectors.toList());

		TreeSet<Character> set = new TreeSet<Character>(lists);

		Collections.sort(lists);

		String input = StringUtils.join(lists, "");

		int maxValue = 0;

		SequenceComponentRatio sequenceRatio = new SequenceComponentRatio();

//		Map<Character, Long> collects = input.chars().boxed()
//				.collect(Collectors.groupingBy(i -> Character.valueOf((char) i.intValue()), Collectors.counting()));
//
//		long maxValue = collects.entrySet().stream().mapToLong(max -> max.getValue()).max().getAsLong();
//		
//		
//
//		List<Character> bases = collects.entrySet().stream().filter(collect -> collect.getValue() == maxValue)
//				.map(collect -> collect.getKey()).collect(Collectors.toList());
//
//		sequenceRatio.setBase(bases.get(0));
//		sequenceRatio.setMaxValue((int) maxValue);
//		int percentage = (int) (maxValue * 100.0 / sequenceSize);
//		sequenceRatio.setPercentage(percentage);

		for (Character character : set) {
			int begin = input.indexOf(character);
			int end = input.lastIndexOf(character);
			int value = end - begin + 1;
			if (value >= maxValue) {
				maxValue = value;
				sequenceRatio.setBase(character);
				sequenceRatio.setMaxValue(maxValue);
				int percentage = (int) (maxValue * 100.0 / sequenceSize);
				sequenceRatio.setPercentage(percentage);

			}
		}

		//System.out.println(sequenceRatio.getBase() + " " + sequenceRatio.getMaxValue());

		return sequenceRatio;
	}

}
