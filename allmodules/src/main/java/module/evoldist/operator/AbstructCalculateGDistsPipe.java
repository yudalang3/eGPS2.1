/**  
* <p>Title: AbstructBuildTreeCommon.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018骞�8鏈�26鏃�  
* @version 1.0  
*/
package module.evoldist.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.util.List;
import java.util.Map;

import module.evoldist.operator.pairdist.DistParameterLevel1;
import module.evoldist.operator.pairdist.DistParameterLevel2;
import module.evoldist.operator.pairdist.DistParameterLevel41;
import module.evoldist.operator.pairdist.DistParameterLevel42;
import module.evoltre.pipline.TreeParameterHandler;
import module.evoltrepipline.ConstantNameClass_EvolutionaryDistance;
import module.evoltrepipline.PairEvoDistance;
import module.evoltrepipline.ParameterAssigner;
import egps2.modulei.RunningTask;

/**
 * <p>
 * Title: The abstract class for calculating genetic distance!
 * </p>
 * <p>
 * Description: Common used methods in pairwise evolutionary or genetic distances !
 * </p>
 * 
 * @author yudalang
 * @date 2019-8-12
 * 
 * @date 2024-04-29 :ydl modification
 */
public abstract class AbstructCalculateGDistsPipe  implements RunningTask{

	protected DistanceCalculateor distanceCalculator;
	protected BootstrapDistCalculator<?> bootstrap;
	
	protected List<String> seqs;
	protected List<String> seqNames;
	protected String tabName;
	protected String filePathHeader = "debug";
	protected final Map<String, String> settingValue;

	// Following codes used for saving temporary variables
	protected boolean ifBootStrap = false;
	protected int bootStrapTimes = 0;

	
	protected BufferedWriter bfWriter;

	public AbstructCalculateGDistsPipe() {
		this(new TreeParameterHandler().getBuildTreeParametersMap());
	}
	public AbstructCalculateGDistsPipe( Map<String, String> settingValue) {
		this.settingValue = settingValue;
//		 if we need to use bootstrap
		ConstantNameClass_EvolutionaryDistance cc = new ConstantNameClass_EvolutionaryDistance();
		String bootstrapValueInP = settingValue.get(cc.label1_varianEstimateMethod);
		if (bootstrapValueInP.contains(cc.varianEstimateMethod_value2_bs)) {
			ifBootStrap = true;
			String bootstrapValueString = settingValue.get(cc.label2_numOfBSRep_index);
			bootStrapTimes = Integer.parseInt(bootstrapValueString);
		}
	}

	/**
	 * This is the tab name of Biomain frame's tab!
	 * 
	 * @author yudalang
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	/**
	 * Don't forget to set seqs and seqNames
	 * 
	 * @author yudalang
	 * @throws Exception 
	 */
	protected void incorporateParametersAndPackageCalculatorInstance() throws Exception {
		if (ifBootStrap) {
			String distName = settingValue.get(new ConstantNameClass_EvolutionaryDistance().label1_modelOrMethod);
			switch (distName) {

			case PairEvoDistance.NUM_OF_DIFF:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel1>(seqs, seqNames);
				break;
			case PairEvoDistance.P_DISTANCE:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel1>(seqs, seqNames);
				break;
			case PairEvoDistance.JC69_MODEL:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel1>(seqs, seqNames);
				break;
			case PairEvoDistance.K2P_MODEL:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel2>(seqs, seqNames);
				break;
			case PairEvoDistance.T3P_MODEL:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel2>(seqs, seqNames);
				break;
			case PairEvoDistance.TAM_NEI_MODEL:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel41>(seqs, seqNames);
				break;
			case PairEvoDistance.TAJ_NEI_MODEL:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel42>(seqs, seqNames);
				break;
			case PairEvoDistance.RATE_OF_TRANS:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel2>(seqs, seqNames);
				break;
			default:
				bootstrap = new BootstrapDistCalculator<DistParameterLevel2>(seqs, seqNames);
				break;
			}
			
			ParameterAssigner.parameterFactorForBootDist(bootstrap, settingValue);
			bootstrap.setbWriter(new File(filePathHeader + "/distBootstrap_" + bootStrapTimes + ".txt"));
			bootstrap.initialize();
		} else {
			// Not use bootstrap
			distanceCalculator = new DistanceCalculateor(seqs,seqNames);
			ParameterAssigner.parameterFactorForDCalculator(distanceCalculator, settingValue);
		}
	}

	public void setSeq_names(List<String> seq_names) {
		this.seqNames = seq_names;
	}

	public void setSeqs(List<String> seqs) {
		this.seqs = seqs;
	}
}




