package module.webmsaoperator.webIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONObject;

import egps2.panels.dialog.SwingDialog;

/**
 * <p>
 * Title: GenomicRegion.java
 * </p>
 * <p>
 * Description: Input species name( e.g. Homo_sapiens) and geneSymbol( e.g.
 * Bpgm) Than return genomic region.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017, PICB
 * </p>
 * <p>
 * Owner: http://www.picb.ac.cn/evolgen/
 * </p>
 * 
 * @author yudalang
 * @date 2018-7-30
 * @version 1.0
 * 
 */
public class RestGenomicRegionOneStep {

	private String speciesName = "Homo_sapiens";
	private String geneSymbol = "Cyhr1";
	
	private String finalRegionStr;
	private String jsonStr;
	
	private boolean isSuccessful2GetPosition = false;
	
	//following codes are for multip step way to use this class
	private int processIndex = 1;

	public RestGenomicRegionOneStep() {
	}
	/**
	 * @param speciesName "homo_sapiens"
	 * @param geneSymbol "Cyhr1"
	 */
	public RestGenomicRegionOneStep(String speciesName, String geneSymbol) {
		this.speciesName = speciesName;
		this.geneSymbol = geneSymbol;
	}
	public boolean isSucessful2GetPosition() {
		return isSuccessful2GetPosition;
	}
	
	/**
	 * The mainly method to get region string: E.g. : "8:144449582-144465677:-1".<br>
	 * Also this is the one step way to use this class!
	 * @author yudalang
	 */
	public String getRegionString() {
		String[] rSplits = getRegionSplits();
		if (rSplits == null)  return null;
		
		finalRegionStr = rSplits[0] + ":" + rSplits[1] + "-" + rSplits[2] + ":" + rSplits[4];
		return finalRegionStr;
	}

	private String[] getRegionSplits() {

		try {
			jsonStr = getJsonString();
			isSuccessful2GetPosition = true;
			
		} catch (IOException e) {
			isSuccessful2GetPosition = false;
			
			SwingDialog.showErrorMSGDialog(
					"Can't get genomic region", "No valid information for gene " + geneSymbol + " in the species " + speciesName);
			
			return null;
		}
		JSONObject data = JSONObject.parseObject(jsonStr);
		/*
		 * source: "ensembl_havana", object_type: "Gene", logic_name:
		 * "ensembl_havana_gene", version: 14, species: "homo_sapiens", description:
		 * "BRCA2, DNA repair associated [Source:HGNC Symbol;Acc:HGNC:1101]",
		 * display_name: "BRCA2", assembly_name: "GRCh38", biotype: "protein_coding",
		 * end: 32400266, seq_region_name: "13", db_type: "core", strand: 1, id:
		 * "ENSG00000139618", start: 32315474
		 */

		String chrom = data.getString("seq_region_name");
		String assembly_name = data.getString("assembly_name");
		String strand = data.getString("strand");
		String start = data.getString("start");
		String end = data.getString("end");
		// System.out.println(jsonStr);
		String[] ret = { chrom, start, end, assembly_name, strand };
		// System.out.println("This\t" + chrom +"\t" + start +"\t"+ end);
		return ret;
	}

	private String getJsonString() throws IOException {
		String server = "http://rest.ensembl.org";
		String ext = "/lookup/symbol/" + speciesName + "/" + geneSymbol + "?expand=0";
		URL url = new URL(server + ext);

		URLConnection connection = url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) connection;

		httpConnection.setRequestProperty("Content-Type", "application/json");

		InputStream response = connection.getInputStream();
		int responseCode = httpConnection.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
		}

		String output;
		Reader reader = null;
		try {
			/* This is a string reader
			 * 这是一个字符流 
			 */
			reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder builder = new StringBuilder();
//			char[] buffer = new char[8192];
			char[] buffer = new char[80];// Real length is 350 - 400!
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				System.out.println(read + "\tTheory is 80");
				builder.append(buffer, 0, read);
			}
			
			output = builder.toString();
		} finally {
			
			if (reader != null)
				try {
					reader.close();
				} catch (IOException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}

		return output;
	}
	
	
	/*public boolean multiWayGetJSONStr() {
		
	}*/

	/**
	 * Multi steps to get the region string!<br>
	 * Repeated call this method util return false!
	 * @return true if we still need to call this method else means finished
	 * @author yudalang
	 */
	/*public boolean getRegionStringMultiStep() {
		switch (processIndex) {
		case 1:
			step1();
			processIndex++;
			break;
		case 2:
			step2();
			processIndex++;
			break;
		case 3:
			step3();
			processIndex++;
			break;
		case 4:
			step4();
			processIndex++;
			break;
		default://processIndex more than last case!
			
		}

		return true;
	}
	private void step1() {
		
	}*/

}
