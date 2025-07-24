package module.sequencelogo.makesequencelogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Triple;

/**
* @author zjw
* @date 2022年9月1日
*类说明 用于计算每个碱基的坐标位置
*/
public class CalculateCoordinate {
 	
	public static List<LinkedHashMap<Character, ArrayList<Double>>> calculateCoordinate(
			MakeSequenceLogoParameter parameter) {

		Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> NFB = parameter.getNFB();
		boolean seqLogoType = parameter.getSeqLogoType();
		double seqLogoFontWidth = parameter.getSeqLogoFontWidth();
		double seqLogoFontHeight = parameter.getSeqLogoFontHeight();
		double abscissa = parameter.getInitialAbscissa();
		double initialOrdinate = parameter.getInitialOrdinate();
		HashMap<Character, Double> adjustedPositionOfEachLetter = parameter.getAdjustedPositionOfEachLetter();
		
		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> bitsOrFrequenceOfBasesPerPosition = new LinkedHashMap<Integer, LinkedHashMap<Character, Double>>();
		if (seqLogoType) {
			bitsOrFrequenceOfBasesPerPosition = NFB.getRight();
		} else {
			bitsOrFrequenceOfBasesPerPosition = NFB.getMiddle();
		}

		List<LinkedHashMap<Character, ArrayList<Double>>> coordinateOfBasesList = new ArrayList<>();		
		for (Map.Entry<Integer, LinkedHashMap<Character, Double>> entry1 : bitsOrFrequenceOfBasesPerPosition.entrySet()) {

			LinkedHashMap<Character, Double> bitsOrFrequenceOfBases = entry1.getValue();
			LinkedHashMap<Character, ArrayList<Double>> coordinateOfPerBase = new LinkedHashMap<Character, ArrayList<Double>>();			
			double ordinate = initialOrdinate;
			for (Map.Entry<Character, Double> entry2 : bitsOrFrequenceOfBases.entrySet()) {
				ArrayList<Double> coordinateOfPerBaseList = new ArrayList<Double>();
				double adjustedPosition = adjustedPositionOfEachLetter.get(entry2.getKey());
				double bitsOrFrequence = entry2.getValue();
				// 对纵坐标进行调整
				ordinate = ordinate - bitsOrFrequence * adjustedPosition;
				coordinateOfPerBaseList.add(abscissa);
				coordinateOfPerBaseList.add(ordinate);
				// 第三个元素存需要缩放的比例
				coordinateOfPerBaseList.add(entry2.getValue());
				ordinate = ordinate - bitsOrFrequence * seqLogoFontHeight;			
							
				coordinateOfPerBase.put(entry2.getKey(), coordinateOfPerBaseList);
			}
			abscissa = abscissa + seqLogoFontWidth;
			
			coordinateOfBasesList.add(coordinateOfPerBase);			
		}
		
		return coordinateOfBasesList;
	}
}
