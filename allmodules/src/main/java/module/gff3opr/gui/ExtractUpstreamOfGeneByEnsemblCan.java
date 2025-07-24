package module.gff3opr.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableInt;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;

import utils.EGPSFileUtil;
import utils.string.StringCounter;
import module.genome.GenomicRange;
import module.gff3opr.model.FeatureList;
import module.gff3opr.model.GFF3Feature;
import module.gff3opr.model.YuGFF3Parser;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModuleRunner;

public class ExtractUpstreamOfGeneByEnsemblCan implements SubTabModuleRunner {

	private final YuGFF3Parser yuGFF3Parser = new YuGFF3Parser();
	private final StringCounter stringCounter = new StringCounter();

	private int upstreamBasePair;
	private int downstreamBasePair;

	@Override
	public String getShortDescription() {
		return "<html>Gene defined as protein coding gene and lncRNA<br>The alternative splicing is take into consideration.<br>As for the alternative splicing, use the transcript has the Ensembl Canonical Tag.";
	}

	@Override
	public String getTabName() {
		return "2.3 Extract the upstream of gene with Ensembl Tag";
	}

	@Override
	public void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.gff3.file", "",
				"Input the file path of the gff3 file. Common compressed format xz,gz,zip is supported.");
		mapProducer.addKeyValueEntryBean("output.bed.file", "", "output file path");
		mapProducer.addKeyValueEntryBean("upstream.base.pair", "2000",
				"The base pair upstream the TSS (Transcription start site)");
		mapProducer.addKeyValueEntryBean("downstream.base.pair", "50",
				"The base pair downstream the TSS (Transcription start site)");
		mapProducer.addKeyValueEntryBean("^", "", "");
		mapProducer.addKeyValueEntryBean("feature.end.directive", "###",
				"This directive (three # signs in a row, officail gff3 file) indicates that all forward references to feature IDs that have been seen to this point have been resolved. ");

	}

	@Override
	public List<String> execute(OrganizedParameterGetter o) throws Exception {
		String inputGFF3File = o.getSimplifiedString("input.gff3.file");
		String outputFilePath = o.getSimplifiedString("output.bed.file");
		int upstreamBasePair = o.getSimplifiedInt("upstream.base.pair");
		int downstreamBasePair = o.getSimplifiedInt("downstream.base.pair");
		String featureEndDirective = o.getSimplifiedString("feature.end.directive");


		Stopwatch stopwatch = Stopwatch.createStarted();
		perform(inputGFF3File, outputFilePath, upstreamBasePair, downstreamBasePair, featureEndDirective);

		stopwatch.stop();
		List<String> outputs = new LinkedList<>();

		Map<String, MutableInt> counterMap = stringCounter.getCounterMap();
		for (Entry<String, MutableInt> entry : counterMap.entrySet()) {
			outputs.add(entry.getKey() + "        " + entry.getValue());
		}

		outputs.add("Elapsed time: ".concat(stopwatch.toString()));

		return outputs;

	}

	void perform(String inputGFF3File, String outputFilePath, int upstreamBasePair, int downstreamBasePair,
			String featureEndDirective) throws IOException {

		stringCounter.clear();

		this.upstreamBasePair = upstreamBasePair;
		this.downstreamBasePair = downstreamBasePair;

		int sz = 100 * 1024 * 1024;
		int length = featureEndDirective.length();

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(inputGFF3File);

		List<String> featureRecords = new LinkedList<>();

		BufferedWriter wr = new BufferedWriter(new FileWriter(outputFilePath));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {

				if (readLine.length() == length && Objects.equal(featureEndDirective, readLine)) {
					processAllRecordsOfFeature(featureRecords, wr);
					// release the records
					featureRecords.clear();
				} else if (readLine.startsWith("#")) {
					// not do anything
				} else {
					featureRecords.add(readLine);
				}

			}
		}

		if (!featureRecords.isEmpty()) {
			processAllRecordsOfFeature(featureRecords, wr);
		}
		wr.close();

	}

	private void processAllRecordsOfFeature(List<String> featureRecords, BufferedWriter writer) throws IOException {
		if (featureRecords.isEmpty()) {
			throw new InputMismatchException("It seems the input gff3 file is not correct.");
		}

		ListIterator<String> iterator = featureRecords.listIterator();
		// 第一个没有 Parent 属性的

		GFF3Feature firstFeature = null;
		FeatureList featureList = new FeatureList();

		while (iterator.hasNext()) {
			String next = iterator.next();
			GFF3Feature feature = yuGFF3Parser.getTheFeature(next);
			if ("biological_region".equals(feature.type())) {
				continue;
			}
			featureList.add(feature);
			if (firstFeature == null && feature.getAttribute("Parent") == null) {
				firstFeature = feature;
			}
		}

		if (firstFeature == null) {
			return;
		}

		boolean isProteinCodingGene = true;
		if (Objects.equal(firstFeature.type(), "gene")
				&& Objects.equal(firstFeature.getAttribute("biotype"), "protein_coding")) {
			isProteinCodingGene = true;
		} else if (Objects.equal(firstFeature.type(), "ncRNA_gene")
				&& Objects.equal(firstFeature.getAttribute("biotype"), "lncRNA")) {
			isProteinCodingGene = false;
		} else {
			return;
		}

		@SuppressWarnings("unused")
		int rangeOfTSS = 0;

		boolean alreadyHasOneEnsemblCan = false;
		GFF3Feature choosedFeature = null;

		String firstFeatureID = firstFeature.getAttribute("ID");
		// 竟然发现有些 基因的转录本是 ensembl_canonical但是他的type是transcript
		//
		FeatureList transcriptsFeatures = featureList.selectByAttribute("Parent", firstFeatureID);
		final String ensemblCanonicalTag = "Ensembl_canonical";
		if (isProteinCodingGene) {

			int transcriptsSize = transcriptsFeatures.size();
			if (featureList.selectByType("mRNA").isEmpty()) {
				System.err.println(firstFeature.getAttribute("ID")
						+ "\tis marked as protein_coding, but do not has mRNA transcript. Warnings");
			}

			int ensemblCanIndex = -1;
			int tempCount = 0;
			for (GFF3Feature gff3Feature : transcriptsFeatures) {
				tempCount++;
				String nameAttribute = gff3Feature.getAttribute("Name");
				if (nameAttribute == null) {
					nameAttribute = gff3Feature.getAttribute("ID");
				}
				String tagAttribute = gff3Feature.getAttribute("tag");
				if (tagAttribute == null) {
					tagAttribute = "NO-TAG";
				}

				if (tagAttribute.contains(ensemblCanonicalTag)) {
					ensemblCanIndex = tempCount;

					if (alreadyHasOneEnsemblCan) {
						System.err.println(nameAttribute.concat("\tGene has not Ensembl_canonical transcript."));
					}

					alreadyHasOneEnsemblCan = true;
					choosedFeature = gff3Feature;
				}
			}

		} else {
			int transcriptsSize = transcriptsFeatures.size();
			if (featureList.selectByType("lnc_RNA").isEmpty()) {
				System.err.println(
						firstFeature.getAttribute("ID")
								+ "\tis marked as ncRNA, but do not has lnc_RNA transcript. Warnings");
			}

			int ensemblCanIndex = -1;
			int tempCount = 0;
			for (GFF3Feature gff3Feature : transcriptsFeatures) {
				tempCount++;
				String nameAttribute = gff3Feature.getAttribute("Name");
				if (nameAttribute == null) {
					nameAttribute = gff3Feature.getAttribute("ID");
				}
				String tagAttribute = gff3Feature.getAttribute("tag");
				if (tagAttribute == null) {
					tagAttribute = "NO-TAG";
				}

				if (tagAttribute.contains(ensemblCanonicalTag)) {
					ensemblCanIndex = tempCount;

					if (alreadyHasOneEnsemblCan) {
						System.err.println(nameAttribute.concat("\tGene has not Ensembl_canonical transcript."));
					}

					alreadyHasOneEnsemblCan = true;
					choosedFeature = gff3Feature;
				}
			}

		}

		if (!alreadyHasOneEnsemblCan) {
			throw new InputMismatchException(
					"Sorry, this gene do not has the Ensembl Can. tag. ".concat(firstFeature.toString()));
		}

		outputTheFeatureInfo(firstFeature, choosedFeature, writer);

	}

	/**
	 * <pre>
	 * For check: AXIN2 SP5 NKD1
	 * SPATS2L: + SPATS2L-204 200306693
	 * AXIN2 - AXIN2-201 65561648
	 * SP5 + 170715337
	 * NKD1 + NKD1-201 50548396
	 * </pre>
	 * 
	 * @param firstFeature
	 * @param ensemblCanFeature
	 * @param wr
	 * @throws IOException
	 */
	private void outputTheFeatureInfo(GFF3Feature firstFeature, GFF3Feature ensemblCanFeature, BufferedWriter wr)
			throws IOException {

		stringCounter.addOneEntry(firstFeature.getAttribute("biotype"));

		char bioStrand = firstFeature.location().bioStrand();

		int leftOneBasedPosition = 0;
		int rightOneBasePosition = 0;

		GenomicRange genomicRange = ensemblCanFeature.location();
		if (bioStrand == '+') {
			leftOneBasedPosition = genomicRange.bioStart() - upstreamBasePair;
			rightOneBasePosition = genomicRange.bioStart() + downstreamBasePair;
		} else {
			leftOneBasedPosition = genomicRange.bioEnd() - downstreamBasePair;
			rightOneBasePosition = genomicRange.bioEnd() + upstreamBasePair;
		}

		if (leftOneBasedPosition < 1) {
			leftOneBasedPosition = 1;
		}

		wr.write(firstFeature.seqname());
		wr.write('\t');
		wr.write(String.valueOf(leftOneBasedPosition - 1));
		wr.write('\t');
		wr.write(String.valueOf(rightOneBasePosition));
		wr.write('\t');
		wr.write(bioStrand);
		wr.write('\t');
		wr.write(firstFeature.type());
		wr.write('\t');
		wr.write(firstFeature.getAttribute("ID"));
		wr.write('\t');
		String nameAttr = firstFeature.getAttribute("Name");
		if (nameAttr == null) {
			nameAttr = "NA";
		}
		wr.write(nameAttr);
		wr.write('\n');

	}
}
