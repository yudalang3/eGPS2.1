package module.heatmap.eheatmap.model.selection;

public class CellSelection {
	
	int ii;
	int jj;
	
	public void setTopLeftPointLocation(int ii, int jj) {
		this.ii = ii;
		this.jj = jj;
	}
	
	public int getIi() {
		return ii;
	}
	
	public int getJj() {
		return jj;
	}
	
	@Override
	public String toString() {
		return ii + "\t" + jj;
	}

	public boolean equals(int i, int j) {
		return (ii == i && jj == j);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof CellSelection) {
			CellSelection nameSelection = (CellSelection) obj;
			return this.equals(nameSelection.getIi(),nameSelection.getJj());
		}
		return false;
	}
}
