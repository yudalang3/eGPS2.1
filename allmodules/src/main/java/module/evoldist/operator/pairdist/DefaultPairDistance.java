package module.evoldist.operator.pairdist;

import module.evoldist.operator.disAlgo.SimplePDistance;

public class DefaultPairDistance<E extends DistParameterLevel1>{
	private Class<E> kind;
	
	private final DistParameterPareparer pareparer;
	private EvoPairwiseDistMethod<E> evopairDistMethod;
	
	public DefaultPairDistance(Class<E> kind,boolean ifAmbiguous,EvoPairwiseDistMethod<E> m){
		this.kind = kind;
		this.evopairDistMethod = m;
		if (ifAmbiguous) {
			this.pareparer = new DistParameterPareparerABEqualPos();
		}else {
			this.pareparer = new DistParameterPareparerABAsGap();
		}
		
	}
	
	/**
	 * @author yudalang
	 * In some cases, you need to get the temporary intermediate distance!
	 * This can be more Optimized!
	 * 这个可以被优化，用一个组合模式，尚不清楚组合效率是否更高！！
	 * 同时因为 EvoPairwiseDistMethod 是传进来的。
	 * 其实这个传进来也似乎是一个冗余的工作。
	 * 这些我想等我在把《JAVA编程思想》积累深厚之后再改。 
	 */
	@SuppressWarnings("unchecked")
	public E getIntermediateDistPara(String seq1, String seq2) {
		if (kind.isAssignableFrom(DistParameterLevel1.class)) {
			return (E) pareparer.getLeve1Para(seq1, seq2);
		}else if (kind.isAssignableFrom(DistParameterLevel2.class)) {
			return (E) pareparer.getLeve2Para(seq1, seq2);
		}else if (kind.isAssignableFrom(DistParameterLevel3.class)) {
			return (E) pareparer.getLeve3Para(seq1, seq2);
		}else if (kind.isAssignableFrom(DistParameterLevel41.class)) {
			return (E) pareparer.getLeve41Para(seq1, seq2);
		}else if (kind.isAssignableFrom(DistParameterLevel42.class)) {
			return (E) pareparer.getLeve42Para(seq1, seq2);
		}else {
			return (E) pareparer.getLeve51Para(seq1, seq2);
		}
	
	}

	/**
	 * This is the final step to get the evolutional distance! Commonly, users just
	 * need to invoke this method!
	 * 
	 * @throws throw
	 *             exceptions: 1) seqs full of gaps. so the validate length equal 0;
	 */
	public double getEvoDistance(String seq1, String seq2) {
		return getEvoDistance(getIntermediateDistPara(seq1, seq2));
	}

	/**
	 * From EvoDistParameter to get genetic distance!
	 */
	public double getEvoDistance(E wholePairwiseProps) {
		return evopairDistMethod.getPairwiseDist(wholePairwiseProps);
	}
	
	public Class<E> getKind() {
		return kind;
	}
	public EvoPairwiseDistMethod<E> getEvopairDistMethod() {
		return evopairDistMethod;
	}
	
	public static void main(String[] args) {
		Class<DistParameterLevel1> kind = DistParameterLevel1.class;
		SimplePDistance<DistParameterLevel1> ss = new SimplePDistance<>();
		DefaultPairDistance<DistParameterLevel1> defaultPairDistance = new DefaultPairDistance<>(kind, false, ss);
	
		double evoDistance = defaultPairDistance.getEvoDistance("ATCGG", "ATCGC");
		System.out.println(evoDistance);
	}


}
