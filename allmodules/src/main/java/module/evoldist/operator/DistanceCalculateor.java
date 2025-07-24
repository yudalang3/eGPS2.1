package module.evoldist.operator;

import java.util.Arrays;
import java.util.List;

import egps2.panels.dialog.SwingDialog;
import module.evoldist.operator.pairdist.AlignmentEvoDistance;
import module.evoltrepipline.PairEvoDistance;
import msaoperator.alignment.AlignmentPreprocesser;

/**
 * Usage:
 * <pre>
 * String[] seqs = {"ACT","ACG"};
		String[] seqNames = {"seq1","seq2"};
		DistanceCalculateor distanceCalculateor = new DistanceCalculateor(seqs, seqNames);
		distanceCalculateor.initDistance(PairEvoDistance.JC69_MODEL);
 * </pre>
 * 
 * @author yudalang
 * @Date 2019-08-31
 *
 */
public class DistanceCalculateor extends AlignmentPreprocesser{

	protected AlignmentEvoDistance<?> alignmentDists;
	
	public DistanceCalculateor() {}
	
	public DistanceCalculateor(String[] sequences, String[] seqNames) {
		super(sequences,seqNames);
	}
	
	public DistanceCalculateor(List<String> seqs, List<String> seq_names) {
		super(seqs,seq_names);
	}
	
	public void initDistance(String distName,boolean ifAB) {
		alignmentDists = new AlignmentEvoDistance<>(distName,ifAB);
	}
	
	protected void validateMatrix(double[][] dist) {
		boolean[] eval = EvoDistanceUtil.evaluateDistM(dist);

		if (!eval[0]) {
			SwingDialog.showErrorMSGDialog("Genetic distance error",
					"All elements in the evolutionary distance matrix are zero!\n"
							+ "If your parameter to deal with deletion is \"Compelete deletion\",you may using other settings!"
							+ "\nElse you can change your input string!");
		}
	}
	public double[][] getFinalDistance() throws Exception {
		String[] processedAlignment = getProcessedAlignment();
		alignmentDists.reSetSeqs(processedAlignment);
		

		double[][] dist = alignmentDists.getEvoDistances();
		validateMatrix(dist);
		return dist;
	}
	public static void main(String[] args) throws Exception {
		String[] seqs = {"ACT","ACG"};
		String[] seqNames = {"seq1","seq2"};
		DistanceCalculateor distanceCalculateor = new DistanceCalculateor(seqs, seqNames);
		distanceCalculateor.initDistance(PairEvoDistance.JC69_MODEL,false);
		double[][] d = distanceCalculateor.getFinalDistance();
		for (double[] es : d) {
			System.err.println(Arrays.toString(es));
		}
	}
}
