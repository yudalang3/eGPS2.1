package graphic.engine;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

/**
 * tutorial:
 * 
 * <pre>
 * Pair<Double, Double> minMax = Pair.of(52.5, 57.5);
 * AxisTickCalculator cal = new AxisTickCalculator();
 * 
 * cal.setMinAndMaxPair(minMax);
 * 
 * cal.setWorkingSpace(250);
 * cal.setWorkSpaceRatio(1f);
 * cal.determineAxisTick();
 * 
 * System.out.println(cal.getTickLabels());
 * System.out.println(cal.getTickLocations());
 * 
 * </pre>
 * 
 * This is referenced from the xChart library
 * 
 */
public class AxisTickCalculator {

	private Pair<Double, Double> minAndMaxPair;

	/** the arraylist of tick label position in pixels */
	private List<Integer> tickLocations = new LinkedList<>();
	private List<String> tickLabels = new LinkedList<>();

	private int workingSpace;
	
	private float workSpaceRatio = 0.95f;

	/** the default tick mark step hint */
	private static final int DEFAULT_TICK_MARK_STEP_HINT = 64;

	protected  final static int AXIS_TICK_PADDING = 4;

	/** the normal format for tick labels */
	private Format normalFormat = new DecimalFormat("#.###########");

	/** the scientific format for tick labels */
	private Format scientificFormat = new DecimalFormat("0.###E0");

	public AxisTickCalculator() {
	}

	public List<Integer> getTickLocations() {
		return tickLocations;
	}
	
	public List<String> getTickLabels() {
		return tickLabels;
	}
	
	/**
	 * Do not forget to call this
	 * 千万不要忘记，否则计算会出问题
	 */
	public void clear() {
		tickLocations = new LinkedList<>();
		tickLabels = new LinkedList<>();
	}

	protected int getWorkingSpace() {
		return this.workingSpace;
	}

	/**
	 * 这是该类的主函数，表示进行tip的计算。
	 */
	public void determineAxisTick() {
//		 System.out.println("workingSpace= " + workingSpace);
		int tickSpace = getTickSpace();
//		 System.out.println("tickSpace= " + tickSpace);

		int margin = getMargin(tickSpace);

		// a check if all axis data are the exact same values
		if (getMax() == getMin()) {
			tickLabels.add(format(getMax()));
			tickLocations.add((int) (margin + tickSpace / 2.0));
		} else {

			final BigDecimal MIN = new BigDecimal(new Double(getMin()).toString());
			BigDecimal gridStep = getGridStep(tickSpace);

			double xyz = MIN.remainder(gridStep).doubleValue();
			BigDecimal firstPosition;
			if (xyz <= 0.0) {
				firstPosition = MIN.subtract(MIN.remainder(gridStep));
			} else {
				firstPosition = MIN.subtract(MIN.remainder(gridStep)).add(gridStep);
			}

			for (BigDecimal b = firstPosition; b.doubleValue() <= getMax(); b = b.add(gridStep)) {

				// System.out.println("b= " + b);
				tickLabels.add(format(b.doubleValue()));
				int tickLabelPosition = (int) (margin
						+ ((b.doubleValue() - getMin()) / (getMax() - getMin()) * tickSpace));
				// System.out.println("tickLabelPosition= " + tickLabelPosition);

				tickLocations.add(tickLabelPosition);
			}
		}
	}

	private int getMargin(int tickSpace) {
		int ret = (int) (0.5 * (workingSpace - tickSpace));
		return ret;
	}

	private int getTickSpace() {
		int ret = (int) (workingSpace * workSpaceRatio);
		return ret;
	}

	private BigDecimal getGridStep(int tickSpace) {

		double length = Math.abs(getMax() - getMin());
		// System.out.println(axis.getMax());
		// System.out.println(axis.getMin());
		// System.out.println(length);
		double gridStepHint = length / tickSpace * DEFAULT_TICK_MARK_STEP_HINT;
        //                  小数部分             指数
		// gridStepHint --> mantissa * 10 ** exponent
		// e.g. 724.1 --> 7.241 * 10 ** 2
		double mantissa = gridStepHint;
		int exponent = 0;
		if (mantissa == 0) {
			exponent = 1;
		} else if (mantissa < 1) {
			while (mantissa < 1) {
				mantissa *= 10.0;
				exponent--;
			}
		} else {
			while (mantissa >= 10) {
				mantissa /= 10.0;
				exponent++;
			}
		}

		// calculate the grid step with hint.
		BigDecimal gridStep;
		if (mantissa > 7.5) {
			// gridStep = 10.0 * 10 ** exponent
			gridStep = BigDecimal.TEN.multiply(pow(10, exponent));
		} else if (mantissa > 3.5) {
			// gridStep = 5.0 * 10 ** exponent
			gridStep = new BigDecimal(new Double(5).toString()).multiply(pow(10, exponent));
		} else if (mantissa > 1.5) {
			// gridStep = 2.0 * 10 ** exponent
			gridStep = new BigDecimal(new Double(2).toString()).multiply(pow(10, exponent));
		} else {
			// gridStep = 1.0 * 10 ** exponent
			gridStep = pow(10, exponent);
		}
		return gridStep;
	}

	/**
	 * Calculates the value of the first argument raised to the power of the second
	 * argument.
	 * 
	 * @param base     the base
	 * @param exponent the exponent
	 * @return the value <tt>a<sup>b</sup></tt> in <tt>BigDecimal</tt>
	 */
	private BigDecimal pow(double base, int exponent) {

		BigDecimal value;
		if (exponent > 0) {
			value = new BigDecimal(new Double(base).toString()).pow(exponent);
		} else {
			value = BigDecimal.ONE.divide(new BigDecimal(new Double(base).toString()).pow(-exponent));
		}
		return value;
	}

	private String format(double value) {

		if (Math.abs(value) < 9999 && Math.abs(value) > .0001 || value == 0) {
			return this.normalFormat.format(value);
		} else {
			return this.scientificFormat.format(value);
		}
	}

	public void setMinAndMaxPair(Pair<Double, Double> minAndMaxPair) {
		this.minAndMaxPair = minAndMaxPair;
	}
	
	/**
	 * 可以设置为1，如果你已经考虑了留白
	 */
	public void setWorkSpaceRatio(float workSpaceRatio) {
		this.workSpaceRatio = workSpaceRatio;
	}

	private double getMin() {
		return this.minAndMaxPair.getLeft();
	}

	private double getMax() {
		return this.minAndMaxPair.getRight();
	}
	
	public void setWorkingSpace(int workingSpace) {
		this.workingSpace = workingSpace;
	}

}
