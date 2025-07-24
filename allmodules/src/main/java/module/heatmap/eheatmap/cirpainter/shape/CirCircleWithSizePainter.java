package module.heatmap.eheatmap.cirpainter.shape;

import java.awt.Shape;
import java.awt.geom.Arc2D;

import module.heatmap.eheatmap.cirpainter.CirShapePainter;

public class CirCircleWithSizePainter extends CirShapePainter {
	private Arc2D.Double painter = new Arc2D.Double();
	public CirCircleWithSizePainter(double w, double h) {
		super(w, h);
	}

	@Override
	public Shape getShape(int x0, int y0, double value, double normedVa) {

		double degree = startDeg + 0.5 * extendDeg;
		double centerX = x0 + r1 * Math.cos(degree * oneDegRadians);
		double centerY = y0 - r1 * Math.sin(degree * oneDegRadians);

		double diameter = shorterOne * normedVa;
		//painter.setArc(centerX - 0.5 * shorterOne, centerY - 0.5 * shorterOne, diameter, diameter, 90, 360, Arc2D.OPEN);
		painter.setArcByCenter(centerX, centerY, 0.5 * diameter, 0, 360, Arc2D.OPEN);
		
		return painter;
	}

}
