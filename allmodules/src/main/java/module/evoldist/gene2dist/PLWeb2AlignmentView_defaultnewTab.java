package module.evoldist.gene2dist;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;

import geneticcodes.GeneticCode;
import egps2.panels.dialog.SwingDialog;
import module.evoltrepipline.ConstantNameClass_GeneticCode;
import module.evoltrepipline.ConstantNameClass_SpeciesSet;
import module.evoltrepipline.ConstantNameClass_WebConnection;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import module.webmsaoperator.webIO.RestGRegion2MSAMultiStep;
import module.webmsaoperator.webIO.RestGenomicRegionMultiStep;
import module.webmsaoperator.webIO.RestWeb2AlignmentMultiStep;
import module.webmsaoperator.webIO.gene.EmployedRegionNotAvailable;
import egps2.modulei.RunningTask;

/**
 * 
 * <p>
 * Description: ElegantJTable pipeline from obtaining data from REST and finally executing
 * the operations !
 * </p>
 * 
 * 从数据库得到联配
 * 
 * @author yudalang
 * @date 2018-11-9
 * @date 2024-04-27
 */
public class PLWeb2AlignmentView_defaultnewTab implements RunningTask {
	// Final variables
	// ##########################################################
	protected List<String> seqs; // ###########################
	protected List<String> seqNames;// ###########################
	// ##########################################################

	protected RestWeb2AlignmentMultiStep restAlignment;

	protected int processIndex = 1;
	protected final boolean shouldGetGenomicRegion;
	protected final HashMap<String, String> settingValues;

	protected String currentTabName;
	/** This is also a flag to indicate what resource to get alignment! */
	protected String speciesSetGroup;

	/**
	 * 
	 * @param geneSymbol      e.g. "Cyhr1"
	 *
	 */
	public PLWeb2AlignmentView_defaultnewTab(HashMap<String, String> t, String geneSymbol) {

		int timeoutMilliseconds = Integer.parseInt(t.get(new ConstantNameClass_WebConnection().label1_timeOut)) * 1000;

		ConstantNameClass_SpeciesSet cc = new ConstantNameClass_SpeciesSet();
		String speciesName = t.get(cc.label3_queryGenome).replaceAll("\\s+", "_");
		speciesSetGroup = t.get(cc.category1_speciesSetForMSA);
		int exonIndex = Integer.parseInt(t.get(cc.category2_consideredGeneRegions));
		this.settingValues = t;

		restAlignment = new RestWeb2AlignmentMultiStep(timeoutMilliseconds, speciesName, geneSymbol, speciesSetGroup,
				exonIndex);
		shouldGetGenomicRegion = true;
		configGeneticCodeTable(t.get(new ConstantNameClass_GeneticCode().label1_geneticCodeTable));
	}

	private void configGeneticCodeTable(String tableName) {
		GeneticCode codeTable = GeneticCode.geneticCodeFactory(tableName);
		restAlignment.setGeneticCode(codeTable);

	}

	/**
	 * Strings for test!! "homo_sapiens", "primates", "8:144449582-144465677:-1"
	 */
	public PLWeb2AlignmentView_defaultnewTab(HashMap<String, String> t, String region, boolean hasGenomicRegion) {

		int timeoutMilliseconds = Integer.parseInt(t.get(new ConstantNameClass_WebConnection().label1_timeOut)) * 1000;
		ConstantNameClass_SpeciesSet cc = new ConstantNameClass_SpeciesSet();
		String speciesName = t.get(cc.label3_queryGenome).replaceAll("\\s+", "_");
		speciesSetGroup = t.get(cc.category1_speciesSetForMSA);
		this.settingValues = t;

		restAlignment = new RestWeb2AlignmentMultiStep(timeoutMilliseconds, speciesName, speciesSetGroup, region);
		shouldGetGenomicRegion = false;
	}

	public void setTabName(String currentName) {
		this.currentTabName = currentName;
	}

