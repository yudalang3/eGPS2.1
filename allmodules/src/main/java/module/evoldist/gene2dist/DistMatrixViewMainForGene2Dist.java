package module.evoldist.gene2dist;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import com.jidesoft.swing.JideSplitPane;

import egps.preferences.gui.PreferenceTree;
import module.evoldist.view.gui.DistanceMatrixParameterMain;
import module.evoltre.pipline.TreeParameterHandler;
import module.evoltrepipline.Panel4BiologyCodesPanel;
import module.evoltrepipline.Panel4EvolutionaryDistancePanel;
import module.evoltrepipline.Panel4WebResources;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

/**
 * 从基因名到算距离矩阵的面板
 * 
 * @ClassName: GeneToGeneticDistViewMain.java
 * 
 * @Package: egps.remnant.geneToGeneticDist
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-03-19 17:04
 * 
 */
public class DistMatrixViewMainForGene2Dist extends DistanceMatrixParameterMain {

	private static final long serialVersionUID = -1319892114519621197L;

	private GridBagConstraints gridBagConstraints;
	private JXTaskPane geneSpeciesSetJXTaskPane;
	private JPanel geneSpeciesSetJPane;
	private JButton buildGeneticDistancesJButton;

	private JRadioButton geneSymbolJRadioButton;
	private JRadioButton chrRegionJRadioButton;
	private JTextField geneSymbolJTextField;
	private JTextField chrRegionJTextField;

	public DistMatrixViewMainForGene2Dist(IModuleLoader moduleLoader) {
		super(moduleLoader);
	}

