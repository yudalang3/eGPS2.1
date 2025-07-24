package module.heatmap.eheatmap.cirpainter.shape;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import module.heatmap.eheatmap.cirpainter.CirShapePainter;

public class CirEllipsePainter extends CirShapePainter {
	private final GeneralPath path = new GeneralPath();
	private final Rectangle2D.Double innerRect = new Rectangle2D.Double();
	private final Rectangle2D.Double outterRect = new Rectangle2D.Double();

	public CirEllipsePainter(double w, double h) {
		super(w, h);
	}
	@Override
	public Shape getShape(int x0, int y0, double value, double normedVa) {
		innerRect.setRect(x0 - r1, y0 - r1, r1 + r1, r1 + r1);
		outterRect.setRect(x0 - r2, y0 - r2, r2 + r2, r2 + r2);
		double xx = x0 + r1 * Math.cos(startDeg * oneDegRadians);
		double yy = y0 - r1 * Math.sin(startDeg * oneDegRadians);
		path.reset();
		path.moveTo(xx, yy);
		path.append(new Arc2D.Double(innerRect, startDeg, extendDeg, Arc2D.OPEN), false);
		path.append(new Arc2D.Double(outterRect, endDeg, -extendDeg, Arc2D.OPEN), true);
		path.closePath();
		return path;
	}
}
