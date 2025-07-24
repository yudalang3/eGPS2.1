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
public class UseTheDerectMappingTools extends JPanel {

	public UseTheDerectMappingTools() {

		setLayout(new BorderLayout());
		JTextArea jTextArea = new JTextArea();
		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();

		jTextArea.setText("Users can use the remnant \"Direct sequence mapping\" to find target sequence bind sites.");
		jTextArea.append(
				"\nIf your input binding sequence contains ambiguous nuclotide sequence, please use the \"Ambiguous Nucl to concrete\" remnant convert to concrete sequence.");

		jTextArea.setFont(launchProperty.getDefaultFont());

		add(jTextArea, BorderLayout.CENTER);
	}


	public String getShortDescription() {
		return null;
	}

	public String getTabName() {
		return "2.2 Use the \"Direct sequence mapping\" remnant";
	}
}
