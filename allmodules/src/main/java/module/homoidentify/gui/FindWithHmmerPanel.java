package module.homoidentify.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class FindWithHmmerPanel extends JPanel {
	public FindWithHmmerPanel() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new BorderLayout(0, 0));

		JTextArea txtrPleaseReferTo = new JTextArea();
		txtrPleaseReferTo.setText(
				"The lastest version of executable hmmer program is only avaliable on the Unix-like Operational System.\r\nAnd with the eGPS development philosophy, it is not resonable to wrap the external program into the eGPS.\r\n\r\nHere, we offers the example of the quick running command lines:\r\nphmmer --cpu 6 -o 1.hmmersearch.results.txt --cut_tc --tblout tblout.txt --domtblout domtblout.txt query.fa target.fa\r\n\r\nWindows users can run on WSL.\r\nAdvanced users please refer to the Hmmer's manual:\r\nhttp://hmmer.org/");

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		txtrPleaseReferTo.setFont(defaultTitleFont);

		add(txtrPleaseReferTo, BorderLayout.CENTER);
	}

}
