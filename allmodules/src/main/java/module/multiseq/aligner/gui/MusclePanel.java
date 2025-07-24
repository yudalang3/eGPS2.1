package module.multiseq.aligner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import com.jidesoft.swing.JideSplitPane;

import egps2.panels.dialog.EGPSJSpinner;
import egps2.UnifiedAccessPoint;
import module.multiseq.aligner.MultipleSeqAlignerMain;

public class MusclePanel extends AbstractAlignmentPanel {

	private static final long serialVersionUID = -2908588357598487831L;
	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
	private JSplitPane mainSplitPane;
	private JPanel leftToolPane;
	private GridBagConstraints gridBagConstraints;

	private JXTaskPane gapPenaltiesJXTaskPane;
	private JLabel gapPenaltiesGapOpenJLabel;
	private JSpinner gapPenaltiesGapOpenJSpinner;

	private JLabel gapPenaltiesGapExtendJLabel;
	private JSpinner gapPenaltiesGapExtendJSpinner;

	private JComboBox<String> advancedOptionsClusterMethodIterationsJComboBox;

	private JXTaskPane memoryOrIterationsJXTaskPane;

	private JLabel memoryMaxMemoryJLabel;
	private JSpinner memoryMaxMemoryJSpinner;

	private JLabel iterationsMaxJLabel;
	private JSpinner iterationsMaxJSpinner;

	private JButton alignSequencesJButton;
	private JXTaskPane advancedOptionsJXTaskPane;
	private JLabel advancedOptionsClusterMethodIterationsJLabel;
	private JLabel advancedOptionsClusterMethodOtherIterationsJLabel;
	private JComboBox<String> advancedOptionsClusterMethodOtherIterationsJComboBox;
	private JLabel advancedOptionsMinDiagLengthJLabelJLabel;
	private EGPSJSpinner advancedOptionsMinDiagLengthMaxMemoryJSpinner;

	public MusclePanel(MultipleSeqAlignerMain alignmentMain) {
		super(alignmentMain);
		setLayout(new BorderLayout());
		add(getMainSplitPane());
	}

