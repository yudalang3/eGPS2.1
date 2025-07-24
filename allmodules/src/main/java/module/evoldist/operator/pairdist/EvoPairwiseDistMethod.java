/**  
* <p>Title: EvoPairwiseDist.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2019年3月18日  
* @version 1.0  

*/ 
package module.evoldist.operator.pairdist;

/**  
* <p>Title: EvoPairwiseDist</p>  
* <p>Description: </p>  
* @author yudalang  
* @date 2019-3-18  
*/
public interface EvoPairwiseDistMethod <E extends DistParameterLevel1>{
	
	/**
	 * Get pairwise genetic distance from EvoDistaParamter 
	 */
	double getPairwiseDist(E evoDistParamter);

}
