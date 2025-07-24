/**  
* <p>Title: BuildPhylogeneticTree.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018-8-23  
* @version 1.0  

*/
package module.remnant.treeoperator.dmMethod;

import java.util.List;

import module.remnant.treeoperator.NodeEGPSv1;
import module.remnant.treeoperator.TreeUtility;
import module.remnant.treeoperator.io.TreeCoder;
import module.remnant.treeoperator.reconAlgo.NJ;
import module.remnant.treeoperator.reconAlgo.TreeReconMethod;
import module.evoldist.operator.DistanceCalculateor;
import module.evoldist.operator.EvoDistanceUtil;
import module.evoltrepipline.PairEvoDistance;

/**
 * <p>
 * Title: BuildPhylogeneticTree
 * </p>
 * <p>
 * Description: 
 *     Usage:
 *     BuildPhylogeneticTree treeBuild_instance = new BuildPhylogeneticTree(seqs, seq_names);
 *     parameterFactor(treeBuild_instance);
 *      
 *     ret = treeBuild_instance.getFinalTree();
 * 
 * or new BuildPhylogeneticTree(); than set seqs and seqNames !
 * </p>
 * 
 * @author yudalang
 * @date 2018-8-23
 */
public class BuilderSinglePhyloTree extends DistanceCalculateor{
	
	protected TreeReconMethod treeReconMethod = new NJ();
	
	public BuilderSinglePhyloTree() {}
	
	public BuilderSinglePhyloTree(List<String> seqs, List<String> seq_names) {
		super(seqs, seq_names);
	}

	public BuilderSinglePhyloTree(String[] sequences, String[] seqNames) {
		super(sequences, seqNames);
	}

	public NodeEGPSv1 getFinalTree() throws Exception {
		return getFinalTree(getFinalDistance());
	}

	public NodeEGPSv1 getFinalTree(double[][] dist) throws Exception {

		// Test For input genetic distance, don't delete
		
//		  DistMatrixTextInput d = new DistMatrixTextInput(
//		  "E:/javaCode/eGPS_example_data_sets/Distance/testGeneticDistance.txt");
//		  
		//double[][] dist = d.getDistanceMatrix();
//		for (double[] ds : dist) {
//			for (double d1 : ds) {
//				System.out.print(d1 + "\t");
//			}
//			System.out.println();
//		}
//		System.out.println(Arrays.toString(seqNames));
		//Node ret = treeReconMethod.tree(dist, d.getOTU_names());
		 
		
		boolean[] evaluateDistM = evaluateDistM(dist);
		
		EvoDistanceUtil.dialogAppear(evaluateDistM);

		NodeEGPSv1 ret = treeReconMethod.tree(dist, seqNames);

		// This will be rooted by the mid-point method
		// http://cabbagesofdoom.blogspot.com/2012/06/how-to-root-phylogenetic-tree.html
		TreeUtility util = new TreeUtility();
		ret = util.rootAtMidPoint(ret);

		return ret;
	}

	/**
	 * @return boolean[] first element indicate whether there exist at least one is positive.<br>
	 *                   second element indicate whether all element are non-NaN value!
	 */
	boolean[] evaluateDistM(double[][] distM) {
		return EvoDistanceUtil.evaluateDistM(distM);
	}

	public void setTreeReconMethod(TreeReconMethod treeReconMethod) {
		this.treeReconMethod = treeReconMethod;
	}
	
	public static void main(String[] args) throws Exception {
		String[] seqs = {"ACTT","ACGT","AGGT"};
		String[] seqNames = {"seq1","seq2","seq3"};
		BuilderSinglePhyloTree tt = new BuilderSinglePhyloTree(seqs, seqNames);
		tt.initDistance(PairEvoDistance.JC69_MODEL,false);
		
		NodeEGPSv1 finalTree = tt.getFinalTree();
		String code = TreeCoder.code(finalTree);
		System.err.println(code);
	}

}
