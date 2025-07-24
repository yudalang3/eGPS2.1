package module.evoldist.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import com.jidesoft.swing.JideSplitPane;

import egps2.panels.dialog.EGPSJSpinner;
import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.math.matrix.MatrixTriangleOp;
import egps2.utils.common.model.datatransfer.ThreeTuple;
import egps.preferences.gui.PreferenceTree;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.MainFrameProperties;
import egps2.frame.MyFrame;
import module.evoltre.pipline.TreeParameterHandler;
import module.evoltrepipline.AbstractPrefShowContent;
import module.evoltrepipline.Panel4BiologyCodesPanel;
import module.evoltrepipline.Panel4EvolutionaryDistancePanel;
import module.evoltrepipline.SettingConstantsBuYaoChuCuo;
import module.heatmap.IndependentModuleLoader;
import egps2.modulei.IModuleLoader;

/**
 * 计算距离矩阵的图形面板的父类
 * 
 *
 */
public class DistanceMatrixParameterMain extends ComputationalModuleFace {
	private static final long serialVersionUID = 3205177869978767857L;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private JSplitPane mainSplitPane = null;
	private JPanel tabbedPanel = null;
	protected JPanel leftToolPane;

	protected JSpinner decimalJSpinner;
	protected JComboBox<String> colormodeStypeJComboBox;
	protected JComboBox<String> triangleStypeJComboBox;

	protected HashSet<AbstractPrefShowContent> treeShowContents = new HashSet<>();

	protected DefaultMutableTreeNode treeRoot = null;
	protected JTree jTree = null;

	protected JScrollPane contentView;

	protected SettingConstantsBuYaoChuCuo cc = new SettingConstantsBuYaoChuCuo();

	private EGPSDistanceMatrixJTableHolder jtable;

	protected List<File> inputFiles;

	public List<File> getInputFiles() {
		return inputFiles;
	}
	
	protected String[] moduleFeatures = {"Decimal adjustment", "Matrix place adjustment", "Color scheme"};

	public DistanceMatrixParameterMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

