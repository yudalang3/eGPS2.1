package module.maplot.maplot.model;

import java.awt.geom.Line2D;
import java.util.List;

public class PaintingLine {

	private Line2D.Double xLine;

	private Line2D.Double yLine;

	List<Scale> xAxisLocations;

	private List<Scale> yAxisLocations;

	public Line2D.Double getXLine() {
		return xLine;
	}

	public void setXLine(Line2D.Double xLine) {
		this.xLine = xLine;
	}

	public Line2D.Double getYLine() {
		return yLine;
	}

	public void setYLine(Line2D.Double yLine) {
		this.yLine = yLine;
	}

	public List<Scale> getXAxisLocations() {
		return xAxisLocations;
	}

	public void setXAxisLocations(List<Scale> xAxisLocations) {
		this.xAxisLocations = xAxisLocations;
	}

	public List<Scale> getYAxisLocations() {
		return yAxisLocations;
	}

	public void setYAxisLocations(List<Scale> yAxisLocations) {
		this.yAxisLocations = yAxisLocations;
	}
	
	

}
