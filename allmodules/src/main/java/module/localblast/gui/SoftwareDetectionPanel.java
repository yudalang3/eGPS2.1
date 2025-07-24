package module.localblast.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jidesoft.swing.JideTabbedPane;

import egps2.frame.gui.handler.EGPSTextTransferHandler;
import egps2.UnifiedAccessPoint;
import egps2.frame.gui.EGPSMainGuiUtil;
import module.localblast.gui.util.Util;

@SuppressWarnings("serial")
public class SoftwareDetectionPanel extends JPanel implements ActionListener {

	private JButton detectByBlastn;
	private JTextArea textArea_normal;
	private JTextField jTextFieldOfBinDir;
	private JButton detectByMakeDB;
	private AreadyInstalledBlastSoftwareBean areadyInstalledBlastSoftwareBean;

	public SoftwareDetectionPanel(LocalBlastMain localBlastMain) {
		setLayout(new BorderLayout(0, 0));

		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JPanel jPanel = new JPanel(new BorderLayout(10, 0));
//		Border emptyBorder = BorderFactory.createEmptyBorder(6, 6, 6, 6);
		TitledBorder titledBorder = BorderFactory.createTitledBorder("Input parameters:");
		titledBorder.setTitleFont(defaultTitleFont);
		jPanel.setBorder(titledBorder);

		JLabel jLabel = new JLabel("Blast bin");
		jLabel.setFont(defaultFont);
		jLabel.setToolTipText("Enter the bin directory of the Blast software.");

		areadyInstalledBlastSoftwareBean = localBlastMain.getAreadyInstalledBlastSoftwareBean();
		String binDirPath = areadyInstalledBlastSoftwareBean.getBinDirPath();
		jTextFieldOfBinDir = new JTextField(100);
		jTextFieldOfBinDir.setText(binDirPath);
		jTextFieldOfBinDir.setFont(defaultFont);
		jTextFieldOfBinDir.setTransferHandler(new EGPSTextTransferHandler());
		/**
		 * 有改动的时候立马传导一下。
		 */
		jTextFieldOfBinDir.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				areadyInstalledBlastSoftwareBean.setBinDirPath(jTextFieldOfBinDir.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				areadyInstalledBlastSoftwareBean.setBinDirPath(jTextFieldOfBinDir.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				areadyInstalledBlastSoftwareBean.setBinDirPath(jTextFieldOfBinDir.getText());
			}
		});
		JButton fileDirLoadingJButton = EGPSMainGuiUtil.getFileDirLoadingJButton(jTextFieldOfBinDir, getClass());
		fileDirLoadingJButton.setFocusable(false);
		fileDirLoadingJButton.setFont(defaultFont);

		jPanel.add(jLabel, BorderLayout.WEST);
		jPanel.add(jTextFieldOfBinDir, BorderLayout.CENTER);
		jPanel.add(fileDirLoadingJButton, BorderLayout.EAST);

		this.add(jPanel, BorderLayout.NORTH);

		JideTabbedPane bottomTabbedPanel = new JideTabbedPane();
		bottomTabbedPanel.setFont(defaultFont);
		bottomTabbedPanel.setPreferredSize(new Dimension(5, 120));

		textArea_normal = new JTextArea();
		textArea_normal.setWrapStyleWord(true);
		textArea_normal.setLineWrap(true);
		textArea_normal.setFont(defaultFont);
		textArea_normal.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		JScrollPane component = new JScrollPane(textArea_normal);
		component.setBorder(null);
		bottomTabbedPanel.addTab("Console", null, component, null);

		this.add(bottomTabbedPanel, BorderLayout.CENTER);

		JPanel jPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		detectByBlastn = new JButton("Detect by Blastn");
		detectByBlastn.setFont(defaultFont);
		detectByBlastn.addActionListener(this);

		detectByMakeDB = new JButton("Detect by MakeDB");
		detectByMakeDB.setFont(defaultFont);
		detectByMakeDB.addActionListener(this);

		jPanel2.add(detectByMakeDB);
		jPanel2.add(detectByBlastn);

		bottomTabbedPanel.setTabTrailingComponent(jPanel2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String text = jTextFieldOfBinDir.getText();

		if (text.isEmpty()) {
			textArea_normal.append("\nPlease input the blast software executable path first.");
			return;
		}

		File file = new File(text);
		if (!file.exists()) {
			textArea_normal.append("\nThe provided file directory not exists.");
			return;
		}

		if (!file.isDirectory()) {
			textArea_normal.append(
					"\nThe provided is not directory, the remnant need to import the bin dir path not the programe is self.");
			return;
		}
		textArea_normal.setText("");

		String temp = null;
		Object source = e.getSource();
		if (detectByMakeDB == source) {
			temp = areadyInstalledBlastSoftwareBean.getWindowsMakeBlastDB().concat(" -h");
		} else if (detectByBlastn == source) {
			temp = areadyInstalledBlastSoftwareBean.getWindowsBlastN().concat(" -h");
		}

		final String runProgrameCommand = temp;

		new Thread(() -> {

			try {
				final Process process = Runtime.getRuntime().exec(runProgrameCommand);
				Util.printMessage(process.getInputStream(), textArea_normal);
				Util.printMessage(process.getErrorStream(), textArea_normal);
				// value 0 is normal
				int value = process.waitFor();
				if (value == 0) {
					textArea_normal.append("\nCongratulations, Successful for the programe to run.");
				} else {
					textArea_normal.append("\nSorry, errors found. Please check your installation.");
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}).start();

	}

}
