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

/**
 * <p>
 * Title: GenomicRegion.java
 * </p>
 * <p>
 * Description: Input species name( e.g. Homo_sapiens) and geneSymbol( e.g.
 * Bpgm) Than return genomic region. This is the multip steps way to get genomic
 * region!

 * Owner: http://www.picb.ac.cn/evolgen/
 * </p>
 * 
 * @author yudalang
 * @date 2018-7-30
 * @version 1.0
 * 
 */
public class RestGenomicRegionMultiStep {

	public final static int neededTotalSteps = 12;
	
	protected StringBuilder jsonStrBuilder = new StringBuilder(2048);
	
	protected boolean isProteinCodingGene = true;
	
	private int processIndex = 1;
	private long TOTALCHARS;
	
	protected Reader reader;
	
	protected GenomicRegionInfo genomicRegionInfo;
	
	protected String speciesName;
	protected String geneSymbol;
	protected int queryIndex;
	
	protected int timeoutMilliseconds = 15000;
	/**
	 * @param speciesName
	 *            "homo_sapiens"
	 * @param geneSymbol
	 *            "Cyhr1"
	 */
	public RestGenomicRegionMultiStep(String speciesName, String geneSymbol, int queryIndex) {
		this.speciesName = speciesName;
		this.geneSymbol = geneSymbol;
		this.queryIndex = queryIndex;
	}
	
	/**
	 * Multi steps to get the region string!<br>
	 * Repeated call this method util return false!
	 * 
	 * <pre>
	 * <b>Commonly speaking: this method will be invoked 8 times for expendIndex = 0</b>
	 * enter getRegionStringMultiStep()
	 * 80	Theory is 80
	 * enter getRegionStringMultiStep()
	 * 80	Theory is 80
	 * enter getRegionStringMultiStep()
	 * 80	Theory is 80
	 * enter getRegionStringMultiStep()
	 * 80	Theory is 80
	 * enter getRegionStringMultiStep()
	 * 66	Theory is 80
	 * enter getRegionStringMultiStep() // judge whether the reader come to an end
	 * enter getRegionStringMultiStep() // processing step2
	 * enter getRegionStringMultiStep() // go to default!
	 * </pre>
	 * 
	 * @return true if we still need to call this method else means finished
	 * @author yudalang
	 * @throws IOException 
	 */
	public boolean getRegionStringMultiStep() throws IOException {
		switch (processIndex) {

		case 1:
			step0_initNetworkConnection();
			processIndex++;
			break;
		case 2:
			if (step1_getJsonStrBuilder()) {
			} else {
				processIndex++;
			}
			break;
		case 3:
			step2_getRegionStr();
			processIndex++;
			break;
		default:// processIndex more than last case!
			return false;
		}

		return true;
	}

	public void setTimeoutMilliseconds(int timeoutMilliseconds) {
		this.timeoutMilliseconds = timeoutMilliseconds;
	}

	
	
	public int getCurrentProgressIndex() {
		double tt = jsonStrBuilder.length() / (double) TOTALCHARS;
		// 9 means I assigned the weight of get json string as 9
		int ret = (int) (9 * tt + processIndex - 1);

		return ret;
	}

	protected void step2_getRegionStr() {
		String jsonStr = jsonStrBuilder.toString();

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
		String end = data.getString("end") + "";

		genomicRegionInfo = new GenomicRegionInfo(chrom, assembly_name, strand, start, end, null);
	}

	/**
	 * Iterately get the content in Reader!
	 * 
	 * @author yudalang
	 * @return true if still need to get the content else
	 * @throws IOException 
	 */
	private boolean step1_getJsonStrBuilder() throws IOException {
		// Real length is 350 - 400 for expend index 0 and other don't!
		char[] buffer = new char[128];
		int read;
		if ((read = reader.read(buffer, 0, buffer.length)) != -1) {
			jsonStrBuilder.append(buffer, 0, read);
		} else {
			if (reader != null)
				reader.close();
			return false;
		}

		return true;
	}

	private void step0_initNetworkConnection() throws IOException {
		// init Network Connection
		InputStream response = null;

		String server = "http://rest.ensembl.org";
		String ext = "/lookup/symbol/" + speciesName + "/" + geneSymbol + "?expand=" + queryIndex;
		String urlAddress = server + ext;
		URL url = new URL(urlAddress);
		System.out.println("URL for genomic region:\n" + urlAddress);

		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(timeoutMilliseconds);
		connection.setReadTimeout(timeoutMilliseconds);
		HttpURLConnection httpConnection = (HttpURLConnection) connection;

		httpConnection.setRequestProperty("Content-Type", "application/json");
		TOTALCHARS = httpConnection.getContentLengthLong();

		response = connection.getInputStream();

		int responseCode = httpConnection.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
		}

		reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
	
	}
	
	public GenomicRegionInfo getGenomicRegionInfo() {
		return genomicRegionInfo;
	}

	public StringBuilder getStringBuilder() {
		return jsonStrBuilder;
	}
	
	/**
	 * Indicate if the obtained information show the gene is protein coding gene!
	 * @author yudalang
	 * @return : 0 un avoilable! 1 true! -2 false!
	 */
	public byte getIfProteinCodingGene() {
		return 0;
	}
}
