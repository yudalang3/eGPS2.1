package primary.struct.display;

import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;
import primary.struct.display.data.PairwiseHomologyData;
import utils.string.EGPSStringUtil;

public class ParametersImporter extends AbstractGuiBaseVoiceFeaturedPanel {

	private MyModuleFace mainFace;

	public ParametersImporter(MyModuleFace mainFace) {
		this.mainFace = mainFace;

	}


	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("query.fasta.file",
				"C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Holzem et al., 2024_used_to_judge_homo\\process\\passing_by_similarity\\dir_to_run_mine_blastp_EDA_no_clan_overlap\\Amoebozoa\\2.proteins_candidates.fa",
				"The query fasta file of input data");
		mapProducer.addKeyValueEntryBean("subject.fasta.file", "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Wnt_QuerySequences\\compoentSet4\\set4\\wntPathway_querySeq_compoentSet4.fa",
				"The subject fasta file of input data");
		mapProducer.addKeyValueEntryBean("blast.fmt6.result.file", "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Holzem et al., 2024_used_to_judge_homo\\process\\passing_by_similarity\\dir_to_run_mine_blastp_EDA_no_clan_overlap\\Amoebozoa\\1.blastp.result.tsv",
				"The blast result file");
		mapProducer.addKeyValueEntryBean("diamond.fmt6.result.file", "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Holzem et al., 2024_used_to_judge_homo\\process\\passing_by_similarity\\dir_to_run_mine_verySensitive\\Amoebozoa\\1.diamond.out.very.sensitive.tsv",
				"The diamond result file");
		mapProducer.addKeyValueEntryBean("query.pfam.scan.path", "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Holzem et al., 2024_used_to_judge_homo\\process\\passing_by_similarity\\dir_to_run_mine_blastp_EDA_no_clan_overlap\\Amoebozoa\\3.wnt_candidates_domain.tbl",
				"The query pfam scan result file");
		mapProducer.addKeyValueEntryBean("subject.pfam.scan.path", "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Wnt_QuerySequences\\compoentSet4\\set4\\pfam_scan_wntComp_domain_rawOut.txt",
				"The subject pfam scan result file");
		mapProducer.addKeyValueEntryBean("subject.name", "PRICKLE1",
				"Subject name");
		mapProducer.addKeyValueEntryBean("query.names", "XP_645632.1;XP_646057.1",
				"One or two query names");
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		PairwiseHomologyData pairwiseHomologyData = new PairwiseHomologyData();
		pairwiseHomologyData.setQueryFastaPath(o.getSimplifiedString("query.fasta.file"));
		pairwiseHomologyData.setSubjectFastaPath(o.getSimplifiedString("subject.fasta.file"));
		pairwiseHomologyData.setBlastFmt6ResultPath(o.getSimplifiedStringWithDefault("blast.fmt6.result.file"));
		pairwiseHomologyData.setDiamondFmt6ResultPath(o.getSimplifiedStringWithDefault("diamond.fmt6.result.file"));
		pairwiseHomologyData.setPfamScanResultQueryPath(o.getSimplifiedString("query.pfam.scan.path"));
		pairwiseHomologyData.setPfamScanResultSubjectPath(o.getSimplifiedString("subject.pfam.scan.path"));
		pairwiseHomologyData.setSubjectName(o.getSimplifiedString("subject.name"));

		String simplifiedString = o.getSimplifiedString("query.names");
		String[] strings = EGPSStringUtil.split(simplifiedString, ';');
		if (strings.length == 1) {
			pairwiseHomologyData.setQueryName1(strings[0]);
			pairwiseHomologyData.setQueryName2(null);
		} else if (strings.length == 2) {
			pairwiseHomologyData.setQueryName1(strings[0]);
			pairwiseHomologyData.setQueryName2(strings[1]);
		}else {
			throw new IllegalArgumentException("Error the query.names parameters.");
		}

		mainFace.resetData(pairwiseHomologyData);

	}

}
