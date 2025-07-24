package module.evolview.gfamily;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.JideTabbedPane.GradientColorProvider;

import egps2.builtin.modules.IconObtainer;
import egps2.panels.dialog.SwingDialog;
import utils.EGPSObjectsUtil;
import egps2.utils.common.util.SaveUtil;
import egps2.EGPSProperties;
import egps2.UnifiedAccessPoint;
import egps2.frame.ModuleFace;
import module.evolview.gfamily.work.beans.GeneFamilyMainFaceBean;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.gfamily.work.gui.ControlPanelContainner;
import module.evolview.gfamily.work.gui.browser.BrowserPanel;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.io.Voice4geneFamilyBrowser;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

/**
 * <pre>
 * |-------------------------------------------------------------------|
 * |mainSplitPanel              |                                      |
 * |                            |                                      |
 * |LeftControlPanelContainer   |  rightSplitPane                      |
 * |                            |   |----------------------------------|
 * |                            |   |                             |    |
 * |                            |   | tabbedPhylogeneticTreePane  |    |
 * |                            |   |                             |    |
 * |                            |   | ----------------------------|    |
 * |                            |   |                             |    |
 * |                            |   | tabbedAnalysisPanel         |    |
 * |                            |   |                             |    |
 * |                            |   |-----------------------------|    |
 * |-------------------------------------------------------------------|
 *
 * </pre>
 *
 * @author yudalang
 */
@SuppressWarnings("serial")
public class GeneFamilyMainFace extends ModuleFace {

	protected GeneFamilyController controller;

	private JideTabbedPane tabbedPhylogeneticTreePane;
	private JideTabbedPane tabbedAnalysisPanel;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected JSplitPane mainSplitPane;
	protected ControlPanelContainner controlPanelContainner;

	private JSplitPane rightSplitPanel;

	private GradientColorProvider tabColorProvider = new GradientColorProvider() {
		Color TopBackGroundColor = new Color(196, 213, 231);
		@Override
		public Color getBackgroundAt(int tabIndex) {
			return Color.white;
		}

		@Override
		public Color getForegroundAt(int tabIndex) {
			return Color.black;
		}

		@Override
		public float getGradientRatio(int tabIndex) {
			return (float) 0.2;
		}

		@Override
		public Color getTopBackgroundAt(int tabIndex) {
			return TopBackGroundColor;
		}

	};

	private Voice4geneFamilyBrowser voicmDataHandler = new Voice4geneFamilyBrowser(this);

	protected GeneFamilyMainFace(IModuleLoader moduleLoader) {
		super(moduleLoader);

	}

	protected String getSavedPersistentPath() {
		final String PERSISTENT_STORE_PATH = EGPSProperties.JSON_DIR.concat("/geneFamilyFace.json");
		return PERSISTENT_STORE_PATH;
	}

