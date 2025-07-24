package module.evoltrepipline;


public class ConstantNameClass_EvolutionaryDistance {
	
	public final String category1_estimateVariance = "Estimate variance";
	public final String label1_varianEstimateMethod = "Variance Estimation Method";
	public final String label1_varianEstimateMethod_index = "Variance Estimation Method_Index";
	public final String varianEstimateMethod_value1_none = "None";
	public final String varianEstimateMethod_value2_bs = "Bootstrap method";
	public final String label2_numOfBSRep = "No. of Bootstrap replications";
	public final String numOfBSRep_value1_NotAppli = "Not applicable";
	public final String label2_numOfBSRep_index = "GeneticDistance_NumOfBootstrapRep";
	
	
	public final String category2_substitutionModel = "Substitution model";
	public final String label1_substitutionType = "Substitutions Type";
	public final String substitutionType_value1_nuc = "Nucleotide";
	public final String label1_modelOrMethod = "Model/Method";
	public final String label1_modelOrMethod_index = "Model/Method_Index";
	public final String[] modelOrMethod_values = PairEvoDistance.METHOD_NAMES;
	public final String label1_substitutionToInclude = "Substitutions To Include";
	public final String substitutionToInclude_value1_two = "Transitions + Transversions";
	
	public final String category3_ratesAndPatterns = "Rates and patterns";
	public final String label1_ratesAmongSites = "Rates among Sites";
	public final String label1_ratesAmongSites_index = "Rates among Sites_Index";
	public final String ratesAmongSites_value1_uni = "Uniform Rates";
	public final String ratesAmongSites_value2_gamma = "Gamma distributed(G)";
	public final String ratesAmongSites_value3_hasI = "Has invariant Sites(I)";
	public final String ratesAmongSites_value4_gPlusI = "Gamma distributed With invariant Sites(G+I)";
	
	public final String category4_dataSubSetTouse = "Data subset to use";
	public final String label1_treatment = "Gaps/Missing Data Treatment";
	public final String label1_treatment_index = "Gaps/Missing Data Treatment_Index";
	public final String treatment_value1_compD = "Complete deletion";
	public final String treatment_value2_pairD = "Pairwise deletion";
	public final String treatment_value3_partD = "Partial deletion";
	public final String label2_siteCoverageCutoff = "Site Coverage Cutoff (%)";
	public final String siteCoverageCutoff_value1_notAppli = "Not applicable";
	public final String label2_siteCoverageCutoff_index = "Site Coverage Cutoff (%)_Index";
}
