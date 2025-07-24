package module.parsimonytre.algo;

import java.util.Objects;

import phylo.msa.util.EvolutionaryProperties;
import module.evoldist.operator.util.QuickDistUtil;

public class StateAfterMutation implements Comparable<StateAfterMutation>, Cloneable {

	public static char DELETION_INFO_CHAR = EvolutionaryProperties.GAP_CHAR;

	char insutePlaceState;

	int position;

	String insertionState = "";

	public StateAfterMutation(char insutePlaceState, int position) {

		this.insutePlaceState = insutePlaceState;
		this.position = position;

	}

	public char getInsutePlaceState() {
		return this.insutePlaceState;
	}

	public int getPosition() {
		return this.position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}

	public String getInsertionState() {
		return this.insertionState;
	}

	public int getStateSize() {
		return this.insertionState.length() + 1;
	}

	public StateAfterMutation clone() {
		StateAfterMutation stateAfterMutation = new StateAfterMutation(this.insutePlaceState, this.position);
		stateAfterMutation.insertionState = this.insertionState;

		return stateAfterMutation;
	}

	public String toString() {
		return getStringOfOneStateAfterMutation().concat(String.valueOf(this.position));
	}

	public int hashCode() {

		return Objects.hash(new Object[] { this.insertionState, Character.valueOf(this.insutePlaceState),
				Integer.valueOf(this.position) });
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StateAfterMutation)) {
			return false;
		}
		StateAfterMutation other = (StateAfterMutation) obj;

		return (this.position == other.position && this.insutePlaceState == other.insutePlaceState
				&& Objects.equals(this.insertionState, other.insertionState));
	}

	public boolean fuzzyEquals(StateAfterMutation anStateAfterMutation) {
		if (this.position == anStateAfterMutation.position) {
			double diff1 = QuickDistUtil.getTwoSNPCharDifferenceWithAmbiguousBaseAccording2IntArray(
					this.insutePlaceState, anStateAfterMutation.insutePlaceState);
			if (diff1 == 0.0D && QuickDistUtil.judgeTwoAllelesIdentities(this.insertionState,
					anStateAfterMutation.insertionState)) {
				return true;
			}
		}

		return false;
	}

	public String getStringOfOneStateAfterMutation() {
		String ret = String.valueOf(this.insutePlaceState);
		return ret.concat(this.insertionState);
	}

	public void setInsertionState(String insertionState) {
		this.insertionState = insertionState;
	}

	public int compareTo(StateAfterMutation o) {
		return this.position - o.position;
	}

	public void setInsutePlaceState(char insutePlaceState) {
		this.insutePlaceState = insutePlaceState;
	}
}
