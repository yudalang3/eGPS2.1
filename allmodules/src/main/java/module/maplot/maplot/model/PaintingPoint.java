package module.maplot.maplot.model;

import java.awt.Color;

public class PaintingPoint {

	private double XPos;

	private double YPos;

	private Color paintingColor;

	private ValuePoint originalPoint;

	public double getXPos() {
		return XPos;
	}

	public void setXPos(double xPos) {
		XPos = xPos;
	}

	public double getYPos() {
		return YPos;
	}

	public void setYPos(double yPos) {
		YPos = yPos;
	}

	public Color getPaintingColor() {
		return paintingColor;
	}

	public void setPaintingColor(Color paintingColor) {
		this.paintingColor = paintingColor;
	}

	public ValuePoint getOriginalPoint() {
		return originalPoint;
	}

	public void setOriginalPoint(ValuePoint originalPoint) {
		this.originalPoint = originalPoint;
	}
	
	
}
