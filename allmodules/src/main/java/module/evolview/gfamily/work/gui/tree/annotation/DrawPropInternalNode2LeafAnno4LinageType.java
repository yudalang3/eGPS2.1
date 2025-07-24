package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.Color;
import java.util.List;

import module.evolview.gfamily.work.model.tree.GraphicsNode;

public class DrawPropInternalNode2LeafAnno4LinageType extends DrawPropInternalNode2LeafAnno {

	private List<GraphicsNode> nodes;

	public DrawPropInternalNode2LeafAnno4LinageType(Color color, List<GraphicsNode> nodes) {
		super(color, nodes.get(0));
		this.nodes = nodes;
	}


	public List<GraphicsNode> getNodes() {
		return nodes;
	}

}
