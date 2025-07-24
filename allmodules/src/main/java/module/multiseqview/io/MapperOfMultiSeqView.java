package module.multiseqview.io;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import utils.string.EGPSStringUtil;
import egps2.EGPSProperties;
import graphic.engine.colors.EGPSColors;
import module.multiseqview.model.Data4SequenceStructureView;
import module.multiseqview.model.Sequence4import;
import module.multiseqview.model.StructureElement;
import rest.ensembl.ensembrest.OverlapTransBeanParser;
import rest.ensembl.ensembrest.OverlapTransElementBean;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;
import fasta.io.FastaReader;

public class MapperOfMultiSeqView extends AbstractParamsAssignerAndParser4VOICE {

	public MapperOfMultiSeqView() {
		super();
		addKeyValueEntryBean("target.type", "Pfam", "Specify the interest annotation type in the json file.");

		String pathHeader = EGPSProperties.PROPERTIES_DIR;
		addKeyValueEntryBean("alignment.file.path",
				pathHeader + "/bioData/sequenceView/multiSeq/Wnt.features.human/prot.human.Wnt.fa",
				"Specify the fasta path, import a file path. One sequence or multisequence both allowed.");

		StringBuilder sBuider = new StringBuilder();
		sBuider.append("Specify the dir path of the sequence features, the format is json.\n");
		sBuider.append("# It will get the needed json file from the input alignment file.\n");
		sBuider.append("# e.g.: the fasta file has 2 sequence >a and >b\n");
		sBuider.append("# The program will get the a.json and b.json from this dir.");
		addKeyValueEntryBean("browser.struct.annotation.dir.path",
				pathHeader + "/bioData/sequenceView/multiSeq/Wnt.features.human",
				sBuider.toString());

		addKeyValueEntryBean("sequence.name.translation.path",
				pathHeader + "/bioData/sequenceView/multiSeq/WNT_human_allGenes.tsv",
				"Specify the meta information of the sequences. The seq. name in the fasta file may be not human-readable, so this will help user understandable.");
		addKeyValueEntryBean("needTo.translate.columnIndex", "10",
				"Specify sequence ID that need to translate, also it is the string to identify the sequence.");
		addKeyValueEntryBean("wanted.name.columnIndex", "8",
				"Specify the column index in the $sequence.name.translation.path for the target name.");
	}


	public ImportInfoBean4gBrowser buildFromListOfString(List<String> strs) {
		ImportInfoBean4gBrowser bean = new ImportInfoBean4gBrowser();

		LinkedList<String> filtered = Lists.newLinkedList();
		strs.forEach(str -> {
			if (!str.startsWith("#")) {
				filtered.add(str);
			}
		});

		VoiceParameterParser parser = new VoiceParameterParser();
		Map<String, LinkedList<String>> map = parser.parseInputString4organization(filtered);

		{
			LinkedList<String> linkedList = map.get("$target.type");
			if (linkedList != null) {
				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				bean.setTargetType(stringAfterEqualStr);
			}
		}
		{
			LinkedList<String> linkedList = map.get("$browser.struct.annotation.dir.path");
			if (linkedList != null) {
				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				bean.setProteinStructAnnotationPath(stringAfterEqualStr);
			}
		}

		{
			LinkedList<String> linkedList = map.get("$alignment.file.path");
			if (linkedList != null) {
				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				bean.setProteinSequenceFastaPath(stringAfterEqualStr);
			}
		}
		{
			LinkedList<String> linkedList = map.get("$sequence.name.translation.path");
			if (linkedList != null) {
				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				if (stringAfterEqualStr.length() > 0) {
					bean.setSequenceNameTranlationPath(stringAfterEqualStr);
				}
			}
		}
		{
			LinkedList<String> linkedList = map.get("$needTo.translate.columnIndex");
			if (linkedList == null) {
				throw new IllegalArgumentException("Please input the parameters of: $needTo.translate.columnIndex");
			} else {
				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				bean.setNeedToTransferIDIndex(Integer.parseInt(stringAfterEqualStr));
			}
		}
		{
			LinkedList<String> linkedList = map.get("$wanted.name.columnIndex");
			if (linkedList == null) {
				throw new IllegalArgumentException("Please input the parameters of: $wanted.name.columnIndex");
			} else {
				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				bean.setWantedNameIndex(Integer.parseInt(stringAfterEqualStr));
			}
		}

		return bean;
	}

