package module.evolview.gfamily.work.calculator.similarityplot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SimilarityPlotData {

    List<ComparedSequenceResult> comparedSequenceResults;

    List<Double> xAxis;

    List<Integer> xAxisIntegers;
    
    Optional<Integer> previousXLocation = Optional.empty();
    Optional<Integer> nextXLocation = Optional.empty();

    /**
     * @return the comparedSequenceResults
     */
    public List<ComparedSequenceResult> getComparedSequenceResults() {
        return comparedSequenceResults;
    }

    /**
     * @param comparedSequenceResults the comparedSequenceResults to set
     */
    public void setComparedSequenceResults(List<ComparedSequenceResult> comparedSequenceResults) {
        this.comparedSequenceResults = comparedSequenceResults;
    }

    /**
     * @return the xAxis
     */
    public List<Double> getxAxis() {
        return xAxis;
    }

    /**
     * @param xAxis the xAxis to set
     */
    public void setxAxis(List<Double> xAxis) {
        this.xAxis = xAxis;
    }

    /**
     * @return the xAxisIntegers
     */
    public List<Integer> getxAxisIntegers() {

        if (xAxisIntegers == null) {
            xAxisIntegers = new ArrayList<Integer>();
            for (Double dd : xAxis) {

                xAxisIntegers.add(dd.intValue());
            }
        }

        return xAxisIntegers;
    }

    /**
     * @param xAxisIntegers the xAxisIntegers to set
     */
    public void setxAxisIntegers(List<Integer> xAxisIntegers) {
        this.xAxisIntegers = xAxisIntegers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comparedSequenceResults, xAxis);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SimilarityPlotData)) {
            return false;
        }
        SimilarityPlotData other = (SimilarityPlotData) obj;
//		System.out.println("xAxis:");
//		System.out.println(xAxis);
//		System.out.println(other.xAxis);
        return Objects.equals(comparedSequenceResults, other.comparedSequenceResults)
                && Objects.equals(xAxis, other.xAxis);
    }

	public Optional<Integer> getPreviousXLocation() {
		return previousXLocation;
	}

	public void setPreviousPoint(Integer previousPoint) {
		this.previousXLocation = Optional.ofNullable(previousPoint);
	}

	public Optional<Integer> getNextXLocation() {
		return nextXLocation;
	}

	public void setNextPoint(Integer nextPoint) {
		this.nextXLocation = Optional.ofNullable(nextPoint);
	}


    
}
