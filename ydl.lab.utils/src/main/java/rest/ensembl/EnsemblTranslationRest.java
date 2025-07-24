package rest.ensembl;

import rest.ensembl.proteins.RestGetProteinDomains;

public class EnsemblTranslationRest {


	public static void main(String[] args) throws Exception {
		RestGetProteinDomains restGetProteinDomains = new RestGetProteinDomains();
//		restGetProteinDomains.doIt();
		
		String protID = "Os01t0974500-01";
		
		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\arabidopsis\\5_Notum\\ff.txt";
		restGetProteinDomains.setOutputPath(outPath);
		
		restGetProteinDomains.doOnceRest(protID);
	}
}