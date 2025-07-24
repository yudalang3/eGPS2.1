package module.evoltrepipline;

import java.util.Map;

import module.remnant.treeoperator.dmMethod.BuilderBootstrapTree4MSA;
import module.remnant.treeoperator.dmMethod.BuilderSinglePhyloTree;
import module.remnant.treeoperator.reconAlgo.NJ;
import module.remnant.treeoperator.reconAlgo.SwiftNJ;
import module.remnant.treeoperator.reconAlgo.TreeReconMethod;
import module.remnant.treeoperator.reconAlgo.Upgma;

import module.evoldist.operator.BootstrapDistCalculator;
import module.evoldist.operator.DistanceCalculateor;

public class ParameterAssigner {
	
	/**
	 * complete the BuildPhylogeneticTree instance by incorporating the parameters.
	* @author yudalang
	* @date 2019年7月3日
	 */
	public final static DistanceCalculateor parameterFactorForDCalculator(DistanceCalculateor distanceCalculateor,Map<String, String> settingValue) {
		
		ConstantNameClass_EvolutionaryDistance cc = new ConstantNameClass_EvolutionaryDistance();
		String gaps_missDataTreatment = settingValue.get(cc.label1_treatment);
		/**
		 * Set "Subsitution model"
		 * "p-distance", "Kimura 2-parameter model", "No. of differences", "Jukes-cantor model",
			"Tajima-Nei model", "Tamura 3-parameter model", "Tamura-Nei model"
		 */
		String subsitutionModel = settingValue.get(cc.label1_modelOrMethod);
		ConstantNameClass_GeneticCode c2 = new ConstantNameClass_GeneticCode();
		String ambiguousBaseTreat = settingValue.get(c2.label2_geneticNucleotide);
		
		if (ambiguousBaseTreat.equalsIgnoreCase(c2.geneticNucleotide_value2)) {
			distanceCalculateor.initDistance(subsitutionModel,true);
			distanceCalculateor.setIfSupportFuzzyBase(true);
		}else {
			distanceCalculateor.initDistance(subsitutionModel,false);
			// Don't need because it need
			//distanceCalculateor.setIfSupportFuzzyBase(false);
		}
		/**
		 * Set "Subsitutions to include"
		 */
		if (gaps_missDataTreatment.equalsIgnoreCase(cc.treatment_value1_compD)) {
			distanceCalculateor.setGap_miss_dataTreatment(1);
		} else if (gaps_missDataTreatment.contains(cc.treatment_value2_pairD)) {
			distanceCalculateor.setGap_miss_dataTreatment(2);
		} else {
			distanceCalculateor.setGap_miss_dataTreatment(3);
			String siteCoverageCutoff = settingValue.get(cc.label2_siteCoverageCutoff_index);
			distanceCalculateor.setPartialDeleteRatio(Double.parseDouble(siteCoverageCutoff) / 100d);
		}
		
		return distanceCalculateor;
	}
	
	/**
	 * complete the BuildPhylogeneticTree instance by incorporating the parameters.
	 * 
	 * @author yudalang
	 * @date 2018-11-5
	 */
	public final static BuilderSinglePhyloTree parameterFactor(BuilderSinglePhyloTree treeBuild_instance,Map<String, String> settingValues) {

		parameterFactorForDCalculator(treeBuild_instance,settingValues);

		ConstantNameClass_TreeBuildMethod cc = new ConstantNameClass_TreeBuildMethod();
		/**
		 * Set "Tree build method"
		 */
		String treeBuildMethod = settingValues.get(cc.label1_treeBuildMethod);
		TreeReconMethod tReconMethod = null;
		// System.out.println(treeBuildMethod);
		if (treeBuildMethod.equalsIgnoreCase(cc.treeBuildMethod_value1_NJ)) {
			tReconMethod = new NJ();
		} else if (treeBuildMethod.equalsIgnoreCase(cc.treeBuildMethod_value2_SNJ)) {
			tReconMethod = new SwiftNJ();
		} else if (treeBuildMethod.equalsIgnoreCase(cc.treeBuildMethod_value3_UPGMA)) {
			tReconMethod = new Upgma();
		}else {
			tReconMethod = new NJ();
		}
		treeBuild_instance.setTreeReconMethod(tReconMethod);

		return treeBuild_instance;
	}

	public final static BuilderBootstrapTree4MSA parameterFactorBootTree(BuilderBootstrapTree4MSA treeBuild_instance,Map<String, String> settingValues) {
		parameterFactorForBootDist(treeBuild_instance,settingValues);
		
		ConstantNameClass_TreeBuildMethod cc = new ConstantNameClass_TreeBuildMethod();
		/**
		 * Set "Tree build method"
		 */
		String treeBuildMethod = settingValues.get(cc.label1_treeBuildMethod);
		TreeReconMethod tReconMethod = null;
		// System.out.println(treeBuildMethod);
		if (treeBuildMethod.equalsIgnoreCase(cc.treeBuildMethod_value1_NJ)) {
			tReconMethod = new NJ();
		} else if (treeBuildMethod.equalsIgnoreCase(cc.treeBuildMethod_value2_SNJ)) {
			tReconMethod = new SwiftNJ();
		} else if (treeBuildMethod.equalsIgnoreCase(cc.treeBuildMethod_value3_UPGMA)) {
			tReconMethod = new Upgma();
		}else {
			tReconMethod = new NJ();
		}
		treeBuild_instance.setTreeReconMethod(tReconMethod);
		return treeBuild_instance;
	}
	
	public final static BootstrapDistCalculator parameterFactorForBootDist(BootstrapDistCalculator distanceCalculateor,Map<String, String> settingValue) {

		parameterFactorForDCalculator(distanceCalculateor, settingValue);
		
		ConstantNameClass_EvolutionaryDistance cc = new ConstantNameClass_EvolutionaryDistance();
		String bootstrapValueInP = settingValue.get(cc.label1_varianEstimateMethod);
		if (bootstrapValueInP.contains(cc.varianEstimateMethod_value2_bs)) {
			int bootTimes = Integer.parseInt(settingValue.get(cc.label2_numOfBSRep_index));
			distanceCalculateor.setBootTimes(bootTimes);
		}
		
		return distanceCalculateor;
	}
}
