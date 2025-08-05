package module.evolview.gfamily.work.gui.render;

import java.awt.Color;

import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.model.tree.GraphicsNode;

public class NoColorRender implements ColorRander {

	@Override
	public void renderNodes(GraphicsNode rootNode) {
		TreeOperationUtil.recursiveIterateTreeIF(rootNode, node ->{
			node.getDrawUnit().setLineColor(Color.black);
		});
	}

}
