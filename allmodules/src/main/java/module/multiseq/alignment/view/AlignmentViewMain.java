package module.multiseq.alignment.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import module.multiseq.alignment.view.gui.AbstructSequenceColor;
import module.multiseq.alignment.view.gui.AlignmentViewContinuousRightPanel;
import module.multiseq.alignment.view.gui.AlignmentViewInterLeavedPanel;
import module.multiseq.alignment.view.gui.UserSelectedViewElement;
import module.multiseq.alignment.view.gui.VisulizationDataProperty;
import module.multiseq.alignment.view.gui.leftcontrol.AlignmentColorSchemeLeftPanel;
import module.multiseq.alignment.view.gui.leftcontrol.AlignmentLayoutLeftPanel;
import module.multiseq.alignment.view.gui.leftcontrol.LeftPanelMarker;
import module.multiseq.alignment.view.io.AlignmentSaver;
import module.multiseq.alignment.view.model.AlignmentDrawProperties;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import module.multiseq.alignment.view.model.SequenceLayout;
import egps2.modulei.AdjusterFillAndLine;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

/**
 * 
 * @ClassName AlignmentViewMain
 * 
 * @author mhl,ydl
 * 
 * @Date Created on:2019-07-25 13:10
 * 
 */
@SuppressWarnings("serial")
public class AlignmentViewMain extends ComputationalModuleFace implements AdjusterFillAndLine, IInformation {

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private JSplitPane mainSplitPane;

	private JPanel leftToolJPanel;
	private JPanel rightTabbedPanel;

	private AlignmentColorSchemeLeftPanel colorSchemePane;
	private JXTaskPane alignmentlayoutPane;
	private JXTaskPane analysisPanel;

	private VisulizationDataProperty alignmentViewPort;

	private AlignmentDrawProperties alignmentDrawProperties;
	private LeftPanelMarker leftMarkerPanel;

	private final String[] FEATURES = new String[] { "Tow layout exchange", "Color scheme rendering", "Mark sequence" };

	protected AlignmentViewMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

		alignmentDrawProperties = new AlignmentDrawProperties();

