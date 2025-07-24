package module.localblast.gui;

public class AreadyInstalledBlastSoftwareBean {

	private String binDirPath = "";
	private String dbFilePath = "";
	private String proteinDBPath = "";

	public String getBinDirPath() {
		return binDirPath;
	}

	public void setBinDirPath(String binDirPath) {
		this.binDirPath = binDirPath;
	}
	
	public String getWindowsBlastN() {
		return binDirPath.concat("/blastn.exe");
	}

	public String getWindowsBlastP() {
		return binDirPath.concat("/blastp.exe");
	}
	public String getWindowsMakeBlastDB() {
		return binDirPath.concat("/makeblastdb.exe");
	}

	public String getDbFilePath() {
		return dbFilePath;
	}

	public void setDbFilePath(String dbFilePath) {
		this.dbFilePath = dbFilePath;
	}

	public String getProteinDBPath() {
		return proteinDBPath;
	}

	public void setProteinDBPath(String proteinDBPath) {
		this.proteinDBPath = proteinDBPath;
	}
	
	
}
