package module.sequencelogo.makesequencelogo;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 
 * @ClassName: CalculateAxisCoordinate
 * @Description: 计算坐标轴的坐标
 * @author zjw
 * @date 2024-04-28 07:59:02
 */
public class CalculateAxisCoordinate {

	/**
	 * 
	 * @MethodName: calculateAbscissaCoordinate
	 * @Description: 计算横坐标轴的的各种坐标
	 * @author zjw
	 * @param seqLength
	 * @param parameter
	 * @return LinkedHashMap<Integer,ArrayList<Double>>
	 * @date 2024-05-07 10:05:59
	 */
	public static LinkedHashMap<Integer, ArrayList<Double>> calculateAbscissaCoordinate(int seqLength,
			MakeSequenceLogoParameter parameter) {

		double initialAbscissa = parameter.getInitialAbscissa();
		double initialOrdinate = parameter.getInitialOrdinate();
		double seqLogoFontWidth = parameter.getSeqLogoFontWidth();

		LinkedHashMap<Integer, ArrayList<Double>> abscissaCoordinates = new LinkedHashMap<Integer, ArrayList<Double>>();

		// x1,y1是横坐标轴的初始x,y坐标。15是横坐标轴与sequence logo的距离
		double x1 = initialAbscissa + seqLogoFontWidth / 2;
		double y1 = initialOrdinate + 15;
		double x2 = initialAbscissa + seqLogoFontWidth / 2;
		double y2 = initialOrdinate + 15 + parameter.tickLength;

		int i = 1;
		while (i <= seqLength) {
			ArrayList<Double> abscissaCoordinate = new ArrayList<Double>();
			abscissaCoordinate.add(x1);
			abscissaCoordinate.add(y1);
			abscissaCoordinate.add(x2);
			abscissaCoordinate.add(y2);
			abscissaCoordinates.put(i, abscissaCoordinate);
			x1 = x1 + seqLogoFontWidth;
			x2 = x2 + seqLogoFontWidth;
			i++;
		}

		ArrayList<Double> abscissaCoordinate = new ArrayList<Double>();
		abscissaCoordinate.add(initialAbscissa + seqLogoFontWidth / 2);
		abscissaCoordinate.add(y1);
		abscissaCoordinate.add(x2 - seqLogoFontWidth);
		abscissaCoordinate.add(y1);
		// index为0的坐标是画横坐标轴那条线的坐标
		abscissaCoordinates.put(0, abscissaCoordinate);

		return abscissaCoordinates;
	}

	/**
	 * 
	 * @MethodName: calculateVerticalAxisCoordinate
	 * @Description: 计算纵坐标轴的的各种坐标
	 * @author zjw
	 * @param parameter
	 * @return LinkedHashMap<Integer,ArrayList<Double>>
	 * @date 2024-05-07 10:06:25
	 */
	public static LinkedHashMap<Integer, ArrayList<Double>> calculateVerticalAxisCoordinate(
			MakeSequenceLogoParameter parameter) {

		LinkedHashMap<Integer, ArrayList<Double>> verticalAxisCoordinates = new LinkedHashMap<Integer, ArrayList<Double>>();
		boolean seqLogoType = parameter.getSeqLogoType();
		double seqLogoFontHeight = parameter.getSeqLogoFontHeight();
		double initialAbscissa = parameter.getInitialAbscissa();
		double initialOrdinate = parameter.getInitialOrdinate();

		int numOfScaleMark;
		if (seqLogoType) {
			numOfScaleMark = 5;
		} else {
			numOfScaleMark = 3;
		}

		// x1,y1是纵坐标轴的初始x,y坐标。15是纵坐标轴与sequence logo的距离
		double x1 = initialAbscissa - 15;
		double y1 = initialOrdinate;
		double x2 = initialAbscissa - 15 - parameter.tickLength;
		double y2 = initialOrdinate;

		int i = 1;
		while (i <= numOfScaleMark) {
			ArrayList<Double> verticalAxisCoordinate = new ArrayList<Double>();
			verticalAxisCoordinate.add(x1);
			verticalAxisCoordinate.add(y1);
			verticalAxisCoordinate.add(x2);
			verticalAxisCoordinate.add(y2);
			verticalAxisCoordinates.put(i, verticalAxisCoordinate);
			y1 = y1 - seqLogoFontHeight / 2;
			y2 = y2 - seqLogoFontHeight / 2;
			i++;
		}

		ArrayList<Double> verticalAxisCoordinate = new ArrayList<Double>();
		verticalAxisCoordinate.add(x1);
		verticalAxisCoordinate.add(initialOrdinate);
		verticalAxisCoordinate.add(x1);
		if (seqLogoType) {
			verticalAxisCoordinate.add(initialOrdinate - 2 * seqLogoFontHeight);
		} else {
			verticalAxisCoordinate.add(initialOrdinate - seqLogoFontHeight);
		}
		// index为0的坐标是画纵坐标轴那条线的坐标
		verticalAxisCoordinates.put(0, verticalAxisCoordinate);

		return verticalAxisCoordinates;
	}

}
