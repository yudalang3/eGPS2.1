package rest.interpro.entrytaxon;

import java.util.List;

public class Entries {
	
	String accession;
	
	int protein_length;
	
	String source_database;
	
	String entry_type;
	
	String entry_integrated;
	
	List<Entry_protein_locations> entry_protein_locations;

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public int getProtein_length() {
		return protein_length;
	}

	public void setProtein_length(int protein_length) {
		this.protein_length = protein_length;
	}

	public String getSource_database() {
		return source_database;
	}

	public void setSource_database(String source_database) {
		this.source_database = source_database;
	}

	public String getEntry_type() {
		return entry_type;
	}

	public void setEntry_type(String entry_type) {
		this.entry_type = entry_type;
	}

	public String getEntry_integrated() {
		return entry_integrated;
	}

	public void setEntry_integrated(String entry_integrated) {
		this.entry_integrated = entry_integrated;
	}

	public List<Entry_protein_locations> getEntry_protein_locations() {
		return entry_protein_locations;
	}

	public void setEntry_protein_locations(List<Entry_protein_locations> entry_protein_locations) {
		this.entry_protein_locations = entry_protein_locations;
	}
	
	

}
