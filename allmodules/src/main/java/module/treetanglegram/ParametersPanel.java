package module.treetanglegram;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;
import egps2.frame.gui.EGPSMainGuiUtil;

public class ParametersPanel extends JPanel {

	private TanglegramController controller;
	private JTextField textField;
	private JCheckBox chckbxOutgroup;
	private JButton btnNameFont;

	public ParametersPanel(TanglegramController controller) {
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		setBorder(new EmptyBorder(20, 20, 20, 20));
		this.controller = controller;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		chckbxOutgroup = new JCheckBox("Outgroup");
		chckbxOutgroup.setFont(globalFont);
		chckbxOutgroup.setToolTipText("This is used to reroot the tree, so that two trees can be comparable.");
		GridBagConstraints gbc_chckbxOutgroup = new GridBagConstraints();
		gbc_chckbxOutgroup.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOutgroup.gridx = 0;
		gbc_chckbxOutgroup.gridy = 0;
		add(chckbxOutgroup, gbc_chckbxOutgroup);
		
		textField = new JTextField();
		textField.setFont(globalFont);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Name font");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		btnNameFont = new JButton("font");
		btnNameFont.setFont(globalFont);
		GridBagConstraints gbc_btnNameFont = new GridBagConstraints();
		gbc_btnNameFont.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNameFont.gridx = 1;
		gbc_btnNameFont.gridy = 1;
		add(btnNameFont, gbc_btnNameFont);
		
		btnNameFont.addActionListener(e -> {
			EGPSMainGuiUtil.fontChanged(btnNameFont, () -> {});
		});
	}

	
	public boolean hasOutgroup() {
		return chckbxOutgroup.isSelected();
	}
	
	public String getOutgroupString() {
		return textField.getText().trim();
	}
	
	public Font getNameFont() {
		return btnNameFont.getFont();
	}
}
