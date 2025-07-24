package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;

import module.evolview.gfamily.work.gui.Triangle;

/**
 * 存储Nucleotid的
 */

public class DrawingPropertyAlignmentNucleotide {
    private String charSeq;
    // private Rectangle2D.Double seqLocation;//碱基绘制的位置
    private double x;//碱基绘制的X位置
    private double y;//碱基绘制的Y位置
    private double w;//绘制的宽度
    private double h;//绘制高度
    private Color baseColor;//碱基颜色
    private int proLocation; //这个位置是相对于当前block的信息(只有蛋白是相对于Block,氨基酸就是当前位置)
    private String proteinName; //蛋白名称

    private boolean containGapBase;
    private Triangle triangle;
    private String gapBase;
	public String getCharSeq() {
		return charSeq;
	}
	public void setCharSeq(String charSeq) {
		this.charSeq = charSeq;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getW() {
		return w;
	}
	public void setW(double w) {
		this.w = w;
	}
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	public Color getBaseColor() {
		return baseColor;
	}
	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}
	public int getProLocation() {
		return proLocation;
	}
	public void setProLocation(int proLocation) {
		this.proLocation = proLocation;
	}
	public String getProteinName() {
		return proteinName;
	}
	public void setProteinName(String proteinName) {
		this.proteinName = proteinName;
	}
	public boolean isContainGapBase() {
		return containGapBase;
	}
	public void setContainGapBase(boolean containGapBase) {
		this.containGapBase = containGapBase;
	}
	public Triangle getTriangle() {
		return triangle;
	}
	public void setTriangle(Triangle triangle) {
		this.triangle = triangle;
	}
	public String getGapBase() {
		return gapBase;
	}
	public void setGapBase(String gapBase) {
		this.gapBase = gapBase;
	}
    

}
