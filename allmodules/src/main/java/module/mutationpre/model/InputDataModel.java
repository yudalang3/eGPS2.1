package module.mutationpre.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;

import fasta.io.FastaReader;
import org.apache.commons.io.FileUtils;

import phylo.msa.util.EvolutionaryProperties;
import egps2.utils.common.util.EGPSFonts;
import module.evoltre.mutation.IMutation;
import module.evoltre.mutation.IMutation4Rec;
import module.evoltre.mutation.MutationOperator;
import graphic.engine.colors.EGPSColors;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class InputDataModel {

	RegionsInputInfor regionsInputInfor = new RegionsInputInfor();
	VariantsInputInfor variants = new VariantsInputInfor();

	double connectingLineCurvature = 0.6;

	// Axis
	Font titleFont = new JLabel().getFont();
	Color titleColor = Color.black;
	Font labelFont = titleFont;
	float axisLineWidth = 1;
	int axisTickSize = 8;

	int axisToRegionVerticalGap = 50;
	Font regionLabelFontt = titleFont;
	Color regionLabelColor = titleColor;
	int roundRectangularCurvature = 5;
	Color regionFillColor = titleColor;
	boolean hasRegionBorder = false;
	Color regionBorderColor = titleColor;
	float regionBorderLineWidth = (float) 0.5;
	int regionHeight = 50;
	float connectingLineWidth = 1;
	int connectingLineVerticalSpacing = 200;
	Color connectingLineColor = titleColor;

	// connecting line的特色渲染
	List<ConnectingRegionRender> listOfConnectingRegionRenders = new ArrayList<>();
	String title = "";
	boolean regionShowMutationVerticalLine = true;
	Font mutationFont = titleFont;

	VoiceParameterParser parser = new VoiceParameterParser();

	public InputDataModel getExample() {
		InputDataModel ret = new InputDataModel();

		ret.regionsInputInfor.totalLength = 1000;
		List<RegionInfor> listOfRegionInfors = ret.regionsInputInfor.listOfRegionInfors;
		{
			RegionInfor regionInfor = new RegionInfor();
			regionInfor.name = "Orf1ab";
			regionInfor.startPostion = 86;
			regionInfor.endPosition = 451;
			listOfRegionInfors.add(regionInfor);
		}
		{
			RegionInfor regionInfor = new RegionInfor();
			regionInfor.name = "S";
			regionInfor.startPostion = 500;
			regionInfor.endPosition = 706;
			listOfRegionInfors.add(regionInfor);
		}
		{
			RegionInfor regionInfor = new RegionInfor();
			regionInfor.name = "M";
			regionInfor.startPostion = 801;
			regionInfor.endPosition = 998;
			listOfRegionInfors.add(regionInfor);
		}

		List<VariantInfo> variants2 = ret.variants.variants;
		{
			VariantInfo variantInfo = new VariantInfo();
			variantInfo.variantName = "Alpha VOC";
			List<InputMutationInfor> variantsList = variantInfo.variantsList;
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "E484K";
				inputMutationInfor.position = 484;
				variantsList.add(inputMutationInfor);
			}
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "N501Y";
				inputMutationInfor.position = 501;
				variantsList.add(inputMutationInfor);
			}
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "D614G";
				inputMutationInfor.position = 614;
				variantsList.add(inputMutationInfor);
			}
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "P681H";
				inputMutationInfor.position = 681;
				variantsList.add(inputMutationInfor);
			}

			Collection<Integer> posCollection = new ArrayList<>();
			variantsList.forEach(m -> {
				posCollection.add(m.position);
			});
			ret.variants.paintedPositions = posCollection;

			variants2.add(variantInfo);
		}
		{
			VariantInfo variantInfo = new VariantInfo();
			variantInfo.variantName = "Delta VOC";
			List<InputMutationInfor> variantsList = variantInfo.variantsList;
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "E484K";
				inputMutationInfor.position = 484;
				variantsList.add(inputMutationInfor);
			}
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "N501Y";
				inputMutationInfor.position = 501;
				variantsList.add(inputMutationInfor);
			}
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "D614G";
				inputMutationInfor.position = 614;
				variantsList.add(inputMutationInfor);
			}
			{
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = "P681H";
				inputMutationInfor.position = 681;
				variantsList.add(inputMutationInfor);
			}

			variants2.add(variantInfo);
		}

		return ret;
	}

	public InputDataModel configInputDataFromStringArray(String[] inputStrings) throws IOException {
		List<String> ret = new ArrayList<>(inputStrings.length);
		for (String string : inputStrings) {
			if (string.startsWith("#")) {
				continue;
			}

			if (string.isEmpty()) {
				continue;
			}

			ret.add(string);
		}

		Map<String, LinkedList<String>> parseInputString4orignization = parser.parseInputString4organization(ret);

		return parseInputString4orignization(parseInputString4orignization);
	}

	public InputDataModel parseInputString4orignization(Map<String, LinkedList<String>> map) throws IOException {
		InputDataModel ret = new InputDataModel();

		LinkedList<String> linkedList = map.get("$totalLength");

		ret.regionsInputInfor.totalLength = Integer.parseInt(getStringAfterEqualStr(linkedList.getFirst()));
		
		linkedList = map.get("$startPosition");
		if (linkedList != null) {
			ret.regionsInputInfor.startPosition = Integer.parseInt(getStringAfterEqualStr(linkedList.getFirst()));
		}

		List<RegionInfor> listOfRegionInfors = ret.regionsInputInfor.listOfRegionInfors;

		linkedList = map.get("$regionInfo");
		Iterator<String> iterator = linkedList.iterator();
		iterator.next();
		while (iterator.hasNext()) {
			String ele = iterator.next();
			String[] split = ele.split("\t");
			RegionInfor regionInfor = new RegionInfor();
			regionInfor.name = split[0];
			regionInfor.startPostion = Integer.parseInt(split[1]);
			regionInfor.endPosition = Integer.parseInt(split[2]);
			regionInfor.fillColor = EGPSColors.parseColor(split[3]);

			if (split.length == 5) {
				regionInfor.downMovedDistance = Integer.parseInt(split[4]);
			}
			listOfRegionInfors.add(regionInfor);
		}

		List<VariantInfo> variants2 = ret.variants.variants;

		linkedList = map.get("$mutation:inputType");
		int mutationInputType = 0;
		if (linkedList != null) {
			String mutationInputTypeString = parser.getStringAfterEqualStr(linkedList.getFirst());
			mutationInputType = Integer.parseInt(mutationInputTypeString);
		}
		ret.variants.mutationInputType = mutationInputType;

		// input for variant information
		linkedList = map.get("$mutationInfo");
		iterator = linkedList.iterator();
		iterator.next();

		if (mutationInputType == 0) {
			final String variantHeaderString = "variant";

			LinkedList<String> variantLinkedList = new LinkedList<>();
			while (iterator.hasNext()) {
				String next = iterator.next();
				int indexOf = next.indexOf(variantHeaderString);
				if (indexOf < 0) {
					variantLinkedList.add(next);
				} else {
					if (variantLinkedList.isEmpty()) {
						variantLinkedList.add(next);
					} else {
						appendingOneVariantInfo(variantLinkedList, variants2);
						variantLinkedList.add(next);
					}
				}
			}
			appendingOneVariantInfo(variantLinkedList, variants2);
		} else if (mutationInputType == 1) {
			final String variantHeaderString = "alignmnetFile";
			File inputFile = null;
			while (iterator.hasNext()) {
				String next = iterator.next();
				if (next.startsWith(variantHeaderString)) {
					String stringAfterEqualStr = parser.getStringAfterEqualStr(next);
					inputFile = new File(stringAfterEqualStr);
					break;
				}
			}

			LinkedHashMap<String, String> readFastaDNASequence = FastaReader.readFastaDNASequence(inputFile);
			assembleFastaFile2variants(readFastaDNASequence, variants2);
		} else {
			// type is 2
			final String variantHeaderString = "mutationsFile";
			File inputFile = null;
			while (iterator.hasNext()) {
				String next = iterator.next();
				if (next.startsWith(variantHeaderString)) {
					String stringAfterEqualStr = parser.getStringAfterEqualStr(next);
					inputFile = new File(stringAfterEqualStr);
					break;
				}
			}

			List<String> readLines = FileUtils.readLines(inputFile, StandardCharsets.UTF_8);
			assembleMutationsFile2variants(readLines, variants2);
		}

		HashSet<Integer> posCollection = new HashSet<>();
		HashSet<Integer> intersections = new HashSet<>();
		for (VariantInfo infor : variants2) {
			List<InputMutationInfor> variantsList = infor.variantsList;

			List<Integer> tempSet = new ArrayList<>(variantsList.size());
			for (InputMutationInfor mut : variantsList) {
				tempSet.add(mut.position);
			}

			posCollection.addAll(tempSet);
			if (intersections.isEmpty()) {
				intersections.addAll(tempSet);
			} else {
				intersections.retainAll(tempSet);
			}
		}

		List<Integer> posIntegers = new ArrayList<>(posCollection);
		Collections.sort(posIntegers);

		ret.variants.paintedPositions = posIntegers;

		linkedList = map.get("$mutation:showConsensusAnnotation");
		boolean showConsensusAnnotation = false;
		if (linkedList != null) {
			String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.getFirst());
			showConsensusAnnotation = Boolean.parseBoolean(stringAfterEqualStr);
		}

		if (showConsensusAnnotation) {
			if (variants2.size() == 1) {
				intersections.clear();
			}
		} else {
			intersections.clear();
		}

		ret.variants.intesectionPositionSet = intersections;

		// 下面针对 $mutation:showPosition 参数增加variant数量
		linkedList = map.get("$mutation:showPositionCoordinate");
		if (linkedList != null) {
			String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.getFirst());
			boolean showPosition = Boolean.parseBoolean(stringAfterEqualStr);
			if (showPosition) {
				appendingOneVariantInfo4position(posIntegers, variants2);
			}

			ret.variants.showPositionCoordinate = showPosition;
		}
		// rotation degree
		linkedList = map.get("$mutation:rotationDegree");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			int parseInt = Integer.parseInt(degreString);
			ret.variants.rotationDegree = -parseInt;
		}
		linkedList = map.get("$mutation:position:rotationDegree");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			int parseInt = Integer.parseInt(degreString);
			ret.variants.positionCoordinateRotationDegree = -parseInt;
		}
		// mutation:font
		linkedList = map.get("$mutation:font");
		if (linkedList != null) {

			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Font parseFont = EGPSFonts.parseFont(degreString);
			ret.mutationFont = parseFont;
		}

		// Axis parameters
		linkedList = map.get("$axis:title");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.title = degreString;
		}
		linkedList = map.get("$axis:titleFont");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Font parseFont = EGPSFonts.parseFont(degreString);
			ret.titleFont = parseFont;
		}
		linkedList = map.get("$axis:titleColor");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Color parseFont = EGPSColors.parseColor(degreString);
			ret.titleColor = parseFont;
		}
		// $axis:labelFont
		linkedList = map.get("$axis:labelFont");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Font parseFont = EGPSFonts.parseFont(degreString);
			ret.labelFont = parseFont;
		}
		linkedList = map.get("$axis:axisLineWidth");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.axisLineWidth = Float.parseFloat(degreString);
		}
		linkedList = map.get("$axis:tickSize");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.axisTickSize = Integer.parseInt(degreString);
		}

		/**
		 * Region parameters
		 * 
		 */
		linkedList = map.get("$region:axisToRegionVerticalGap");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			// int axisToRegionVerticalGap = 50;
			ret.axisToRegionVerticalGap = Integer.parseInt(degreString);
		}
		linkedList = map.get("$region:regionLabelFont");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Font parseFont = EGPSFonts.parseFont(degreString);
			ret.regionLabelFontt = parseFont;
		}
		linkedList = map.get("$region:showMutationVerticalLine");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.regionShowMutationVerticalLine = Boolean.parseBoolean(degreString);
		}
		linkedList = map.get("$region:regionLabelColor");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Color parseFont = EGPSColors.parseColor(degreString);
			ret.regionLabelColor = parseFont;
		}
		linkedList = map.get("$region:roundRectangularCurvature");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.roundRectangularCurvature = Integer.parseInt(degreString);
		}
		linkedList = map.get("$region:regionFillColor");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Color parseFont = EGPSColors.parseColor(degreString);
			ret.regionFillColor = parseFont;
		}
		linkedList = map.get("$region:hasRegionBorder");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.hasRegionBorder = Boolean.parseBoolean(degreString);
		}
		linkedList = map.get("$region:regionBorderColor");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Color parseFont = EGPSColors.parseColor(degreString);
			ret.regionBorderColor = parseFont;
		}
		linkedList = map.get("$region:regionBorderLineWidth");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.regionBorderLineWidth = Float.parseFloat(degreString);
		}
		linkedList = map.get("$region:height");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.regionHeight = Integer.parseInt(degreString);
		}

		/**
		 * Connecting line parameters
		 */
		linkedList = map.get("$connectingLine:connectingLineWidth");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.connectingLineWidth = Float.parseFloat(degreString);
		}

		// $connectingLine:curvature
		linkedList = map.get("$connectingLine:curvature");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.connectingLineCurvature = Double.parseDouble(degreString);
		}
		linkedList = map.get("$connectingLine:verticalSpacing");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			ret.connectingLineVerticalSpacing = Integer.parseInt(degreString);
		}
		linkedList = map.get("$connectingLine:connectingLineColor");
		if (linkedList != null) {
			String degreString = parser.getStringAfterEqualStr(linkedList.getFirst());
			Color parseFont = EGPSColors.parseColor(degreString);
			ret.connectingLineColor = parseFont;
		}

		/**
		 * $connectingLine:special 就是这个线条的地方要特色渲染
		 */
		linkedList = map.get("$connectingLine:special");
		if (linkedList != null) {
			Iterator<String> iterator2 = linkedList.iterator();
			iterator2.next();
			List<ConnectingRegionRender> listOfConnectingRegionRenders = ret.listOfConnectingRegionRenders;
			while (iterator2.hasNext()) {
				String next = iterator2.next();
				String[] split = next.split("\t");
				ConnectingRegionRender render = new ConnectingRegionRender();
				render.startPos = Integer.parseInt(split[0]);
				render.endPos = Integer.parseInt(split[1]);
				render.basicStroke = new BasicStroke(Float.parseFloat(split[2]));
				render.color = EGPSColors.parseColor(split[3]);
				listOfConnectingRegionRenders.add(render);
			}
		}

		return ret;
	}

	private void assembleMutationsFile2variants(List<String> readLines, List<VariantInfo> variants2) {

		final int startSymbol = ':';
		final String splitChar = ",";
		int index = 1;
		for (String mutString : readLines) {
			if (mutString.charAt(0) == '#') {
				continue;
			}
			String replaceAll = mutString.replaceAll(" ", "");

			int indexOf = replaceAll.indexOf(startSymbol);
			String variantName = null, mutationString = null;
			if (indexOf == -1) {
				variantName = "variant-".concat(String.valueOf(index));
				mutationString = replaceAll;
			} else {
				variantName = replaceAll.substring(0, indexOf);
				mutationString = replaceAll.substring(indexOf + 1);
			}

			index++;

			String[] splits = mutationString.split(splitChar);

			List<IMutation> mutations = new ArrayList<>();
			for (String mut : splits) {
				IMutation4Rec parseMutation = MutationOperator.parseMutation(mut, true);
				mutations.add(parseMutation);
			}

			Collections.sort(mutations);

			ListIterator<IMutation> listIterator = mutations.listIterator();

			int prevPosi = 0;
			VariantInfo variantInfo = new VariantInfo();
			{
				IMutation firstMutation = listIterator.next();
				prevPosi = firstMutation.getPosition();

				// 第一个variant信息
				variantInfo.variantName = variantName;
				{
					InputMutationInfor mutationInputInfor = new InputMutationInfor();
					mutationInputInfor.annotationString = firstMutation.getFullInformation();
					mutationInputInfor.color = Color.black;
					mutationInputInfor.position = firstMutation.getPosition();
					variantInfo.variantsList.add(mutationInputInfor);
				}
			}

			while (listIterator.hasNext()) {
				IMutation currMutation = listIterator.next();

				if (currMutation.getPosition() == prevPosi) {
					InputMutationInfor prevInputInfor = variantInfo.variantsList
							.get(variantInfo.variantsList.size() - 1);
					prevInputInfor.annotationString = prevInputInfor.annotationString
							.concat("," + currMutation.getFullInformation());
				} else {
					InputMutationInfor mutationInputInfor = new InputMutationInfor();
					mutationInputInfor.annotationString = currMutation.getFullInformation();
					mutationInputInfor.color = Color.black;
					mutationInputInfor.position = currMutation.getPosition();
					variantInfo.variantsList.add(mutationInputInfor);
				}

				prevPosi = currMutation.getPosition();
			}

			variants2.add(variantInfo);
		}

	}

	private void assembleFastaFile2variants(LinkedHashMap<String, String> readFastaDNASequence,
			List<VariantInfo> variants2) {
		variants2.clear();

		HashSet<Integer> ploymorphicSites = new HashSet<>();
		String alignedRef = null;
		String originalRefString = null;
		String refGenomeName = null;

		boolean isFirstEntery = true;
		for (Entry<String, String> entry : readFastaDNASequence.entrySet()) {
			if (isFirstEntery) {
				alignedRef = entry.getValue();
				originalRefString = alignedRef.replace("-", "");
				refGenomeName = entry.getKey();
				isFirstEntery = false;
				continue;
			}

			List<IMutation4Rec> pairwiseSeqMutationsGui = MutationOperator.getPairwiseSeqMutationsGui(originalRefString,
					alignedRef, entry.getValue());

			VariantInfo variantInfo = new VariantInfo();
			variantInfo.variantName = entry.getKey();
			List<InputMutationInfor> variantsList = variantInfo.variantsList;

			for (IMutation4Rec mut : pairwiseSeqMutationsGui) {
				int position = mut.getPosition();

				if (mut.getDerivedState().length() == 1) {
					if (mut.getDerivedState().charAt(0) == EvolutionaryProperties.GAP_CHAR) {
						// Deletion
						String ancestralState = mut.getAncestralState();
						int length = ancestralState.length();
						for (int i = 0; i < length; i++) {
							InputMutationInfor inputMutationInfor = new InputMutationInfor();
							inputMutationInfor.annotationString = "-";
							int currPosi = position + i;
							inputMutationInfor.position = currPosi;
							inputMutationInfor.color = Color.black;
							variantsList.add(inputMutationInfor);
							ploymorphicSites.add(currPosi);
						}
					} else {
						// snp
						String annotationString = mut.getDerivedState();
						InputMutationInfor inputMutationInfor = new InputMutationInfor();
						inputMutationInfor.annotationString = annotationString;
						inputMutationInfor.position = position;
						inputMutationInfor.color = Color.black;
						variantsList.add(inputMutationInfor);
						ploymorphicSites.add(position);
					}
				} else {
					// insertion
					String annotationString = mut.getDerivedState();
					InputMutationInfor inputMutationInfor = new InputMutationInfor();
					inputMutationInfor.annotationString = annotationString;
					inputMutationInfor.position = position;
					inputMutationInfor.color = Color.black;
					variantsList.add(inputMutationInfor);
					ploymorphicSites.add(position);
				}

			}

			variants2.add(variantInfo);
		}
		{
			ArrayList<Integer> arrayList = new ArrayList<>(ploymorphicSites);
			Collections.sort(arrayList);
			VariantInfo variantInfo = new VariantInfo();
			variantInfo.variantName = refGenomeName;
			List<InputMutationInfor> variantsList = variantInfo.variantsList;
			for (Integer integer : arrayList) {
				char charAt = originalRefString.charAt(integer - 1);
				InputMutationInfor inputMutationInfor = new InputMutationInfor();
				inputMutationInfor.annotationString = String.valueOf(charAt);
				inputMutationInfor.position = integer;
				inputMutationInfor.color = Color.black;
				variantsList.add(inputMutationInfor);
			}
			variants2.add(0, variantInfo);
		}
	}

	private void appendingOneVariantInfo(LinkedList<String> variantLinkedList, List<VariantInfo> variants2) {

		Iterator<String> iterator = variantLinkedList.iterator();
		VariantInfo variantInfo = new VariantInfo();
		variantInfo.variantName = getStringAfterEqualStr(iterator.next());
		List<InputMutationInfor> variantsList = variantInfo.variantsList;

		while (iterator.hasNext()) {
			String next = iterator.next();
			String[] split = next.split("\t");
			InputMutationInfor inputMutationInfor = new InputMutationInfor();
			inputMutationInfor.annotationString = split[2];
			inputMutationInfor.color = EGPSColors.parseColor(split[1]);
			inputMutationInfor.position = Integer.parseInt(split[0]);
			variantsList.add(inputMutationInfor);
		}

		Collections.sort(variantsList);
		variants2.add(variantInfo);
		variantLinkedList.clear();
	}

	private void appendingOneVariantInfo4position(Collection<Integer> paintedPositions, List<VariantInfo> variants2) {

		Iterator<Integer> iterator = paintedPositions.iterator();
		VariantInfo variantInfo = new VariantInfo();
		variantInfo.variantName = "Coordinate";
		List<InputMutationInfor> variantsList = variantInfo.variantsList;

		while (iterator.hasNext()) {
			Integer next = iterator.next();
			InputMutationInfor inputMutationInfor = new InputMutationInfor();
			inputMutationInfor.annotationString = next.toString();
			inputMutationInfor.color = Color.black;
			inputMutationInfor.position = next;
			variantsList.add(inputMutationInfor);
		}

		Collections.sort(variantsList);
		// 注意，插入到第一个位置
		variants2.add(0, variantInfo);
	}

	private String getStringAfterEqualStr(String first) {
		return parser.getStringAfterEqualStr(first);
	}

}
