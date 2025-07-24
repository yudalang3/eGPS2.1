package module.heatmap.eheatmap.model.selection;

public class AnnotationSelection {
	
	private int theStorageIndex;
	private boolean ifRow;
	
	public AnnotationSelection(int theStorageIndex, boolean ifRow) {
		this.theStorageIndex = theStorageIndex;
		this.ifRow = ifRow;
	}
	public int getTheStorageIndex() {
		return theStorageIndex;
	}
	public void setTheStorageIndex(int theStorageIndex) {
		this.theStorageIndex = theStorageIndex;
	}
	public boolean isIfRow() {
		return ifRow;
	}
	public void setIfRow(boolean ifRow) {
		this.ifRow = ifRow;
	}
	
}
