package module.evolview.gfamily.work.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.tree.control.SubCircularLayout;
import module.evolview.gfamily.work.gui.tree.control.SubRadicalLayout;
import module.evolview.gfamily.work.gui.tree.control.SubRectangularLayout;
import module.evolview.gfamily.work.gui.tree.control.SubSlopeLayout;
import module.evolview.gfamily.work.gui.tree.control.SubSprialLayout;
import module.evolview.phylotree.visualization.graphics.struct.TreeLayout;
import module.evolview.gfamily.work.model.enums.TreeLayoutEnum;

@SuppressWarnings("serial")
public class CtrlTreeLayoutPanel extends BaseCtrlPanel {

	protected GUITreeZoomContainerInTopFixPanel guiZoomAndRotateContainner;
	protected JPanel centerPanelContainner;
	protected CardLayout cardLayout = null; // CardLayout布局管理器

	protected SubRectangularLayout subRectLayout;
	private SubCircularLayout subCirLayout;
	private SubRadicalLayout subRadicalLayout;
	private SubSlopeLayout subSlopeLayout;
	private SubSprialLayout subSprialLayout;

	protected TreeLayoutSwitcher toplayoutSwither;

	public CtrlTreeLayoutPanel() {
		setLayout(new BorderLayout(0, 0));

		setBorder(BorderFactory.createEmptyBorder());

		setTopDiminsionWithSwitherPanel();
		setCenterPanel();
	}

	@Override
	public void setController(GeneFamilyController controller) {
		super.setController(controller);

		controller.setTreeLayoutSwither(toplayoutSwither);
		guiZoomAndRotateContainner.setController(controller);

		subRectLayout.setController(controller);
		subCirLayout.setController(controller);
		subRadicalLayout.setController(controller);
		subSlopeLayout.setController(controller);
		subSprialLayout.setController(controller);
	}

	/**
	 * 这个Panel是常驻的，不会随着布局的切换而切换
	 */
	protected void setTopDiminsionWithSwitherPanel() {
		JPanel topPanelContainner = new JPanel(new BorderLayout());
		toplayoutSwither = new TreeLayoutSwitcher(this);

		// toplayoutSwither 这个类的GUI是
		// Rectangular / Circular / Spiral / Slant / 和 Radial 布局切换按钮
		topPanelContainner.add(toplayoutSwither, BorderLayout.NORTH);

		guiZoomAndRotateContainner = new GUITreeZoomContainerInTopFixPanel();
		// 这个类的GUI是 Height scale, Width scale的按钮
		topPanelContainner.add(guiZoomAndRotateContainner, BorderLayout.CENTER);

		add(topPanelContainner, BorderLayout.NORTH);
	}

	protected void setCenterPanel() {

		cardLayout = new CardLayout(5, 5);
		{
			centerPanelContainner = new JPanel(cardLayout);
			add(centerPanelContainner, BorderLayout.CENTER);
		}

		{
			subRectLayout = new SubRectangularLayout();
			centerPanelContainner.add(subRectLayout, TreeLayoutEnum.RECTANGULAR_LAYOUT.getName());
		}
		{
			subCirLayout = new SubCircularLayout();
			centerPanelContainner.add(subCirLayout, TreeLayoutEnum.CIRCULAR_LAYOUT.getName());
		}
		{
			subRadicalLayout = new SubRadicalLayout();
			centerPanelContainner.add(subRadicalLayout, TreeLayoutEnum.RADICAL_LAYOUT.getName());
		}
		{
			subSlopeLayout = new SubSlopeLayout();
			centerPanelContainner.add(subSlopeLayout, TreeLayoutEnum.SLOPE_LAYOUT.getName());
		}
		{
			subSprialLayout = new SubSprialLayout();
			centerPanelContainner.add(subSprialLayout, TreeLayoutEnum.SPRIAL_LAYOUT.getName());
		}
	}

	void showCardLayout(TreeLayoutEnum treeLayoutType) {
		cardLayout.show(centerPanelContainner, treeLayoutType.getName());

		switch (treeLayoutType) {
		case RECTANGULAR_LAYOUT:
			subRectLayout.actionsTurn2ThisLayout(treeLayoutType);
			guiZoomAndRotateContainner.actionsTurn2ThisLayout(treeLayoutType);
			break;
		case CIRCULAR_LAYOUT:
			subCirLayout.actionsTurn2ThisLayout(treeLayoutType);
			guiZoomAndRotateContainner.actionsTurn2ThisLayout(treeLayoutType);
			break;
		case RADICAL_LAYOUT:
			subRadicalLayout.actionsTurn2ThisLayout(treeLayoutType);
			guiZoomAndRotateContainner.actionsTurn2ThisLayout(treeLayoutType);
			break;
		case SPRIAL_LAYOUT:
			subSprialLayout.actionsTurn2ThisLayout(treeLayoutType);
			guiZoomAndRotateContainner.actionsTurn2ThisLayout(treeLayoutType);
			break;
		default:
			// SLOPE_LAYOUT_NAME
			controller.getSelectedPhylogeneticTreePanel()
					.updateViewAccording2TreeLayout(TreeLayout.SLOPE_CLADO_ALIGNED_LEFT);
			guiZoomAndRotateContainner.actionsTurn2ThisLayout(treeLayoutType);
			break;
		}
	}

	public void resetZoomInAndOut2zero() {
		guiZoomAndRotateContainner.resetZoomInAndOut2zero();

	}

	public void resetZoomLayoutPanel(int newHeight, int newWidth) {
		guiZoomAndRotateContainner.resetZoomLayoutPanel(newHeight, newWidth);
	}

}
