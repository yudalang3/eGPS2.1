package module.evoldist.operator.pairdist;

public class SimpleObj {
	double[] numOfTransition = new double[2];
	double numOfTransversion = 0;
	double validLength = 0;

	public void addDiffInfo(double ag, double ct, double version) {
		this.numOfTransition[0] += ag;
		this.numOfTransition[1] += ct;

		this.numOfTransversion += version;
	}

	/**
	 * 
	 * @param factor: [0,1]
	 */
	public void addOneValidateLength(double factor) {
		validLength += factor;
	}
	
	public double getDifference() {
		return numOfTransition[0] + numOfTransition[1] + numOfTransversion;
	}
}