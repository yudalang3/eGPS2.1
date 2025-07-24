package module.pill.gui;

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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import egps2.frame.gui.comp.toggle.toggle.ToggleAdapter;
import egps2.frame.gui.comp.toggle.toggle.ToggleButton;
import egps2.UnifiedAccessPoint;

public class CreativeModeTaskPanel extends JPanel {

	private static final long serialVersionUID = 1L;


	/**
	 * Create the panel.
	 */
	public CreativeModeTaskPanel() {
		
		Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		
		setBorder(new EmptyBorder(8, 15, 8, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel_1 = new JLabel("Mode Switch Button");
		lblNewLabel_1.setFont(titleFont);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		ToggleButton toggleButtonCreative = new ToggleButton();
		toggleButtonCreative.setSelected(true);
		toggleButtonCreative.setForeground(new java.awt.Color(40, 139, 236));
		toggleButtonCreative.setText("Toggle creative mode");
		toggleButtonCreative.setFocusable(false);
		toggleButtonCreative.setFont(defaultFont);
		toggleButtonCreative.setPreferredSize(new Dimension(100, 35));

		toggleButtonCreative.setToolTipText(
				"Click to toggle free-create mode, enable users to free drag, create and manipulate the tree nodes.");
		GridBagConstraints gbc_toggleButtonCreative = new GridBagConstraints();
		gbc_toggleButtonCreative.fill = GridBagConstraints.BOTH;
		gbc_toggleButtonCreative.insets = new Insets(0, 0, 5, 0);
		gbc_toggleButtonCreative.gridx = 0;
		gbc_toggleButtonCreative.gridy = 1;
		add(toggleButtonCreative, gbc_toggleButtonCreative);
//		toggleButtonCreative.addActionListener(e -> {
//			if (treeLayoutProperties == null) {
//				return;
//			}
//			treeLayoutProperties.setCreativeMode(toggleButtonCreative.isSelected());
//		});
		
		
		toggleButtonCreative.addEventToggleSelected(new ToggleAdapter() {
            @Override
            public void onSelected(boolean selected) {
            }
        });

		JButton buttonAlign2right = new JButton("Wait for the function to implement");
		buttonAlign2right.setHorizontalAlignment(SwingConstants.LEFT);
		buttonAlign2right.setFont(defaultFont);
		buttonAlign2right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		JLabel lblNewLabel = new JLabel("Quick operation: ");
		lblNewLabel.setFont(titleFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 3;
		add(lblNewLabel, gbc_lblNewLabel);
		GridBagConstraints gbc_buttonAlign2right = new GridBagConstraints();
		gbc_buttonAlign2right.fill = GridBagConstraints.BOTH;
		gbc_buttonAlign2right.insets = new Insets(0, 0, 5, 0);
		gbc_buttonAlign2right.gridx = 0;
		gbc_buttonAlign2right.gridy = 4;
		add(buttonAlign2right, gbc_buttonAlign2right);


	}


}
