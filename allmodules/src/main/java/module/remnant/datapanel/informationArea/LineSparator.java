package module.remnant.datapanel.informationArea;

public enum LineSparator {
	
	TABULATOR(0,"tab (\\t)","\\t"),
	COMMA(1,"Comma (,)",","),
	WHITESPACE(2, "white space"," "),
	VERTICALLINE(3,"Bar (\\|)","\\|");
	
	private int indexInCombox;
	private String name;
	private String regExp;

	LineSparator(int indexInCombox,String name,String regExp){
		this.indexInCombox = indexInCombox;
		this.name = name;
		this.regExp = regExp;
	}
	
	public int getIndexInCombox() {
		return indexInCombox;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRegularExp() {
		return regExp;
	}
}
