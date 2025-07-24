package module.webmsaoperator.webIO;

import java.util.ArrayList;

/**
 * 
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName: GenomicRegionTranscriptInfo.java
 * 
 * @Package: egps.core.seqDistance.io
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-02-20 17:30
 * 
 */
public class GenomicRegionTranscriptInfo {

	private ArrayList<GenomicRegionExonInfo> transcriptInfo;

	public GenomicRegionTranscriptInfo(ArrayList<GenomicRegionExonInfo> transcriptInfo) {
		this.transcriptInfo = transcriptInfo;
	}

	public ArrayList<GenomicRegionExonInfo> getTranscriptInfo() {
		return transcriptInfo;
	}

	public void setTranscriptInfo(ArrayList<GenomicRegionExonInfo> transcriptInfo) {
		this.transcriptInfo = transcriptInfo;
	}

}
