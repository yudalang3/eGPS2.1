package analysis.math;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Utility class for operations on lists and arrays of double values.
 *
 * Provides methods for finding minimum and maximum values in double data structures.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class DoubleListUtils {

    /**
     * Finds the minimum and maximum values in a list of Double objects.
     * 
     * @param data the list of double values
     * @return a Pair containing (min, max) values
     */
    public static Pair<Double, Double> getMinMax(List<Double> data) {
        // 使用Stream API一次性获取最小值和最大值
        double minValue = data.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN); // 获取最小值，用Double.NaN作为默认值（如果没有元素）
        double maxValue = data.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN); // 获取最大值

        return Pair.of(minValue, maxValue);
    }

    /**
     * Finds the minimum and maximum values in an array of double primitives.
     * 
     * @param data the array of double values
     * @return a Pair containing (min, max) values
     */
    public static Pair<Double, Double> getMinMax(double[] data) {
        // 检查数组是否为空
        if (data == null || data.length == 0) {
            return Pair.of(Double.NaN, Double.NaN);
        }

        // 使用DoubleStream API一次性获取最小值和最大值
        OptionalDouble minValue = Arrays.stream(data).min();
        OptionalDouble maxValue = Arrays.stream(data).max();

        // 获取OptionalDouble中的值，如果数组有元素则获取，否则返回Double.NaN
        double minVal = minValue.orElse(Double.NaN);
        double maxVal = maxValue.orElse(Double.NaN);

        return Pair.of(minVal, maxVal);
    }

}
