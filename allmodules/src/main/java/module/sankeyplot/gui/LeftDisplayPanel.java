package module.sankeyplot.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import egps2.panels.dialog.EGPSFontChooser;
import egps2.utils.common.model.datatransfer.CallBackBehavior;
import module.sankeyplot.SankeyPlotMain;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class LeftDisplayPanel extends JPanel implements ParameterInitialized {

	private JSpinner transparentSpinner;
	private JSpinner barWidthspinner;
	private JSpinner curveRateSpinner;

	private PaintingPanel sankeyPlotDrawer;
	private JButton btnFontTitle;
	private JButton btnFontName;

	public LeftDisplayPanel(PaintingPanel sankeyPlotDrawer) {
		this.sankeyPlotDrawer = sankeyPlotDrawer;

		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 2, 0, 10));

		SankeyPlotMain sankeyPlotMain = sankeyPlotDrawer.getSankeyPlotMain();
		JLabel lblLeftBarWidth = new JLabel("Bar width : ");
		lblLeftBarWidth.setFont(sankeyPlotMain.defaultFont);
		add(lblLeftBarWidth);

		barWidthspinner = new JSpinner(new SpinnerNumberModel(40, 1, 100, 1));
		barWidthspinner.setFont(sankeyPlotMain.defaultFont);
		add(barWidthspinner);

		JLabel lblTopBarWidth = new JLabel("Transparency : ");
		lblTopBarWidth.setFont(sankeyPlotMain.defaultFont);
		add(lblTopBarWidth);

		transparentSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 255, 20));
		transparentSpinner.setFont(sankeyPlotMain.defaultFont);
		add(transparentSpinner);

		JLabel lblBottomBarWidth = new JLabel("Curve rate : ");
		lblBottomBarWidth.setFont(sankeyPlotMain.defaultFont);
		add(lblBottomBarWidth);

		curveRateSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 100, 1));
		curveRateSpinner.setFont(sankeyPlotMain.defaultFont);
		add(curveRateSpinner);

		JLabel lblTitleFont = new JLabel("Title font");
		lblTitleFont.setFont(sankeyPlotMain.defaultFont);
		add(lblTitleFont);

		btnFontTitle = new JButton("Font");
		btnFontTitle.setFont(sankeyPlotMain.defaultFont);
		add(btnFontTitle);

		JLabel lblNameFont = new JLabel("Name font");
		lblNameFont.setFont(sankeyPlotMain.defaultFont);
		add(lblNameFont);

		btnFontName = new JButton("Font");
		btnFontName.setFont(sankeyPlotMain.defaultFont);
		add(btnFontName);
	}

	@Override
	public void initializeParameters() {
	}

	@Override
	public void addListeners() {
		transparentSpinner.addChangeListener(e -> {
			int value = (int) transparentSpinner.getValue();
			sankeyPlotDrawer.setTransparent(value);
			sankeyPlotDrawer.getSankeyPlotMain().recordTheFeatureInvoked(1);
		});

		curveRateSpinner.addChangeListener(e -> {
			double value = (int) curveRateSpinner.getValue();
			value /= 100;
			sankeyPlotDrawer.setCurveRatio(value);
			sankeyPlotDrawer.getSankeyPlotMain().recordTheFeatureInvoked(2);
		});

		barWidthspinner.addChangeListener(e -> {
			int value = (int) barWidthspinner.getValue();
			sankeyPlotDrawer.setBarWidth(value);
		});

		btnFontTitle.addActionListener(e -> {
			fontChanged(btnFontTitle, () -> {
				sankeyPlotDrawer.setTitleFont(btnFontTitle.getFont());
			});
		});
		btnFontName.addActionListener(e -> {
			fontChanged(btnFontName, () -> {
				sankeyPlotDrawer.setNameFont(btnFontName.getFont());
			});
		});
	}

	private void fontChanged(JButton button, CallBackBehavior callBackBehevior) {
		EGPSFontChooser fontChooser = new EGPSFontChooser();
		int result = fontChooser.showDialog(sankeyPlotDrawer);
		if (result == EGPSFontChooser.OK_OPTION) {
			Font font = fontChooser.getSelectedFont();
			button.setFont(font);
			callBackBehevior.doAfterCorrectClick();
		}
	}

	@Override
	public void removeListeners() {

	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("The visual effects options includes: ");
		sBuilder.append(transparentSpinner.getValue()).append(" % of transparency,");
		sBuilder.append(" rectangue bar width is ").append(barWidthspinner.getValue());
		sBuilder.append(", curve rate is ").append(curveRateSpinner.getValue()).append("%.");

		return sBuilder.toString();
	}

}
