package geneticcodes;

public interface IGeneticCode {
	/**
	 * yudalang: 最好不要直接用这个直接去初始化
	 */
	String[] GeneticCodeTableNames = { "The Standard Code", 
			"The Vertebrate Mitochondrial Code",
			"The Yeast Mitochondrial Code", 
			"The Invertebrate Mitochondrial Code",
			"The Ciliate, Dasycladacean and Hexamita Nuclear Code", 
			"The Echinoderm and Flatworm Mitochondrial Code",
			"The Euplotid Nuclear Code", 
			"The Bacterial, Archaeal and Plant Plastid Code",
			"The Mold, Protozoan, and Coelenterate Mitochondrial Code and the Mycoplasma/Spiroplasma Code" };

	int THE_STANDARD_CODE = 0;
	int The_Bacterial_ArchaealandPlant_Plastid_Code = 7;
}
