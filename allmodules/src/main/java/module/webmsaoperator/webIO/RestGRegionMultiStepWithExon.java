package module.webmsaoperator.webIO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import module.webmsaoperator.webIO.gene.Gene;
import module.webmsaoperator.webIO.gene.Transcript;
/**
 * 
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName: GenomicRegionMultiStep2.java
 * 
 * @Package: egps.core.seqDistance.io
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-02-21 09:11
 * 
 */
public class RestGRegionMultiStepWithExon extends RestGenomicRegionMultiStep {

	public RestGRegionMultiStepWithExon(String speciesName, String geneSymbol, int expandIndex)  {
		super(speciesName, geneSymbol, expandIndex);
	}

	@Override
	protected void step2_getRegionStr() {
		String finalRegionStr = jsonStrBuilder.toString();
		JSONObject geneInfo = JSONObject.parseObject(finalRegionStr);
		String chrom = geneInfo.getString("seq_region_name");
		String assemblyName = geneInfo.getString("assembly_name");
		String strand = geneInfo.getString("strand");
		int geneStartInt = geneInfo.getIntValue("start");
		int geneEndInt = geneInfo.getIntValue("end");

		String transcriptsStr = geneInfo.getString("Transcript");
		JSONObject transcriptObjects = JSONObject.parseObject(transcriptsStr);

		int cordinateBase = geneStartInt - 1;
		Gene gene = new Gene();
		gene.setStart(1);
		gene.setEnd(geneEndInt - cordinateBase);
		gene.setChrom(chrom);
		gene.setString(strand);
		gene.setAssemblyName(assemblyName);

		/**
		 * Note, currently we just need first transcript
		 */
		final int FIRST_TRANSCRIPT_ELE_INDEX = 0;
		Object transcriptObject = transcriptObjects.get(FIRST_TRANSCRIPT_ELE_INDEX);
		JSONObject firstTranscriptJSONobj = JSONObject.parseObject(transcriptObject.toString());
		String exon = firstTranscriptJSONobj.getString("Exon");
		
		JSONArray exonObjects = JSONArray.parseArray(exon);

		Transcript transcript = new Transcript();
		int transcriptStart = firstTranscriptJSONobj.getIntValue("start");
		int transcriptEnd = firstTranscriptJSONobj.getIntValue("end");
		
		transcript.setStart(transcriptStart - cordinateBase);
		transcript.setEnd(transcriptEnd - cordinateBase);
		
		
		try {
			String translation = firstTranscriptJSONobj.getString("Translation");
			JSONObject translationObjs = JSONObject.parseObject(translation);
			int cdsStartInt = translationObjs.getIntValue("start");
			int cdsEndInt = translationObjs.getIntValue("end");
			
			transcript.setCds(new int[] { cdsStartInt - cordinateBase, cdsEndInt - cordinateBase });
			
		} catch (JSONException e) {
			isProteinCodingGene = false;
		}
		
		gene.setIfProteinCodingGene(isProteinCodingGene);
		
		// Assign exons information to transcript 
		List<int[]> exons = new ArrayList<>();
		for (int j = 0; j < exonObjects.size(); j++) {
			Object exonObject = exonObjects.get(j);
			JSONObject exonSingle = JSONObject.parseObject(exonObject.toString());
			int exonStart = exonSingle.getIntValue("start");
			int exonEnd = exonSingle.getIntValue("end");
			exons.add(new int[] { exonStart - cordinateBase, exonEnd - cordinateBase });
		}
		// sort exons
		Collections.sort(exons, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				int v1 = o1[0];
				int v2 = o2[0];
				// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				return v1 > v2 ? 1 : (v1 < v2) ? -1 : 0;
			}
		});
		
		transcript.setExons(exons);
		// Infer introns from exons
		List<int[]> introns = new ArrayList<>();
		int len = exons.size();
		for (int i = 1; i < len ; i++) {
			int[] preExonArr = exons.get(i - 1);
			int[] currExonArr = exons.get(i);
			introns.add(new int[] { preExonArr[1] + 1, currExonArr[0] - 1 });
		}
		transcript.setIntrons(introns);
		transcript.setGene(gene);
		
		gene.addOneTranscript(transcript);
		
		
		genomicRegionInfo = new GenomicRegionInfo(chrom, assemblyName, strand, geneStartInt+"", geneEndInt+"", gene);
		
	}

	
	@Override
	public byte getIfProteinCodingGene() {
		if (isProteinCodingGene) {
			return 1;
		}else {
			return -1;
		}
	}
}
