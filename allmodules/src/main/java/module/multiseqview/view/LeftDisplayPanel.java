package module.multiseqview.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.EGPSFontChooser;
import egps2.utils.common.model.datatransfer.CallBackBehavior;
import module.multiseqview.SequenceStructureViewController;

public class LeftDisplayPanel extends JPanel{
	
	private JSpinner transparentSpinner;
	private JSpinner sequenceIntervalMarginSpinner;
	private JSpinner curveRateSpinner;
	
	private SequenceStructureViewController controller;
	private JButton btnFontTitle;
	private JButton btnFontName;
	

	public LeftDisplayPanel(SequenceStructureViewController controller) {
		this.controller = controller;

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 2, 0, 10));
		
		JLabel lblLeftBarWidth = new JLabel("Sequence height : ");
		lblLeftBarWidth.setFont(controller.defaultFont);
		add(lblLeftBarWidth);
		
		sequenceIntervalMarginSpinner = new JSpinner(new SpinnerNumberModel(20, 1, 100, 1));
		sequenceIntervalMarginSpinner.setFont(controller.defaultFont);
		add(sequenceIntervalMarginSpinner);
		
//		JLabel lblTopBarWidth = new JLabel("Transparency(0~255): ");
//		lblTopBarWidth.setFont(controller.defaultFont);
//		add(lblTopBarWidth);
//		
//		transparentSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 255, 20));
//		transparentSpinner.setFont(controller.defaultFont);
//		add(transparentSpinner);
		
		JLabel lblBottomBarWidth = new JLabel("Curve rate(%): ");
		lblBottomBarWidth.setFont(controller.defaultFont);
		add(lblBottomBarWidth);
		
		curveRateSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 100, 1));
		curveRateSpinner.setFont(controller.defaultFont);
		add(curveRateSpinner);
		
		JLabel lblTitleFont = new JLabel("Sequence font");
		lblTitleFont.setFont(controller.defaultFont);
		add(lblTitleFont);
		
		btnFontTitle = new JButton("Font");
		btnFontTitle.setFont(controller.defaultFont);
		add(btnFontTitle);
		
		JLabel lblNameFont = new JLabel("Name font");
		lblNameFont.setFont(controller.defaultFont);
		add(lblNameFont);
		
		btnFontName = new JButton("Font");
		btnFontName.setFont(controller.defaultFont);
		add(btnFontName);
		
		addListeners();
	}


	public void addListeners() {
//		transparentSpinner.addChangeListener( e ->{
//			int value = (int) transparentSpinner.getValue();
//			controller.setTransparent(value);
//		});
		
		curveRateSpinner.addChangeListener( e ->{
			double value = (int) curveRateSpinner.getValue() ;
			value /= 100;
			controller.setCurveRatio(value);
		});
		
		sequenceIntervalMarginSpinner.addChangeListener( e ->{
			int value = (int) sequenceIntervalMarginSpinner.getValue();
			controller.setIntervalMargin(value);
		});
		
		btnFontTitle.addActionListener(e ->{
			fontChanged(btnFontTitle, () ->{
				controller.setTitleFont(btnFontTitle.getFont());
			});
		});          
		btnFontName.addActionListener(e ->{
			fontChanged(btnFontName, () ->{
				controller.setNameFont(btnFontName.getFont());
			});
		});
	}
	
	private void fontChanged(JButton button,CallBackBehavior callBackBehevior) {
		EGPSFontChooser fontChooser = new EGPSFontChooser();
		int result = fontChooser.showDialog(controller.getMain());
		if (result == EGPSFontChooser.OK_OPTION) {
			Font font = fontChooser.getSelectedFont();
			button.setFont(font);
			callBackBehevior.doAfterCorrectClick();
		}
	}

}
