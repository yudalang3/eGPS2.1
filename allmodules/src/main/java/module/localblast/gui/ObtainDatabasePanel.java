package module.localblast.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ObtainDatabasePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ObtainDatabasePanel() {

		this.setLayout(new BorderLayout(0, 0));

		JTextArea textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(4, 50));
		this.add(textArea, BorderLayout.SOUTH);

		JPanel upParametersAndButtonPanel = new JPanel();
		this.add(upParametersAndButtonPanel, BorderLayout.CENTER);
		upParametersAndButtonPanel.setLayout(new BorderLayout(0, 0));

		JPanel buttonsPanel = new JPanel();
		upParametersAndButtonPanel.add(buttonsPanel, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Run");
		buttonsPanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Open directory");
		buttonsPanel.add(btnNewButton_1);

		JScrollPane scrollPane = new JScrollPane();
		upParametersAndButtonPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel parametersPanel = new JPanel();
		scrollPane.setViewportView(parametersPanel);
		GridBagLayout gbl_parametersPanel = new GridBagLayout();
		gbl_parametersPanel.columnWidths = new int[] { 0 };
		gbl_parametersPanel.rowHeights = new int[] { 0 };
		gbl_parametersPanel.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_parametersPanel.rowWeights = new double[] { Double.MIN_VALUE };
		parametersPanel.setLayout(gbl_parametersPanel);

	}
	

}
