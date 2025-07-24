package module.multiseq.aligner.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import module.multiseq.aligner.MultipleSeqAlignerMain;

public class ClustaW2Alignment {

	private List<String> seqs;
	private List<String> seqNames;
	private String command;

	private String outputFilePath;
	private MultipleSeqAlignerMain alignmentMain;
	private String fileName = " ";

	public ClustaW2Alignment(MultipleSeqAlignerMain alignmentMain) {
		this.alignmentMain = alignmentMain;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isControllable() {
		return false;
	}

	public boolean ifEstimatedTimeKnown() {
		return false;
	}

	public int getMaxIterations() {

		return 1;
	}

	public void clustaW(String runExePath) throws Exception {

		Process exec = Runtime.getRuntime().exec(runExePath + command);
		// 等待执行结束
		exec.waitFor();
		if (exec.exitValue() == 0) {
			BufferedReader reader = new BufferedReader(new FileReader(new File(outputFilePath)));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(">")) {
					seqNames.add(line.substring(1));
					if (sb.length() > 0) {
						seqs.add(sb.toString());
						sb.delete(0, sb.length());
					}
				} else {
					sb.append(line);
				}
			}
			if (sb.length() > 0) {
				seqs.add(sb.toString());
				sb.delete(0, sb.length());
			}
		}

	}

	public int processNext() throws Exception {
		String property = System.getProperty("os.name");
		if (property.toLowerCase().startsWith("win")) {
			String runExePath = "config/softwares/ClustalW2/clustalw2.exe";

			clustaW(runExePath);
		} else if (property.toLowerCase().startsWith("mac")) {
			String runExePath = "./config/softwares/ClustalW2-mac/clustalw2";

			clustaW(runExePath);
		} else if (property.toLowerCase().startsWith("linux")) {
			String runExePath = "./config/softwares/ClustalW2-linux/clustalw2";

			clustaW(runExePath);
		}
		return 1;
	}

	public void actionsBeforeStart() throws Exception {
		seqs = new ArrayList<String>();
		seqNames = new ArrayList<String>();
	}

	public void actionsAfterFinished(boolean isNatural) throws Exception {
		if (!isNatural)
			return;

		if (seqs == null) {
			return;
		}

//		JideTabbedPane tabbedPane = BioMainFrame.getInstance().getTabbedPane();
//
//		String currentTabName = ModuleNameCenter.ALIGNMENT_VIEWER_NAME + ":" + fileName + ":"
//				+ alignmentMain.getAlignmentIndex();
//		if (BioMainFrame.getInstance().ifcouldCreateNewTabInTabbedPane(currentTabName)) {
//			SwingUtilities.invokeLater(() -> {
//
//				List<SequenceI> vector = new ArrayList<>();
//				for (int i = 0; i < seqs.size(); i++) {
//					Sequence sequence = new Sequence(seqNames.get(i), seqs.get(i));
//					vector.add(sequence);
//				}
//
//				final SequenceDataForAViewer sequenceData = new SequenceDataForAViewer(vector);
//
//				VisulizationDataProperty alignmentViewPort = new VisulizationDataProperty(sequenceData);
//
//				AlignmentViewMain aMain = new AlignmentViewMain();
//
//				aMain.setAlignmentViewPort(alignmentViewPort);
//
//				aMain.setAlignmentLayout();
//
//				tabbedPane.add(currentTabName, aMain);
//				tabbedPane.setSelectedComponent(aMain);
//				tabbedPane.getSelectedComponent().requestFocus();
//
//			});
//		}
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	class StreamGobbler extends Thread {
		InputStream is;
		String type;
		OutputStream os;
		boolean isOutput;

		StreamGobbler(InputStream is, String type) {
			this(is, type, null);
		}

		StreamGobbler(InputStream is, String type, boolean isOutput) {
			this.is = is;
			this.type = type;
			this.isOutput = isOutput;
		}

		StreamGobbler(InputStream is, String type, OutputStream redirect) {
			this.is = is;
			this.type = type;
			this.os = redirect;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					if (isOutput) {
						if (line.startsWith(">")) {
							seqNames.add(line.substring(1));
							if (sb.length() > 0) {
								seqs.add(sb.toString());
								sb.delete(0, sb.length());
							}
						} else {
							sb.append(line);
						}

					}
					System.out.println(type + ">" + line);
				}
				if (sb.length() > 0) {
					seqs.add(sb.toString());
					sb.delete(0, sb.length());
				}

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public void setFileName(String name) {
		fileName = name;

	}
}
