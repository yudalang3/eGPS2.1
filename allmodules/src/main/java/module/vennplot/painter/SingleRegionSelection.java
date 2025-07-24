package module.vennplot.painter;

public class SingleRegionSelection {
	
	int index;
	
	public SingleRegionSelection(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SingleRegionSelection) {
			return ((SingleRegionSelection) obj).getIndex() == this.index;
		}
		return false;
	}
	
	
}
