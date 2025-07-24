package module.tablelikeview.gui;

public class TSVImportInfoBean {

	protected String filePath = "";

	protected boolean hasHeader = true;

	protected char seperator;
	
	protected int tableContentLineNumber = 100;

	protected TSVImportInfoBean() {
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}


	public void setSeperator(char c) {
		this.seperator = c;

	}

	public char getSeperator() {
		return seperator;
	}

	public int getTableContentLineNumber() {
		return tableContentLineNumber;
	}

	public void setTableContentLineNumber(int tableContentLineNumber) {
		this.tableContentLineNumber = tableContentLineNumber;
	}

}
