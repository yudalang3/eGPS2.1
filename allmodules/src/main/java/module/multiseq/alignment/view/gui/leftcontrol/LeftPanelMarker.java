package module.multiseq.alignment.view.gui.leftcontrol;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.gui.AlignmentViewContinuousRightPanel;
import module.multiseq.alignment.view.gui.UserSelectedViewElement;
import module.multiseq.alignment.view.gui.VisulizationDataProperty;

/**
 * 
 * @author ydl
 *
 */
@SuppressWarnings("serial")
public class LeftPanelMarker extends JPanel {
	private JCheckBox chckbxMarkSite;
	private JCheckBox chckbxWhetherWithGap;
	private JCheckBox chckbxMarkSequence;
	private JComboBox<String> comboBoxSeqName;
	private JSpinner spinnerMarkSite;

	private AlignmentViewMain alignmentViewMain;
	private DefaultComboBoxModel<String> seqNamesModel;

	public LeftPanelMarker(AlignmentViewMain alignmentViewMain) {

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		this.alignmentViewMain = alignmentViewMain;

		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 5, 2, 5, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		chckbxMarkSequence = new JCheckBox("Mark sequence");
		chckbxMarkSequence.setToolTipText("");
		chckbxMarkSequence.setFont(globalFont);
		GridBagConstraints gbc_chckbxMarkSequence = new GridBagConstraints();
		gbc_chckbxMarkSequence.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxMarkSequence.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxMarkSequence.gridx = 0;
		gbc_chckbxMarkSequence.gridy = 0;
		add(chckbxMarkSequence, gbc_chckbxMarkSequence);

		JLabel label_1 = new JLabel("");
		label_1.setToolTipText(
				"<html><body>A.k.a mark row of interest.<br>\r\nNote: click \"Search and mark\" button to initialize marking.<br>\r\nUncheck the checkbox to un-mark the row or column.");
		label_1.setIcon(EGPSShellIcons.getHelpIcon());
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 0;
		add(label_1, gbc_label_1);

		comboBoxSeqName = new JComboBox<>();
		
		seqNamesModel = new DefaultComboBoxModel<String>();
		comboBoxSeqName.setModel(seqNamesModel);
		comboBoxSeqName.setFont(globalFont);
		
		GridBagConstraints gbc_comboBoxSeqName = new GridBagConstraints();
		gbc_comboBoxSeqName.gridwidth = 3;
		gbc_comboBoxSeqName.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxSeqName.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxSeqName.gridx = 0;
		gbc_comboBoxSeqName.gridy = 1;
		add(comboBoxSeqName, gbc_comboBoxSeqName);

		chckbxMarkSite = new JCheckBox("Mark site");
		chckbxMarkSite.setToolTipText("");
		chckbxMarkSite.setFont(globalFont);
		GridBagConstraints gbc_chckbxMarkSite = new GridBagConstraints();
		gbc_chckbxMarkSite.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxMarkSite.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxMarkSite.gridx = 0;
		gbc_chckbxMarkSite.gridy = 2;
		add(chckbxMarkSite, gbc_chckbxMarkSite);

		JLabel label = new JLabel("");
		label.setToolTipText(
				"<html><body>A.k.a mark column of interest.<br>\r\nThe number you input is relative to <b>non-gap</b> order.<br>\r\nNote: click \"Search and mark\" button to initialize marking.<br>\r\nUncheck the checkbox to un-mark the row or column.<br>");
		label.setIcon(EGPSShellIcons.get("help_blue.png"));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 2;
		add(label, gbc_label);

		chckbxWhetherWithGap = new JCheckBox("With gap");
		GridBagConstraints gbc_chckbxWhetherWithGap = new GridBagConstraints();
		gbc_chckbxWhetherWithGap.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxWhetherWithGap.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxWhetherWithGap.gridx = 2;
		gbc_chckbxWhetherWithGap.gridy = 2;
		chckbxWhetherWithGap.setFont(globalFont);
		add(chckbxWhetherWithGap, gbc_chckbxWhetherWithGap);

		spinnerMarkSite = new JSpinner();
		spinnerMarkSite.setFont(globalFont);
		spinnerMarkSite.setModel(new SpinnerNumberModel(1, 1, 100000, 1));
		GridBagConstraints gbc_spinnerMarkSite = new GridBagConstraints();
		gbc_spinnerMarkSite.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMarkSite.gridwidth = 3;
		gbc_spinnerMarkSite.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerMarkSite.gridx = 0;
		gbc_spinnerMarkSite.gridy = 3;
		add(spinnerMarkSite, gbc_spinnerMarkSite);

		JButton btnNewButton = new JButton("Search and mark");
		btnNewButton.setFont(globalFont);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridwidth = 3;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 4;
		add(btnNewButton, gbc_btnNewButton);

		btnNewButton.addActionListener(e -> {
			searchAndMarkProcess();
			
			alignmentViewMain.invokeTheFeature(2);
		});
	}
	
