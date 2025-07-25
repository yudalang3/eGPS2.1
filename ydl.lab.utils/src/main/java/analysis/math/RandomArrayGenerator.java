package analysis.math;


import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Random;

public class RandomArrayGenerator {

	public double[] generateRandomDoublesByColt(int length, double min, double max, long seed) {
		// 创建随机数生成器
		// 有 MersenneTwister64 和MersenneTwister两个类
		MersenneTwister rng = new MersenneTwister(seed);           // 32‑bit MT
		UniformRealDistribution dist = new UniformRealDistribution(rng, min, max);

		return dist.sample(length);
	}
	public double[] generateRandomDoubles(int length, double min, double max) {
		Random random = new Random();
		double[] randomArray = new double[length];
		for (int i = 0; i < length; i++) {
			randomArray[i] = min + (max - min) * random.nextDouble();
		}
		return randomArray;
	}



	public double[] generateRandomDoublesByMath3(int arrayLength, double min, double max, long seed) {
		// 创建随机数生成器
		org.apache.commons.math3.random.RandomGenerator rng = new org.apache.commons.math3.random.MersenneTwister(seed);

		// 创建均匀分布对象，区间是 [0.0, 1.0]
		org.apache.commons.math3.distribution.UniformRealDistribution uniformDist = new org.apache.commons.math3.distribution.UniformRealDistribution(
				rng, min, max);

		// 创建并填充均匀分布的 double 数组
		double[] uniformArray = new double[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			uniformArray[i] = uniformDist.sample();
		}

		return uniformArray;
	}
}