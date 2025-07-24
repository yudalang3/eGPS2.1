package module.vennplot.model;

import java.awt.Color;

public class IntersectionRegionElement implements Comparable<IntersectionRegionElement>{

	/**
	 * 1 contains;0 complement;-1 don't care
	 */
	byte[] booleans;
	int count;
	float ratio_this2highest;
	
	Color filledColor = Color.black;
	
	Color borderColor;
	
	Element[] elements;
	private boolean showPantagrame = false;

	/**
	 * 1 contains;0 complement;-1 don't care
	 */
	public byte[] getBooleans() {
		return booleans;
	}

	/**
	 * 1 contains;0 complement;-1 don't care
	 */
	public void setBooleans(byte[] booleans) {
		this.booleans = booleans;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getRatio_this2highest() {
		return ratio_this2highest;
	}

	public void setRatio_this2highest(float ratio_this2highest) {
		this.ratio_this2highest = ratio_this2highest;
	}

	public Color getFilledColor() {
		return filledColor;
	}

	public void setFilledColor(Color filledColor) {
		this.filledColor = filledColor;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public String[] getElementNames() {
		int length = elements.length;
		String[] ret = new String[length];
		for (int i = 0; i < length; i++) {
			ret[i] = elements[i].toString();
		}
		return ret;
	}

	public Element[] getElements() {
		return elements;
	}

	public void setElements(Element[] elements) {
		this.elements = elements;
	}

	@Override
	public int compareTo(IntersectionRegionElement o) {
		if (this.count > o.getCount()) {
			return -1;
		}else if (this.count < o.getCount()) {
			return 1;
		}else {
			return 0;
		}
	}

	public boolean ifShowPantagrame() {
		return showPantagrame;
	}
	
	public void setShowPantagrame(boolean showPantagrame) {
		this.showPantagrame = showPantagrame;
	}
	
	
}
