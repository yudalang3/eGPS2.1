package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D.Double;

import module.evolview.model.tree.GraphicsNode;

import java.awt.geom.GeneralPath;

/**
 * 
 * Both for common annotation and linage type specific annotation!
 */
public class DrawPropInternalNodeInsituAnno extends AnnotationRender {

	int size;
	CommonShape shape;
	Color shapeColor;

	String text;
	Font font;
	Color fontColor;

	Shape drawShape;

	public DrawPropInternalNodeInsituAnno(GraphicsNode node) {
		super(node);
	}

	public void setShapParameter(int size, CommonShape shape, Color shapeColor) {
		this.size = size;
		this.shape = shape;
		this.shapeColor = shapeColor;
	}

	public void setFontParameter(Font font, String text, Color fontColor) {
		this.font = font;
		this.text = text;
		this.fontColor = fontColor;
	}

	/**
	 * 这个类的配置的话是比较方便的，直接调用这个方法就行了！
	 */
	public void configurate() {

		double xLocation = node.getXSelf();
		double yLocation = node.getYSelf();

		configurate4lineageType(xLocation, yLocation);
	}

	public void configurate4lineageType(double xLocation, double yLocation) {
		GeneralPath generalPath = new GeneralPath();
		switch (shape) {
		case TRIANGLE:
			generalPath.reset();
			generalPath.moveTo(xLocation + size, yLocation - size);
			generalPath.lineTo(xLocation - size, yLocation);
			generalPath.lineTo(xLocation + size, yLocation + size);
			generalPath.closePath();
			drawShape = generalPath;
			break;
		case RECTANGE:
			generalPath.reset();
			generalPath.moveTo(xLocation - size, yLocation - size);
			generalPath.lineTo(xLocation - size, yLocation + size);
			generalPath.lineTo(xLocation + size, yLocation + size);
			generalPath.lineTo(xLocation + size, yLocation - size);
			generalPath.closePath();
			drawShape = generalPath;
			break;
		case ELLIPSE:

			double dd = size * 0.5;
			Double ellipse = new Double(xLocation - dd, yLocation - dd, size, size);
			drawShape = ellipse;
			break;
		default:
			break;
		}
	}

	@Override
	public void drawAnnotation(Graphics2D g2d) {
		g2d.setColor(shapeColor);
		g2d.fill(drawShape);

		g2d.setFont(font);
		g2d.setColor(fontColor);

		double centerX = drawShape.getBounds().getCenterX() + size + 5;
		double centerY = drawShape.getBounds().getCenterY();
		g2d.drawString(text, (float) centerX, (float) centerY);
	}

	public GraphicsNode getCurrentGraphicsNode() {
		return node;
	}

	public DrawPropInternalNodeInsituAnno clone(GraphicsNode newNode) {
		DrawPropInternalNodeInsituAnno ret = new DrawPropInternalNodeInsituAnno(newNode);
		ret.setFontParameter(this.font, this.text, this.fontColor);
		ret.setShapParameter(this.size, this.shape, this.shapeColor);
		return ret;
	}

	
	public String getText() {
		return text;
	}
	
	public Shape getDrawShape() {
		return drawShape;
	}
	
	public void setDrawShape(Shape drawShape) {
		this.drawShape = drawShape;
	}
}
