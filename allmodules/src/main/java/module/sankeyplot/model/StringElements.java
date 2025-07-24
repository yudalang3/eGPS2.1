package module.sankeyplot.model;

public class StringElements {
	
	String[] eleStrings;
	
	public StringElements(String[] eleStrings) {
		this.eleStrings = eleStrings;
	}


	public int getNumOfString() {
		return eleStrings.length;
	}
	
	public String[] getEleStrings() {
		return eleStrings;
	}

}
