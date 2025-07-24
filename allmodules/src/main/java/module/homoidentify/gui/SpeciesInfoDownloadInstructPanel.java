package module.homoidentify.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class SpeciesInfoDownloadInstructPanel extends JPanel {
	public SpeciesInfoDownloadInstructPanel() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new BorderLayout(0, 0));

		JTextArea txtrPleaseReferTo = new JTextArea();
		txtrPleaseReferTo.setText(
				"The species information can be obtained in Ensembl.\r\n\r\nhttps://www.ensembl.org/info/data/ftp/index.html?redirect=no\r\n\r\nhttps://www.ensembl.org/info/about/species.html");

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		txtrPleaseReferTo.setFont(defaultTitleFont);

		add(txtrPleaseReferTo, BorderLayout.CENTER);
	}

}
