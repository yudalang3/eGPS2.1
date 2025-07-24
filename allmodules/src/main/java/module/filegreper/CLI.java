package module.filegreper;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import utils.string.EGPSStringUtil;
import utils.EGPSUtil;

public class CLI {

	public static void main(String[] args) throws Exception {
		if (args.length < 4) {
			System.err.println("Usage:");
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println(cliUtilityName
					+ " queryString filePath ifExportHeaderLine neighborLineNumber");
			System.err.println("Example: " + cliUtilityName + " query input/file/path T 0");
			return;
		}
		String queryString = args[0];
		String inputFilePath = args[1];
		boolean exportHeaderLine = BooleanUtils.toBoolean(args[2]);
		int neiberLineNumber = Integer.parseInt(args[3]);

		DoGrepAction doGrepAction = new DoGrepAction();
		doGrepAction.inputFilePath = inputFilePath;
		doGrepAction.searchKeys = EGPSStringUtil.split(queryString, '|');
		doGrepAction.exportHeader = exportHeaderLine;
		doGrepAction.followingLineNumber = neiberLineNumber;

		doGrepAction.doIt();

		List<String> outputList = doGrepAction.getOutputList();
		for (String string : outputList) {
			System.out.println(string);
		}

	}

}
