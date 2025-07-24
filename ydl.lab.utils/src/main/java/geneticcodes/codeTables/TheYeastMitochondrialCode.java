package geneticcodes.codeTables;

import geneticcodes.GeneticCode;

public class TheYeastMitochondrialCode extends GeneticCode{

	String[] tripletNNs_aas = { "TTT F Phe","TCT S Ser","TAT Y Tyr","TGT C Cys","TTC F Phe","TCC S Ser","TAC Y Tyr","TGC C Cys","TTA L Leu","TCA S Ser","TAA * Ter","TGA W Trp","TTG L Leu","TCG S Ser","TAG * Ter","TGG W Trp","CTT T Thr","CCT P Pro","CAT H His","CGT R Arg","CTC T Thr","CCC P Pro","CAC H His","CGC R Arg","CTA T Thr","CCA P Pro","CAA Q Gln","CGA R Arg","CTG T Thr","CCG P Pro","CAG Q Gln","CGG R Arg","ATT I Ile","ACT T Thr","AAT N Asn","AGT S Ser","ATC I Ile","ACC T Thr","AAC N Asn","AGC S Ser","ATA M Met","ACA T Thr","AAA K Lys","AGA R Arg","ATG M Met","ACG T Thr","AAG K Lys","AGG R Arg","GTT V Val","GCT ElegantJTable Ala","GAT D Asp","GGT G Gly","GTC V Val","GCC ElegantJTable Ala","GAC D Asp","GGC G Gly","GTA V Val","GCA ElegantJTable Ala","GAA E Glu","GGA G Gly","GTG V Val","GCG ElegantJTable Ala","GAG E Glu","GGG G Gly" };
	
	public TheYeastMitochondrialCode() {
		initializeMap(tripletNNs_aas);
	}

	@Override
	public int getNCBICodeTableIndex() {
		return 3;
	}

}
