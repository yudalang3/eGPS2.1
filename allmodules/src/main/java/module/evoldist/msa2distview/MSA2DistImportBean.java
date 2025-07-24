package module.evoldist.msa2distview;

import module.multiseq.alignment.view.io.AlignmentImportBean;

public class MSA2DistImportBean extends AlignmentImportBean {
	
	private boolean outputToFile = false;
	private String outputPath = "";
	public boolean isOutputToFile() {
		return outputToFile;
	}
	public void setOutputToFile(boolean outputToFile) {
		this.outputToFile = outputToFile;
	}
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
	

}
