package module.multiseqview.model;

import java.util.List;

public class Data4SequenceStructureView {
	
	List<Sequence4import> sequences;

	/**
	 * @return the sequences
	 */
	public List<Sequence4import> getSequences() {
		return sequences;
	}

	/**
	 * @param sequences the sequences to set
	 */
	public void setSequences(List<Sequence4import> sequences) {
		this.sequences = sequences;
	}
	
	
	@Override
	public String toString() {
		return sequences.toString();
	}

}
