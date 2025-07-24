package module.evoltrepipline;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTree;

/**
 * 
 * @author ydl
 * 
 * @Date 2024-04-27
 */
public class Panel4WebQueryString extends AbstractPrefShowContent {

	private static final long serialVersionUID = 5562371735380833299L;
	private Map<String, String> parameterMap;

	private GridBagConstraints gridBagConstraints;
	private JRadioButton geneSymbolJRadioButton;
	private JTextField geneSymbolJTextField;
	private JTextField chrRegionJTextField;
	private JRadioButton chrRegionJRadioButton;

	ConstantNameClass_WebQuery constantNameClass_WebQuery = new ConstantNameClass_WebQuery();
	private JPanel contentPanel;

	public Panel4WebQueryString(Map<String, String> parameterMap, String connectionName) {
		super.userObject = connectionName;
		this.parameterMap = parameterMap;

	}

	@Override
	public JPanel getViewJPanel() {
		if (contentPanel != null) {
			return contentPanel;
		}
		geneSymbolJRadioButton = new JRadioButton("Gene symbol");
		geneSymbolJRadioButton.setFont(defaultFont);
		geneSymbolJRadioButton.setSelected(true);
		geneSymbolJRadioButton.setToolTipText("for example: TP53");
		geneSymbolJTextField = new JTextField(30);
		geneSymbolJTextField.setFont(defaultFont);

		chrRegionJRadioButton = new JRadioButton("chr-region");
		chrRegionJRadioButton.setFont(defaultFont);
		chrRegionJRadioButton.setToolTipText("17:7,661,779-7,687,550");
		chrRegionJTextField = new JTextField(30);
		chrRegionJTextField.setFont(defaultFont);
		chrRegionJTextField.setEnabled(false);
		
		
		String string1 = parameterMap.get(constantNameClass_WebQuery.label1_geneSymbol);
		geneSymbolJTextField.setText(string1);
		String string2 = parameterMap.get(constantNameClass_WebQuery.label2_genomicRegion);
		chrRegionJTextField.setText(string2);

		geneSymbolJRadioButton.setFocusPainted(false);
		chrRegionJRadioButton.setFocusPainted(false);

		ButtonGroup ignoredButtonGroup = new ButtonGroup();
		ignoredButtonGroup.add(geneSymbolJRadioButton);
		ignoredButtonGroup.add(chrRegionJRadioButton);

		geneSymbolJRadioButton.addActionListener(e -> {

			if (geneSymbolJRadioButton.isSelected()) {
				geneSymbolJTextField.setEnabled(true);
				if (geneSymbolJTextField.getText().trim().equals("")
						|| "".equals(geneSymbolJTextField.getText().trim())) {
//						buildGeneTreeButton.setEnabled(false);
				} else {
//						buildGeneTreeButton.setEnabled(true);
				}

				chrRegionJTextField.setEnabled(false);
			}

		});

		chrRegionJRadioButton.addActionListener(e -> {

			if (chrRegionJRadioButton.isSelected()) {
				chrRegionJTextField.setEnabled(true);
				geneSymbolJTextField.setEnabled(false);
				if (chrRegionJTextField.getText().trim().equals("")
						|| "".equals(chrRegionJTextField.getText().trim())) {
//						buildGeneTreeButton.setEnabled(false);
				} else {
//						buildGeneTreeButton.setEnabled(true);
				}
			}

		});


		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(3, 0, 3, 0);
		gridBagConstraints.anchor = GridBagConstraints.WEST;

		JPanel jRadioPane = new JPanel(new GridBagLayout());
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jRadioPane.add(geneSymbolJRadioButton, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		jRadioPane.add(geneSymbolJTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		jRadioPane.add(chrRegionJRadioButton, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		jRadioPane.add(chrRegionJTextField, gridBagConstraints);

		JPanel jButtonPane = new JPanel(new GridBagLayout());
		gridBagConstraints.anchor = GridBagConstraints.CENTER;

		JPanel jContentPane = new JPanel(new GridBagLayout());
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jContentPane.add(jRadioPane, gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		jContentPane.add(jButtonPane, gridBagConstraints);

		contentPanel = new JPanel();
		contentPanel.add(jContentPane, BorderLayout.NORTH);

		return contentPanel;
	}

	@Override
	public void saveParameter(Map<String, String> parameters) {
		if (geneSymbolJRadioButton.isSelected()) {
			String trim = geneSymbolJTextField.getText().trim();
			parameters.put(constantNameClass_WebQuery.label1_geneSymbol, trim);
			parameters.put(constantNameClass_WebQuery.label2_genomicRegion, "");
		} else {
			String trim = chrRegionJTextField.getText().trim();
			parameters.put(constantNameClass_WebQuery.label1_geneSymbol, "");
			parameters.put(constantNameClass_WebQuery.label2_genomicRegion, trim);
		}

	}

	@Override
	public boolean checkParameters(JTree jTree) {
		boolean empty1 = geneSymbolJTextField.getText().trim().isEmpty();
		boolean empty2 = chrRegionJTextField.getText().trim().isEmpty();

		if (empty2 && empty1) {
			return false;
		}

		return true;
	}
}