	@Override
	public boolean closeTab() {

		controlPanelContainner.actionForSaveFile();

		try {
			GeneFamilyMainFaceBean bean = new GeneFamilyMainFaceBean();
			bean.setSavedLocationOfMainSplitPanel(mainSplitPane.getDividerLocation());
			double aa = rightSplitPanel.getDividerLocation() / (double) getHeight();
			bean.setSavedPerOfTreeAndBrowser(aa);
			EGPSObjectsUtil.persistentSaveJavaBeanByFastaJSON(bean, new File(getSavedPersistentPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void changeToThisTab() {

	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		/**
		 * 请帮我写一个界面，它的主体是一个 JDialog，它的布局是Border layout，Center部分是一个JTabbedPanel，纵向分布
		 * North部分是一个FlowLayout的Panel，包含有OK和Cancel按钮，这两个按钮分布在右下角
		 * 这个JTabbedPanel名字叫做jTabContainer，它第一个Tab叫做TreeTab，名称就是 "Phylogenetic tree".
		 * 第二个叫做BroserTab,名称叫做 "Browser tab"。
		 * 
		 * 在TreeTab这个面板中，有一个
		 */
		voicmDataHandler.doUserImportAction();
	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		PhylogeneticTreePanel selectedPhylogeneticTreePanel = getSelectedPhylogeneticTreePanel();

		if (selectedPhylogeneticTreePanel == null) {
			SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(), "Export error",
					"There are no data in the remnant,\nPlease input data first.");
		} else {
			int tabCount = tabbedAnalysisPanel.getTabCount();
			if (tabCount > 0) {
				BrowserPanel selectedBrowserPanel = getSelectedBrowserPanel();
				new SaveUtil().saveData(selectedBrowserPanel.getContextPanel(), getClass());
			}
		}
	}

	@Override
	public void initializeGraphics() {
		initializeTheModule();
		importData();
	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Gene family visualization" };
	}

	public GeneFamilyController getController() {
		return controller;
	}

	protected void initializeTheModule() {
		controller = new GeneFamilyController(this);

		mainSplitPane = getMainSplitPane();
		mainSplitPane.setBorder(null);

		controlPanelContainner = new ControlPanelContainner(controller);
		mainSplitPane.setLeftComponent(controlPanelContainner);

		getRightSplitPane();

		// ==== for persistant storage
		GeneFamilyMainFaceBean bean = null;
		File jsonFile = new File(getSavedPersistentPath());
		if (jsonFile.exists()) {
			try {
				bean = EGPSObjectsUtil.obtainJavaBeanByFastaJSON(GeneFamilyMainFaceBean.class, jsonFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (bean == null) {
			bean = new GeneFamilyMainFaceBean();
		}
		double savedPerOfTreeAndBrowser = bean.getSavedPerOfTreeAndBrowser();
		int location = (int) (getHeight() * savedPerOfTreeAndBrowser);
		rightSplitPanel.setDividerLocation(location);
		mainSplitPane.setDividerLocation(bean.getSavedLocationOfMainSplitPanel());
		// ====

		tabbedPhylogeneticTreePane = new JideTabbedPane();
		tabbedPhylogeneticTreePane.setTabShape(JideTabbedPane.SHAPE_OFFICE2003);
		tabbedPhylogeneticTreePane.setTabPlacement(JideTabbedPane.TOP);
		tabbedPhylogeneticTreePane.setTabColorProvider(tabColorProvider);
		tabbedPhylogeneticTreePane.setTabEditingAllowed(true);

		Font selectedTabTitleFont = controller.getTitleFont();
		tabbedPhylogeneticTreePane.setSelectedTabFont(selectedTabTitleFont);

		tabbedPhylogeneticTreePane.setShowCloseButtonOnTab(true);
		tabbedPhylogeneticTreePane.setBoldActiveTab(true);

//		tabbedPhylogeneticTreePane.setTabTrailingComponent(tabTrailingJPanel);

		rightSplitPanel.setLeftComponent(tabbedPhylogeneticTreePane);

		getTabbedAnalysisPanel();
		rightSplitPanel.setRightComponent(tabbedAnalysisPanel);

		mainSplitPane.setRightComponent(rightSplitPanel);
		add(mainSplitPane, BorderLayout.CENTER);

	}

	public JideTabbedPane getTabbedAnalysisPanel() {
		if (tabbedAnalysisPanel == null) {
			tabbedAnalysisPanel = new JideTabbedPane();
			tabbedAnalysisPanel.setTabShape(JideTabbedPane.SHAPE_OFFICE2003);
			tabbedAnalysisPanel.setTabPlacement(JideTabbedPane.TOP);
			tabbedAnalysisPanel.setTabColorProvider(tabColorProvider);
//			ColorProvider tabColorProvider = tabbedAnalysisPanel.getTabColorProvider();
			tabbedAnalysisPanel.setTabEditingAllowed(true);

			Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
//			tabbedAnalysisPanel.setSelectedTabFont(defaultTitleFont);
			tabbedAnalysisPanel.setFont(defaultTitleFont);
			tabbedAnalysisPanel.setBoldActiveTab(true);
		}

		return tabbedAnalysisPanel;
	}

	public JideTabbedPane getTabbedPhylogeneticTreePane() {
		return tabbedPhylogeneticTreePane;
	}

	/**
	 * 
	 * The main frame consist of following elements:
	 * 
	 * <pre>
	 * 		
	 * 首先是垂直上的构造
	 * 
	 * 
	 * Shell Panel
	 * 
	 * ControlPanel
	 *     |---------------------------------------|
	 *     |                         |             | TreePanel
	 *     |                         |             |
	 *     |                         |             |  
	 *     |                         |             |
	 *     |                         | ----------- |
	 *     |                         |             | BrowserPanel
	 *     |                         |             |
	 *     |                         |             |
	 *     |---------------------------------------|
	 * 
	 * 
	 * 
	 * </pre>
	 */
	public void initializeTheModule(RequiredGeneData geneData, TreeLayoutProperties treeLayoutProperties) {

		// 这个导入过程，如果更改需要注意三处：VOICM4MTV类的execute()；GeneFamilyMainFace类的initialize和MTreeViewMainFace的initializeGraphics()

		GraphicsNode root = treeLayoutProperties.getOriginalRootNode();
		PhylogeneticTreePanel phylogeneticTreePanel = new PhylogeneticTreePanel(treeLayoutProperties, root, null, null);
		controller.setGlobalPhylogeneticTreePanel(phylogeneticTreePanel);

		controller.setTreeLayoutProperties(treeLayoutProperties);

		ImageIcon imageIcon = IconObtainer.get("global.png");

		JScrollPane jScrollPane = new JScrollPane(phylogeneticTreePanel);
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		BrowserPanel ncov2019GenomeMain = new BrowserPanel(controller, geneData);

		int tabCount = tabbedAnalysisPanel.getTabCount();
		if (tabCount > 0) {
			tabbedPhylogeneticTreePane.removeAll();
			tabbedAnalysisPanel.removeAll();
		}
		tabbedPhylogeneticTreePane.addTab("Phylogeny", imageIcon, jScrollPane, "The gene family");
		tabbedAnalysisPanel.addTab("Genome browser", IconObtainer.get("dna.png"), ncov2019GenomeMain,
				"Track-view of SARS-CoV-2 genome structure, allele frequency, et al.");

		SwingUtilities.invokeLater(() -> {phylogeneticTreePanel.initializeLeftPanel();	});

		// ==========================================================================================================

	}

	protected JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setDividerSize(7);
			mainSplitPane.setOneTouchExpandable(true);
		}
		return mainSplitPane;
	}

	protected JSplitPane getRightSplitPane() {
		if (rightSplitPanel == null) {
			rightSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			rightSplitPanel.setDividerSize(10);
			rightSplitPanel.setOneTouchExpandable(true);
		}
		return rightSplitPanel;
	}

	public BrowserPanel getSelectedBrowserPanel() {
		Component selectedComponent = tabbedAnalysisPanel.getSelectedComponent();

		if (selectedComponent == null) {
			return null;
		}
		BrowserPanel phylogeneticTreePanel = (BrowserPanel) selectedComponent;
		return phylogeneticTreePanel;
	}

	public PhylogeneticTreePanel getSelectedPhylogeneticTreePanel() {
		if (tabbedPhylogeneticTreePane == null) {
			SwingDialog.showWarningMSGDialog("Not import data",
					"Sorry, you have not import data yet, Please load the phylogenetic tree.");
			return null;
		}

		Component selectedComponent = tabbedPhylogeneticTreePane.getSelectedComponent();
		if (selectedComponent == null) {
//			SwingDialog.showErrorMSGDialog("Internal error", "Please tell the developers: " + getClass());
			return null;
		}

		PhylogeneticTreePanel phylogeneticTreePanel = null;
		if (selectedComponent instanceof PhylogeneticTreePanel) {
			phylogeneticTreePanel = (PhylogeneticTreePanel) selectedComponent;
		} else if (selectedComponent instanceof JScrollPane) {
			JScrollPane comp = (JScrollPane) selectedComponent;
			phylogeneticTreePanel = (PhylogeneticTreePanel) comp.getViewport().getView();
		}

		return phylogeneticTreePanel;
	}

	public List<PhylogeneticTreePanel> getExistedPhylogeneticTreePanels() {
		List<PhylogeneticTreePanel> ret = new ArrayList<>();
		Component[] components = tabbedPhylogeneticTreePane.getComponents();

		for (Component component : components) {
			PhylogeneticTreePanel phylogeneticTreePanel = null;
			if (component instanceof PhylogeneticTreePanel) {
				phylogeneticTreePanel = (PhylogeneticTreePanel) component;
			} else if (component instanceof JScrollPane) {
				JScrollPane comp = (JScrollPane) component;
				phylogeneticTreePanel = (PhylogeneticTreePanel) comp.getViewport().getView();
			}

			if (phylogeneticTreePanel != null) {
				ret.add(phylogeneticTreePanel);
			}
		}
		return ret;

	}

	public PhylogeneticTreePanel getGlobalPhylogeneticTreePanel() {
		// JIDE TAB第一个不是你所要的
		Component[] components = tabbedPhylogeneticTreePane.getComponents();

		for (Component component : components) {
			if (component instanceof PhylogeneticTreePanel) {
				PhylogeneticTreePanel phylogeneticTreePanel = (PhylogeneticTreePanel) component;
				return phylogeneticTreePanel;
			}
		}

		return null;
	}

	public PhylogeneticTreePanel generateGlobalPhylogeneticTreePanel(GraphicsNode root) {

		PhylogeneticTreePanel phylogeneticTreePanel = new PhylogeneticTreePanel(controller.getTreeLayoutProperties(),
				root, null, null);
		return phylogeneticTreePanel;
	}

	public void showGlobalTreePanel() {
		if (tabbedPhylogeneticTreePane.getSelectedIndex() != 0) {
			tabbedPhylogeneticTreePane.setSelectedIndex(0);
		}
	}

	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The data is loading from the import dialog.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The phylogenetic tree with sequence structure is generated by the eGPS software.";
			}
		};
		return iInformation;
	}

}
