/**  
* <p>Title: TreeReconstruction.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018-7-27  
* @version 1.0  

*/ 
package module.remnant.treeoperator.reconAlgo;

import module.remnant.treeoperator.NodeEGPSv1;

/**  
* <p>Title: TreeReconstruction</p>  
* <p>Description: 
* 	Phylogenetic reconstruction interface
* </p>  
* @author yudalang  
* @date 2018-727  
*/
public interface TreeReconMethod {
	
	public NodeEGPSv1 tree( double[][] dist, String[] OTUnames );
	public NodeEGPSv1 tree( double[][] dist );

}
