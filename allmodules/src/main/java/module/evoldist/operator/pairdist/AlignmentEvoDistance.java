package module.evoldist.operator.pairdist;

import module.evoldist.operator.disAlgo.JukesCantorModel;
import module.evoldist.operator.disAlgo.Kimura2ParamModel;
import module.evoldist.operator.disAlgo.NumOfDifferences;
import module.evoldist.operator.disAlgo.SimplePDistance;
import module.evoldist.operator.disAlgo.TajimaNeiModel;
import module.evoldist.operator.disAlgo.Tamura3ParamModel;
import module.evoldist.operator.disAlgo.TamuraNeiModel;
import module.evoldist.operator.disAlgo.TheTransversionRate;
import module.evoltrepipline.PairEvoDistance;

public class AlignmentEvoDistance <T extends DistParameterLevel1>{

	DefaultPairDistance<?> evoDistance;

	protected String[] seqs = {};
	protected String[] seqNames = {};

	/**
	 * 
	 * @param distName : Note this should not be assigned manually!
	 * @param ifAB: if ambiguous base was treat as candidate bases with equal possibility!
	 */
	public AlignmentEvoDistance(String distName,boolean ifAB) {
		initialize(distName,ifAB);
	}

	private void initialize(String distName, boolean ifAB) {
		switch (distName) {

		case PairEvoDistance.NUM_OF_DIFF:
			Class<DistParameterLevel1> kind = (Class<DistParameterLevel1>) DistParameterLevel1.class;
			evoDistance = new DefaultPairDistance<>( kind, ifAB, new NumOfDifferences<DistParameterLevel1>());
			break;
		case PairEvoDistance.P_DISTANCE:
			Class<DistParameterLevel1> kind2 = (Class<DistParameterLevel1>) DistParameterLevel1.class;
			evoDistance = new DefaultPairDistance<>(kind2, ifAB, new SimplePDistance<DistParameterLevel1>());
			break;
		case PairEvoDistance.JC69_MODEL:
			Class<DistParameterLevel1> kind3 = (Class<DistParameterLevel1>) DistParameterLevel1.class;
			evoDistance = new DefaultPairDistance<>(kind3, ifAB, new JukesCantorModel<DistParameterLevel1>());
			break;
		case PairEvoDistance.K2P_MODEL:
			Class<DistParameterLevel2> kind4 = (Class<DistParameterLevel2>) DistParameterLevel2.class;
			evoDistance = new DefaultPairDistance<>(kind4, ifAB, new Kimura2ParamModel<DistParameterLevel2>());
			break;
		case PairEvoDistance.T3P_MODEL:
			Class<DistParameterLevel3> kind5 = (Class<DistParameterLevel3>) DistParameterLevel3.class;
			evoDistance = new DefaultPairDistance<>(kind5, ifAB, new Tamura3ParamModel<DistParameterLevel3>());
			break;
		case PairEvoDistance.TAM_NEI_MODEL:
			Class<DistParameterLevel41> kind6 = (Class<DistParameterLevel41>) DistParameterLevel41.class;
			evoDistance = new DefaultPairDistance<>(kind6, ifAB, new TamuraNeiModel<DistParameterLevel41>());
			break;
		case PairEvoDistance.TAJ_NEI_MODEL:
			Class<DistParameterLevel42> kind7 = (Class<DistParameterLevel42>) DistParameterLevel42.class;
			evoDistance = new DefaultPairDistance<>(kind7, ifAB, new TajimaNeiModel<DistParameterLevel42>());
			break;
		case PairEvoDistance.RATE_OF_TRANS:
			Class<DistParameterLevel2> kind8 = (Class<DistParameterLevel2>) DistParameterLevel2.class;
			evoDistance = new DefaultPairDistance<>(kind8, ifAB, new TheTransversionRate<DistParameterLevel2>());
			break;
		default:
			Class<DistParameterLevel2> kind9 = (Class<DistParameterLevel2>) DistParameterLevel2.class;
			evoDistance = new DefaultPairDistance<>(kind9, ifAB, new Kimura2ParamModel<DistParameterLevel2>());
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	public DefaultPairDistance<T> getPairEvoDistance() {
		return (DefaultPairDistance<T>) evoDistance;
	}
	
	public void reSetSeqs_and_seqNames(String[] seqs, String[] seq_names) {
		this.seqs = seqs;
		this.seqNames = seq_names;
	}
	public void reSetSeqs(String[] seqs) {
		this.seqs = seqs;
	}
	
	/**
	 * @return a distance value matix
	 * 
	 * <pre>
	 * num of row = num of col = num of OTUs -1
	 * 
	 * |0.1
	 * |0.2	0.3
	 * |0.4	0.5
	 * |0.6	0.7	0.8
	 * </pre>
	 */
	public double[][] getEvoDistances(){
		// Also num of OTUs - 1
		int numOfRowsInDisM = seqs.length - 1;
		
		double[][] ret = new double[numOfRowsInDisM][];
		// be caution of i,j
		for (int i = 1; i <= numOfRowsInDisM; i++) {
			double[] aRow = new double[i];
			for (int j = 0; j < i; j++) {
				aRow[j] = evoDistance.getEvoDistance(seqs[i], seqs[j]);
			}
			ret[i - 1] = aRow;
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public T[][] getEvoDistParameters() {
		// Also num of OTUs - 1
		int numOfRowsInDisM = seqs.length - 1;

		T[][] ret = (T[][]) new DistParameterLevel1[numOfRowsInDisM][];
		// be caution of i,j
		for (int i = 1; i <= numOfRowsInDisM; i++) {
			DistParameterLevel1[] aRow1 = new DistParameterLevel1[i];
			for (int j = 0; j < i; j++) {
				aRow1[j] = evoDistance.getIntermediateDistPara(seqs[i], seqs[j]);
			}
			
			T[] aRow = (T[]) aRow1;
			ret[i - 1] = aRow;
		}

		return ret;
	}
	
	public static void main(String[] args) {
		AlignmentEvoDistance<DistParameterLevel1> tt = new AlignmentEvoDistance<DistParameterLevel1>(PairEvoDistance.JC69_MODEL, false);
		
		
		tt.reSetSeqs(new String[] {"ACT","ACG"});
		double[][] evoDistances = tt.getEvoDistances();
		System.out.println(evoDistances[0][0]);
		DistParameterLevel1[][] paras = tt.getEvoDistParameters();
		System.out.println(paras[0][0].getNumOfMismatch()+"\t"+paras[0][0].getValidateLength());
	}
}
