package module.treebuilder.fromdist;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import egps2.frame.gui.handler.EGPSTextTransferHandler;
import egps2.UnifiedAccessPoint;
import egps2.frame.gui.EGPSMainGuiUtil;

@SuppressWarnings("serial")
public class ParameterPanel extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public ParameterPanel() {

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		TitledBorder titledBorder = BorderFactory.createTitledBorder(
				"Evolutionary distance importer:");
		titledBorder.setTitleFont(globalFont);

		setBorder(titledBorder);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel_1 = new JLabel("Input");
		lblNewLabel_1.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 5, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		textField = new JTextField();
		textField.setTransferHandler(new EGPSTextTransferHandler());
		textField.setFont(globalFont);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);

		JButton btnNewButton = EGPSMainGuiUtil.getFileLoadingJButton(textField, getClass());
		btnNewButton.setText("Load");
		btnNewButton.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
		btnNewButton.setFont(globalFont);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);

	}

	public String getFilePath() {
		return textField.getText();
	}

}
