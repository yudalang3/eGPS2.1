package module.evoltre.mutation;

import java.util.Objects;

/**
 * 这个类应该是2022年3月2日创造的
 * 
 * @author yudalang
 *
 */
public class Mutation0302 implements IMutation4Rec {

	/**
	 * Note: this is 1-based both inclose index!
	 */
	int position;

	String ancestralState;

	String derivedState;

	boolean reconbinationFlag = false;

	int mutationSize = 0;
	int lastPositionInsertLength = 0;

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
		StringBuilder sBuilder = new StringBuilder();

		if (ancestralState.length() > 3) {
			sBuilder.append(ancestralState.charAt(0)).append("...")
					.append(ancestralState.charAt(ancestralState.length() - 1));
		} else {
			sBuilder.append(ancestralState);
		}

		sBuilder.append(position);

		if (derivedState.length() > 3) {
			sBuilder.append(derivedState.charAt(0)).append("...")
					.append(derivedState.charAt(derivedState.length() - 1));
		} else {
			sBuilder.append(derivedState);
		}
		return sBuilder.toString();

	}

	public String getFullInformation() {

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(ancestralState).append(position);
		if (lastPositionInsertLength == 0) {
			sBuilder.append(derivedState);
		} else {
			char[] charArray = derivedState.toCharArray();
			int index = charArray.length;
			char[] novelCharArray = new char[index + 1];

			int leftBracketIndex = index - lastPositionInsertLength;
			System.arraycopy(charArray, 0, novelCharArray, 0, leftBracketIndex);
			novelCharArray[leftBracketIndex] = LAST_POSITION_INSERTION_SPLIT_CHAR;
			System.arraycopy(charArray, leftBracketIndex, novelCharArray, leftBracketIndex + 1,
					lastPositionInsertLength);

			sBuilder.append(novelCharArray);
		}

		return sBuilder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IMutation) {
			IMutation other = (IMutation) obj;

			if (position == other.getPosition()) {
				if( Objects.equals(ancestralState, other.getAncestralState())
						&& Objects.equals(derivedState, other.getDerivedState())) {
					if (lastPositionInsertLength == other.getLastPositionInsertLength()) {
						return true;
					}
				}
			} 
			// 下面的这个是模糊匹配，现在不用 yudalang
//			if (position == other.getPosition()) {
//				return QuickDistUtil.judgeTwoAllelesIdentities(ancestralState, other.getAncestralState())
//						&& QuickDistUtil.judgeTwoAllelesIdentities(derivedState, other.getDerivedState());
//			} else {
//				return false;
//			}

		} 
		
		return false;
	}

	@Override
	public int hashCode() {
		return ancestralState.hashCode() + position + derivedState.hashCode();
	}

	@Override
	public int compareTo(IMutation o) {
		return this.getPosition() - o.getPosition();
	}

	@Override
	public int getLastPositionInsertLength() {
		return lastPositionInsertLength;
	}

	@Override
	public void setLastPositionInsertLength(int insertLen) {
		this.lastPositionInsertLength = insertLen;
	}

}
