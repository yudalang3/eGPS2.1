package module.heatmap.eheatmap.cirpainter.shape;

import java.awt.Shape;
import java.awt.geom.Arc2D;

import module.heatmap.eheatmap.cirpainter.CirShapePainter;


public class CirCircleWithAnglePainter extends CirShapePainter {
	private Arc2D.Double painter = new Arc2D.Double();

	public CirCircleWithAnglePainter(double w, double h) {
		super(w, h);
	}

	@Override
	public Shape getShape(int x0, int y0, double value, double normedVa) {

		double degree = startDeg + 0.5 * extendDeg;
		double centerX = x0 + r1 * Math.cos(degree * oneDegRadians);
		double centerY = y0 - r1 * Math.sin(degree * oneDegRadians);

//		painter.setArc(centerX - 0.5 * shorterOne, centerY - 0.5 * shorterOne, shorterOne, shorterOne, 90,
//				360 * normedVa, Arc2D.PIE);
		
		painter.setArcByCenter(centerX, centerY, 0.5 * shorterOne, 0, 360 * normedVa, Arc2D.PIE);
		return painter;
	}

}
