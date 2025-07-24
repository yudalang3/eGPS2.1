package module.vennplot.model;

import java.awt.Color;

public class SingleRegionElement implements Comparable<SingleRegionElement>{
	
	int count;
	String setName = "setName";
	float ratio_this2higest;
	
	Color fillColor = Color.black;
	
	Color borColor;
	private int originalIndex;


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public float getRatio_this2higest() {
		return ratio_this2higest;
	}

	public void setRatio_this2higest(float ratio_this2higest) {
		this.ratio_this2higest = ratio_this2higest;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Color getBorColor() {
		return borColor;
	}

	public void setBorColor(Color borColor) {
		this.borColor = borColor;
	}

	@Override
	public int compareTo(SingleRegionElement o) {
		if (this.count > o.getCount()) {
			return 1;
		}else if (this.count < o.getCount()) {
			return -1;
		}else {
			return 0;
		}
	}

	public void setOriginalIndex(int i) {
		this.originalIndex = i;
	}

	public int getOriginalIndex() {
		return originalIndex;
	}
	
}
