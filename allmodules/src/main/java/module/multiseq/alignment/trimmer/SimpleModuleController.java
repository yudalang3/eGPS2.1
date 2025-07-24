package module.multiseq.alignment.trimmer;

import egps2.Authors;
import module.multiseq.alignment.trimmer.gui.DataImportPanel;
import module.multiseq.alignment.trimmer.gui.ParametersPanel;

public class SimpleModuleController {

	private SimpleModuleMain main;
	private DataImportPanel dataImportPanel;
	private ParametersPanel parametersPanel;
	private String[] infos;

	public SimpleModuleController(SimpleModuleMain simpleModuleMain) {
		this.main = simpleModuleMain;
	}

	public String[] getTeamAndAuthors() {
		if (infos == null) {
			infos = new String[3];
			infos[0] = "EvolGen";
			infos[1] = Authors.YUDALANG + "," + Authors.LIHAIPENG;
			infos[2] = "http://www.picb.ac.cn/evolgen/";
		}
		return infos;
	}


	public String getInputFilePath() {
		return dataImportPanel.getInputFilePath();
	}

	public String getRefSequenceName() {
		return parametersPanel.getRefSequenceName();
	}

	public int getRefSequenceStartPos() {
		return parametersPanel.getRefSequenceStartPos();
	}

	public int getRefSequenceEndPos() {
		return parametersPanel.getRefSequenceEndPos();
	}

	public void setDataImportPanel(DataImportPanel dataImportPanel) {
		this.dataImportPanel = dataImportPanel;

	}

	public ParametersPanel getParametersPanel() {
		return parametersPanel;
	}

	public void setParametersPanel(ParametersPanel parametersPanel) {
		this.parametersPanel = parametersPanel;

	}

}