	public void initializeSequenceNames(List<String> names) {
		seqNamesModel.removeAllElements();
		for (String string : names) {
			seqNamesModel.addElement(string);
		}
	}

	private void searchAndMarkProcess() {
		boolean withGap = chckbxWhetherWithGap.isSelected();

		Optional<AlignmentViewContinuousRightPanel> charDrawingPanel = alignmentViewMain
				.getAlignmentViewCharDrawingPanel4ContinuousLayout();

		if (!charDrawingPanel.isPresent()) {
			return;
		}
		
		VisulizationDataProperty alignmentViewPort = alignmentViewMain.getAlignmentViewPort();
		
		int selectedIndex = comboBoxSeqName.getSelectedIndex();

		int value4jump = 0;
		
		if (chckbxMarkSite.isSelected()) {
			int value = (int) spinnerMarkSite.getValue();

			int totalSequenceLength = alignmentViewPort.getTotalSequenceLength();
			if (value > totalSequenceLength) {
				SwingDialog.showWarningMSGDialog("Warning",
						"You input site position should less than the total length: " + totalSequenceLength);
				return;
			}

			if (withGap) {
				value4jump = value - 1;
			} else {
				char[] seq = alignmentViewPort.getSequenceData().getSequenceI(selectedIndex).getSeq();
				
				int nonGapIndex = 0;
				for (int i = 0; i < totalSequenceLength; i++) {
					if ('-' != seq[i]) {
						nonGapIndex++;
					}
					
					if (nonGapIndex == value) {
						value4jump = i;
						break;
					}
				}
				
			}

			AlignmentViewContinuousRightPanel alignmentViewInnerRightPanel = charDrawingPanel.get();

			UserSelectedViewElement selectionElement = new UserSelectedViewElement();
			selectionElement.setxPos(value4jump);
			selectionElement.setyPos(0);
			selectionElement.setSelectRowNum(1);
			selectionElement.setSelectColumnNum(alignmentViewPort.getTotalSequenceCount());

			alignmentViewPort.getSelectionElements().add(selectionElement);

			alignmentViewInnerRightPanel.getBottomJScrollBar().setValue(value4jump);
		}

		if (chckbxMarkSequence.isSelected()) {
			
			value4jump = selectedIndex ;

			AlignmentViewContinuousRightPanel alignmentViewInnerRightPanel = charDrawingPanel.get();

			UserSelectedViewElement selectionElement = new UserSelectedViewElement();
			
			selectionElement.setxPos(0);
			selectionElement.setyPos(value4jump);
			selectionElement.setSelectRowNum(alignmentViewPort.getSequenceData().getLength());
			selectionElement.setSelectColumnNum(1);

			alignmentViewPort.getSelectionElements().add(selectionElement);

			alignmentViewInnerRightPanel.getRightJScrollBar().setValue(value4jump);

		}

	}

}
