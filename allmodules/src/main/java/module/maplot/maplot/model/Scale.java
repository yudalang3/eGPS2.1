package module.maplot.maplot.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Scale {

	private Line2D.Double xLine;

	private double axisValue;

	private Point2D.Double valueLocation;

	public Line2D.Double getXLine() {
		return xLine;
	}

	public void setXLine(Line2D.Double xLine) {
		this.xLine = xLine;
	}

	public double getAxisValue() {
		return axisValue;
	}

	public void setAxisValue(double axisValue) {
		this.axisValue = axisValue;
	}

	public Point2D.Double getValueLocation() {
		return valueLocation;
	}

	public void setValueLocation(Point2D.Double valueLocation) {
		this.valueLocation = valueLocation;
	}
	
	

}
