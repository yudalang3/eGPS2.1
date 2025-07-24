package module.tablelikeview.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;
import module.tablelikeview.MainGUI;

public class TableConfiguration extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 * 
	 * @param mainGUI
	 */
	public TableConfiguration(MainGUI mainGUI) {
		setBorder(new EmptyBorder(15, 15, 15, 5));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		
		ButtonGroup buttonGroup = new ButtonGroup();
		JRadioButton rdbtnAutoresizeoff = new JRadioButton("AUTO_RESIZE_OFF");
		rdbtnAutoresizeoff.setSelected(true);

		GridBagConstraints gbc_rdbtnAutoresizeoff = new GridBagConstraints();
		gbc_rdbtnAutoresizeoff.gridwidth = 3;
		gbc_rdbtnAutoresizeoff.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAutoresizeoff.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnAutoresizeoff.gridx = 0;
		gbc_rdbtnAutoresizeoff.gridy = 0;
		add(rdbtnAutoresizeoff, gbc_rdbtnAutoresizeoff);
		buttonGroup.add(rdbtnAutoresizeoff);

		JLabel lblNewLabel = new JLabel("preference width :");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 40, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);

		JSpinner spinner = new JSpinner();
		SpinnerNumberModel model = new SpinnerNumberModel(200, 1, 2000, 1);
		spinner.setModel(model);
		spinner.setFont(defaultFont);
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 2;
		gbc_spinner.gridy = 1;
		add(spinner, gbc_spinner);
		rdbtnAutoresizeoff.addActionListener(e -> {
			if (rdbtnAutoresizeoff.isSelected()) {
				Object value = model.getValue();
				Integer vv = (Integer) value;
				mainGUI.setPreferenceWidth(vv);
				mainGUI.changeJTableResizeMode(JTable.AUTO_RESIZE_OFF);
			}
		});

		JRadioButton rdbtnNewRadioButton = new JRadioButton("AUTO_RESIZE_NEXT_COLUMN");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.gridwidth = 3;
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 2;
		add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		buttonGroup.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.addActionListener(e -> {
			if (rdbtnNewRadioButton.isSelected()) {
				mainGUI.changeJTableResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
			}
		});

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("AUTO_RESIZE_SUBSEQUENT_COLUMNS");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.gridwidth = 3;
		gbc_rdbtnNewRadioButton_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton_1.gridx = 0;
		gbc_rdbtnNewRadioButton_1.gridy = 3;
		add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
		buttonGroup.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.addActionListener(e -> {
			if (rdbtnNewRadioButton_1.isSelected()) {
				mainGUI.changeJTableResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			}
		});

		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("AUTO_RESIZE_LAST_COLUMN");
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.gridwidth = 3;
		gbc_rdbtnNewRadioButton_2.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton_2.gridx = 0;
		gbc_rdbtnNewRadioButton_2.gridy = 4;
		add(rdbtnNewRadioButton_2, gbc_rdbtnNewRadioButton_2);
		buttonGroup.add(rdbtnNewRadioButton_2);
		rdbtnNewRadioButton_2.addActionListener(e -> {
			if (rdbtnNewRadioButton_2.isSelected()) {
				mainGUI.changeJTableResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			}
		});

		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("AUTO_RESIZE_ALL_COLUMNS");
		GridBagConstraints gbc_rdbtnNewRadioButton_3 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_3.gridwidth = 3;
		gbc_rdbtnNewRadioButton_3.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_3.gridx = 0;
		gbc_rdbtnNewRadioButton_3.gridy = 5;
		add(rdbtnNewRadioButton_3, gbc_rdbtnNewRadioButton_3);
		buttonGroup.add(rdbtnNewRadioButton_3);

		rdbtnNewRadioButton_3.addActionListener(e -> {
			if (rdbtnNewRadioButton_3.isSelected()) {
				mainGUI.changeJTableResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			}
		});

		

		Component[] components = getComponents();
		for (Component component : components) {
			component.setFont(defaultFont);
		}

	}

}
