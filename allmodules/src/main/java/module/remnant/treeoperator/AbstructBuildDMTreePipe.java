/**  
* <p>Title: AbstructBuildTreeCommon.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @version 1.0  
*/
package module.remnant.treeoperator;

import java.util.Map;

import module.remnant.treeoperator.dmMethod.BuilderBootstrapTree4MSA;
import module.remnant.treeoperator.dmMethod.BuilderSinglePhyloTree;
import module.evoldist.operator.AbstructCalculateGDistsPipe;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evoltrepipline.ParameterAssigner;

/**
 * <p>
 * Title: AbstructBuildTreeCommon
 * </p>
 * <p>
 * Description: Common used methods in phylogenetic tree reconstruction
 * </p>
 * 
 * @author yudalang
 * @date 2018-8-26
 * 
 * @date 2024-04-29 :ydl modification
 */
public abstract class AbstructBuildDMTreePipe extends AbstructCalculateGDistsPipe {

	protected BuilderBootstrapTree4MSA<?> treeBuild_instance_BS;
	protected BuilderSinglePhyloTree treeBuild_instance;

	public AbstructBuildDMTreePipe() {
		super();
	}
	protected AbstructBuildDMTreePipe(Map<String, String> settingValue) {
		super(settingValue);
	}

	/**
	 * Don't forget to set seqs and seqNames
	 * 
	 * @author yudalang
	 */
	protected void incorporateParametersAndPackageBuildPhylogeneticTreeInstance() {
		if (ifBootStrap) {
			treeBuild_instance_BS = new BuilderBootstrapTree4MSA<>();

			treeBuild_instance_BS.setBootTimes(bootStrapTimes);
			ParameterAssigner.parameterFactorBootTree(treeBuild_instance_BS, settingValue);
		} else {
			// Not use bootstrap
			treeBuild_instance = new BuilderSinglePhyloTree();
			ParameterAssigner.parameterFactor(treeBuild_instance, settingValue);
		}
	}

	protected NodeEGPSv1 getTreeWithoutBS() throws Exception {
		return treeBuild_instance.getFinalTree();
	}

	/**
	 * Before using this method, you need to use while
	 * (treeBuild_instance.runOneBS_returnIfContinue()) {}
	 * 
	 * @author yudalang
	 * @throws Exception
	 */
	protected NodeEGPSv1 getTreeWithBS() throws Exception {
		return treeBuild_instance_BS.getFinalTree();
	}
	
	
	protected GraphicsNode convertTheTree4visualization(NodeEGPSv1 tempTree) {
		GraphicsNode ret = new TreeUtility().node4eGPSToGraphicsNode(tempTree);
		return ret;
	}
}
