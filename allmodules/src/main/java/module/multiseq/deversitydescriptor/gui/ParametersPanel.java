package module.multiseq.deversitydescriptor.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.multiseq.deversitydescriptor.SimpleModuleController;
import fasta.io.FastaReader;

public class ParametersPanel extends JPanel {

	private JComboBox<String> refNameList;

	/**
	 * Create the panel.
	 */
	public ParametersPanel(SimpleModuleController controller) {
		controller.setParametersPanel(this);

		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Reference sequence");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		refNameList = new JComboBox<>();
		refNameList.setEditable(true);
		GridBagConstraints gbc_textField_refName = new GridBagConstraints();
		gbc_textField_refName.insets = new Insets(0, 0, 5, 5);
		gbc_textField_refName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_refName.gridx = 1;
		gbc_textField_refName.gridy = 0;
		add(refNameList, gbc_textField_refName);

		Runnable action = () -> {

			String path = controller.getInputFilePath();
			if (path.isEmpty()) {
				SwingDialog.showErrorMSGDialog("Input error", "You need to import file first!");
			} else {
				LinkedHashMap<String, String> map = null;
				try {
					map = FastaReader.readFastaDNASequence(new File(path));
				} catch (IOException e1) {
					SwingDialog.showErrorMSGDialog("Input error", "Please check your input sequence, the sequence should be aligned.");
					e1.printStackTrace();
				}

				String refSequenceName = controller.getRefSequenceName();
				if (refSequenceName.isEmpty()) {
					// When the input JTextfield
					refNameList.removeAllItems();
					Set<String> keySet = map.keySet();
					for (String string : keySet) {
						refNameList.addItem(string);
					}
				} else {
					String seq = map.get(refSequenceName);

					if (seq == null) {
						SwingDialog.showErrorMSGDialog("Input error", "Can not find your input reference sequence!");
					} else {
						if (refNameList.getItemCount() > 0) {
							refNameList.removeAllItems();
						}
						Set<String> keySet = map.keySet();
						for (String string : keySet) {
							refNameList.addItem(string);
						}
						refNameList.setSelectedItem(refSequenceName);
					}
				}

			}

		};

		JButton btnNewButton = new JButton("Initialize");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(action).start();
			}
		});
		btnNewButton.setToolTipText("Clear the value in the editor to detect availiable sequences !");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);

		JLabel lblNewLabel_3 = new JLabel("Output type");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		ButtonGroup buttonGroup = new ButtonGroup();
				
						JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Reference to variant mutations");
						GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
						gbc_rdbtnNewRadioButton_1.gridwidth = 2;
						gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 0);
						gbc_rdbtnNewRadioButton_1.gridx = 1;
						gbc_rdbtnNewRadioButton_1.gridy = 1;
						rdbtnNewRadioButton_1.setSelected(true);
						add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
						buttonGroup.add(rdbtnNewRadioButton_1);
				
						JRadioButton rdbtnNewRadioButton = new JRadioButton("Variation state");
						// 先收敛起来，现在暂时不支持
						rdbtnNewRadioButton.setVisible(false);
						GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
						gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
						gbc_rdbtnNewRadioButton.gridx = 1;
						gbc_rdbtnNewRadioButton.gridy = 2;
						add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
						buttonGroup.add(rdbtnNewRadioButton);

		Component[] components = getComponents();
		for (Component component : components) {
			component.setFont(UnifiedAccessPoint.getLaunchProperty().getDefaultFont());
		}

	}

	public String getRefSequenceName() {
		Object selectedItem = refNameList.getSelectedItem();

		if (selectedItem == null) {
			return "";
		}
		return selectedItem.toString();
	}

	public JComboBox<String> getRefNameList() {
		return refNameList;
	}

}
