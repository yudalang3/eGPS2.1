/**  
* <p>Title: BuildTreeMSA.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018年8月24日  
* @version 1.0  

*/
package module.treebuilder.frommaf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import egps2.panels.dialog.SwingDialog;
import module.remnant.mafoperator.mafParser.MafFastaParser;
import module.remnant.treeoperator.AbstructBuildDMTreePipe;
import module.remnant.treeoperator.NodeEGPSv1;
import module.remnant.treeoperator.dmMethod.BuilderForLargefile;
import module.remnant.treeoperator.reconAlgo.NJ;
import module.remnant.treeoperator.reconAlgo.TreeReconMethod;
import egps2.frame.MainFrameProperties;
import module.evoldist.operator.pairdist.EvoDistParamter;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.moderntreeviewer.IndependentModuleLoader;
import module.evoltrepipline.ParameterAssigner;

/**
 * <p>
 * Title: BuildTreeMSA
 * </p>
 * <p>
 * Description: Build tree for multiple sequence alignment.
 * </p>
 * 
 * @author yudalang
 * @date 2018-8-24
 * 
 * 再次重构
 * @data 2024-04-29
 */
public class PLMAF2PhyloTreeBenchMode extends AbstructBuildDMTreePipe {

	protected TreeReconMethod tReconMethod = new NJ();
	protected EvoPairwiseDistMethod pairwiseDis;

	protected NodeEGPSv1 finalTree;
	protected final List<File> inputFiles;

	protected String[] speciesNames = { "panTro4", "hg38", "ponAbe2" };

	protected EvoDistParamter[][] wholePairwiseMiddlePros;
	protected boolean ifBootstrap;

	public PLMAF2PhyloTreeBenchMode(List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}

	public void setSpeciesNames(String[] speciesNames) {
		this.speciesNames = speciesNames;
	}

	@Override
	public boolean isTimeCanEstimate() {
		return false;
	}

	@Override
	public int processNext() throws Exception {

		processForMAF();
		// processForMAF_para();
		return PROGRESS_FINSHED;
	}

	public void setIfBootstrap(boolean ifBootstrap) {
		this.ifBootstrap = ifBootstrap;
	}

	private void processForMAF() throws Exception {

		BuilderForLargefile<?> treeBuild_instance = new BuilderForLargefile<>(speciesNames);
		ParameterAssigner.parameterFactorBootTree(treeBuild_instance, settingValue);
		treeBuild_instance.setIfBootstrap(ifBootstrap);
		treeBuild_instance.initialize();

		for (File file : inputFiles) {
			System.out.println("!!! This is file\t" + file.getName());

			MafFastaParser mfp = new MafFastaParser(file);

			while (mfp.parseBlocks()) {
				Map<String, String> hp = mfp.getBlock().getMap_namesAndSeqs();

				List<String> blockSeqs = new ArrayList<>(40);
				boolean ifContigue = false;
				for (String string : speciesNames) {
					String seqOfInterest = hp.get(string);
					if (seqOfInterest == null) {
						ifContigue = true;
						break;
					} else {
						blockSeqs.add(seqOfInterest);
					}

				}
				if (ifContigue) {
					continue;
				}
				treeBuild_instance.addSeqsForEachBlock(blockSeqs);
			}
			mfp.close();

			// treeBuild_instance.displayWholePairwiseProp();
		}

		if (ifBootstrap) {
			finalTree = treeBuild_instance.runBootstrapAndGetFinalTree();
		} else {
			finalTree = treeBuild_instance.getFinalTree();
		}
	}

