package module.treenodecoll;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import utils.EGPSUtil;
import egps2.frame.ModuleFace;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class CLI {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println("Usage: " + cliUtilityName + " configFile");
			System.err.println("Warnings: do not use the whitespace, or the parameters will be split.");
			return;
		}

		IndependentModuleLoader independentModuleLoader = new IndependentModuleLoader();
		ModuleFace guiMain = independentModuleLoader.getFace();
		VOICM4TreeLeafInfoGainer voicm4TreeLeafInfoGainer = new VOICM4TreeLeafInfoGainer((GuiMain) guiMain);

		String path = args[0];
		System.out.println("The input path is: " + path);
		String fileToString = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);

		VoiceParameterParser voiceParameterParser = new VoiceParameterParser();
		OrganizedParameterGetter organizedParameterGetter = voiceParameterParser
				.getOrganizedParameterGetter(fileToString);

		try {
			voicm4TreeLeafInfoGainer.execute(organizedParameterGetter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Successfully execute.");
	}

}