	public Data4SequenceStructureView prepareData(ImportInfoBean4gBrowser buildFromListOfString) throws IOException {
		Color[] predinedCellChatColors = EGPSColors.getPredinedCellChatColors();

		/**
		 * 这里的读取逻辑是：先读取fasta文件， 然后从fasta文件中寻找序列名字， 根据名字拼接从一个json文件的文件名。 所以这要求json文件的格式是
		 * name.json
		 */
		String targetType2 = buildFromListOfString.getTargetType();
		LinkedHashMap<String, String> fastaFile = FastaReader
				.readFastaDNASequence(new File(buildFromListOfString.getProteinSequenceFastaPath()));
		File dir = new File(buildFromListOfString.getProteinStructAnnotationPath());

		Optional<String> transverPath = buildFromListOfString.getSequenceNameTranlationPath();
		HashMap<String, String> nameTransferMap = Maps.newLinkedHashMap();
		if (transverPath.isPresent()) {

			// 需要将1-base转成 0-based
			int needToTransferIDIndex2 = buildFromListOfString.getNeedToTransferIDIndex() - 1;
			int wantedNameIndex2 = buildFromListOfString.getWantedNameIndex() - 1;

			List<String> readAllLines = Files.readAllLines(Paths.get(transverPath.get()));
			Iterator<String> iterator = readAllLines.iterator();
			iterator.next();

			while (iterator.hasNext()) {
				String next = iterator.next();
				String[] split = EGPSStringUtil.split(next, '\t');
				String key = split[needToTransferIDIndex2];
				String value = split[wantedNameIndex2];
				nameTransferMap.put(key, value);
			}
		} else {
			throw new InputMismatchException("Please input the file in the $sequenceNameTranlationPath.");
		}

		OverlapTransBeanParser overlapTransBeanParser = new OverlapTransBeanParser();

		Data4SequenceStructureView ret = new Data4SequenceStructureView();
		List<Sequence4import> sequences = new ArrayList<Sequence4import>();

		for (Entry<String, String> entry : nameTransferMap.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();

			String sequenceContent = fastaFile.get(name);
			Objects.requireNonNull(sequenceContent,
					"Found name in your input translation file not have sequence: ".concat(name));

			Sequence4import sequence = new Sequence4import();

			sequence.setName(value);
			sequence.setLength(sequenceContent.length());

			File jsonFile = new File(dir, name.concat(".json"));
			Map<String, List<OverlapTransElementBean>> type2listBeanMap = overlapTransBeanParser.parse(jsonFile);
			List<OverlapTransElementBean> list = type2listBeanMap.get(targetType2);
			if (list == null) {
				throw new InputMismatchException("The type name is not exists ".concat(targetType2));
			}

			List<StructureElement> structureElements = new ArrayList<>();

			for (OverlapTransElementBean sequence2 : list) {
				StructureElement se1 = new StructureElement();
				se1.setName(sequence2.getDescription());
				se1.setStart(Integer.parseInt(sequence2.getStart()));
				se1.setEnd(Integer.parseInt(sequence2.getEnd()));
				structureElements.add(se1);
			}

			Collections.sort(structureElements);
			int index = 0;
			for (StructureElement entry1 : structureElements) {
				Color color = predinedCellChatColors[index];
				entry1.setColorString(EGPSColors.toHexFormColor(color));
				index++;
			}

			sequence.setStructureElementLists(structureElements);
			sequences.add(sequence);
		}

		ret.setSequences(sequences);
		return ret;

	}

}
