package module.evoltrepipline;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTree;

/**
 * 
 * @Package: egps.core.preferences
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-01-11 10:51
 * 
 * @author ydl
 * 
 * @Date 2024-04-27
 */
public class Panel4DistTreeBuildMethod extends AbstractPrefShowContent {

	private static final long serialVersionUID = 5562371735380833299L;
	private Map<String, String> parameterMap;
	protected ConstantNameClass_TreeBuildMethod cctm = new ConstantNameClass_TreeBuildMethod();
	private ButtonGroup treeButtonGroup;
	private JPanel contentPanel;

	public Panel4DistTreeBuildMethod(Map<String, String> parameterMap, String connectionName) {
		super.userObject = connectionName;
		this.parameterMap = parameterMap;
	}

	@Override
	public JPanel getViewJPanel() {
		if( contentPanel != null) {
			return contentPanel;
		}
		
		JPanel jContentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		JLabel methodLabel = new JLabel(cctm.label1_treeBuildMethod);
		methodLabel.setFont(defaultFont);
		jContentPane.add(methodLabel, gridBagConstraints);

		gridBagConstraints.gridy = 1;
		JRadioButton neighborRadio = new JRadioButton(cctm.treeBuildMethod_value1_NJ);
		neighborRadio.setToolTipText(
				"<html><body> Saitou, N, Nei, M. The neighbor-joining method: a new method for reconstructing <br/> phylogenetic trees. Mol Biol Evol. 1987; 4: 406-25. </body></html>");
		neighborRadio.setFont(defaultFont);
		jContentPane.add(neighborRadio, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		JRadioButton swiftRadio = new JRadioButton(cctm.treeBuildMethod_value2_SNJ);
		swiftRadio.setToolTipText("Saitou, N. Introduction to Evolutionary Genomics: Springer; 2018.");
		swiftRadio.setFont(defaultFont);
		jContentPane.add(swiftRadio, gridBagConstraints);

		gridBagConstraints.gridy = 3;
		JRadioButton upRadio = new JRadioButton(cctm.treeBuildMethod_value3_UPGMA);
		upRadio.setToolTipText("Sneath P.H.A. and Sokal R.R. (1973). Numerical Taxonomy. Freeman, San Francisco.");
		upRadio.setFont(defaultFont);
		jContentPane.add(upRadio, gridBagConstraints);

		neighborRadio.setFocusPainted(false);
		swiftRadio.setFocusPainted(false);
		upRadio.setFocusPainted(false);

		treeButtonGroup = new ButtonGroup();
		treeButtonGroup.add(neighborRadio);
		treeButtonGroup.add(swiftRadio);
		treeButtonGroup.add(upRadio);

		String parameter = parameterMap.get(cctm.label1_treeBuildMethod);

		if (!parameter.equals("") && parameter != null) {
			if (cctm.treeBuildMethod_value1_NJ.equals(parameter)) {
				neighborRadio.setSelected(true);
			} else if (cctm.treeBuildMethod_value2_SNJ.equals(parameter)) {
				swiftRadio.setSelected(true);
			} else if (cctm.treeBuildMethod_value3_UPGMA.equals(parameter)) {
				upRadio.setSelected(true);
			} else {
				neighborRadio.setSelected(true);
			}
		} else {
			neighborRadio.setSelected(true);
		}

		contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
//		contentPanel.add(jContentPane, BorderLayout.WEST);
		contentPanel.add(jContentPane);

		return contentPanel;

	}

	@Override
	public void saveParameter(Map<String, String> parameters) {
		Enumeration<AbstractButton> elements = treeButtonGroup.getElements();
		String str = null;
		while (elements.hasMoreElements()) {
			AbstractButton nextElement = elements.nextElement();
			if (nextElement.isSelected()) {
				str = nextElement.getText();
				break;
			}

		}

		parameters.put(cctm.label1_treeBuildMethod, str);
	}

	@Override
	public boolean checkParameters(JTree jTree) {
		return true;
	}

}
