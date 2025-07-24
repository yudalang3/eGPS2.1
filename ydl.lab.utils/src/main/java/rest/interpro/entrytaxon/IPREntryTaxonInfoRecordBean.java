package rest.interpro.entrytaxon;

import java.util.List;

public class IPREntryTaxonInfoRecordBean {

	Metadata metadata;
	
	List<Entries> entries;
	
	Extra_fields extra_fields;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public List<Entries> getEntries() {
		return entries;
	}

	public void setEntries(List<Entries> entries) {
		this.entries = entries;
	}

	public Extra_fields getExtra_fields() {
		return extra_fields;
	}

	public void setExtra_fields(Extra_fields extra_fields) {
		this.extra_fields = extra_fields;
	}
	
	
}
