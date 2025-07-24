package module.heatmap.eheatmap.tree;

import evoltree.struct.EvolNode;

public interface TreeMethod {
	
	/**
	 * 这里实际返回的就是 GraphicsNode
	 * @param distance
	 * @param names
	 * @return
	 */
	EvolNode tree(double[][] distance, String[] names);

}
