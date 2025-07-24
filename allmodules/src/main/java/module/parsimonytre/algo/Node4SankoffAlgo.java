package module.parsimonytre.algo;

import java.util.Arrays;

import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;

public class Node4SankoffAlgo extends ArrayBasedNode {
	private final int[] minParsimonyScore;
	ChosenIndexOfChildren chosenIndexes;
	int choosedCharIndex;
	private PrepareFactor prepareFactor;

	public Node4SankoffAlgo(ComplexSnakoffAlgo snakoffAlgo) {
		PrepareFactor prepareFactor = snakoffAlgo.getPrepareFactor();
		this.prepareFactor = prepareFactor;
		int totalStates = prepareFactor.getTotalStates();
		this.minParsimonyScore = new int[totalStates];
	}

	public StateAfterMutation getChosenChar() {
		StateAfterMutation[] index2stateMapRelationship = prepareFactor.getIndex2stateMapRelationship();
		return index2stateMapRelationship[this.choosedCharIndex];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID\t").append(this.ID);
		sb.append("\tscore:\t").append(Arrays.toString(this.minParsimonyScore));
		sb.append("\tchar is: ").append(getChosenChar().getStringOfOneStateAfterMutation());
		sb.append("\tChildren are: ");

		int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			EvolNode child = getChildAt(i);
			sb.append(child.getID()).append(",");
		}

		sb.deleteCharAt(sb.length() - 1);

		if (this.chosenIndexes != null) {
			sb.append("\n");

			Indexes4EachStates[] indexes4EachStates = this.chosenIndexes.getIndexes4EachStates();
			byte b;
			int j;
			Indexes4EachStates[] arrayOfIndexes4EachStates1;
			for (j = (arrayOfIndexes4EachStates1 = indexes4EachStates).length, b = 0; b < j;) {
				Indexes4EachStates oneMinEntry = arrayOfIndexes4EachStates1[b];
				sb.append(oneMinEntry.toString()).append(" | ");
				b++;
			}

		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	void setChoosedCharIndex(int choosedCharIndex) {
		this.choosedCharIndex = choosedCharIndex;
	}

	void setChosenIndexes(ChosenIndexOfChildren chosenIndexes) {
		this.chosenIndexes = chosenIndexes;
	}

	public int[] getMinParsimonyScore() {
		return this.minParsimonyScore;
	}

	public PrepareFactor getPrepareFactor() {
		return prepareFactor;
	}

	public void setMinParsimonyScore(int[] array) {
		for (int i = 0; i < array.length; i++)
			this.minParsimonyScore[i] = array[i];
	}
}
