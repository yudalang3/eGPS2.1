package module.homoidentify;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JTabbedPane;

import com.google.common.collect.Lists;

import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.gui.EGPSCustomTabbedPaneUI;
import module.homoidentify.gui.AnnotateGeneWithSpeciesPanel;
import module.homoidentify.gui.CurateDataWithSpeciesPanel;
import module.homoidentify.gui.FindWithBlastpPanel;
import module.homoidentify.gui.FindWithHmmerPanel;
import module.homoidentify.gui.HmmerResultsFilteringPanel;
import module.homoidentify.gui.SpeciesInfoDownloadInstructPanel;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

public class GuiMain extends ComputationalModuleFace {

	List<DIYToolSubTabModuleFace> listOfSubTabModuleFace = Lists.newArrayList();
	List<DockableTabModuleFaceOfVoice> list2OfSubTabModuleFace = Lists.newArrayList();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		jTabbedPane.setUI(new EGPSCustomTabbedPaneUI());
		jTabbedPane.setFont(defaultTitleFont);


		jTabbedPane.add("1. Find With Hmmer", new FindWithHmmerPanel());

		jTabbedPane.add("2. Find With Blastp", new FindWithBlastpPanel());

		HmmerResultsFilteringPanel panel3 = new HmmerResultsFilteringPanel(this);
		list2OfSubTabModuleFace.add(panel3);
		jTabbedPane.add("3. Hmmer Results Filtering", panel3);

		jTabbedPane.add("4. Species Info. Download Instruction", new SpeciesInfoDownloadInstructPanel());

		AnnotateGeneWithSpeciesPanel panel5 = new AnnotateGeneWithSpeciesPanel(this);
		listOfSubTabModuleFace.add(panel5);
		jTabbedPane.addTab("5. Annotate Gene with Species info.", null, panel5,
				"Note: the species information directly download from the Ensembl need manually check.");

		CurateDataWithSpeciesPanel panel6 = new CurateDataWithSpeciesPanel();
		jTabbedPane.addTab("6. Curate Data With Species Info.", null, panel6,
				"Curate the annotated homologous results with species information.");

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
