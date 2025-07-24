package module.parsimonytre.algo;

public class PrepareFactor {

	private StateAfterMutation[] index2stateMapRelationship;
	private int totalStates;

	public PrepareFactor(StateAfterMutation[] index2stateMapRelationship) {
		this.index2stateMapRelationship = index2stateMapRelationship;
		this.totalStates = index2stateMapRelationship.length;
	}

	public StateAfterMutation[] getIndex2stateMapRelationship() {
		return index2stateMapRelationship;
	}

	public int getTotalStates() {
		return totalStates;
	}

	/**
	 * 直接得到 score matrix，这个 matrix 不区分转换和颠换
	 * 
	 * @return
	 */
	public int[][] getScoreMatrix() {
		int[][] ret = new int[this.totalStates][this.totalStates];

		for (int i = 0; i < this.totalStates; i++) {
			for (int j = 0; j < this.totalStates; j++) {
				if (j != i) {
					ret[i][j] = 1;
				}
			}
		}
		return ret;
	}
}
