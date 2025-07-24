package analysis;

import com.google.common.base.Stopwatch;

/**
 * 任何实现分析动作的基类。
 * 
 * 封装了三个东西：inputPath，outputPath 两个属性 和 doIt()这个动作
 * 
 * @author yudal
 *
 */
public abstract class AbstractAnalysisAction {

	protected String inputPath;
	protected String outputPath;

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	/**
	 * 设置好参数之后，执行的动作。
	 * 
	 * @throws Exception
	 */
	public abstract void doIt() throws Exception;

	/**
	 * 具有运行时间的快捷调用方式
	 * 
	 * @throws Exception
	 */
	public void runIt() throws Exception {
		Stopwatch stopwatch = Stopwatch.createStarted();
		doIt();
		stopwatch.stop();
		System.out.println("Elapsed time: ".concat(stopwatch.toString()));
	}

	protected void check() {
		if (inputPath == null) {
			throw new IllegalArgumentException("Please enter input path first.");
		}

		if (outputPath == null) {
			throw new IllegalArgumentException("Please enter output path first.");
		}

	}

}
