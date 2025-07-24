/**
 * 
 */
package module.targetoftf.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class MappingMotifToGenomePanel extends JPanel {

	public MappingMotifToGenomePanel() {

		setLayout(new BorderLayout());
		JTextArea jTextArea = new JTextArea();
		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();

		jTextArea.setText("Users can use the FIMO programe in the MEME suite to find target sequence bind sites.");
		jTextArea.append(
				"\nThe following tutorial is very usefull https://meme-suite.org/meme/doc/fimo-tutorial.html.");
		jTextArea.append(
				"\nSo, direct mapping to the genomo will get too much results. Use the \"GFF3 operator\" remnant to get the upstram of the genes.");
		jTextArea.append(
				"\nUsers can also use the \"Direct sequence mapping\" remnant to get exact match of motif.");
		jTextArea.append("\nFor example: query is ACG, target is the AAATACGTTTGG. The exact match is position: 5,6,7");

		jTextArea.setFont(launchProperty.getDefaultFont());

		add(jTextArea, BorderLayout.CENTER);
	}


	public String getShortDescription() {
		return null;
	}

	public String getTabName() {
		return "2. Mapping Motif To Genome";
	}
}
