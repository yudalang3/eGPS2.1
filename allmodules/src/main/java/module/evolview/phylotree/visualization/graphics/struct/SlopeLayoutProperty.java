package module.evolview.phylotree.visualization.graphics.struct;

public class SlopeLayoutProperty {
	
	double leafLocationRightMostRatio = 1.0;
	double blankMarginRatio = 0.0;
	
	private int slopeLayoutRotationDeg = 0;

	public double getLeafLocationRightMostRatio() {
		return leafLocationRightMostRatio;
	}

	public void setLeafLocationRightMostRatio(double leafLocationRightMostRatio) {
		this.leafLocationRightMostRatio = leafLocationRightMostRatio;
	}

	public int getSlopeLayoutRotationDeg() {
		return slopeLayoutRotationDeg;
	}

	public void setSlopeLayoutRotationDeg(int slopeLayoutRotationDeg) {
		this.slopeLayoutRotationDeg = slopeLayoutRotationDeg;
	}

	public void setBlankMarginRatio(double d) {
		blankMarginRatio = d;
		
	}
	
	
	public double getBlankMarginRatio() {
		return blankMarginRatio;
	}

}
