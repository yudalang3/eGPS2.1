package primary.struct.display.data;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public class GeneData {
	private Map<String, Gene> genes;

	public GeneData() {

	}

	public GeneData(String jsonString) {
		com.alibaba.fastjson.TypeReference<Map<String, Gene>> typeReference = new com.alibaba.fastjson.TypeReference<>() {};
		genes = JSON.parseObject(jsonString, typeReference);
	}

	// Getters and Setters
	public Map<String, Gene> getGenes() {
		return genes;
	}

	public void setGenes(Map<String, Gene> genes) {
		this.genes = genes;
	}

}