	@Override
	protected void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {
		taskPaneContainer.add(getGeneSpeciesSetJPanel());

		taskPaneContainer.add(getDecimalJXTaskPane());
		taskPaneContainer.add(getDisplayJXTaskPane());
		taskPaneContainer.add(getFurtherAnalysisJXTaskPane());

		taskPaneContainer.add(getSetParametersTaskPane());
		taskPaneContainer.add(getBuildGeneticDistancesJButton());
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
		Panel4WebResources webResources = new Panel4WebResources(parameterMap, cc.dialog3_speciesSet);

		treeRoot.add(evolutionaryDistancePanel);
		treeRoot.add(biologyCodesPanel);
		treeRoot.add(webResources);

		jTree.expandRow(0);
		jTree.setRootVisible(false);
		jTree.setFont(defaultFont);
		jTree.setRowHeight(25);
		jTree.setBackground(new Color(240, 240, 240));

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

	@Override
	protected void addLeftToolPanelListner() {
		super.addLeftToolPanelListner();

		buildGeneticDistancesJButton.addActionListener(e -> {
			int decimalPlacesValue = (int) decimalJSpinner.getValue();
			String userEnteredString = null;
			HashMap<String, String> settingMap = (HashMap<String, String>) new TreeParameterHandler().getBuildTreeParametersMap();
			PLWeb2GeneticDist geneToGeneTreeAndJump = null;
			if (geneSymbolJRadioButton.isSelected()) {
				userEnteredString = geneSymbolJTextField.getText();
				geneToGeneTreeAndJump = new PLWeb2GeneticDist(settingMap, userEnteredString);

			} else {
				userEnteredString = chrRegionJTextField.getText();
				geneToGeneTreeAndJump = new PLWeb2GeneticDist(settingMap, userEnteredString, true);

			}
			geneToGeneTreeAndJump.setGeneToGeneticDistMain(this);
			registerRunningTask(geneToGeneTreeAndJump);

		});

		geneSymbolJRadioButton.addActionListener(e -> {
			if (geneSymbolJRadioButton.isSelected()) {
				geneSymbolJTextField.setEnabled(true);
				if ("".equals(geneSymbolJTextField.getText().trim())) {
					buildGeneticDistancesJButton.setEnabled(false);
				} else {
					buildGeneticDistancesJButton.setEnabled(true);
				}
				chrRegionJTextField.setEnabled(false);
			}

		});
		chrRegionJRadioButton.addActionListener(e -> {
			if (chrRegionJRadioButton.isSelected()) {
				chrRegionJTextField.setEnabled(true);
				geneSymbolJTextField.setEnabled(false);
				if ("".equals(chrRegionJTextField.getText().trim())) {
					buildGeneticDistancesJButton.setEnabled(false);
				} else {
					buildGeneticDistancesJButton.setEnabled(true);
				}
			}
		});

		chrRegionJRadioButton.addMouseListener(new GeneToGeneticDistMouseAdapter());
		geneSymbolJRadioButton.addMouseListener(new GeneToGeneticDistMouseAdapter());
		chrRegionJTextField.getDocument().addDocumentListener(new GeneToGeneticDistDocumentListener());
		geneSymbolJTextField.getDocument().addDocumentListener(new GeneToGeneticDistDocumentListener());
	}

	private JXTaskPane getGeneSpeciesSetJPanel() {
		if (geneSpeciesSetJXTaskPane == null) {
			geneSpeciesSetJXTaskPane = new JXTaskPane();
			geneSpeciesSetJXTaskPane.setTitle("Gene & species set");
			geneSpeciesSetJXTaskPane.setFont(titleFont);
			geneSymbolJRadioButton = new JRadioButton("Gene symbol");
			geneSymbolJRadioButton.setFont(defaultFont);
			geneSymbolJRadioButton.setSelected(true);
			geneSymbolJRadioButton.setToolTipText("TP53");
			geneSymbolJTextField = new JTextField(9);
			geneSymbolJTextField.setFont(defaultFont);
			chrRegionJRadioButton = new JRadioButton("chr-region");
			chrRegionJRadioButton.setFont(defaultFont);
			chrRegionJRadioButton.setToolTipText("17:7,661,779-7,687,550");
			chrRegionJTextField = new JTextField(9);
			chrRegionJTextField.setFont(defaultFont);
			chrRegionJTextField.setEnabled(false);
			geneSymbolJRadioButton.setFocusPainted(false);
			chrRegionJRadioButton.setFocusPainted(false);
			ButtonGroup ignoredButtonGroup = new ButtonGroup();
			ignoredButtonGroup.add(geneSymbolJRadioButton);
			ignoredButtonGroup.add(chrRegionJRadioButton);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(3, 0, 3, 0);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			JPanel jRadioPane = new JPanel(new GridBagLayout());
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jRadioPane.add(geneSymbolJRadioButton, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			jRadioPane.add(geneSymbolJTextField, gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			jRadioPane.add(chrRegionJRadioButton, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			jRadioPane.add(chrRegionJTextField, gridBagConstraints);
			JPanel jButtonPanel = new JPanel(new GridBagLayout());
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			geneSpeciesSetJPane = new JPanel(new GridBagLayout());
			geneSpeciesSetJPane.add(jRadioPane, gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			geneSpeciesSetJPane.add(jButtonPanel, gridBagConstraints);
			geneSpeciesSetJXTaskPane.add(geneSpeciesSetJPane);
		}
		return geneSpeciesSetJXTaskPane;
	}

	private JButton getBuildGeneticDistancesJButton() {
		if (buildGeneticDistancesJButton == null) {
			buildGeneticDistancesJButton = new JButton("Get genetic distances");
			buildGeneticDistancesJButton.setFont(defaultFont);
			buildGeneticDistancesJButton.setEnabled(false);
		}
		return buildGeneticDistancesJButton;
	}

	class GeneToGeneticDistDocumentListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			Document doc = e.getDocument();
			try {
				String s = doc.getText(0, doc.getLength()).trim();
				if (s.equals("") || "".equals(s)) {
					buildGeneticDistancesJButton.setEnabled(false);
				} else {
					buildGeneticDistancesJButton.setEnabled(true);
				}
			} catch (BadLocationException e1) {

				e1.printStackTrace();
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			Document doc = e.getDocument();
			try {
				String s = doc.getText(0, doc.getLength()).trim();
				if (s.equals("") || "".equals(s)) {
					buildGeneticDistancesJButton.setEnabled(false);
				} else {
					buildGeneticDistancesJButton.setEnabled(true);
				}

			} catch (BadLocationException e1) {

				e1.printStackTrace();
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {

		}
	}

	class GeneToGeneticDistMouseAdapter extends MouseAdapter {

		@Override
		public void mouseExited(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

	}

	@Override
	public void setEnabled(boolean enabled) {
		buildGeneticDistancesJButton.setEnabled(enabled);
		geneSymbolJRadioButton.setEnabled(enabled);
		chrRegionJRadioButton.setEnabled(enabled);
		decimalJSpinner.setEnabled(enabled);
		triangleStypeJComboBox.setEnabled(enabled);
		if (geneSymbolJRadioButton.isSelected()) {
			geneSymbolJTextField.setEnabled(enabled);
		} else {
			chrRegionJTextField.setEnabled(enabled);
		}

	}

	public void actionsBeforeProcess() throws Exception {
		setEnabled(false);
	}

	public void actionsAfterProcess(boolean isNatural) throws Exception {
		setEnabled(true);
	}

	@Override
	public void openGesture() {
		setEnabled(false);
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	@Override
	public void closeGesture() {
		setEnabled(true);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
				return "The functionality is powered by the eGPS software.";
			}
		};
		return iInformation;
	}
}
