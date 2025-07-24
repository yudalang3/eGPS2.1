package module.sequencelogo.makesequencelogo;

import java.util.ArrayList;

/** 
* @author zjw
* @version 创建时间：2022年8月25日 下午2:27:00 
* 类说明 用来计算和bites相关的参数
*/
public class CalculateBits {

	//计算以base为底，value的对数
	 public static double log(double value, double base) {
		if (value != 0 && base != 0) {
			return Math.log(value) / Math.log(base);
		} else {
			return 0;
		}
	}

	//计算每个位置的Rsequence(l)
	 public static double calculateAmountOfInformationPresentAtPerPosition(
			ArrayList<Double> frequenceOfBasesPerPosition) {

		return (2.0 - (-(frequenceOfBasesPerPosition.get(0) * log(frequenceOfBasesPerPosition.get(0), 2)
				+ frequenceOfBasesPerPosition.get(1) * log(frequenceOfBasesPerPosition.get(1), 2)
				+ frequenceOfBasesPerPosition.get(2) * log(frequenceOfBasesPerPosition.get(2), 2)
				+ frequenceOfBasesPerPosition.get(3) * log(frequenceOfBasesPerPosition.get(3), 2))));
	}

	//计算校正因子e(n),当样本数过少时使用
	 public static double calculateCorrectionFactor(int s, int n) {
		return ((s - 1) / (Math.log(2) * 2 * n));
	}
}
