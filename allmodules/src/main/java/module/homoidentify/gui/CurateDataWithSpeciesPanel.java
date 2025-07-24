package module.homoidentify.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class CurateDataWithSpeciesPanel extends JPanel {
	public CurateDataWithSpeciesPanel() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new BorderLayout(0, 0));

		JTextArea txtrPleaseReferTo = new JTextArea();
		txtrPleaseReferTo.setText(
				"Please refer to the remnant \"Table-like text curation\" sub remnant \"Count Row Entries\", the normal procedure is:\r\n\r\n1. Setting table-like text file.\r\n2. Setting the dim1 and dim2 columns.\r\n dim1 is the Scientific name or with Comom name and assembly name.\r\n dim2 is the queryName.\r\n3. The remnant will curate the results.");

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		txtrPleaseReferTo.setFont(defaultTitleFont);

		add(txtrPleaseReferTo, BorderLayout.CENTER);
	}

}
