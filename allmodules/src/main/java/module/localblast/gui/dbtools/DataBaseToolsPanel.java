package module.localblast.gui.dbtools;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.swing.JPanel;

import egps2.panels.dialog.SwingDialog;
import module.localblast.gui.AbstractBasicPanel;
import module.localblast.gui.LocalBlastMain;
import egps2.modulei.RunningTask;

@SuppressWarnings("serial")
public class DataBaseToolsPanel extends AbstractBasicPanel {

	private BasicModeParameter allParameters;
	private BasicModePanel parametersPanel;

	/**
	 * Create the panel.
	 * 
	 * @param localBlastMain
	 */
	public DataBaseToolsPanel(LocalBlastMain localBlastMain) {
		super(localBlastMain);
	}

	@Override
	protected JPanel getUpParametersPanel() {
		parametersPanel = new BasicModePanel();
		return parametersPanel;
	}

	@Override
	protected void processRun() {
		RunningTask runningTask = new RunningTask() {

			@Override
			public int processNext() throws Exception {
				runButtonAction();
				return PROGRESS_FINSHED;
			}

			@Override
			public boolean isTimeCanEstimate() {
				return false;
			}
		};
		localBlastMain.registerRunningTask(runningTask);
	}
	
	private void runButtonAction() {
		allParameters = parametersPanel.getAllParameters();

		String windowsMakeBlastDB = localBlastMain.getAreadyInstalledBlastSoftwareBean().getWindowsMakeBlastDB();
		String runProgrameCommand = allParameters.getRunProgrameCommand(windowsMakeBlastDB);

		textArea_normal.setText("");
		textArea_error.setText("");
		textArea_normal.append(runProgrameCommand);
		/**
		 * https://www.biostars.org/p/413294/ 可能会出现问题说： No volumes were created.BLAST
		 * Database creation error: mdb_env_open: 磁盘空间不足。
		 * 
		 * 解决方法：可以放到高级参数中！ String[] envp = {"BLASTDB_LMDB_MAP_SIZE","100000000"};
		 */
		try {
			// final Process process = Runtime.getRuntime().exec("cmd.exe /c dir");

			final Process process = Runtime.getRuntime().exec(runProgrameCommand);
			printMessage(process.getInputStream(), false);
			printMessage(process.getErrorStream(), true);
			// value 0 is normal
			int value = process.waitFor();
			if (value != 0) {
				bottomTabbedPanel.setSelectedIndex(1);
				textArea_error.requestFocus();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@Override
	protected void processOpenDir() {
		if (allParameters == null) {
			SwingDialog.showInfoMSGDialog("Information", "Click the execute button first.");
		} else {
			String outParameterString = allParameters.getOutParameterString();
			if (outParameterString != null && outParameterString.length() > 0) {

				try {
					Desktop.getDesktop().open(new File(outParameterString).getParentFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}

	private void printMessage(final InputStream input, boolean isError) {
		new Thread(() -> {
			Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
			BufferedReader bf = new BufferedReader(reader);
			String line = null;
			try {
				while ((line = bf.readLine()) != null) {
					if (isError) {
						textArea_error.append(line + "\n");
						System.err.println(line);
					} else {
						textArea_normal.append(line + "\n");
						// System.out.println(line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

}
