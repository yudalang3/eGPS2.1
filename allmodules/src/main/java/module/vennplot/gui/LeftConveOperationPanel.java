package module.vennplot.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionElement;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class LeftConveOperationPanel extends JPanel implements ParameterInitialized{

	private UpsetRPanelOuterPanel upsetRPanel;
	
	private JCheckBox chckbxShowLefttopPie;
	private JCheckBox chckbxShowLegend;
	private JCheckBox chckbxShowIntersectionValues;

	private VennPlotMain vennPlotMain;

	public LeftConveOperationPanel(VennPlotMain vennPlotMain) {
		this.upsetRPanel = vennPlotMain.getUpsetRPanel();
		this.vennPlotMain = vennPlotMain;
		
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		chckbxShowLefttopPie = new JCheckBox("Show lefttop pie");
		chckbxShowLefttopPie.setBackground(Color.WHITE);
		chckbxShowLefttopPie.setFont(vennPlotMain.defaultFont);
		add(chckbxShowLefttopPie);
		
		chckbxShowLegend = new JCheckBox("Show legend");
		chckbxShowLegend.setBackground(Color.WHITE);
		chckbxShowLegend.setFont(vennPlotMain.defaultFont);
		add(chckbxShowLegend);
		
		chckbxShowIntersectionValues = new JCheckBox("Show intersection values");
		chckbxShowIntersectionValues.setBackground(Color.WHITE);
		chckbxShowIntersectionValues.setFont(vennPlotMain.defaultFont);
		add(chckbxShowIntersectionValues);
		
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
				upsetRPanel.autoSizePanel();
			}
		});
		panel.add(btnAutosize);
		
		JButton btnRamdonColor = new JButton();
		btnRamdonColor.setToolTipText("<html><body>Set random color to all bars.");
		btnRamdonColor.setIcon(EGPSShellIcons.get("random.png"));
		btnRamdonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ParameterModel parameterModel = upsetRPanel.getParameterModel();
				List<IntersectionRegionElement> eles = parameterModel.getIntersectionRegionDataModel().getEles();
				for (IntersectionRegionElement intersectionRegionElement : eles) {
					intersectionRegionElement.setFilledColor(EGPSColors.randomColor());
				}
				
				List<SingleRegionElement> eles2 = parameterModel.getSingleRegionDataModel().getEles();
				for (SingleRegionElement singleRegionElement : eles2) {
					singleRegionElement.setFillColor(EGPSColors.randomColor());
				}
				
				upsetRPanel.weakestUpdate();
			}
		});
		panel.add(btnRamdonColor);
		
		JButton btnRemoveZero = new JButton();
		btnRemoveZero.setToolTipText("<html><body>Remove all zero intersection items.");
		ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("removePart.png"));
		btnRemoveZero.setIcon(imageIcon2);
		btnRemoveZero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ParameterModel parameterModel = upsetRPanel.getParameterModel();
				IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
				List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
				
				int size = eles.size();
				List<IntersectionRegionElement> newElements = new ArrayList<>(size);
				for (int i = 0; i < size; i++) {
					IntersectionRegionElement e2 = eles.get(i);
					if (e2.getCount() > 0) {
						newElements.add(e2);
					}
				}
				
				intersectionRegionDataModel.setEles(newElements);
				
				upsetRPanel.adjustPaintingElemetsForPaintingPartial();
				upsetRPanel.initializeControlPanels();
				upsetRPanel.weakestUpdate();
			}
		});
		panel.add(btnRemoveZero);
		
		JButton btnSearchItem = new JButton();
		btnSearchItem.setToolTipText("<html><body>Search an item and the result location will be displayed.");
		
		ImageIcon imageIcon = EGPSShellIcons.get("search.png");
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(16, 16, Image.SCALE_FAST));
		
		btnSearchItem.setIcon(imageIcon);
		btnSearchItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				upsetRPanel.searchSpecificItem();
			}
		});
		panel.add(btnSearchItem);
		
		
		JButton btnChange2annotherView = new JButton();
		btnChange2annotherView.setToolTipText("<html><body>Change to Plain text infor. tab.");
		ImageIcon imageIcon3 = new ImageIcon(getClass().getResource("view.png"));
		btnChange2annotherView.setIcon(imageIcon3);
		btnChange2annotherView.addActionListener(e ->{
			
			if (upsetRPanel.getDataModel().getNumOfSets() <= 5) {
				upsetRPanel.changeView2ClassicalPlot();
			} 
		});
		
		if (upsetRPanel.getDataModel().getNumOfSets() > 5) {
			btnChange2annotherView.setEnabled(false);
		} 
		
        panel.add(btnChange2annotherView);
	}

	@Override
	public void initializeParameters() {
		ParameterModel parameterModel = upsetRPanel.getParameterModel();
		chckbxShowLefttopPie.setSelected(parameterModel.isShowLeftPie());
		chckbxShowLegend.setSelected(parameterModel.isShowLegend());
		chckbxShowIntersectionValues.setSelected(parameterModel.isShowIntersectionValues());
	}

	@Override
	public void addListeners() {
		chckbxShowLefttopPie.addActionListener( e ->{
			upsetRPanel.getParameterModel().setShowLeftPie(chckbxShowLefttopPie.isSelected());
			upsetRPanel.weakestUpdate();
			
			vennPlotMain.invokeTheFeatureMethod(4);
		});
		chckbxShowLegend.addActionListener( e ->{
			upsetRPanel.getParameterModel().setShowLegend(chckbxShowLegend.isSelected());
			upsetRPanel.weakestUpdate();
		});
		
		chckbxShowIntersectionValues.addActionListener( e ->{
			upsetRPanel.getParameterModel().setShowIntersectionValues(chckbxShowIntersectionValues.isSelected());
			upsetRPanel.weakestUpdate();
		});
	}

	@Override
	public void removeListeners() {
		
	}

}
