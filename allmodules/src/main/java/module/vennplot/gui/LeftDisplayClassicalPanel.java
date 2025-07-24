package module.vennplot.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.EGPSFontChooser;
import egps2.panels.dialog.EGPSJSpinner;
import egps2.utils.common.model.datatransfer.CallBackBehavior;
import module.vennplot.model.ClassicalParameterModel;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class LeftDisplayClassicalPanel extends JPanel implements ParameterInitialized {

	final private ClassicalVennPlotOutterFrame classicalVennPlotPanel;

	private JButton btnFontIntersectionValue;
	private JButton btnFontName;

	private EGPSJSpinner radicus;

	private JCheckBox showSetValues;

	private JCheckBox showSetLegend;

	/**
	 * Create the panel.
	 * 
	 * @param upsetRPanel
	 */
	public LeftDisplayClassicalPanel(ClassicalVennPlotOutterFrame classicalVennPlotPanel) {
		this.classicalVennPlotPanel = classicalVennPlotPanel;

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 2, 0, 4));

		showSetValues = new JCheckBox("Show set values");
		showSetValues.setSelected(true);
		classicalVennPlotPanel.getParameterModel().setShowSetValues(showSetValues.isSelected());
		showSetValues.setFont(classicalVennPlotPanel.defaultFont);
		showSetValues.setFocusPainted(false);
		showSetValues.setBackground(Color.white);
		add(showSetValues);

		showSetLegend = new JCheckBox("Show set legend");
		showSetLegend.setSelected(true);
		classicalVennPlotPanel.getParameterModel().setShowSetLegend(showSetLegend.isSelected());
		showSetLegend.setFont(classicalVennPlotPanel.defaultFont);
		showSetLegend.setFocusPainted(false);
		showSetLegend.setBackground(Color.white);
		add(showSetLegend);

		JLabel lblIntersectionValueFont = new JLabel("IntersectionValue font");
		lblIntersectionValueFont.setFont(classicalVennPlotPanel.defaultFont);
		add(lblIntersectionValueFont);

		btnFontIntersectionValue = new JButton("Font");
		btnFontIntersectionValue.setFocusPainted(false);
		btnFontIntersectionValue.setFont(classicalVennPlotPanel.defaultFont);
		add(btnFontIntersectionValue);

		JLabel lblNameFont = new JLabel("Name font");
		lblNameFont.setFont(classicalVennPlotPanel.defaultFont);
		add(lblNameFont);
		btnFontName = new JButton("Font");
		btnFontName.setFocusPainted(false);
		btnFontName.setFont(classicalVennPlotPanel.defaultFont);
		add(btnFontName);

		JLabel circleRadicus = new JLabel("Circle radicus");
		circleRadicus.setFont(classicalVennPlotPanel.defaultFont);
		add(circleRadicus);

		radicus = new EGPSJSpinner(200, 0, 500, 1);

		radicus.setFont(classicalVennPlotPanel.defaultFont);
		add(radicus);

	}

	@Override
	public void initializeParameters() {
		ClassicalParameterModel parameterModel = getParameterModel();
		btnFontIntersectionValue.setFont(parameterModel.getIntersectionValueFont());
		btnFontName.setFont(parameterModel.getNameFont());

	}

	@Override
	public void addListeners() {
		btnFontIntersectionValue.addActionListener(e -> {
			fontChanged(btnFontIntersectionValue, () -> {
				getParameterModel().setIntersectionValueFont(btnFontIntersectionValue.getFont());
				classicalVennPlotPanel.weakestUpdate();
			});
		});
		btnFontName.addActionListener(e -> {
			fontChanged(btnFontName, () -> {
				getParameterModel().setNameFont(btnFontName.getFont());
				classicalVennPlotPanel.weakestUpdate();
			});
		});

		radicus.addChangeListener(e -> {

			classicalVennPlotPanel.getPaintingPanel().setCircleR((int) radicus.getValue());
			classicalVennPlotPanel.weakestUpdate();
		});

		showSetValues.addActionListener(e -> {
			classicalVennPlotPanel.getParameterModel().setShowSetValues(showSetValues.isSelected());
			classicalVennPlotPanel.weakestUpdate();
		});
		showSetLegend.addActionListener(e -> {
			classicalVennPlotPanel.getParameterModel().setShowSetLegend(showSetLegend.isSelected());
			classicalVennPlotPanel.weakestUpdate();
		});

	}

	private ClassicalParameterModel getParameterModel() {
		return classicalVennPlotPanel.getParameterModel();
	}

	private void fontChanged(JButton button, CallBackBehavior callBackBehevior) {
		EGPSFontChooser fontChooser = new EGPSFontChooser();
		int result = fontChooser.showDialog(classicalVennPlotPanel);
		if (result == EGPSFontChooser.OK_OPTION) {
			Font font = fontChooser.getSelectedFont();
			button.setFont(font);
			callBackBehevior.doAfterCorrectClick();
		}
	}

	@Override
	public void removeListeners() {
		
	}

}
