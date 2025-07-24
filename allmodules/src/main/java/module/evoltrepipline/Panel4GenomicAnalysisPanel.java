/**
 * 
 */
package module.evoltrepipline;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

import egps2.panels.dialog.EGPSJSpinner;
import egps2.panels.dialog.SwingDialog;

/**
 * @author YFQ
 * @date 2019-11-21 13:26:19
 * @version 1.0
 * @author ydl
 * 
 * @Date 2024-04-27
 *       <p>
 *       Description:
 *       </p>
 */
public class Panel4GenomicAnalysisPanel extends AbstractPrefShowContent {

	private static final long serialVersionUID = 253605067200415450L;

	private ConstantNameClass_GenomicAnalysis ccga = new ConstantNameClass_GenomicAnalysis();

	private JRadioButton ignoredRadio;
	private JSpinner toleSpinner;
	private Map<String, String> parameterMap;

	private JPanel contentPanel;

	public Panel4GenomicAnalysisPanel(Map<String, String> parameterMap, String insertionOrDeletionsName) {
		super.userObject = insertionOrDeletionsName;
		this.parameterMap = parameterMap;
	}

	@Override
	public JPanel getViewJPanel() {
		if (contentPanel != null) {
			return contentPanel;
		}

		contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel genomicAnalysisPanel = new JPanel(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		// gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		JPanel inserPanel = new JPanel(new GridBagLayout());
		inserPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccga.category1_indels, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));

		ignoredRadio = new JRadioButton(ccga.indels_value1_ignore);
		inserPanel.add(ignoredRadio, gridBagConstraints);

		JRadioButton nucleotideRadio = new JRadioButton(ccga.indels_value2_fifth);
		gridBagConstraints.gridx = 1;
		inserPanel.add(nucleotideRadio, gridBagConstraints);

		ignoredRadio.setFocusPainted(false);
		ignoredRadio.setFont(defaultFont);
		nucleotideRadio.setFocusPainted(false);
		nucleotideRadio.setFont(defaultFont);

		ButtonGroup ignoredButtonGroup = new ButtonGroup();
		ignoredButtonGroup.add(ignoredRadio);
		ignoredButtonGroup.add(nucleotideRadio);

		String insertionDeletionsValue = parameterMap.get(ccga.category1_indels);

		if (insertionDeletionsValue != null && !insertionDeletionsValue.equals("")) {
			if (Integer.valueOf(insertionDeletionsValue) == 0) {
				ignoredRadio.setSelected(true);
			} else {
				nucleotideRadio.setSelected(true);
			}
		} else {
			ignoredRadio.setSelected(true);
		}

		JPanel missPanel = new JPanel(new GridBagLayout());
		missPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccga.category2_missingData, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));

		JLabel toleLabel = new JLabel(ccga.missingData_label1_tolerance);
		toleLabel.setFont(defaultFont);

		int minValue = 0;
		int maxValue = 100;
		int currentValue = 50;
		int steps = 1;
		toleSpinner = new EGPSJSpinner(currentValue, minValue, maxValue, steps);
		toleSpinner.setFont(defaultFont);
		toleSpinner.setToolTipText("The value must be between 0 and 100.");

		JLabel percentageLabel = new JLabel("%");
		percentageLabel.setFont(defaultFont);

		int missingDataValue = Integer.valueOf(parameterMap.get(ccga.category2_missingData).trim());

		if (missingDataValue >= 0 && missingDataValue <= 100) {
			toleSpinner.setValue(Integer.valueOf(missingDataValue));
		} else {
			toleSpinner.setValue("50");
		}

		JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		spinnerPanel.add(toleSpinner);
		spinnerPanel.add(percentageLabel);

		gridBagConstraints.gridx = 0;
		missPanel.add(toleLabel, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		missPanel.add(spinnerPanel, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		genomicAnalysisPanel.add(inserPanel, gridBagConstraints);
		gridBagConstraints.gridy = 1;
		genomicAnalysisPanel.add(missPanel, gridBagConstraints);

		contentPanel.add(genomicAnalysisPanel);

		return contentPanel;
	}

	@Override
	public void saveParameter(Map<String, String> parameters) {
		
		int isIgnored;

		if (ignoredRadio.isSelected()) {
			isIgnored = 0;
		} else {
			isIgnored = 1;
		}

		int toleValue = Integer.valueOf(toleSpinner.getValue().toString());
		
		parameters.put(ccga.category1_indels, String.valueOf(isIgnored));
		parameters.put(ccga.category2_missingData, String.valueOf(toleValue));
	}

	@Override
	public boolean checkParameters(JTree jTree) {

		int data = Integer.valueOf(toleSpinner.getValue().toString());

		if (data >= 0 && data <= 100) {
			return true;
		} else {
			jTree.setSelectionRow(3);
			toleSpinner.requestFocus();
			SwingDialog.showMSGDialog("Error", "The value must be between 0 and 100!", 1);
			return false;
		}
	}

	public JRadioButton getRadio() {
		return ignoredRadio;
	}

	public JSpinner getJSpinner() {
		return toleSpinner;
	}
}
