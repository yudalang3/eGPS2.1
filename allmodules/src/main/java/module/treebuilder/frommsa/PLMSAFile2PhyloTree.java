/**  
* <p>Title: BuildTreeMSA.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018年8月24日  
* @version 1.0  

*/
package module.treebuilder.frommsa;

import java.io.File;
import java.util.Map;

import egps2.panels.dialog.SwingDialog;
import module.remnant.treeoperator.NodeEGPSv1;
import egps2.frame.MainFrameProperties;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.moderntreeviewer.IndependentModuleLoader;
import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.io.MSAFileParser;
import msaoperator.io.seqFormat.MSA_DATA_FORMAT;

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
 */
public class PLMSAFile2PhyloTree extends PLMSA2PhyloTree {

	protected File inputFile = null;
	protected MSA_DATA_FORMAT format = MSA_DATA_FORMAT.ALIGNED_FASTA;

	public PLMSAFile2PhyloTree(File file) {
		super();
		this.inputFile = file;
	}
	public PLMSAFile2PhyloTree(File file, Map<String, String> settings) {
		super(settings);
		this.inputFile = file;
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

	private void step1() throws Exception {
		MSAFileParser msaFileParser = new MSAFileParser();
		BasicSequenceData seqElements = msaFileParser.parserMSAFile(inputFile, format);
		this.seqs = seqElements.getSequences();
		this.seqNames = seqElements.getSequenceNames();

	}
	
	public void setFormat(MSA_DATA_FORMAT format) {
		this.format = format;
	}

	@Override
	public int getTotalSteps() {
		return 1 + super.getTotalSteps();
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
		independentModuleLoader.setHowModuleLaunchedWithData("Module launched by reading pipline multiple sequence alignment to construct the phylogenetic tree.", 
				"Data source is from the file: ".concat(inputFile.getName()));
		/**
		 * treeLayoutProperties 可选，如果输入为null，会自动根据 root生成 treeLayoutProperties
		 * 开发者也可以自己生成 TreeLayoutProperties类示例，进而设置一些参数
		 */
		independentModuleLoader.setModuleData(ret, null);
		MainFrameProperties.loadTheModuleFromIModuleLoader(independentModuleLoader);
	}
}
