package module.localblast.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONObject;

import egps2.EGPSProperties;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import module.localblast.gui.dbtools.DataBaseToolsPanel;
import egps2.modulei.CreditBean;
import egps2.modulei.IInformation;

@SuppressWarnings("serial")
public class LocalBlastMain extends ComputationalModuleFace {

	private final String STORE_PATH = EGPSProperties.JSON_DIR.concat("/localBlast.json");
	private AreadyInstalledBlastSoftwareBean areadyInstalledBlastSoftwareBean;
	private SearchToolsPanel searchTools;

	/**
	 * Create the panel.
	 * 
	 * @param independentModuleLoader
	 */
	protected LocalBlastMain(IndependentModuleLoader independentModuleLoader) {
		super(independentModuleLoader);
		setLayout(new BorderLayout(0, 0));

		setBackground(Color.white);
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

//		JTabbedPane tabbedPane = new JideTabbedPane(JideTabbedPane.LEFT);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

		tabbedPane.setFont(defaultTitleFont);
		add(tabbedPane, BorderLayout.CENTER);

		File file = new File(STORE_PATH);
		if (file.exists()) {
			try {
				String fileToString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
				areadyInstalledBlastSoftwareBean = JSONObject.parseObject(fileToString,
						AreadyInstalledBlastSoftwareBean.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			areadyInstalledBlastSoftwareBean = new AreadyInstalledBlastSoftwareBean();
		}

		JPanel softwareDetectionPanel = new SoftwareDetectionPanel(this);
		tabbedPane.addTab("Software detection", null, softwareDetectionPanel, "Configuration the software.");

		JPanel dataBaseTool = new DataBaseToolsPanel(this);
		tabbedPane.addTab("Database tools", null, dataBaseTool, "Formats input FASTA file(s) into a BLAST database for further utils.");

		searchTools = new SearchToolsPanel(this);
		tabbedPane.addTab("Search tools", null, searchTools, "Employing the built databased to search targets.");

		// 后面两个先去掉
//		JPanel seqFilteringTools = new SequenceFilteringPanel();
//		tabbedPane.addTab("Sequence filtering tools", null, seqFilteringTools, null);
//		
//		JPanel remoteBlast = new RemoteBlastPanel();
//		tabbedPane.addTab("Remote blast", null, remoteBlast, null);

	}

	@Override
	public boolean closeTab() {
		areadyInstalledBlastSoftwareBean.setDbFilePath(searchTools.getBlastnDBFilePath());
		areadyInstalledBlastSoftwareBean.setProteinDBPath(searchTools.getBlastpDBFilePath());
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(STORE_PATH));
			JSONObject.writeJSONString(fileOutputStream,StandardCharsets.UTF_8 ,areadyInstalledBlastSoftwareBean);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
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
		return new String[] { "Features AS subtitles" };
	}

	@Override
	protected void initializeGraphics() {

	}

	public AreadyInstalledBlastSoftwareBean getAreadyInstalledBlastSoftwareBean() {
		return areadyInstalledBlastSoftwareBean;
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
				return "The convenient GUI functionalities are powered by the BLAST program and eGPS software. Please cite BLAST first.";
			}
			
			@Override
			public String getHowUserOperates() {
				return "The parameters are entered by users. All mandatory parameters are passed to the local blast software.";
			}
		};
		return iInformation;
	}

	@Override
	public CreditBean getDevTeam() {
		CreditBean creditBean = new CreditBean();
		creditBean.setWebSite("https://blast.ncbi.nlm.nih.gov/doc/blast-help/references.html");
		creditBean.setTeam("BLAST Team");

		String evolGeneDevs = creditBean.getDevelopers();
		creditBean.setDevelopers("The NIH blast development team | " + evolGeneDevs);
		return creditBean;
	}
}