		setLayout(new BorderLayout());
		add(getMainSplitPane(), BorderLayout.CENTER);
	}

	private JSplitPane getMainSplitPane() {
		mainSplitPane = new JSplitPane(JideSplitPane.HORIZONTAL_SPLIT);

		mainSplitPane.setDividerSize(10);
		mainSplitPane.setDividerLocation(280);
		mainSplitPane.setLeftComponent(getLeftToolPane());
		mainSplitPane.setRightComponent(getTabbedPanel());
		mainSplitPane.setOneTouchExpandable(true);

		addLeftToolPanelListner();

		return mainSplitPane;
	}

	public void addMatrixPanel(EGPSDistanceMatrixJTableHolder jtable) {
		this.jtable = jtable;

		tabbedPanel = new JPanel();
		tabbedPanel.setLayout(new BorderLayout());

		tabbedPanel.add(jtable, BorderLayout.CENTER);

		mainSplitPane.setRightComponent(tabbedPanel);
		jtable.updateJtableAndLegendState();
	}

	private JPanel getLeftToolPane() {
		leftToolPane = new JPanel(new BorderLayout());

		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();

		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);

		addJXTaskPanels(taskPaneContainer);

		// leftToolPane.setMinimumSize(new Dimension(250, 200));

		// a scrollPane is needed to handle situation when JXTaskPanes extend original
		// area!
		leftToolPane.add(new JScrollPane(taskPaneContainer), BorderLayout.CENTER);

		return leftToolPane;
	}

	protected void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {
		taskPaneContainer.add(getDecimalJXTaskPane());
		taskPaneContainer.add(getDisplayJXTaskPane());
		taskPaneContainer.add(getSetParametersTaskPane());
		//这个先去掉了
//		taskPaneContainer.add(getFurtherAnalysisJXTaskPane());
		taskPaneContainer.add(getCalculateDistButton());
	}

	protected JButton getCalculateDistButton() {
		JButton calculateDistanceButton = new JButton("Calculate Distance");
		calculateDistanceButton.setFont(defaultFont);
		calculateDistanceButton.setEnabled(true);
		calculateDistanceButton.addActionListener(e -> {
			actionsForCalculateDistance();
		});
		return calculateDistanceButton;
	}

	public void setInputFiles(List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}
	protected void actionsForCalculateDistance() {
//		List<EGPSThreadRunnableTask> tasks = new ArrayList<>();
//
//		configurateInputFiles();
//
//		boolean checkFile = DataImportPanel_OneTypeOneFile.checkFile(inputFiles);
//		if (!checkFile) {
//			return;
//		}
//
//		if (inputFiles.size() > 1) {
//			int g = JOptionPane.showConfirmDialog(this, "Would you like to run with bench mode?", "Run mode?",
//					JOptionPane.YES_NO_OPTION);
//
//			if (g == JOptionPane.YES_OPTION) {
//				for (File file : inputFiles) {
//					String fileName = file.getName();
//					String tabName = "Genetic distance : " + fileName.substring(0, fileName.lastIndexOf("."));
//					PLMSAFile2DistMatrixBenchMode task = new PLMSAFile2DistMatrixBenchMode(file);
//					task.setTabName(tabName);
//					tasks.add(task);
//				}
//				EGPSThreadUtil.quicklyRunThread(this, tasks);
//				return;
//			}
//		}
//
//		PLMSAFile2DistMatrixOnce task = new PLMSAFile2DistMatrixOnce(inputFiles.get(0));
//		task.setDistanceMatrixViewMain(this);
//		tasks.add(task);
//
//		EGPSThreadUtil.quicklyRunThread(this, tasks);

	}

	protected JXTaskPane getSetParametersTaskPane() {
		JXTaskPane ret = new JXTaskPane();
		ret.setTitle("Set parameters");
		ret.setFont(titleFont);
		ret.setLayout(new BorderLayout());

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());

		JButton moreJbutton = new JButton("Advanced parameters");
		moreJbutton.setFont(defaultFont);
		jPanel.add(moreJbutton);
		moreJbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getParameterDialog();
			}
		});

		ret.add(jPanel, BorderLayout.CENTER);

		return ret;
	}

	private JDialog getParameterDialog() {

		MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();

		JDialog jDialog = new JDialog(instanceFrame, "Advanced parameters", true);

		Dimension dimension = new Dimension(700, 600);
		jDialog.setSize(dimension);
		jDialog.setLocationRelativeTo(instanceFrame);
		jDialog.setLayout(new BorderLayout());
		jDialog.add(getCenterPanel(), BorderLayout.CENTER);
		jDialog.add(getJBottomPanel(jDialog), BorderLayout.SOUTH);

		jDialog.setVisible(true);

		return jDialog;
	}

	protected JideSplitPane getCenterPanel() {

		Map<String, String> parameterMap = new TreeParameterHandler().getBuildTreeParametersMap();

		JideSplitPane splitPane = new JideSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDragResizable(true);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerSize(7);

		treeRoot = new DefaultMutableTreeNode("");
		jTree = new JTree(treeRoot);
		jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree.setCellRenderer(new PreferenceTree());

		Panel4EvolutionaryDistancePanel evolutionaryDistancePanel = new Panel4EvolutionaryDistancePanel(parameterMap,
				cc.dialog5_evolutionaryDistance);
		Panel4BiologyCodesPanel biologyCodesPanel = new Panel4BiologyCodesPanel(parameterMap, cc.dialog7_biologyCode);

		treeRoot.add(evolutionaryDistancePanel);
		treeRoot.add(biologyCodesPanel);

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


		JScrollPane optionView = new JScrollPane(jTree);
		optionView.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		contentView = new JScrollPane();
		contentView.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		jTree.setSelectionRow(0);
		contentView.setViewportView(evolutionaryDistancePanel.getViewJPanel());
		treeShowContents.add(evolutionaryDistancePanel);

		splitPane.add(optionView);
		splitPane.add(contentView);

		return splitPane;
	}

	protected void switchingOptions(JTree jTree) {

		AbstractPrefShowContent node = (AbstractPrefShowContent) jTree.getLastSelectedPathComponent();

		if (node == null) {
			return;
		}

		treeShowContents.add(node);
		contentView.setViewportView(node.getViewJPanel());
	}

	protected JPanel getJBottomPanel(JDialog jDialog) {

		JPanel jBottomPanel = new JPanel();
		jBottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton jButtonApply = new JButton("Apply and Close");
		JButton jButtonCancel = new JButton("Cancel");

		jButtonApply.setFont(defaultFont);
		jButtonCancel.setFont(defaultFont);

		jButtonApply.setFocusable(false);
		jButtonCancel.setFocusable(false);

		jBottomPanel.add(jButtonCancel);
		jBottomPanel.add(jButtonApply);

		jButtonApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkParameters()) {
					saveParameter();
					jDialog.dispose();
					treeShowContents.clear();
				}

			}
		});

		jButtonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jDialog.dispose();
				treeShowContents.clear();
			}
		});

		return jBottomPanel;
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

	public JPanel getTabbedPanel() {
		if (tabbedPanel == null) {
			tabbedPanel = new JPanel();
			tabbedPanel.setLayout(new BorderLayout());
			JPanel iniBlankPanel = new JPanel();
			iniBlankPanel.setBackground(Color.WHITE);
			tabbedPanel.add(iniBlankPanel);

		}
		return tabbedPanel;
	}

	public JComponent getViewPanel() {
		return jtable;
	}

	protected JXTaskPane getDecimalJXTaskPane() {

		JXTaskPane decimalJXTaskPane = new JXTaskPane();
		decimalJXTaskPane.setTitle("Decimal");
		decimalJXTaskPane.setFont(titleFont);
		JPanel decimalJPane = new JPanel(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		JLabel decimalJLabel = new JLabel();
		decimalJLabel.setText("Decimal places");
		decimalJLabel.setFont(defaultFont);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		decimalJPane.add(decimalJLabel, gridBagConstraints);
		int minValue = 0;
		int maxValue = 10;
		int currentValue = 5;
		int steps = 1;
		// SpinnerNumberModel nModel = new SpinnerNumberModel(currentValue, minValue,
		// maxValue, steps);
		decimalJSpinner = new EGPSJSpinner(currentValue, minValue, maxValue, steps);
		decimalJSpinner.setFont(defaultFont);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		decimalJPane.add(decimalJSpinner, gridBagConstraints);

		JLabel matrixJLabel = new JLabel();
		matrixJLabel.setText("Matrix");
		matrixJLabel.setFont(defaultFont);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		decimalJPane.add(matrixJLabel, gridBagConstraints);
		String[] listData = new String[] { "Lower Left", "Upper Right", "All" };
		triangleStypeJComboBox = new JComboBox<String>(listData);
		triangleStypeJComboBox.setFont(defaultFont);
		triangleStypeJComboBox.setSelectedIndex(0);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		decimalJPane.add(triangleStypeJComboBox, gridBagConstraints);
		decimalJXTaskPane.add(decimalJPane);
		return decimalJXTaskPane;
	}

	protected JXTaskPane getDisplayJXTaskPane() {
		JXTaskPane displayJXTaskPane = new JXTaskPane();
		displayJXTaskPane.setTitle("Display");
		displayJXTaskPane.setFont(titleFont);

		JPanel displayJPane = new JPanel(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;

		JLabel decimalJLabel = new JLabel();
		decimalJLabel.setText("Color Render ");
		decimalJLabel.setFont(defaultFont);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		displayJPane.add(decimalJLabel, gridBagConstraints);

		String[] listData = new String[] { "No color", "blue-red", "green-red", "white-red" };
		colormodeStypeJComboBox = new JComboBox<String>(listData);
		colormodeStypeJComboBox.setFont(defaultFont);
		colormodeStypeJComboBox.setSelectedIndex(3);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		displayJPane.add(colormodeStypeJComboBox, gridBagConstraints);
		displayJXTaskPane.add(displayJPane);
		return displayJXTaskPane;
	}

	protected JXTaskPane getFurtherAnalysisJXTaskPane() {
		JXTaskPane jxTaskPane = new JXTaskPane();
		jxTaskPane.setTitle("Further analysis");
		jxTaskPane.setFont(titleFont);
		jxTaskPane.setCollapsed(true);

		JPanel displayJPane = new JPanel(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.CENTER;

		JButton jButtonModifyDistance = new JButton("Beautify matrix");
		jButtonModifyDistance.setFont(defaultFont);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		displayJPane.add(jButtonModifyDistance, gridBagConstraints);

		jButtonModifyDistance.addActionListener(e -> {
			if (jtable == null) {
				showImportFileInformationDialog();
				return;
			}
			ThreeTuple<String[], String[], double[][]> element = new ThreeTuple<String[], String[], double[][]>(
					jtable.getRealColumnNames(), jtable.getRealColumnNames(),
					MatrixTriangleOp.downTriangle2full(jtable.getRealValueMatrix()));

			IndependentModuleLoader loader = new IndependentModuleLoader();
			loader.setElement(element);
			MainFrameProperties.loadTheModuleFromIModuleLoader(loader);
		});

		JButton jButtonBuildTree = new JButton("Build tree");
		jButtonBuildTree.setFont(defaultFont);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		displayJPane.add(jButtonBuildTree, gridBagConstraints);

		jButtonBuildTree.addActionListener(e -> {
			if (jtable == null) {
				showImportFileInformationDialog();
				return;
			}
			SwingUtilities.invokeLater(() -> {
//				DistBuildTreeParameter distMainPanel = new DistBuildTreeParameter() {
//
//					public void actionsForBuildTreeButton() {
//
//						String treeBuildMethod = saveGeneralParameters();
//						List<EGPSThreadRunnableTask> tasks = new ArrayList<>();
//						PLDistMatrix2PhyloTreeOnce task = new PLDistMatrix2PhyloTreeOnce(jtable.getRealColumnNames(),
//								jtable.getOrignalValueMatrix());
//						task.setDistToTreeMain(this);
//						tasks.add(task);
//
//						EGPSThreadUtil.quicklyRunThread(this, tasks);
//					}
//				};
//				distMainPanel.setShouldAnalysisButtonAlwaysEnable(true);
//
//				JideTabbedPane tabbedPane = bioMainFrame.getTabbedPane();
//				tabbedPane.add("Distance To Tree", distMainPanel);
//				tabbedPane.setSelectedComponent(distMainPanel);
//				tabbedPane.getSelectedComponent().requestFocus();
			});
		});

		jxTaskPane.add(displayJPane);
		return jxTaskPane;
	}

	private void showImportFileInformationDialog() {
		SwingDialog.showInfoMSGDialog("Information", "Please import distance matrix first!");
	}

	public void setModuleFont(Font font) {
		if (jtable == null) {
			return;
		}
		jtable.setFontFamilyAndFontSize(font);
		jtable.updateJtableAndLegendState();
	}


	public int getDecimalPlacesValue() {
		return (int) decimalJSpinner.getValue();
	}

	public int getMatrixIndex() {
		return triangleStypeJComboBox.getSelectedIndex();
	}

	public int getDisplayedColorModeIndex() {
		return colormodeStypeJComboBox.getSelectedIndex();
	}

	protected void addLeftToolPanelListner() {

		decimalJSpinner.addChangeListener(e -> {
			if (jtable != null) {
				Object value = decimalJSpinner.getValue();
				jtable.setNumberAfterTheDecimalPoint((int) value);
				jtable.updateJtableAndLegendState();
				recordFeatureUsed4user(moduleFeatures[0]);
			}
		});
		triangleStypeJComboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (jtable != null) {
					int selectedIndex = triangleStypeJComboBox.getSelectedIndex();
					jtable.setDispalyedLayoutStyle(selectedIndex);
					jtable.updateJtableAndLegendState();
					recordFeatureUsed4user(moduleFeatures[1]);
				}
			}
		});

		colormodeStypeJComboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (jtable != null) {
					int selectedIndex = colormodeStypeJComboBox.getSelectedIndex();
					jtable.setDisplayedCorlorMode(selectedIndex);
					jtable.updateJtableAndLegendState();
					
					recordFeatureUsed4user(moduleFeatures[2]);
				}
			}
		});

	}

	public void setJtable(EGPSDistanceMatrixJTableHolder jtable) {
		this.jtable = jtable;
	}

