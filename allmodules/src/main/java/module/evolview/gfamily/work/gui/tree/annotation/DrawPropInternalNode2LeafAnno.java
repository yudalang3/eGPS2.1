package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.work.model.tree.GraphicsNode;

public class DrawPropInternalNode2LeafAnno extends AnnotationRender {

	final Color color;
	Shape drawShape;

	String text;
	Font font = UnifiedAccessPoint.getLaunchProperty().getDefaultFont().deriveFont(12f);

	public DrawPropInternalNode2LeafAnno(Color color, GraphicsNode node) {
		super(node);
		this.color = color;
	}

	public void drawAnnotation(Graphics2D g2d) {
		if (drawShape == null) {
			return;
		}

		if (text != null) {
			g2d.setColor(Color.black);
			g2d.setFont(font);
			Rectangle bounds = drawShape.getBounds();
			
			double xx = bounds.x + bounds.width;
			float yy = (float) (bounds.y + 0.5 * bounds.height);
			final double degrees = Math.toRadians(-90);
			float xxx = (float) (xx + 15);
			g2d.rotate(degrees, xxx, yy);
			g2d.drawString(text, xxx, yy);
			g2d.rotate(-degrees, xxx, yy);
		}

		g2d.setColor(color);
		g2d.fill(drawShape);
	}

	public GraphicsNode getCurrentGraphicsNode() {
		return node;
	}

	public void setDrawShape(Shape rect) {
		this.drawShape = rect;

	}

	public Color getColor() {
		return color;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public Shape getDrawShape() {
		return drawShape;
	}
	
}
