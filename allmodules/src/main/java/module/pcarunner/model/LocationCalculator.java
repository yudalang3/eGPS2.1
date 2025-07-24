package module.pcarunner.model;

import java.awt.geom.Point2D;

import egps2.utils.common.math.Tools;
import egps2.utils.common.model.datatransfer.TwoTuple;

public class LocationCalculator {

	private ParameterModel parameterModel;
	private double newMinValue;
	private double newRangeToRepresent;

	public PaintingLocations calculatePaintingLocations(TwoTuple<double[], double[]> twoDimMatrix, int width, int height) {
		PaintingLocations ret = new PaintingLocations();
		
		double blinkSpaceLength = parameterModel.getBlinkSpaceLength();
		
		double availableWidth = width - blinkSpaceLength - blinkSpaceLength;
		double availableHeight = height - blinkSpaceLength - blinkSpaceLength;
		
		double yForXAxis = height - blinkSpaceLength;
		ret.setOriginCoordinate(new Point2D.Double(blinkSpaceLength, yForXAxis));
		ret.setyMostCoordinate(new Point2D.Double(blinkSpaceLength, blinkSpaceLength));
		ret.setxMostCoordinate(new Point2D.Double(width - blinkSpaceLength, yForXAxis));
		
		double[] first = twoDimMatrix.first;
		// x axis also is the PC1
		ret.setxAxis(configureXAxis(twoDimMatrix.first,availableWidth,blinkSpaceLength));
		int length = first.length;
		
		double[] xLocations = new double[length];
		for (int i = 0; i < length; i++) {
			xLocations[i] = (first[i] - newMinValue) / newRangeToRepresent * availableWidth + blinkSpaceLength;
		}
		
		// y axis also is the PC2
		ret.setyAxis(configureYAxis(twoDimMatrix.second, availableHeight, blinkSpaceLength));
		double[] second = twoDimMatrix.second;
		double[] yLocations = new double[length];
		for (int i = 0; i < length; i++) {
			yLocations[i] = availableHeight + blinkSpaceLength - (second[i] - newMinValue) / newRangeToRepresent * availableHeight;
		}
		
		ret.setTwoDimArraysForPainting(new TwoTuple<double[], double[]>(xLocations, yLocations));
		
		ret.setxForYAxis(blinkSpaceLength);
		ret.setyForXAxis(yForXAxis);
		
		return ret;
	}

	/**
	 * This method will update the newMinValue;
	 * 
	 * @param first
	 * @param availableLen
	 * @param blinkSpaceLength
	 * @return first is the Axis locations; second is the Axis values!
	 */
	private TwoTuple<double[], double[]> configureXAxis(double[] first,double availableLen,double blinkSpaceLength) {
		
		double[] minAndMax = Tools.minAndMax(first);
		double extendingPercentage = parameterModel.getExtendingPercentage();
		double range = minAndMax[1] - minAndMax[0];
		double xLeftBlink = extendingPercentage * range;
		double newRange = range + xLeftBlink + xLeftBlink;

		int numOfTicks = parameterModel.getNumOfTicks();
		double[] xAxisLocations = new double[numOfTicks];
		double[] xAxisValues = new double[numOfTicks];
		for (int i = 0; i < numOfTicks; i++) {
			double per = i / (double) numOfTicks;
			xAxisLocations[i] = per * availableLen + blinkSpaceLength;
			xAxisValues[i] = Tools.formatDoubleKeepTwoDecimal(per * newRange);
		}

		newMinValue = minAndMax[0] - xLeftBlink;
		newRangeToRepresent = newRange;
		
		return new TwoTuple<double[], double[]>(xAxisLocations, xAxisValues);
	}
	/**
	 * This method will update the newMinValue;
	 * 
	 * @param first
	 * @param availableLen
	 * @param blinkSpaceLength
	 * @return first is the Axis locations; second is the Axis values!
	 */
	private TwoTuple<double[], double[]> configureYAxis(double[] first,double availableLen,double blinkSpaceLength) {
		
		double[] minAndMax = Tools.minAndMax(first);
		double extendingPercentage = parameterModel.getExtendingPercentage();
		double range = minAndMax[1] - minAndMax[0];
		double xLeftBlink = extendingPercentage * range;
		double newRange = range + xLeftBlink + xLeftBlink;
		
		int numOfTicks = parameterModel.getNumOfTicks();
		double[] xAxisLocations = new double[numOfTicks];
		double[] xAxisValues = new double[numOfTicks];
		for (int i = 0; i < numOfTicks; i++) {
			double per = i / (double) numOfTicks;
			xAxisLocations[i] = availableLen + blinkSpaceLength - per * availableLen;
			xAxisValues[i] = Tools.formatDoubleKeepTwoDecimal(per * newRange);
		}
		
		newMinValue = minAndMax[0] - xLeftBlink;
		newRangeToRepresent = newRange;
		
		return new TwoTuple<double[], double[]>(xAxisLocations, xAxisValues);
	}

	public void setParameterModel(ParameterModel parameterModel) {
		this.parameterModel = parameterModel;
	}


}
