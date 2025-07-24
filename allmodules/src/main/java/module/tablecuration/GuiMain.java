package module.tablecuration;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JTabbedPane;

import com.google.common.collect.Lists;

import egps2.UnifiedAccessPoint;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.gui.EGPSCustomTabbedPaneUI;
import module.tablecuration.gui.CountRowEntriesPanel;
import module.tablecuration.gui.ExtractTargetRowsPanel;
import module.tablecuration.gui.FilteringRowsAccording2AnotherTablePanel;
import module.tablecuration.gui.RemoveDupEntriesPanel;
import module.tablecuration.gui.TwoColumnTextComparatorPanel;
import module.tablecuration.gui.TwoTableComparatorPanel;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	List<DIYToolSubTabModuleFace> listOfSubTabModuleFace = Lists.newArrayList();
	List<DockableTabModuleFaceOfVoice> list2OfSubTabModuleFace = Lists.newArrayList();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		jTabbedPane.setFont(defaultTitleFont);

		{
			ExtractTargetRowsPanel panel3 = new ExtractTargetRowsPanel(this);
			listOfSubTabModuleFace.add(panel3);
			jTabbedPane.addTab("Extract target records", null, panel3,
					"Input table1, extract target rows according to directly entries or table2.");
		}
		{
			RemoveDupEntriesPanel panel3 = new RemoveDupEntriesPanel(this);
			listOfSubTabModuleFace.add(panel3);
			jTabbedPane.add("Remove duplicated enties", panel3);
		}
		{
			CountRowEntriesPanel panel3 = new CountRowEntriesPanel(this);
			listOfSubTabModuleFace.add(panel3);
			jTabbedPane.add("Count row entries", panel3);
		}
		{
			TwoTableComparatorPanel panel3 = new TwoTableComparatorPanel(this);
			list2OfSubTabModuleFace.add(panel3);
			jTabbedPane.add("Two Table Comparator", panel3);
		}
		{
			TwoColumnTextComparatorPanel panel3 = new TwoColumnTextComparatorPanel(this);
			listOfSubTabModuleFace.add(panel3);
			jTabbedPane.add("Two Column Text Comparator", panel3);
		}
		{
			FilteringRowsAccording2AnotherTablePanel panel3 = new FilteringRowsAccording2AnotherTablePanel(this);
			listOfSubTabModuleFace.add(panel3);
			jTabbedPane.add(panel3.getTabName(), panel3);
		}

		jTabbedPane.setUI(new EGPSCustomTabbedPaneUI());
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
		return new String[] { "Identify the homologous genes" };
	}

	@Override
	protected void initializeGraphics() {
		for (DIYToolSubTabModuleFace diyToolSubTabModuleFace : listOfSubTabModuleFace) {
			diyToolSubTabModuleFace.initializeGraphics();
		}
		for (DockableTabModuleFaceOfVoice diyToolSubTabModuleFace : list2OfSubTabModuleFace) {
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
