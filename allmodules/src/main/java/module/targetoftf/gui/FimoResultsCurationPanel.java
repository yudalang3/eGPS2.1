package module.targetoftf.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class FimoResultsCurationPanel extends JPanel {

	public FimoResultsCurationPanel() {

		setLayout(new BorderLayout());
		JTextArea jTextArea = new JTextArea();
		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();

		jTextArea.setText("The results of FIMO can be curated with the score and p-value.");
		jTextArea.append(
				"\nIf you want to known the promoter of the gene whether can be bound by the TF. Best narrow peak file can be recommended.");
		jTextArea.append(
				"\nWhile the cutoff values for the statics need to be explored, so the better cutoff is not fixed.");

		jTextArea.setFont(launchProperty.getDefaultFont());

		add(jTextArea, BorderLayout.CENTER);
	}

	public String getShortDescription() {
		return "Curate the FIMO program output file";
	}

	public String getTabName() {
		return "3. FIMO program output curation";
	}
}
