package module.evolview.treebarplot;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

import module.evolview.treebarplot.gui.PaintingPanel;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.CommandLineArgsInterpreter4VOICE;

public class CLI extends CommandLineArgsInterpreter4VOICE {

	@Override
	protected void process() throws Exception {
		IndependentModuleLoader loader = new IndependentModuleLoader();
		// Get the remnant face
		GuiMain face = (GuiMain) loader.getFace();
		JScrollPane getjScollPanel = face.getjScollPanel();
		
		JViewport viewport = getjScollPanel.getViewport();
		viewport.setSize(width, height);
		face.setSize(width, height);
		PaintingPanel paintingPanel = face.paintingPanel;
		paintingPanel.setSize(width, height);

		// set the configuration file content
		VOICM4TreeBarPlot voicm4TreeLeafInforObtainer = face.getVoicm4TreeLeafInforObtainer();
		OrganizedParameterGetter o = getConfigFileOrganizedParameterGetter();
		voicm4TreeLeafInforObtainer.execute(o);

		// save the patingPanel with user specified file format
		savePaintingPanel2file(paintingPanel);
	}

	public static void main(String[] args) throws Exception {
		CLI cli = new CLI();
		cli.parseOptions(args);
	}

}
