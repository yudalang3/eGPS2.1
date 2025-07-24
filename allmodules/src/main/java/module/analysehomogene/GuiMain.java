package module.analysehomogene;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JTabbedPane;

import com.google.common.collect.Lists;

import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.gui.EGPSCustomTabbedPaneUI;
import module.analysehomogene.gui.InferringInternalNodeStatesPanel;
import module.analysehomogene.gui.StatisticalSignificantTestPanel;
import module.analysehomogene.gui.SummaryNodeStatesResultsPanel;
import module.analysehomogene.gui.VisualizeTheInferredResultsPanel;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

public class GuiMain extends ComputationalModuleFace {

	List<DockableTabModuleFaceOfVoice> listOfSubTabModuleFace = Lists.newArrayList();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		jTabbedPane.setUI(new EGPSCustomTabbedPaneUI());
		jTabbedPane.setFont(defaultTitleFont);

		{
			InferringInternalNodeStatesPanel panel = new InferringInternalNodeStatesPanel(this);
			listOfSubTabModuleFace.add(panel);
			jTabbedPane.addTab(panel.getTabName(),null,panel, panel.getShortDescription());
		}
		{
			SummaryNodeStatesResultsPanel panel = new SummaryNodeStatesResultsPanel(this);
			listOfSubTabModuleFace.add(panel);
			jTabbedPane.addTab(panel.getTabName(),null,panel, panel.getShortDescription());
		}

		{
			VisualizeTheInferredResultsPanel panel = new VisualizeTheInferredResultsPanel(this);
			listOfSubTabModuleFace.add(panel);
			jTabbedPane.addTab(panel.getTabName(),null,panel, panel.getShortDescription());
		}

		{
			StatisticalSignificantTestPanel panel = new StatisticalSignificantTestPanel(this);
			listOfSubTabModuleFace.add(panel);
			jTabbedPane.addTab(panel.getTabName(),null,panel, panel.getShortDescription());
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
