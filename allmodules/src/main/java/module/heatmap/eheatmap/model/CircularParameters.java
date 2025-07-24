package module.heatmap.eheatmap.model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class CircularParameters implements Cloneable {

	private double totalSstartDeg = 120;
	private double totalEextendDeg = 300;// 360
	private double TotalDegreeForPaintingColTree = 30;

	Map<String, Point2D.Double> linkages = new HashMap<String, Point2D.Double>();

	private double innerRadius = 80;
	private double rowAnnotationRadius = 52;
	private double ringThickness = 20;
	
	private boolean shouldAnimate = false;

	// This is for test!
	String[] froms = { "Name1", "Name2", "Name3", "Name4", "Name0", "Name1", "Name0", "Name4", "Name6", "Name9" };
	String[] tos = { "Name9", "Name3", "Name9", "Name5", "Name9", "Name8", "Name4", "Name5", "Name0", "Name7" };
	private double rowAnnotationEachThickness = 12;
	private int colEachAnnotationDegree = 3;
	
	@Override
	protected CircularParameters clone() throws CloneNotSupportedException {
		CircularParameters ret = new CircularParameters();
		ret.totalSstartDeg = this.totalSstartDeg;
		ret.totalEextendDeg = this.totalEextendDeg;
		ret.linkages = this.linkages;
		ret.innerRadius = this.innerRadius;
		ret.ringThickness = this.ringThickness;
		
		ret.froms = this.froms;
		ret.tos = this.tos;
		ret.shouldAnimate = this.shouldAnimate;
		return ret;
	}
	
	
	
	public boolean isShouldAnimate() {
		return shouldAnimate;
	}



	public void setShouldAnimate(boolean shouldAnimate) {
		this.shouldAnimate = shouldAnimate;
	}



	public double getInnerRadius() {
		return innerRadius;
	}
	public void setInnerRadius(double innerRadius) {
		this.innerRadius = innerRadius;
	}

	public double getTotalSstartDeg() {
		return totalSstartDeg;
	}
	public void setTotalSstartDeg(double totalSstartDeg) {
		this.totalSstartDeg = totalSstartDeg;
	}
	public double getTotalEextendDeg() {
		return totalEextendDeg;
	}
	public void setTotalEextendDeg(double totalEextendDeg) {
		this.totalEextendDeg = totalEextendDeg;
	}
	public Map<String, Point2D.Double> getLinkages() {
		return linkages;
	}
	public void setLinkages(Map<String, Point2D.Double> linkages) {
		this.linkages = linkages;
	}
	public double getRingThickness() {
		return ringThickness;
	}
	public void setRingThickness(double ringThickness) {
		this.ringThickness = ringThickness;
	}
	public String[] getFroms() {
		return froms;
	}
	public void setFroms(String[] froms) {
		this.froms = froms;
	}
	public String[] getTos() {
		return tos;
	}
	public void setTos(String[] tos) {
		this.tos = tos;
	}

	public void setRowAnnotationRadius(double rowAnnotationRadius) {
		this.rowAnnotationRadius = rowAnnotationRadius;
		
	}
	
	public double getRowAnnotationRadius() {
		return rowAnnotationRadius;
	}



	public double getRowAnnotationEachThickness() {
		return rowAnnotationEachThickness;
	}
	
	public void setRowAnnotationEachThickness(double rowAnnotationEachThickness) {
		this.rowAnnotationEachThickness = rowAnnotationEachThickness;
	}

	public final double getRowAnnGapFactor() {
		return 0.3;
	}



	public int getColEachAnnotationDegree() {
		return colEachAnnotationDegree;
	}



	public void setColEachAnnotationDegree(int i) {
		this.colEachAnnotationDegree = i;
	}



	public int getColEachAnnotationDegreeGap() {
		return 6;
	}



	public double getTotalDegreeForPaintingColTree() {
		return TotalDegreeForPaintingColTree;
	}



	public void setTotalDegreeForPaintingColTree(double totalDegreeForPaintingColTree) {
		TotalDegreeForPaintingColTree = totalDegreeForPaintingColTree;
	}



}
