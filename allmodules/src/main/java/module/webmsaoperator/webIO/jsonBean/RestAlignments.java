package module.webmsaoperator.webIO.jsonBean;

/**
 * 
 * @author mhl
 * 
 * @Date Created on: 2018-07-21 13:03
 * 
 */
public class RestAlignments {

	private int strand;
	private String seq_region;
	private String species;
	private String description;
	private String seq;
	private int end;
	private int start;

	public int getStrand() {
		return strand;
	}

	public void setStrand(int strand) {
		this.strand = strand;
	}

	public String getSeq_region() {
		return seq_region;
	}

	public void setSeq_region(String seq_region) {
		this.seq_region = seq_region;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
