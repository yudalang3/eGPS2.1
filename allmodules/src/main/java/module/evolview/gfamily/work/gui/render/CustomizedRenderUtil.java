package module.evolview.gfamily.work.gui.render;

import java.util.HashMap;
import java.util.Map;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.colorscheme.DialogFrame;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.gfamily.work.model.tree.GraphicsNode;

public class CustomizedRenderUtil {

	public static String getDefaultCustomizedString(GeneFamilyController controller) {
//		int lastColorSchemeIndex = controller.getLastColorSchemeIndex();
		int lastColorSchemeIndex = 0;
		StringBuilder getinitialCustomizedString = new StringBuilder();
		if (lastColorSchemeIndex == 0) {
			// When the previous selection is customized !
			CustomizedSaveBean customizedSaveBean = controller.getCustomizedSaveBean();
			getinitialCustomizedString.append(getinitialCustomizedString(customizedSaveBean.getBeforeRenderingIndex()));

			GraphicsNode root = controller.getTreeLayoutProperties().getOriginalRootNode();
			Map<Integer, GraphicsNode> nodeID2nodeMap = new HashMap<>(8192);
			TreeOperationUtil.recursiveIterateTreeIF(root, node -> {
				nodeID2nodeMap.put(node.getID(), node);
			});

			for (CustomizedRenderRecord ele : customizedSaveBean.getRecords()) {
				getinitialCustomizedString.append("\n").append(getString4CustomizedRenderRecord(ele, nodeID2nodeMap));
			}

		} else {
			getinitialCustomizedString = getinitialCustomizedString.append(getinitialCustomizedString(1))
					.append("\nSingapore/4/2020	self	#FF0000\n")
					.append("Russia/Vector_80501/2020	self	#A020F0\n")
					.append("Russia/Vector_80503/2020	self	#A020F0\n");
		}

		return getinitialCustomizedString.toString();
	}

	private static String getString4CustomizedRenderRecord(CustomizedRenderRecord ele,
			Map<Integer, GraphicsNode> nodeID2nodeMap) {
		String typeString = ele.isSelf() ? "self" : "all";

		String ret = "";
		GraphicsNode node = nodeID2nodeMap.get(ele.getNodeID());
		if (node.getChildCount() == 0) {
			ret = node.getName() + "\t" + typeString + "\t" + ele.getHexFormatColor();
		} else {
			GraphicsNode tmpNode = node;
			while (tmpNode.getChildCount() != 0) {
				tmpNode = (GraphicsNode) tmpNode.getFirstChild();
			}
			ret += tmpNode.getName();
			ret += ",";

			tmpNode = node;
			while (tmpNode.getChildCount() != 0) {
				tmpNode = (GraphicsNode) tmpNode.getLastChild();
			}
			ret += tmpNode.getName();
			ret += ("\t" + typeString + "\t" + ele.getHexFormatColor());
		}
		return ret;
	}

	/**
	 * 用没有用StingBuilder，因为默认JDK会帮你优化！
	 * 
	 * @param renderIndex
	 * @return
	 */
	public static String getinitialCustomizedString(int renderIndex) {
		String ss = "##Leaf render\n" + "#format: name	self/all	color\n"
				+ "#name for internal node is: leafName1,leafName2\n" + "#following are an example!\n"
				+ "#leafName1 is the name of the first leaf under the internal node in the present tree\n"
				+ "# leafName2 is the name of the last leaf under the internal node in the present tree\n"
				+ "##RENDER_SCHEME=" + renderIndex;
		return ss;
	}

	public static void actions4Recolor2all(GeneFamilyController controller, GraphicsNode currNode) {
		if (currNode == null) {
			return;
		}
		//新的版本，自己定义了dialog,加入了apply to descendant的勾选选项
		DialogFrame dialog = DialogFrame.nodeRecolorDialogFrame(controller);
		dialog.setVisible(true);
		
	}
}
