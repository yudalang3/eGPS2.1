package module.webmsaoperator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import egps2.EGPSProperties;

public class SpeciesNameTrans {
	
	List<String[]> dataForStorage;
	/**
	 * Input common name. out put String[3]
	 * Defaultnew String[] {"Human","Homo_sapiens","hg38"};
	 * Note: sci name will join with _
	* @author yudalang
	 */
	public String[] getdatFromCommonName(String comName) {
		if (dataForStorage == null) {
			loadData();
		}
		for (String[] strings : dataForStorage) {
			if (strings[0].equalsIgnoreCase(comName)) {
				return strings;
			}
		}
		
		return new String[] {"Human","Homo_sapiens","hg38"};
	}
	
	private void loadData() {
		dataForStorage = new ArrayList<>(100);
		final String fileName = EGPSProperties.PROPERTIES_DIR + "/bioData/UCSC_100species_converter.csv";

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] strs = line.split(",");
				strs[1] = strs[1].replace(' ', '_');
				dataForStorage.add(strs);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * input name Don't have "_";
	* @author yudalang
	* @date 2019年4月28日
	 */
	public String[] getdatFromSciName(String name) {
		if (dataForStorage == null) {
			loadData();
		}
		for (String[] strings : dataForStorage) {
			if (strings[1].equalsIgnoreCase(name)) {
				return strings;
			}
		}
		
		return new String[] {"Human","Homo_sapiens","hg38"};
	}
	
	
	/**
	 * input name Don't have "_";
	* @author yudalang
	* @date 2019-4-28
	 */
	public String[] getdatFromAssemblyName(String name) {
		if (dataForStorage == null) {
			loadData();
		}
		for (String[] strings : dataForStorage) {
			if (strings[2].equalsIgnoreCase(name)) {
				return strings;
			}
		}
		
		return new String[] {"Human","Homo_sapiens","hg38"};
	}
	
	
	public static void main(String[] args) {
		SpeciesNameTrans speciesNameTrans = new SpeciesNameTrans();
		String[] tt = speciesNameTrans.getdatFromAssemblyName("speTri2");
		System.out.println(Arrays.toString(tt));
		
		tt = speciesNameTrans.getdatFromCommonName("Squirrel");
		System.out.println(Arrays.toString(tt));
	}

}
