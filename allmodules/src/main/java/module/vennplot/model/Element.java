package module.vennplot.model;

public class Element {
	
	String rawStr;
	boolean ifGenomicRegion = false;
	int start;
	int end;
	
	
	public Element(String rawStr) {
		this.rawStr = rawStr;
	}
	public String getRawStr() {
		return rawStr;
	}
	public void setRawStr(String rawStr) {
		this.rawStr = rawStr;
	}
	public boolean isIfGenomicRegion() {
		return ifGenomicRegion;
	}
	public void setIfGenomicRegion(boolean ifGenomicRegion) {
		this.ifGenomicRegion = ifGenomicRegion;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Element) {
			Element other = (Element) obj;
			if (ifGenomicRegion) {
				if (rawStr.equals(other.getRawStr())) {
					return judgeOverlap(this.start,this.end,other.start,other.end);
				}
			}else {
				return rawStr.equals(other.getRawStr());
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return rawStr.hashCode() + start + end;
	}
	
	
	private boolean judgeOverlap(int s1, int e1, int s2, int e2) {
		if (s1 > s2) {
			return e1 > s2;
		}else if (s1 < s2) {
			return e2 > s1;
		}else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		if (ifGenomicRegion) {
			return rawStr+","+start+","+end;
		}else {
			return rawStr;
		}
	}

}
