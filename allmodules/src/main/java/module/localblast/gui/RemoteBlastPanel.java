package module.localblast.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import module.localblast.gui.search.programe.BasicBlastnPanel;
import module.localblast.gui.search.programe.SearchProgrameCommon;

public class RemoteBlastPanel extends JPanel {

	private JTextArea textArea_normal;
	private JTextArea textArea_error;
	private SearchProgrameCommon[] prgramePanels;
	private JTabbedPane tabbedPane_searchgrame;

	/**
	 * Create the panel.
	 */
	public RemoteBlastPanel() {

		this.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		textArea_normal = new JTextArea();
		textArea_normal.setWrapStyleWord(true);
		textArea_normal.setLineWrap(true);
		tabbedPane.addTab("Normal output console", null, new JScrollPane(textArea_normal), null);
		textArea_error = new JTextArea();
		textArea_error.setWrapStyleWord(true);
		textArea_error.setLineWrap(true);
		tabbedPane.addTab("Error console", null, new JScrollPane(textArea_error), null);
		

		JPanel upParametersAndButtonPanel = new JPanel();
		upParametersAndButtonPanel.setLayout(new BorderLayout(0, 0));

		JPanel buttonsPanel = new JPanel();
		upParametersAndButtonPanel.add(buttonsPanel, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(e -> {

			new Thread(() -> {
				SearchProgrameCommon cc = prgramePanels[tabbedPane_searchgrame.getSelectedIndex()];
				cc.run(textArea_normal, textArea_error);

			}).start();
		});
		buttonsPanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Open directory");
		btnNewButton_1.addActionListener(e -> {
			SearchProgrameCommon cc = prgramePanels[tabbedPane_searchgrame.getSelectedIndex()];
			String targetDir = cc.getTargetDir();
			if (targetDir != null && targetDir.length() > 0) {

				new Thread(() -> {
					try {
						Desktop.getDesktop().open(new File(targetDir).getParentFile());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}).start();

			}
		});
		buttonsPanel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Advanced mode");
		buttonsPanel.add(btnNewButton_2);
		
		tabbedPane_searchgrame = new JTabbedPane(JTabbedPane.TOP);
		upParametersAndButtonPanel.add(tabbedPane_searchgrame, BorderLayout.CENTER);
		
		// Need change
		BasicBlastnPanel panel = new BasicBlastnPanel(null);
		tabbedPane_searchgrame.addTab("blastn", null, new JScrollPane(panel), null);
		prgramePanels = new SearchProgrameCommon[1];
		prgramePanels[0] = panel;
	
		JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upParametersAndButtonPanel,tabbedPane);
		jSplitPane.setResizeWeight(0.5);
		this.add(jSplitPane, BorderLayout.CENTER);

	}
	
}