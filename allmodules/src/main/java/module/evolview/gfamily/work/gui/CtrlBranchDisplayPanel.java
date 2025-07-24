package module.evolview.gfamily.work.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;

@SuppressWarnings("serial")
public class CtrlBranchDisplayPanel extends BaseCtrlPanel {

	public CtrlBranchDisplayPanel() {
		setBorder(new EmptyBorder(15, 15, 15, 15));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		ButtonGroup buttonGroup = new ButtonGroup();
		JLabel lblBranchLength = new JLabel("Branch length");
		lblBranchLength.setFont(globalFont);
		GridBagConstraints gbc_lblBranchLength = new GridBagConstraints();
		gbc_lblBranchLength.anchor = GridBagConstraints.WEST;
		gbc_lblBranchLength.insets = new Insets(0, 0, 5, 5);
		gbc_lblBranchLength.gridx = 0;
		gbc_lblBranchLength.gridy = 0;
		add(lblBranchLength, gbc_lblBranchLength);

		JRadioButton rdbtnDivergence = new JRadioButton("Divergence");
		rdbtnDivergence.setFont(globalFont);
		rdbtnDivergence.setFocusable(false);
		GridBagConstraints gbc_rdbtnDivergence = new GridBagConstraints();
		gbc_rdbtnDivergence.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDivergence.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnDivergence.gridx = 1;
		gbc_rdbtnDivergence.gridy = 0;
		add(rdbtnDivergence, gbc_rdbtnDivergence);
		buttonGroup.add(rdbtnDivergence);
		rdbtnDivergence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnDivergence.isSelected()) {
//					controller.getNewFixedThreadPool().execute(() -> {
//						controller.setBranchLengthType(BranchLengthType.DIVERGENCE);
//					});
				}
			}
		});

		JRadioButton rdbtnMutationCount = new JRadioButton("Mutation");
		rdbtnMutationCount.setFocusable(false);
		rdbtnMutationCount.setFont(globalFont);
		GridBagConstraints gbc_rdbtnMutationCount = new GridBagConstraints();
		gbc_rdbtnMutationCount.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnMutationCount.anchor = GridBagConstraints.WEST;
		gbc_rdbtnMutationCount.gridx = 1;
		gbc_rdbtnMutationCount.gridy = 1;
		add(rdbtnMutationCount, gbc_rdbtnMutationCount);
		buttonGroup.add(rdbtnMutationCount);
		rdbtnMutationCount.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (rdbtnMutationCount.isSelected()) {
//					controller.getNewFixedThreadPool().execute(() -> {
//						controller.setBranchLengthType(BranchLengthType.MUTATION_COUNT);
//					});
				}
			}
		});

		JRadioButton rdbtnTime = new JRadioButton("Time");
		rdbtnTime.setFocusable(false);
		rdbtnTime.setFont(globalFont);
		GridBagConstraints gbc_rdbtnTime = new GridBagConstraints();
		gbc_rdbtnTime.anchor = GridBagConstraints.WEST;
		gbc_rdbtnTime.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnTime.gridx = 1;
		gbc_rdbtnTime.gridy = 2;
		add(rdbtnTime, gbc_rdbtnTime);
		rdbtnTime.setSelected(true);
		buttonGroup.add(rdbtnTime);
		rdbtnTime.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (rdbtnTime.isSelected()) {
//					controller.getNewFixedThreadPool().execute(() -> {
//						controller.setBranchLengthType(BranchLengthType.REAL_TIME);
//					});
				}
			}
		});

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 3;
		add(separator, gbc_separator);

		JCheckBox chckbxBranchLabel = new JCheckBox("Branch label");
		chckbxBranchLabel.setFocusable(false);
		chckbxBranchLabel.setFont(globalFont);
		GridBagConstraints gbc_chckbxBranchLabel = new GridBagConstraints();
		gbc_chckbxBranchLabel.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxBranchLabel.gridx = 0;
		gbc_chckbxBranchLabel.gridy = 4;
		add(chckbxBranchLabel, gbc_chckbxBranchLabel);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setFocusable(false);
		comboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] { "Mutaion string", "Mutation bar", "Branch length" }));
		comboBox.setFont(globalFont);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		add(comboBox, gbc_comboBox);

		JCheckBox chckbxHighlightBackMuts = new JCheckBox("Highlight back mutations");
		chckbxHighlightBackMuts.setFont(globalFont);
		GridBagConstraints gbc_chckbxHighlightBackMuts = new GridBagConstraints();
		gbc_chckbxHighlightBackMuts.anchor = GridBagConstraints.WEST;
		gbc_chckbxHighlightBackMuts.gridwidth = 2;
		gbc_chckbxHighlightBackMuts.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxHighlightBackMuts.gridx = 0;
		gbc_chckbxHighlightBackMuts.gridy = 5;
		add(chckbxHighlightBackMuts, gbc_chckbxHighlightBackMuts);

		chckbxBranchLabel.addActionListener(e -> {
			boolean selected = chckbxBranchLabel.isSelected();
//			controller.showBranchLabel(selected, chckbxHighlightBackMuts.isSelected(), comboBox.getSelectedIndex());
			chckbxHighlightBackMuts.setEnabled(selected);
		});
		chckbxHighlightBackMuts.addActionListener(e -> {
//			controller.showBranchLabel(chckbxBranchLabel.isSelected(), chckbxHighlightBackMuts.isSelected(),
//					comboBox.getSelectedIndex());
		});

		comboBox.addActionListener(e -> {
//			controller.showBranchLabel(chckbxBranchLabel.isSelected(), chckbxHighlightBackMuts.isSelected(),
//					comboBox.getSelectedIndex());
		});
	}

}
