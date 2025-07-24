package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.geom.Line2D;
import java.util.List;

public class DrawingPropertyMutationBarPlot {

    private float trackNameXLocation;
    private float trackNameYLocation;
    private Line2D.Double xLine;// 横坐标线段

    private Line2D.Double yLine;// 纵坐标线段

    private List<AxisTicks<String>> xAxisLocations; // 横坐标刻度

    private List<AxisTicks<Double>> yAxisLocations;// 纵坐标刻度

    private List<Line2D.Double> rect;

	public float getTrackNameXLocation() {
		return trackNameXLocation;
	}

	public void setTrackNameXLocation(float trackNameXLocation) {
		this.trackNameXLocation = trackNameXLocation;
	}

	public float getTrackNameYLocation() {
		return trackNameYLocation;
	}

	public void setTrackNameYLocation(float trackNameYLocation) {
		this.trackNameYLocation = trackNameYLocation;
	}

	public Line2D.Double getxLine() {
		return xLine;
	}

	public void setxLine(Line2D.Double xLine) {
		this.xLine = xLine;
	}

	public Line2D.Double getyLine() {
		return yLine;
	}

	public void setyLine(Line2D.Double yLine) {
		this.yLine = yLine;
	}

	public List<AxisTicks<String>> getxAxisLocations() {
		return xAxisLocations;
	}

	public void setxAxisLocations(List<AxisTicks<String>> xAxisLocations) {
		this.xAxisLocations = xAxisLocations;
	}

	public List<AxisTicks<Double>> getyAxisLocations() {
		return yAxisLocations;
	}

	public void setyAxisLocations(List<AxisTicks<Double>> yAxisLocations) {
		this.yAxisLocations = yAxisLocations;
	}

	public List<Line2D.Double> getRect() {
		return rect;
	}

	public void setRect(List<Line2D.Double> rect) {
		this.rect = rect;
	}
    
    
}
