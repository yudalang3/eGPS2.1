package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * 画谱系分类的Sideward类型的标记
 * 
 * @author
 *
 */
public class DrawPropOutterSidewardAnno4LinageType {

	final SidewardNodeAnnotation sidewardNodeAnnotation;

	float textXLocation;
	float textYLocation;
	Shape shape;

	boolean is4sideward = true;
	boolean isJust4annotationNot4lineageType = false;

	public DrawPropOutterSidewardAnno4LinageType(SidewardNodeAnnotation sidewardNodeAnnotation) {
		this.sidewardNodeAnnotation = sidewardNodeAnnotation;
	}

	public void drawAnnotation(Graphics2D g2d) {
		if (shape == null) {
			return;
		}

		Color lineColor = sidewardNodeAnnotation.getLineColor();
		g2d.setColor(lineColor);
		g2d.setStroke(sidewardNodeAnnotation.getLineStroke());

		if (!is4sideward) {
			g2d.fill(shape);
			g2d.draw(shape);
		}
		g2d.draw(shape);

		if (isJust4annotationNot4lineageType) {
			g2d.setColor(sidewardNodeAnnotation.getTextColor());
			g2d.setFont(sidewardNodeAnnotation.getTextFont());
			String text = sidewardNodeAnnotation.getTextString();

			if (shape instanceof Line2D) {
				if (sidewardNodeAnnotation.isDrawTextHorizontal()) {
					g2d.drawString(text, textXLocation, textYLocation);
				} else {
					final double degrees = Math.toRadians(-90);
					float xx = textXLocation + 15;
					float yy = (float) (textYLocation + 0.5 * g2d.getFontMetrics().stringWidth(text));
					g2d.rotate(degrees, xx, yy);
					g2d.drawString(text, xx, yy);
					g2d.rotate(-degrees, xx, yy);
				}
			} else {
				g2d.drawString(text, textXLocation, textYLocation);
			}
		}

	}

	public SidewardNodeAnnotation getSidewardNodeAnnotation() {
		return sidewardNodeAnnotation;
	}

	public void setTextLocationAndShape(float x, float y, Shape shape) {
		float lineWidth = sidewardNodeAnnotation.getLineStroke().getLineWidth();

		this.textXLocation = (float) (x + lineWidth);
		this.textYLocation = y;

		this.shape = shape;

	}

	/**
	 * 这个方法会新式
	 * 
	 * @param is4sideward
	 */
	public void setIs4sideward(boolean is4sideward) {
		this.is4sideward = is4sideward;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Point2D.Double getTextLocation() {
		return new Point2D.Double(textXLocation, textYLocation);
	}

	public void setTextLocation(Point2D.Double point) {
		textXLocation = (float) point.getX();
		textYLocation = (float) point.getY();
	}
	
	public void setJust4annotationNot4lineageType(boolean isJust4annotationNot4lineageType) {
		this.isJust4annotationNot4lineageType = isJust4annotationNot4lineageType;
	}
}
