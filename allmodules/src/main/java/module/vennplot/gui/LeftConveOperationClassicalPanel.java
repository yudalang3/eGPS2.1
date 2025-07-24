package module.vennplot.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import egps2.utils.common.util.EGPSShellIcons;
import graphic.engine.colors.EGPSColors;
import module.vennplot.VennPlotMain;
import module.vennplot.model.ClassicalParameterModel;
import egps2.panels.reusablecom.ParameterInitialized;

public class LeftConveOperationClassicalPanel extends JPanel implements ParameterInitialized {
	private static final long serialVersionUID = -8488873696764405165L;
	private ClassicalVennPlotOutterFrame upsetRPanel;
	private JCheckBox consistentWithSize;
	private JButton btnRamdonColor;

	public LeftConveOperationClassicalPanel(ClassicalVennPlotOutterFrame classicalVennPlotPanel, VennPlotMain vennPlotMain) {
		this.upsetRPanel = classicalVennPlotPanel;

		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		int length = upsetRPanel.getDataModel().getSetItems().length;

		if (length == 2 || length == 3) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			consistentWithSize = new JCheckBox("If painting consistent with size");
			consistentWithSize.setFocusable(false);
			consistentWithSize.setBackground(Color.WHITE);

			consistentWithSize.setFont(upsetRPanel.defaultFont);

			add(consistentWithSize);

			consistentWithSize.addItemListener(e -> {
				ClassicalParameterModel parameterModel = upsetRPanel.getParameterModel();

				parameterModel.setPaintingConsistentWithSize(consistentWithSize.isSelected());

				upsetRPanel.weakestUpdate();
				vennPlotMain.invokeTheFeatureMethod(3);
			});

		}

		add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JButton btnAutosize = new JButton();
		btnAutosize.setToolTipText("<html><body>Autosize to fit screen, or re set to default status.<br>Note: If you zoom in the graph before,please zoom out first!");
		btnAutosize.setIcon(EGPSShellIcons.get("auto-size.png"));
		btnAutosize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				classicalVennPlotPanel.weakestUpdate();
			}
		});
		panel.add(btnAutosize);

		btnRamdonColor = new JButton();
		btnRamdonColor.setToolTipText("<html><body>Set random color schema for all sets!");
		btnRamdonColor.setIcon(EGPSShellIcons.get("random.png"));
		btnRamdonColor.setFocusable(false);
		panel.add(btnRamdonColor);
		
		panel.add(Box.createHorizontalStrut(10));
		
		JButton changeView = new JButton();
		changeView.setToolTipText("<html><body>Change to another venn plot graphics!");
		ImageIcon imageIcon3 = new ImageIcon(getClass().getResource("view.png"));
		
		changeView.setIcon(imageIcon3);
		changeView.setFocusable(false);
		changeView.addActionListener(e ->{
		    upsetRPanel.changeTab();
		});
		panel.add(changeView);
	}

	@Override
	public void initializeParameters() {

	}

	@Override
	public void addListeners() {

		btnRamdonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClassicalParameterModel parameterModel = upsetRPanel.getParameterModel();
				List<Color> colors = parameterModel.getColors();
				int size = colors.size();
				colors.clear();
				for (int i = 0; i < size; i++) {
					colors.add(EGPSColors.randomColor());
				}
				upsetRPanel.weakestUpdate();
			}
		});
	}

	@Override
	public void removeListeners() {
		
	}

}
