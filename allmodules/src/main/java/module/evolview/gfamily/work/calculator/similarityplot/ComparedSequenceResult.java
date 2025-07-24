package module.evolview.gfamily.work.calculator.similarityplot;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

public class ComparedSequenceResult {

	
	String name;
	
	List<Double> yAxis;
	
	Color color;
	
	double previousY;
	
	double nextY;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the yAxis
	 */
	public List<Double> getyAxis() {
		return yAxis;
	}

	/**
	 * @param yAxis the yAxis to set
	 */
	public void setyAxis(List<Double> yAxis) {
		this.yAxis = yAxis;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, yAxis);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ComparedSequenceResult)) {
			return false;
		}
		ComparedSequenceResult other = (ComparedSequenceResult) obj;
//		System.out.println("yAxis:");
//		System.out.println(yAxis);
//		System.out.println(other.yAxis);
//		System.out.println("name:");
//		System.out.println(name);
//		System.out.println(other.name);
		return Objects.equals(name, other.name) && Objects.equals(yAxis, other.yAxis);
	}

	public double getPreviousY() {
		return previousY;
	}

	public void setPreviousY(double previousY) {
		this.previousY = previousY;
	}

	public double getNextY() {
		return nextY;
	}

	public void setNextY(double nextY) {
		this.nextY = nextY;
	}
	
	
	
	
}
