package module.remnant.datapanel.data;

/**
 * @author mhl,yudalang
 * 
 */
public interface DataType {
	
	/** Evolution phylogeny; Phylogenetic tree */
	int TREE = 300;
	
	/** Evolutionary distance; Genetic distance */
	int GENETIC_DIST = 800;
	
	/** sequence alignment; Multiple sequence alignment(MSA) */
	int MULTIPLE_SEQS = 500;
	
	/** Whole genome alignment; Multiple alignment format (MAF) */
	int MAF = 700;
	
	/** Population genetics; Population history model;Simulator file format*/
	int SIMULATOR = 600;
	
	/** Omic study Genomics; Variant Call Format (VCF)*/
	int VCF = 100;
	
	/** Omic study; Transcriptomics	circ RNA visulization; Circular rna list(LIST)*/
	int CIRCUSRNA = 400;
	
	/** a format specifically for evolution! */
	int NEXML = 1000;
	
	/** a format specifically for evolution! */
	int NEXUS = 1100;
	
	/** a format specifically for data matrix! */
	int EHEATMAP = 1200;
	
	int NO_DATA = 0;
	
	/** General text file */
	int GENERAL_TEXT = 10000;
	
	int MATRIX_TABLE = 11000;

}
