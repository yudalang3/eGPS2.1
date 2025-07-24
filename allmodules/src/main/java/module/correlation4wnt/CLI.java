package module.correlation4wnt;

import egps2.frame.ModuleFace;
import egps2.builtin.modules.voice.template.CommandLineArgsInterpreter4VOICE;

public class CLI extends CommandLineArgsInterpreter4VOICE {

	@Override
	protected void process() throws Exception {
		ModuleLoader loader = new ModuleLoader();
		ModuleFace face = loader.getFace();
		GuiMain theFace = (GuiMain) face;
		PaintingPanel paintingPanel = theFace.getPaintingPanel();
		paintingPanel.setSize(width, height);

		VoiceImporter voicm4General2dPlot = theFace.getVoicm4General2dPlot();
		performVoiceExecute(voicm4General2dPlot);
		// save the patingPanel with user specified file format
		savePaintingPanel2file(paintingPanel);
	}

	public static void main(String[] args) throws Exception {
		CLI cli = new CLI();
		cli.parseOptions(args);
	}

}