package module.evoldist.msa2distview;

import java.io.File;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import module.evoldist.view.contorl.SaveDistanceMatrix;
import module.evoldist.view.gui.EGPSDistanceMatrixJTableHolder;
import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.io.seqFormat.MSA_DATA_FORMAT;
import msaoperator.io.seqFormat.SequenceParser;
import msaoperator.io.seqFormat.parser.ClustalWParser;
import msaoperator.io.seqFormat.parser.FastaParser;
import msaoperator.io.seqFormat.parser.GCGMSFParser;
import msaoperator.io.seqFormat.parser.MEGAParser;
import msaoperator.io.seqFormat.parser.NEXUSParser;
import msaoperator.io.seqFormat.parser.PAMLParser;
import msaoperator.io.seqFormat.parser.PHYParser;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import egps2.panels.dialog.SwingDialog;


/**
 * 
 * @ClassName BuildPipeForMSA2Dist.java
 * 
 * @Package egps.remnant.geneToGeneticDist
 * 
 * @author ydl,mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-03-29 15:49
 * @modify 2024-04-02
 * 
 */
public class PLMSAFile2DistMatrix extends PLMSA2DistMatrix {

	protected File inputFile = null;
	private MSA2DistImportBean object;
	private MSA2DistanceMatrixViewerMain main;

	public PLMSAFile2DistMatrix(MSA2DistImportBean object) {
		this.object = object;
	}

	@Override
	public void actionsBeforeStart() throws Exception {
		
		File tempFile = new File(object.getFilePath());
		String upperCase = object.getFileFormat();
		MSA_DATA_FORMAT dataFormat = MSA_DATA_FORMAT.getFormatAccording2name(upperCase);

		
		SequenceDataForAViewer parseData = parseData(tempFile, dataFormat);
		
		this.seqNames = parseData.getSequenceNames();
		this.seqs = parseData.getSequences();
		super.actionsBeforeStart();
	}
	
	@Override
	public void actionsAfterFinished() throws Exception {
		double[][] finalDistance = distMatrix;
		String[] otu_names = seqNames.toArray(new String[0]);
		if (!object.isOutputToFile() && this.main != null) {
			SwingUtilities.invokeLater(() -> {

				EGPSDistanceMatrixJTableHolder jtable = new EGPSDistanceMatrixJTableHolder(finalDistance, otu_names);
				jtable.setNumberAfterTheDecimalPoint(main.getDecimalPlacesValue());
				jtable.setDispalyedLayoutStyle(main.getMatrixIndex());
				jtable.setDisplayedCorlorMode(main.getDisplayedColorModeIndex());
				jtable.updateJtableAndLegendState();

				main.setJtable(jtable);
				JPanel tabbedPanel = main.getTabbedPanel();
				tabbedPanel.removeAll();
				tabbedPanel.add(jtable);
				tabbedPanel.revalidate();
			});
		}else {
			SaveDistanceMatrix saver = new SaveDistanceMatrix();
			File file = new File(object.getOutputPath());
			saver.saveAsJTableToDist(file, otu_names, finalDistance);
		}
	}

	public SequenceDataForAViewer parseData(File inputFile,MSA_DATA_FORMAT dataFormat) {
//		ALIGNED_CLUSTALW("ClustalW"), ALIGNED_FASTA("Aligned fasta"), ALIGNED_GCGMSF("GCG MSF"),
//		ALIGNED_PAML("PAML"),
//		ALIGNED_MEGA("MEGA"), ALIGNED_PHYLIP("PHYLIP"), NEXUS_SEQ("NEXUS");

		SequenceParser seqPar = null;

		switch (dataFormat) {
		case ALIGNED_CLUSTALW:
			seqPar = new ClustalWParser(inputFile);
			break;
		case ALIGNED_FASTA:
			seqPar = new FastaParser(inputFile);
			break;
		case ALIGNED_GCGMSF:
			seqPar = new GCGMSFParser(inputFile);
			break;
		case ALIGNED_PAML:
			seqPar = new PAMLParser(inputFile);
			break;
		case ALIGNED_MEGA:
			seqPar = new MEGAParser(inputFile);
			break;
		case ALIGNED_PHYLIP:
			seqPar = new PHYParser(inputFile);
			break;

		default:
			seqPar = new NEXUSParser(inputFile);
			break;
		}

		try {
			seqPar.parse();
		} catch (Exception e1) {
			SwingDialog.showErrorMSGDialog("File error", e1.getMessage());
			return null;
		}

		BasicSequenceData seqElements = (BasicSequenceData) seqPar.getSeqElements();

		return new SequenceDataForAViewer(seqElements.getDataSequences());
	}

	public void setMainFace(MSA2DistanceMatrixViewerMain main) {
		this.main = main;
	}
}
