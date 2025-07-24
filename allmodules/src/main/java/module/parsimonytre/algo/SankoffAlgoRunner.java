package module.parsimonytre.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;

public class SankoffAlgoRunner {

	private static int maxValue = 10000;

	private static Comparator<StateAfterMutation> comparator = new Comparator<StateAfterMutation>(){
		@Override
		public int compare(StateAfterMutation o1, StateAfterMutation o2) {
			int compare = Character.compare(o1.insutePlaceState, o2.insutePlaceState);
			if (compare == 0) {
				compare = o1.insertionState.compareTo(o2.insertionState);
			}
			return compare;
		}
		
	};

	public static Node4SankoffAlgo quickRunAlgo(EvolNode root, Map<Integer, StateAfterMutation> leaf2stateMap) {
		return quickRunAlgo(root, leaf2stateMap, null);
	}

	public static Node4SankoffAlgo quickRunAlgo(EvolNode root, Map<Integer, StateAfterMutation> leaf2stateMap,
			StateAfterMutation preferState) {

		int size = leaf2stateMap.size();

		Collection<StateAfterMutation> values = leaf2stateMap.values();
		HashSet<StateAfterMutation> hashSet = new HashSet<>(values);
		
		ArrayList<StateAfterMutation> arrayList = new ArrayList<>(hashSet);
		Collections.sort(arrayList, comparator);
		
		
		int uniqueStates = hashSet.size();

		StateAfterMutation[] allStates = new StateAfterMutation[uniqueStates];
		int index = 0;

		Map<StateAfterMutation, Integer> state2indexMap = new HashMap<>();
		for (StateAfterMutation stateAfterMutation : arrayList) {
			allStates[index] = stateAfterMutation;
			state2indexMap.put(stateAfterMutation, Integer.valueOf(index));
			index++;
		}
		
		
		Integer indexOfPreferedState = null;
		if (preferState != null) {
			indexOfPreferedState = state2indexMap.get(preferState);
			if (indexOfPreferedState == null) {
				throw new IllegalArgumentException(
						"Sorry the prefered the state not in the leaves.\nFor example, all leaves' states are ('a','b'), while prefer state is 'c'.");
			}
		}

		List<EvolNode> leaves = EvolNodeUtil.getLeaves(root);

		if (leaves.size() != size) {
			throw new IllegalArgumentException();
		}

		PrepareFactor prepareFactor = new PrepareFactor(allStates);

		ComplexSnakoffAlgo snakoffAlgo = new ComplexSnakoffAlgo(prepareFactor);

		Node4SankoffAlgo newRoot = transferRoot(root, leaf2stateMap, state2indexMap, uniqueStates,
				snakoffAlgo);

		snakoffAlgo.calculate(newRoot, indexOfPreferedState);

		return newRoot;
	}

	public static Node4SankoffAlgo transferRoot(EvolNode node, Map<Integer, StateAfterMutation> leaf2stateMap,
			Map<StateAfterMutation, Integer> state2indexMap, int totalState, ComplexSnakoffAlgo snakoffAlgo) {
		int childCount = node.getChildCount();

		Node4SankoffAlgo newNode = new Node4SankoffAlgo(snakoffAlgo);

		newNode.setID(node.getID());
		newNode.setName(node.getName());

		if (childCount == 0) {

			StateAfterMutation stateAfterMutation = leaf2stateMap.get(Integer.valueOf(node.getID()));

			Objects.requireNonNull(stateAfterMutation);

			Integer integer = state2indexMap.get(stateAfterMutation);

			newNode.setMinParsimonyScore(getScoreArray(integer.intValue(), totalState));
		}

		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				EvolNode child = node.getChildAt(i);
				Node4SankoffAlgo transferRoot = transferRoot(child, leaf2stateMap, state2indexMap, totalState,
						snakoffAlgo);
				newNode.addChild((EvolNode) transferRoot);
			}
		}
		return newNode;
	}

	private static int[] getScoreArray(int i, int totalSiteCount) {
		int[] ret = new int[totalSiteCount];

		for (int j = 0; j < totalSiteCount; j++) {
			if (j == i) {
				ret[j] = 0;
			} else {
				ret[j] = maxValue;
			}
		}
		return ret;
	}

}
