package module.evoldist.operator.pairdist;

public class DistParameterLevel1 {

	protected double numOfMismatch;
	/** Sequences except ATGC( a.k.a fuzzy nucleotide and/or gap‘-’) */
	protected double validateLength;

	public DistParameterLevel1() {
		clear();
	}
	
	public DistParameterLevel1(double mis,double vl){
		numOfMismatch = mis;
		validateLength = vl;
	}

	public void clear() {
		numOfMismatch = 0;
		validateLength = 0;
	}

	public <E extends DistParameterLevel1> void add(E dpl) {
		numOfMismatch += dpl.getNumOfMismatch();
		validateLength += dpl.getValidateLength();
	}

	public double getNumOfMismatch() {
		return numOfMismatch;
	}

	public double getValidateLength() {
		return validateLength;
	}

}
