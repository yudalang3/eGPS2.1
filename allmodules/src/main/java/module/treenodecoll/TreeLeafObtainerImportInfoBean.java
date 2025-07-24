package module.treenodecoll;

import module.evoltreio.EvolTreeImportInfoBean;

public class TreeLeafObtainerImportInfoBean extends EvolTreeImportInfoBean {

	private int ladderize = 0;
	
	private String exportPath;

	/**
	 * 0 for all,
	 * 1 for internal node,
	 * 2 for leaf
	 */
	private int collectNodeType;

	public int getLadderize() {
		return ladderize;
	}

	public void setLadderize(int ladderize) {
		this.ladderize = ladderize;
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

	public void setCollectNodeType(int int1) {
		collectNodeType = int1;
		
	}
	
	public int getCollectNodeType() {
		return collectNodeType;
	}
	
	
}
