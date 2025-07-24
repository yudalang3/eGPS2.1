package module.remnant.datapanel.data;

public interface DataFormat {
	/** VCF file format*/
	int VCF_UNCOMPRESS = 101;
	int VCF_BZIP = 102;
	int VCF_GZIP = 103;

	//下面这些暂时不需要
//	int VCF_UNCOMPRESS_KNOWN_OUTGROUP = 104;
//	int VCF_BZIP_KNOWN_OUTGROUP = 105;
//	int VCF_GZIP_KNOWN_OUTGROUP = 106;

	int TREE_NHX = 302;
	/** newick */
	int TREE_NH = 303;
	int TREE_ETREE = 304;
	
	/** CIRCUSRNA file format*/
	int CIRCUS_RNA = 401;

	/** Multipe sequence alignemt format*/
	int ALIGNED_EMBL = 502;
	int ALIGNED_STOCKHOLM = 503;
	int ALIGNED_PIRNBRF = 506;
	int ALIGNED_PHYLIP = 507;
	int ALIGNED_MEGA = 508;
	int ALIGNED_PAML = 509;
	int ALIGNED_CLUSTALW = 510;
	int ALIGNED_GCGMSF = 511;
	int ALIGNED_FASTA = 512;
	int UNALIGNED_GENBANK = 521;
	int UNALIGNED_EMBL = 522;
	int UNALIGNED_PIRNBRF = 525;
	int UNALIGNED_FASTA = 526;
	
	/** NEXML data type 's data format*/
	int NEXML_TREE = 1001;
	int NEXML_SEQ = 1002;
	int NEXML_BOTH = 1003;
	
	/** NEXUS data type 's data format*/
	int NEXUS_TREE = 1101;
	int NEXUS_SEQ = 1102;
	int NEXUS_BOTH = 1103;
	
	int EHEATMAP = 1201;
	
	String GENBANK_FORMAT_NAME = "genbank";
	String EMBL_FORMAT_NAME = "embl";
	String STOCKHOLM_FORMAT_NAME = "stockholm";
	String NEXML_FORMAT_NAME = "nexml";
	String NEXUS_FORMAT_NAME = "paup/nexus";
	String PIRNBRF_FORMAT_NAME = "pir/nbrf";
	String PHYLIP_FORMAT_NAME = "phylip";
	String MEGA_FORMAT_NAME = "mega";
	String PAML_FORMAT_NAME = "paml";
	String CLUSTALW_FORMAT_NAME = "clustalW";
	String GCGMSF_FORMAT_NAME = "gcg(msa)/msf";
	String FASTA_FORMAT_NAME = "fasta";

	/** Simulator file format*/
	int SIMULATOR = 601;
	
	/** MAF file format*/
	int MAF_UNCOMPRESS = 701;
	int MAF_COMPRESSED = 702;
	
	/** Genetic distance file format*/
	int GENETIC_DIST = 801;
	
	int NO_FOMAT = 0;
	
	/** General text file */
	int GENERAL_TEXT_FORMAT = 10001;
	int MATRIX_TABLE = 11001;
	
}
