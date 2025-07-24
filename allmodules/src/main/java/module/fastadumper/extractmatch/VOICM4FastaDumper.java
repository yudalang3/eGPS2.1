package module.fastadumper.extractmatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import module.fastadumper.GuiMain;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class VOICM4FastaDumper extends VersatileOpenInputClickAbstractGuiBase {

	private VoiceParameterParser voiceParameterParser = new VoiceParameterParser();
	private FastaDumperRunner fastaDumperRunner = new FastaDumperRunner(this);
	
	private GuiMain guiMain;

	public VOICM4FastaDumper(GuiMain guiMain) {
		this.guiMain = guiMain;
	}

	@Override
	public String getExampleText() {
		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append("# This is annotation.\n");
		sBuilder.append("# The inputPath parameter could be a directory or file. If input a directory, see the $fileSuffix parameter.\n");
		sBuilder.append("# Following is an example\n");

		sBuilder.append("$inputPath=config/bioData/sequenceView/multiSeq/Wnt.features.human/prot.human.Wnt.fa\n");
		sBuilder.append("# The fileSuffix parameter has no effect when the $inputPath is a file\n");
		sBuilder.append("# When $inputPath is a dir. only file with following suffix will process. Default is fas.\n");
		sBuilder.append("# For example: a dir has file a.fas and b.txt. Only the first will be processed.\n");
		sBuilder.append("$fileSuffix=fas\n");
		sBuilder.append("# The removeGap parameter indicates whether keep the original alignment\n");
		sBuilder.append("# If true all '-' character will be removed\n");
		sBuilder.append("$removeGap=T\n");
		sBuilder.append("# The outputFile parameter means the location to export\n");
		sBuilder.append("$outputFile=\n");
		sBuilder.append("## Following are sequences you want to extract.\n");
		sBuilder.append("# Each element seperate with tab key.\n");
		sBuilder.append("# Each row represents a sequence that you want to extract.\n");
		sBuilder.append("# Four columns are: target_name\tstart_position(1-based)\tend_position(1-based)\tnew_name.\n");
		sBuilder.append("# Except for the first column, none of the other columns are mandatory.\n");
		sBuilder.append("# Records without position will be considered as full length.\n");
		sBuilder.append("# Note: when $removeGap=T, the second and third columns refers to the position of the sequence that gaps have been removed.\n");
		sBuilder.append("ENSP00000285018\n");
		sBuilder.append("ENSP00000222462	5\n");
		sBuilder.append("ENSP00000258411	6	100\n");
		sBuilder.append("ENSP00000264634	6	100	Human_WNT");
		return sBuilder.toString();
	}

	@Override
	public void execute(String inputs) throws Exception {
		List<String> lists = voiceParameterParser.getStringsFromLongSingleLine(inputs);

		try {
			guiMain.clearConsole();
			fastaDumperRunner.run(lists);
			this.guiMain.invokeTheFeatureMethod(0);
		} catch (Exception e) {
			List<String> strings = new ArrayList<>();
			strings.add("Errors:");
			strings.add(e.getMessage());
			guiMain.setText4Console(strings);
		}
	}
	
	public void setContent4progress(String str) {
		guiMain.setText4Console(Arrays.asList(str));
	}

}
