package module.multiseq.alignment.trimmer.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.EGPSFileChooser;
import egps2.frame.gui.handler.EGPSTextTransferHandler;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import module.multiseq.alignment.trimmer.SimpleModuleController;

public class DataImportPanel extends JPanel {
	private JTextField textField;
	private SimpleModuleController controller;

	/**
	 * Create the panel.
	 */
	public DataImportPanel(SimpleModuleController controller) {
		this.controller = controller;
		controller.setDataImportPanel(this);
		
		setPreferredSize(new Dimension(550, 50));
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		
		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("import file");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(EGPSShellIcons.getHelpIcon());
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 0;
		lblNewLabel_1.setToolTipText("The file format should be fasta format!");
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		textField.setFont(globalFont);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);
		textField.setTransferHandler(new EGPSTextTransferHandler());
		
		JButton btnNewButton = new JButton("load");
		btnNewButton.setFont(globalFont);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				EGPSFileChooser egpsFileChooser = new EGPSFileChooser(this.getClass());
				int showOpenDialog = egpsFileChooser.showOpenDialog();
				if (showOpenDialog == EGPSFileChooser.APPROVE_OPTION) {
					textField.setText(egpsFileChooser.getSelectedFile().getAbsolutePath());
					//在load选中导入文件后，就初始化Parameters的Reference sequence,将其清空
					controller.getParametersPanel().getRefNameList().removeAllItems();
					
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);

	}

	public String getInputFilePath() {
		return textField.getText();
	}


}
