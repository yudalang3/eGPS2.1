package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public class SidewardNodeAnnotation {
	// For line property
	private BasicStroke lineStroke;
	private Color lineColor;
	// For text property
	private String textString;
	private Color textColor;
	private Font textFont;
	private boolean isDrawTextHorizontal;

	public SidewardNodeAnnotation() {
	}
	public BasicStroke getLineStroke() {
		return lineStroke;
	}

	public void setLineStroke(BasicStroke lineStroke) {
		this.lineStroke = lineStroke;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public String getTextString() {
		return textString;
	}

	public void setTextString(String textString) {
		this.textString = textString;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Font getTextFont() {
		return textFont;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
	}

	public boolean isDrawTextHorizontal() {
		return isDrawTextHorizontal;
	}

	public void setDrawTextHorizontal(boolean isDrawTextHorizontal) {
		this.isDrawTextHorizontal = isDrawTextHorizontal;
	}

}
