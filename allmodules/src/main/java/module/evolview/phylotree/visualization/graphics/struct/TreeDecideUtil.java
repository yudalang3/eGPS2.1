
package module.evolview.phylotree.visualization.graphics.struct;

import module.evolview.model.tree.GraphicsNode;
import evoltree.txtdisplay.BaseGraphicNode;

public class TreeDecideUtil {

    /**
     * 因为经常需要递归，所以写了这个结构体! 用法：
     *
     * <pre>
     * NodeType nodeType = TreeDecideUtil.decideNodeType(node);
     * switch (nodeType) {
     * 	case LEAF:
     *
     * 	break;
     * 	case ROOT:
     *
     * 	break;
     *
     * 	default:
     * 	// Internal nodes but not root!
     *
     * 	break;
     * }
     * </pre>
     *
     * @param node
     * @return
     */
    public static NodeType decideNodeType(BaseGraphicNode node, BaseGraphicNode rootNode) {

        if (node.getChildCount() != 0) {
            if (node.getCollapse()) {
                return NodeType.LEAF;
            }
            if (node.getParentCount() == 0 || node == rootNode) {
                return NodeType.ROOT;
            }

            // Else is Internal root except root!
        } else {
            return NodeType.LEAF;
        }

        return NodeType.INTERNAL_EXCEPT_ROOT;
    }

    public static boolean decideNodeTypeOfLeaf(GraphicsNode node) {

        if (node.getChildCount() != 0) {
            if (node.getCollapse()) {
                return true;
            }
            // Else is Internal root except root!
        } else {
            return true;
        }

        return false;
    }

}
