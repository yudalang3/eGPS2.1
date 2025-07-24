package module.gff3opr.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import utils.string.StringCounter;
import egps2.frame.ComputationalModuleFace;
import module.gff3opr.model.YuGFF3Parser;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;

@SuppressWarnings("serial")
public class GlimmpseOfTheGFF3File extends DockableTabModuleFaceOfVoice {

	public GlimmpseOfTheGFF3File(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	public String getShortDescription() {
		return null;
	}

	@Override
	public String getTabName() {
		return "1. Glimmpse the GFF3 file";
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.gff3.file", "",
				"Input the file path of the gff3 file. Common compressed format xz,gz,zip is supported.");

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		String inputGFF3File = o.getSimplifiedString("input.gff3.file");
		YuGFF3Parser yuGFF3Parser = new YuGFF3Parser();

		MutableObject<String> gffVersion = new MutableObject<>();

		Map<String, Integer> topType2countMap = new HashMap<>();

		StringCounter ncRNAtypeCounter = new StringCounter();
		StringCounter genetypeCounter = new StringCounter();

		yuGFF3Parser.parseGff3LineByLine(inputGFF3File, (line, feature) -> {

			if (feature == null) {
				if (line.length() > 13 && line.contains("##gff-version")) {
					gffVersion.setValue(line);
				}
			} else {
				String attribute = feature.getAttribute("Parent");
				if (attribute == null) {
					String type = feature.type();

					Integer integer = topType2countMap.get(type);
					if (integer == null) {
						topType2countMap.put(type, 1);
					} else {
						topType2countMap.put(type, ++integer);
					}

					if (Objects.equal("gene", type)) {
						genetypeCounter.addOneEntry(feature.getAttribute("biotype"));
					} else if (Objects.equal("ncRNA_gene", type)) {
						ncRNAtypeCounter.addOneEntry(feature.getAttribute("biotype"));
					}

				}
			}

			return false;
		});

		List<String> outputs = new LinkedList<>();

		outputs.add(gffVersion.getValue());
		outputs.add("The top feature counts are:");
		for (Entry<String, Integer> entry : topType2countMap.entrySet()) {
			
			String padEnd = Strings.padEnd(entry.getKey(), 30, ' ');
			outputs.add(padEnd + entry.getValue());
		}
		outputs.add("gene biotypes are:");
		for (Entry<String, MutableInt> entry : genetypeCounter.getCounterMap().entrySet()) {
			String padEnd = Strings.padEnd(entry.getKey(), 30, ' ');
			outputs.add(padEnd + entry.getValue());
		}
		outputs.add("ncRNA_gene biotypes are:");
		for (Entry<String, MutableInt> entry : ncRNAtypeCounter.getCounterMap().entrySet()) {
			String padEnd = Strings.padEnd(entry.getKey(), 30, ' ');
			outputs.add(padEnd + entry.getValue());
		}

		setText4Console(outputs);

	}

}
