package module.pcarunner.model;

import java.awt.Font;

import javax.swing.JLabel;

public class ParameterModel {

	double blinkSpaceLength = 100;
	
	float tick = -4;
	int numOfTicks = 5;
	float shorkeWidth = 4;
	/**
	 * 1: low and left sides
	 * 2: all sides
	 */
	byte boxStyle = 1;
	double extendingPercentage = 0.05;
	
	boolean ifPaintGradient = false;
	
	/**
	 * 1 for round
	 * 2 for rectangular
	 */
	byte paintingShape = 1;
	
	Font paintFont;
	Font xLabelFont;
	Font yLabelFont;
	Font titleFont;
	Font legnedFont;
	
	double shapeSize = 2;
	
	
	public ParameterModel() {
		//得到默认的字体
		Font font = new JLabel().getFont();
		paintFont = font.deriveFont(12f);
		xLabelFont = font.deriveFont(12f);
		yLabelFont = font.deriveFont(12f);
		titleFont = font.deriveFont(12f);
		legnedFont = font.deriveFont(12f);
	}


	
	public double getShapeSize() {
		return shapeSize;
	}



	public void setShapeSize(double shapeSize) {
		this.shapeSize = shapeSize;
	}



	public float getTick() {
		return tick;
	}


	public void setTick(float tick) {
		this.tick = tick;
	}


	public int getNumOfTicks() {
		return numOfTicks;
	}


	public void setNumOfTicks(int numOfTicks) {
		this.numOfTicks = numOfTicks;
	}


	public float getShorkeWidth() {
		return shorkeWidth;
	}


	public void setShorkeWidth(float shorkeWidth) {
		this.shorkeWidth = shorkeWidth;
	}


	public byte getBoxStyle() {
		return boxStyle;
	}


	public void setBoxStyle(byte boxStyle) {
		this.boxStyle = boxStyle;
	}


	public double getExtendingPercentage() {
		return extendingPercentage;
	}


	public void setExtendingPercentage(double extendingPercentage) {
		this.extendingPercentage = extendingPercentage;
	}


	public boolean isIfPaintGradient() {
		return ifPaintGradient;
	}


	public void setIfPaintGradient(boolean ifPaintGradient) {
		this.ifPaintGradient = ifPaintGradient;
	}


	public byte getPaintingShape() {
		return paintingShape;
	}


	public void setPaintingShape(byte paintingShape) {
		this.paintingShape = paintingShape;
	}


	public Font getPaintFont() {
		return paintFont;
	}


	public void setPaintFont(Font paintFont) {
		this.paintFont = paintFont;
	}


	public Font getxLabelFont() {
		return xLabelFont;
	}


	public void setxLabelFont(Font xLabelFont) {
		this.xLabelFont = xLabelFont;
	}


	public Font getyLabelFont() {
		return yLabelFont;
	}


	public void setyLabelFont(Font yLabelFont) {
		this.yLabelFont = yLabelFont;
	}


	public Font getTitleFont() {
		return titleFont;
	}


	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}


	public Font getLegnedFont() {
		return legnedFont;
	}


	public void setLegnedFont(Font legnedFont) {
		this.legnedFont = legnedFont;
	}


	public double getBlinkSpaceLength() {
		return blinkSpaceLength;
	}


	public void setBlinkSpaceLength(double blinkSpaceLength) {
		this.blinkSpaceLength = blinkSpaceLength;
	}
	
	
}
