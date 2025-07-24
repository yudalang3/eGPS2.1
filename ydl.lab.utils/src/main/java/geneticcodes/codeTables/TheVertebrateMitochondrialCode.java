package geneticcodes.codeTables;

import geneticcodes.GeneticCode;

public class TheVertebrateMitochondrialCode extends GeneticCode{

	String[] tripletNNs_aas = { "TTT F Phe", "TCT S Ser", "TAT Y Tyr", "TGT C Cys", "TTC F Phe", "TCC S Ser",
			"TAC Y Tyr", "TGC C Cys", "TTA L Leu", "TCA S Ser", "TAA * Ter", "TGA W Trp", "TTG L Leu", "TCG S Ser",
			"TAG * Ter", "TGG W Trp", "CTT L Leu", "CCT P Pro", "CAT H His", "CGT R Arg", "CTC L Leu", "CCC P Pro",
			"CAC H His", "CGC R Arg", "CTA L Leu", "CCA P Pro", "CAA Q Gln", "CGA R Arg", "CTG L Leu", "CCG P Pro",
			"CAG Q Gln", "CGG R Arg", "ATT I Ile", "ACT T Thr", "AAT N Asn", "AGT S Ser", "ATC I Ile", "ACC T Thr",
			"AAC N Asn", "AGC S Ser", "ATA M Met", "ACA T Thr", "AAA K Lys", "AGA * Ter", "ATG M Met", "ACG T Thr",
			"AAG K Lys", "AGG * Ter", "GTT V Val", "GCT ElegantJTable Ala", "GAT D Asp", "GGT G Gly", "GTC V Val", "GCC ElegantJTable Ala",
			"GAC D Asp", "GGC G Gly", "GTA V Val", "GCA ElegantJTable Ala", "GAA E Glu", "GGA G Gly", "GTG V Val", "GCG ElegantJTable Ala",
			"GAG E Glu", "GGG G Gly" };

	public TheVertebrateMitochondrialCode() {
		initializeMap(tripletNNs_aas);
	}

	@Override
	public int getNCBICodeTableIndex() {
		return 2;
	}

}
