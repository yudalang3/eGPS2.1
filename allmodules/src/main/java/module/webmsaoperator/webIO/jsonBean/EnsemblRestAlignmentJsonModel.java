package module.webmsaoperator.webIO.jsonBean;

import java.util.List;

/**
 * 
 * @author mhl
 * @Date Created on: 2018-07-21 13:03
 * 
 */
public class EnsemblRestAlignmentJsonModel {
	
	private String tree;

	private List<RestAlignments> alignments;

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

	public List<RestAlignments> getAlignments() {
		return alignments;
	}

	public void setAlignments(List<RestAlignments> alignments) {
		this.alignments = alignments;
	}

}
