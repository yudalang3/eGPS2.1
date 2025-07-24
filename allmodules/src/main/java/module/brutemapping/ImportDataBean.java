package module.brutemapping;

public class ImportDataBean {

	String queryFastaPath;
	String genomeFastaPath;

	String onlyIncludeKeySeqeunce;
	String excludeSequenceName;
	String outputBedPath;

	boolean needReverseComplement = false;
	private String outputTotalCountFilePath;

	public String getOnlyIncludeKeySeqeunce() {
		return onlyIncludeKeySeqeunce;
	}

	public void setOnlyIncludeKeySeqeunce(String onlyIncludeKeySeqeunce) {
		this.onlyIncludeKeySeqeunce = onlyIncludeKeySeqeunce;
	}

	public String getQueryFastaPath() {
		return queryFastaPath;
	}

	public void setQueryFastaPath(String queryFastaPath) {
		this.queryFastaPath = queryFastaPath;
	}

	public String getGenomeFastaPath() {
		return genomeFastaPath;
	}

	public void setGenomeFastaPath(String genomeFastaPath) {
		this.genomeFastaPath = genomeFastaPath;
	}

	public String getOutputBedPath() {
		return outputBedPath;
	}

	public void setOutputBedPath(String outputBedPath) {
		this.outputBedPath = outputBedPath;
	}

	public boolean isNeedReverseComplement() {
		return needReverseComplement;
	}

	public void setNeedReverseComplement(boolean needReverseComplement) {
		this.needReverseComplement = needReverseComplement;
	}

	public String getExcludeSequenceName() {
		return excludeSequenceName;
	}

	public void setExcludeSequenceName(String excludeSequenceName) {
		this.excludeSequenceName = excludeSequenceName;
	}

	public void setOutputTotalCountFilePath(String optionValue) {
		this.outputTotalCountFilePath = optionValue;
	}

	public String getOutputTotalCountFilePath() {
		return outputTotalCountFilePath;
	}
}
