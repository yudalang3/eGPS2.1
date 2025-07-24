package module.targetoftf;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JTabbedPane;

import com.google.common.collect.Lists;

import egps2.UnifiedAccessPoint;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.frame.ComputationalModuleFace;
import module.targetoftf.gui.BatchRunningFimoFromGff3Results;
import module.targetoftf.gui.FimoResultsCurationPanel;
import module.targetoftf.gui.MappingMotifToGenomePanel;
import module.targetoftf.gui.MotifCollectionGuidePanel;
import module.targetoftf.gui.UseTheDerectMappingTools;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	List<DockableTabModuleFaceOfVoice> listOfSubTabModuleFace = Lists.newArrayList();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		jTabbedPane.setFont(defaultTitleFont);

		{
			MotifCollectionGuidePanel panel = new MotifCollectionGuidePanel();
			jTabbedPane.addTab(panel.getTabName(),null,panel, panel.getShortDescription());
		}
		{
			MappingMotifToGenomePanel panel = new MappingMotifToGenomePanel();
			jTabbedPane.addTab(panel.getTabName(),null,panel, panel.getShortDescription());
		}
		{
			UseTheDerectMappingTools panel = new UseTheDerectMappingTools();
			jTabbedPane.addTab(panel.getTabName(), null, panel, panel.getShortDescription());
		}
		{
			FimoResultsCurationPanel panel = new FimoResultsCurationPanel();
			jTabbedPane.addTab(panel.getTabName(), null, panel, panel.getShortDescription());
		}
		{
			BatchRunningFimoFromGff3Results panel = new BatchRunningFimoFromGff3Results();
			jTabbedPane.addTab(panel.getTabName(), null, panel, panel.getShortDescription());
		}


		add(jTabbedPane);
	}

	@Override
	public boolean canImport() {
		return false;
	}

	@Override
	public void importData() {

	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {

	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Download the annotation file from the Ensembl ftp." };
	}

	@Override
	protected void initializeGraphics() {
		for (DockableTabModuleFaceOfVoice diyToolSubTabModuleFace : listOfSubTabModuleFace) {
			diyToolSubTabModuleFace.initializeGraphics();
		}
	}
	
	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The data is loading from the import dialog.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The functionality is powered by the eGPS software.";
			}
		};
		return iInformation;
	}

}
