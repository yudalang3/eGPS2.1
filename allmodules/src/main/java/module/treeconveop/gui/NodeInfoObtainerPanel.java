/**
 * 
 */
package module.treeconveop.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class NodeInfoObtainerPanel extends JPanel {

	public NodeInfoObtainerPanel() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new BorderLayout(0, 0));

		JTextArea txtrPleaseReferTo = new JTextArea();
		txtrPleaseReferTo.setText(
				"Please refer to the remnant \"Tree node info collector\", the normal procedure is:\r\n1. Setting phylogentic tree with various format.\r\n2. Select parameters.\r\n3. Get the target node info.");

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		txtrPleaseReferTo.setFont(defaultTitleFont);

		add(txtrPleaseReferTo, BorderLayout.CENTER);
	}


	public String getTabName() {
		return new String("Tree node info collector");
	}

	public String getShortDescription() {
		return new String("Import phylogenetic tree and obtain the node information.");
	}
}
