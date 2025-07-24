package module.heatmap.eheatmap.rectpainter.shape;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public class RectEllipsePainter extends RectShapePainter {
	private Ellipse2D.Double painter = new Ellipse2D.Double();
	public RectEllipsePainter(double w, double h) {
		super(w, h);
	}
	@Override
	public Shape getShape(double x, double y, double value, double normedVa) {
		painter.setFrame(x, y, w, h);
		return painter;
	}

}
