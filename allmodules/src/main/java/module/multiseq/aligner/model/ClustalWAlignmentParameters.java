package module.multiseq.aligner.model;

import java.io.File;

public class ClustalWAlignmentParameters {

	private int pairWise_gapOpenPenalty;
	private int pairWise_gapExtensionPenalty;
	
	private int multiAlign_gapOpenPenalty;
	private int multiAlign_gapExtensionPenalty;
	
	boolean ifKeepOriginalOrder;
	
	File treeLocation;

	public final int getPairWise_gapOpenPenalty() {
		return pairWise_gapOpenPenalty;
	}

	public final void setPairWise_gapOpenPenalty(int pairWise_gapOpenPenalty) {
		this.pairWise_gapOpenPenalty = pairWise_gapOpenPenalty;
	}

	public final int getPairWise_gapExtensionPenalty() {
		return pairWise_gapExtensionPenalty;
	}

	public final void setPairWise_gapExtensionPenalty(int pairWise_gapExtensionPenalty) {
		this.pairWise_gapExtensionPenalty = pairWise_gapExtensionPenalty;
	}

	public final int getMultiAlign_gapOpenPenalty() {
		return multiAlign_gapOpenPenalty;
	}

	public final void setMultiAlign_gapOpenPenalty(int multiAlign_gapOpenPenalty) {
		this.multiAlign_gapOpenPenalty = multiAlign_gapOpenPenalty;
	}

	public final int getMultiAlign_gapExtensionPenalty() {
		return multiAlign_gapExtensionPenalty;
	}

	public final void setMultiAlign_gapExtensionPenalty(int multiAlign_gapExtensionPenalty) {
		this.multiAlign_gapExtensionPenalty = multiAlign_gapExtensionPenalty;
	}


	public final boolean isIfKeepOriginalOrder() {
		return ifKeepOriginalOrder;
	}

	public final void setIfKeepOriginalOrder(boolean ifKeepOriginalOrder) {
		this.ifKeepOriginalOrder = ifKeepOriginalOrder;
	}

	public final File getTreeLocation() {
		return treeLocation;
	}

	public final void setTreeLocation(File treeLocation) {
		this.treeLocation = treeLocation;
	}
}
