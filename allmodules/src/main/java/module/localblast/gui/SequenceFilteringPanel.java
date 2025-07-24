package module.localblast.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import module.localblast.gui.seqfilter.BasicModePanel;
import module.localblast.gui.seqfilter.BasicModeParameter;
import module.localblast.gui.util.Util;

public class SequenceFilteringPanel extends JPanel {

	private BasicModeParameter allParameters;
	private JTextArea textArea_normal;
	private JTextArea textArea_error;

	/**
	 * Create the panel.
	 */
	public SequenceFilteringPanel() {

		this.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(5, 120));
		
		textArea_normal = new JTextArea();
		textArea_normal.setWrapStyleWord(true);
		textArea_normal.setLineWrap(true);
		tabbedPane.addTab("Normal output console", null, new JScrollPane(textArea_normal), null);
		textArea_error = new JTextArea();
		textArea_error.setWrapStyleWord(true);
		textArea_error.setLineWrap(true);
		tabbedPane.addTab("Error console", null, new JScrollPane(textArea_error), null);
		
		this.add(tabbedPane, BorderLayout.SOUTH);

		JPanel upParametersAndButtonPanel = new JPanel();
		this.add(upParametersAndButtonPanel, BorderLayout.CENTER);
		upParametersAndButtonPanel.setLayout(new BorderLayout(0, 0));

		JPanel buttonsPanel = new JPanel();
		upParametersAndButtonPanel.add(buttonsPanel, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();
		upParametersAndButtonPanel.add(scrollPane, BorderLayout.CENTER);

		BasicModePanel parametersPanel = new BasicModePanel();
		scrollPane.setViewportView(parametersPanel);

		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(e -> {

			new Thread(() -> {

				allParameters = parametersPanel.getAllParameters();
				String runProgrameCommand = allParameters.getRunProgrameCommand();
				
				textArea_normal.setText("");
				textArea_error.setText("");
				textArea_normal.append(runProgrameCommand);
				/**
				 * https://www.biostars.org/p/413294/
				 * 可能会出现问题说：
				 * No volumes were created.BLAST Database creation error: mdb_env_open: 磁盘空间不足。
				 * 
				 * 解决方法：可以放到高级参数中！
				 * String[] envp = {"BLASTDB_LMDB_MAP_SIZE","100000000"};
				 */
				try {
					//final Process process = Runtime.getRuntime().exec("cmd.exe /c dir");
					
					final Process process = Runtime.getRuntime().exec(runProgrameCommand);
					Util.printMessage(process.getInputStream(),false,textArea_normal,textArea_error);
					Util.printMessage(process.getErrorStream(),true,textArea_normal,textArea_error);
				    // value 0 is normal
					int value = process.waitFor();
				    //System.out.println(value);
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}).start();
		});
		buttonsPanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Open directory");
		btnNewButton_1.addActionListener(e -> {
			if (allParameters != null) {
				String outParameterString = allParameters.getOutParameterString();
				if (outParameterString != null && outParameterString.length() > 0) {

					new Thread(() -> {
						try {
							Desktop.getDesktop().open(new File(outParameterString).getParentFile());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}).start();

				}
			}
		});
		buttonsPanel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Advanced mode");
		buttonsPanel.add(btnNewButton_2);
		
	}
	
	

}
