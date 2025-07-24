package module.evoldist.operator.pairdist;

public class DistParameterLevel2 extends DistParameterLevel1 {
	/** No. of ElegantJTable <-> G; No. of C <-> T */
	protected double numOfTransition;
	
	public DistParameterLevel2() {
		clear();
	}
	
	public DistParameterLevel2(double mis,double trans,double vl){
		numOfMismatch = mis;
		numOfTransition = trans;
		validateLength = vl;
	}
	
	@Override
	public void clear() {
		super.clear();
		numOfTransition = 0;
	}
	
	public double getNumOfTransition() {
		return numOfTransition;
	}
	
	public double getNumOfTransversion() {
		return numOfMismatch - numOfTransition;
	}
	
	@Override
	public <E extends DistParameterLevel1> void add(E dpl) {
		super.add(dpl);
		numOfTransition += ((DistParameterLevel2) dpl).getNumOfTransition();
	}
}
