package module.maplot.maplot.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import module.maplot.maplot.CONSTANT;
import module.maplot.maplot.model.OriginalData;
import module.maplot.maplot.model.ValuePoint;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class MAReadFile {

	public static OriginalData importData(List<String> contents) {
		List<String> parameterLines = new ArrayList<>();
		List<String> contentLines = new ArrayList<>();

		for (String str : contents) {

			if (str.startsWith("#")) {
				continue;
			}

			if (str.startsWith("$")) {
				parameterLines.add(str);
				continue;
			}

			contentLines.add(str);
		}

		VoiceParameterParser parser = new VoiceParameterParser();
		Map<String, LinkedList<String>> maps = parser.parseInputString4organization(parameterLines);

		int uidIndex = 0;
		{
			LinkedList<String> linkedList = maps.get(CONSTANT.UID);
			if (linkedList == null) {
				throw new IllegalArgumentException("You most input the ".concat(CONSTANT.UID));
			}
			uidIndex = Integer.parseInt(parser.getStringAfterEqualStr(linkedList.getFirst()));

		}
		int condi1Index = 0;
		{
			LinkedList<String> linkedList = maps.get(CONSTANT.COND1);
			if (linkedList == null) {
				throw new IllegalArgumentException("You most input the ".concat(CONSTANT.COND1));
			}
			condi1Index = Integer.parseInt(parser.getStringAfterEqualStr(linkedList.getFirst()));
		}
		int condi2Index = 0;
		{
			LinkedList<String> linkedList = maps.get(CONSTANT.COND2);
			if (linkedList == null) {
				throw new IllegalArgumentException("You most input the ".concat(CONSTANT.COND2));
			}
			condi2Index = Integer.parseInt(parser.getStringAfterEqualStr(linkedList.getFirst()));
		}
		int sigIndex = -1;
		{
			LinkedList<String> linkedList = maps.get(CONSTANT.SIGNIFICANT);
			if (linkedList != null) {
			sigIndex = Integer.parseInt(parser.getStringAfterEqualStr(linkedList.getFirst()));
			}
		}

		List<ValuePoint> originalPoints = new ArrayList<ValuePoint>();
		Iterator<String> iterator = contentLines.iterator();
		String headerLine = iterator.next();
		while (iterator.hasNext()) {
			String str = iterator.next();
			ValuePoint originalPoint = new ValuePoint();
			String[] split = str.trim().split("\t");

			String id = split[uidIndex - 1];
			Double valueOf = Double.valueOf(split[condi1Index - 1]);
			Double valueOf2 = Double.valueOf(split[condi2Index - 1]);
			originalPoint.setId(id);
			originalPoint.setXValue(valueOf);
			originalPoint.setYValue(valueOf2);

			if (sigIndex > 0) {
				originalPoint.setPValue(Optional.of(Double.valueOf(split[sigIndex - 1])));
			} else {
				originalPoint.setPValue(Optional.empty());
			}

			originalPoint.setAnnotations(Arrays.asList(split));

			originalPoints.add(originalPoint);
		}

		OriginalData originalData = new OriginalData(originalPoints);
		{
			String[] split = headerLine.trim().split("\t");
			originalData.setHeaderFields(Arrays.asList(split));
		}
		return originalData;
	}

	public static OriginalData readFile(File inputFile) throws IOException {
		List<String> readLines = FileUtils.readLines(inputFile, StandardCharsets.UTF_8);
		return importData(readLines);
	}

}
