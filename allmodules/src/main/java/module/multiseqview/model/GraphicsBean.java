package module.multiseqview.model;

import java.awt.Color;
import java.awt.Font;

import egps2.UnifiedAccessPoint;

public class GraphicsBean {

	double curveRatio = 0.5;
	/** 1-255 */
	int transparent = 100;

	private Color crossLinkedRegion = new Color(204, 204, 204, transparent);

	private Font sequenceFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private Font nameFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	
	public double getCurveRatio() {
		return curveRatio;
	}

	public void setCurveRatio(double curveRatio) {
		this.curveRatio = curveRatio;
	}

	public int getTransparent() {
		return transparent;
	}

	public void setTransparent(int transparent) {
		this.transparent = transparent;
	}

	public Color getCrossLinkedRegion() {
		return crossLinkedRegion;
	}

	public void setCrossLinkedRegion(Color crossLinkedRegion) {
		this.crossLinkedRegion = crossLinkedRegion;
	}

	public Font getSequenceFont() {
		return sequenceFont;
	}

	public void setSequenceFont(Font sequenceFont) {
		this.sequenceFont = sequenceFont;
	}

	public Font getNameFont() {
		return nameFont;
	}

	public void setNameFont(Font nameFont) {
		this.nameFont = nameFont;
	}

	
	
}