	public JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setDividerSize(0);
			mainSplitPane.setDividerLocation(450);
			mainSplitPane.setBorder(null);
			mainSplitPane.add(getLeftToolPane());
			mainSplitPane.add(new JPanel());
		}
		return mainSplitPane;
	}

	private JPanel getLeftToolPane() {
		if (leftToolPane == null) {
			leftToolPane = new JPanel(new BorderLayout());
			// leftToolPane.set
			JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
			taskPaneContainer.setBackground(new Color(245, 245, 245));
			taskPaneContainer.setBackgroundPainter(null);
			taskPaneContainer.add(getGapPenaltiesJPanel());
			taskPaneContainer.add(getMemoryAndIterationsJPanel());
			taskPaneContainer.add(getAdvancedOptionsJPanel());
			taskPaneContainer.add(getAlignSequencesJButton());
			leftToolPane.setMinimumSize(new Dimension(500, 200));
			leftToolPane.add(new JScrollPane(taskPaneContainer), BorderLayout.CENTER);
		}
		return leftToolPane;
	}

	private JXTaskPane getGapPenaltiesJPanel() {
		if (gapPenaltiesJXTaskPane == null) {
			gapPenaltiesJXTaskPane = new JXTaskPane();
			gapPenaltiesJXTaskPane.setTitle("GAP PENALTIES");
			gapPenaltiesJXTaskPane.setFont(titleFont);
			gapPenaltiesGapOpenJLabel = new JLabel("Gap open    ");
			gapPenaltiesGapOpenJLabel.setFont(defaultFont);

			double gapPenaltiesGapOpenJSpinnerMinValue = -Double.MAX_VALUE;
			double gapPenaltiesGapOpenJSpinnerMaxValue = 0.00;
			double gapPenaltiesGapOpenJSpinnerCurrentValue = -1.00;
			double gapPenaltiesGapOpenJSpinnerSteps = 0.10;
			gapPenaltiesGapOpenJSpinner = new EGPSJSpinner(gapPenaltiesGapOpenJSpinnerCurrentValue,
					gapPenaltiesGapOpenJSpinnerMinValue, gapPenaltiesGapOpenJSpinnerMaxValue,
					gapPenaltiesGapOpenJSpinnerSteps);
			gapPenaltiesGapOpenJSpinner.setPreferredSize(new Dimension(200, 18));

			gapPenaltiesGapExtendJLabel = new JLabel("Gap Extend    ");
			gapPenaltiesGapExtendJLabel.setFont(defaultFont);

			// gapExtensionPenaltyJLabel.setToolTipText("17:7,661,779-7,687,550");
			double gapPenaltiesGapExtendJSpinnerMinValue = -Double.MAX_VALUE;
			double gapPenaltiesGapExtendJSpinnerMaxValue = 0.00;
			double gapPenaltiesGapExtendJSpinnerCurrentValue = 0.00;
			double gapPenaltiesGapExtendJSpinnerSteps = 0.10;
			gapPenaltiesGapExtendJSpinner = new EGPSJSpinner(gapPenaltiesGapExtendJSpinnerCurrentValue,
					gapPenaltiesGapExtendJSpinnerMinValue, gapPenaltiesGapExtendJSpinnerMaxValue,
					gapPenaltiesGapExtendJSpinnerSteps);
			gapPenaltiesGapExtendJSpinner.setPreferredSize(new Dimension(200, 18));

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(3, 0, 3, 0);

			gridBagConstraints.anchor = GridBagConstraints.EAST;
			// gridBagConstraints.fill = GridBagConstraints.WEST;
			gridBagConstraints.weightx = 1.0;
			JPanel gapPenaltiesJPane = new JPanel(new GridBagLayout());
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gapPenaltiesJPane.add(gapPenaltiesGapOpenJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gapPenaltiesJPane.add(gapPenaltiesGapOpenJSpinner, gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gapPenaltiesJPane.add(gapPenaltiesGapExtendJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gapPenaltiesJPane.add(gapPenaltiesGapExtendJSpinner, gridBagConstraints);

			JPanel gapPenaltiesJPane1 = new JPanel();
			gapPenaltiesJPane1.setLayout(new BorderLayout());
			gapPenaltiesJPane1.add(gapPenaltiesJPane, BorderLayout.EAST);

			gapPenaltiesJXTaskPane.add(gapPenaltiesJPane1);
		}
		return gapPenaltiesJXTaskPane;
	}

	private JXTaskPane getMemoryAndIterationsJPanel() {
		if (memoryOrIterationsJXTaskPane == null) {
			memoryOrIterationsJXTaskPane = new JXTaskPane();
			memoryOrIterationsJXTaskPane.setTitle("MEMORY/ITERATIONS");
			memoryOrIterationsJXTaskPane.setFont(titleFont);

			memoryMaxMemoryJLabel = new JLabel("Max Memory in MB    ");
			memoryMaxMemoryJLabel.setFont(defaultFont);

			int memoryJSpinnerMinValue = 256;
			int memoryJSpinnerMaxValue = 2147483647;
			int memoryJSpinnerCurrentValue = 256;
			int memoryJSpinnerSteps = 256;
			memoryMaxMemoryJSpinner = new EGPSJSpinner(memoryJSpinnerCurrentValue, memoryJSpinnerMinValue,
					memoryJSpinnerMaxValue, memoryJSpinnerSteps);
			memoryMaxMemoryJSpinner.setPreferredSize(new Dimension(200, 18));

			iterationsMaxJLabel = new JLabel("Max Iterations    ");
			iterationsMaxJLabel.setFont(defaultFont);

			int iterationsMaxJSpinnerMinValue = 1;
			int iterationsMaxJSpinnerMaxValue = 2147483647;
			int iterationsMaxJSpinnerCurrentValue = 16;
			int iterationsMaxJSpinnerSteps = 1;
			iterationsMaxJSpinner = new EGPSJSpinner(iterationsMaxJSpinnerCurrentValue, iterationsMaxJSpinnerMinValue,
					iterationsMaxJSpinnerMaxValue, iterationsMaxJSpinnerSteps);
			iterationsMaxJSpinner.setPreferredSize(new Dimension(200, 18));

			JPanel memoryAndIterationsJPane = new JPanel(new GridBagLayout());

			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			memoryAndIterationsJPane.add(memoryMaxMemoryJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			memoryAndIterationsJPane.add(memoryMaxMemoryJSpinner, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			memoryAndIterationsJPane.add(iterationsMaxJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			memoryAndIterationsJPane.add(iterationsMaxJSpinner, gridBagConstraints);

			JPanel memoryAndIterationsJPane1 = new JPanel();
			memoryAndIterationsJPane1.setLayout(new BorderLayout());
			memoryAndIterationsJPane1.add(memoryAndIterationsJPane, BorderLayout.EAST);

			memoryOrIterationsJXTaskPane.add(memoryAndIterationsJPane1);
		}
		return memoryOrIterationsJXTaskPane;
	}

	private JXTaskPane getAdvancedOptionsJPanel() {

		if (advancedOptionsJXTaskPane == null) {
			advancedOptionsJXTaskPane = new JXTaskPane();
			advancedOptionsJXTaskPane.setTitle("ADVANCED OPTIONS");
			advancedOptionsJXTaskPane.setFont(titleFont);

			advancedOptionsClusterMethodIterationsJLabel = new JLabel("Cluster Method (Iterations 1,2)");
			advancedOptionsClusterMethodIterationsJLabel.setFont(defaultFont);

			String[] clusterMethodIterationsListData = { "UPGMA", "UPGMX", "Neighbor Joining" };
			advancedOptionsClusterMethodIterationsJComboBox = new JComboBox<String>(clusterMethodIterationsListData);
			advancedOptionsClusterMethodIterationsJComboBox.setFont(defaultFont);
			advancedOptionsClusterMethodIterationsJComboBox.setPreferredSize(new Dimension(200, 18));

			advancedOptionsClusterMethodOtherIterationsJLabel = new JLabel("Cluster Method (Other Iterations)");
			advancedOptionsClusterMethodOtherIterationsJLabel.setFont(defaultFont);

			String[] clusterMethodOtherIterationsListData = { "UPGMA", "UPGMX", "Neighbor Joining" };
			advancedOptionsClusterMethodOtherIterationsJComboBox = new JComboBox<String>(
					clusterMethodOtherIterationsListData);
			advancedOptionsClusterMethodOtherIterationsJComboBox.setFont(defaultFont);
			advancedOptionsClusterMethodOtherIterationsJComboBox.setPreferredSize(new Dimension(200, 18));

			advancedOptionsMinDiagLengthJLabelJLabel = new JLabel("Min Diag Length (Lambda)");
			advancedOptionsMinDiagLengthJLabelJLabel.setFont(defaultFont);

			int minDiagLengthJSpinnerMinValue = 0;
			int minDiagLengthJSpinnerMaxValue = 2147483647;
			int minDiagLengthJSpinnerCurrentValue = 24;
			int minDiagLengthJSpinnerSteps = 1;
			advancedOptionsMinDiagLengthMaxMemoryJSpinner = new EGPSJSpinner(minDiagLengthJSpinnerCurrentValue,
					minDiagLengthJSpinnerMinValue, minDiagLengthJSpinnerMaxValue, minDiagLengthJSpinnerSteps);
			advancedOptionsMinDiagLengthMaxMemoryJSpinner.setPreferredSize(new Dimension(200, 18));

			JPanel advancedOptionsJPane = new JPanel(new GridBagLayout());

			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			advancedOptionsJPane.add(advancedOptionsClusterMethodIterationsJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			advancedOptionsJPane.add(advancedOptionsClusterMethodIterationsJComboBox, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			advancedOptionsJPane.add(advancedOptionsClusterMethodOtherIterationsJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			advancedOptionsJPane.add(advancedOptionsClusterMethodOtherIterationsJComboBox, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			advancedOptionsJPane.add(advancedOptionsMinDiagLengthJLabelJLabel, gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			advancedOptionsJPane.add(advancedOptionsMinDiagLengthMaxMemoryJSpinner, gridBagConstraints);

			advancedOptionsJXTaskPane.add(advancedOptionsJPane);
		}
		return advancedOptionsJXTaskPane;
	}

	private JButton getAlignSequencesJButton() {
		if (alignSequencesJButton == null) {
			alignSequencesJButton = new JButton("Align sequences");
			alignSequencesJButton.setFont(defaultFont);
			alignSequencesJButton.setPreferredSize(new Dimension(160, 25));
		}
		return alignSequencesJButton;
	}

	@Override
	public void setEnabled(boolean enabled) {

		alignSequencesJButton.setEnabled(enabled);
	}

}
