package module.evolview.genebrowser.io;

import java.util.InputMismatchException;

import com.google.common.base.Strings;

import egps2.EGPSProperties;
import module.evolview.genebrowser.GBRequiredGeneData_Prot;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class MapperOfGeneBrowser extends AbstractParamsAssignerAndParser4VOICE {

	public MapperOfGeneBrowser() {

		addKeyValueEntryBean("browser.input.type", "0",
				"Specify the struct file. 0 for json format, 1 for the normal tsv format. If choose 0, $browser.struct.annotation.path should be 0.");

		addKeyValueEntryBean("alignment.file.path",
				EGPSProperties.PROPERTIES_DIR + "/bioData/gBrowser/prot.human.Norrin.fa",
				"Specify the fasta path, import a file path. One sequence or multisequence both allowed.");

		addKeyValueEntryBean("browser.struct.annotation.path",
				EGPSProperties.PROPERTIES_DIR + "/bioData/gBrowser/ENSP00000495972.json",
				"Specify the struct file, import a file path. If browser.input.type=0, the path is json; =1, the path is tsv file.");

		addKeyValueEntryBean("keyFeature.file.path", EGPSProperties.PROPERTIES_DIR + "/bioData/gfamily/key_domains.tsv",
				"The key domains file, optional, can be null. Only work for browser.input.type=1.");
		addKeyValueEntryBean("primers.file.path", EGPSProperties.PROPERTIES_DIR + "/bioData/gfamily/primers.txt",
				"The primer file, optional, can be null. Only work for browser.input.type=1.");
	}

	public RequiredGeneData parseImportedData(OrganizedParameterGetter str) {
		return parserImportDataFromMap(str);
	}

	private RequiredGeneData parserImportDataFromMap(OrganizedParameterGetter keyValueStringMap) {
		RequiredGeneData ret = null;
		int typeOfInput = 0;
		String alignmentFilePath;
		String browserStructAnnoPath;

		String string = keyValueStringMap.getSimplifiedStringWithDefault("$browser.input.type");
		if (Strings.isNullOrEmpty(string)) {
			throw new InputMismatchException("The parameter browser.input.type is mandatory.");
		} else {
			typeOfInput = Integer.parseInt(string);
		}

		string = keyValueStringMap.getSimplifiedStringWithDefault("$alignment.file.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new InputMismatchException("The parameter alignment.file.path is mandatory.");
		} else {
			alignmentFilePath = string;
		}

		string = keyValueStringMap.getSimplifiedStringWithDefault("$browser.struct.annotation.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new InputMismatchException("The parameter alignment.file.path is mandatory.");
		} else {
			browserStructAnnoPath = string;
		}

		if (typeOfInput == 0) {
			GBRequiredGeneData_Prot requiredGeneData = new GBRequiredGeneData_Prot();
			requiredGeneData.protFastaFile = alignmentFilePath;
			requiredGeneData.protFeatureJsonFile = browserStructAnnoPath;
			ret = requiredGeneData;
		} else {
			ret = new RequiredGeneData();
			// requiredGeneData赋值
			ret.setGeneStructInfoPath(browserStructAnnoPath);
			ret.setAlignmentFilePath(alignmentFilePath);

			string = keyValueStringMap.getSimplifiedStringWithDefault("$keyFeature.file.path");
			if (!Strings.isNullOrEmpty(string)) {
				ret.setKeyDomainsFilePath(string);
			}
			string = keyValueStringMap.getSimplifiedStringWithDefault("$primers.file.path");
			if (!Strings.isNullOrEmpty(string)) {
				ret.setPrimersFilePath(string);
			}

		}

		return ret;
	}

//	public ImportInfoBean4gBrowser buildFromListOfString(List<String> strs) {
//		ImportInfoBean4gBrowser bean = new ImportInfoBean4gBrowser();
//
//		LinkedList<String> filtered = Lists.newLinkedList();
//		strs.forEach(str -> {
//			if (!str.startsWith("#")) {
//				filtered.add(str);
//			}
//		});
//
//		EgpsParameterParser parser = new EgpsParameterParser();
//		Map<String, LinkedList<String>> map = parser.parseInputString4orignization(filtered);
//
//		{
//			LinkedList<String> linkedList = map.get("$target.type");
//			if (linkedList != null) {
//				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
//				bean.setTargetType(stringAfterEqualStr);
//			}
//		}
//
//		{
//			LinkedList<String> linkedList = map.get("$browser.struct.annotation.path");
//			if (linkedList != null) {
//				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
//				bean.setProteinStructAnnotationPath(stringAfterEqualStr);
//			}
//		}
//
//		{
//			LinkedList<String> linkedList = map.get("$alignment.file.path");
//			if (linkedList != null) {
//				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
//				bean.setProteinSequenceFastaPath(stringAfterEqualStr);
//			}
//		}
//		{
//			LinkedList<String> linkedList = map.get("$sequence.name.translation.path");
//			if (linkedList != null) {
//				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
//				if (stringAfterEqualStr.length() > 0) {
//					bean.setSequenceNameTranlationPath(stringAfterEqualStr);
//				}
//			}
//		}
//		{
//			LinkedList<String> linkedList = map.get("$needTo.translate.columnIndex");
//			if (linkedList == null) {
//				throw new IllegalArgumentException("Please input the parameters of: $needTo.translate.columnIndex");
//			} else {
//				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
//				bean.setNeedToTransferIDIndex(Integer.parseInt(stringAfterEqualStr));
//			}
//		}
//		{
//			LinkedList<String> linkedList = map.get("$wanted.name.columnIndex");
//			if (linkedList == null) {
//				throw new IllegalArgumentException("Please input the parameters of: $wanted.name.columnIndex");
//			} else {
//				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
//				bean.setWantedNameIndex(Integer.parseInt(stringAfterEqualStr));
//			}
//		}
//
//		return bean;
//	}
//
//	public Data4SequenceStructureView prepareData(ImportInfoBean4gBrowser buildFromListOfString) throws IOException {
//		Color[] predinedCellChatColors = EGPSColors.getPredinedCellChatColors();
//
//		/**
//		 * 这里的读取逻辑是：先读取fasta文件， 然后从fasta文件中寻找序列名字， 根据名字拼接从一个json文件的文件名。 所以这要求json文件的格式是
//		 * name.json
//		 */
//		String targetType2 = buildFromListOfString.getTargetType();
//		LinkedHashMap<String, String> fastaFile = EGPSFastaReader
//				.readFastaDNASequence(new File(buildFromListOfString.getProteinSequenceFastaPath()));
//		File dir = new File(buildFromListOfString.getProteinStructAnnotationPath());
//
//		Optional<String> transverPath = buildFromListOfString.getSequenceNameTranlationPath();
//		HashMap<String, String> nameTransferMap = Maps.newLinkedHashMap();
//		if (transverPath.isPresent()) {
//
//			// 需要将1-base转成 0-based
//			int needToTransferIDIndex2 = buildFromListOfString.getNeedToTransferIDIndex() - 1;
//			int wantedNameIndex2 = buildFromListOfString.getWantedNameIndex() - 1;
//
//			List<String> readAllLines = Files.readAllLines(Paths.get(transverPath.get()));
//			Iterator<String> iterator = readAllLines.iterator();
//			iterator.next();
//
//			while (iterator.hasNext()) {
//				String next = iterator.next();
//				String[] split = EGPSStringUtil.split(next, '\t');
//				String key = split[needToTransferIDIndex2];
//				String value = split[wantedNameIndex2];
//				nameTransferMap.put(key, value);
//			}
//		} else {
//			throw new InputMismatchException("Please input the file in the $sequenceNameTranlationPath.");
//		}
//
//		OverlapTransBeanParser overlapTransBeanParser = new OverlapTransBeanParser();
//
//		Data4SequenceStructureView ret = new Data4SequenceStructureView();
//		List<Sequence4import> sequences = new ArrayList<Sequence4import>();
//
//		for (Entry<String, String> entry : nameTransferMap.entrySet()) {
//			String name = entry.getKey();
//			String value = entry.getValue();
//
//			String sequenceContent = fastaFile.get(name);
//			Objects.requireNonNull(sequenceContent,
//					"Found name in your input translation file not have sequence: ".concat(name));
//
//			Sequence4import sequence = new Sequence4import();
//
//			sequence.setName(value);
//			sequence.setLength(sequenceContent.length());
//
//			File jsonFile = new File(dir, name.concat(".json"));
//			Map<String, List<OverlapTransElementBean>> type2listBeanMap = overlapTransBeanParser.parse(jsonFile);
//			List<OverlapTransElementBean> list = type2listBeanMap.get(targetType2);
//			if (list == null) {
//				throw new InputMismatchException("The type name is not exists ".concat(targetType2));
//			}
//
//			List<StructureElement> structureElements = new ArrayList<>();
//
//			for (OverlapTransElementBean sequence2 : list) {
//				StructureElement se1 = new StructureElement();
//				se1.setName(sequence2.getDescription());
//				se1.setStart(Integer.parseInt(sequence2.getStart()));
//				se1.setEnd(Integer.parseInt(sequence2.getEnd()));
//				structureElements.add(se1);
//			}
//
//			Collections.sort(structureElements);
//			int index = 0;
//			for (StructureElement entry1 : structureElements) {
//				Color color = predinedCellChatColors[index];
//				entry1.setColorString(EGPSColors.toHexFormColor(color));
//				index++;
//			}
//
//			sequence.setStructureElementLists(structureElements);
//			sequences.add(sequence);
//		}
//
//		ret.setSequences(sequences);
//		return ret;
//
//	}

}
