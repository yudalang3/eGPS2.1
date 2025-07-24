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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import module.remnant.treeoperator.NodeEGPSv1;
import module.remnant.treeoperator.io.TreeCoder;
import module.remnant.treeoperator.reconAlgo.TreeReconMethod;
import module.evoldist.operator.EvoDistanceUtil;
import module.evoldist.operator.pairdist.DistParameterLevel1;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

/**
 * <p>Title: Bootstrap
 * </p>
 * <p>
 * Description: I use the approach: super impose the bootstrap values on the tree
 * obtained from the original tree.
 * </p>
 * <p>
 * Usage: Bootstrap bootstrap = new Bootstrap(seqs, seq_names);</br>
 * bootstrap.setBootTimes(1); // Or other settings </br>
 * bootstrap.initDistance(DistanceMethodName) </br>
 * bootstrap.initialize(); </br>
 * bootstrap.runOneBS_returnIfContinue();</br>
 * Node root = bootstrap.getFinalTree();</br>
 * </p>
 * 
 * @author yudalang
 * @date 2018-7-26
 * @date 2024-04-30 yudalang再次修改
 */
public class BuilderBootstrapTree4Dist<E extends DistParameterLevel1> extends BuilderBootstrapTree4MSA<E> {
	
	private BufferedWriter multiNwkFileWriter;
	
	/** 
	 * In some son classes, we need to new the class first, 
	 * but don't forget to set names and seqs 
	 */
	public BuilderBootstrapTree4Dist() {}
	
	public BuilderBootstrapTree4Dist(List<String> seqs, List<String> seq_names) {
		super(seqs, seq_names);
	}
	public BuilderBootstrapTree4Dist(String[] sequences, String[] seqNames) {
		super(sequences, seqNames);
	}
	
	@Override
	public void initialize() throws Exception {
		evoPairwiseDistMethod = (EvoPairwiseDistMethod<E>) alignmentDists.getPairEvoDistance().getEvopairDistMethod();
		initWholeTempStoreVariable();
	}
	
	/**
	 *  Original tree built by input sequence alignment!
	 * @param oTU_names 
	 */
	public void getOriPhylogeneticTree(double[][] tempWholeSeqsDistanceM, String[] oTU_names) throws Exception {
		seqNames = oTU_names;
		
		boolean[] eval = EvoDistanceUtil.evaluateDistM(tempWholeSeqsDistanceM);
		EvoDistanceUtil.dialogAppear(eval);
		
		theFinalOnePhyloTree = treeReconMethod.tree(tempWholeSeqsDistanceM,oTU_names);
		theFinalOnePhyloTree = util.rootAtMidPoint(theFinalOnePhyloTree);
		
			multiNwkFileWriter.write(TreeCoder.code(theFinalOnePhyloTree));
			multiNwkFileWriter.write("\n");
		
		Set<NodeEGPSv1> theFinalOnePhyloTreeInterNodes = theFinalOnePhyloTree.getAll_internalNodes();
		for (NodeEGPSv1 node : theFinalOnePhyloTreeInterNodes) {
			interNodes2LeafNames.put(node, interNodeLeaves2joinString(node));
			// System.out.print(interNodeLeaves2joinString(node)+"\t");
		}
		// System.out.println("\nOriginal\n");
	}

	/**
	 * |--- a ---node|--- b |--- c input node and return: "a-b-c"
	 * 
	 * @author yudalang
	 * @date 2018-8-22
	 */
	protected String interNodeLeaves2joinString(NodeEGPSv1 node) {

		Set<NodeEGPSv1> leavesNodes = node.getAll_leaves();

		String[] array = new String[leavesNodes.size()];
		int i = 0;
		for (NodeEGPSv1 node2 : leavesNodes) {
			array[i] = node2.getLeafName();
			i++;
		}
		Arrays.sort(array);

		return String.join("-", array);
	}

	/**
	 * Run once bootstrap for out class to invoke!
	 * 
	 *  @return whether outer class should invoke again!
	 */
	public boolean runOnceBSIteration(double[][] distM) {
		
		boolean[] eval = EvoDistanceUtil.evaluateDistM(distM);
		if (!eval[1]) {
			return true;
		}
		/** 
		 * Reconstruct the tree
		 * This will encounter the situation when there is no sequence diversity! 
		 */
		if (eval[0]) {
			/**
			 * Situation when at least one element in distance matrix is positive!
			 */
			NodeEGPSv1 oneBootstrapTree = null;
			try {
				oneBootstrapTree = treeReconMethod.tree(distM, seqNames);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println(seqNames);
			}
			
			oneBootstrapTree = util.rootAtMidPoint(oneBootstrapTree);
			
			try {
				multiNwkFileWriter.write(TreeCoder.code(oneBootstrapTree));
				multiNwkFileWriter.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			// to get a Set<String>, String is a join leaves' names. E.G. "seq1-seq2"
			Set<NodeEGPSv1> oneBootstrapTreeInterNodes = oneBootstrapTree.getAll_internalNodes();
			Set<String> interNodesJoinNameString = new HashSet<>();
			for (NodeEGPSv1 node : oneBootstrapTreeInterNodes) {
				interNodesJoinNameString.add(interNodeLeaves2joinString(node));
				// System.out.print( interNodeLeaves2joinString(node) +"\t");
			}
//			System.out.println("String in one bootstrap is\t" +
//			interNodesJoinNameString.toString());

			Iterator<?> iter = interNodes2LeafNames.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) iter.next();
				NodeEGPSv1 key1 = (NodeEGPSv1) entry.getKey();
				String val = (String) entry.getValue();
				// System.out.println("String in the map is\t" + val);

				if (interNodesJoinNameString.contains(val)) {
					key1.setBs(key1.getBs() + 1);
					// System.out.println(key1.getBs());
					//System.out.println("String in the map is\t" + val);
				}
			}

		} else {
			/**
			 * no sequence diversity!
			 */
			Iterator<?> iter = interNodes2LeafNames.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) iter.next();
				NodeEGPSv1 key1 = (NodeEGPSv1) entry.getKey();
				// String val = (String) entry.getValue();
				key1.setBs(key1.getBs() + 1);
			}
			
			try {
				multiNwkFileWriter.write("**no sequence diversity");
				multiNwkFileWriter.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
			
		bootstrapRunTimeIndex++;
		return bootstrapRunTimeIndex < bootTimes;
	}


//	private void printTempDisMat() {
//		for (int i = 0; i < tempWholeSeqsDistanceM.length; i++) {
//			for (int j = 0; j <= i; j++) {
//				E evoDistParamter = (E) tempWholeSeqsDistanceM[i][j];
//				System.out.print(evoDistParamter.toString() + "  ");
//			}
//			System.out.println();
//		}
//	}


	public NodeEGPSv1 getFinalTree() throws Exception{
		
		multiNwkFileWriter.close();

		Iterator<Entry<NodeEGPSv1, String>> iter = interNodes2LeafNames.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<NodeEGPSv1, String> entry = iter.next();
			NodeEGPSv1 key1 = entry.getKey();
			//String val = entry.getValue();

			double matchCounts = key1.getBs();
			double finalBSValue = (double) Math.round( 100 * matchCounts / bootTimes) / 100;
			//System.out.print(finalBSValue+"\t");
			key1.setBs(finalBSValue);

		}

		return theFinalOnePhyloTree;
	}

	public void setTreeReconMethod(TreeReconMethod tReconMethod) {
		treeReconMethod = tReconMethod;
	}

	public void setMultiNwkOutputFile(File file) throws IOException {
		multiNwkFileWriter = new BufferedWriter(new FileWriter(file));
	}
}
