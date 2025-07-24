package analysis.math;

import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;

import java.util.Random;

public class RandomArrayGenerator {
	public double[] generateRandomDoubles(int length, double min, double max) {
		Random random = new Random();
		double[] randomArray = new double[length];
		for (int i = 0; i < length; i++) {
			randomArray[i] = min + (max - min) * random.nextDouble();
		}
		return randomArray;
	}

	public double[] generateRandomDoublesByColt(int length, double min, double max, long seed) {
		// 创建随机数生成器
		// 有 MersenneTwister64 和MersenneTwister两个类
		RandomEngine engine = new MersenneTwister64((int) seed);
		Uniform uniformDist = new Uniform(min, max, engine);

		double[] randomArray = new double[length];
		for (int i = 0; i < length; i++) {
			randomArray[i] = uniformDist.nextDouble();
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