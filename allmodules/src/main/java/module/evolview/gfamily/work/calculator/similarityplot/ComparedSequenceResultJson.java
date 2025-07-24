package module.evolview.gfamily.work.calculator.similarityplot;

import java.util.List;

public class ComparedSequenceResultJson{
	String name;
	
	List<Double> yAxis;

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
	
	
}