package module.evoldist.operator.pairdist;

public class DistParameterLevel41 extends DistParameterLevel3{

	double numOftransitionAG;
	
	public DistParameterLevel41() {
		clear();
	}
	
	public DistParameterLevel41(double mis,double trans,double vl,double[] t4n,double traAG){
		numOfMismatch = mis;
		numOfTransition = trans;
		validateLength = vl;
		totalCountOf4Nucl = t4n;
		numOftransitionAG = traAG;
	}
	
	@Override
	public void clear() {
		super.clear();
		numOftransitionAG = 0;
	}
	
	public double getNumOfTransitionAG() {
		return numOftransitionAG;
	}
	public double getNumOfTransitionTC() {
		return numOfTransition - numOftransitionAG;
	}
	
	@Override
	public <E extends DistParameterLevel1> void add(E dpl) {
		super.add(dpl);
		numOftransitionAG += ((DistParameterLevel41) dpl).getNumOfTransitionAG();
	}
	
}
