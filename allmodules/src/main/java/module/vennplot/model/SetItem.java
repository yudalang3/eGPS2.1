package module.vennplot.model;

public class SetItem {
	
	private String name;
	private Element[] setElements;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getSetLists() {
		int length = setElements.length;
		String[] ret = new String[length];
		for (int i = 0; i < length; i++) {
			ret[i] = setElements[i].toString();
		}
		return ret;
	}
	public Element[] getSetElements() {
		return setElements;
	}
	public void setSetElements(Element[] setElements) {
		this.setElements = setElements;
	}

}
