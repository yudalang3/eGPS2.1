package module.sankeyplot.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class LeftConveOperationPanel extends JPanel implements ParameterInitialized {

	private PaintingPanel sankeyPlotDrawer;
	private JComboBox<String> comboBoxColorResource;
	private JButton btnRamdonColor;

	public LeftConveOperationPanel(PaintingPanel sankeyPlotDrawer) {
		this.sankeyPlotDrawer = sankeyPlotDrawer;

		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblColorResource = new JLabel("Color scheme");
		lblColorResource.setFont(globalFont);
		GridBagConstraints gbc_lblColorResource = new GridBagConstraints();
		gbc_lblColorResource.anchor = GridBagConstraints.WEST;
		gbc_lblColorResource.insets = new Insets(0, 0, 5, 5);
		gbc_lblColorResource.gridx = 0;
		gbc_lblColorResource.gridy = 1;
		add(lblColorResource, gbc_lblColorResource);

		comboBoxColorResource = new JComboBox<>();
		comboBoxColorResource.setFont(globalFont);
		comboBoxColorResource.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Follow Left", "Gradient", "Follow Right" }));
		GridBagConstraints gbc_comboBoxColorResource = new GridBagConstraints();
		gbc_comboBoxColorResource.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxColorResource.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxColorResource.gridx = 2;
		gbc_comboBoxColorResource.gridy = 1;
		add(comboBoxColorResource, gbc_comboBoxColorResource);

		JLabel lblNewLabel = new JLabel("Random color");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		add(lblNewLabel, gbc_lblNewLabel);

		btnRamdonColor = new JButton();
		
		btnRamdonColor.setToolTipText("<html><body>Set random color to all bars!");
		btnRamdonColor.setIcon(EGPSShellIcons.get("random.png"));

		GridBagConstraints gbc_btnRamdonColor = new GridBagConstraints();
		gbc_btnRamdonColor.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRamdonColor.gridx = 2;
		gbc_btnRamdonColor.gridy = 2;
		add(btnRamdonColor, gbc_btnRamdonColor);

	}

	@Override
	public void initializeParameters() {

	}

	@Override
	public void addListeners() {
		btnRamdonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sankeyPlotDrawer.randomColor();
			}
		});
		
		comboBoxColorResource.addItemListener(e ->{
			int selectedIndex = comboBoxColorResource.getSelectedIndex();
			if (selectedIndex == 0) {
				sankeyPlotDrawer.setRectColorSource(RectColorSource.Left);
			}else if (selectedIndex == 1) {
				sankeyPlotDrawer.setRectColorSource(RectColorSource.Gradient);
			}else {
				sankeyPlotDrawer.setRectColorSource(RectColorSource.Right);
			}
			
			sankeyPlotDrawer.getSankeyPlotMain().recordTheFeatureInvoked(0);
		});

	}

	@Override
	public void removeListeners() {
		
	}

	@Override
	public String toString() {
		Object selectedItem = comboBoxColorResource.getSelectedItem();
		return selectedItem.toString();
	}
}
