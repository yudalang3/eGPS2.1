/**
 * 
 */
package module.evoltrepipline;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

/**
 * @author YFQ
 * @date 2019-11-21 13:12:37
 * @version 1.0
 *          <p>
 *          Description:
 *          </p>
 * 
 * @author ydl
 * 
 * @Date 2024-04-27
 * 
 */
public class Panel4BiologyCodesPanel extends AbstractPrefShowContent {

	private static final long serialVersionUID = -7862050211960918866L;

	private ConstantNameClass_GeneticCode ccgc = new ConstantNameClass_GeneticCode();

	private JComboBox<Object> codeComboBox;
	private ButtonGroup nucleotideGroup;
	private Map<String, String> parameterMap;

	private JPanel contentPanel;

	public Panel4BiologyCodesPanel(Map<String, String> parameterMap, String alignViewName) {
		super.userObject = alignViewName;
		this.parameterMap = parameterMap;
	}

	@Override
	public JPanel getViewJPanel() {
		if (contentPanel != null) {
			return contentPanel;
		}

		contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel biologyCodePanel = new JPanel(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		JPanel codePanel = new JPanel(new GridBagLayout());
		codePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccgc.category1_geneticCode, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));

		JLabel lengthLabel = new JLabel(ccgc.label1_geneticCodeTable);
		lengthLabel.setFont(defaultFont);

		List<Object> list = new ArrayList<>();

		list.add(ccgc.geneticCodeTable_value1_standardName);
		list.add(ccgc.geneticCodeTable_value2_vertebrateName);
		list.add(ccgc.geneticCodeTable_value3_yeastName);
		list.add(ccgc.geneticCodeTable_value4_invertebrateName);
		list.add(ccgc.geneticCodeTable_value5_ciliateName);
		list.add(ccgc.geneticCodeTable_value6_echinodermName);
		list.add(ccgc.geneticCodeTable_value7_euplotidName);
		list.add(ccgc.geneticCodeTable_value8_bacterialName);
		// The incoming object can contain tooltips.
		Item item = new Item("The Mold, Protozoan, and...", ccgc.geneticCodeTable_value9_moldName);
		list.add(item);

		codeComboBox = new JComboBox<>(list.toArray());
		// Setting up a renderer for parsing objects.
		codeComboBox.setRenderer(new EGPSComboBoxRenderer());
		codeComboBox.setFont(defaultFont);

		int geneticCodeIndex = Integer.parseInt(parameterMap.get(ccgc.label1_geneticCodeTable_index));

		if (geneticCodeIndex >= 0 && geneticCodeIndex <= 8) {
			codeComboBox.setSelectedIndex(geneticCodeIndex);
		} else {
			codeComboBox.setSelectedIndex(0);
		}

		codePanel.add(lengthLabel, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		codePanel.add(codeComboBox, gridBagConstraints);

		JPanel nucleotidePanel = new JPanel(new GridBagLayout());
		nucleotidePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				ccgc.label2_geneticNucleotidePanel, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));

		JLabel iupacLabel = new JLabel(ccgc.label2_geneticNucleotideLabel);
		iupacLabel.setFont(defaultFont);

		JRadioButton gapJRadio = new JRadioButton(ccgc.geneticNucleotide_value1);
		gapJRadio.setFont(defaultFont);
		gapJRadio.setFocusPainted(false);

		JRadioButton possibilityJRadio = new JRadioButton(ccgc.geneticNucleotide_value2);
		possibilityJRadio.setFont(defaultFont);
		possibilityJRadio.setFocusPainted(false);

		nucleotideGroup = new ButtonGroup();
		nucleotideGroup.add(gapJRadio);
		nucleotideGroup.add(possibilityJRadio);

		String nucleotideIndex = parameterMap.get(ccgc.label2_geneticNucleotide);

		if (nucleotideIndex.equals(ccgc.geneticNucleotide_value1)) {
			gapJRadio.setSelected(true);
		} else {
			possibilityJRadio.setSelected(true);
		}

		gridBagConstraints.gridx = 0;
		nucleotidePanel.add(iupacLabel, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		nucleotidePanel.add(gapJRadio, gridBagConstraints);
		gridBagConstraints.gridy = 1;
		nucleotidePanel.add(possibilityJRadio, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		biologyCodePanel.add(codePanel, gridBagConstraints);
		gridBagConstraints.gridy = 1;
		biologyCodePanel.add(nucleotidePanel, gridBagConstraints);

		contentPanel.add(biologyCodePanel);

		return contentPanel;
	}

	@Override
	public void saveParameter(Map<String, String> parameters) {

		String code = codeComboBox.getSelectedItem().toString();
		int codeID = codeComboBox.getSelectedIndex();

		parameters.put(ccgc.label1_geneticCodeTable, code);
		parameters.put(ccgc.label1_geneticCodeTable_index, String.valueOf(codeID));

		Enumeration<AbstractButton> treeRadioBtns = nucleotideGroup.getElements();
		String value = ccgc.geneticNucleotide_value1;
		while (treeRadioBtns.hasMoreElements()) {
			AbstractButton btn = treeRadioBtns.nextElement();
			if (btn.isSelected()) {
				value = btn.getText();
				break;
			}
		}

		parameters.put(ccgc.label2_geneticNucleotide, value);

	}

	@Override
	public boolean checkParameters(JTree jTree) {
		return true;
	}

}
