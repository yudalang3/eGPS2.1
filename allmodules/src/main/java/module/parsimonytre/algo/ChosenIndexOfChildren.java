package module.parsimonytre.algo;

public class ChosenIndexOfChildren {
	private Indexes4EachStates[] indexes4EachStates;
	private int nextIndex = 0;

	public Indexes4EachStates[] getIndexes4EachStates() {
		return this.indexes4EachStates;
	}

	public void setIndexes4EachStates(Indexes4EachStates[] indexes4EachStates) {
		this.indexes4EachStates = indexes4EachStates;
	}

	public void initializeStatesBean(int totalStates) {
		this.indexes4EachStates = new Indexes4EachStates[totalStates];
		this.nextIndex = 0;
	}

	public void addOneStatesIndex(Indexes4EachStates oneEntry) {
		this.indexes4EachStates[this.nextIndex++] = oneEntry;
	}
}
