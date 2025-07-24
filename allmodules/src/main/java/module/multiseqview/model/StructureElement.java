package module.multiseqview.model;

public class StructureElement implements Comparable<StructureElement>{

	String name;
	int start;
	int end;
	
	String colorString;
	
	@Override
	public String toString() {
		return name+" : "+start+" : "+end + " : "+colorString;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	/**
	 * @return the colorString
	 */
	public String getColorString() {
		return colorString;
	}
	/**
	 * @param colorString the colorString to set
	 */
	public void setColorString(String colorString) {
		this.colorString = colorString;
	}
	@Override
	public int compareTo(StructureElement o) {
		return this.start - o.start;
	}
	
	
}
