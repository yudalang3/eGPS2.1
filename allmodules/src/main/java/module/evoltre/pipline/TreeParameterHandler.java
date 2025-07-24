package module.evoltre.pipline;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import egps2.EGPSProperties;
import utils.storage.MapPersistence;

public class TreeParameterHandler {
	private final String BUILD_TREE = EGPSProperties.PROPERTIES_DIR.concat("/build.tree.setting.json");
	private final String UCSC_SPECIES_PRO = EGPSProperties.PROPERTIES_DIR.concat("/ucsc.species.info.json");
	private final String ENSEMBL_SPECIES_PRO = EGPSProperties.PROPERTIES_DIR.concat("/ensembl.species.info.json");
	private final String ENSEMBL_GENOME_MSA_INFO_PRO = EGPSProperties.PROPERTIES_DIR
			.concat("/ensembl.genome.msa.species.info.json");

	public Map<String, String> getBuildTreeParametersMap() {

		String path = BUILD_TREE;
		if (!new File(BUILD_TREE).exists()) {
			path = EGPSProperties.PROPERTIES_DIR.concat("/build.tree.setting.default.json");
		}
		try {
			return MapPersistence.getStr2StrMapFromJSON(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.emptyMap();

	}

	public void saveBuildTreeParametersMap(Map<String, String> map) {
		try {
			MapPersistence.saveStr2StrMapToJSON(map, BUILD_TREE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getUCSCSpeciesPropertiesMap() {
		try {
			return MapPersistence.getStr2StrMapFromJSON(UCSC_SPECIES_PRO);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.emptyMap();

	}

	public void saveUCSCSpeciesPropertiesMap(Map<String, String> map) {
		try {
			MapPersistence.saveStr2StrMapToJSON(map, UCSC_SPECIES_PRO);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Map<String, String> getEnsembelSpeciesPropertiesMap() {
		try {
			return MapPersistence.getStr2StrMapFromJSON(ENSEMBL_SPECIES_PRO);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.emptyMap();

	}

	public void saveEnsembelSpeciesPropertiesMap(Map<String, String> map) {
		try {
			MapPersistence.saveStr2StrMapToJSON(map, ENSEMBL_SPECIES_PRO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

//		HashMap<String, String> parameterMap = new HashMap<>();
//		SAXReader reader = new SAXReader();
//		Document document;
//
//		
//		try {
//			document = reader.read(new File("config/ucsc_properties.xml"));
//			Element root = document.getRootElement();
//			List<Element> List = root.elements();
//
//			for (Element e : List) {
//				String attribute = (String) e.element("Common_name").getData();
//				parameterMap.put(attribute, e.element("Scientific_name").getText());
//			}
//		} catch (DocumentException e1) {
//			e1.printStackTrace();
//		}
//		
//		File file = new File(UCSC_SPECIES_PRO);
//		
//		
//		String jsonString = JSONObject.toJSONString(parameterMap, true);
//		FileUtils.writeStringToFile(file, jsonString,StandardCharsets.UTF_8);

		/**
		 * 注意 species_properties.xml 这个类还没转，因为它比较复杂，而这个文件又貌似和ucsc_properties.xml
		 * 耦合在一起，先不动了。 ucsc_properties.xml setting_properties.xml 和
		 * ensembl_properties.xml 都已经处理过了 所以现在xml还剩下两个
		 */

	}
}
