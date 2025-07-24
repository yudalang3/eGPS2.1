package module.multiseq.aligner.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import egps2.UnifiedAccessPoint;

public class Mafft2AlignmentCli {

	protected List<String> seqs;
	protected List<String> seqNames;
	protected List<String> parameters;

	protected OperationSystem osType = null;
	private boolean showErrorMessage = false;

	class StreamGobbler extends Thread {
		InputStream is;
		String type;
		OutputStream os;
		/**
		 * 如果 output 为 T那么要输出，一般为 OUTPUT的时候为T ERROR为 False
		 */
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

		@Override
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

					} else {
						// 这里输出的是 isOutput 为 False的结果；也就是 ERROR的情况
						if (showErrorMessage) {
							System.err.println(type + ">" + line);
						}
					}
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

	public Mafft2AlignmentCli(List<String> parameters) {
		this.parameters = parameters;

		String property = System.getProperty("os.name");

		if (property.toLowerCase().startsWith("win")) {
			osType = OperationSystem.WINDOWS;
		} else if (property.toLowerCase().startsWith("mac")) {
			osType = OperationSystem.MACOS;
		} else {
			osType = OperationSystem.LINUX;
		}

	}

	public int processNextDefaultMAFFT(String inputPath) throws Exception {
		final String key = "MAFFT";
		String runBatPath = UnifiedAccessPoint.getExternalProgramPath().get(key);
		switch (osType) {
		case WINDOWS:
//			runBatPath = propertiesDir.concat("/softwares/mafft-win/mafft.bat");
			break;
		case MACOS:
//			runBatPath = propertiesDir.concat("/softwares/mafft-mac/mafft.bat");
			break;
		default:
			// linux
//			runBatPath = propertiesDir.concat("/softwares/mafft-linux/mafft.bat");
			break;
		}
		if (runBatPath == null || !new File(runBatPath).exists()) {
			throw new Exception(
					"The software is not installed, please put the executable program in global config file.\nKey is "
							.concat(key));
		}

		processNextWithMAFFTPath(runBatPath, inputPath);
		return 1;
	}

	public void processNextWithMAFFTPath(String mafftAlignerPath, String inputPath) throws Exception {
		if (!new File(mafftAlignerPath).exists()) {
			throw new InputMismatchException("The input MAFFT software path is not existed, please check.");
		}

		List<String> command = new ArrayList<String>();

		switch (osType) {
		case WINDOWS:
			command.add("cmd.exe");
			command.add("/c");
			command.add("call");
			command.add(mafftAlignerPath);
			for (String parameter : parameters) {
				command.add(parameter);
			}
			break;
		case MACOS:
			command.add("/bin/sh");
			command.add("/c");
			command.add(mafftAlignerPath);
			for (String parameter : parameters) {
				command.add(parameter);
			}
			break;
		default:
			// linux
			command.add("/bin/sh");
			command.add("/c");
			command.add(mafftAlignerPath);
			for (String parameter : parameters) {
				command.add(parameter);
			}
			break;
		}

		// 不要忘记把输入路径也放进去，如果inputPath为null的话，说明全局变量中的parameters已经包含了输入fasta的路径了。
		// 重要
		if (inputPath != null) {
			command.add(inputPath);
		}

		alignByMafft(command);
	}

	private void alignByMafft(List<String> newCommand) throws Exception {
		ProcessBuilder builder = new ProcessBuilder(newCommand);
		Process proc = builder.start();

		// FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
		// 参考文献:https://www.javaworld.com/article/2071275/when-runtime-exec---won-t.html?page=2

		// any error message?
		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", false);
		// any output?
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", true);

		// kick them off
		errorGobbler.start();
		outputGobbler.start();

		// any error???
		proc.waitFor();

//		fos.flush();
//		fos.close();
	}

	public void actionsBeforeStart() throws Exception {
		seqs = new ArrayList<String>();
		seqNames = new ArrayList<String>();
	}

	public List<String> getSeqNames() {
		return seqNames;
	}

	public List<String> getSeqs() {
		return seqs;
	}

	public void setShowErrorMessage(boolean showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}

}
