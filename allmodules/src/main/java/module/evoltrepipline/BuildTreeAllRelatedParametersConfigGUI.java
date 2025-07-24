package module.evoltrepipline;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import egps.preferences.gui.PreferenceTree;
import egps2.UnifiedAccessPoint;
import module.evoltre.pipline.TreeParameterHandler;

/**
 * 这是图形界面设置参数的类
 */
public class BuildTreeAllRelatedParametersConfigGUI implements ActionListener {

	private DefaultMutableTreeNode treeRoot = null;
	private JTree jTree = null;

	private JScrollPane contentView;

	protected HashSet<AbstractPrefShowContent> treeShowContents = new HashSet<>();

	private SettingConstantsBuYaoChuCuo cc = new SettingConstantsBuYaoChuCuo();
	protected ConstantNameClass_TreeBuildMethod cctm = new ConstantNameClass_TreeBuildMethod();

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private JPanel jBottomPanel;

	public BuildTreeAllRelatedParametersConfigGUI() {

	}

	public JComponent getCenterPanel4all() {
		return getCenterPanel(true, true, true, true, true, true, true, true);
	}

	public JComponent getCenterPanel4gene2msa() {
		return getCenterPanel(true, false, false, false, false, true, false, true);
	}
	public JComponent getCenterPanel4gene2geneTree() {
		return getCenterPanel(true, true, true, true, false, true, false, true);
	}
	
	public JComponent getCenterPanel4buildTreeFromMSA() {
		return getCenterPanel(false, true, true, true, false, false, false, false);
	}
	public JComponent getCenterPanel4buildTreeFromDist() {
		return getCenterPanel(false, false, true, true, false, false, false, false);
	}
	
	public JComponent getCenterPanel4buildTreeFromMAF() {
		return getCenterPanel(false, true, true, true, false, false, true, false);
	}

	public JSplitPane getCenterPanel(boolean webQueryString, boolean evolDist, boolean distTreeBuild, boolean bolCode,
			boolean gBrowser, boolean webRes, boolean gAna, boolean webCon) {
		Map<String, String> parameterMap = new TreeParameterHandler().getBuildTreeParametersMap();

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(null);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerSize(7);

		treeRoot = new DefaultMutableTreeNode("");
		jTree = new JTree(treeRoot);
		jTree.setBackground(Color.white);
		jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree.setCellRenderer(new PreferenceTree());

		Panel4EvolutionaryDistancePanel evolutionaryDistancePanel = new Panel4EvolutionaryDistancePanel(parameterMap,
				cc.dialog5_evolutionaryDistance);
		Panel4BiologyCodesPanel biologyCodesPanel = new Panel4BiologyCodesPanel(parameterMap, cc.dialog7_biologyCode);
		Panel4WebResources webResources = new Panel4WebResources(parameterMap, cc.dialog3_speciesSet);

		Panel4GenomeBrowser genomeBrowser = new Panel4GenomeBrowser(parameterMap, cc.dialog1_genomeBrowser);
		Panel4GenomicAnalysisPanel genomicAnalysisPanel = new Panel4GenomicAnalysisPanel(parameterMap,
				cc.dialog4_indel);
		Panel4WebConnection webConnection = new Panel4WebConnection(parameterMap, cc.dialog2_connection);

		Panel4WebQueryString panel4WebQueryString = new Panel4WebQueryString(parameterMap, cc.dialog8_webQueryString);
		Panel4DistTreeBuildMethod panel4DistTreeBuildMethod = new Panel4DistTreeBuildMethod(parameterMap,
				cc.dialog6_treeBuildMethod);

		if (webQueryString) {
			treeRoot.add(panel4WebQueryString);
		}
		if (evolDist) {
			treeRoot.add(evolutionaryDistancePanel);
		}
		if (distTreeBuild) {
			treeRoot.add(panel4DistTreeBuildMethod);
		}
		if (bolCode) {
			treeRoot.add(biologyCodesPanel);
		}
		if (gBrowser) {
			treeRoot.add(genomeBrowser);
		}
		if (webRes) {
			treeRoot.add(webResources);
		}
		if (gAna) {
			treeRoot.add(genomicAnalysisPanel);
		}
		if (webCon) {
			treeRoot.add(webConnection);
		}

		jTree.expandRow(0);
		jTree.setRootVisible(false);
		jTree.setFont(defaultFont);
		jTree.setRowHeight(25);

		jTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				switchingOptions(jTree);
			}
		});

		jTree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jTree.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});

		contentView = new JScrollPane();
		contentView.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		jTree.setSelectionRow(0);

		// 这里是初始界面的panel
		switchingOptions(jTree);

		JScrollPane optionView = new JScrollPane(jTree);
		splitPane.setLeftComponent(optionView);

		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(contentView, BorderLayout.CENTER);
		jPanel.add(getJBottomPanel(), BorderLayout.SOUTH);
		jPanel.setBorder(BorderFactory.createEmptyBorder());

		splitPane.setRightComponent(jPanel);

		return splitPane;
	}

	private JPanel getJBottomPanel() {
		if (jBottomPanel == null) {
			jBottomPanel = new JPanel();
			jBottomPanel.setBorder(BorderFactory.createEmptyBorder());
			jBottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			configurateJBottomPanel(jBottomPanel);
		}

		return jBottomPanel;
	}

	protected void configurateJBottomPanel(JPanel jBottomPanel2) {

	}

	private void switchingOptions(JTree jTree) {

		AbstractPrefShowContent node = (AbstractPrefShowContent) jTree.getLastSelectedPathComponent();

		if (node == null) {
			return;
		}

		treeShowContents.add(node);

		contentView.setViewportView(node.getViewJPanel());
	}

	protected void saveParameter() {
		TreeParameterHandler treeParameterHandler = new TreeParameterHandler();
		Map<String, String> parameterMap = treeParameterHandler.getBuildTreeParametersMap();
		for (AbstractPrefShowContent abstructPrefShowContent : treeShowContents) {
			abstructPrefShowContent.saveParameter(parameterMap);
		}
		treeParameterHandler.saveBuildTreeParametersMap(parameterMap);

	}

	protected boolean checkParameters() {
		for (AbstractPrefShowContent abstructPrefShowContent : treeShowContents) {
			boolean checkParameters = abstructPrefShowContent.checkParameters(jTree);
			if (!checkParameters) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (checkParameters()) {
			saveParameter();
			treeShowContents.clear();
		}
	}



}
