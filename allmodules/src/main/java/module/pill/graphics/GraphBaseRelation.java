package module.pill.graphics;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * https://www.kegg.jp/kegg/xml/docs/
 */
public class GraphBaseRelation {

	private int entry1;
	private int entry2;

	private List<RelationSubtype> subTypes = Lists.newArrayList();

	public int getEntry1() {
		return entry1;
	}

	public void setEntry1(int entry1) {
		this.entry1 = entry1;
	}

	public int getEntry2() {
		return entry2;
	}

	public void setEntry2(int entry2) {
		this.entry2 = entry2;
	}

	public List<RelationSubtype> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(List<RelationSubtype> subTypes) {
		this.subTypes = subTypes;
	}

}


