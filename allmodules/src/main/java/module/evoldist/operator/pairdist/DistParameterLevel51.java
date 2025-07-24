package module.evoldist.operator.pairdist;

public class DistParameterLevel51 extends DistParameterLevel41{

	double numOftransitionAG;
	double[] seq1CountOf4Nucl;
	
	public DistParameterLevel51() {
		clear();
	}
	
	public DistParameterLevel51(double mis,double trans,double vl,double[] t4n,double traAG,double[] seq14n){
		numOfMismatch = mis;
		numOfTransition = trans;
		validateLength = vl;
		totalCountOf4Nucl = t4n;
		numOftransitionAG = traAG;
		seq1CountOf4Nucl = seq14n;
	}
	
	@Override
	public void clear() {
		super.clear();
		seq1CountOf4Nucl = new double[4];
	}
	
	@Override
	public <E extends DistParameterLevel1> void add(E dpl) {
		super.add(dpl);
		double[] otherSeq1CountOf4Nucl = ((DistParameterLevel51) dpl).getSeq1CountOf4Nucl();
		for (int i = 0; i < 4; i++) {
			seq1CountOf4Nucl[i] += otherSeq1CountOf4Nucl[i];
		}
	}
	
	public double[] getSeq1CountOf4Nucl() {
		return seq1CountOf4Nucl;
	}
	public double[] getSeq2CountOf4Nucl() {
		double[] seq2CountOf4Nucl = new double[4];
		for (int i = 0; i < 4; i++) {
			seq2CountOf4Nucl[i]= totalCountOf4Nucl[i] - seq1CountOf4Nucl[i];
		}
		return seq2CountOf4Nucl;
	}
	
}
