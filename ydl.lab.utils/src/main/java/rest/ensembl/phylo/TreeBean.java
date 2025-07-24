package rest.ensembl.phylo;

import java.util.List;

/**
 * 外节点还有更多属性，这里没有实现。等有需要的时候再说
 * 外节点没有  timetree_mya 属性
 * @author yudal
 *
 */
public class TreeBean {

	EventsBean events;
	float branch_length;
	
	List<TreeBean> children;
	
	TaxonomyBean taxonomy;
	
	ConfidenceBean confidence;

	public EventsBean getEvents() {
		return events;
	}

	public void setEvents(EventsBean events) {
		this.events = events;
	}

	public float getBranch_length() {
		return branch_length;
	}

	public void setBranch_length(float branch_length) {
		this.branch_length = branch_length;
	}

	public List<TreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<TreeBean> children) {
		this.children = children;
	}

	public TaxonomyBean getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(TaxonomyBean taxonomy) {
		this.taxonomy = taxonomy;
	}

	public ConfidenceBean getConfidence() {
		return confidence;
	}

	public void setConfidence(ConfidenceBean confidence) {
		this.confidence = confidence;
	}
	
	
	
}
