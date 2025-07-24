package module.remnant.mafoperator.mafParser;

/**
 * 
 * <p>
 * Title: MafLine
 * </p>
 * <p>
 * Description: ElegantJTable MAF line contains the data of one alignment in a MAF block
 * e.g. the ref, start, stop, strand, ref length, aligned sequences, alignment
 * score, ...<br>
 * <br>
 * <code>
 * s gorGor1.Supercontig_0439211 0 184 +   616 GATCACAGGTCTATCACCCT...<br>
 * q gorGor1.Supercontig_0439211               57889999999999999999...<br>
 * i gorGor1.Supercontig_0439211 N 0 I 21<br>
 * </code>
 * 
 * </p>
 * 
 * @author yudalang
 * @date 2018-10-25
 */
public class MafLine {
	
	/**
	 * The name of one of the source sequences for the alignment. 
	 * For sequences that are resident in a browser assembly, the form 'database.chromosome' allows automatic creation of links to other assemblies. 
	 * Non-browser sequences are typically reference by the species name alone.
	 */
    public final static int COL_SRC = 1;
    /*
     * The start of the aligning region in the source sequence. This is a zero-based number. 
     * If the strand field is "-" then this is the start relative to the reverse-complemented source sequence
     */
    public final static int COL_START = 2;
    /**
     * The size of the aligning region in the source sequence. 
     * This number is equal to the number of non-dash characters in the alignment text field below.
     */
    public final static int COL_SIZE = 3;
    /**
     * Either "+" or "-". If "-", then the alignment is to the reverse-complemented source.
     */
    public final static int COL_STRAND = 4;
    /**
     * The size of the entire source sequence, not just the parts involved in the alignment.
     */
    public final static int COL_SRCSIZE = 5;
    /**
     * The nucleotides (or amino acids) in the alignment and any insertions (dashes) as well.
     */
    public final static int COL_TEXT = 6;
    
	private String refSrc;
	private String assembly;

	private int start = -1;
	private int size = -1;

	private int end = -1;
	private char strand;

	private StringBuffer sequence;
	
	public MafLine(String lineInBlock) {
		
		String[] tt = lineInBlock.split("\\s+");
		String refSrc = tt[COL_SRC];
		setRefSrc(refSrc);
		setAssembly(refSrc.split("\\.")[0]);
		setStart(Integer.parseInt( tt[COL_START]) );
		setSize(Integer.parseInt(tt[COL_SIZE]));
		setStrand(tt[COL_STRAND].charAt(0));
		
		setSequence(tt[COL_TEXT]);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	public String getAssembly() {
		return assembly;
	}
	public void setAssembly(String assembly) {
		this.assembly = assembly;
	}
    /**
     * Sets the ref src (eg. mm9.chr1, gorgor1.cjr2)
     * @param ref_assembly the ref_assembly to set
     */
    public void setRefSrc(String ref_assembly) {
        this.refSrc = ref_assembly;
    }
    public String getRefSrc() {
		return refSrc;
	}
    /**
     * Returns the start position in the block.
     * @return the start
     */
    public int getStart() {
        return start;
    }
    /**
     * Sets the start position in the block.
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }
    /**
     * Returns the end position in the block.
     * @return the end
     */
    public int getEnd() {
        return end;
    }
    /**
     * Sets the end position in the block.
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }
    /**
     * Returns the strand of the aligned sequence (e.g. true = +, false = -)
     * @return the strand
     */
    public char getStrand() {
        return strand;
    }
    /**
     * Sets the strand of the aligned sequence (e.g. true = +, false = -)
     * @param strand the strand to set
     */
    public void setStrand(char strand) {
        this.strand = strand;
    }
    /**
     * Returns the sequence of the block.
     * @return the sequence
     */
    public StringBuffer getSeqBuffer() {
        return sequence;
    }
    public String getSequence() {
        return sequence.toString();
    }
    /**
     * Returns the sequence of the block.
     * @param sequence the sequence to set
     */
    public void setSequence(StringBuffer sequence) {
        this.sequence = sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = new StringBuffer(sequence);
    }

    public String toString(boolean verbose) {
		if (verbose) {
			 return ">" +refSrc +
		                ":" + start +
		                ":" + size +
		                "(" +strand+")";
		}else {
			 return ">" +refSrc +
		                ":" + start +
		                ":" + size +
		                "(" +strand+")"+
		                 sequence ;
		}
	}

    /**
     * to fasta format
     */
    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * This will append the data from line2 to the current {@link MafLine}.
     * @param line2
     * @throws IllegalMafArgumentException
     */
    public void append(MafLine line2) throws IllegalMafArgumentException{
        // ref_assembly
		if (!this.refSrc.equals(line2.refSrc))
			throw new IllegalMafArgumentException(this.refSrc + " != " + line2.refSrc);
        // chromosom
//		if(this.chromosom != null && !this.chromosom.equals(line2.chromosom))
//			throw new IllegalMAFarguments(this.chromosom+" != "+line2.chromosom);
        // strand
//		System.out.println(line2);
//		if(this.strand ^ line2.strand){
//			throw new IllegalMAFarguments(this.strand+" != "+line2.strand);
//		}
        // start
		if (this.start == -1 & line2.start != -1)
			this.start = line2.start;
		// end
		if (this.end < line2.end)
			this.end = line2.end;
        // sequence
        this.sequence.append(line2.sequence);

    }

}
