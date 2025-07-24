package module.evolview.gfamily.work.gui.tree;

import java.awt.Color;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.colorscheme.BaseContentPanel;
import module.evolview.gfamily.work.gui.render.CustomizedRenderRecord;
import module.evolview.gfamily.work.gui.render.CustomizedSaveBean;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import graphic.engine.colors.EGPSColors;

/**
 * 自定义的一个colorChooser，增加了一个勾选染色是否应用于子节点的checkbox
 * 
 * @author yjn
 *
 */
@SuppressWarnings("serial")
public class JColorChooser4Node extends BaseContentPanel {
	JColorChooser colorChooser = null;
	Color color;
	GeneFamilyController controller;
	JCheckBox isApply2Descendant;

	public JColorChooser4Node(GeneFamilyController controller) {
		this.controller = controller;
		colorChooser = new JColorChooser(Color.LIGHT_GRAY);
		this.add(colorChooser);
		isApply2Descendant = new JCheckBox("Apply to descendant nodes");
		this.add(isApply2Descendant);
		isApply2Descendant.setSelected(true);
	}

	@Override
	/**
	 * 选择好颜色后对枝进行重新染色的操作，可以争对单枝，也可针对其所有的子节点，同时染色方式跳转自定义染色
	 */
	public void executeRenderProcess() {
		color = colorChooser.getColor();
		if (color == null) {
			return;
		}
		boolean ifAllColorRendering = isApply2Descendant.isSelected();
		List<GraphicsNode> selectedNodes = controller.getTreeLayoutProperties().getSelectedNodes();

		// 处理 customizedSaveBean
		int lastColorSchemeIndex = 0;
		CustomizedSaveBean customizedSaveBean = controller.getCustomizedSaveBean();
		/**
		 * 如果 lastColorSchemeIndex 是0 ，那么上次就产生的是个定制的配色方案。不需要创建了直接放
		 */
		if (lastColorSchemeIndex != 0) {
			customizedSaveBean.setBeforeRenderingIndex(lastColorSchemeIndex);
			customizedSaveBean.getRecords().clear();
		}

		for (GraphicsNode node : selectedNodes) {
			appendInformation2CustomizedFile(customizedSaveBean, node, color, ifAllColorRendering);
			if (ifAllColorRendering) {
				TreeOperationUtil.recursiveIterateTreeIF(node, tmpNode -> {
					tmpNode.getDrawUnit().setLineColor(color);
				});
			} else {
				node.getDrawUnit().setLineColor(color);
			}

		}
		controller.getLeftControlPanelContainner().setCustomizedRadioButton();
		controller.refreshPhylogeneticTree();

	}
	

	private static void appendInformation2CustomizedFile(CustomizedSaveBean customizedSaveBean,
			GraphicsNode selectedNode, Color color, boolean isAll) {
		String hexFromColor = EGPSColors.toHexFormColor(color);

		CustomizedRenderRecord customizedRenderRecord = new CustomizedRenderRecord();
		customizedRenderRecord.setSelf(!isAll);
		customizedRenderRecord.setHexFormatColor(hexFromColor);
		customizedRenderRecord.setNodeID(selectedNode.getID());

		customizedSaveBean.getRecords().add(customizedRenderRecord);

	}

}
