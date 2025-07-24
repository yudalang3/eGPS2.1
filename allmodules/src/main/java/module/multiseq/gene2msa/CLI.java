package module.multiseq.gene2msa;

import java.io.IOException;

import utils.EGPSUtil;

public class CLI {
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println("Usage: " + cliUtilityName + " configFile");
			System.err.println("Warnings: do not use the whitespace, or the parameters will be split.");
			return;
		}
		
//		IndependentModuleLoader independentModuleLoader = new IndependentModuleLoader();
//		GuiMain guiMain = (GuiMain) independentModuleLoader.getFace();
//		VOICM4TreeLeafInforObtainer voicm4TreeLeafInforObtainer = new VOICM4TreeLeafInforObtainer(guiMain);
//		
//		String path = args[0];
//		System.out.println("The input path is: " + path);
//		String fileToString = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
//		voicm4TreeLeafInforObtainer.execute(fileToString);
//		
//		System.out.println("Successfully execute.");
	}

}
