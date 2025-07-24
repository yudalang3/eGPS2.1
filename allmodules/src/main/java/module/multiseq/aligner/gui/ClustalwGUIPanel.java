package module.multiseq.aligner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import com.jidesoft.swing.JideSplitPane;

import egps2.panels.dialog.EGPSJSpinner;
import egps2.frame.gui.comp.DataImportPanel_OneTypeOneFile;
import egps2.UnifiedAccessPoint;
import module.multiseq.aligner.MultipleSeqAlignerMain;

public class ClustalwGUIPanel extends AbstractAlignmentPanel {
	private static final long serialVersionUID = -2908588357598487831L;
	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
	private JSplitPane mainSplitPane;
	private JPanel leftToolPane;
	private GridBagConstraints gridBagConstraints;

	private JXTaskPane pairwiseAlignmentJXTaskPane;
	private JLabel pairwiseGapOpenPenaltyJLabel;
	private JSpinner pairwiseGapOpenPenaltyJSpinner;

	private JLabel pairwiseGapExtensionPenaltyJLabel;
	private JSpinner pairwiseGapExtensionPenaltyJSpinner;

	private JLabel pairwiseDnaWeightMatrixJLabel;
	private JComboBox<String> pairwiseDnaWeightMatrixJComboBox;

	private JXTaskPane multipleAlignmentOptionsJXTaskPane;

	private JLabel multipleGapOpenPenaltyJLabel;
	private JSpinner multipleGapOpenPenaltyJSpinner;

	private JLabel multipleGapExtensionPenaltyJLabel;
	private JSpinner multipleGapExtensionPenaltyJSpinner;
	private JLabel multipleDelayDivergentSequencesJLabel;
	private JSpinner multipleDelayDivergentSequencesJSpinner;
	private JLabel multipleDnaWeightMatrixJLabel;
	private JComboBox<String> multipleDnaWeightMatrixJLabelDnaWeightMatrixJComboBox;
	private JLabel multipleDnatransitionJLabel;
	private JSpinner multipleDnatransitionJSpinner;
	private JCheckBox multipleKeepOriginalJCheckBox;
	private JCheckBox multipleUserTreeJCheckBox;
	private JButton alignSequencesJButton;
	private JTextField multipleUserTreeJTextArea;
	private JButton multipleUserTreeJButton;

	private List<File> inputFiles;

	public ClustalwGUIPanel(MultipleSeqAlignerMain alignmentMain) {
		super(alignmentMain);
		setLayout(new BorderLayout());
		add(getMainSplitPane());
	}

	public void setInputFiles(List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}

