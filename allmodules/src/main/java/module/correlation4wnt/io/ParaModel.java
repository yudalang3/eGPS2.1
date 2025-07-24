package module.correlation4wnt.io;

import java.util.List;

public class ParaModel {
	private List<String> expMatrixCol;
	private List<String> cat1Col;
	private List<String> cat2Col;

	// Getters and Setters
	public List<String> getExpMatrixCol() {
		return expMatrixCol;
	}

	public void setExpMatrixCol(List<String> expMatrixCol) {
		this.expMatrixCol = expMatrixCol;
	}

	public List<String> getCat1Col() {
		return cat1Col;
	}

	public void setCat1Col(List<String> cat1Col) {
		this.cat1Col = cat1Col;
	}

	public List<String> getCat2Col() {
		return cat2Col;
	}

	public void setCat2Col(List<String> cat2Col) {
		this.cat2Col = cat2Col;
	}

	@Override
	public String toString() {
		return "JsonData{" + "expMatrixCol=" + expMatrixCol + ", cat1Col=" + cat1Col + ", cat2Col=" + cat2Col + '}';
	}
}
