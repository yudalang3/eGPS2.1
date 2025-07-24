package module.webmsaoperator.webIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import egps2.panels.dialog.SwingDialog;
import module.webmsaoperator.webIO.jsonBean.EnsemblRestAlignmentJsonModel;
import module.webmsaoperator.webIO.jsonBean.RestAlignments;

/**
 * 
 * <p>
 * Title: EnsemblRest
 * </p>
 * <p>
 * Description:
 * Enter the <code>String species, String region, String species_set_group</code>
 * Then it will return the alignment JSON format string!<br>
 * This class also packaging information of that string, so we can directly get <code>seqs and seqNames</code>!
 * </p>
 * 
 * @author yudalang
 * @date 2018-7-20
 * @help http://www.biotrainee.com/jmzeng/book/basic/database.html
 *       https://rest.ensembl.org/documentation/info/genomic_alignment_region
 */
public class RestGRegion2MSAOneStep {

	private String region;
	private String species;
	private String species_set_group;
	private String jsonOutputString;
	
	private boolean isSuccessful2GetAlign = false;
	
	private List<String> seqs = new ArrayList<>();
	private List<String> seq_names = new ArrayList<>();
	
	/**
	 * 
	 * @param species "homo_sapiens"
	 * @param region "2:106040000-106040050"
	 * @param species_set_group "primates"
	 */
	public RestGRegion2MSAOneStep(String species, String region, String species_set_group) {
		this.region = region;
		this.species = species;
		this.species_set_group = species_set_group;

		try {
			getJSONString();
			packagingInformation();
			isSuccessful2GetAlign = true;
		} catch (Exception e) {
			SwingDialog.showErrorMSGDialog("Get online alignment error!", "There are no alignments for the region  " + region);
			System.err.println("Error\t" + getClass() + "\t" + region);
			isSuccessful2GetAlign = false;
			return;
		}
	}
	public List<String> getSeqs() {
		return seqs;
	}
	public List<String> getSeq_names() {
		return seq_names;
	}
	
	public boolean isSucessful2GetAlign() {
		return isSuccessful2GetAlign;
	}

	private void packagingInformation() {
		
		List<RestAlignments> parsedRet = parseJson();
		
		for (int i = 0; i < parsedRet.size(); i++) {
			
			String spe_name = parsedRet.get(i).getSpecies();
			
			// yudalang: Abandon the a-b-v[3] like ancestray states
			if (spe_name.contains("[")) {continue;}
			// yudalang: if there are repeat genes
			if (seq_names.contains(spe_name)) {
				spe_name = spe_name + "-" +parsedRet.get(i).getSeq_region();
			}
			
			seq_names.add(spe_name);
			
			//System.out.println( parsedRet.get(i).getSpecies());
			seqs.add(parsedRet.get(i).getSeq());
			
		}
		
		//System.out.println(jsonOutputString);
	}

	private void getJSONString() throws Exception {
		// It's better to use http service, instead of https service
		String server = "http://rest.ensembl.org";
		//String server = "http://jul2018.rest.ensembl.org";
		String ext = null;
		//region = "1:16050508-16051008:1";
		if (species_set_group.equalsIgnoreCase("amniotes")) {
			/**
			 * According to https://rest.ensembl.org/alignment/region/homo_sapiens/1:1000000-1001000:1?content-type=application/json;species_set_group=amniotes;method=PECAN
			 * https://rest.ensembl.org/alignment/region/homo_sapiens/17:7661779-7687550?content-type=application/json;species_set_group=primates
			 * if you choose amniotes, method shoud change.
			 */
			ext = "/alignment/region/" + species + "/" + region + "?species_set_group=" + species_set_group+";method=PECAN";
		} else {
			ext = "/alignment/region/" + species + "/" + region + "?species_set_group=" + species_set_group;
		}
				
		System.out.println(server + ext + ";content-type=application/json");
		// when primate species set group, seq names error happen
		//http://rest.ensembl.org/alignment/region/homo_sapiens/8:144449582-144465677:-1?content-type=application/json;species_set_group=primates
		URL url = new URL(server + ext);

		URLConnection connection = url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) connection;

		httpConnection.setRequestProperty("Content-Type", "application/json");
		//17:7661779-7687550
		InputStream response = connection.getInputStream();
		int responseCode = httpConnection.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
		}

		Reader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder builder = new StringBuilder(5000);
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			jsonOutputString = builder.toString();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}

	}

	public String getJsonOutputString() {
		return jsonOutputString;
	}

	

	/**
	 * 
	 * @Title: parseJson
	 * @author mhl
	 * @param: json
	 * @Date Created on: 2018-07-21 13:03
	 * 
	 */
	private List<RestAlignments> parseJson() {

		List<RestAlignments> alignments = null;

		JSONArray jsonArray = JSONArray.parseArray(jsonOutputString);

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			EnsemblRestAlignmentJsonModel restModel = JSONObject.parseObject(jsonObject.toString(), EnsemblRestAlignmentJsonModel.class);

//			Map<String, Class> map = new HashMap<String, Class>();
//
//			map.put("alignments", RestAlignments.class);

			//EnsemblRestAlignmentJsonModel restModel = (jsonObject, EnsemblRestAlignmentJsonModel.class, map);

			alignments = restModel.getAlignments();

			//System.out.println(restModel.getTree());

		}
		return alignments;

	}
	


	/*public static void main(String[] args) throws Exception {
		//  homo_sapiens	primates / mammals
		EnsemblRestGetMSA ensemblRMaf = new EnsemblRestGetMSA("homo_sapiens", "2:106040000-106040050:1",
				"primates");
		System.out.println(ensemblRMaf.getSeqs());
		System.out.println(ensemblRMaf.getSeq_names());

		
//		int n = 7;
////	int length = n - 1;
////    double[][] distance = new double[length][];
////    
////    double[] temp0 = {19.0d};
////    double[] temp1 = {27.0d, 31.0d};
////    double[] temp2 = {8.0d, 18.0d, 26.0d};
////    double[] temp3 = {33.0d, 36.0d, 41.0d, 31.0d};
////    double[] temp4 = {18.0d, 1.0d, 32.0d, 17.0d, 35.0d};
////    double[] temp5 = {13.0d, 13.0d, 29.0d, 14.0d, 28.0d, 12.0d};
////    distance[0] = temp0;
////    distance[1] = temp1;
////    distance[2] = temp2;
////    distance[3] = temp3;
////    distance[4] = temp4;
////    distance[5] = temp5;
////    
////    Upgma upgma = new Upgma();
////    Node root = upgma.tree(distance, null).getRoot();
////    System.out.println();
//    
//    int n = 5;
//	int length = n - 1;
//    double[][] distance = new double[length][];
//    
//    double[] temp0 = {17.0d};
//    double[] temp1 = {21.0d, 30.0d};
//    double[] temp2 = {31.0d, 34.0d, 28.0d};
//    double[] temp3 = {23.0d, 21.0d, 39.0d, 43.0d};
//    distance[0] = temp0;
//    distance[1] = temp1;
//    distance[2] = temp2;
//    distance[3] = temp3;
//    
//    String[] names = {"a", "b", "c", "d", "e"};
//    Upgma upgma = new Upgma();
//    Node root = upgma.tree(distance, names);
//    
//    System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
		

	}*/
}
