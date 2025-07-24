package primary.struct.display.data;

import com.google.common.collect.Maps;

import java.util.Map;

public class GeneDataV2 {
	private Map<String, GeneV2> genes = Maps.newLinkedHashMap();

	public GeneDataV2() {

	}

	// Getters and Setters
	public Map<String, GeneV2> getGenes() {
		return genes;
	}

	public void setGenes(Map<String, GeneV2> genes) {
		this.genes = genes;
	}

}