	@Override
	public int getTotalSteps() {
		return 1 + restAlignment.getNeededSteps();
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

		default:
			return PROGRESS_FINSHED;
		}
	}

	protected void get_seqs_seqsNames() throws Exception {
		seqNames = restAlignment.getSeq_names();
		seqs = restAlignment.getSeqsIfNeedToJoin();

		if (seqs == null) {
			SwingDialog.showErrorMSGDialog("Get alignmnet error!", "Oos! eGPS finally can't get sequences properly!");

			throw new Exception("Oos! eGPS finally can't get sequences properly!");
		}
	}

	protected boolean internally_invoke_restWeb2Alignment() throws Exception {

		boolean ret = true;
		try {
			ret = restAlignment.getAlignmentFromWebMultiStep();
		} catch (UnknownHostException e) {
			SwingDialog.showErrorMSGDialog("Network error!",
					"eGPS can't connect to Internet. Please check your network!");
			throw e;
		} catch (java.net.ConnectException | java.net.SocketTimeoutException e) {
			SwingDialog.showErrorMSGDialog("Time out!",
					"eGPS can't connect to Internet time out. Please check your network or set timeout value larger!");
			throw e;
		} catch (SocketException e) {
			SwingDialog.showErrorMSGDialog("Time out!",
					"eGPS can't connect to Internet time out. Please check your network or set timeout value larger!");
			throw e;
		} catch (IOException e) {
			String message = e.getMessage();
			if (message.contains("503")) {
				SwingDialog.showErrorMSGDialog("Network error!",
						"eGPS can't get sequence alignment from web properly. \n"
								+ "Because the remote Ensembl service is Unavailable!\nPlease try again later!");
			} else {

				SwingDialog.showErrorMSGDialog("Network error!",
						"eGPS can't get sequence alignment from web properly!\n"
								+ "Please check your query infromation or your web environment!\n");
			}
			throw e;
		} catch (EmployedRegionNotAvailable e) {
			SwingDialog.showErrorMSGDialog(e.getTitle(), e.getInformation());
			throw e;
		}

		return ret;
	}

	protected int getAlreadyProgressed() {
		int ret = 0;
		if (shouldGetGenomicRegion) {
			ret += RestGenomicRegionMultiStep.neededTotalSteps;
		}

		ret += RestGRegion2MSAMultiStep.neededTotalSteps;

		return ret;
	}

	public List<String> getSeqNames() {
		return seqNames;
	}

	@Override
	public void actionsBeforeStart() throws Exception {
	}

	@Override
	public void actionsAfterFinished() throws Exception {

		List<SequenceI> vector = new ArrayList<>();
		for (int i = 0; i < seqs.size(); i++) {
			Sequence sequence = new Sequence(seqNames.get(i), seqs.get(i));
			vector.add(sequence);
		}

		final SequenceDataForAViewer sequenceData = new SequenceDataForAViewer(vector);

//		BioMainFrame bmf = BioMainFrame.getInstance();
//		JideTabbedPane tabbedPane = bmf.getTabbedPane();
//		SwingUtilities.invokeLater(() -> {
//
//			VisulizationDataProperty alignmentViewPort = new VisulizationDataProperty(sequenceData);
//			AlignmentViewMain aMain = new AlignmentViewMain();
//
//			aMain.setAlignmentViewPort(alignmentViewPort);
//
//			aMain.setAlignmentLayout();
//
//			tabbedPane.add(currentTabName, aMain);
//			tabbedPane.setSelectedComponent(aMain);
//			tabbedPane.getSelectedComponent().requestFocus();
//
//		});
	}

	@Override
	public boolean isTimeCanEstimate() {
		// when use eGPS Cloud, we can't estimate time!

		if ("100 vertebrate species".equalsIgnoreCase(speciesSetGroup)) {
			return false;
		}

		return true;
	}

	/*
	 * public static void main(String[] args) { EGPSThreadUtilForTest
	 * .quicklyRunThread(new PLWeb2AlignmentView_newTab(15000, "homo_sapiens",
	 * "primates", "7:55019017-55211628:1")); }
	 */
}
