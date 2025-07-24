package module.filegreper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import utils.string.EGPSStringUtil;
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
		return "File greper";
	}

	@Override
	public String getShortDescription() {
		return "ElegantJTable convenient graphic tool to find target lines as the Linux command grep.";
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
		mapProducer.addKeyValueEntryBean("input.file.path", "",
				"The target file path for search, a text file, Necessary.");
		mapProducer.addKeyValueEntryBean("query.string", "my",
				"The search key string, multiple string is supported (with | char), i.e. ENSP00000488240.1|ENSP00000487941.1. Necessary");
		mapProducer.addKeyValueEntryBean("following.line.number", "0",
				"The following lines after the searched line to output. Necessary");
		mapProducer.addKeyValueEntryBean("export.header", "F", "Whether export header line.");
		mapProducer.addKeyValueEntryBean("results.limitation.count", Integer.toString(Integer.MAX_VALUE),
				"The maximun number of results to output. For e.g.: 1 only output one searched line content.");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {

		String queryString = null;
		String inputFilePath = null;
		boolean exportHeaderLine = false;
		int neiberLineNumber = 0;
		int searchedResultsLimitation = 0;

		String string = para.get("$input.file.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $input.file.path");
		} else {
			inputFilePath = string;
		}

		string = para.get("$query.string");
		if (Strings.isNullOrEmpty(string)) {
			// this means query all strings.
			queryString = "";
		} else {
			queryString = string;
		}

		string = para.get("$export.header");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $export.header");
		} else {
			exportHeaderLine = BooleanUtils.toBoolean(string);
		}

		string = para.get("$following.line.number");
		if (string != null) {
			neiberLineNumber = Integer.parseInt(string);
		}

		string = para.get("$results.limitation.count");
		if (string != null) {
			searchedResultsLimitation = Integer.parseInt(string);
			if (searchedResultsLimitation < 1) {
				throw new IllegalArgumentException("Parameter $results.limitation.count should greater 0.");
			}
		}

		DoGrepAction doGrepAction = new DoGrepAction();
		doGrepAction.inputFilePath = inputFilePath;
		doGrepAction.searchKeys = EGPSStringUtil.split(queryString, '|');
		doGrepAction.exportHeader = exportHeaderLine;
		doGrepAction.followingLineNumber = neiberLineNumber;
		doGrepAction.searchedResultsLimitation = searchedResultsLimitation;

		long timeMillis = System.currentTimeMillis();

		doGrepAction.doIt();
		List<String> strings4output = doGrepAction.getOutputList();

		long thisTimeMillis = System.currentTimeMillis();
		strings4output.add("Take time of  " + (thisTimeMillis - timeMillis) + " milliseconds to grep.");

		this.setText4Console(strings4output);

	}
}
