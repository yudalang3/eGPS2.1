package module.evolview.gfamily.work.calculator.similarityplot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * How to use:
 *
 * CalculatorSimilarity calculatorSimilarity = new CalculatorSimilarity();
 * calculatorSimilarity.setWindowProperty(1, 2);
 * calculatorSimilarity.setSequencesForAnalysis(0, comparedSeqIndexes);
 * calculatorSimilarity.setSequences(seqNames, seqeuences);
 * SimPlotData calculateSimilarity = calculatorSimilarity.calculateSimilarity();
 *
 *
 * </pre>
 */
public class CalculatorSimilarity {

    int windowStep = 100;
    int windowSize = 200;

    List<String> seqNames;
    List<String> seqeuences;

    int analysisSeqIndex;
    int[] comparedSeqIndexes;
    private final char gapChar = '-';
    
    

    public CalculatorSimilarity() {
		super();
	}

	public void setWindowProperty(int windowStep, int windowSize) {
        this.windowStep = windowStep;
        this.windowSize = windowSize;
    }

    public void setSequences(List<String> seqNames, List<String> seqeuences) {
        this.seqNames = seqNames;
        this.seqeuences = seqeuences;
    }

    public void setSequencesForAnalysis(int analysisSeqIndex, int[] comparedSeqIndexes) {
        this.analysisSeqIndex = analysisSeqIndex;
        this.comparedSeqIndexes = comparedSeqIndexes;
    }

    public SimilarityPlotData calculateSimilarity() {
        List<Double> xAxis = new ArrayList<Double>();
        @SuppressWarnings("unchecked")
        List<Double>[] yAxises = new ArrayList[seqeuences.size()];
        for (int i = 0; i < yAxises.length; i++) {
            yAxises[i] = new ArrayList<Double>();
        }


        int orderIgnoreGaps = 0;
        int nextWindowStartLocation = 1;

        String analysisString = seqeuences.get(analysisSeqIndex);
        int lengthInvokeGaps = analysisString.length();
        int lengthWithoutGaps = 0;
        for (int i = 0; i < lengthInvokeGaps; i++) {
            char c = analysisString.charAt(i);
            if (c != gapChar) {
                lengthWithoutGaps++;
            }
        }

        for (int i = 0; i < lengthInvokeGaps; i++) {
            if (analysisString.charAt(i) != gapChar) {
                orderIgnoreGaps++;
            }

            // 这个时候到了一个window该总结的时候
            if (orderIgnoreGaps == nextWindowStartLocation) {

                int beginIndex = i;
                int endIndex = 1 + getWindowEndLocationInvokeGaps(analysisString, beginIndex);

                /**
                 if (endIndex > lengthInvokeGaps) {
                 //当前window的终止点超过 lengthInvokeGaps - 1时退出！
                 System.out.println(getClass()+" endIndex > lengthInvokeGaps ");
                 break;
                 }*/
                parserOneWindow(yAxises, beginIndex, endIndex, analysisString);
                xAxis.add(0.5 * (nextWindowStartLocation + windowSize - 1 + nextWindowStartLocation));

                if (nextWindowStartLocation + windowStep + windowSize - 1 > lengthWithoutGaps) {
                    //下一个window的终止位置大于 lengthWithoutGaps 时退出！
                    break;
                } else {
                    nextWindowStartLocation = nextWindowStartLocation + windowStep;
                }
            }
        }

        SimilarityPlotData ret = new SimilarityPlotData();
        List<ComparedSequenceResult> comparedSequenceResults = new ArrayList<ComparedSequenceResult>();
        Color[] color = {new Color(182, 165, 240), new Color(179, 33, 33), new Color(0, 0, 255), new Color(23, 139, 139), new Color(0, 255, 0), new Color(0, 0, 0)};

        for (int index : comparedSeqIndexes) {
            ComparedSequenceResult result = new ComparedSequenceResult();
            result.setName(seqNames.get(index));
            result.setyAxis(yAxises[index]);
            if (result.getColor() == null) {
                result.setColor(color[index]);
            }

            comparedSequenceResults.add(result);
        }
        ret.setxAxis(xAxis);
        ret.setComparedSequenceResults(comparedSequenceResults);

        return ret;

    }

    /**
     * example: AAAAA and beginindex 0;
     * For window size 3;
     * shoud return 2!
     * <p>
     * if the returned index more than length -1, return length -1!
     *
     * @param analysisString
     * @param beginIndex     : zero based!
     * @return
     */
    protected int getWindowEndLocationInvokeGaps(String analysisString, int beginIndex) {
        int length = analysisString.length();
        int ii = beginIndex;
        int numOfValidateChars = 0;
        while (ii < length) {
            char cc = analysisString.charAt(ii);
            if (gapChar != cc) {
                numOfValidateChars++;

                if (numOfValidateChars == windowSize) {
                    return ii;
                }
            }

            ii++;
        }

        return length - 1;
    }

    private void parserOneWindow(List<Double>[] yAxises, int beginIndex, int endIndex,
                                 String analysisString) {

        String analysisWindowSeq = analysisString.substring(beginIndex, endIndex);

        for (int k : comparedSeqIndexes) {
            String comparedwindowSeq = seqeuences.get(k);
            double identity = getIdentity(analysisWindowSeq, comparedwindowSeq.substring(beginIndex, endIndex));
            yAxises[k].add(identity);
        }
    }

    protected double getIdentity(String analysisWindowSeq, String comparedwindowSeq) {
        int length = analysisWindowSeq.length();
        if (length != comparedwindowSeq.length()) {
            throw new IllegalArgumentException("Input sequence should be equal length!");
        }
        double matchedChars = 0;

        String upperCaseAnalysisWindowSeq = analysisWindowSeq.toUpperCase();
        String upperCaseComparedwindowSeq = comparedwindowSeq.toUpperCase();
        for (int i = 0; i < length; i++) {
            char char1 = upperCaseAnalysisWindowSeq.charAt(i);
            char char2 = upperCaseComparedwindowSeq.charAt(i);
            if (char1 == char2) {
                matchedChars++;
            }
        }

        return matchedChars / length;
    }


}
