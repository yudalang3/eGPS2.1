package rest.interpro.entrytaxon;

import analysis.AbstractAnalysisAction;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InterProEntryTaxonInfoParser extends AbstractAnalysisAction{

	private IPREntryTaxonInfoBean parseObject;

	@Override
	public void doIt() throws Exception {
		
		List<String> readLines = FileUtils.readLines(new File(inputPath), StandardCharsets.UTF_8);
		StringBuilder sBuilder = new StringBuilder();
		for (String string : readLines) {
			sBuilder.append(string);
		}
		
		parseObject = JSONObject.parseObject(sBuilder.toString(), IPREntryTaxonInfoBean.class);
		
	}
	
	public IPREntryTaxonInfoBean getParseObject() {
		return parseObject;
	}
	
	public static void main(String[] args) throws Exception {
		InterProEntryTaxonInfoParser worker = new InterProEntryTaxonInfoParser();
		
		worker.setInputPath("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\1_Norrin\\comparaInfo\\Norrin_InterPro\\norrin.domain.protein.all.interPro.json");
		
		worker.doIt();
		
		IPREntryTaxonInfoBean parseObject2 = worker.getParseObject();
		
		
		List<String> output = new ArrayList<>();
		output.add("ID\tName");
		
		for (IPREntryTaxonInfoRecordBean bean : parseObject2.results) {
			Metadata metadata = bean.getMetadata();
			String accession = metadata.getAccession();
			String name = metadata.getName();
			
			output.add(accession + "\t" + name);
		}
//		System.out.println(parseObject2.results.size());
		
		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\1_Norrin\\comparaInfo\\Norrin_InterPro\\norrin.domain.protein.all.transfered.tsv";
		FileUtils.writeLines(new File(outPath), output);
	}

}
