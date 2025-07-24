package module.heatmap.eheatmap.rectpainter.shape;

import java.awt.Shape;
import java.awt.geom.Arc2D;

import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public class RectCircleWithAnglePainter extends RectShapePainter {
	private Arc2D.Double painter = new Arc2D.Double();
	public RectCircleWithAnglePainter(double w, double h) {
		super(w, h);
	}
	@Override
	public Shape getShape(double x, double y, double value, double normedVa) {
		double centerX = x + 0.5 * w;
		double centerY = y + 0.5 * h;
//		painter.setArc(centerX - 0.5 * shorterOne, centerY - 0.5 *shorterOne, 
//				shorterOne, shorterOne, 90, 360*normedVa, Arc2D.PIE);
		painter.setArcByCenter(centerX, centerY, 0.5 * shorterOne, 90, 360 * normedVa, Arc2D.PIE);
		return painter;
	}

}
