package module.multiseqview.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import graphic.engine.guibean.ColorIcon;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import module.multiseqview.SequenceStructureViewController;
import module.multiseqview.model.GraphicsBean;

@SuppressWarnings("serial")
public class LeftConveOperationPanel extends JPanel {
	
	SequenceStructureViewController controller;

	private JButton btnRamdonColor;
	
	public LeftConveOperationPanel(SequenceStructureViewController controller) {
		super();
		setBorder(new EmptyBorder(10, 10, 10, 10));
		this.controller = controller;
		
		
		setBackground(Color.WHITE);
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel_1 = new JLabel("Auto fit frame");
		lblNewLabel_1.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		
		JButton btnNewButton = new JButton();
		btnNewButton.setToolTipText("<html><body>Autosize to fit screen, or re set to default status.<br>Note: If you zoom in the graph before,please zoom out first!");
		btnNewButton.setIcon(EGPSShellIcons.get("auto-size.png"));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.recalculateAndRefresh();
			}
		});
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 1;
		add(btnNewButton, gbc_btnNewButton);

		JLabel lblNewLabel = new JLabel("Color of connecting line");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		add(lblNewLabel, gbc_lblNewLabel);

		GraphicsBean graphicsBean = controller.getGraphicsBean();
		ColorIcon colorIcon = new ColorIcon(graphicsBean.getCrossLinkedRegion());
		btnRamdonColor = new JButton(colorIcon);
		btnRamdonColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();
				Color showDialog = JColorChooser.showDialog(instanceFrame, "Choose color", colorIcon.getColor());
				if (showDialog != null) {
					colorIcon.setColor(showDialog);
					controller.refresh();
				}
			}
		});
		
		btnRamdonColor.setToolTipText("<html><body>Set random color to all bars!");

		GridBagConstraints gbc_btnRamdonColor = new GridBagConstraints();
		gbc_btnRamdonColor.gridx = 2;
		gbc_btnRamdonColor.gridy = 2;
		add(btnRamdonColor, gbc_btnRamdonColor);

	}
	
	
	
}