	public void processForMAF_para() throws Exception {

		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		/*
		 * BuilderForMAFfile treeBuild_instance = new BuilderForMAFfile(speciesNames);
		 * parameterFactor(treeBuild_instance); treeBuild_instance.initialize(); //This
		 * can be optimazed by implemts clone!!
		 */
		int len = inputFiles.size();
		for (int i = 0; i < len; i++) {

			File file = inputFiles.get(i);
			System.out.println("!!! This is file\t" + file.getName());

			BuilderForLargefile<?> treeBuild_instance = new BuilderForLargefile<>(speciesNames);
			ParameterAssigner.parameterFactorBootTree(treeBuild_instance, settingValue);
			treeBuild_instance.initialize();

			if (i == 0) {
				initMiddleProp();
			}

			RunForOneChrom oneRun = new RunForOneChrom(treeBuild_instance, file);

//			fixedThreadPool.execute(oneRun);
			fixedThreadPool.submit(oneRun);
		}

		fixedThreadPool.shutdown();// you most shutdown else isTerminated() never return true

		while (true) {// wait for all tasks to be finished!
			if (fixedThreadPool.isTerminated()) {
				System.out.println("All threads finished!!");
				break;
			}
		}
		// displayProp();

		// finalTree = finalSummaryTaskToGetTree();
	}

//	private Node finalSummaryTaskToGetTree() throws Exception {
//		// get geneitc distance
//		int nn = speciesNames.length - 1;
//		double[][] distM = new double[nn][];
//		
//		try {
//			for (int i = 0; i < nn; i++) {
//				double[] aRow = new double[i + 1];
//				for (int j = 0; j <= i; j++) {
//					aRow[j] = pairwiseDis.getPairwiseDist(wholePairwiseMiddlePros[i][j]);
//				}
//				distM[i] = aRow;
//			}
//		} catch (ValidateLenZeroException e) {
//			SwingDialog.showMSGDialog_createNewThread("Validate length zero", "Please check your data to make sure:\n"
//					+ "1. your MAF file is large enough, so the validate length is not zero!"
//					+ "2. you may need to change the gap or miss data treatment to pair wise deletion!");
//			throw new Exception("validate length zero!");
//		}
//		
//		
////		for (double[] ds : distM) {
////			for (double d1 : ds) {
////				System.out.print(d1 + "\t");
////			}
////			System.out.println();
////		}
//		// build tree
//		Node ret = tReconMethod.tree(distM, speciesNames);
//
//		// This will be rooted by the mid-point method
//		// http://cabbagesofdoom.blogspot.com/2012/06/how-to-root-phylogenetic-tree.html
//		TreeUtility util = new TreeUtility();
//		ret = util.rootAtMidPoint(ret);
//
//		return ret;
//		
//	}

	@Override
	public void actionsBeforeStart() throws Exception {
	}

	@Override
	public void actionsAfterFinished() throws Exception {
		SwingUtilities.invokeLater(() -> {
			if (finalTree == null) {
				SwingDialog.showErrorMSGDialog("Reconstruction Error!",
						"The pairwise distance of OUTs has NAN!\t" + "Please check you OTUs");
				return;
			}
			
			final GraphicsNode ret = convertTheTree4visualization(finalTree);

			IndependentModuleLoader independentModuleLoader = new IndependentModuleLoader();
			String name = inputFiles.get(0).getName();
			independentModuleLoader.setHowModuleLaunchedWithData("Module launched by pipline MAF to construct the phylogenetic tree.", 
					"Data source is from MAF, first file name is : ".concat(name));
			/**
			 * treeLayoutProperties 可选，如果输入为null，会自动根据 root生成 treeLayoutProperties
			 * 开发者也可以自己生成 TreeLayoutProperties类示例，进而设置一些参数
			 */
			independentModuleLoader.setModuleData(ret, null);
			MainFrameProperties.loadTheModuleFromIModuleLoader(independentModuleLoader);

		});
	}

	/**
	 * Before using this class, you need to ensure <code>pairwiseDis</code> variable
	 * is not null!
	 */
	private void initMiddleProp() {
		wholePairwiseMiddlePros = new EvoDistParamter[speciesNames.length - 1][];
		for (int i = 0; i < wholePairwiseMiddlePros.length; i++) {
			EvoDistParamter[] aRow = new EvoDistParamter[i + 1];

			for (int j = 0; j <= i; j++) {
				// aRow[j] = new EvoDistParamter(pairwiseDis.getDistanceMethodType());
			}
			wholePairwiseMiddlePros[i] = aRow;
		}
	}

	class RunForOneChrom implements Runnable {

		private BuilderForLargefile<?> treeBuild_instance;
		private File inputFile;

		public RunForOneChrom(BuilderForLargefile<?> t, File file) {
			treeBuild_instance = t;
			inputFile = file;
		}

		@Override
		public void run() {
			MafFastaParser mfp = new MafFastaParser(inputFile);

			while (mfp.parseBlocks()) {
				Map<String, String> hp = mfp.getBlock().getMap_namesAndSeqs();

				List<String> blockSeqs = new ArrayList<>(40);
				for (String string : speciesNames) {
					blockSeqs.add(hp.get(string));
				}
				treeBuild_instance.addSeqsForEachBlock(blockSeqs);
			}
			mfp.close();

			// treeBuild_instance.displayWholePairwiseProp();

			// addMiddleProp(treeBuild_instance.getWholePairwiseMiddlePro());

		}

	}

}
