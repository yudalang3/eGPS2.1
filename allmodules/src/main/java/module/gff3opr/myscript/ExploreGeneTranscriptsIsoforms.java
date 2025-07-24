package module.gff3opr.myscript;

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

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;

import utils.EGPSFileUtil;
import analysis.AbstractAnalysisAction;
import module.gff3opr.model.FeatureList;
import module.gff3opr.model.GFF3Feature;
import module.gff3opr.model.YuGFF3Parser;

public class ExploreGeneTranscriptsIsoforms extends AbstractAnalysisAction {

	private final YuGFF3Parser yuGFF3Parser = new YuGFF3Parser();

	private final String inputGFF3File;
	private final String outputFile;

	public ExploreGeneTranscriptsIsoforms() {
		this.inputGFF3File = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\DataBase\\EnsemblDB\\MainSite\\SpeciesColl\\Ensembl_taxon\\20240906\\gff3\\Homo_sapiens.GRCh38.112.gff3.gz";
		this.outputFile = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\基于富集分析的通路激活\\estimate_WntPathway_effects_with_targetGenes\\TargetGeneScan\\human_ExploreGeneTranscriptsIsoforms.tsv";

	}

	@Override
	public void doIt() throws Exception {
		perform(inputGFF3File, outputFile, "###");
	}

