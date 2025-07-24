package module.tableleftjoin;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolModuleFace;
import egps2.modulei.ModuleClassification;

@SuppressWarnings("serial")
public class IndependentModuleLoader extends DIYToolModuleFace {

	public IndependentModuleLoader() {
		super(null);
	}

	@Override
	public String getTabName() {
		return "Two table LeftJoin";
	}

	@Override
	public String getShortDescription() {
		return "Quick query column values according to the reference table, large file is supported.";
	}


	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_SIMPLE_TOOLS_INDEX,
				ModuleClassification.BYAPPLICATION_COMMON_MODULE_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER
		);
		return ret;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("query.table.path", "",
				"The file path has the query table, file format is tsv. Necessary");

		mapProducer.addKeyValueEntryBean("reference.table.path", "",
				"The file path has the reference table, file format is tsv. Necessary");

		mapProducer.addKeyValueEntryBean("output.table.path", "",
				"The file path for output, file format is tsv. Necessary");

		mapProducer.addKeyValueEntryBean("query.column.index", "1",
				"This is the column index need to query (First is 1)");

		mapProducer.addKeyValueEntryBean("ref.column.index", "1",
				"This is the column index need for reference, (First is 1)");

		mapProducer.addKeyValueEntryBean("query.file.hasHeader", "F",
				"If the query file has header line.");
		mapProducer.addKeyValueEntryBean("ref.file.hasHeader", "F",
				"If the reference file has header line.");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String refPath = null;
		String queryFilePath = null;
		String outputPath = null;
		int queryColumnIndex = 0;
		int refColumnIndex = 0;

		boolean queryHasHeader = false;
		boolean refHasHeader = false;

		String string = para.get("$query.table.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the query file path.");
		} else {
			queryFilePath = string;
		}

		string = para.get("$reference.table.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the need to reference file path.");
		} else {
			refPath = string;
		}
		string = para.get("$output.table.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the need to output file path.");
		} else {
			outputPath = string;
		}

		string = para.get("$query.column.index");
		if (string != null) {
			queryColumnIndex = Integer.parseInt(string) - 1;
		}

		string = para.get("$ref.column.index");
		if (string != null) {
			refColumnIndex = Integer.parseInt(string) - 1;
		}
		string = para.get("$query.file.hasHeader");
		if (string != null) {
			queryHasHeader = BooleanUtils.toBoolean(string);
		}
		string = para.get("$ref.file.hasHeader");
		if (string != null) {
			refHasHeader = BooleanUtils.toBoolean(string);
		}

		LeftJoinTable runner = new LeftJoinTable();
		runner.setInputPath(queryFilePath);
		runner.setRefernecedTablePath(refPath);
		runner.setOutputPath(outputPath);
		runner.setQueryColumnIndex(queryColumnIndex);
		runner.setReferenceColumnIndex(refColumnIndex);
		runner.setQueryFileHasHeader(queryHasHeader);
		runner.setRefFileHasHeader(refHasHeader);
		
		runner.doIt();

		List<String> strings4output = runner.strings4output;

		this.setText4Console(strings4output);
	}
}
