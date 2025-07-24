package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class DrawingPropertyPaintBar {
    private String trackName;
    private float trackNameXLocation;
    private float trackNameYLocation;
    private Line2D.Double xLine;// 横坐标线段
    private Line2D.Double yLine;// 纵坐标线段
    private List<AxisTicks<Integer>> xAxisLocations; // 横坐标刻度

    private List<AxisTicks<Double>> yAxisLocations;// 纵坐标刻度

    private List<DrawingPropertyLine> ncov2019GenomePaintLine; // 具体的内容线段

    private double maxHeight; //绘制突变率区间的高度

    private double blinkTopSpaceLength; //绘制突变率顶部留白区域得高度

    private Rectangle2D.Double constrainedFromRectangle;   //绘制左边矩形区域(没有数据部分)
    private Rectangle2D.Double constrainedToRectangle;  //绘制右边矩形区域
    private List<Line2D.Double> constrainedFromLines; //绘制from切斜线段
    private List<Line2D.Double> constrainedToLines;
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
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
	public List<AxisTicks<Integer>> getxAxisLocations() {
		return xAxisLocations;
	}
	public void setxAxisLocations(List<AxisTicks<Integer>> xAxisLocations) {
		this.xAxisLocations = xAxisLocations;
	}
	public List<AxisTicks<Double>> getyAxisLocations() {
		return yAxisLocations;
	}
	public void setyAxisLocations(List<AxisTicks<Double>> yAxisLocations) {
		this.yAxisLocations = yAxisLocations;
	}
	public List<DrawingPropertyLine> getNcov2019GenomePaintLine() {
		return ncov2019GenomePaintLine;
	}
	public void setNcov2019GenomePaintLine(List<DrawingPropertyLine> ncov2019GenomePaintLine) {
		this.ncov2019GenomePaintLine = ncov2019GenomePaintLine;
	}
	public double getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}
	public double getBlinkTopSpaceLength() {
		return blinkTopSpaceLength;
	}
	public void setBlinkTopSpaceLength(double blinkTopSpaceLength) {
		this.blinkTopSpaceLength = blinkTopSpaceLength;
	}
	public Rectangle2D.Double getConstrainedFromRectangle() {
		return constrainedFromRectangle;
	}
	public void setConstrainedFromRectangle(Rectangle2D.Double constrainedFromRectangle) {
		this.constrainedFromRectangle = constrainedFromRectangle;
	}
	public Rectangle2D.Double getConstrainedToRectangle() {
		return constrainedToRectangle;
	}
	public void setConstrainedToRectangle(Rectangle2D.Double constrainedToRectangle) {
		this.constrainedToRectangle = constrainedToRectangle;
	}
	public List<Line2D.Double> getConstrainedFromLines() {
		return constrainedFromLines;
	}
	public void setConstrainedFromLines(List<Line2D.Double> constrainedFromLines) {
		this.constrainedFromLines = constrainedFromLines;
	}
	public List<Line2D.Double> getConstrainedToLines() {
		return constrainedToLines;
	}
	public void setConstrainedToLines(List<Line2D.Double> constrainedToLines) {
		this.constrainedToLines = constrainedToLines;
	}

    
    
}
