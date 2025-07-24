package module.localblast.gui.dbtools;

public class BasicModeParameter {

	final String[] parameterNames = {
			"in",
			"dbtype",
			"input_type",
			"title",
			"parse_seqids",
			"hash_index",
			"out",
	};
	
	String inParameterString;
	String input_typeParameterString;
	String titleParameterString;
	String outParameterString;
	String dbtypeParameterString;
	boolean ifParse_seqids;
	boolean ifHash_index;
	
	
	
	public void setInParameterString(String inParameterString) {
		this.inParameterString = inParameterString;
	}



	public void setInput_typeParameterString(String input_typeParameterString) {
		this.input_typeParameterString = input_typeParameterString;
	}



	public void setTitleParameterString(String titleParameterString) {
		this.titleParameterString = titleParameterString;
	}



	public void setOutParameterString(String outParameterString) {
		this.outParameterString = outParameterString;
	}
	
	public void setDbtypeParameterString(String dbtypeParameterString) {
		this.dbtypeParameterString = dbtypeParameterString;
	}



	public void setIfParse_seqids(boolean ifParse_seqids) {
		this.ifParse_seqids = ifParse_seqids;
	}


	public void setIfHash_index(boolean ifHash_index) {
		this.ifHash_index = ifHash_index;
	}



	public String[] getParameterNames() {
		return parameterNames;
	}
	
	public String getRunProgrameCommand(String exePath) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(exePath);
		sBuilder.append(" -in ").append(inParameterString);
		sBuilder.append(" -dbtype ").append(dbtypeParameterString);
		sBuilder.append(" -input_type ").append(input_typeParameterString);
		
		if (titleParameterString.length() > 0) {
			sBuilder.append(" -title ").append(titleParameterString);
		}
		
		if (ifParse_seqids) {
			sBuilder.append(" -parse_seqids");
		}
		
		if (ifHash_index) {
			sBuilder.append(" -hash_index");
		}
		
		sBuilder.append(" -out ").append(outParameterString);
		
		return sBuilder.toString();
	}
	
	public String getOutParameterString() {
		return outParameterString;
	}
}
