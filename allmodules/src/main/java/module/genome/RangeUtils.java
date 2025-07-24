package module.genome;

public class RangeUtils {

	public static boolean isOverlap(Range a, Range b) {
		// 检查两个范围是否有重叠
		return a.start < b.end && a.end > b.start;
	}
}
