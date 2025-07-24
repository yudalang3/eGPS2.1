package module.evolknow;

import java.awt.Color;

public class TimeScaleEntity {

	private String name;
	private String chineseName;
	/**
	 * million years ago
	 */
	private float startMya = 0;
	private float endMya = 0;
	
	private Color color = Color.magenta;
	
	
	TimeScaleEntity(String name, String chineseName, float startMya, float endMya) {
		super();
		this.name = name;
		this.chineseName = chineseName;
		this.startMya = startMya;
		this.endMya = endMya;
	}
	
	
	
	public TimeScaleEntity(String name, String chineseName, float startMya, float endMya, Color color) {
		super();
		this.name = name;
		this.chineseName = chineseName;
		this.startMya = startMya;
		this.endMya = endMya;
		this.color = color;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public float getStartMya() {
		return startMya;
	}

	public void setStartMya(float startMya) {
		this.startMya = startMya;
	}

	public float getEndMya() {
		return endMya;
	}

	public void setEndMya(float endMya) {
		this.endMya = endMya;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	
	
	
	
}
