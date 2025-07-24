package module.multiseq.alignment.view.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.alignment.sequence.SequenceComponentRatio;

public class PrintSequenceDataForAViewer extends BasicSequenceData {

	private List<SequenceI> currentPaintSequences = new ArrayList<SequenceI>();

	private List<SequenceComponentRatio> ratio = new ArrayList<SequenceComponentRatio>();

	public PrintSequenceDataForAViewer(List<SequenceI> dataSequences) {
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

			char[] columnSequence = getColumnSequence(i, sequenceSize);

			SequenceComponentRatio sequenceRatio = getSequenceRatio(columnSequence, sequenceSize);

			ratio.add(sequenceRatio);
		}
	}

	public char[] getColumnSequence(int index, int sequenceSize) {
		char[] c = new char[sequenceSize];
		for (int i = 0; i < sequenceSize; i++) {
			SequenceI sequenceI = dataSequences.get(i);
			c[i] = sequenceI.getCharAt(index);
		}
		return c;
	}

	public SequenceComponentRatio getSequenceRatio(char[] chars, int sequenceSize) {
		ArrayList<Character> lists = new ArrayList<Character>();
		TreeSet<Character> set = new TreeSet<Character>();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '-') {
				continue;
			}
			lists.add(c);
			set.add(c);
		}
		Collections.sort(lists);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lists.size(); i++) {
			sb.append(lists.get(i));
		}
		String input = sb.toString();
		int maxValue = 0;
		SequenceComponentRatio sequenceRatio = new SequenceComponentRatio();
		Iterator<Character> its = set.iterator();
		while (its.hasNext()) {
			Character os = its.next();
			int begin = input.indexOf(os);
			int end = input.lastIndexOf(os);
			int value = end - begin + 1;
			if (value >= maxValue) {
				maxValue = value;
				sequenceRatio.setBase(os);
				sequenceRatio.setMaxValue(maxValue);
				int percentage = (int) (maxValue * 100.0 / sequenceSize);
				sequenceRatio.setPercentage(percentage);

			}
		}

		return sequenceRatio;
	}

}