//	public void createHistory(Map<String, String> infor, String filePathHeader) throws Exception {
//		String readmeName = filePathHeader + "/readme.txt";
//		if (EGPSFileUtil.createFile(readmeName)) {
//			((HistoryTreePanel) bioMainFrame.getHistoryTreePane()).upHistoryPane();
//		}
//		StringBuilder sBuilder = new StringBuilder();
//		for (Map.Entry<String, String> entry : infor.entrySet()) {
//			sBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
//		}
//		ToolTipFactory.getInstance().createToolTipFile(readmeName, sBuilder.toString());
//		new SaveDistanceMatrix().saveAsJTableToDist(new File(filePathHeader + "/dist.dist"),
//				jtable.getRealColumnNames(), jtable.getRealValueMatrix());
//
//		HistoryTreePanel panel = (HistoryTreePanel) BioMainFrame.getInstance().getHistoryTreePane();
//		panel.upHistoryPane();
//	}

	public void openGesture() {
		setEnabled(false);
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	public void closeGesture() {
		setEnabled(true);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public Font getCurrentFontStatus() {
		if (jtable == null) {
			return getFont();
		}
		return jtable.getjTableFont();
	}

	@Override
	public boolean canImport() {
		return false;
	}

	@Override
	public void importData() {

	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		jtable.saveViewPanelAs();
	}

	@Override
	public String[] getFeatureNames() {
		return moduleFeatures;
	}

	@Override
	protected void initializeGraphics() {

	}

}
