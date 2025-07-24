package module.fastatools;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JTabbedPane;

import com.google.common.collect.Lists;

import egps2.UnifiedAccessPoint;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.gui.EGPSCustomTabbedPaneUI;
import module.fastatools.gui.ExtractSequencePartialMatchPanel;
import module.fastatools.gui.FastaConcatenatePanel;
import module.fastatools.gui.GenomeFastaSummarizer;
import module.fastatools.gui.RenamerPartialMatchPanel;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleRunnerCreator;
import egps2.builtin.modules.voice.template.SubTabJPanel4Voice;
import egps2.builtin.modules.voice.template.SubTabJPanel4VoiceCreator;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	List<DIYToolSubTabModuleFace> listOfSubTabModuleFace = Lists.newArrayList();
	List<DockableTabModuleFaceOfVoice> listOfSubTabModuleFace2 = Lists.newArrayList();
	List<SubTabJPanel4Voice> listOfSubTabModuleFace3 = Lists.newArrayList();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		jTabbedPane.setFont(defaultTitleFont);

		{

			GenomeFastaSummarizer cal = new GenomeFastaSummarizer();
			DockableTabModuleRunnerCreator subtab = new DockableTabModuleRunnerCreator(this, cal);
			listOfSubTabModuleFace2.add(subtab);
			jTabbedPane.addTab(subtab.getTabName(), null, subtab, subtab.getShortDescription());
		}
		FastaConcatenatePanel panel1 = new FastaConcatenatePanel(this);
		listOfSubTabModuleFace.add(panel1);
		jTabbedPane.add("1. Fasta Concatenate", panel1);

		ExtractSequencePartialMatchPanel extractUniquePeptidePanel = new ExtractSequencePartialMatchPanel(this);
		listOfSubTabModuleFace.add(extractUniquePeptidePanel);
		jTabbedPane.add("2. Extract Sequence Partial Match", extractUniquePeptidePanel);

		RenamerPartialMatchPanel panel3 = new RenamerPartialMatchPanel();
		SubTabJPanel4VoiceCreator subTabJPanel4VoiceCreator = new SubTabJPanel4VoiceCreator(this, panel3);
		listOfSubTabModuleFace3.add(subTabJPanel4VoiceCreator);
		jTabbedPane.addTab(panel3.getTabName(), null, subTabJPanel4VoiceCreator, panel3.getShortDescription());

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
		return new String[] { "Download the annotation file from the Ensembl ftp." };
	}

	@Override
	protected void initializeGraphics() {
		for (DIYToolSubTabModuleFace diyToolSubTabModuleFace : listOfSubTabModuleFace) {
			diyToolSubTabModuleFace.initializeGraphics();
		}
		for (DockableTabModuleFaceOfVoice diyToolSubTabModuleFace : listOfSubTabModuleFace2) {
			diyToolSubTabModuleFace.initializeGraphics();
		}
		for (SubTabJPanel4Voice diyToolSubTabModuleFace : listOfSubTabModuleFace3) {
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
