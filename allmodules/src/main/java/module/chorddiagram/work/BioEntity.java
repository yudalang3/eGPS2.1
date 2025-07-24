package module.chorddiagram.work;

import java.awt.Color;

public class BioEntity {
    private String name;
    private int length;
    private Color color;

	private double avaliableAngle;
	private double startAngle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

	public double getAvaliableAngle() {
		return avaliableAngle;
	}

	public void setAvaliableAngle(double avaliableAngle) {
		this.avaliableAngle = avaliableAngle;
	}

	public double getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}

}
