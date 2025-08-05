/**  
* <p>Title: BuildTreeForGeneticDist.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018-11-29  
* @version 1.0  

*/
package module.treebuilder.fromdist;

import java.io.File;

import egps2.panels.dialog.SwingDialog;
import module.remnant.treeoperator.AbstructBuildDMTreePipe;
import module.remnant.treeoperator.NodeEGPSv1;
import module.remnant.treeoperator.dmMethod.BuilderSinglePhyloTree;
import egps2.frame.MainFrameProperties;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.moderntreeviewer.IndependentModuleLoader;
import module.evoltrepipline.ParameterAssigner;
import module.webmsaoperator.webIO.DistMatrixTextInput;

/**
 * <p>
 * Title: BuildTreeForGeneticDist
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author yudalang
 * @date 2018-11-29
 */
public class PLGeneticDistsFile2Phylotree extends AbstructBuildDMTreePipe {

	private File inputFile;

	protected int processIndex = 1;

	protected String[] names;
	protected double[][] distanceMatrix;

	protected NodeEGPSv1 finalTree;
	

	public PLGeneticDistsFile2Phylotree(File file) {
		inputFile = file;
	}
	
	public PLGeneticDistsFile2Phylotree() {}
	

	@Override
	public boolean isTimeCanEstimate() {
		return true;
	}

	@Override
	public int getTotalSteps() {
		return 3;
	}

	@Override
	public int processNext() throws Exception {
		switch (processIndex) {
		case 1:
			step1();
			processIndex++;
			break;
		case 2:
			assignParameters();
			processIndex++;
			break;
		case 3:
			runningBuildTree();
			processIndex++;
			break;
		default:
			return PROGRESS_FINSHED;
		}

		return processIndex;
	}

	protected void runningBuildTree() throws Exception {
		treeBuild_instance.setSeqNames(names);

		finalTree = treeBuild_instance.getFinalTree(distanceMatrix);
	}

	protected void assignParameters() throws Exception {
		// Not need to use bootstrap
		treeBuild_instance = new BuilderSinglePhyloTree();
		ParameterAssigner.parameterFactor(treeBuild_instance, settingValue);
	}

	protected void step1() {
		DistMatrixTextInput d = new DistMatrixTextInput(inputFile);
		distanceMatrix = d.getDistanceMatrix();
		names = d.getOTU_names();
	}

	@Override
	public void actionsBeforeStart() throws Exception {
	}
	@Override
	public void actionsAfterFinished() throws Exception {

		if (finalTree == null) {
			SwingDialog.showErrorMSGDialog("Error", "The tree is null! Please check the input file!");
			return;
		}
		
		final GraphicsNode ret = convertTheTree4visualization(finalTree);

		IndependentModuleLoader independentModuleLoader = new IndependentModuleLoader();
		independentModuleLoader.setHowModuleLaunchedWithData("Module launched by pipline evolutionary distance to construct the phylogenetic tree.", 
				"Data source is from evolutionary distance file: ".concat(inputFile.getName()));
		/**
		 * treeLayoutProperties 可选，如果输入为null，会自动根据 root生成 treeLayoutProperties
		 * 开发者也可以自己生成 TreeLayoutProperties类示例，进而设置一些参数
		 */
		independentModuleLoader.setModuleData(ret, null);
		MainFrameProperties.loadTheModuleFromIModuleLoader(independentModuleLoader);
	}

}
