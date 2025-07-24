package module.fastadumper.extractmatch;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import utils.EGPSUtil;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;


public class CLI {

	public static void main(String[] args) throws IOException {

		if (args.length < 1) {
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println(cliUtilityName + " configFile");
			return;
		}

		String inputs = FileUtils.readFileToString(new File(args[0]), StandardCharsets.UTF_8);

		VoiceParameterParser voiceParameterParser = new VoiceParameterParser();

		List<String> lists = voiceParameterParser.getStringsFromLongSingleLine(inputs);

		FastaDumperRunner fastaDumperRunner = new FastaDumperRunner();
		fastaDumperRunner.run(lists);
	}

}
