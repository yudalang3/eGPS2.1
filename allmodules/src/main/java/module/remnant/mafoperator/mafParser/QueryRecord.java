package module.remnant.mafoperator.mafParser;

import java.util.List;

public class QueryRecord {
	
	String chr;
	// 0 - based
	int startPos;
	
	int length;
	private char strand;
	
	public String getChr() {
		return chr;
	}
	public void setChr(String chr) {
		this.chr = chr;
	}
	public int getStartPos() {
		return startPos;
	}
	public void setStartPos(int pos) {
		this.startPos = pos;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chr == null) ? 0 : chr.hashCode());
		result = prime * result + startPos;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryRecord other = (QueryRecord) obj;
		if (chr == null) {
			if (other.chr != null)
				return false;
		} else if (!chr.equals(other.chr))
			return false;
		if (startPos != other.startPos)
			return false;
		return true;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	protected QueryRecord clone()  {
		QueryRecord newObj = new QueryRecord();
		newObj.setChr(this.chr);
		newObj.setStartPos(this.startPos);
		newObj.setLength(this.length);
		newObj.setStrand(this.strand);
		return newObj;
	}
	public void setStrand(char c) {
		this.strand = c;
	}
	
	public char getStrand() {
		return strand;
	}
}


class ResultRecord{
	String queryChr;
	// 0 based
	int queryPos;
	String targetChr;
	// 0 based
	int targetPos;
	
	QueryRecord queryRecord;
	List<MafLine> mafLines;
	private String matchedChars;

	public String getQueryChr() {
		return queryChr;
	}

	public void setQueryChr(String queryChr) {
		this.queryChr = queryChr;
	}

	public int getQueryPos() {
		return queryPos;
	}

	public void setQueryPos(int queryPos) {
		this.queryPos = queryPos;
	}

	public String getTargetChr() {
		return targetChr;
	}

	public void setTargetChr(String targetChr) {
		this.targetChr = targetChr;
	}

	public int getTargetPos() {
		return targetPos;
	}

	public void setTargetPos(int targetPos) {
		this.targetPos = targetPos;
	}

	public List<MafLine> getMafLines() {
		return mafLines;
	}

	public void setMafLines(List<MafLine> mafLines) {
		this.mafLines = mafLines;
	}
	
	@Override
	public String toString() {
		// to 1 based
		return queryChr+"\t"+(queryPos+1)+"\t"+targetChr+"\t"+(targetPos + 1) +"\t"+matchedChars;
	}

	public void setMatchedChars(String ss) {
		this.matchedChars = ss;
		
	}
	
	public String getMatchedChars() {
		return matchedChars;
	}
	
	public void setQueryRecord(QueryRecord queryRecord) {
		this.queryRecord = queryRecord;
	}
	public QueryRecord getQueryRecord() {
		return queryRecord;
	}
}
