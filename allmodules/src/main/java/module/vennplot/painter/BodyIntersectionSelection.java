package module.vennplot.painter;

public class BodyIntersectionSelection {
	int index;

	public BodyIntersectionSelection(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BodyIntersectionSelection) {
			return ((BodyIntersectionSelection) obj).getIndex() == this.index;
		}
		return false;
	}
}
