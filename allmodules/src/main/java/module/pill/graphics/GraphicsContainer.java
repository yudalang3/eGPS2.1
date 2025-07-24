package module.pill.graphics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * 根据 symbols, 这个 objects就是 entities objects包括 gene, compound and map groups是
 * objects的组合
 */
public class GraphicsContainer {

	List<GraphBaseNotation> geneObjs = Lists.newArrayList();
	List<GraphBaseNotation> compoundObjs = Lists.newArrayList();

	// KEGG map，是一个特定的术语。不是现实生活中的地图奥
	List<GraphBaseNotation> mapObjs = Lists.newArrayList();

	// objecs的集合
	List<GraphBaseNotation> groups = Lists.newArrayList();

	// protein-protein relation ship
	List<GraphBaseRelation> proteinProteinRelations = Lists.newArrayList();
	// protein-compound interaction
	List<GraphBaseRelation> proteinCompoundRelations = Lists.newArrayList();

	public List<GraphBaseNotation> getGeneObjs() {
		return geneObjs;
	}

	public List<GraphBaseNotation> getCompoundObjs() {
		return compoundObjs;
	}

	public List<GraphBaseNotation> getMapObjs() {
		return mapObjs;
	}

	public List<GraphBaseNotation> getGroups() {
		return groups;
	}

	public List<GraphBaseRelation> getProteinProteinRelations() {
		return proteinProteinRelations;
	}

	public List<GraphBaseRelation> getProteinCompoundRelations() {
		return proteinCompoundRelations;
	}

	public Map<Integer, GraphBaseNotation> getID2GraphicNodeMap() {
		Map<Integer, GraphBaseNotation> ret = new HashMap<>();

		for (GraphBaseNotation graphBaseNotation : geneObjs) {
			ret.put(graphBaseNotation.getId(), graphBaseNotation);
		}
		for (GraphBaseNotation graphBaseNotation : compoundObjs) {
			ret.put(graphBaseNotation.getId(), graphBaseNotation);
		}
		for (GraphBaseNotation graphBaseNotation : mapObjs) {
			ret.put(graphBaseNotation.getId(), graphBaseNotation);
		}
		for (GraphBaseNotation graphBaseNotation : groups) {
			ret.put(graphBaseNotation.getId(), graphBaseNotation);
		}

		return ret;
	}

}
