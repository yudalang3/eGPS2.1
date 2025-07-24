package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * 绘制VirusStrainsInfo
 */
public class DrawingPropertySequenceElement {
    private Rectangle2D.Double rectangle;
    private String geneName;
    private float nameLocationX;
    private float nameLocationY;

    private int startPose;
    private int endPose;
    
    


    // join(266..13468,13468..21555)
    // gene="orf1ab"
    // note="orf1ab; translated by -1 ribosomal frameshift"

    public DrawingPropertySequenceElement() {
		super();
	}


	private Color color;


	public Rectangle2D.Double getRectangle() {
		return rectangle;
	}


	public void setRectangle(Rectangle2D.Double rectangle) {
		this.rectangle = rectangle;
	}


	public String getGeneName() {
		return geneName;
	}


	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}


	public float getNameLocationX() {
		return nameLocationX;
	}


	public void setNameLocationX(float nameLocationX) {
		this.nameLocationX = nameLocationX;
	}


	public float getNameLocationY() {
		return nameLocationY;
	}


	public void setNameLocationY(float nameLocationY) {
		this.nameLocationY = nameLocationY;
	}


	public int getStartPose() {
		return startPose;
	}


	public void setStartPose(int startPose) {
		this.startPose = startPose;
	}


	public int getEndPose() {
		return endPose;
	}


	public void setEndPose(int endPose) {
		this.endPose = endPose;
	}


	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}
    
    
}
