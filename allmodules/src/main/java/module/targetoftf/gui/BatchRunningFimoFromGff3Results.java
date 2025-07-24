package module.targetoftf.gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import utils.EGPSFileUtil;
import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class BatchRunningFimoFromGff3Results extends JPanel {

	public BatchRunningFimoFromGff3Results() {

		setLayout(new BorderLayout());
		JTextArea jTextArea = new JTextArea();
		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();

		InputStream resourceAsStream = getClass().getResourceAsStream("running.sh");
		String contentFromInputStreamAsString = null;
		try {
			contentFromInputStreamAsString = EGPSFileUtil.getContentFromInputStreamAsString(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		jTextArea.setText(contentFromInputStreamAsString);

		jTextArea.setFont(launchProperty.getDefaultFont());

		add(jTextArea, BorderLayout.CENTER);
	}

	public String getShortDescription() {
		return "<html>Step4 is Batch running extract gene upstream and obtain sequences.<br>Here we directly provide the running codes.";
	}

	public String getTabName() {
		return "5. Batch running FIMO from GFF3 operator step4";
	}
}
