package module.gff3opr.model;

import com.google.common.base.Joiner;

import module.genome.GenomicRange;

public class YuFeatureFormatter {

	public final String GENE_FEATURE_HEADER = "Source\tChr\tStart\tEnd\tStrand\tGeneSymbol\tName\tDesc";
	public final String GENE_WITH_LONGEST_CDS_HEADER = "Source\tChr\tStart\tEnd\tStrand\tGeneID\tGeneSymbol\tmRNA\tProtein\tDesc";

	public String formatOfGeneFeature(GFF3Feature feature) {
		Joiner on = Joiner.on('\t');

		GenomicRange location = feature.location();
		char strand = location.bioStrand();
		int start = location.bioStart();
		int end = location.bioEnd();

		String geneID = feature.getAttribute("gene_id");
		String name = feature.getAttribute("Name");
		String desc = feature.getAttribute("description");

		String join = on.join(feature.source(), feature.seqname(), start, end, strand, geneID, name, desc);

		return join;
	}

	public String formatOfGeneFeatureWithLongestCDS(GFF3Feature feature, GFF3Feature longestCDS) {
		Joiner on = Joiner.on('\t');

		GenomicRange location = feature.location();
		char strand = location.bioStrand();
		int start = location.bioStart();
		int end = location.bioEnd();

		String geneID = feature.getAttribute("gene_id");
		String name = feature.getAttribute("Name");
		String desc = feature.getAttribute("description");

		String mRNA = longestCDS.getAttribute("Parent");
		String prot = longestCDS.getAttribute("protein_id");

		// 在实际的处理中发现，有些gff3的feature没有desc属性
		// 如果没有这个值就直接用""代替
		desc = desc == null ? "" : desc;

		String join = on.join(feature.source(), feature.seqname(), start, end, strand, geneID, name, mRNA, prot, desc);

		return join;
	}

}
