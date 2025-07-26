package module.multiseq.alignment;

/**
 * 
* <p>Title: DataFileSuffixNames</p>  
* <p>Description: A class to store input file suffix names regular expressions</p>
* @author yudalang  
* @date 2019-2-27
* @see https://stackoverflow.com/questions/10205437/compare-one-string-with-multiple-values-in-one-expression
 */
public interface DataFileSuffixNames {
	
	String VCF_FILE_SUFFIX = "(?i)vcf|vcf.gz";
	String CIRCUSRNA_FILE_SUFFIX = "(?i)list";
	String SIMULATOR_FILE_SUFFIX = "(?i)simu";
	String MAF_FILE_SUFFIX = "(?i)maf|maf.gz";
	String GENETIC_DIST_FILE_SUFFIX = "(?i)dist|edist";
	String EHEATMAP_FILE_SUFFIX = "(?i)eheatmap";
	
	// Tree foramts
	String TREE_FILE_ETREE_SUFFIX_REG = "(?i)etree";
	String TREE_FILE_NWK_SUFFIX_REG = "(?i)tre|tree|nwk|nh";
	String TREE_FILE_NHX_SUFFIX_REG = "(?i)nhx";
	
	
	// Sequences
	// File suffix name regular expression 
	// (?i) means ignore case
	String GENBANK_FILE_SUFFIX_REG = "(?i)gb|gbk";
	String EMBL_FILE_SUFFIX_REG = "(?i)emb|embl";
	String STOCKHOLM_FILE_SUFFIX_REG = "(?i)sto|sth";
	String PIRNBRF_FILE_SUFFIX_REG = "(?i)pir|nbrf";
	String PHYLIP_FILE_SUFFIX_REG = "(?i)phy|phylip";
	String MEGA_FILE_SUFFIX_REG = "(?i)meg|mega";
	String PAML_FILE_SUFFIX_REG = "(?i)pml|nuc|paml";
	String CLUSTALW_FILE_SUFFIX_REG = "(?i)aln|clu";
	String GCGMSF_FILE_SUFFIX_REG = "(?i)msf";
	String FASTA_FILE_SUFFIX_REG = "(?i)fn|fa|fas|fasta";
	
	// Default suffix name
	String PHYLIP_DEFAULT_SUFFIX = "phy";
	String MEGA_DEFAULT_SUFFIX = "meg";
	String PAML_DEFAULT_SUFFIX = "nuc";
	String CLUSTALW_DEFAULT_SUFFIX = "aln";
	String GCGMSF_DEFAULT_SUFFIX = "msf";
	String FASTA_DEFAULT_SUFFIX = "fas";
	String NEXUS_DEFAULT_SUFFIX = "nex";
	
	String NEXML_FILE_SUFFIX_REG = "(?i)xml";
	String NEXUS_FILE_SUFFIX_REG = "(?i)nx|nex|nexus";

	
}
