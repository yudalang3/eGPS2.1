package module.vennplot.painter;

public class NameSelection {
	/**
	 * -2 top title
	 * -1 bottom title
	 * 0.. is the single region element
	 */
	int index;

	public NameSelection(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NameSelection) {
			return ((NameSelection) obj).getIndex() == this.index;
		}
		return false;
	}
}
