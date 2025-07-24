package module.remnant.datapanel.data;

public interface DataFormatError {
	// VCF file format
	/** The input VCF file is not a VCF file format, cause header line error*/
	int VCF_HEADERERROR = 10001; 
	/** GT not locate in the first part of FORMAT field!!*/
	int VCF_GTERROR = 10002;
	/** Different sampleIDs*/
	int VCF_UNSAMPLEIDSERROR = 10003; 
	/** Different file format*/
	int VCF_FORMATERROR = 10004; 
	/** No meta-information*/
	int VCF_NOANNOTATIONSERROR = 10005; 
	int VCF_NOCONTENT_ERROR = 10006;
	
	// PROTEOMICS file format
	/**The input PROTEOMICS file is not a PROTEOMICS file format*/
	int PROTEOMICS_FILEERROR = 20001;
	
	// TREE file format
	/** Input file format error*/
	int TREE_FILEERROR = 30001;
	// int TREE_NEWICKERROR = 3002;

	// CIRCUSRNA file format
	/** The input CircusRNA file is not a CircusRNA file format*/
	int CIRCUSRNA_HEADERERROR = 40001; 
	/** accompany file not crrect*/
	int CIRCUSRNA_LIBRARYCORRECT = 40002;

	// Error Fasta file format
	int FASTA_HEADERERROR = 50001;
	int FASTA_SEQUENCEERROR = 50002;
	
	int SEQS_ALL_FILE_ERROR = 50003;
	
	// Error Simulator file format
	int SIMULATOREERROR = 60001;
	
	// Error MAF file format
	int MAF_HEADERERROR = 70001;
	int MAF_BLOCKERROR = 70002;
	
	/** edist error*/
	int GENETIC_DISTERROR = 80001;
	
	/** New input file is duplacated with existed files*/
	int SAMEFILEERROR = 90000;
	
	/***/
	int NEXUS_FILE_ERROR = 11001;
	int NEXUS_FILE_HEADERERROR = 11002;
	int NEXUS_FILE_CONTENTERROR = 11003;

	/** New file's format is incompatiable with current datype, current data type is*/
	int FILETYPEERROR = 0;
	

}
