package module.evolview.gfamily.work.beans;

import java.awt.Color;

public class SequenceElementInfo {

	/** */
	String geneName;
	/** Start position*/
	int startPos;
	/** End position*/
	int endPos;
	
	Color color;

	public String getGeneName() {
		return geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return geneName +"\t"+startPos+"\t"+endPos+"\t"+color+"\n";
	}
}
