package module.multiseq.alignment.view.io;

import egps2.EGPSProperties;
import msaoperator.io.seqFormat.MSA_DATA_FORMAT;

public class AlignmentImportBean {

	private String filePath = EGPSProperties.PROPERTIES_DIR + "/bioData/genomicMuts/example.longAlignedFasta_2.fas";
	private String fileFormat = MSA_DATA_FORMAT.ALIGNED_FASTA.getName();

	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

}
