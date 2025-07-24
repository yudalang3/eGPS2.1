package module.vennplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.vennplot.VennPlotMain;
import module.vennplot.model.Element;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SetItem;
import module.vennplot.model.SingleRegionDataModel;
import module.vennplot.model.SingleRegionElement;
import module.vennplot.util.AdjustAndCalculate;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class LeftExporaryAnalysisPanel extends JPanel implements ParameterInitialized{
	
	private VennPlotMain vennPlotMain;
	
	private JCheckBox chckbxEnableAnalysis;
	private JPanel informationBox;
	
	private JButton btnAddItem;
	private JButton btnRemoveLast;
	private JButton btnClear;
	
	private AdjustAndCalculate adjust =  new AdjustAndCalculate();
	/**
	 * Create the panel.
	 * @param vennPlotMain 
	 */
	public LeftExporaryAnalysisPanel(VennPlotMain vennPlotMain) {
		this.vennPlotMain = vennPlotMain;
		setLayout(new BorderLayout(0, 0));
		
		vennPlotMain.getUpsetRPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(0.7);
		splitPane.setDividerSize(8);
		add(splitPane, BorderLayout.CENTER);
		
		JPanel leftJpanel = new JPanel();
		leftJpanel.setBackground(Color.WHITE);
		splitPane.setLeftComponent(leftJpanel);
		leftJpanel.setLayout(new BoxLayout(leftJpanel, BoxLayout.Y_AXIS));
		
		chckbxEnableAnalysis = new JCheckBox("Enable analysis");
		chckbxEnableAnalysis.setBackground(Color.WHITE);
		chckbxEnableAnalysis.setFont(vennPlotMain.defaultFont);
		leftJpanel.add(chckbxEnableAnalysis);
		
		informationBox = new JPanel();
		informationBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		informationBox.setBackground(Color.WHITE);
		TitledBorder border = new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Set information", TitledBorder.LEADING, TitledBorder.TOP, null, null);
		border.setTitleFont(vennPlotMain.defaultFont);
		informationBox.setBorder(border);
		
		leftJpanel.setPreferredSize(new Dimension(300, 150));
		leftJpanel.add(new JScrollPane(informationBox));
		
		informationBox.setLayout(new BoxLayout(informationBox, BoxLayout.Y_AXIS));
		
		
		JPanel rightJpanel = new JPanel();
		rightJpanel.setBackground(Color.WHITE);
		rightJpanel.setBorder(new EmptyBorder(10, 10, 0, 0));
		splitPane.setRightComponent(rightJpanel);
		rightJpanel.setLayout(new BoxLayout(rightJpanel, BoxLayout.Y_AXIS));
		
		JLabel lblExExclude = new JLabel("Ex : exclude this set");
		lblExExclude.setFont(vennPlotMain.defaultFont);
		rightJpanel.add(lblExExclude);
		
		JLabel lblInInclude = new JLabel("In : include this set");
		lblInInclude.setFont(vennPlotMain.defaultFont);
		rightJpanel.add(lblInInclude);
		
		JLabel lblNotNot = new JLabel("Not : not take into consideration");
		lblNotNot.setFont(vennPlotMain.defaultFont);
		rightJpanel.add(lblNotNot);
		
		rightJpanel.add(Box.createVerticalGlue());
		
		JPanel jPanel = new JPanel(new FlowLayout());
		jPanel.setAlignmentX(0f);
		jPanel.setBackground(Color.white);
		
		
		btnAddItem = new JButton("Add item");
		btnAddItem.setFont(vennPlotMain.defaultFont);
		jPanel.add(btnAddItem);
		
		btnClear = new JButton("Clear");
		btnClear.setFont(vennPlotMain.defaultFont);
		jPanel.add(btnClear);
		
		btnRemoveLast = new JButton("Remove last");
		btnRemoveLast.setFont(vennPlotMain.defaultFont);
		jPanel.add(btnRemoveLast);
		
		rightJpanel.add(jPanel);
	}
	
	void addInformationBox(String setName){
		informationBox.add(new InformationItemPanel(setName));
	}
	@Override
	public void initializeParameters() {
		if (informationBox.getComponentCount() == 0) {
			chckbxEnableAnalysis.setSelected(false);
			
			enableAllComponents(false);
			UpsetRPanelOuterPanel upsetRPanel = vennPlotMain.getUpsetRPanel();
			ParameterModel parameterModel = upsetRPanel.getParameterModel();
			SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
			List<SingleRegionElement> eles = singleRegionDataModel.getEles();
			
			int size = eles.size();
			for (int i = 0; i < size; i++) {
				for (SingleRegionElement singleRegionElement : eles) {
					if (singleRegionElement.getOriginalIndex() == i) {
						
						addInformationBox(singleRegionElement.getSetName());
						break;
					}
				}
			}
			
		}
	}
	
	public void enableAllComponents(boolean tt) {
		btnAddItem.setEnabled(tt);
		btnRemoveLast.setEnabled(tt);
		btnClear.setEnabled(tt);
		
		Component[] components = informationBox.getComponents();
		int aa = components.length;
		for (int i = 0; i < aa; i++) {
			InformationItemPanel pp = (InformationItemPanel) components[i];
			pp.enableAllComponents(tt);
		}
		informationBox.revalidate();
	}
	@Override
	public void addListeners() {
		
		UpsetRPanelOuterPanel upsetRPanel = vennPlotMain.getUpsetRPanel();
		chckbxEnableAnalysis.addActionListener(e ->{
			if(chckbxEnableAnalysis.isSelected()) {
				IntersectionRegionDataModel intersectionRegionDataModel = upsetRPanel.getParameterModel().getIntersectionRegionDataModel();
				intersectionRegionDataModel.getEles().clear();
				
				upsetRPanel.adjustPaintingElemetsForPaintingPartial();
				upsetRPanel.initializeControlPanels();
				upsetRPanel.weakestUpdate();
				
				enableAllComponents(true);
			}else {
				upsetRPanel.initializePanel();
				
			}
		});
		
		btnAddItem.addActionListener(e ->{
			
			boolean ifContainsNot = false;
			boolean ifNotContainsInclude = true;
			
			Component[] components = informationBox.getComponents();
			int aa = components.length;
			byte[] ba = new byte[aa];
			
			for (int i = 0; i < aa; i++) {
				InformationItemPanel tt = (InformationItemPanel) components[i];
				byte considerIndex = tt.getConsiderIndex();
				ba[i] = considerIndex;
				
				if (considerIndex == -1) {
					ifContainsNot = true;
				}else if(considerIndex == 1){
				    ifNotContainsInclude = false;
                }else {
                    
                }
			}
			
            if (ifNotContainsInclude) {
                SwingDialog.showErrorMSGDialog("Input error",
                    "Sorry!\nYou shoud input at least one set to Include item!");
                return;
            }
		        
			ParameterModel parameterModel = upsetRPanel.getParameterModel();
			if (ifContainsNot) {
				parameterModel.setContainsNotTakeintoCon(ifContainsNot);
			}
			
			IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
			
			
			IntersectionRegionElement intersectionRegionElement = new IntersectionRegionElement();
			intersectionRegionElement.setBooleans(ba);
			intersectionRegionElement.setFilledColor(Color.black);
			
			SetItem[] setItems = upsetRPanel.getDataModel().getSetItems();

			Element[] elementNames = adjust.getSetAfterIntersectAndComplement(ba, setItems);
			int length = elementNames.length;
			intersectionRegionElement.setCount(length);
			intersectionRegionElement.setElements(elementNames);
			intersectionRegionDataModel.getEles().add(intersectionRegionElement);
			
			initializePlotProperties(intersectionRegionDataModel);
			
			
			upsetRPanel.adjustPaintingElemetsForPaintingPartial();
			upsetRPanel.initializeControlPanels();
			upsetRPanel.weakestUpdate();
		});
		btnRemoveLast.addActionListener(e ->{
			ParameterModel parameterModel = upsetRPanel.getParameterModel();
			IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
			
			List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
			
			if (eles.size() == 0) {
				return;
			}else if (eles.size() == 1) {
				parameterModel.setContainsNotTakeintoCon(false);
			}
			eles.remove(eles.size() - 1);
			
			upsetRPanel.adjustPaintingElemetsForPaintingPartial();
			upsetRPanel.initializeControlPanels();
			upsetRPanel.weakestUpdate();
		});
		btnClear.addActionListener(e ->{
			ParameterModel parameterModel = upsetRPanel.getParameterModel();
			IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
			intersectionRegionDataModel.getEles().clear();
			
			parameterModel.setContainsNotTakeintoCon(false);
			
			upsetRPanel.adjustPaintingElemetsForPaintingPartial();
			upsetRPanel.initializeControlPanels();
			upsetRPanel.weakestUpdate();
		});
		
	}

	private void initializePlotProperties(IntersectionRegionDataModel intersectionRegionDataModel) {
		List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
		int highestValue = 0;
		for (IntersectionRegionElement intersectionRegionElement : eles) {
			int length = intersectionRegionElement.getCount();
			if (length > highestValue) {
				highestValue = length;
			}
		}

		intersectionRegionDataModel.setHigestNumber(highestValue);
		int expendHigestNumber = getExpendHigestNumber(highestValue);
		intersectionRegionDataModel.setExpendHigestNumber(expendHigestNumber);
		// TO get highest value and set ratio
		for (IntersectionRegionElement intersectionRegionElement : eles) {
			int count = intersectionRegionElement.getCount();
			intersectionRegionElement.setRatio_this2highest(count / (float) expendHigestNumber);
		}
		
		Collections.sort(intersectionRegionDataModel.getEles());
		
	}
	
	private final int getExpendHigestNumber(int highest) {
		/**
		 * Temporary we see the result!
		 */
		int ret = (int) (1.1 * highest);
		if (ret == highest) {
			ret++;
		}

		return ret;
	}

	@Override
	public void removeListeners() {
		
	}

}

