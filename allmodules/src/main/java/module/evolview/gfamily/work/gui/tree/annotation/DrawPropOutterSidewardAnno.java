package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

import module.evolview.model.tree.GraphicsNode;

public class DrawPropOutterSidewardAnno extends AnnotationRender {

	/**
	 * This is also the draw from leaf not from internal node!
	 */
	DrawPropOutterSidewardAnno4LinageType drawPropOutterSidewardAnno4LinageType;

	public DrawPropOutterSidewardAnno(SidewardNodeAnnotation sidewardNodeAnnotation, GraphicsNode gNode) {
		super(gNode);
		drawPropOutterSidewardAnno4LinageType = new DrawPropOutterSidewardAnno4LinageType(sidewardNodeAnnotation);
	}

	@Override
	public void drawAnnotation(Graphics2D g2d) {
		drawPropOutterSidewardAnno4LinageType.drawAnnotation(g2d);
	}

	public GraphicsNode getCurrentGraphicsNode() {
		return node;
	}

	public void setTextLocationAndShape(double x, double y, Shape shape) {
		drawPropOutterSidewardAnno4LinageType.setTextLocationAndShape((float) x, (float) y, shape);
	}

	public SidewardNodeAnnotation getSidewardNodeAnnotation() {
		return drawPropOutterSidewardAnno4LinageType.getSidewardNodeAnnotation();
	}
	
	
	public Shape getShape() {
		return drawPropOutterSidewardAnno4LinageType.getShape();
	}
	
	public void setShape(Shape shape) {
		this.drawPropOutterSidewardAnno4LinageType.setShape(shape);
	}
	
	public Point2D.Double getTextLocation() {
		return drawPropOutterSidewardAnno4LinageType.getTextLocation();
	}
	
	public void setTextLocation(Point2D.Double point) {
		drawPropOutterSidewardAnno4LinageType.setTextLocation(point);
	}
	
	
	public void setJust4annotationNot4lineageTypeTrue() {
		drawPropOutterSidewardAnno4LinageType.setJust4annotationNot4lineageType(true);
	}

}
