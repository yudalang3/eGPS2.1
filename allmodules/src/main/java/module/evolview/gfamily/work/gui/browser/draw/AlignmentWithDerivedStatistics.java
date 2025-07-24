package module.evolview.gfamily.work.gui.browser.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import module.evolview.gfamily.work.calculator.similarityplot.ComparedSequenceResult;
import module.evolview.gfamily.work.calculator.similarityplot.SimilarityPlotData;
import module.evolview.gfamily.work.model.AlignmentGetSequence;

/**
 * Derived statistics
 * 
 * GeneCalculatedDerivedStatistics
 * @author yudal
 *
 */
public class AlignmentWithDerivedStatistics {

	private AlignmentGetSequence sequence;

	private SimilarityPlotData simPlotData;


	public SimilarityPlotData subGenomeDataModeltPolylinePlot(int drawStart, int drawEnd) {
		if (drawStart > drawEnd) {
			int temp = drawStart;
			drawStart = drawEnd;
			drawEnd = temp;
		}

		SimilarityPlotData newSimPlotData = new SimilarityPlotData();

		List<ComparedSequenceResult> comparedSequenceResults = simPlotData.getComparedSequenceResults();
		// 存储新的ComparedSequenceResults数据
		List<ComparedSequenceResult> newComparedSequenceResults = new ArrayList<>();

		List<Integer> xAxis = simPlotData.getxAxisIntegers();
		// 存储截取xAxis后的数据
		List<Integer> newXAxis = new ArrayList<>();
		// 存储截取xAxis的坐标
		List<Integer> indexs = new ArrayList<>();

		int firstElementIndexBeforeDrawStart = Integer.MIN_VALUE;
		int nextElementIndexAfterDrawEnd = Integer.MAX_VALUE;
		int originalPointsSize = xAxis.size();
		for (int i = 0; i < originalPointsSize; i++) {
			Integer integer = xAxis.get(i);
			if (integer < drawStart) {
				firstElementIndexBeforeDrawStart = i;
			}
			if (integer < drawStart || integer > drawEnd) {
				continue;
			}
			newXAxis.add(integer);
			indexs.add(i);
		}
		for (int i = originalPointsSize - 1; i > -1; i--) {
			Integer integer = xAxis.get(i);
			if (integer > drawEnd) {
				nextElementIndexAfterDrawEnd = i;
			} else {
				break;
			}
		}

		int newSize = indexs.size();

		for (ComparedSequenceResult comparedSequenceResult : comparedSequenceResults) {
			// 存储截取xAxis后的数据
			List<Double> newYAxis = new ArrayList<>();
			List<Double> doubles = comparedSequenceResult.getyAxis();
			for (int i = 0; i < newSize; i++) {
				Integer integer = indexs.get(i);
				newYAxis.add(doubles.get(integer));
			}
			ComparedSequenceResult comparedSequenceResult1 = new ComparedSequenceResult();
			comparedSequenceResult1.setColor(comparedSequenceResult.getColor());
			comparedSequenceResult1.setName(comparedSequenceResult.getName());
			comparedSequenceResult1.setyAxis(newYAxis);
			newComparedSequenceResults.add(comparedSequenceResult1);
		}

		int comparedSeqSize = newComparedSequenceResults.size();
		// 赋予 Previous与 Next 横纵坐标
		if (firstElementIndexBeforeDrawStart > -1) {
			Integer integer = xAxis.get(firstElementIndexBeforeDrawStart);
			newSimPlotData.setPreviousPoint(integer);

			for (int i = 0; i < comparedSeqSize; i++) {
				double previousY = comparedSequenceResults.get(i).getyAxis().get(firstElementIndexBeforeDrawStart);
				newComparedSequenceResults.get(i).setPreviousY(previousY);
			}

		}
		if (nextElementIndexAfterDrawEnd < originalPointsSize) {
			Integer integer = xAxis.get(nextElementIndexAfterDrawEnd);
			newSimPlotData.setNextPoint(integer);
			for (int i = 0; i < comparedSeqSize; i++) {
				double nextY = comparedSequenceResults.get(i).getyAxis().get(nextElementIndexAfterDrawEnd);
				newComparedSequenceResults.get(i).setNextY(nextY);
			}
		}

		int leftMostXLocation = drawStart + 1;
		int rightMostXLocation = drawEnd;
		Optional<Integer> previousXLocation = newSimPlotData.getPreviousXLocation();
		if (previousXLocation.isPresent() && newSize > 0) {
			Integer prevX = previousXLocation.get();

			for (int i = 0; i < comparedSeqSize; i++) {
				ComparedSequenceResult comparedSequenceResult = newComparedSequenceResults.get(i);
				double previousY = comparedSequenceResult.getPreviousY();

				Double firstYAxis = comparedSequenceResult.getyAxis().get(0);
				double middleYLocation = getMiddleYLocation(prevX, previousY, newXAxis.get(0), firstYAxis,
						leftMostXLocation);

				comparedSequenceResult.getyAxis().add(0, middleYLocation);
			}

			newXAxis.add(0, leftMostXLocation);
		}

		Optional<Integer> nextXLocation = newSimPlotData.getNextXLocation();
		if (nextXLocation.isPresent() && newSize > 0) {
			Integer nextX = nextXLocation.get();
			for (int i = 0; i < comparedSeqSize; i++) {
				ComparedSequenceResult comparedSequenceResult = newComparedSequenceResults.get(i);
				double nextY = comparedSequenceResult.getNextY();

				Double lastYAxis = comparedSequenceResult.getyAxis().get(newSize - 1);
				double middleYLocation = getMiddleYLocation(newXAxis.get(newSize - 1), lastYAxis, nextX, nextY,
						rightMostXLocation);
				comparedSequenceResult.getyAxis().add(middleYLocation);
			}
			newXAxis.add(rightMostXLocation);
		}

		if (newSize == 0) {
			if (previousXLocation.isPresent() && nextXLocation.isPresent()) {
				Integer prevX = previousXLocation.get();
				Integer nextX = nextXLocation.get();
				newXAxis.add(leftMostXLocation);
				newXAxis.add(rightMostXLocation);
				for (int i = 0; i < comparedSeqSize; i++) {
					ComparedSequenceResult comparedSequenceResult = newComparedSequenceResults.get(i);
					double previousY = comparedSequenceResult.getPreviousY();
					double nextY = comparedSequenceResult.getNextY();

					double middleYLocation = getMiddleYLocation(prevX, previousY, nextX, nextY, leftMostXLocation);
					comparedSequenceResult.getyAxis().add(middleYLocation);
					middleYLocation = getMiddleYLocation(prevX, previousY, nextX, nextY, rightMostXLocation);
					comparedSequenceResult.getyAxis().add(middleYLocation);
				}
			}
		}

//		System.out.println("firstElementIndexBeforeDrawStart  " + firstElementIndexBeforeDrawStart
//				+ "\tnextElementIndexAfterDrawEnd  " + nextElementIndexAfterDrawEnd);
//		System.out.println("new xAxis values\t" + newXAxis);
//		System.out.println("Last xAxis  "+xAxis.subList(originalPointsSize -2, originalPointsSize));

		newSimPlotData.setComparedSequenceResults(newComparedSequenceResults);
		newSimPlotData.setxAxisIntegers(newXAxis);
		return newSimPlotData;
	}

	private double getMiddleYLocation(double x1, double y1, double x2, double y2, double x) {
		double ret = 0;

		if (y1 == y2) {
			ret = y1;
		} else {
			ret = y2;
			ret -= ((y2 - y1) * (x2 - x) / (x2 - x1));
		}

		return ret;
	}

	public void setSequence(AlignmentGetSequence sequence) {
		this.sequence = sequence;
	}

	public AlignmentGetSequence getSequence() {
		return sequence;
	}

	public void setSimPlotData(SimilarityPlotData simPlotData) {
		this.simPlotData = simPlotData;
	}

	public SimilarityPlotData getSimPlotData() {
		return simPlotData;
	}
}
