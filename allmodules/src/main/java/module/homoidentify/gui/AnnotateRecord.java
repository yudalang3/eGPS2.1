package module.homoidentify.gui;

import java.util.StringJoiner;

public class AnnotateRecord {

	private String geneName;
	private String pepName;
	private String queryName;
	private String scientificName;
	private String commonName;
	private String assemblyName;

	@Override
	public String toString() {
		StringJoiner sJoiner = new StringJoiner("\t");
		sJoiner.add(queryName);
		sJoiner.add(geneName);
		sJoiner.add(pepName);
		sJoiner.add(assemblyName);
		sJoiner.add(scientificName);
		if (commonName != null) {
			sJoiner.add(commonName);
		}
		return sJoiner.toString();
	}

	public String getHeader() {
		if (commonName == null) {
			return "queryName\tgeneName\tpepName\tassemblyName\tscientificName";
		}

		return "queryName\tgeneName\tpepName\tassemblyName\tscientificName\tcommonName";
	}

	public String getGeneName() {
		return geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	public String getPepName() {
		return pepName;
	}

	public void setPepName(String pepName) {
		this.pepName = pepName;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getAssemblyName() {
		return assemblyName;
	}

	public void setAssemblyName(String assemblyName) {
		this.assemblyName = assemblyName;
	}

}
