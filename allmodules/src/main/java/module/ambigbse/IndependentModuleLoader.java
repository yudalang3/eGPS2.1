package module.ambigbse;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolModuleFace;
import egps2.modulei.ModuleClassification;
import fasta.io.FastaReader;

@SuppressWarnings("serial")
public class IndependentModuleLoader extends DIYToolModuleFace {

	public IndependentModuleLoader() {
		super(null);
	}

	@Override
	public String getTabName() {
		return "Ambiguous Nucl to concrete";
	}

	@Override
	public String getShortDescription() {
		return "ElegantJTable convenient graphic tool to make the nucleotide sequence to concrete sequence.";
	}


	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_SIMPLE_TOOLS_INDEX,
				ModuleClassification.BYAPPLICATION_GENOMICS_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER
		);
		return ret;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.fasta.path", "",
				"The file path has the ambiguous nucleotide sequence, in fasta format. Necessary");
		mapProducer.addKeyValueEntryBean("input.ambuguous.sequence", "CTTTGWWS;AGAWAW",
				"Directly input the ambiguous nucleotide sequence, with ; as splitter. If user both set the $input.fasta.path and $input.ambuguous.sequence, only take this one.");
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputFilePath = null;
		String inputAmbuguousSeq = null;

		String string = para.get("$input.fasta.path");
		if (Strings.isNullOrEmpty(string)) {

		} else {
			inputFilePath = string;
		}
		string = para.get("$input.ambuguous.sequence");
		if (Strings.isNullOrEmpty(string)) {

		} else {
			inputAmbuguousSeq = string;
		}


		LinkedHashMap<String, String> fastaDNASequence = null;
		AmbiguousNuclBase runner = new AmbiguousNuclBase();
		List<String> ret = Lists.newArrayList();
		
		if (!Strings.isNullOrEmpty(inputAmbuguousSeq)) {
			fastaDNASequence = new LinkedHashMap<>();

			String[] splits = inputAmbuguousSeq.split(";");
			int index = 1;
			for (String string2 : splits) {
				fastaDNASequence.put("seq" + index, string2);
				index++;
			}
		} else if (!Strings.isNullOrEmpty(inputFilePath)) {
			fastaDNASequence = FastaReader.readFastaDNASequence(new File(inputFilePath));

		}else {
			throw new IllegalArgumentException("Please input at last one parameter");
		}
		

		
		for (Entry<String, String> entry : fastaDNASequence.entrySet()) {
			ret.add(entry.getKey());
			runner.guiUsage(entry.getValue());
			List<String> strings4output = runner.getOutputStrs();
			ret.addAll(strings4output);
		}

		this.setText4Console(ret);
	}
}
