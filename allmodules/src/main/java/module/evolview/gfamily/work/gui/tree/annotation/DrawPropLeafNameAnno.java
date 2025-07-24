package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;

public class DrawPropLeafNameAnno extends AnnotationRender{
	final Color color;
	
	public DrawPropLeafNameAnno(Color color, GraphicsNode node) {
		super(node);
		this.color = color;
	}

	@Override
	public void drawAnnotation(Graphics2D g2d) {
		throw new UnsupportedOperationException();
	}
	
	public Color getColor() {
		return color;
	}
	
	public List<GraphicsNode> getLeaves() {
		return GraphicTreePropertyCalculator.getLeaves(node);
	}

	public GraphicsNode getCurrentGraphicsNode() {
		return node;
	}

}
