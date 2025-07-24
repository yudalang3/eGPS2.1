/**  
* <p>Title: RestAlignment.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018-11-9  
* @version 1.0  

*/
package module.webmsaoperator.webIO;

import java.util.List;

/**
 * <p>
 * Title: RestAlignment
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author yudalang
 * @date 2018-11-9
 */
public class RestAlignmentOneStep {

	private String speciesName;
	private String geneSymbol;
	private String speciesSetGroup;
	private String region;
	
	private RestGRegion2MSAOneStep ensemblRMaf;

	private List<String> seqs;
	private List<String> seq_names;

	public RestAlignmentOneStep(String speciesName, String geneSymbol, String speciesSetGroup) {
		this.speciesName = speciesName;
		this.geneSymbol = geneSymbol;
		this.speciesSetGroup = speciesSetGroup;
	}

	/**
	 * yudalang: When you use this constructor, the geneSymbol paramter could be any
	 * arbitrarily string.
	 * 
	 * @param speciesName
	 * @param geneSymbol
	 * @param speciesSetGroup
	 * @param region
	 */
	public RestAlignmentOneStep(String speciesName, String geneSymbol, String speciesSetGroup, String region) {
		this.speciesName = speciesName;
		this.geneSymbol = geneSymbol;
		this.speciesSetGroup = speciesSetGroup;
		this.region = region;
	}

	public void init() {

		if (region == null) {
			// System.out.println(speciesName+"\t"+geneSymbol);
			RestGenomicRegionOneStep genomicRegion = new RestGenomicRegionOneStep(speciesName, geneSymbol);
			// GenomicRegion genomicRegion = new GenomicRegion();//"Homo_sapiens" "Cyhr1"
			region = genomicRegion.getRegionString();

			if (!genomicRegion.isSucessful2GetPosition()) {
				// throw new Exception("Con't find the gene region");
				return;
			}

		} else {
			region = region + ":1";
		}

		/**
		 * Get the alignment from the EMBL's REST API!
		 */
		// System.out.println(getClass()+"\t"+speciesName+"\t"+region+"\t"+speciesSetGroup);
		ensemblRMaf = new RestGRegion2MSAOneStep(speciesName, region, speciesSetGroup);
		// ensemblRMaf = new EnsemblRestGetMSA("homo_sapiens", "2:106040000-106040050:1", "primates");
		if (ensemblRMaf.isSucessful2GetAlign()) {

			seqs = ensemblRMaf.getSeqs();
			seq_names = ensemblRMaf.getSeq_names();

		} else {
			return;
		}
	}

	public List<String> getSeq_names() {
		return seq_names;
	}

	public List<String> getSeqs() {
		return seqs;
	}
	
	public String getJsonOutputString() {
		return ensemblRMaf.getJsonOutputString();
	}

}
