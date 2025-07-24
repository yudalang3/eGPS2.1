package rest.ensembl.phylo;

import analysis.AbstractAnalysisAction;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import evoltree.phylogeny.DefaultPhyNode;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;

public class EnsJsonTreeParser extends AbstractAnalysisAction {
	
	JsonTreeBean parseObject;

	@Override
	public void doIt() throws Exception {

		String readFileToString = FileUtils.readFileToString(new File(inputPath), StandardCharsets.UTF_8);

		parseObject = JSONObject.parseObject(readFileToString, JsonTreeBean.class);

	}

	
	public JsonTreeBean getParseObject() {
		return parseObject;
	}
	
	public DefaultPhyNode getTheGraphicsTree() {
		if (parseObject == null) {
			throw new InputMismatchException("Call the doIt method first.");
		}
		TreeBean tree = parseObject.getTree();
		DefaultPhyNode ret = iterate2transferTree(tree);
		return ret;
	}


	protected DefaultPhyNode iterate2transferTree(TreeBean tree) {
		DefaultPhyNode node = new DefaultPhyNode();
		node.setLength(tree.branch_length);
		
		String humanNamedName = tree.getTaxonomy().getCommon_name();
		if (Strings.isEmpty(humanNamedName)) {
			humanNamedName = tree.getTaxonomy().getScientific_name();
		}
		
		List<TreeBean> children = tree.children;
		if (children == null) {
			node.setName(humanNamedName);
			return node;
		}
		
		String name = Joiner.on('|').join(humanNamedName,tree.taxonomy.timetree_mya);
		node.setName(name);
		for (TreeBean treeBean : children) {
			DefaultPhyNode child = iterate2transferTree(treeBean);
			node.addChild(child);
		}
		
		return node;
	}
	

}
