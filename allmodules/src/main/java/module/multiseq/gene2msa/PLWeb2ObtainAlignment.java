package module.multiseq.gene2msa;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import egps2.panels.dialog.SwingDialog;
import egps2.frame.MainFrameProperties;
import module.evoltrepipline.ConstantNameClass_GeneticCode;
import module.evoltrepipline.ConstantNameClass_SpeciesSet;
import module.evoltrepipline.ConstantNameClass_WebConnection;
import geneticcodes.GeneticCode;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.Launcher4ModuleLoader;
import module.multiseq.alignment.view.MS2AlignmentUtil;
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
public class PLWeb2ObtainAlignment implements RunningTask {
	// Final variables
	// ##########################################################
	protected List<String> seqs; // ###########################
	protected List<String> seqNames;// ###########################
	// ##########################################################

	protected RestWeb2AlignmentMultiStep restAlignment;

	protected int processIndex = 1;
	protected final boolean shouldGetGenomicRegion;
	protected final Map<String, String> settingValues;

	protected String currentTabName;
	/** This is also a flag to indicate what resource to get alignment! */
	protected String speciesSetGroup;

	protected final ConstantNameClass_SpeciesSet constantOfSpeciesSet = new ConstantNameClass_SpeciesSet();

	protected Optional<File> outputFastaFile = Optional.empty();

	/**
	 * 现在参数都放在Map里面了，这样传入的参数少一点
	 * 
	 * @param speciesName     e.g. "homo_sapiens"
	 * @param geneSymbol      e.g. "Cyhr1"
	 * @param speciesSetGroup e.g. "primates"
	 * @param expandIndex     e.g. "0/1"
	 * 
	 */
	public PLWeb2ObtainAlignment(Map<String, String> t, String geneSymbol) {

		int timeoutMilliseconds = Integer.parseInt(t.get(new ConstantNameClass_WebConnection().label1_timeOut)) * 1000;

		String speciesName = t.get(constantOfSpeciesSet.label3_queryGenome).replaceAll("\\s+", "_");
		speciesSetGroup = t.get(constantOfSpeciesSet.category1_speciesSetForMSA);
		int exonIndex = Integer.parseInt(t.get(constantOfSpeciesSet.category2_consideredGeneRegions));
		this.settingValues = t;

		restAlignment = new RestWeb2AlignmentMultiStep(timeoutMilliseconds, speciesName, geneSymbol, speciesSetGroup,
				exonIndex);
		shouldGetGenomicRegion = true;
		configGeneticCodeTable(t.get(new ConstantNameClass_GeneticCode().label1_geneticCodeTable));
	}

	/**
	 * Strings for test!! "homo_sapiens", "primates", "8:144449582-144465677:-1"
	 * 
	 * @param t
	 * @param region
	 * @param hasGenomicRegion 这个参数主要是为了有多态，因为构造函数也是一种设计模式
	 */
	public PLWeb2ObtainAlignment(Map<String, String> t, String region, boolean hasGenomicRegion) {

		int timeoutMilliseconds = Integer.parseInt(t.get(new ConstantNameClass_WebConnection().label1_timeOut)) * 1000;
		String speciesName = t.get(constantOfSpeciesSet.label3_queryGenome).replaceAll("\\s+", "_");
		speciesSetGroup = t.get(constantOfSpeciesSet.category1_speciesSetForMSA);
		this.settingValues = t;

		restAlignment = new RestWeb2AlignmentMultiStep(timeoutMilliseconds, speciesName, speciesSetGroup, region);
		shouldGetGenomicRegion = false;
	}

	private void configGeneticCodeTable(String tableName) {
		GeneticCode codeTable = GeneticCode.geneticCodeFactory(tableName);
		restAlignment.setGeneticCode(codeTable);

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
		if (outputFastaFile.isPresent()) {
			File file = outputFastaFile.get();
			new MS2AlignmentUtil().quickWriteToFasta(seqNames, seqs, file);
		} else {
			// 导入数据
			List<SequenceI> vector = new ArrayList<>();
			for (int i = 0; i < seqs.size(); i++) {
				Sequence sequence = new Sequence(seqNames.get(i), seqs.get(i));
				vector.add(sequence);
			}
			// 生成一个封装类
			final SequenceDataForAViewer sequenceData = new SequenceDataForAViewer(vector);
			// New出来一个Loader实例
			Launcher4ModuleLoader launcher4ModuleLoader = new Launcher4ModuleLoader();
			// 设置模块是怎么打开的，数据是哪里来的提示符
			launcher4ModuleLoader.setModuleLaunchWay("This remnant is launched by the Gene to MSA function");
			launcher4ModuleLoader
					.setWhatDataInvoked("The data source is from the Ensembl RESTful service or eGSP cloud.");
			// 把包含数据的封装类实例导入
			launcher4ModuleLoader.setSequenceDataForAViewer(sequenceData);
			// 让主框架来加载这个类
			MainFrameProperties.loadTheModuleFromIModuleLoader(launcher4ModuleLoader);
		}

	}

	public void setOutputFile(String path) {
		if (path == null) {
			outputFastaFile = Optional.empty();
		} else {
			outputFastaFile = Optional.of(new File(path));
		}
	}

	@Override
	public boolean isTimeCanEstimate() {
		// when use eGPS Cloud, we can't estimate time!
		if (constantOfSpeciesSet.egpsCloud_value1_1000species.equalsIgnoreCase(speciesSetGroup)) {
			return false;
		}

		return true;
	}

}
