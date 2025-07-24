package module.evoltrepipline;


/**
 *  Level1:NumOfDiff,P distance,JC69
 *  Level2:K2P,Transversion rate
 *  Level3:T3P
 *  Level41:TamuraNei Level42:TajimaNei
 *  Level51:LogDet
 * 
 * @author yudalang
 * @date 2019-08-30
 *
 * @param <E> son of DistParameterLevel1
 */
public interface PairEvoDistance{

	String NUM_OF_DIFF = "No. of differences";
	String P_DISTANCE = "p-distance";
	String JC69_MODEL = "Jukes-cantor model";
	String K2P_MODEL = "Kimura 2-parameter model";
	String T3P_MODEL = "Tamura 3-parameter model";
	String TAM_NEI_MODEL = "Tamura-Nei model";
	String TAJ_NEI_MODEL = "Tajima-Nei model";
	String RATE_OF_TRANS = "The rate of transversion";
	
	String[] METHOD_NAMES = { 
			NUM_OF_DIFF,
			P_DISTANCE,
			JC69_MODEL,
			K2P_MODEL,
			T3P_MODEL,
			TAM_NEI_MODEL,
			TAJ_NEI_MODEL,
			RATE_OF_TRANS
	};
	
}
