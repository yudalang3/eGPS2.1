package module.evolview.gfamily.work.calculator.similarityplot;

import java.util.List;

public class SimPlotJsonData {

	List<ComparedSequenceResultJson> comparedSequenceResults;
	List<Integer> xAxisIntegers;
	/**
	 * @return the xAxisIntegers
	 */
	public List<Integer> getxAxisIntegers() {
		return xAxisIntegers;
	}
	/**
	 * @param xAxisIntegers the xAxisIntegers to set
	 */
	public void setxAxisIntegers(List<Integer> xAxisIntegers) {
		this.xAxisIntegers = xAxisIntegers;
	}
	/**
	 * @return the comparedSequenceResults
	 */
	public List<ComparedSequenceResultJson> getComparedSequenceResults() {
		return comparedSequenceResults;
	}
	/**
	 * @param comparedSequenceResults the comparedSequenceResults to set
	 */
	public void setComparedSequenceResults(List<ComparedSequenceResultJson> comparedSequenceResults) {
		this.comparedSequenceResults = comparedSequenceResults;
	}
	
	
	
}

