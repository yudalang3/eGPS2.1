package module.alignedpro2cds;

import java.io.File;

import utils.EGPSUtil;

public class CLI {

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println("Usage:");
			System.err.println(cliUtilityName + " filePath geneticCodeName");
			System.err.println("Example: " + cliUtilityName + " aligned.protein.fas cds.fas aligned.cds.fas ");
			return;
		}
		String inputFilePath = args[0];

		AlignedProt2AlignedCDS.makeTheConversion(new File(inputFilePath), new File(args[1]), new File(args[2]));

		System.out.println("Successfully accomplished.");

	}

}
