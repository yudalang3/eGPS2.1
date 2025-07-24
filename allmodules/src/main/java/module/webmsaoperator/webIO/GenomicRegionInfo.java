package module.webmsaoperator.webIO;

import module.webmsaoperator.webIO.gene.Gene;

/**
 * 
 * 
 * @ClassName: GenomicRegionInfo.java
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
public class GenomicRegionInfo {

	private String chrom;
	private String assembly;
	private String strand;
	private String start;
	private String end;
	private Gene genomicRegionInfor;

	public GenomicRegionInfo(String chrom, String assembly, String strand, String start, String end,
			Gene genomicRegionTranscriptInfo) {
		this.chrom = chrom;
		this.assembly = assembly;
		this.strand = strand;
		this.start = start;
		this.end = end;
		this.genomicRegionInfor = genomicRegionTranscriptInfo;
	}

	public String getRegionStr() {
		return new StringBuffer().append(chrom).append(":").append(start).append("-").append(end).append(":")
				.append(strand).toString();
	}

	public String getChrom() {
		return chrom;
	}

	public void setChrom(String chrom) {
		this.chrom = chrom;
	}

	public String getAssembly() {
		return assembly;
	}

	public void setAssembly(String assembly) {
		this.assembly = assembly;
	}

	public String getStrand() {
		return strand;
	}

	public void setStrand(String strand) {
		this.strand = strand;
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

	public Gene getGeneStructureInfo() {
		return genomicRegionInfor;
	}


}
