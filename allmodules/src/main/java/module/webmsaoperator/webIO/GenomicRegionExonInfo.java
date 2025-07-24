package module.webmsaoperator.webIO;

/**
 * 
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName: GenomicRegionExonInfo.java
 * 
 * @Package: egps.core.seqDistance.io
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-02-20 17:35
 * 
 */
public class GenomicRegionExonInfo {

	private String start;
	private String end;

	public GenomicRegionExonInfo(String start, String end) {
		super();
		this.start = start;
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