@SuppressWarnings("serial")
class InformationItemPanel extends JPanel{
	
	private JRadioButton rdbtnIn;
	private JRadioButton rdbtnEx;
	private JRadioButton rdbtnNot;
	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	public InformationItemPanel(String setName) {
		this.setBackground(Color.white);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		JLabel lblNewLabel = new JLabel(setName+" ");
		lblNewLabel.setFont(defaultFont);
		lblNewLabel.setPreferredSize(new Dimension(90, 15));
		this.add(lblNewLabel);
		
		rdbtnIn = new JRadioButton("In");
		rdbtnIn.setBackground(Color.white);
		rdbtnIn.setFont(defaultFont);
		this.add(rdbtnIn);
		
		rdbtnEx = new JRadioButton("Ex");
		rdbtnEx.setBackground(Color.white);
		rdbtnEx.setFont(defaultFont);
		this.add(rdbtnEx);
		
		rdbtnNot = new JRadioButton("Not");
		rdbtnNot.setBackground(Color.white);
		rdbtnNot.setFont(defaultFont);
		this.add(rdbtnNot);
		
		ButtonGroup colorButtonGroup = new ButtonGroup();
	    colorButtonGroup.add(rdbtnIn);
	    colorButtonGroup.add(rdbtnEx);
	    colorButtonGroup.add(rdbtnNot);
	    rdbtnIn.setSelected(true);
	    
	}
	
	public void enableAllComponents(boolean tt) {
		rdbtnIn.setEnabled(tt);
		rdbtnEx.setEnabled(tt);
		rdbtnNot.setEnabled(tt);
		
	}

	/**
	 * @return 1 for include; 0 for exclude;-1 for not take into consideration!
	 */
	public byte getConsiderIndex() {
		if (rdbtnIn.isSelected()) {
			return 1;
		}else if (rdbtnEx.isSelected()) {
			return 0;
		}else {
			return -1;
		}
	}
	
}
