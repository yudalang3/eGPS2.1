package module.benchensdownloader;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JTabbedPane;

import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.gui.EGPSCustomTabbedPaneUI;
import module.benchensdownloader.gui.AlreadyDownloadCheckerPanel;
import module.benchensdownloader.gui.AlternativeDownloadApproachPanel;
import module.benchensdownloader.gui.DownloadFileValidator;
import module.benchensdownloader.gui.SpeciesCognitionPanel;
import module.benchensdownloader.gui.URLDownloaderPanel;
import module.benchensdownloader.gui.UrlParserPanel;
import module.benchensdownloader.gui.UrlParserPanelOnlyOneLevel;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {


	private SpeciesCognitionPanel speciesRecongPanel;
	private UrlParserPanel urlBatchDownloader;
	private URLDownloaderPanel urlDownloaderPanel;
	private AlreadyDownloadCheckerPanel alreadyDownloadCheckerPanel;
	private DownloadFileValidator downloadFileValidator;
	private UrlParserPanelOnlyOneLevel urlParserPanelOnlyOneLevel;

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);

		jTabbedPane.setFont(defaultTitleFont);

		speciesRecongPanel = new SpeciesCognitionPanel(this);
		jTabbedPane.addTab("1. Species Recongnization", speciesRecongPanel);
		urlBatchDownloader = new UrlParserPanel(this);
		jTabbedPane.addTab("2.1 URL Batch Parser", urlBatchDownloader);

		urlParserPanelOnlyOneLevel = new UrlParserPanelOnlyOneLevel(this);
		jTabbedPane.addTab(urlParserPanelOnlyOneLevel.getTabName(), null, urlParserPanelOnlyOneLevel,
				urlParserPanelOnlyOneLevel.getShortDescription());


		alreadyDownloadCheckerPanel = new AlreadyDownloadCheckerPanel(this);
		jTabbedPane.addTab("3. Already Download Checker", alreadyDownloadCheckerPanel);

		urlDownloaderPanel = new URLDownloaderPanel(this);
		jTabbedPane.addTab("4. URL Batch Downloader", urlDownloaderPanel);

		AlternativeDownloadApproachPanel aldownAppPanel = new AlternativeDownloadApproachPanel();
		jTabbedPane.addTab("5. Alternative Download Approach", aldownAppPanel);

		downloadFileValidator = new DownloadFileValidator(this);
		jTabbedPane.addTab("6. Download File Validator", downloadFileValidator);

		jTabbedPane.setUI(new EGPSCustomTabbedPaneUI());
		add(jTabbedPane);
	}


	@Override
	public void changeToThisTab() {

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
		speciesRecongPanel.initializeGraphics();
		urlBatchDownloader.initializeGraphics();
		urlDownloaderPanel.initializeGraphics();
		alreadyDownloadCheckerPanel.initializeGraphics();
		downloadFileValidator.initializeGraphics();
		urlParserPanelOnlyOneLevel.initializeGraphics();
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
