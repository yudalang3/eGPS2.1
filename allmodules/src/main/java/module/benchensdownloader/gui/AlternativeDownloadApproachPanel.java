package module.benchensdownloader.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Panel;

import javax.swing.JTextArea;

import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class AlternativeDownloadApproachPanel extends Panel {
	public AlternativeDownloadApproachPanel() {
		setLayout(new BorderLayout(0, 0));

		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		JTextArea txtrInPracticeWe = new JTextArea();
		txtrInPracticeWe.setWrapStyleWord(true);
		txtrInPracticeWe.setFont(defaultFont);
		txtrInPracticeWe.setText(
				"In practice, we found some data download software provides a better way to download data by invoke the URL list.\r\n    So, if you like, you can download data with this alternative approach:\r\n    I found the motrix app is pretty good: https://motrix.app/");
		add(txtrInPracticeWe, BorderLayout.CENTER);
	}

}
