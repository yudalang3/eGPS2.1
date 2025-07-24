/**  
* <p>Title: Bootstrap.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018年7月26日  
* @version 1.0  

*/
package module.remnant.treeoperator.dmMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import module.remnant.treeoperator.NodeEGPSv1;
import module.remnant.treeoperator.TreeUtility;
import module.evoldist.operator.EvoDistanceUtil;
import module.evoldist.operator.pairdist.DistParameterLevel1;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

/**
 * <p>
 * Title: BuildForMAFfile
 * </p>
 * <p>
 * Description: use MAF files to build tree for whole genome.
 * </p>
 * <p>
 * Usage: </br>
 * BuildForMAFfile build = new BuildForMAFfile(String[] seq_names);</br>
 * build.initialize(); </br>
 * build.addSeqsForEachBlock();</br>
 * Node root = build.getFinalTree();</br>
 * </p>
 * 
 * @author yudalang
 * @date 2018-7-26
 */
public class BuilderForLargefile<E extends DistParameterLevel1> extends BuilderBootstrapTree4MSA<E> {
	private boolean ifBootstrap = false;
	private boolean fullLoadValues = false;
	
	public BuilderForLargefile(String[] seq_names) {
		this.seqNames = seq_names;
	}

	public void addSeqsForEachBlock(List<String> blockSeqs) {
		
		// we should deal with gap before compute evolutionary distance!
		String[] sequences = new String[blockSeqs.size()];
		sequences = blockSeqs.toArray(sequences);
		addSeqsForEachBlock(sequences);
	}
	
	public void addSeqsForEachBlock(String[] sequences) {
		
		
		int len = tempWholeSeqsDistanceM.length;
		if (ifBootstrap) {
			addToPreCalSegments(sequences);
		}else {
			String[] processedAlignment = dealWithDeletion(sequences);
			alignmentDists.reSetSeqs(processedAlignment);
			E[][] distPs = (E[][]) alignmentDists.getEvoDistParameters();
			for (int i = 0; i < len; i++) {
				for (int j = 0; j <= i; j++) {
					tempWholeSeqsDistanceM[i][j].add(distPs[i][j]);
				}
			}
			distPs = null;
		}
	}
	
	@Override
	protected void addToPreCalSegments(String[] temp_seqs) {
		if (preCalSegmentPairwiseDistIndex == 1000) {
			preCalSegmentPairwiseDistIndex = 0;
			fullLoadValues  = true;
		}
		
		String[] processedAlignment = dealWithDeletion(temp_seqs);
		alignmentDists.reSetSeqs(processedAlignment);
		
		E[][] oneSegmentDistOneBS = (E[][]) alignmentDists.getEvoDistParameters();
		
		preCalSegmentPairwiseDist[preCalSegmentPairwiseDistIndex] = oneSegmentDistOneBS;
		preCalSegmentPairwiseDistIndex ++;
	}
	
//	public E[][] getWholePairwiseMiddlePro() {
//		return (E[][]) tempWholeSeqsDistanceM;
//	}
	
	public double[][] getDistMatrix(){
		return segmentDistToDoubleDist(tempWholeSeqsDistanceM);
	}
	
//	public void displayWholePairwiseProp() {
//		System.out.println();
//		System.out.println(getClass()+"\tThis is whole pairwise prop display!!");
//		for (int i = 0; i < tempWholeSeqsDistanceM.length; i++) {
//
//			for (int j = 0; j <= i; j++) {
//
//				double numofMismatch = tempWholeSeqsDistanceM[i][j].getNumOfMismatch();
//				double validLength = tempWholeSeqsDistanceM[i][j].getValidateLength();
//				System.out.println(seqNames[j] + "\t" + seqNames[i+1] + "\t" + numofMismatch + "\t" + validLength + "\t"
//						+ (double) numofMismatch / validLength);
//			}
//		}
//	}
	
	@Override
	public void initialize() {
		evoPairwiseDistMethod = (EvoPairwiseDistMethod<E>) alignmentDists.getPairEvoDistance().getEvopairDistMethod();
		initWholeTempStoreVariable();
	}
	@Override
	protected void initWholeTempStoreVariable() {
		super.initWholeTempStoreVariable();
		if (ifBootstrap) {
			preCalSegmentPairwiseDist = (E[][][]) new DistParameterLevel1[1000][][];
		}
	}
	
	@Override
	public NodeEGPSv1 getFinalTree() throws Exception {
		
		double[][] distM = segmentDistToDoubleDist(tempWholeSeqsDistanceM);
		
		System.out.println(getClass()+"\t This is used to check something:");
		System.out.println("------------------------------------------------------");
		System.out.println("OTU Names:\t" + Arrays.toString(seqNames));
		for (double[] ds : distM) {
			System.out.print(Arrays.toString(ds) + "\t");
			System.out.println();
		}
		
		boolean[] eval = EvoDistanceUtil.evaluateDistM(distM);
		EvoDistanceUtil.dialogAppear(eval);
		
		NodeEGPSv1 ret = treeReconMethod.tree(distM, seqNames);

		TreeUtility util = new TreeUtility();
		ret = util.rootAtMidPoint(ret);

		return ret;
	}
	

	public void setIfBootstrap(boolean ifBootstrap) {
		this.ifBootstrap = ifBootstrap;
	}

	public NodeEGPSv1 runBootstrapAndGetFinalTree() throws Exception {
		
		if (!fullLoadValues) {
			seqLength = preCalSegmentPairwiseDistIndex;
		}else {
			seqLength = preCalSegmentPairwiseDist.length;
		}
		
		reConfigPreCalculatedPairwiseDist();
		theFinalOnePhyloTree = getFinalTree();
		
		Set<NodeEGPSv1> theFinalOnePhyloTreeInterNodes = theFinalOnePhyloTree.getAll_internalNodes();
		for (NodeEGPSv1 node : theFinalOnePhyloTreeInterNodes) {
			interNodes2LeafNames.put(node, interNodeLeaves2joinString(node));
			// System.out.print(interNodeLeaves2joinString(node)+"\t");
		}
		while (runOnceBSIteration()) {}
		
		return super.getFinalTree();
	}

	private void reConfigPreCalculatedPairwiseDist() {
		for (int k = 0; k < seqLength; k++) {
			E[][] curr = preCalSegmentPairwiseDist[k];
			
			for (int i = 0; i < tempWholeSeqsDistanceM.length; i++) {
				for (int j = 0; j <= i; j++) {
					tempWholeSeqsDistanceM[i][j].add(curr[i][j]);
				}
			}
		}
		
	}

}
