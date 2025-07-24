package module.geoprocessor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JTabbedPane;

import com.google.common.collect.Lists;

import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import module.geoprocessor.gui.ContentsExtractorWithTagPanel;
import module.geoprocessor.gui.SoftFormattedFamilyExtractorPanel;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	List<DIYToolSubTabModuleFace> listOfSubTabModuleFace = Lists.newArrayList();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		jTabbedPane.setFont(defaultTitleFont);

		{
			ContentsExtractorWithTagPanel panel = new ContentsExtractorWithTagPanel(this);
			listOfSubTabModuleFace.add(panel);
			jTabbedPane.add("1. Contents extractor with start/end tag", panel);
		}
		{
			SoftFormattedFamilyExtractorPanel panel = new SoftFormattedFamilyExtractorPanel(this);
			listOfSubTabModuleFace.add(panel);
			jTabbedPane.add("2. Soft Formatted Family Sample Info. extractor", panel);
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
		for (DIYToolSubTabModuleFace diyToolSubTabModuleFace : listOfSubTabModuleFace) {
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
