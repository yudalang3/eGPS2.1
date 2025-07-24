package evoltree.tanglegram;

import evoltree.struct.EvolNode;
import evoltree.txtdisplay.ReflectGraphicNode;

import java.awt.*;

public abstract class BaseRectangularLayoutCalculator {
	
	public final int blankLength = 30;
	
	public abstract <T extends EvolNode> void calculateTree(ReflectGraphicNode<T> root, Dimension dim);

	
}
