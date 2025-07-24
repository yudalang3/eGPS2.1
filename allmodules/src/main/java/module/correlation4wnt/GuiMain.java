package module.correlation4wnt;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import egps2.utils.common.util.SaveUtil;
import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;

public class GuiMain extends ModuleFace {

	private VoiceImporter voicm4General2dPlot;
	private PaintingPanel paintingPanel;

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

		paintingPanel = new PaintingPanel();
		JScrollPane jScrollPane = new JScrollPane(paintingPanel);
		add(jScrollPane, BorderLayout.CENTER);
	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		VoiceImporter voicm4General2dPlot2 = getVoicm4General2dPlot();
		voicm4General2dPlot2.doUserImportAction();
	}

	public VoiceImporter getVoicm4General2dPlot() {
		if (voicm4General2dPlot == null) {
			voicm4General2dPlot = new VoiceImporter(this);
		}
		return voicm4General2dPlot;
	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		new SaveUtil().saveData(paintingPanel);
	}

	@Override
	protected void initializeGraphics() {
//		importData();
	}

	public PaintingPanel getPaintingPanel() {
		return paintingPanel;
	}
}
