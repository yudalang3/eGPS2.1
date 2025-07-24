package module.gff3opr;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JTabbedPane;

import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleRunnerCreator;
import org.apache.commons.compress.utils.Lists;

import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.gui.EGPSCustomTabbedPaneUI;
import module.gff3opr.gui.BatchRunningExtractWithEnsemblCanAndObtainSequence;
import module.gff3opr.gui.ExtractTSSUpstreamOfGene;
import module.gff3opr.gui.ExtractUpstreamOfGeneByEnsemblCan;
import module.gff3opr.gui.ExtractUpstreamOfTopFeature;
import module.gff3opr.gui.GlimmpseOfTheGFF3File;
import module.gff3opr.gui.ObtainSequenceFromBedFile;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.modulei.IModuleLoader;

/**
 * <pre>
 * |---------------------------------------------------------------|
 * |mainSplitPanel              |                                  |
 * |                            |                                  |
 * |LeftControlPanelContainner  |  rightSplitPane                  |
 * |                            |                                  |
 * |                            |  tabbedPhylogeneticTreePane      | 
 * |                            |                                  |
 * |                            | ---------------------------------|
 * |                            |                                  |
 * |                            |  tabbedAnalysisPanel             | 
 * |---------------------------------------------------------------|
 * 
 * </pre>
 * 
 * @author yudalang
 *
 */
@SuppressWarnings("serial")
public class GFF3OperatorMainFace extends ComputationalModuleFace {

	private List<DockableTabModuleFaceOfVoice> subTabs = Lists.newArrayList();

	GFF3OperatorMainFace(IModuleLoader moduleLoader) {
		super(moduleLoader);

		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);

		jTabbedPane.setFont(defaultTitleFont);

		{
			GlimmpseOfTheGFF3File subtab = new GlimmpseOfTheGFF3File(this);
			subTabs.add(subtab);
			jTabbedPane.addTab(subtab.getTabName(), null, subtab, subtab.getShortDescription());
		}
		{
			ExtractUpstreamOfTopFeature subtab = new ExtractUpstreamOfTopFeature(this);
			subTabs.add(subtab);
			jTabbedPane.addTab(subtab.getTabName(), null, subtab, subtab.getShortDescription());
		}
		{
			ExtractTSSUpstreamOfGene subtab = new ExtractTSSUpstreamOfGene(this);
			subTabs.add(subtab);
			jTabbedPane.addTab(subtab.getTabName(), null, subtab, subtab.getShortDescription());
		}
		{

			ExtractUpstreamOfGeneByEnsemblCan cal = new ExtractUpstreamOfGeneByEnsemblCan();
			DockableTabModuleRunnerCreator subtab = new DockableTabModuleRunnerCreator(this, cal);
			subTabs.add(subtab);
			jTabbedPane.addTab(subtab.getTabName(), null, subtab, subtab.getShortDescription());
		}
		{
			ObtainSequenceFromBedFile cal = new ObtainSequenceFromBedFile();
			DockableTabModuleRunnerCreator subtab = new DockableTabModuleRunnerCreator(this, cal);
			subTabs.add(subtab);
			jTabbedPane.addTab(subtab.getTabName(), null, subtab, subtab.getShortDescription());
		}
		{
			BatchRunningExtractWithEnsemblCanAndObtainSequence cal = new BatchRunningExtractWithEnsemblCanAndObtainSequence();
			DockableTabModuleRunnerCreator subtab = new DockableTabModuleRunnerCreator(this, cal);
			subTabs.add(subtab);
			jTabbedPane.addTab(subtab.getTabName(), null, subtab, subtab.getShortDescription());
		}

		jTabbedPane.setUI(new EGPSCustomTabbedPaneUI());
		add(jTabbedPane, BorderLayout.CENTER);
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

	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {
	}

	@Override
	public void initializeGraphics() {

		for (DockableTabModuleFaceOfVoice dockableTabModuleFaceOfVoice : subTabs) {
			dockableTabModuleFaceOfVoice.initializeGraphics();
		}
	}

	@Override
	public String[] getFeatureNames() {
		return null;
	}

}
