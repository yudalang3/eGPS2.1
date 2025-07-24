package module.treenodecoll;

import module.evoltreio.ParamsAssignerAndParser4PhyloTree;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class ParamsAssignerAndParser4TreeNodeInfoCollector extends ParamsAssignerAndParser4PhyloTree {
	
	public ParamsAssignerAndParser4TreeNodeInfoCollector() {
		super();
		addKeyValueEntryBean("ladderize", "0", "0 for no ladderize, 1 for ladderize up, 2 for ladderize down.");
		addKeyValueEntryBean("collect.node.type", "2", "0 means all nodes, 1 means internal node, 2 means leaf.");
		
		String userHome = System.getProperty("user.home");
		String string = userHome.concat("/example.txt").replaceAll("\\\\", "/");
		addKeyValueEntryBean("export.path", string, "The export path for the results.");
	}
	
	@Override
	public TreeLeafObtainerImportInfoBean generateTreeFromKeyValue(OrganizedParameterGetter input) {
		TreeLeafObtainerImportInfoBean ret = new TreeLeafObtainerImportInfoBean();
		assignValues(input, ret);
		
		
		String string = input.getSimplifiedStringWithDefault("$ladderize");
		if (!string.isEmpty()) {
			ret.setLadderize(Integer.parseInt(string));
		}
		
		string = input.getSimplifiedStringWithDefault("$export.path");
		if (!string.isEmpty()) {
			ret.setExportPath(string);
		}
		string = input.getSimplifiedStringWithDefault("$collect.node.type");
		if (!string.isEmpty()) {
			ret.setCollectNodeType(Integer.parseInt(string));
		}
		
		return ret;
	}


}
