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

import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
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

@SuppressWarnings("serial")
public class ExtractUpstreamOfTopFeature extends DockableTabModuleFaceOfVoice {

	private final YuGFF3Parser yuGFF3Parser = new YuGFF3Parser();
	private final StringCounter stringCounter = new StringCounter();

	private final Comparator<GenomicRange> bioEndComparator = new Comparator<GenomicRange>() {

		@Override
		public int compare(GenomicRange o1, GenomicRange o2) {
			return o1.end - o2.end;
		}

	};

	public ExtractUpstreamOfTopFeature(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	public String getShortDescription() {
		return "Gene defined as protein coding gene and lncRNA";
	}

	@Override
	public String getTabName() {
		return "2.1 Extract the upstream location of gene";
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
		mapProducer.addKeyValueEntryBean("determine.tss.method", "distal",
				"proximal or distal: for example, a + gene is 1001-2000, mRNA1 is 1001-1900, mRNA2 is 1200-2000, mRNA 3 is 1100-1800.\n# proximal is mRNA2's start site, distal is the mRNA1's start site, while distal is the tss directly return 1001");
		mapProducer.addKeyValueEntryBean("^", "",
				"");
		mapProducer.addKeyValueEntryBean("feature.end.directive", "###",
				"This directive (three # signs in a row, officail gff3 file) indicates that all forward references to feature IDs that have been seen to this point have been resolved. ");

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		String inputGFF3File = o.getSimplifiedString("input.gff3.file");
		String outputFilePath = o.getSimplifiedString("output.bed.file");
		int upstreamBasePair = o.getSimplifiedInt("upstream.base.pair");
		int downstreamBasePair = o.getSimplifiedInt("downstream.base.pair");
		String determineTssMethod = o.getSimplifiedString("determine.tss.method");
		boolean isProximal = false;
		if (Objects.equal("proximal", determineTssMethod)) {
			isProximal = true;
		}
		String featureEndDirective = o.getSimplifiedString("feature.end.directive");

		stringCounter.clear();

		run(inputGFF3File, outputFilePath, upstreamBasePair, downstreamBasePair, isProximal, featureEndDirective);

	}

	private void run(String inputGFF3File, String outputFilePath, int upstreamBasePair, int downstreamBasePair,
			boolean isProximal, String featureEndDirective) throws IOException {

		int sz = 100 * 1024 * 1024;
		int length = featureEndDirective.length();

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(inputGFF3File);

		List<String> featureRecords = new LinkedList<>();

		BufferedWriter wr = new BufferedWriter(new FileWriter(outputFilePath));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {

				if (readLine.length() == length && Objects.equal(featureEndDirective, readLine)) {
					processAllRecordsOfFeature(featureRecords, upstreamBasePair, downstreamBasePair, isProximal, wr);
					// release the records
					featureRecords.clear();
				} else if (readLine.startsWith("#")) {
					// not do anything
				} else {
					featureRecords.add(readLine);
				}


			}
		}

		processAllRecordsOfFeature(featureRecords, upstreamBasePair, downstreamBasePair, isProximal, wr);
		wr.close();


		List<String> outputs = new LinkedList<>();

		Map<String, MutableInt> counterMap = stringCounter.getCounterMap();
		for (Entry<String, MutableInt> entry : counterMap.entrySet()) {
			outputs.add(entry.getKey() + "        " + entry.getValue());
		}

		setText4Console(outputs);

	}

	private void processAllRecordsOfFeature(List<String> featureRecords, int upstreamBasePair, int downstreamBasePair,
			boolean isProximal, BufferedWriter wr) throws IOException {
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
		int tssPositionOneBased = 0;

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
		if (isProximal) {
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
				tssPositionOneBased = genomicRange.bioStart();

				leftOneBasedPosition = tssPositionOneBased - upstreamBasePair;
				rightOneBasePosition = tssPositionOneBased + downstreamBasePair;

				rangeOfTSS = tssPositionOneBased - tssLocations.getFirst().bioStart() + 1;
			} else {
				
				Collections.sort(tssLocations, bioEndComparator);
				GenomicRange genomicRange = tssLocations.getFirst();
				tssPositionOneBased = genomicRange.bioEnd();

				leftOneBasedPosition = tssPositionOneBased - downstreamBasePair;
				rightOneBasePosition = tssPositionOneBased + upstreamBasePair;

				rangeOfTSS = tssLocations.getLast().bioEnd() - tssPositionOneBased + 1;
			}
		} else {
			if (bioStrand == '+') {
				tssPositionOneBased = firstFeature.location().bioStart();

				leftOneBasedPosition = tssPositionOneBased - upstreamBasePair;
				rightOneBasePosition = tssPositionOneBased + downstreamBasePair;
			} else {
				tssPositionOneBased = firstFeature.location().bioEnd();

				leftOneBasedPosition = tssPositionOneBased - downstreamBasePair;
				rightOneBasePosition = tssPositionOneBased + upstreamBasePair;
			}

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
