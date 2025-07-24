package module.targetoftf.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class MotifCollectionGuidePanel extends JPanel {

	public MotifCollectionGuidePanel() {

		setLayout(new BorderLayout());
		JTextArea jTextArea = new JTextArea();
		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();

		jTextArea.setText("Users can download the motifs from the JASPER TF motif website.");
		jTextArea.append("\nAlso can get the motif from the research article.");
		jTextArea.append(
				"\nIf the motify is a fasta file with ambiguous nucleotide. Please use the \"Ambiguous Nucl to concrete\" remnant to convert concrete sequences.");
		jTextArea.append("\nFor example: ATCGN will be convert to 1) ATCGA 2)ATCGT 3)ATCGC 4)ATCGG .");

		jTextArea.setFont(launchProperty.getDefaultFont());

		add(jTextArea, BorderLayout.CENTER);
	}

	public String getShortDescription() {
		return "At the initial step to collect the motifs";
	}

	public String getTabName() {
		return "1. Motif Collection";
	}
}