	private JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setDividerSize(0);
			mainSplitPane.setDividerLocation(500);
			mainSplitPane.setBorder(null);
			mainSplitPane.add(getLeftToolPane());
			JPanel jPanel = new JPanel();
			jPanel.setBackground(Color.WHITE);
			mainSplitPane.add(jPanel);
		}
		return mainSplitPane;
	}

	private JPanel getLeftToolPane() {
		if (leftToolPane == null) {
			leftToolPane = new JPanel(new BorderLayout());
			// leftToolPane.set
			JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
			taskPaneContainer.setBackground(Color.WHITE);
			taskPaneContainer.setBackgroundPainter(null);
			
			configTaskPanes(taskPaneContainer);
			leftToolPane.setMinimumSize(new Dimension(500, 200));
			leftToolPane.add(new JScrollPane(taskPaneContainer), BorderLayout.CENTER);
		}
		return leftToolPane;
	}
	
	protected void configTaskPanes(JXTaskPaneContainer taskPaneContainer) {
		taskPaneContainer.add(getPairwiseAlignmentOptionsJPanel());
		taskPaneContainer.add(getMultipleAlignmentOptionsJPanel());
		taskPaneContainer.add(getAlignSequencesJButton());
	}

	private JXTaskPane getPairwiseAlignmentOptionsJPanel() {
		if (pairwiseAlignmentJXTaskPane == null) {
			pairwiseAlignmentJXTaskPane = new JXTaskPane();
			pairwiseAlignmentJXTaskPane.setTitle("Pair-wise alignment options");
			pairwiseAlignmentJXTaskPane.setFont(titleFont);

			pairwiseGapOpenPenaltyJLabel = new JLabel("  Gap open penalty(6 to 20)");
			pairwiseGapOpenPenaltyJLabel.setFont(defaultFont);
			// gapOpeningPenalty.setToolTipText("TP53");
			int gapOpenPenaltyJSpinnerMinValue = 6;
			int gapOpenPenaltyJSpinnerMaxValue = 20;
			int gapOpenPenaltyJSpinnerCurrentValue = 12;
			int gapOpenPenaltyJSpinnerSteps = 1;
			pairwiseGapOpenPenaltyJSpinner = new EGPSJSpinner(gapOpenPenaltyJSpinnerCurrentValue,
					gapOpenPenaltyJSpinnerMinValue, gapOpenPenaltyJSpinnerMaxValue, gapOpenPenaltyJSpinnerSteps);
			pairwiseGapOpenPenaltyJSpinner.setFont(defaultFont);

			pairwiseGapExtensionPenaltyJLabel = new JLabel("  Gap extension penalty(1 to 5)");
			pairwiseGapExtensionPenaltyJLabel.setFont(defaultFont);

			// gapExtensionPenaltyJLabel.setToolTipText("17:7,661,779-7,687,550");
			int gapExtensionPenaltyJSpinnerMinValue = 1;
			int gapExtensionPenaltyJSpinnerMaxValue = 5;
			int gapExtensionPenaltyJSpinnerCurrentValue = 5;
			int gapExtensionPenaltyJSpinnerSteps = 1;
			pairwiseGapExtensionPenaltyJSpinner = new EGPSJSpinner(gapExtensionPenaltyJSpinnerCurrentValue,
					gapExtensionPenaltyJSpinnerMinValue, gapExtensionPenaltyJSpinnerMaxValue,
					gapExtensionPenaltyJSpinnerSteps);
			pairwiseGapExtensionPenaltyJSpinner.setFont(defaultFont);

			pairwiseDnaWeightMatrixJLabel = new JLabel("  DNA weight matrix");
			pairwiseDnaWeightMatrixJLabel.setFont(defaultFont);

			String[] listData = { "IUB", "CLUSTALW" };
			pairwiseDnaWeightMatrixJComboBox = new JComboBox<String>(listData);
			pairwiseDnaWeightMatrixJComboBox.setFont(defaultFont);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(3, 0, 3, 0);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			// gridBagConstraints.fill = GridBagConstraints.WEST;
			gridBagConstraints.weightx = 1.0;
			JPanel pairwiseAlignmentOptionsJPane = new JPanel(new GridBagLayout());
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			pairwiseAlignmentOptionsJPane.add(pairwiseGapOpenPenaltyJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			pairwiseAlignmentOptionsJPane.add(pairwiseGapOpenPenaltyJSpinner, gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			pairwiseAlignmentOptionsJPane.add(pairwiseGapExtensionPenaltyJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			pairwiseAlignmentOptionsJPane.add(pairwiseGapExtensionPenaltyJSpinner, gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			pairwiseAlignmentOptionsJPane.add(pairwiseDnaWeightMatrixJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			pairwiseAlignmentOptionsJPane.add(pairwiseDnaWeightMatrixJComboBox, gridBagConstraints);

			pairwiseAlignmentJXTaskPane.add(pairwiseAlignmentOptionsJPane);
		}
		return pairwiseAlignmentJXTaskPane;
	}

	private JXTaskPane getMultipleAlignmentOptionsJPanel() {
		if (multipleAlignmentOptionsJXTaskPane == null) {
			multipleAlignmentOptionsJXTaskPane = new JXTaskPane();
			multipleAlignmentOptionsJXTaskPane.setTitle("Multiple alignment options");
			multipleAlignmentOptionsJXTaskPane.setFont(titleFont);

			multipleGapOpenPenaltyJLabel = new JLabel("  Gap open penalty(6 to 20)");
			multipleGapOpenPenaltyJLabel.setFont(defaultFont);

			int multipleGapOpenPenaltyJSpinnerMinValue = 6;
			int multipleGapOpenPenaltyJSpinnerMaxValue = 20;
			int multipleGapOpenPenaltyJSpinnerCurrentValue = 12;
			int multipleGapOpenPenaltyJSpinnerSteps = 1;
			multipleGapOpenPenaltyJSpinner = new EGPSJSpinner(multipleGapOpenPenaltyJSpinnerCurrentValue,
					multipleGapOpenPenaltyJSpinnerMinValue, multipleGapOpenPenaltyJSpinnerMaxValue,
					multipleGapOpenPenaltyJSpinnerSteps);
			multipleGapOpenPenaltyJSpinner.setFont(defaultFont);

			multipleGapExtensionPenaltyJLabel = new JLabel("  Gap extension penalty(1 to 5)");
			multipleGapExtensionPenaltyJLabel.setFont(defaultFont);

			int multipleGapExtensionPenaltyJSpinnerMinValue = 1;
			int multipleGapExtensionPenaltyJSpinnerMaxValue = 5;
			int multipleGapExtensionPenaltyJSpinnerCurrentValue = 5;
			int multipleGapExtensionPenaltyJSpinnerSteps = 1;
			multipleGapExtensionPenaltyJSpinner = new EGPSJSpinner(multipleGapExtensionPenaltyJSpinnerCurrentValue,
					multipleGapExtensionPenaltyJSpinnerMinValue, multipleGapExtensionPenaltyJSpinnerMaxValue,
					multipleGapExtensionPenaltyJSpinnerSteps);
			multipleGapExtensionPenaltyJSpinner.setFont(defaultFont);

			multipleDelayDivergentSequencesJLabel = new JLabel("  Delay divergent sequences(0 to 100)");
			multipleDelayDivergentSequencesJLabel.setFont(defaultFont);

			int multipleDelayDivergentSequencesJSpinnerMinValue = 0;
			int multipleDelayDivergentSequencesJSpinnerMaxValue = 100;
			int multipleDelayDivergentSequencesJSpinnerCurrentValue = 30;
			int multipleDelayDivergentSequencesJSpinnerSteps = 1;
			multipleDelayDivergentSequencesJSpinner = new EGPSJSpinner(
					multipleDelayDivergentSequencesJSpinnerCurrentValue,
					multipleDelayDivergentSequencesJSpinnerMinValue, multipleDelayDivergentSequencesJSpinnerMaxValue,
					multipleDelayDivergentSequencesJSpinnerSteps);
			multipleDelayDivergentSequencesJSpinner.setFont(defaultFont);

			multipleDnaWeightMatrixJLabel = new JLabel("  DNA weight matrix");
			multipleDnaWeightMatrixJLabel.setFont(defaultFont);

			String[] listData = { "IUB", "CLUSTALW" };
			multipleDnaWeightMatrixJLabelDnaWeightMatrixJComboBox = new JComboBox<String>(listData);
			multipleDnaWeightMatrixJLabelDnaWeightMatrixJComboBox.setFont(defaultFont);

			multipleDnatransitionJLabel = new JLabel("  DNA transition(0 to 1)");
			multipleDnatransitionJLabel.setFont(defaultFont);

			int multipleDnatransitionJSpinnerMinValue = 0;
			int multipleDnatransitionJSpinnerMaxValue = 1;
			int multipleDnatransitionJSpinnerCurrentValue = 1;
			int multipleDnatransitionJSpinnerSteps = 1;
			multipleDnatransitionJSpinner = new EGPSJSpinner(multipleDnatransitionJSpinnerCurrentValue,
					multipleDnatransitionJSpinnerMinValue, multipleDnatransitionJSpinnerMaxValue,
					multipleDnatransitionJSpinnerSteps);
			multipleDnatransitionJSpinner.setFont(defaultFont);

			multipleKeepOriginalJCheckBox = new JCheckBox("Keep original sequence order");
			multipleKeepOriginalJCheckBox.setFont(defaultFont);

			multipleUserTreeJCheckBox = new JCheckBox("User tree");
			multipleUserTreeJCheckBox.setFont(defaultFont);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(3, 0, 3, 0);
			gridBagConstraints.anchor = GridBagConstraints.WEST;

			JPanel multipleUserTreeJPanel = new JPanel(new GridBagLayout());

			multipleUserTreeJTextArea = new JTextField(15);
			multipleUserTreeJTextArea.setFont(defaultFont);
			// multipleUserTreeJTextArea.setEnabled(false);
			multipleUserTreeJTextArea.setEditable(false);

			multipleUserTreeJButton = new JButton("Set");
			multipleUserTreeJButton.setFont(defaultFont);
			multipleUserTreeJButton.setEnabled(false);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			multipleUserTreeJPanel.add(multipleUserTreeJTextArea, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			multipleUserTreeJPanel.add(multipleUserTreeJButton, gridBagConstraints);

			JPanel multipleUserTree = new JPanel(new GridBagLayout());

			gridBagConstraints.insets = new Insets(3, 0, 3, 10);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			multipleUserTree.add(multipleUserTreeJCheckBox, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			multipleUserTree.add(multipleUserTreeJPanel, gridBagConstraints);

			gridBagConstraints.insets = new Insets(3, 0, 3, 0);

			JPanel multipleAlignmentOptionsJPane = new JPanel(new GridBagLayout());

			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			multipleAlignmentOptionsJPane.add(multipleGapOpenPenaltyJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			multipleAlignmentOptionsJPane.add(multipleGapOpenPenaltyJSpinner, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			multipleAlignmentOptionsJPane.add(multipleGapExtensionPenaltyJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			multipleAlignmentOptionsJPane.add(multipleGapExtensionPenaltyJSpinner, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			multipleAlignmentOptionsJPane.add(multipleDelayDivergentSequencesJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			multipleAlignmentOptionsJPane.add(multipleDelayDivergentSequencesJSpinner, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			multipleAlignmentOptionsJPane.add(multipleDnaWeightMatrixJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			multipleAlignmentOptionsJPane.add(multipleDnaWeightMatrixJLabelDnaWeightMatrixJComboBox,
					gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 4;
			multipleAlignmentOptionsJPane.add(multipleDnatransitionJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 4;
			multipleAlignmentOptionsJPane.add(multipleDnatransitionJSpinner, gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

			multipleAlignmentOptionsJPane.add(multipleKeepOriginalJCheckBox, gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 6;
			multipleAlignmentOptionsJPane.add(multipleUserTree, gridBagConstraints);
			multipleAlignmentOptionsJXTaskPane.add(multipleAlignmentOptionsJPane);

			addMultipleAlignmentOptionsListener();
		}
		return multipleAlignmentOptionsJXTaskPane;
	}

	private void addMultipleAlignmentOptionsListener() {
		multipleUserTreeJCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox source = (JCheckBox) e.getSource();
				if (source.isSelected()) {
					multipleUserTreeJButton.setEnabled(true);
				} else {
					multipleUserTreeJButton.setEnabled(false);
				}
			}
		});

		multipleUserTreeJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 记录上一次打开文件的位置
				String saveFolder = "C:\\";
				Preferences pref = Preferences.userRoot().node(this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				JFileChooser jfc = null;
				if (!lastPath.equals("")) {
					jfc = new JFileChooser(lastPath);
				} else {
					jfc = new JFileChooser(saveFolder);
				}
				jfc.setDialogTitle("Open File");
				jfc.setFileSelectionMode(0);
				int returnVal = jfc.showOpenDialog(UnifiedAccessPoint.getInstanceFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					saveFolder = selectedFile.getPath();
					multipleUserTreeJTextArea.setText(saveFolder);
					pref.put("lastPath", saveFolder);
				}
			}
		});

	}
	
	/**
	 * 
	 * 说明：这里没有调用对inputFiles进行赋值。他的独立的子类应该继承这个方法，然后对其进行赋值。
	 *  如果没有继承这个方法，那么应该
	 *  
	 * @title obtainInputFiles
	 * @createdDate 2020-10-23 09:15
	 * @lastModifiedDate 2020-10-23 09:15
	 * @author yudalang
	 * @since 1.7
	 *   
	 * @return void
	 */
	protected void obtainInputFiles() {
		
	}

	private JButton getAlignSequencesJButton() {
		if (alignSequencesJButton == null) {
			alignSequencesJButton = new JButton("Align sequences");
			alignSequencesJButton.setFont(defaultFont);

			alignSequencesJButton.addActionListener(e -> {

				List<ClustaW2Alignment> tasks = new ArrayList<>();
				obtainInputFiles();

				if (!DataImportPanel_OneTypeOneFile.checkFile(inputFiles)) {
					return;
				}
				for (File file : inputFiles) {
					ClustaW2Alignment clustaW2Alignment = createClustaW2Alignment(file);
					tasks.add(clustaW2Alignment);
				}
//				EGPSThreadUtil.quicklyRunThread(alignmentMain, tasks);

			});
		}
		return alignSequencesJButton;
	}

	private ClustaW2Alignment createClustaW2Alignment(File inputFile) {

		ClustaW2Alignment clustaW2Alignment = new ClustaW2Alignment(alignmentMain);
		String inputFileParameter = " -INFILE=" + inputFile;
		String outputFile = "config/softwares/ClustalW2/result.fa";
		String outputFileParameter = " -OUTFILE=" + outputFile;
		String outPutFormat = " -OUTPUT=FASTA";
		String alignParameter = " -ALIGN";
		String pairGrapOpenParameter = " -PWGAPOPEN=" + pairwiseGapOpenPenaltyJSpinner.getValue();

		String pairwiseGapExtensionPenaltyParameter = " -PWGAPEXT=" + pairwiseGapExtensionPenaltyJSpinner.getValue();

		String pairwiseDnaWeightMatrixParameter = " -PWDNAMATRIX=" + pairwiseDnaWeightMatrixJComboBox.getSelectedItem();

		String multipleGapOpenPenaltyParameter = " -GAPOPEN=" + multipleGapOpenPenaltyJSpinner.getValue();
		String multipleGapExtensionPenaltyParameter = " -GAPEXT=" + multipleGapExtensionPenaltyJSpinner.getValue();
		String multipleDelayDivergentSequencesParameter = " -MAXDIV="
				+ multipleDelayDivergentSequencesJSpinner.getValue();

		String multipleDnaWeightMatrixParameter = " -DNAMATRIX="
				+ multipleDnaWeightMatrixJLabelDnaWeightMatrixJComboBox.getSelectedItem();
		StringBuilder sb = new StringBuilder();

		sb.append(inputFileParameter).append(outputFileParameter).append(outPutFormat).append(alignParameter)
				.append(pairGrapOpenParameter).append(pairwiseGapExtensionPenaltyParameter)
				.append(pairwiseDnaWeightMatrixParameter).append(multipleGapOpenPenaltyParameter)
				.append(multipleGapExtensionPenaltyParameter).append(multipleDelayDivergentSequencesParameter)
				.append(multipleDnaWeightMatrixParameter);
		if (multipleKeepOriginalJCheckBox.isSelected()) {
			String multipleKeepOriginalParameter = " -OUTORDER=INPUT";
			sb.append(multipleKeepOriginalParameter);
		} else {
			String multipleKeepOriginalParameter = " -OUTORDER=ALIGNED";
			sb.append(multipleKeepOriginalParameter);
		}

		if (multipleUserTreeJCheckBox.isSelected()) {

			if (!multipleUserTreeJTextArea.getText().isEmpty()) {
				String multipleUserTreeParameter = " -NEWTREE=" + multipleUserTreeJTextArea.getText();
				sb.append(multipleUserTreeParameter);
			}
		}
		clustaW2Alignment.setCommand(sb.toString());
		clustaW2Alignment.setFileName(inputFile.getName());
		clustaW2Alignment.setOutputFilePath(outputFile);

		return clustaW2Alignment;
	}

	@Override
	public void setEnabled(boolean enabled) {

		alignSequencesJButton.setEnabled(enabled);

	}

}
