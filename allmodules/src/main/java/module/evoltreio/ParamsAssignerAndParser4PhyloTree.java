package module.evoltreio;

import egps2.EGPSProperties;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class ParamsAssignerAndParser4PhyloTree extends AbstractParamsAssignerAndParser4VOICE {


	public ParamsAssignerAndParser4PhyloTree() {
		addKeyValueEntryBean("input.nwk.string", "", "Direct input the nwk string content. High priority.");
		addKeyValueEntryBean("input.nwk.path",
				EGPSProperties.PROPERTIES_DIR + "/bioData/example/treeBarplot/SpeciesTree.nwk",
				"Input the nwk file path.");
		addKeyValueEntryBean("remove.whitespace", "F",
				"Whether remove whitespace (spaces, tabs, carriage returns, and linefeed) for the nwk file. Default False.");
		addKeyValueEntryBean("input.tableLike.path", "", "Input the table-like tree file path.");
	}


	/**
	 * 根据键值对生成进化树解析所需信息。
	 *
	 * @return 生成的树结构信息对象
	 */
	public EvolTreeImportInfoBean generateTreeFromKeyValue(OrganizedParameterGetter o) {
		EvolTreeImportInfoBean ret = new EvolTreeImportInfoBean();
		assignValues(o, ret);
		return ret;
	}

	public void assignValues(OrganizedParameterGetter o, EvolTreeImportInfoBean ret) {
		// 解析的时候不要忘记把 $ 符号加进去
		String string = o.getSimplifiedStringWithDefault("input.nwk.string");
		if (!string.isEmpty()) {
			ret.setInput_nwk_string(string);
		}

		string = o.getSimplifiedStringWithDefault("input.nwk.path");
		if (!string.isEmpty()) {
			ret.setInput_nwk_path(string);
		}

		string = o.getSimplifiedStringWithDefault("input.tableLike.path");
		if (!string.isEmpty()) {
			ret.setInput_tableLike_path(string);
		}
		if (o.isSimplifiedValueExist("remove.whitespace")) {
			boolean simplifiedBool = o.getSimplifiedBool("remove.whitespace");
			ret.setRemoveWhitespace(simplifiedBool);
		}

		
	}

}
