package module.localblast.gui.seqfilter;

public class BasicModeParameter {
	
	String dbParameterString;
	String dbtypeParameterString;
	String entryParameterString;
	String rangeParameterString;
	String strandParameterString;
	String entry_batchParameterString;
	String outParameterString;
	String outfmtParameterString;
	
	
	public void setDbParameterString(String dbParameterString) {
		this.dbParameterString = dbParameterString;
	}

	public void setDbtypeParameterString(String dbtypeParameterString) {
		this.dbtypeParameterString = dbtypeParameterString;
	}

	public void setEntryParameterString(String entryParameterString) {
		this.entryParameterString = entryParameterString;
	}

	public void setRangeParameterString(String rangeParameterString) {
		this.rangeParameterString = rangeParameterString;
	}

	public void setStrandParameterString(String strandParameterString) {
		this.strandParameterString = strandParameterString;
	}

	public void setEntry_batchParameterString(String entry_batchParameterString) {
		this.entry_batchParameterString = entry_batchParameterString;
	}

	public void setOutParameterString(String outParameterString) {
		this.outParameterString = outParameterString;
	}

	public void setOutfmtParameterString(String outfmtParameterString) {
		this.outfmtParameterString = outfmtParameterString;
	}

	public String getRunProgrameCommand() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("input/file/path/config/software/blast/blastdbcmd.exe");
		sBuilder.append(" -db ").append(dbParameterString);
		sBuilder.append(" -dbtype ").append(dbtypeParameterString);
		if (entryParameterString.length()> 0) {
			sBuilder.append(" -entry ").append(entryParameterString);
		}
		if (rangeParameterString.length() > 0) {
			sBuilder.append(" -range ").append(rangeParameterString);
		}
		if (strandParameterString.length()>0) {
			sBuilder.append(" -strand ").append(strandParameterString);
		}
		if (entry_batchParameterString.length() > 0) {
			sBuilder.append(" -entry_batch ").append(entry_batchParameterString);
		}
		sBuilder.append(" -out ").append(outParameterString);
		sBuilder.append(" -outfmt ").append(outfmtParameterString);
		
		return sBuilder.toString();
	}
	
	public String getOutParameterString() {
		return outParameterString;
	}
}
