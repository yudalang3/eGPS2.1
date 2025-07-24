package module.multiseqview.io;

import java.util.Optional;

public class ImportInfoBean4gBrowser {

	/**
	 * 这里的type指的是 从Rest接口获取到数据的 type属性。我初步看了一下，先用 Pfam注释的结果好了。
	 */
	private String targetType = "Pfam";
	private String proteinStructAnnotationPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\protein\\DVL.features.human.tsv";
	private String proteinSequenceFastaPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\protein\\DVL.features.human.tsv\\prot.human.DVL.fa";

	protected String sequenceNameTranlationPath;
	/**
	 * 0-based
	 */
	protected int needToTransferIDIndex = 0;
	/**
	 * 0-based
	 */
	protected int wantedNameIndex = 1;
	
	
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getProteinStructAnnotationPath() {
		return proteinStructAnnotationPath;
	}

	public void setProteinStructAnnotationPath(String proteinStructAnnotationPath) {
		this.proteinStructAnnotationPath = proteinStructAnnotationPath;
	}

	public String getProteinSequenceFastaPath() {
		return proteinSequenceFastaPath;
	}

	public void setProteinSequenceFastaPath(String proteinSequenceFastaPath) {
		this.proteinSequenceFastaPath = proteinSequenceFastaPath;
	}

	public Optional<String> getSequenceNameTranlationPath() {
		return Optional.ofNullable(sequenceNameTranlationPath);
	}

	public void setSequenceNameTranlationPath(String sequenceNameTranlationPath) {
		this.sequenceNameTranlationPath = sequenceNameTranlationPath;
	}

	public int getNeedToTransferIDIndex() {
		return needToTransferIDIndex;
	}

	public void setNeedToTransferIDIndex(int needToTransferIDIndex) {
		this.needToTransferIDIndex = needToTransferIDIndex;
	}

	public int getWantedNameIndex() {
		return wantedNameIndex;
	}

	public void setWantedNameIndex(int wantedNameIndex) {
		this.wantedNameIndex = wantedNameIndex;
	}
	
	
	

}
