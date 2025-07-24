package module.heatmap.eheatmap.model.selection;

public class NameSelection {

	private String name;
	
	private int index;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "##Name is: " + name +"\t"+index;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof NameSelection) {
			NameSelection nameSelection = (NameSelection) obj;
			return this.name.equalsIgnoreCase(nameSelection.getName()) && this.index == nameSelection.getIndex();
		}
		return false;
	}
	
}
