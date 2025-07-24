package module.parsimonytre.algo;

import java.util.Objects;

public class StateAfterMutationWithCacheHash extends StateAfterMutation {
	private int cachedHashCode = 0;
	private boolean isHashCodeCached = false;

	public StateAfterMutationWithCacheHash(char insutePlaceState, int position) {
		super(insutePlaceState, position);

	}

	public StateAfterMutationWithCacheHash(char insutePlaceState, int position, String insert) {
		super(insutePlaceState, position);
		setInsertionState(insert);
	}

	public int hashCode() {
		if (!isHashCodeCached) {
			cachedHashCode = Objects.hash(new Object[] { this.insertionState, Character.valueOf(this.insutePlaceState),
					Integer.valueOf(this.position) });
			isHashCodeCached = true;
		}
		return cachedHashCode;

	}

}
