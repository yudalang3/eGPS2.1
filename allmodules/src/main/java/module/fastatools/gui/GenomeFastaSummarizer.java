package module.fastatools.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import fasta.io.FastaReader;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.util.Strings;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

import module.genome.EnsemblFastaName;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModuleRunner;

public class GenomeFastaSummarizer implements SubTabModuleRunner {

	@Override
	public String getShortDescription() {
		return "Quick summary the fasta file for genome data";
	}

	@Override
	public String getTabName() {
		return "Genome Fasta Summarizer";
	}

	@Override
	public void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.fasta.path", "",
				"The file path of the fasta, compressed file is supported.");
		mapProducer.addKeyValueEntryBean("keep.field2.contains", "",
				"Only the field 2 contains the value will processed, default is no filtering string.");
		mapProducer.addKeyValueEntryBean("keep.field4.contains", "",
				"Only the field 4 equal to this value will processed, default is no filtering string.\n # Values could be REF, HAP");

	}

	@Override
	public List<String> execute(OrganizedParameterGetter o) throws Exception {
		Stopwatch stopwatch = Stopwatch.createStarted();

		String inputFastaPath = o.getSimplifiedString("input.fasta.path");
		String keepField2Contains = o.getSimplifiedStringWithDefault("keep.field2.contains");
		String keepField4Contains = o.getSimplifiedStringWithDefault("keep.field4.contains");

		List<String> outputs = perform(inputFastaPath, keepField2Contains, keepField4Contains);

		stopwatch.stop();
		outputs.add("Elapsed time: ".concat(stopwatch.toString()));
		return outputs;
	}

	private List<String> perform(String inputFastaPath, String keepField2Contains, String keepField4Contains)
			throws IOException {

		LinkedHashMap<String, String> map = Maps.newLinkedHashMap();

		FastaReader.readAndProcessFastaPerEntry(inputFastaPath, (name, seq) -> {
			EnsemblFastaName ensemblFastaName = new EnsemblFastaName(name);

			if (Strings.isNotEmpty(keepField2Contains)) {
				String type = ensemblFastaName.getType();
				if (!type.contains(keepField2Contains)) {
					return false;
				}
			}
			if (Strings.isNotEmpty(keepField4Contains)) {
				Optional<String> sequenceType2 = ensemblFastaName.getSequenceType();
				if (sequenceType2.isPresent()) {
					String sequenceType = sequenceType2.get();
					if (!Objects.equal(sequenceType, keepField4Contains)) {
						return false;
					}
				} else {
					return false;
				}


			}

			map.put(name, name);
			return false;
		});

		ArrayList<String> rets = Lists.newArrayList();
		for (Entry<String, String> entry : map.entrySet()) {
			rets.add(entry.getKey());
		}

		return rets;
	}

}
