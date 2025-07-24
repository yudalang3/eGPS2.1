package module.vennplot;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class ParameterPanel extends JPanel {
	private JTextArea textArea;
	private JButton displayExampleBtn;
	private JComboBox<String> elementTypeBox;

	private VennPlotDataImportLoader independentVennPlotLoader0509;
	private int displaySampleIndex;

	/**
	 * Create the panel.
	 */
	public ParameterPanel() {
		setBorder(new EmptyBorder(20, 20, 20, 20));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Element type");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		elementTypeBox = new JComboBox<>();
		elementTypeBox.setModel(new DefaultComboBoxModel(new String[] { "String", "Genomic region" }));
		GridBagConstraints gbc_elementTypeBox = new GridBagConstraints();
		gbc_elementTypeBox.anchor = GridBagConstraints.WEST;
		gbc_elementTypeBox.insets = new Insets(0, 15, 5, 10);
		gbc_elementTypeBox.gridx = 2;
		gbc_elementTypeBox.gridy = 0;
		add(elementTypeBox, gbc_elementTypeBox);

		JLabel lblNewLabel_1 = new JLabel("Example");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 3;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		displayExampleBtn = new JButton("Display next example");
		GridBagConstraints gbc_displayExampleBtn = new GridBagConstraints();
		gbc_displayExampleBtn.anchor = GridBagConstraints.WEST;
		gbc_displayExampleBtn.insets = new Insets(0, 10, 5, 10);
		gbc_displayExampleBtn.gridx = 4;
		gbc_displayExampleBtn.gridy = 0;
		add(displayExampleBtn, gbc_displayExampleBtn);
		displayExampleBtn.addActionListener(e -> {
			List<String> actions4displayNextExample = actions4displayNextExample();
			independentVennPlotLoader0509.displayNextExample(actions4displayNextExample);
		});

		JLabel lblNewLabel_2 = new JLabel("Statement");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		add(lblNewLabel_2, gbc_lblNewLabel_2);

		textArea = new JTextArea();
		textArea.setEditable(false);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 4;
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 2;
		gbc_textArea.gridy = 2;
		add(new JScrollPane(textArea), gbc_textArea);
		textArea.setText("Input 3 sets:\n set1 with 7 count\n set2 with 6 count\n set3 with 7 count");

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();


		Component[] components = getComponents();
		for (Component component : components) {
			component.setFont(globalFont);
		}

	}

	public void setStatement(List<String> inputStrings) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String string : inputStrings) {
			stringBuilder.append(string).append("\n");
		}
		textArea.setText(stringBuilder.toString());

	}

	public List<String> actions4displayNextExample() {

		List<String> displayedStrings = new LinkedList<>();

		if (displaySampleIndex == 3) {
			displaySampleIndex = 0;
		}

		/**
		 * YDL: for input string is genomic region ,we only show the basic usage!
		 */
		if (isGenomicRegionDataType()) {
			displayedStrings.add("Set1	chr1,100,201	chr1,200,301	chr2,301,401	chr2,501,600");
			displayedStrings.add("Set2	chr2,301,401	chr2,501,600	chr3,300,401	chr4,450,500");
			displayedStrings.add("Set3	chr2,301,401	chr4,450,500");

		} else {

			if (displaySampleIndex == 0) {

				displayedStrings.add("Set1	a	b	c	d	e	f	g	i");
				displayedStrings.add("Set2	b	d	f	g	i");
				displayedStrings.add("Set3	g	h	i	j	k");
			} else if (displaySampleIndex == 1) {

				StringBuilder stringBuilder = new StringBuilder(2000);
				stringBuilder.append("Set1");
				for (int i = 0; i < 2000; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set2");
				for (int i = 1001; i < 3000; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set3");
				for (int i = 1501; i < 2500; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set4");
				for (int i = 1601; i < 3000; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set5");
				for (int i = 1; i < 500; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

			} else {
				StringBuilder stringBuilder = new StringBuilder(2000);
				stringBuilder.append("Set1");
				for (int i = 3001; i < 5050; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set2");
				for (int i = 4001; i < 5090; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set3");
				for (int i = 4501; i < 5500; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set4");
				for (int i = 5001; i < 6000; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set5");
				for (int i = 5501; i < 6000; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set6");
				for (int i = 7001; i < 8000; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());

				stringBuilder.setLength(0);
				stringBuilder.append("Set7");
				for (int i = 300; i < 400; i++) {
					stringBuilder.append("	");
					stringBuilder.append(i);
				}
				displayedStrings.add(stringBuilder.toString());
			}
		}

		displaySampleIndex++;

		return displayedStrings;

	}


	public void setIndependentVennPlotLoader0509(VennPlotDataImportLoader independentVennPlotLoader0509) {
		this.independentVennPlotLoader0509 = independentVennPlotLoader0509;
	}

	public boolean isGenomicRegionDataType() {
		return elementTypeBox.getSelectedIndex() == 1;
	}

}
