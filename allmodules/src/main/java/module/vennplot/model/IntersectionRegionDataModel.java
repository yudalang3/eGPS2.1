package module.vennplot.model;

import java.util.ArrayList;
import java.util.List;

public class IntersectionRegionDataModel {
	
	List<IntersectionRegionElement> eles = new ArrayList<IntersectionRegionElement>();
	int higestNumber;
	String title = "Intersection size";
	int expendHigestNumber = 1000;
	
	public List<IntersectionRegionElement> getEles() {
		return eles;
	}
	public int getHigestNumber() {
		return higestNumber;
	}
	public void setHigestNumber(int higestNumber) {
		this.higestNumber = higestNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getExpendHigestNumber() {
		return expendHigestNumber;
	}
	public void setExpendHigestNumber(int expendHigestNumber) {
		this.expendHigestNumber = expendHigestNumber;
	}
	
	public void setEles(List<IntersectionRegionElement> eles) {
		this.eles = eles;
	}
}