	private void perform(String inputGFF3File, String outputFilePath, String featureEndDirective) throws IOException {

		int sz = 100 * 1024 * 1024;
		int length = featureEndDirective.length();

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(inputGFF3File);

		List<String> featureRecords = new LinkedList<>();

		BufferedWriter wr = new BufferedWriter(new FileWriter(outputFilePath));
		wr.write("transcripts\tnumOfTranscripts\tEnsemblCanIndex\tlongestCDS\tindex\tlongestmRNA\tindex\n");
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

	private void processAllRecordsOfFeature(List<String> featureRecords,
			BufferedWriter wr) throws IOException {
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

		@SuppressWarnings("unused")
		int rangeOfTSS = 0;

		if (isProteinCodingGene) {
			FeatureList transcriptsFeatures = featureList.selectByType("mRNA");
			int transcriptsSize = transcriptsFeatures.size();
			if (transcriptsSize < 2) {
				return;
			}

			GFF3Feature geneWithLongestCDS = geneWithLongestCDS(featureList, transcriptsFeatures);
			GFF3Feature geneWithLongestmRNA = geneWithLongestmRNA(transcriptsFeatures);

			int ensemblCanIndex = -1;
			int tempCount = 0;
			boolean alreadyHasOneEnsemblCan = false;
			for (GFF3Feature gff3Feature : transcriptsFeatures) {
				tempCount++;
				String nameAttribute = gff3Feature.getAttribute("Name");
				if (nameAttribute == null) {
					nameAttribute = gff3Feature.getAttribute("ID");
				}
				wr.write(nameAttribute);
				wr.write(";");
				String tagAttribute = gff3Feature.getAttribute("tag");
				if (tagAttribute == null) {
					tagAttribute = "NO-TAG";
				}
				wr.write(tagAttribute);
				wr.write("[");
				wr.write(String.valueOf(tempCount));
				wr.write("|");

				if (tagAttribute.contains("Ensembl_canonical")) {
					ensemblCanIndex = tempCount;

					if (alreadyHasOneEnsemblCan) {
						System.err.println(nameAttribute.concat("\tGene has not Ensembl_canonical transcript."));
					}

					alreadyHasOneEnsemblCan = true;
				}
			}

			wr.write("\t");
			wr.write(String.valueOf(transcriptsSize));

			wr.write("\t");
			wr.write(String.valueOf(ensemblCanIndex));

			wr.write("\t");
			String nameAttribute = geneWithLongestCDS.getAttribute("Name");
			if (nameAttribute == null) {
				nameAttribute = geneWithLongestCDS.getAttribute("ID");
			}
			wr.write(nameAttribute);
			wr.write("\t");
			int indexOf = transcriptsFeatures.indexOf(geneWithLongestCDS);
			wr.write(String.valueOf(indexOf + 1));

			wr.write("\t");
			nameAttribute = geneWithLongestmRNA.getAttribute("Name");
			if (nameAttribute == null) {
				nameAttribute = geneWithLongestmRNA.getAttribute("ID");
			}
			wr.write(nameAttribute);

			wr.write("\t");
			indexOf = transcriptsFeatures.indexOf(geneWithLongestmRNA);
			wr.write(String.valueOf(indexOf + 1));

			wr.write('\n');
		} else {
			if (true) {
				return;
			}
			FeatureList transcriptsFeatures = featureList.selectByType("lnc_RNA");
			int transcriptsSize = transcriptsFeatures.size();
//			if (transcriptsSize < 2) {
//				return;
//			}

			int ensemblCanIndex = -1;
			int tempCount = 0;
			boolean alreadyHasOneEnsemblCan = false;
			for (GFF3Feature gff3Feature : transcriptsFeatures) {
				tempCount++;
				String nameAttribute = gff3Feature.getAttribute("Name");
				if (nameAttribute == null) {
					nameAttribute = gff3Feature.getAttribute("ID");
				}
				wr.write(nameAttribute);
				wr.write(";");
				String tagAttribute = gff3Feature.getAttribute("tag");
				if (tagAttribute == null) {
					tagAttribute = "NO-TAG";
				}
				wr.write(tagAttribute);
				wr.write("[");
				wr.write(String.valueOf(tempCount));
				wr.write("|");

				if (tagAttribute.contains("Ensembl_canonical")) {
					ensemblCanIndex = tempCount;

					if (alreadyHasOneEnsemblCan) {
						System.err.println(nameAttribute.concat("\tGene has not Ensembl_canonical transcript."));
					}

					alreadyHasOneEnsemblCan = true;
				}
			}

			wr.write("\n");

		}


	}

	private GFF3Feature geneWithLongestmRNA(FeatureList transcriptsFeatures) {
		int maxLengthFeature = -100000;
		GFF3Feature ret = null;
		for (GFF3Feature gff3Feature : transcriptsFeatures) {
			int length = gff3Feature.location().length();
			if (length > maxLengthFeature) {
				maxLengthFeature = length;
				ret = gff3Feature;
			}
		}
		return ret;
	}

	private GFF3Feature geneWithLongestCDS(FeatureList featureList, FeatureList transcriptsFeatures) {
		int maxCDSLength = -100000;

		GFF3Feature longestMRNAFeature = null;
		for (GFF3Feature feature : transcriptsFeatures) {
			String attribute = feature.getAttribute("ID");


			FeatureList selectByAttribute = featureList.selectByAttribute("Parent", attribute);

			FeatureList cdsFeatureList = selectByAttribute.selectByType("CDS");
			int featureListLength = cdsFeatureList.getFeatureListLength();

			if (cdsFeatureList.isEmpty()) {
				throw new InputMismatchException("Please check your gff file, no CDS type records found.");
			}
//			GFF3Feature firstCDSFeature = cdsFeatureList.get(0);
//			String protName = firstCDSFeature.getAttribute("protein_id");
//			logger.trace("Gene is {}, This is mRNA {}, CDS feature sum is {} , protName is {} ",
//					feature.getAttribute("Name"), attribute, featureListLength, protName);

			if (featureListLength > maxCDSLength) {
				maxCDSLength = featureListLength;
				longestMRNAFeature = feature;
			}
		}

		return longestMRNAFeature;
	}

	public static void main(String[] args) throws Exception {
		Stopwatch stopwatch = Stopwatch.createStarted();
		new ExploreGeneTranscriptsIsoforms().runIt();
		stopwatch.stop();
		System.out.println("Total Elapsed time: ".concat(stopwatch.toString()));

	}


}
