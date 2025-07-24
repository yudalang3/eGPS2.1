package module.pcarunner.model;

import java.awt.geom.Point2D;

import egps2.utils.common.model.datatransfer.TwoTuple;

public class PaintingLocations {

	TwoTuple<double[], double[]> twoDimArraysForPainting;
	TwoTuple<double[], double[]> xAxis;
	TwoTuple<double[], double[]> yAxis;
	
	
	Point2D.Double originCoordinate;
	Point2D.Double yMostCoordinate;
	Point2D.Double xMostCoordinate;

	double yForXAxis;
	double xForYAxis;
	
	
	public double getyForXAxis() {
		return yForXAxis;
	}

	public void setyForXAxis(double yForXAxis) {
		this.yForXAxis = yForXAxis;
	}

	public double getxForYAxis() {
		return xForYAxis;
	}

	public void setxForYAxis(double xForYAxis) {
		this.xForYAxis = xForYAxis;
	}

	public TwoTuple<double[], double[]> getTwoDimArraysForPainting() {
		return twoDimArraysForPainting;
	}

	public void setTwoDimArraysForPainting(TwoTuple<double[], double[]> twoDimArraysForPainting) {
		this.twoDimArraysForPainting = twoDimArraysForPainting;
	}

	public TwoTuple<double[], double[]> getxAxis() {
		return xAxis;
	}

	public void setxAxis(TwoTuple<double[], double[]> xAxis) {
		this.xAxis = xAxis;
	}

	public TwoTuple<double[], double[]> getyAxis() {
		return yAxis;
	}

	public void setyAxis(TwoTuple<double[], double[]> yAxis) {
		this.yAxis = yAxis;
	}

	public Point2D.Double getOriginCoordinate() {
		return originCoordinate;
	}

	public void setOriginCoordinate(Point2D.Double originCoordinate) {
		this.originCoordinate = originCoordinate;
	}

	public Point2D.Double getyMostCoordinate() {
		return yMostCoordinate;
	}

	public void setyMostCoordinate(Point2D.Double yMostCoordinate) {
		this.yMostCoordinate = yMostCoordinate;
	}

	public Point2D.Double getxMostCoordinate() {
		return xMostCoordinate;
	}

	public void setxMostCoordinate(Point2D.Double xMostCoordinate) {
		this.xMostCoordinate = xMostCoordinate;
	}
	
	
}
