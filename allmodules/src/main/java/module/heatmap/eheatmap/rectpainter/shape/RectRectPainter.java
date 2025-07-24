package module.heatmap.eheatmap.rectpainter.shape;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public class RectRectPainter extends RectShapePainter {

	public RectRectPainter(double w, double h) {
		super(w, h);
	}
	private Rectangle2D.Double painter = new Rectangle2D.Double();
	@Override
	public Shape getShape(double x, double y, double value, double normedVa) {
		painter.setRect(x, y, w, h);
		return painter;
	}

}
