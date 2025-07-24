package module.vennplot.model;

import egps2.frame.gui.interacive.RectAdjustMent;

public class PaintingLocations {
	
	private double leftPieTopleftX;
	private double leftPieTopleftY;
	private double leftPieWidth;
	private double leftPieHeight;
	
	private double middleAreaTopleftX;
	private double middleAreaTopleftY;
	private double middleAreaWidth;
	private double middleAreaHeight;

	
	private double intersectRegionTopleftX;
	private double intersectRegionTopleftY;
	private double intersectRegionWidth;
	private double intersectRegionHeight;
	
	private double singleRegionTopleftX;
	private double singleRegionTopleftY;
	private double singleRegionWidth;
	private double singleRegionHeight;
	
	
	private double nameRegionTopleftX;
	private double nameRegionTopleftY;
	private double nameRegionWidth;
	private double nameRegionHeight;
	
	private double bodyTopleftX;
	private double bodyTopleftY;
	private double bodyWidth;
	private double bodyHeight;
	
	private double intersectRegionElementWidth;
	private double bodyRegionElementHeight;
	
	RectAdjustMent verticalAdjustor;
	RectAdjustMent[] horizontalAdjustors;
	
	public double getLeftPieTopleftX() {
		return leftPieTopleftX;
	}
	public void setLeftPieTopleftX(double leftPieTopleftX) {
		this.leftPieTopleftX = leftPieTopleftX;
	}
	public double getLeftPieTopleftY() {
		return leftPieTopleftY;
	}
	public void setLeftPieTopleftY(double leftPieTopleftY) {
		this.leftPieTopleftY = leftPieTopleftY;
	}
	public double getLeftPieWidth() {
		return leftPieWidth;
	}
	public void setLeftPieWidth(double leftPieWidth) {
		this.leftPieWidth = leftPieWidth;
	}
	public double getLeftPieHeight() {
		return leftPieHeight;
	}
	public void setLeftPieHeight(double leftPieHeight) {
		this.leftPieHeight = leftPieHeight;
	}
	public double getMiddleAreaTopleftX() {
		return middleAreaTopleftX;
	}
	public void setMiddleAreaTopleftX(double middleAreaTopleftX) {
		this.middleAreaTopleftX = middleAreaTopleftX;
	}
	public double getMiddleAreaTopleftY() {
		return middleAreaTopleftY;
	}
	public void setMiddleAreaTopleftY(double middleAreaTopleftY) {
		this.middleAreaTopleftY = middleAreaTopleftY;
	}
	public double getMiddleAreaWidth() {
		return middleAreaWidth;
	}
	public void setMiddleAreaWidth(double middleAreaWidth) {
		this.middleAreaWidth = middleAreaWidth;
	}
	public double getMiddleAreaHeight() {
		return middleAreaHeight;
	}
	public void setMiddleAreaHeight(double middleAreaHeight) {
		this.middleAreaHeight = middleAreaHeight;
	}
	public double getIntersectRegionTopleftX() {
		return intersectRegionTopleftX;
	}
	public void setIntersectRegionTopleftX(double intersectRegionTopleftX) {
		this.intersectRegionTopleftX = intersectRegionTopleftX;
	}
	public double getIntersectRegionTopleftY() {
		return intersectRegionTopleftY;
	}
	public void setIntersectRegionTopleftY(double intersectRegionTopleftY) {
		this.intersectRegionTopleftY = intersectRegionTopleftY;
	}
	public double getIntersectRegionWidth() {
		return intersectRegionWidth;
	}
	public void setIntersectRegionWidth(double intersectRegionWidth) {
		this.intersectRegionWidth = intersectRegionWidth;
	}
	public double getIntersectRegionHeight() {
		return intersectRegionHeight;
	}
	public void setIntersectRegionHeight(double intersectRegionHeight) {
		this.intersectRegionHeight = intersectRegionHeight;
	}
	public double getSingleRegionTopleftX() {
		return singleRegionTopleftX;
	}
	public void setSingleRegionTopleftX(double singleRegionTopleftX) {
		this.singleRegionTopleftX = singleRegionTopleftX;
	}
	public double getSingleRegionTopleftY() {
		return singleRegionTopleftY;
	}
	public void setSingleRegionTopleftY(double singleRegionTopleftY) {
		this.singleRegionTopleftY = singleRegionTopleftY;
	}
	public double getSingleRegionWidth() {
		return singleRegionWidth;
	}
	public void setSingleRegionWidth(double singleRegionWidth) {
		this.singleRegionWidth = singleRegionWidth;
	}
	public double getSingleRegionHeight() {
		return singleRegionHeight;
	}
	public void setSingleRegionHeight(double singleRegionHeight) {
		this.singleRegionHeight = singleRegionHeight;
	}
	public double getNameRegionTopleftX() {
		return nameRegionTopleftX;
	}
	public void setNameRegionTopleftX(double nameRegionTopleftX) {
		this.nameRegionTopleftX = nameRegionTopleftX;
	}
	public double getNameRegionTopleftY() {
		return nameRegionTopleftY;
	}
	public void setNameRegionTopleftY(double nameRegionTopleftY) {
		this.nameRegionTopleftY = nameRegionTopleftY;
	}
	public double getNameRegionWidth() {
		return nameRegionWidth;
	}
	public void setNameRegionWidth(double nameRegionWidth) {
		this.nameRegionWidth = nameRegionWidth;
	}
	public double getNameRegionHeight() {
		return nameRegionHeight;
	}
	public void setNameRegionHeight(double nameRegionHeight) {
		this.nameRegionHeight = nameRegionHeight;
	}
	public double getBodyTopleftX() {
		return bodyTopleftX;
	}
	public void setBodyTopleftX(double bodyTopleftX) {
		this.bodyTopleftX = bodyTopleftX;
	}
	public double getBodyTopleftY() {
		return bodyTopleftY;
	}
	public void setBodyTopleftY(double bodyTopleftY) {
		this.bodyTopleftY = bodyTopleftY;
	}
	public double getBodyWidth() {
		return bodyWidth;
	}
	public void setBodyWidth(double bodyWidth) {
		this.bodyWidth = bodyWidth;
	}
	public double getBodyHeight() {
		return bodyHeight;
	}
	public void setBodyHeight(double bodyHeight) {
		this.bodyHeight = bodyHeight;
	}
	public double getIntersectRegionElementWidth() {
		return intersectRegionElementWidth;
	}
	public void setIntersectRegionElementWidth(double intersectRegionElementWidth) {
		this.intersectRegionElementWidth = intersectRegionElementWidth;
	}
	public double getBodyRegionElementHeight() {
		return bodyRegionElementHeight;
	}
	public void setBodyRegionElementHeight(double bodyRegionElementHeight) {
		this.bodyRegionElementHeight = bodyRegionElementHeight;
	}
	
	public void setVerticalAdjustor(RectAdjustMent verticalAdjustor) {
		this.verticalAdjustor = verticalAdjustor;
	}
	
	public void setHorizontalAdjustors(RectAdjustMent[] horizontalAdjustors) {
		this.horizontalAdjustors = horizontalAdjustors;
	}
	
	public RectAdjustMent getVerticalAdjustor() {
		return verticalAdjustor;
	}
	public RectAdjustMent[] getHorizontalAdjustors() {
		return horizontalAdjustors;
	}
	
}
