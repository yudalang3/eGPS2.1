package module.backmutpres;

import module.evoldist.operator.util.QuickDistUtil;

public class Mutation0302{

	/**
	 * Note: this is 1-based both inclose index!
	 */
	int position;
	
	String ancestralState;
	
	String derivedState;
	
	boolean reconbinationFlag = false;

	int mutationSize = 0;
	
	
	public Mutation0302(int position, String ancestralState, String derivedState) {
		super();
		this.position = position;
		this.ancestralState = ancestralState;
		this.derivedState = derivedState;
	}

	/**
	 * @return the {@link #position}
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the {@link #position} to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the {@link #ancestralState}
	 */
	public String getAncestralState() {
		return ancestralState;
	}

	/**
	 * @param ancestralState the {@link #ancestralState} to set
	 */
	public void setAncestralState(String ancestralState) {
		this.ancestralState = ancestralState;
	}

	/**
	 * @return the {@link #derivedState}
	 */
	public String getDerivedState() {
		return derivedState;
	}

	/**
	 * @param derivedState the {@link #derivedState} to set
	 */
	public void setDerivedState(String derivedState) {
		this.derivedState = derivedState;
	}

	/**
	 * @return the {@link #reconbinationFlag}
	 */
	public boolean isRecFlag() {
		return reconbinationFlag;
	}

	/**
	 * @param reconbinationFlag the {@link #reconbinationFlag} to set
	 */
	public void setRecFlag(boolean reconbinationFlag) {
		this.reconbinationFlag = reconbinationFlag;
	}

	/**
	 * @return the {@link #mutationSize}
	 */
	public int getMutationSize() {
		return mutationSize;
	}

	/**
	 * @param mutationSize the {@link #mutationSize} to set
	 */
	public void setMutationSize(int mutationSize) {
		this.mutationSize = mutationSize;
	}

	@Override
	public String toString() {
		String newOrigin = ancestralState;
		if (ancestralState.length() > 3) {
			newOrigin = ancestralState.substring(0, 1) + "..." + ancestralState.substring(ancestralState.length() - 1);
		}
		String newDestination = derivedState;
		if (derivedState.length() > 3) {
			newDestination = derivedState.substring(0, 1) + "..." + derivedState.substring(derivedState.length() - 1);
		}
		return newOrigin + "" + position + "" + newDestination;

	}

	public String getFullInformation() {
		return ancestralState + "" + position + "" + derivedState;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Mutation0302) {
			Mutation0302 other = (Mutation0302) obj;

			if (position == other.getPosition()) {
				return QuickDistUtil.judgeTwoAllelesIdentities(ancestralState, other.getAncestralState())
						&& QuickDistUtil.judgeTwoAllelesIdentities(derivedState, other.getDerivedState());
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ancestralState.hashCode() + position + derivedState.hashCode();
	}

	public int compareTo(Mutation0302 o) {
		return this.getPosition() - o.getPosition();
	}



}
