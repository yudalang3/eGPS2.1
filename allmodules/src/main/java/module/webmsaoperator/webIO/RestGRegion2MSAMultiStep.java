package module.webmsaoperator.webIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import utils.string.EGPSStringUtil;
import module.webmsaoperator.webIO.jsonBean.EnsemblRestAlignmentJsonModel;
import module.webmsaoperator.webIO.jsonBean.RestAlignments;

/**
 * 
 * <p>
 * Title: EnsemblRest
 * </p>
 * <p>
 * Description: Enter the
 * <code>String species, String region, String species_set_group</code> Then it
 * will return the alignment JSON format string!<br>
 * This class also packaging information of that string, so we can directly get
 * <code>seqs and seqNames</code>!
 * </p>
 * 
 * @author mhl,yudalang
 * @date 2018-7-20
 * @help http://www.biotrainee.com/jmzeng/book/basic/database.html
 *       https://rest.ensembl.org/documentation/info/genomic_alignment_region
 */
public class RestGRegion2MSAMultiStep {

	public final static int neededTotalSteps = 250;
	
	private int processIndex = 1;
	protected Reader reader;
	protected StringBuilder builder = new StringBuilder(16384);
	private long TOTALCHARS;

	protected List<String> seqs = new ArrayList<>(50);
	protected List<String> seq_names;
	protected int timeoutMilliseconds;
	protected String species;
	protected String region;
	protected String species_set_group;

	char[] buffer = new char[2048];
	/**
	 * 
	 * @param species
	 *            "homo_sapiens"
	 * @param region
	 *            "2:106040000-106040050"
	 * @param species_set_group
	 *            "primates"
	 * @throws IOException
	 */
	public RestGRegion2MSAMultiStep(int timeoutMilliseconds,String species, String region, String species_set_group)  {
		this.timeoutMilliseconds = timeoutMilliseconds;
		this.species = species;
		this.region = region;
		this.species_set_group = species_set_group;
	}
	
	/**
	 * Multi steps to get the region string!<br>
	 * Repeated call this method util return false!
	 * 
	 * @return true if we still need to call this method else means finished
	 * @author yudalang
	 * @throws IOException 
	 */
	public boolean getEnsembleMSAMultiStep() throws IOException {

		switch (processIndex) {
		case 1:
			step0_initNetworkConnection();
			//System.out.println(getClass()+" case 1 initNetworkConnection finshed!");
			processIndex++;
			break;
		case 2:
			if (step1_getJSONString()) {
			} else {
				processIndex++;
				//System.out.println(getClass()+" case 2 getJsonString finshed!");
			}
			break;
		case 3:
			step2_packagingInformation(builder.toString());
			//System.out.println(getClass()+" case 3 packagingInformation finshed!");
			processIndex++;
			break;
		default:// processIndex more than last case!
			return false;
		}

		return true;
	}
	
	private void step0_initNetworkConnection() throws IOException {
		URL url = configURL();

		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(timeoutMilliseconds);// set the time out!!

		HttpURLConnection httpConnection = (HttpURLConnection) connection;
		httpConnection.setReadTimeout(timeoutMilliseconds);

		httpConnection.setRequestProperty("Content-Type", "application/json");

		long contentLengthLong = httpConnection.getContentLengthLong();
		//System.out.println("contentLengthLong:\t"+contentLengthLong);
		// -1 means can't get contentLengthLong, so we assign an arbitrary value!
		if (contentLengthLong == -1) {
			contentLengthLong = 1000;
		}
		TOTALCHARS = contentLengthLong;
		// System.err.println("BUFF_LEN is:\t"+BUFF_LEN);
		// 17:7661779-7687550
		InputStream response = connection.getInputStream();
		int responseCode = httpConnection.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
		}

		reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));

	}

	private URL configURL() throws MalformedURLException {
		URL url = null;
		
		if ("100 vertebrate species".equalsIgnoreCase(species_set_group)) {
			/** 
			 * Cloud example:
			 * http://bigd.big.ac.cn/egpscloudREST/ws/alignment/chr2A:3888888-3888920?species=panTro4
			 * 
			 * Note: 
			 * 1. The region in cloud and Ensembl is different!
			 * 2. cloud may not support -1 
			 */
			String[] strs = region.split(":");
			String httpStr = "http://bigd.big.ac.cn/egpscloudREST/ws/alignment/chr" + strs[0] + ":" + strs[1]
					+ "?species=" + species;
			System.out.println(httpStr);
			url = new URL(httpStr);
		}else {
			// It's better to use http service, instead of https service
			//String server = EnsemblAlignmentRestAPICheck.getEnsemblServer();
			//String server = "http://jul2018.rest.ensembl.org";
			String server = "http://rest.ensembl.org";
			String ext = null;
			// region = "1:16050508-16051008:1";
			if (species_set_group.equalsIgnoreCase("amniotes")) {
				/**
				 * According to
				 * https://rest.ensembl.org/alignment/region/homo_sapiens/1:1000000-1001000:1?content-type=application/json;species_set_group=amniotes;method=PECAN
				 * https://rest.ensembl.org/alignment/region/homo_sapiens/17:7661779-7687550?content-type=application/json;species_set_group=primates
				 * if you choose amniotes, method shoud change.
				 */
				ext = "/alignment/region/" + species + "/" + region + "?species_set_group=" + species_set_group
						+ ";method=PECAN";
			} else {
				ext = "/alignment/region/" + species + "/" + region + "?species_set_group=" + species_set_group;
			}

			System.out.println(server + ext + ";content-type=application/json");
			// when primate species set group, seq names error happen
			// http://rest.ensembl.org/alignment/region/homo_sapiens/8:144449582-144465677:-1?content-type=application/json;species_set_group=primates
			url = new URL(server + ext);
		}
		
		return url;
	}
	
	private boolean step1_getJSONString() throws IOException {
		int read;
		if ((read = reader.read(buffer, 0, buffer.length)) != -1) {
			builder.append(buffer, 0, read);
		} else {
			if (reader != null)
				reader.close();
			return false;
		}

		return true;
	}

	

	/**
	 * Get the Alignment set with the smallest sum of distance matrix elements
	 * 
	 * @author mhl
	 * 
	 * @Date Created on: 2019-02-27 10:22
	 * 
	 */
	void step2_packagingInformation(String jsonStr) {
		List<List<RestAlignments>> parsedJsonAlignments = parseJson(jsonStr);
		// sort inner elements according to reference genome's start position
		Collections.sort(parsedJsonAlignments, new Comparator<List<RestAlignments>>() {

			@Override
			public int compare(List<RestAlignments> o1, List<RestAlignments> o2) {
				int v1 = o1.get(0).getStart();
				int v2 = o2.get(0).getStart();
				// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				if (region.endsWith(":-1")) {
					return v1 > v2 ? -1 : (v1 < v2) ? 1 : 0;
				}else {
					return v1 > v2 ? 1 : (v1 < v2) ? -1 : 0;
				}
				
			}
		});
		int maxElementIndex = getMaxSeqNamesFromAllAlignments(parsedJsonAlignments);
		getJoinedAlignmentAndAssignValues(parsedJsonAlignments);
	}

	private void getJoinedAlignmentAndAssignValues(List<List<RestAlignments>> t) {
		int size = seq_names.size();
		Map<String, StringBuilder> map = new HashMap<>(size);
		
		for (String seqName : seq_names) {
			map.put(seqName, new StringBuilder(16384));
		}
		// joined sequences
		for (int i = 0; i < t.size(); i++) {
			List<RestAlignments> temporaryAlignments = t.get(i);
			int longestLen = 0;int appendLen = 0;
			for (RestAlignments rest_Alignment : temporaryAlignments) {
				String sequence = rest_Alignment.getSeq();
				String species = rest_Alignment.getSpecies();
				if (map.containsKey(species)) {
					StringBuilder sb = map.get(species);
					sb.append(sequence);
					longestLen = sb.length();
					appendLen = sequence.length();
				}
			}
			
			String gapsToApp = EGPSStringUtil.fillString('-', appendLen);
			for (Map.Entry<String, StringBuilder> entry: map.entrySet()) {
				StringBuilder value = entry.getValue();
				if (value.length() < longestLen) {
					value.append(gapsToApp);
				}
			}
		}
		
		for (int i = 0; i < size; i++) {
			StringBuilder stringBuilder = map.get(seq_names.get(i).toString());
			seqs.add(stringBuilder.toString());
		}
	}

	private int getMaxSeqNamesFromAllAlignments(List<List<RestAlignments>> t) {

		int maxElementIndex = 0;
		int maxSequenceSize = 0;
		List<String> tmp_seqNames = new ArrayList<>(50);

		for (int i = 0; i < t.size(); i++) {

			List<RestAlignments> temporaryAlignments = t.get(i);
			for (RestAlignments rest_Alignment : temporaryAlignments) {
				String species = rest_Alignment.getSpecies();
				// yudalang: Abandon the a-b-v[3] like ancestray states
				if (species.contains("[")) {
					continue;
				}
				tmp_seqNames.add(species);
			}
			int filteredSize = tmp_seqNames.size();
			if (filteredSize > maxSequenceSize) {
				maxSequenceSize = filteredSize;
				maxElementIndex = i;
				seq_names = tmp_seqNames.stream().collect(Collectors.toList());
			}
			tmp_seqNames.clear();
		}
		
		modifyTheSeqNamesIfNeeded();

		return maxElementIndex;
	}

	private void modifyTheSeqNamesIfNeeded() {
		
	}

	/**
	 * Parse the json string to return the alignments collection
	 * 
	 * @author mhl
	 * @Date Created on: 2018-07-21 13:03
	 * 
	 */
	private List<List<RestAlignments>> parseJson(String jsonStr) {
		List<List<RestAlignments>> alignmentsList = new ArrayList<>();
		JSONArray jsonArray = JSONArray.parseArray(jsonStr);

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
//			Map<String, Class> map = new HashMap<String, Class>();
//			map.put("alignments", RestAlignments.class);
//
//			EnsemblRestAlignmentJsonModel restModel = (EnsemblRestAlignmentJsonModel) JSONObject.toBean(jsonObject, EnsemblRestAlignmentJsonModel.class, map);

			String string = jsonArray.getString(i);
			EnsemblRestAlignmentJsonModel restModel = JSONObject.parseObject(string, EnsemblRestAlignmentJsonModel.class);
			List<RestAlignments> alignments = restModel.getAlignments();

			alignmentsList.add(alignments);

		}
		return alignmentsList;
	}

	
	public int getCurrentProgressIndex() {
		double tt = builder.length() / (double) TOTALCHARS;
		// 247 is the weight for get json string
		int ret = (int) (247 * tt + processIndex - 1);

		return ret;
	}
	
	public List<String> getSeqs() {
		return seqs;
	}

	public List<String> getSeq_names() {
		return seq_names;
	}

	public String getJsonOutputString() {
		return builder.toString();
	}
}
