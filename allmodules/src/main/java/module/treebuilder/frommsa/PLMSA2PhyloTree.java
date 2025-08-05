package module.treebuilder.frommsa;

import java.util.Map;

import egps2.panels.dialog.SwingDialog;
import module.remnant.treeoperator.AbstructBuildDMTreePipe;
import module.remnant.treeoperator.NodeEGPSv1;
import egps2.frame.MainFrameProperties;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.moderntreeviewer.IndependentModuleLoader;

public class PLMSA2PhyloTree extends AbstructBuildDMTreePipe {
	protected int processIndex = 1;

	public PLMSA2PhyloTree() {
		super();
	}
	
	public PLMSA2PhyloTree(Map<String, String> settings) {
		super(settings);
	}

	@Override
	public int processNext() throws Exception {
		switch (processIndex) {
		case 1:
			assignParameters();
			processIndex++;
			break;
		case 2:
			runningBuildTree();
			processIndex++;
			break;
		default:
			processIndex++;
			if (ifBootStrap) {
				if (treeBuild_instance_BS.runOnceBSIteration()) {
					return processIndex;
				}
			}
			return PROGRESS_FINSHED;
		}

		return processIndex;
	}

	protected void assignParameters() {
		incorporateParametersAndPackageBuildPhylogeneticTreeInstance();
	}

	protected void runningBuildTree() throws Exception {

		if (ifBootStrap) {
			treeBuild_instance_BS.setSeqsAndSeqNames(seqs, seqNames);
			treeBuild_instance_BS.initialize();
		} else {
			treeBuild_instance.setSeqsAndSeqNames(seqs, seqNames);
		}

	}


	@Override
	public boolean isTimeCanEstimate() {
		return true;
	}

	@Override
	public int getTotalSteps() {
		return 2 + bootStrapTimes;
	}

	@Override
	public void actionsBeforeStart() throws Exception {
	}

	@Override
	public void actionsAfterFinished() throws Exception {
		NodeEGPSv1 finalTree = null;
		if (ifBootStrap) {
			finalTree = getTreeWithBS();
		} else {
			finalTree = getTreeWithoutBS();
			// double[][] dm = treeBuild_instance.getFinalDistance();
		}
		if (finalTree == null) {
			SwingDialog.showErrorMSGDialog("Error", "The tree is null! Please check the input file!");
			return;
		}
		final GraphicsNode ret = convertTheTree4visualization(finalTree);

		IndependentModuleLoader independentModuleLoader = new IndependentModuleLoader();
		independentModuleLoader.setHowModuleLaunchedWithData("Module launched by pipline multiple sequence alignment to construct the phylogenetic tree.", 
				"Data source is from the internal sequences.");
		/**
		 * treeLayoutProperties 可选，如果输入为null，会自动根据 root生成 treeLayoutProperties
		 * 开发者也可以自己生成 TreeLayoutProperties类示例，进而设置一些参数
		 */
		independentModuleLoader.setModuleData(ret, null);
		MainFrameProperties.loadTheModuleFromIModuleLoader(independentModuleLoader);
	}

}
