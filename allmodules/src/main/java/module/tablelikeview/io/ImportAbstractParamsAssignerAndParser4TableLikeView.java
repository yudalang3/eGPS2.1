package module.tablelikeview.io;

import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import egps2.EGPSProperties;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;

public class ImportAbstractParamsAssignerAndParser4TableLikeView extends AbstractParamsAssignerAndParser4VOICE {

	public ImportAbstractParamsAssignerAndParser4TableLikeView() {
		super();

		addKeyValueEntryBean("import.file.path",
				EGPSProperties.PROPERTIES_DIR + "/bioData/example/treeBarplot/02_species_meta_info.txt",
				"The import data content, with tsv/csv format.\n#config/bioData/UCSC_100species_converter.csv");
		addKeyValueEntryBean("has.header", "T", "Whether has the header line.");
		addKeyValueEntryBean("seperate.key", "tab",
				"The record seperator, tsv file is the tab and csv file is the comma.\n# user can enter tab or comma");
		addKeyValueEntryBean("page.size", "100", "The number of lines per page, default is 100.");
//		addKeyValueEntryBean("show.row.index", "true",
//				"Whether show the row index");
//		addKeyValueEntryBean("show.column.index", "false",
//				"Whether show the column index");
	}

	private WorkImportInfoBean generateImportBean(Map<String, String> keyValueMap) {
		WorkImportInfoBean ret = new WorkImportInfoBean();

		String string = keyValueMap.get("$import.file.path");
		if (string != null) {
			ret.setFilePath(string);
		}
		string = keyValueMap.get("$has.header");
		if (string != null) {
			ret.setHasHeader(BooleanUtils.toBoolean(string));
		}
		string = keyValueMap.get("$page.size");
		if (string != null) {
			int int1 = Integer.parseInt(string);
			ret.setTableContentLineNumber(int1);
		}
		string = keyValueMap.get("$seperate.key");
		if (string != null) {
			if (string.equalsIgnoreCase("tab")) {
				ret.setSeperator('\t');
			} else {
				ret.setSeperator(',');
			}
		}

		return ret;
	}

	public WorkImportInfoBean getImportBeanFromString(String str) {
		Map<String, String> keyValueStringMap = getKeyValueStringMap(str);
		return generateImportBean(keyValueStringMap);
	}

}
