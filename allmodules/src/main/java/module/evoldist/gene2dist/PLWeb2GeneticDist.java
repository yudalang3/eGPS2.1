package module.evoldist.gene2dist;

import java.util.HashMap;

import javax.swing.SwingUtilities;

import module.evoldist.operator.DistanceCalculateor;
import module.evoldist.view.gui.DistanceMatrixParameterMain;
import module.evoldist.view.gui.EGPSDistanceMatrixJTableHolder;
import module.evoltrepipline.ParameterAssigner;

/**
 * 
 * A pipeline from obtaining data from REST, calculate genetic distance and
 * finally executing the operations !
 * 
 * @ClassName: GeneToGeneticDistAndJump.java
 * 
 * @Package: egps.remnant.geneToGeneticDist
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-03-21 08:53
 * 
 */
public class PLWeb2GeneticDist extends PLWeb2AlignmentView_defaultnewTab {

	public PLWeb2GeneticDist(HashMap<String, String> t, String geneSymbol) {
		super(t, geneSymbol);
	}

	public PLWeb2GeneticDist(HashMap<String, String> t, String region, boolean hasGenomicRegion) {
		super(t, region, hasGenomicRegion);
	}

	private DistanceMatrixParameterMain geneToGeneticDistMain;

	private double[][] finalDistance = null;

	public void setGeneToGeneticDistMain(DistanceMatrixParameterMain geneToGeneTreeMain) {
		this.geneToGeneticDistMain = geneToGeneTreeMain;
	}

	@Override
	public int processNext() throws Exception {
		switch (processIndex) {
		case 1:
			if (internally_invoke_restWeb2Alignment()) {
			} else {
				processIndex++;
			}
			return restAlignment.getCurrentProgressIndex() + processIndex - 1;
		case 2:
			get_seqs_seqsNames();
			processIndex++;
			return getAlreadyProgressed() + processIndex - 1;
		case 3:
			calculate_evolutionary_distance();
			processIndex++;
			return getAlreadyProgressed() + processIndex - 1;

		default:
			return PROGRESS_FINSHED;
		}
	}

	private void calculate_evolutionary_distance() throws Exception {
		DistanceCalculateor distanceCalculateor = new DistanceCalculateor(seqs, seqNames);

		ParameterAssigner.parameterFactorForDCalculator(distanceCalculateor, settingValues);
		finalDistance = distanceCalculateor.getFinalDistance();
	}

	@Override
	public int getTotalSteps() {
		return 1 + super.getTotalSteps();
	}

	@Override
	public void actionsAfterFinished() throws Exception {

		SwingUtilities.invokeLater(() -> {
			if (finalDistance != null) {

				String[] array = seqNames.stream().toArray(String[]::new);
				EGPSDistanceMatrixJTableHolder jtable = new EGPSDistanceMatrixJTableHolder(finalDistance, array);
				jtable.setNumberAfterTheDecimalPoint(geneToGeneticDistMain.getDecimalPlacesValue());
				jtable.setDispalyedLayoutStyle(geneToGeneticDistMain.getMatrixIndex());
				jtable.setDisplayedCorlorMode(geneToGeneticDistMain.getDisplayedColorModeIndex());
				jtable.updateJtableAndLegendState();

				geneToGeneticDistMain.setJtable(jtable);
				;
				geneToGeneticDistMain.getTabbedPanel().removeAll();
				geneToGeneticDistMain.getTabbedPanel().add(jtable);
			}
		});
	}

}
