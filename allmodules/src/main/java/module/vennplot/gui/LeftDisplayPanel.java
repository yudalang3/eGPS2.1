package module.vennplot.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import egps2.utils.common.model.datatransfer.CallBackBehavior;
import egps2.frame.gui.EGPSMainGuiUtil;
import module.vennplot.VennPlotMain;
import module.vennplot.model.ParameterModel;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class LeftDisplayPanel extends JPanel implements ParameterInitialized{

	final private UpsetRPanelOuterPanel upsetRPanel;
	private JSpinner topBarWidthspinner;
	private JSpinner leftBarWidthspinner;
	private JSpinner bottomBarWidthspinner;
	private JSpinner circleSizespinner;
	private JButton btnTitleFont;
	private JButton btnNameFont;
	private JButton btnNumberFont;
	private VennPlotMain vennPlotMain;

	/**
	 * Create the panel.
	 * @param vennPlotMain 
	 */
	public LeftDisplayPanel(VennPlotMain vennPlotMain) {
		this.upsetRPanel = vennPlotMain.getUpsetRPanel();
		this.vennPlotMain = vennPlotMain;
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 2, 0, 10));
		
		JLabel lblLeftBarWidth = new JLabel("Left bar width");
		lblLeftBarWidth.setFont(vennPlotMain.defaultFont);
		add(lblLeftBarWidth);
		
		leftBarWidthspinner = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
		leftBarWidthspinner.setFont(vennPlotMain.defaultFont);
		add(leftBarWidthspinner);
		
		JLabel lblTopBarWidth = new JLabel("Top bar width");
		lblTopBarWidth.setFont(vennPlotMain.defaultFont);
		add(lblTopBarWidth);
		
		topBarWidthspinner = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
		topBarWidthspinner.setFont(vennPlotMain.defaultFont);
		add(topBarWidthspinner);
		
		JLabel lblBottomBarWidth = new JLabel("Connecting line width");
		lblBottomBarWidth.setFont(vennPlotMain.defaultFont);
		add(lblBottomBarWidth);
		
		bottomBarWidthspinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
		bottomBarWidthspinner.setFont(vennPlotMain.defaultFont);
		add(bottomBarWidthspinner);
		
		JLabel lblCircleSize = new JLabel("Circle size");
		lblCircleSize.setFont(vennPlotMain.defaultFont);
		add(lblCircleSize);
		
		circleSizespinner = new JSpinner(new SpinnerNumberModel(5, 1, 40, 1));
		circleSizespinner.setFont(vennPlotMain.defaultFont);
		add(circleSizespinner);
		
		JLabel lblTitleFont = new JLabel("Title font");
		lblTitleFont.setFont(vennPlotMain.defaultFont);
		add(lblTitleFont);
		
		btnTitleFont = new JButton("Font");
		btnTitleFont.setFont(vennPlotMain.defaultFont.deriveFont(16f));
		add(btnTitleFont);
		
		JLabel lblNameFont = new JLabel("Name font");
		lblNameFont.setFont(vennPlotMain.defaultFont);
		add(lblNameFont);
		
		btnNameFont = new JButton("Font");
		btnNameFont.setFont(vennPlotMain.defaultFont);
		add(btnNameFont);
		
		
		JLabel lblNumberFont = new JLabel("Number font");
		lblNumberFont.setFont(vennPlotMain.defaultFont);
		add(lblNumberFont);
		
		btnNumberFont = new JButton("Font");
		btnNumberFont.setFont(vennPlotMain.defaultFont.deriveFont(8f));
		add(btnNumberFont);

	}
	
	private ParameterModel getParameterModel() {
		return upsetRPanel.getParameterModel();
	}

	@Override
	public void initializeParameters() {

		ParameterModel parameterModel = getParameterModel();
		topBarWidthspinner.setValue((int) parameterModel.getTopBarWidth());
		leftBarWidthspinner.setValue((int) parameterModel.getLeftBarWidth());
		bottomBarWidthspinner.setValue((int) parameterModel.getLinkingBarThick());
		circleSizespinner.setValue((int) parameterModel.getRoundRadius());
		
	}

	@Override
	public void addListeners() {
		
		topBarWidthspinner.addChangeListener(e ->{
			getParameterModel().setTopBarWidth((int) topBarWidthspinner.getValue());
			upsetRPanel.weakestUpdate();
		});
		leftBarWidthspinner.addChangeListener( e ->{
			getParameterModel().setLeftBarWidth((int) leftBarWidthspinner.getValue());
			upsetRPanel.weakestUpdate();
		});  
		bottomBarWidthspinner.addChangeListener(e ->{
			getParameterModel().setLinkingBarThick((int) bottomBarWidthspinner.getValue());
			upsetRPanel.weakestUpdate();
		});
		circleSizespinner.addChangeListener(e ->{
			getParameterModel().setRoundRadius((int) circleSizespinner.getValue());
			upsetRPanel.weakestUpdate();
		}); 
		btnTitleFont.addActionListener(e ->{
			fontChanged(btnTitleFont, () ->{
				getParameterModel().setTitleFont(btnTitleFont.getFont());
				upsetRPanel.weakestUpdate();
			});
		});          
		btnNameFont.addActionListener(e ->{
			fontChanged(btnNameFont, () ->{
				getParameterModel().setNameFont(btnNameFont.getFont());
				upsetRPanel.weakestUpdate();
			});
		});
		btnNumberFont.addActionListener(e ->{
			fontChanged(btnNumberFont, () ->{
				getParameterModel().setNumberFont(btnNumberFont.getFont());
				upsetRPanel.weakestUpdate();
			});
		});
		
		getParameterModel().setTitleFont(btnTitleFont.getFont());
		getParameterModel().setNameFont(btnNameFont.getFont());
		getParameterModel().setNumberFont(btnNumberFont.getFont());
		
	}
	
	private void fontChanged(JButton button,CallBackBehavior callBackBehevior) {
		EGPSMainGuiUtil.fontChanged(button, callBackBehevior);
	}

	@Override
	public void removeListeners() {
		
	}

}
