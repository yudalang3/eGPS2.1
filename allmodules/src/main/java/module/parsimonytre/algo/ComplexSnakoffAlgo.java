package module.parsimonytre.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplexSnakoffAlgo {
	public static int largeEnoughValue = 10000;

	int TOTAL_STATES;

	protected int[][] scoreMatrix;

	private Node4SankoffAlgo rootNode;

	private Optional<Integer> prefferedState4Root;

	private PrepareFactor prepareFactor;

	public ComplexSnakoffAlgo(PrepareFactor prepareFactor) {
		this.scoreMatrix = prepareFactor.getScoreMatrix();
		this.TOTAL_STATES = prepareFactor.getTotalStates();
		this.prepareFactor = prepareFactor;
	}

	public void calculate(Node4SankoffAlgo root) {
		calculate(root, null);
	}

	public PrepareFactor getPrepareFactor() {
		return prepareFactor;
	}

	public void calculate(Node4SankoffAlgo root, Integer indexOfPreferedState) {
		this.rootNode = root;

		this.prefferedState4Root = Optional.ofNullable(indexOfPreferedState);

		leafFirstRecursiveCalculate(root);

		traceBack(root);
	}

	private void leafFirstRecursiveCalculate(Node4SankoffAlgo node) {
		int childCount = node.getChildCount();

		if (childCount > 0) {

			for (int i = 0; i < childCount; i++) {
				Node4SankoffAlgo child = (Node4SankoffAlgo) node.getChildAt(i);
				leafFirstRecursiveCalculate(child);
			}

			calculateOneNodeStatic(node);
		}
	}

	private void calculateOneNodeStatic(Node4SankoffAlgo node) {
		int childCount = node.getChildCount();
		if (childCount == 0) {
			return;
		}

		ChosenIndexOfChildren chosenIndexesBean = new ChosenIndexOfChildren();

		chosenIndexesBean.initializeStatesBean(this.TOTAL_STATES);

		for (int i = 0; i < this.TOTAL_STATES; i++) {

			ArrayList<Integer>[] arrayOfArrayList = new ArrayList[childCount];
			int sum = 0;

			for (int k = 0; k < childCount; k++) {
				Node4SankoffAlgo child = (Node4SankoffAlgo) node.getChildAt(k);

				int[] arrayOfInt = child.getMinParsimonyScore();
				int temp = Integer.MAX_VALUE;
				ArrayList<Integer> optimalIndexes = new ArrayList<>();

				for (int j = 0; j < this.TOTAL_STATES; j++) {
					int currValue = arrayOfInt[j] + this.scoreMatrix[j][i];
					if (currValue < temp) {
						temp = currValue;

						optimalIndexes.clear();
						optimalIndexes.add(Integer.valueOf(j));
					} else if (currValue == temp) {
						optimalIndexes.add(Integer.valueOf(j));
					}
				}

				arrayOfArrayList[k] = optimalIndexes;

				sum += temp;
			}

			int[] minParsimonyScore = node.getMinParsimonyScore();
			minParsimonyScore[i] = sum;

			chosenIndexesBean.addOneStatesIndex(new Indexes4EachStates((List<Integer>[]) arrayOfArrayList));
		}

		node.setChosenIndexes(chosenIndexesBean);
	}

	public void traceBack(Node4SankoffAlgo root) {
		internalNodeFirstIteration2traceBack(root);
	}

	private void internalNodeFirstIteration2traceBack(Node4SankoffAlgo node) {
		int childCount = node.getChildCount();

		if (this.rootNode == node) {
			int[] minParsimonyScore = node.getMinParsimonyScore();

			node.setChoosedCharIndex(getBestChoosedCharIndex4Root(minParsimonyScore));

			assignChoosedIndex2children(node, childCount);
		} else {
			assignChoosedIndex2children(node, childCount);
		}

		for (int i = 0; i < childCount; i++) {
			internalNodeFirstIteration2traceBack((Node4SankoffAlgo) node.getChildAt(i));
		}
	}

	private int getBestChoosedCharIndex4Root(int[] minParsimonyScore) {
		List<Integer> miniIndexes = new ArrayList<>();

		int min = Integer.MAX_VALUE;
		for (int i = 0; i < this.TOTAL_STATES; i++) {
			if (minParsimonyScore[i] < min) {
				min = minParsimonyScore[i];
				miniIndexes.clear();
				miniIndexes.add(Integer.valueOf(i));
			} else if (minParsimonyScore[i] == min) {
				miniIndexes.add(Integer.valueOf(i));
			}
		}

		if (miniIndexes.size() > 1 && this.prefferedState4Root.isPresent()) {
			Integer preferred = this.prefferedState4Root.get();
			if (miniIndexes.contains(preferred)) {
				return preferred.intValue();
			}
		}

		return miniIndexes.get(0).intValue();
	}

	private void assignChoosedIndex2children(Node4SankoffAlgo node, int childCount) {
		if (childCount == 0) {
			return;
		}

		int parentChosenIndex = node.choosedCharIndex;
		ChosenIndexOfChildren chosenIndexesOfChildren = node.chosenIndexes;
		Indexes4EachStates[] indexes4EachStates = chosenIndexesOfChildren.getIndexes4EachStates();

		for (int i = 0; i < childCount; i++) {

			Indexes4EachStates indexes4EachState = indexes4EachStates[parentChosenIndex];
			List<Integer>[] chosenOfChildren = (List<Integer>[]) indexes4EachState.getChosenOfChildren();
			List<Integer> bestIndexes = chosenOfChildren[i];

			Integer j = bestIndexes.get(0);
			if (bestIndexes.size() > 1 && bestIndexes.contains(Integer.valueOf(parentChosenIndex))) {
				j = Integer.valueOf(parentChosenIndex);
			}

			Node4SankoffAlgo child = (Node4SankoffAlgo) node.getChildAt(i);
			child.setChoosedCharIndex(j.intValue());
		}
	}
}