		setLayout(new BorderLayout());
		add(getMainSplitPane(), BorderLayout.CENTER);
	}

	public VisulizationDataProperty getAlignmentViewPort() {
		return alignmentViewPort;
	}

	public AlignmentDrawProperties getAlignmentDrawProperties() {

		return alignmentDrawProperties;
	}

	public void setAlignmentLayout() {

		String myLayout = getAlignmentDrawProperties().getMyLayout();

		JPanel tabbedPanel = getRightTabbedPanel();

		tabbedPanel.removeAll();

		if (myLayout.equalsIgnoreCase(SequenceLayout.CONTINUOUS)) {
			tabbedPanel.add(new AlignmentViewContinuousRightPanel(this));
		} else if (myLayout.equalsIgnoreCase(SequenceLayout.INTERLEAVED)) {
			tabbedPanel.add(new AlignmentViewInterLeavedPanel(this));
		}

		tabbedPanel.updateUI();
	}

	public Optional<AlignmentViewContinuousRightPanel> getAlignmentViewCharDrawingPanel4ContinuousLayout() {
		AlignmentViewContinuousRightPanel ret = null;

		JPanel tabbedPanel = getRightTabbedPanel();
		Component[] components = tabbedPanel.getComponents();
		for (Component component : components) {
			if (component instanceof AlignmentViewContinuousRightPanel) {
				ret = (AlignmentViewContinuousRightPanel) component;
				break;
			}
		}
		return Optional.ofNullable(ret);
	}

	public void setAlignmentViewPort(VisulizationDataProperty alignmentViewPort) {

		this.alignmentViewPort = alignmentViewPort;

		if (leftMarkerPanel != null) {
			List<String> sequenceNames = alignmentViewPort.getSequenceData().getSequenceNames();
			leftMarkerPanel.initializeSequenceNames(sequenceNames);
		}
	}

	private JSplitPane getMainSplitPane() {
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setContinuousLayout(false);
		mainSplitPane.setDividerSize(7);

		mainSplitPane.add(getLeftToolJPanel());
		mainSplitPane.add(getRightTabbedPanel());

		mainSplitPane.setBorder(null);
		return mainSplitPane;
	}

	public JPanel getRightTabbedPanel() {
		if (rightTabbedPanel == null) {
			rightTabbedPanel = new JPanel();
			rightTabbedPanel.setBackground(Color.WHITE);
			rightTabbedPanel.setLayout(new BorderLayout());
		}
		return rightTabbedPanel;
	}

	private JPanel getLeftToolJPanel() {

		if (leftToolJPanel == null) {

			leftToolJPanel = new JPanel(new BorderLayout());
			JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
			taskPaneContainer.setBackground(Color.white);
			taskPaneContainer.setBackgroundPainter(null);
			leftToolJPanel.setMinimumSize(new Dimension(250, 200));

			addJXTaskPanels(taskPaneContainer);

			leftToolJPanel.add(new JScrollPane(taskPaneContainer), BorderLayout.CENTER);
		}
		return leftToolJPanel;

	}

	protected void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {

		taskPaneContainer.add(getLayoutPane());
		taskPaneContainer.add(getColorSchemePane());
		// 加这个功能要改很多东西，怕出错！我想等以后按照Pipline为目的进行开发的时候加上去!
		JXTaskPane markPanel = getMarkPanel();
		markPanel.setCollapsed(true);
		taskPaneContainer.add(markPanel);
		// 因为无法选择参数，先去掉这个功能。
		// taskPaneContainer.add(getFurtherAnalysisJXTaskPane());
	}

	public JXTaskPane getFurtherAnalysisJXTaskPane() {
		if (analysisPanel == null) {
			analysisPanel = new JXTaskPane();
			analysisPanel.setTitle("Further analysis");
			analysisPanel.setFont(titleFont);
			analysisPanel.setCollapsed(true);

			JPanel displayJPane = new JPanel(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.anchor = GridBagConstraints.WEST;

			JButton builderTreeButton = new JButton("Build phylogenetic tree");
			builderTreeButton.setFont(defaultFont);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			displayJPane.add(builderTreeButton, gridBagConstraints);

			builderTreeButton.addActionListener(e -> {

				JPanel tabbedPanel = getRightTabbedPanel();

				if (tabbedPanel.getComponentCount() > 0) {

//					SequenceDataForAViewer sequenceData = getAlignmentViewPort().getSequenceData();
//
//					HashMap<String, String> findSettingValue = Preference.findSettingValue();
//					final String treeBuildMethod = findSettingValue
//							.get(new ConstantNameClass_TreeBuildMethod().label1_treeBuildMethod);
//					final String distMethod = findSettingValue
//							.get(new ConstantNameClass_EvolutionaryDistance().label1_modelOrMethod);
//
//					String tabName = "From " + ModuleNameCenter.ALIGNMENT_VIEWER_NAME + ":" + distMethod + ":"
//							+ treeBuildMethod;
//					PLMSA2PhyloTree plmsa2PhyloTree = new PLMSA2PhyloTree();
//					plmsa2PhyloTree.setSeqs(sequenceData.getSequences());
//					plmsa2PhyloTree.setSeq_names(sequenceData.getSequenceNames());
//					plmsa2PhyloTree.setTabName(tabName);
//					EGPSThreadUtil.quicklyRunThread(this, plmsa2PhyloTree);

				}

			});

			JButton calculateDistanceButton = new JButton("Calculate distance matrix");
			calculateDistanceButton.setFont(defaultFont);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			displayJPane.add(calculateDistanceButton, gridBagConstraints);
			calculateDistanceButton.addActionListener(e -> {

				JPanel tabbedPanel = getRightTabbedPanel();

				if (tabbedPanel.getComponentCount() > 0) {

//					final String distMethod = Preference
//							.findSettingValue(new ConstantNameClass_EvolutionaryDistance().label1_modelOrMethod);
//					String tabName = "From " + ModuleNameCenter.ALIGNMENT_VIEWER_NAME + ":" + distMethod;
//
//					SequenceDataForAViewer sequenceData = getAlignmentViewPort().getSequenceData();
//
//					PLMSA2DistMatrix plmsa2DistMatrix = new PLMSA2DistMatrix();
//					plmsa2DistMatrix.setSeqs(sequenceData.getSequences());
//					plmsa2DistMatrix.setSeq_names(sequenceData.getSequenceNames());
//					plmsa2DistMatrix.setTabName(tabName);
//					EGPSThreadUtil.quicklyRunThread(this, plmsa2DistMatrix);

				}

			});
			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(displayJPane, BorderLayout.WEST);

			analysisPanel.add(jPanel);
		}
		return analysisPanel;
	}

	public JXTaskPane getLayoutPane() {
		if (alignmentlayoutPane == null) {
			alignmentlayoutPane = new JXTaskPane();
			alignmentlayoutPane.setTitle("Layout");
			alignmentlayoutPane.setFont(titleFont);

			alignmentlayoutPane.add(new AlignmentLayoutLeftPanel(this));
		}

		return alignmentlayoutPane;
	}

	public JXTaskPane getColorSchemePane() {
		JXTaskPane jTaskPane = new JXTaskPane();
		jTaskPane.setTitle("Color scheme");
		jTaskPane.setFont(titleFont);

		colorSchemePane = new AlignmentColorSchemeLeftPanel(this);
		jTaskPane.add(colorSchemePane);
		return jTaskPane;
	}

	private JXTaskPane getMarkPanel() {

		JXTaskPane taskPane = new JXTaskPane();
		taskPane.setTitle("Mark");
		taskPane.setFont(titleFont);
		leftMarkerPanel = new LeftPanelMarker(this);
		taskPane.add(leftMarkerPanel);

		return taskPane;
	}

	@Override
	public boolean closeTab() {
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
		VOICM4AlignmentView inputViewPanel = new VOICM4AlignmentView(this);
		SwingDialog.QuickWrapperJCompWithDialog(inputViewPanel, "Data Import Dialog", 1000, 800);
	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		new AlignmentSaver(this).saveData(this);
	}

	@Override
	public String[] getFeatureNames() {
		return FEATURES;
	}

	public void invokeTheFeature(int featureType) {
		recordFeatureUsed4user(FEATURES[featureType]);
	}

	@Override
	protected void initializeGraphics() {
		Optional<IModuleLoader> moduleLoaderOpt = getModuleLoader();
		if (!moduleLoaderOpt.isPresent()) {
			throw new InputMismatchException("No remnant loader, please check by the developer.");
		}

		Launcher4ModuleLoader moduleLoader2 = (Launcher4ModuleLoader) moduleLoaderOpt.get();
		SequenceDataForAViewer sequenceDataForAViewer = moduleLoader2.getSequenceDataForAViewer();
		if (sequenceDataForAViewer == null) {
			importData();
		} else {
			loadingAlignmentDataToSeeView(sequenceDataForAViewer);
		}

	}

	private void loadingAlignmentDataToSeeView(SequenceDataForAViewer data) {
		VisulizationDataProperty alignmentViewPort = new VisulizationDataProperty(data);
		setAlignmentViewPort(alignmentViewPort);
		setAlignmentLayout();
	}

	@Override
	public Optional<Color> couldSetFillColor() {
		return Optional.empty();
	}

	@Override
	public Optional<Font> couldSetFont() {
		Font ret = null;
		if (getAlignmentViewPort() == null) {
			ret = getFont();
		} else {
			ret = getAlignmentViewPort().getFont();
		}

		return Optional.of(ret);
	}

	@Override
	public Optional<Color> couldSetLineColor() {
		return Optional.empty();
	}

	@Override
	public Optional<Integer> couldSetLineThickness() {
		return Optional.empty();
	}

	@Override
	public void adjustFillColor(Color newCol) {

	}

	@Override
	public void adjustFillFont(Font newFont) {
		if (getRightTabbedPanel().getComponentCount() > 0) {
			getAlignmentViewPort().setFont(newFont);
			repaint();
		}
	}

	@Override
	public void adjustLineColor(Color newCol) {

	}

	@Override
	public void adjustLineThickness(int newThickNess) {

	}

	// Information信息方法的开始
	/**
	 * 下面是ModuleInfo
	 */
	@Override
	public IInformation getModuleInfo() {
		return this;
	}

	@Override
	public String getHowModuleLaunch() {
		Optional<IModuleLoader> moduleLoaderOpt = getModuleLoader();
		if (!moduleLoaderOpt.isPresent()) {
			throw new InputMismatchException("No remnant loader, please check by the developer.");
		}

		Launcher4ModuleLoader moduleLoader2 = (Launcher4ModuleLoader) moduleLoaderOpt.get();
		return moduleLoader2.moduleLaunchWay;
	}

	@Override
	public String getWhatDataInvoked() {
		Optional<IModuleLoader> moduleLoaderOpt = getModuleLoader();
		if (!moduleLoaderOpt.isPresent()) {
			throw new InputMismatchException("No remnant loader, please check by the developer.");
		}

		Launcher4ModuleLoader moduleLoader2 = (Launcher4ModuleLoader) moduleLoaderOpt.get();
		return moduleLoader2.whatDataInvoked;
	}

	@Override
	public String getSummaryOfResults() {
		StringBuilder stringBuilder = new StringBuilder();
		AlignmentDrawProperties alignmentDrawProperties2 = getAlignmentDrawProperties();
		String myLayout = alignmentDrawProperties2.getMyLayout();
		stringBuilder.append("The eGPS desktop software was employed to visualize the alignmnet of *. The layout is ");
		stringBuilder.append(myLayout);
		
		AbstructSequenceColor sequenceBackgroundColor = alignmentDrawProperties2.getSequenceBackgroundColor();
		stringBuilder.append(". The rendering color scheme is ");
		stringBuilder.append(sequenceBackgroundColor.toString());
		stringBuilder.append(".");
		
		VisulizationDataProperty alignmentViewPort = this.getAlignmentViewPort();
		List<UserSelectedViewElement> selectionElements = alignmentViewPort.getSelectionElements();
		if (!selectionElements.isEmpty()) {
			stringBuilder.append("<br>Following characters are highlighted for the purpose of *:<br>");
			for (UserSelectedViewElement userSelectedViewElement : selectionElements) {
				stringBuilder.append(userSelectedViewElement.toString());
				stringBuilder.append(".");
				stringBuilder.append("<br>");
			}
		}
		
		return stringBuilder.toString();
	}
	// Information信息方法的结束
}
