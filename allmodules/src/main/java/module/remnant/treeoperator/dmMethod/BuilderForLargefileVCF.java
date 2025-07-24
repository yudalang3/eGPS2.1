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

import module.remnant.treeoperator.NodeEGPSv1;
import module.evoldist.operator.pairdist.DistParameterLevel1;

/**
 * <p>
 * Title: BuildForMAFfile
 * </p>
 * <p>
 * Description: use VCF files to build tree for whole genome.
 * note this class produced because the VCF file only record the variant site.
 * </p>
 * <p>
 * Usage: </br>
 * BuilderForLargefileVCF build = new BuilderForLargefileVCF(String[] seq_names);</br>
 * build.initialize(); </br>
 * build.addSeqsForEachBlock();</br>
 * Node root = build.getFinalTree();</br>
 * </p>
 * 
 * @author yudalang
 * @date 2018-7-26
 */
public class BuilderForLargefileVCF<E extends DistParameterLevel1> extends BuilderForLargefile<E> {
	
	int referenceGenomeLength;
	private long genomeLength;

	
	public BuilderForLargefileVCF(String[] seq_names) {
		super(seq_names);
	}
	
	
	public void setReferenceGenomeLength(int referenceGenomeLength) {
		this.referenceGenomeLength = referenceGenomeLength;
	}

	public void setGenomeLength(long genomeLength) {
		this.genomeLength=genomeLength;
	}
	@Override
	public NodeEGPSv1 getFinalTree() throws Exception {
		// There is a prepare process, we need to plus the conserve sites to the validate length of all pairwise DistParameterLevel
		double numberOfConservedSize =genomeLength-referenceGenomeLength;
		System.out.println("genomeLength"+genomeLength);
		System.out.println("referenceGenomeLength"+referenceGenomeLength);
		DistParameterLevel1 distParameterLevel1 = new DistParameterLevel1(0,numberOfConservedSize);
		
		for (E[] es : tempWholeSeqsDistanceM) {
			for (E e : es) {
				e.add(distParameterLevel1);
			}
		}
		
		return super.getFinalTree();
	}

}
