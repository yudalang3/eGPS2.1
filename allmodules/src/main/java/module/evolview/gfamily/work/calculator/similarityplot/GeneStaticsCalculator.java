package module.evolview.gfamily.work.calculator.similarityplot;

import java.util.ArrayList;
import java.util.List;

import phylo.msa.util.EvolutionaryProperties;
import module.evolview.model.AlignmentGetSequence;
import msaoperator.alignment.sequence.SequenceI;

public class GeneStaticsCalculator {
	/**
		 * 如何想生成json恢复代码！
		 * 
		 * @param getSequence
		 * @param windowStep
		 * @param windowSize
		 * @return
		 */
		public static SimilarityPlotData getSimplotData(AlignmentGetSequence getSequence, int windowStep, int windowSize) {
			char[] refSeqence = getSequence.getRefSequence().getSeq();
			int geneLength = 0;
			for (char c : refSeqence) {
				if (c != EvolutionaryProperties.GAP_CHAR) {
					geneLength++;
				}
			}

			List<SequenceI> returenedDNASequence = getSequence.returnDNASequence(0,geneLength);
//			getSequence.ridGap(returenedDNASequence);
			SequenceI refSequence = returenedDNASequence.get(0);
			List<SequenceI> altSequence = returenedDNASequence.subList(1, returenedDNASequence.size());
	
			CalculatorSimilarity calculatorSimilarity = new CalculatorSimilarity();
			calculatorSimilarity.setWindowProperty(windowStep, windowSize);
			int[] comparedSeqIndexes = new int[] { 1, 2, 3, 4, 5 };
			calculatorSimilarity.setSequencesForAnalysis(0, comparedSeqIndexes);
	
			List<String> seqNames = new ArrayList<String>();
			List<String> seqeuences = new ArrayList<String>();
			seqNames.add(refSequence.getSeqName());
			seqeuences.add(refSequence.getSeqAsString());
			for (SequenceI seq : altSequence) {
				seqNames.add(seq.getSeqName());
				seqeuences.add(seq.getSeqAsString());
			}
			calculatorSimilarity.setSequences(seqNames, seqeuences);
			SimilarityPlotData simPlotData = calculatorSimilarity.calculateSimilarity();
	
			// 下面的代码用以产生json数据，不要删除。
	//		SimPlotJsonData simPlotJsonData = new SimPlotJsonData();
	//		simPlotJsonData.setxAxisIntegers(simPlotData.getxAxisIntegers());
	//		List<egps.remnant.ncov.calculator.simplot.ComparedSequenceResultJson> comparedSequenceResultJsons = new ArrayList<>();
	//		List<ComparedSequenceResult> comparedSequenceResults = simPlotData.getComparedSequenceResults();
	//		for (ComparedSequenceResult comparedSequenceResult : comparedSequenceResults) {
	//			egps.remnant.ncov.calculator.simplot.ComparedSequenceResultJson tt = new egps.remnant.ncov.calculator.simplot.ComparedSequenceResultJson();
	//			tt.setName(comparedSequenceResult.getName());
	//			tt.setyAxis(comparedSequenceResult.getyAxis());
	//			comparedSequenceResultJsons.add(tt);
	//		}
	//		simPlotJsonData.setComparedSequenceResults(comparedSequenceResultJsons);
	//		
	//		JSONObject jsonObject = JSONObject.fromObject(simPlotJsonData);
	//		String string = jsonObject.toString();
	//		try {
	//			FileUtils.writeStringToFile(new File("G:/json/example1.json"), string);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	
			return simPlotData;
		}
}

