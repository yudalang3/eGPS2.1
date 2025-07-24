package module.heatmap.eheatmap.rectpainter;

import java.awt.Shape;

public abstract class RectShapePainter {
	
	protected final double shorterOne;
	protected double w,h;

	public RectShapePainter(double w,double h) {
		shorterOne = Math.min(w, h);
		this.w = w;
		this.h = h;
	}
	
	public abstract Shape getShape(double x,double y,double value,double normedVa);
}
