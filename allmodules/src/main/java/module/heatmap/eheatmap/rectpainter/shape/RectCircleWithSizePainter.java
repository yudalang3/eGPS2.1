package module.heatmap.eheatmap.rectpainter.shape;

import java.awt.Shape;
import java.awt.geom.Arc2D;

import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public class RectCircleWithSizePainter extends RectShapePainter {
	private Arc2D.Double painter = new Arc2D.Double();
	public RectCircleWithSizePainter(double w, double h) {
		super(w, h);
	}
	@Override
	public Shape getShape(double x, double y, double value, double normedVa) {
		double centerX = x + 0.5 * w;
		double centerY = y + 0.5 * h;
		double diameter = 0.5 * shorterOne * normedVa;
//		painter.setArc(centerX - 0.5 * shorterOne, centerY - 0.5 *shorterOne, 
//				diameter, diameter, 90, 360, Arc2D.PIE);
		painter.setArcByCenter(centerX, centerY, diameter, 0, 360, Arc2D.PIE);
		return painter;
	}

}
