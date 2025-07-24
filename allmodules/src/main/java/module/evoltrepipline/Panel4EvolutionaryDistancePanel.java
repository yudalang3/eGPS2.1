/**
 * 
 */
package module.evoltrepipline;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.math.CheckedNumber;

/**
* @author YFQ
* @date 2019-11-21 10:11:46
* @version 1.0
* 
 * @author ydl
 * 
 * @Date 2024-04-27
* <p>Description:</p>
*/
public class Panel4EvolutionaryDistancePanel extends AbstractPrefShowContent{

	private static final long serialVersionUID = 2400968238973355748L;
	
	private ConstantNameClass_EvolutionaryDistance ccgd = new ConstantNameClass_EvolutionaryDistance();
	
	private JTextField bootstrapText;
	private JComboBox<String> methodComboBox;
	private JComboBox<String> sitesCombox;
	private JComboBox<String> gapsCombox;
	private JComboBox<String> modelComboBox;
	private JTextField cutText;

	private Map<String, String> parameterMap;

	private JPanel contentPanel;
	
	public Panel4EvolutionaryDistancePanel(Map<String, String> parameterMap, String evolutionaryDistanceName) {
		super.userObject = evolutionaryDistanceName;
		this.parameterMap = parameterMap;
	}

	@Override
	public JPanel getViewJPanel() {
		if (contentPanel != null) {
			return contentPanel;
		}
		
		contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JPanel geneticDistancePanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
	
		JPanel estimatePanel = new JPanel(new GridBagLayout());
	
		JPanel modelPanel = new JPanel(new GridBagLayout());
	
		JPanel ratesPanel = new JPanel(new GridBagLayout());
	
		JPanel dataPanel = new JPanel(new GridBagLayout());
	
		estimatePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccgd.category1_estimateVariance, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));
	
		JLabel methodLabel = new JLabel(ccgd.label1_varianEstimateMethod);
		methodLabel.setFont(defaultFont);
	
		methodComboBox = new JComboBox<String>();
		methodComboBox.addItem(ccgd.varianEstimateMethod_value1_none);
		methodComboBox.addItem(ccgd.varianEstimateMethod_value2_bs);
		methodComboBox.setFont(defaultFont);
		methodComboBox.setFocusable(false);
	
		JLabel bootstrapLabel = new JLabel(ccgd.label2_numOfBSRep);
		bootstrapLabel.setFont(defaultFont);
	
		bootstrapText = new JTextField();
		bootstrapText.setFont(defaultFont);
	
		String geneticDistanceVarianceIndexValue = parameterMap.get(ccgd.label1_varianEstimateMethod_index);
	
		if (geneticDistanceVarianceIndexValue != null && !geneticDistanceVarianceIndexValue.equals("")) {
			methodComboBox.setSelectedIndex(Integer.valueOf(geneticDistanceVarianceIndexValue));
		} else {
			methodComboBox.setSelectedIndex(0);
		}
	
		String geneticDistanceNumOfBootstrapRepValue = parameterMap.get(ccgd.label2_numOfBSRep_index);
		if (methodComboBox.getSelectedIndex() == 0) {
			bootstrapText.setText(ccgd.numOfBSRep_value1_NotAppli);
			bootstrapText.setToolTipText(null);
			bootstrapText.setEnabled(false);
		} else if (geneticDistanceNumOfBootstrapRepValue != null && !geneticDistanceNumOfBootstrapRepValue.equals("")
				&& !geneticDistanceNumOfBootstrapRepValue.equals(ccgd.numOfBSRep_value1_NotAppli)) {
			bootstrapText.setText(geneticDistanceNumOfBootstrapRepValue);
			bootstrapText.setToolTipText("The value must be between 1 and 10000.");
			bootstrapText.setEnabled(true);
		} else {
			bootstrapText.setText(ccgd.numOfBSRep_value1_NotAppli);
			bootstrapText.setToolTipText(null);
			bootstrapText.setEnabled(false);
		}
	
		methodComboBox.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = methodComboBox.getSelectedIndex();
				if (id == 0) {
					bootstrapText.setText(ccgd.numOfBSRep_value1_NotAppli);
					bootstrapText.setToolTipText(null);
					bootstrapText.setEnabled(false);
				} else {
					bootstrapText.setText("500");
					bootstrapText.setToolTipText("The value must be between 1 and 10000.");
					bootstrapText.setEnabled(true);
					bootstrapText.requestFocus();
				}
			}
		});
		
		estimatePanel.add(methodLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		estimatePanel.add(methodComboBox,gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		estimatePanel.add(bootstrapLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		estimatePanel.add(bootstrapText,gridBagConstraints);
	
		modelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccgd.category2_substitutionModel, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));
	
		JLabel typeLabel = new JLabel(ccgd.label1_substitutionType);
		typeLabel.setFont(defaultFont);
	
		JLabel nucleotideLabel = new JLabel(ccgd.substitutionType_value1_nuc);
		nucleotideLabel.setFont(defaultFont);
	
		JLabel modelLabel = new JLabel(ccgd.label1_modelOrMethod);
		modelLabel.setFont(defaultFont);
	
		modelComboBox = new JComboBox<String>();
		for (String string : ccgd.modelOrMethod_values) {
			modelComboBox.addItem(string);
		}
	
		modelComboBox.setFont(defaultFont);
		
		// be caution of this value!
		final int indexOf4DTv = 7;
		javax.swing.ListCellRenderer<Object> comboRenderer = new DefaultListCellRenderer() {
	
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
	
				if (isSelected) {
					if (indexOf4DTv == index && null != value) {
			            list.setToolTipText("Recommend to calculate 4DTv rates (the rate of transversion at fourfold degenerate sites)");
			        } else {
						list.setToolTipText("Calculate genetic distance for any pair of sequence.");
					}
				} else {
					list.setToolTipText(null);
				}
	
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
	
		};
		modelComboBox.setRenderer(comboRenderer);
		String modelIndexValue = parameterMap.get(ccgd.label1_modelOrMethod_index);
	
		if (modelIndexValue != null && !modelIndexValue.equals("")) {
			modelComboBox.setSelectedIndex(Integer.valueOf(modelIndexValue));
		} else {
			modelComboBox.setSelectedIndex(0);
		}
	
		JLabel includeLabel = new JLabel(ccgd.label1_substitutionToInclude);
		includeLabel.setFont(defaultFont);
	
		JLabel transitionsLabel = new JLabel(ccgd.substitutionToInclude_value1_two);
		transitionsLabel.setFont(defaultFont);
	
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		modelPanel.add(typeLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		modelPanel.add(nucleotideLabel,gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		modelPanel.add(modelLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		modelPanel.add(modelComboBox,gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		modelPanel.add(includeLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		modelPanel.add(transitionsLabel,gridBagConstraints);
	
		ratesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccgd.category3_ratesAndPatterns, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));
	
		JLabel sitesLabel = new JLabel(ccgd.label1_ratesAmongSites);
		sitesLabel.setFont(defaultFont);
	
		sitesCombox = new JComboBox<String>();
		sitesCombox.addItem(ccgd.ratesAmongSites_value1_uni);
		sitesCombox.addItem(ccgd.ratesAmongSites_value2_gamma);
		/*sitesCombox.addItem(ccg.ratesAmongSites_value3_hasI);
		sitesCombox.addItem(ccg.ratesAmongSites_value4_gPlusI);*/
		sitesCombox.setFont(defaultFont);
	
		sitesCombox.setEnabled(false);
		sitesCombox.setFocusable(false);
	
		String geneticDistanceRatesPatternsIndexValue = parameterMap.get(ccgd.label1_ratesAmongSites_index);
	
		if (geneticDistanceRatesPatternsIndexValue != null && !geneticDistanceRatesPatternsIndexValue.equals("")) {
			sitesCombox.setSelectedIndex(Integer.valueOf(geneticDistanceRatesPatternsIndexValue));
		} else {
			sitesCombox.setSelectedIndex(0);
		}
	
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		ratesPanel.add(sitesLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		ratesPanel.add(sitesCombox,gridBagConstraints);
	
		dataPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccgd.category4_dataSubSetTouse, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));
	
		JLabel gapsLabel = new JLabel(ccgd.label1_treatment);
		gapsLabel.setFont(defaultFont);
	
		gapsCombox = new JComboBox<String>();
		gapsCombox.addItem(ccgd.treatment_value1_compD);
		gapsCombox.addItem(ccgd.treatment_value2_pairD);
		gapsCombox.addItem(ccgd.treatment_value3_partD);
		gapsCombox.setFont(defaultFont);
		gapsCombox.setFocusable(false);
	
		JLabel cutLabel = new JLabel(ccgd.label2_siteCoverageCutoff);
		cutLabel.setFont(defaultFont);
	
		cutText = new JTextField();
	
		cutText.setFont(defaultFont);
	
		String geneticDistanceDataSubsetIndexValue = parameterMap.get(ccgd.label1_treatment_index);
	
		if (geneticDistanceDataSubsetIndexValue != null && !geneticDistanceDataSubsetIndexValue.equals("")) {
			gapsCombox.setSelectedIndex(Integer.valueOf(geneticDistanceDataSubsetIndexValue));
		} else {
			gapsCombox.setSelectedIndex(0);
		}
	
		String siteCoverageCutoffValue = parameterMap.get(ccgd.label2_siteCoverageCutoff_index);
	
		if (gapsCombox.getSelectedIndex() == 2) {
			if (siteCoverageCutoffValue != null && !siteCoverageCutoffValue.equals("")
					&& !siteCoverageCutoffValue.equals("Not applicable")) {
				cutText.setText(siteCoverageCutoffValue);
				cutText.setToolTipText("The value must be between 0 and 99.");
				cutText.setEnabled(true);
			} else {
				cutText.setText(ccgd.siteCoverageCutoff_value1_notAppli);
				cutText.setToolTipText(null);
				cutText.setEnabled(false);
			}
		} else {
			cutText.setText(ccgd.siteCoverageCutoff_value1_notAppli);
			cutText.setToolTipText(null);
			cutText.setEnabled(false);
		}
	
		gapsCombox.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = gapsCombox.getSelectedIndex();
				if (num == 2) {
					cutText.setText("75");
					cutText.setToolTipText("The value must be between 0 and 99.");
					cutText.setEnabled(true);
					cutText.requestFocus();
				} else {
					cutText.setText(ccgd.siteCoverageCutoff_value1_notAppli);
					cutText.setToolTipText(null);
					cutText.setEnabled(false);
				}
			}
		});
	
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		dataPanel.add(gapsLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		dataPanel.add(gapsCombox,gridBagConstraints);
	
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		dataPanel.add(cutLabel,gridBagConstraints);
		gridBagConstraints.gridx = 1;
		dataPanel.add(cutText,gridBagConstraints);
	
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		geneticDistancePanel.add(estimatePanel,gridBagConstraints);
		gridBagConstraints.gridy = 1;
		geneticDistancePanel.add(modelPanel,gridBagConstraints);
		gridBagConstraints.gridy = 2;
		geneticDistancePanel.add(ratesPanel,gridBagConstraints);
		gridBagConstraints.gridy = 3;
		geneticDistancePanel.add(dataPanel,gridBagConstraints);
		
		contentPanel.add(geneticDistancePanel);
		
		return contentPanel;
	}

	@Override
	public void saveParameter(Map<String, String> parameters) {
		String method = methodComboBox.getSelectedItem().toString();
		int methodID = methodComboBox.getSelectedIndex();
		String boot = bootstrapText.getText();
		String sites = sitesCombox.getSelectedItem().toString();
		int sitesID = sitesCombox.getSelectedIndex();
		String gaps = gapsCombox.getSelectedItem().toString();
		int gapsID = gapsCombox.getSelectedIndex();
		String model = modelComboBox.getSelectedItem().toString();
		int modelID = modelComboBox.getSelectedIndex();
		String cut = cutText.getText();
		
		parameters.put(ccgd.label1_varianEstimateMethod, method);
		parameters.put(ccgd.label1_varianEstimateMethod_index, String.valueOf(methodID));
		parameters.put(ccgd.label2_numOfBSRep_index, boot);
		parameters.put(ccgd.label1_ratesAmongSites, sites);
		parameters.put(ccgd.label1_ratesAmongSites_index, String.valueOf(sitesID));
		parameters.put(ccgd.label1_treatment, gaps);
		parameters.put(ccgd.label1_treatment_index, String.valueOf(gapsID));
		parameters.put(ccgd.label1_modelOrMethod, model);
		parameters.put(ccgd.label1_modelOrMethod_index, String.valueOf(modelID));
		parameters.put(ccgd.label2_siteCoverageCutoff_index, cut);
	}

	@Override
	public boolean checkParameters(JTree jTree) {
		
		int methodId = methodComboBox.getSelectedIndex();
		int gapsId = gapsCombox.getSelectedIndex();
		boolean flagMethod = false;
		boolean flagGaps = false;
		
		try {
			if (methodId == 1) {
				String bootValue = bootstrapText.getText();
				boolean bootFlag = CheckedNumber.isPositiveInteger(bootValue, false);
				if (bootFlag) {
					int bootNumber = Integer.valueOf(bootValue);
					if (bootNumber >= 1 && bootNumber <= 10000) {
						flagMethod = true;
					} else {
						bootstrapText.requestFocus();
						bootstrapText.selectAll();
						SwingDialog.showMSGDialog("Error", "The value must be between 1 and 10000!", 1);
					}
				} else {
					bootstrapText.requestFocus();
					bootstrapText.selectAll();
					SwingDialog.showMSGDialog("Error", "The value must be between 1 and 10000!", 1);
				}
			} else {
				flagMethod = true;
			}
		} catch (Exception e1) {
			bootstrapText.requestFocus();
			bootstrapText.selectAll();
			SwingDialog.showMSGDialog("Error", "The value must be between 1 and 10000!", 1);
		}
		try {
			if (flagMethod == true) {
				if (gapsId == 2) {
					String cutValue = cutText.getText();
					boolean cutFlag = CheckedNumber.isPositiveInteger(cutValue, false);
					if (cutFlag) {
						int cutNumber = Integer.valueOf(cutValue);
						if (cutNumber >= 0 && cutNumber <= 99) {
							flagGaps = true;
						} else {
							cutText.requestFocus();
							cutText.selectAll();
							SwingDialog.showMSGDialog("Error", "The value must be between 0 and 99!", 1);
						}
					} else {
						cutText.requestFocus();
						cutText.selectAll();
						SwingDialog.showMSGDialog("Error", "The value must be between 0 and 99!", 1);
					}
				} else {
					flagGaps = true;
				}
			}
		} catch (Exception e) {
			cutText.requestFocus();
			cutText.selectAll();
			SwingDialog.showMSGDialog("Error", "The value must be between 0 and 99!", 1);
		}
	
		return flagGaps;
	}

}
