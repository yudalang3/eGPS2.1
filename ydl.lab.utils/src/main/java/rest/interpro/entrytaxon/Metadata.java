package rest.interpro.entrytaxon;

import java.util.List;

public class Metadata {

	String accession;
	String name;
	
	String parent;
	
	String source_database;
	
	List<String> children;

	public Metadata() {

	}

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getSource_database() {
		return source_database;
	}

	public void setSource_database(String source_database) {
		this.source_database = source_database;
	}

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}
	
	
}
