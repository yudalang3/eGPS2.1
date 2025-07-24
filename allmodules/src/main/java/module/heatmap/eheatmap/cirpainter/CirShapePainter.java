package module.heatmap.eheatmap.cirpainter;

import java.awt.Shape;

public abstract class CirShapePainter {

	protected final double shorterOne;
	protected double w, h;
	
	protected double r1;
	protected double startDeg;
	protected double r2;
	protected double extendDeg;
	protected double endDeg;
	
	protected final double oneDegRadians = Math.PI / 180;

	public CirShapePainter(double w, double h) {
		shorterOne = Math.min(w, h);
		this.w = w;
		this.h = h;
	}

	/**
	 * @param r1 小圆半径
	 * @param r2 大圆半径
	 * @param startDeg 是角度
	 * @param extendDeg 是角度
	 */
	public void setProperty(double r1, double r2, double startDeg, double extendDeg) {
		this.r1 = r1;
		this.r2 = r2;
		this.startDeg = startDeg;
		this.extendDeg = extendDeg;
		this.endDeg = startDeg + extendDeg;
	}
	public abstract Shape getShape(int x0, int y0,
			double value, double normedVa);
}
