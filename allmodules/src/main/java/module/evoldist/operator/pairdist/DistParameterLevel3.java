package module.evoldist.operator.pairdist;

public class DistParameterLevel3 extends DistParameterLevel2 {

	/** count of A G C T */
	protected double[] totalCountOf4Nucl;
	
	public DistParameterLevel3() {
		clear();
	}
	
	public DistParameterLevel3(double mis,double trans,double vl,double[] t4n){
		numOfMismatch = mis;
		numOfTransition = trans;
		validateLength = vl;
		totalCountOf4Nucl = t4n;
	}
	
	@Override
	public void clear() {
		super.clear();
		totalCountOf4Nucl = new double[4];
	}
	
	public double[] getTotalCountOf4Nucleotide() {
		return totalCountOf4Nucl;
	}
	
	@Override
	public <E extends DistParameterLevel1> void add(E dpl) {
		super.add(dpl);
		double otherDpl4nucl[] = ((DistParameterLevel3) dpl).getTotalCountOf4Nucleotide();
		for (int i = 0; i < 4; i++) {
			totalCountOf4Nucl[i] += otherDpl4nucl[i];
		}
	}
}
