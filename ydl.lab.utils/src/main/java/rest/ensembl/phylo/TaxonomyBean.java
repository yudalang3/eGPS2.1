package rest.ensembl.phylo;

public class TaxonomyBean {

	String common_name;
	float timetree_mya;
	String scientific_name;
	
	int id;

	public String getCommon_name() {
		return common_name;
	}

	public void setCommon_name(String common_name) {
		this.common_name = common_name;
	}

	public float getTimetree_mya() {
		return timetree_mya;
	}

	public void setTimetree_mya(float timetree_mya) {
		this.timetree_mya = timetree_mya;
	}

	public String getScientific_name() {
		return scientific_name;
	}

	public void setScientific_name(String scientific_name) {
		this.scientific_name = scientific_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
