package evoltree.swingvis;

import java.awt.Graphics2D;

import evoltree.struct.EvolNode;
import evoltree.txtdisplay.ReflectGraphicNode;

@FunctionalInterface
public interface OneNodeDrawer<T extends EvolNode> {

	void draw(Graphics2D paramGraphics2D, ReflectGraphicNode<T> paramReflectGraphicNode);
}
