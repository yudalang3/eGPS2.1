package module.gff3opr.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableInt;

import com.google.common.base.Objects;

import utils.EGPSFileUtil;
import utils.string.StringCounter;
import egps2.frame.ComputationalModuleFace;
import module.genome.GenomicRange;
import module.gff3opr.model.GFF3Feature;
import module.gff3opr.model.YuGFF3Parser;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;

@SuppressWarnings("serial")
public class ExtractTSSUpstreamOfGene extends DockableTabModuleFaceOfVoice {

	private final YuGFF3Parser yuGFF3Parser = new YuGFF3Parser();
	private final StringCounter stringCounter = new StringCounter();

	private final Comparator<GenomicRange> bioEndComparator = new Comparator<GenomicRange>() {

		@Override
		public int compare(GenomicRange o1, GenomicRange o2) {
			return o1.end - o2.end;
		}

	};

	public ExtractTSSUpstreamOfGene(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	public String getShortDescription() {
		return "Gene defined as protein coding gene and lncRNA<br>The alternative splicing is take into consideration.";
	}

	@Override
	public String getTabName() {
		return "2.2 Extract the TSS upstream region of gene";
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
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
	protected void execute(OrganizedParameterGetter o) throws Exception {
		String inputGFF3File = o.getSimplifiedString("input.gff3.file");
		String outputFilePath = o.getSimplifiedString("output.bed.file");
		int upstreamBasePair = o.getSimplifiedInt("upstream.base.pair");
		int downstreamBasePair = o.getSimplifiedInt("downstream.base.pair");
		String featureEndDirective = o.getSimplifiedString("feature.end.directive");

		stringCounter.clear();

		run(inputGFF3File, outputFilePath, upstreamBasePair, downstreamBasePair, featureEndDirective);

	}

	private void run(String inputGFF3File, String outputFilePath, int upstreamBasePair, int downstreamBasePair,
			String featureEndDirective) throws IOException {

		int sz = 100 * 1024 * 1024;
		int length = featureEndDirective.length();

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(inputGFF3File);

		List<String> featureRecords = new LinkedList<>();

		BufferedWriter wr = new BufferedWriter(new FileWriter(outputFilePath));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {

				if (readLine.length() == length && Objects.equal(featureEndDirective, readLine)) {
					processAllRecordsOfFeature(featureRecords, upstreamBasePair, downstreamBasePair, wr);
					// release the records
					featureRecords.clear();
				} else if (readLine.startsWith("#")) {
					// not do anything
				} else {
					featureRecords.add(readLine);
				}

			}
		}

		processAllRecordsOfFeature(featureRecords, upstreamBasePair, downstreamBasePair, wr);
		wr.close();

		List<String> outputs = new LinkedList<>();

		Map<String, MutableInt> counterMap = stringCounter.getCounterMap();
		for (Entry<String, MutableInt> entry : counterMap.entrySet()) {
			outputs.add(entry.getKey() + "        " + entry.getValue());
		}

		setText4Console(outputs);

	}

	private void processAllRecordsOfFeature(List<String> featureRecords, int upstreamBasePair, int downstreamBasePair,
			BufferedWriter wr) throws IOException {
		if (featureRecords.isEmpty()) {
			throw new InputMismatchException("It seems the input gff3 file is not correct.");
		}

		ListIterator<String> iterator = featureRecords.listIterator();
		// 第一个没有 Parent 属性的

		GFF3Feature firstFeature = null;

		while (iterator.hasNext()) {
			GFF3Feature feature = yuGFF3Parser.getTheFeature(iterator.next());
			if (feature.getAttribute("Parent") == null) {
				firstFeature = feature;
			} else {
				// has Parent attr
				break;
			}
		}
		iterator.previous();

		char bioStrand = firstFeature.location().bioStrand();

		int leftOneBasedPosition = 0;
		int rightOneBasePosition = 0;

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

		stringCounter.addOneEntry(firstFeature.getAttribute("biotype"));

		@SuppressWarnings("unused")
		int rangeOfTSS = 0;
		LinkedList<GenomicRange> tssLocations = new LinkedList<>();

		if (isProteinCodingGene) {
			while (iterator.hasNext()) {
				String next = iterator.next();
				GFF3Feature feature = yuGFF3Parser.getTheFeature(next);
				if (Objects.equal(feature.type(), "mRNA")) {
					tssLocations.add(feature.location());
				}

			}
		} else {
			while (iterator.hasNext()) {
				String next = iterator.next();
				GFF3Feature feature = yuGFF3Parser.getTheFeature(next);
				if (Objects.equal(feature.type(), "lnc_RNA")) {
					tssLocations.add(feature.location());
				}

			}
		}

		if (bioStrand == '+') {
			Collections.sort(tssLocations);

			GenomicRange genomicRange = tssLocations.getLast();

			leftOneBasedPosition = genomicRange.bioStart() - upstreamBasePair;

			GenomicRange last = tssLocations.getLast();

			rightOneBasePosition = last.bioStart() + downstreamBasePair;

			rangeOfTSS = leftOneBasedPosition - tssLocations.getFirst().bioStart() + 1;
		} else {

			Collections.sort(tssLocations, bioEndComparator);
			GenomicRange genomicRange = tssLocations.getFirst();
			GenomicRange last = tssLocations.getLast();

			leftOneBasedPosition = genomicRange.bioEnd() - downstreamBasePair;
			rightOneBasePosition = last.bioEnd() + upstreamBasePair;

			rangeOfTSS = rightOneBasePosition - leftOneBasedPosition + 1;
		}

		if (leftOneBasedPosition < 1) {
			leftOneBasedPosition = 1;
		}

//		wr.write(String.valueOf(rangeOfTSS));
//		wr.write('\t');
//		wr.write(String.valueOf(tssPositionOneBased));
//		wr.write('\t');
		wr.write(firstFeature.seqname());
		wr.write('\t');
		wr.write(String.valueOf(leftOneBasedPosition - 1));
		wr.write('\t');
		wr.write(String.valueOf(rightOneBasePosition));
		wr.write('\t');
		wr.write(bioStrand);
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
