package module.evolview.gfamily.work.gui.browser;

import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;


public class GeneDrawingLengthCursor {
    private int drawStart;
    private int drawEnd;
    private int minValue;
    private int maxValue;
    private boolean reCalculator;
    /**
     * 突变频率的track中选中的位置，如果没有选过则为0
     */
    private int selectPostion;
    private double charWidth;
    private int charHeight;
    private Font font;

    /**
     * 目前鼠标所在位置对应的基因组position
     */
    private int mousePos;
    
    
    public GeneDrawingLengthCursor(Font font) {
    	setFont(font);
    }

	public int getMousePos() {
		return mousePos;
	}

	public void setMousePos(int mousePos) {
		this.mousePos = mousePos;
	}

	private void setFont(Font font) {
        this.font = font;
        Container c = new Container();
        FontMetrics fm = c.getFontMetrics(font);
        double ww = fm.charWidth('W');
        setCharHeight(fm.getAscent());
        setCharWidth(ww);
    }

	public int getDrawStart() {
		return drawStart;
	}

	public void setDrawStart(int drawStart) {
		this.drawStart = drawStart;
	}

	public int getDrawEnd() {
		return drawEnd;
	}

	public void setDrawEnd(int drawEnd) {
		this.drawEnd = drawEnd;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isReCalculator() {
		return reCalculator;
	}

	public void setReCalculator(boolean reCalculator) {
		this.reCalculator = reCalculator;
	}

	public int getSelectPostion() {
		return selectPostion;
	}

	public void setSelectPostion(int selectPostion) {
		this.selectPostion = selectPostion;
	}

	public double getCharWidth() {
		return charWidth;
	}

	public void setCharWidth(double charWidth) {
		this.charWidth = charWidth;
	}

	public int getCharHeight() {
		return charHeight;
	}

	public void setCharHeight(int charHeight) {
		this.charHeight = charHeight;
	}

	public Font getFont() {
		return font;
	}

}
