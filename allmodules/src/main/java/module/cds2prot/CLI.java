package module.cds2prot;

import java.util.List;

import utils.EGPSUtil;
import geneticcodes.IGeneticCode;

public class CLI {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage:");
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println(cliUtilityName + " filePath geneticCodeName");
			System.err.println(
					"Example: " + cliUtilityName + " input/file/path " + IGeneticCode.GeneticCodeTableNames[0]);
			return;
		}
		String inputFilePath = args[0];

		DoCds2ProtTranslateAction doGrepAction = new DoCds2ProtTranslateAction();
		doGrepAction.inputFilePath = inputFilePath;
		doGrepAction.geneticodeName = args[1];

		doGrepAction.doIt();

		List<String> outputList = doGrepAction.getOutputList();
		for (String string : outputList) {
			System.out.println(string);
		}

	}

}
