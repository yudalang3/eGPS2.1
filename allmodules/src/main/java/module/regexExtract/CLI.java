package module.regexExtract;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import utils.EGPSUtil;

public class CLI {
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println("Usage: " + cliUtilityName + " configFile");
			System.err.println("Warnings: do not use the whitespace, or the parameters will be split.");
			return;
		}
		
		IndependentModuleLoader independentModuleLoader = new IndependentModuleLoader();
		GuiMain guiMain = (GuiMain) independentModuleLoader.getFace();
		VOICM4RegexExtract voicm = new VOICM4RegexExtract(guiMain);
		
		String path = args[0];
		System.out.println("The input path is: " + path);
		String fileToString = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
		voicm.execute(fileToString);
		System.out.println("Successfully execute.");
	}
	

}
