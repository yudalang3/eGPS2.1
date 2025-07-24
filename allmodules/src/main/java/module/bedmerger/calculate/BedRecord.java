package module.bedmerger.calculate;

import module.genome.GenomicInterval;

public class BedRecord extends GenomicInterval {
	
	public BedRecord() {
		
	}
	public BedRecord(String chrom, int start, int end) {
		super(chrom, start, end);
	}

}
