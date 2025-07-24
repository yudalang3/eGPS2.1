package module.skeletonscatter;

import java.awt.BorderLayout;

import egps2.utils.common.util.SaveUtil;
import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;


@SuppressWarnings("serial")
public class MainFace extends ModuleFace {

	private VOICM4General2dPlot histogramDataImportHandler;
	PaintingPanel paintingPanel;

	MainFace(IModuleLoader moduleLoader) {
		super(moduleLoader);
	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public void changeToThisTab() {

	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		VOICM4General2dPlot handler = getHistogramDataImportHandler();
		handler.doUserImportAction();

	}

	public VOICM4General2dPlot getHistogramDataImportHandler() {
		if (histogramDataImportHandler == null) {
			histogramDataImportHandler = new VOICM4General2dPlot(this);
		}
		return histogramDataImportHandler;
	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		new SaveUtil().saveData(paintingPanel, getClass());
	}


	@Override
	public void initializeGraphics() {
		paintingPanel = new PaintingPanel();
		add(paintingPanel, BorderLayout.CENTER);
		importData();

	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Plot the scatter skeleton" };
	}

}
