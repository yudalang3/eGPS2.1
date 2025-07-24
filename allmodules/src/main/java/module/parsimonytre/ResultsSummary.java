package module.parsimonytre;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;

public class ResultsSummary extends WatchNodeStatesWithChange {

	protected String outputSummaryByPosition;
	protected String outputSummaryByNode;

	public ResultsSummary() {
		indexOfPositionOneBased = 0;
		interactiveWatch = false;

	}

	public void setOutputSummaryPaths(String outputSummaryByNode, String outputSummaryByPosition) {
		this.outputSummaryByPosition = outputSummaryByPosition;
		this.outputSummaryByNode = outputSummaryByNode;
	}

	@Override
	protected void summarizeAll(List<Entry<String, Map<String, String>>> list, EvolNode root) {

		List<String> outputSummarys = new ArrayList<>();
		Map<String, StringBuilder> nodeName2stringBuilder = new HashMap<>();
		EvolNodeUtil.recursiveIterateTreeIF(root, currNode -> {
			String name = currNode.getName();
			nodeName2stringBuilder.put(name, new StringBuilder());
		});

		//outputSummarys.add("NodeName\tcounts\tevents");
		//注意：这里没有Header line
		for (Entry<String, Map<String, String>> entry : list) {
			String key = entry.getKey();

			StringJoiner stringJoiner = new StringJoiner("\t");
			stringJoiner.add(key);

			EvolNodeUtil.recursiveIterateTreeIF(root, currNode -> {
				Map<String, String> name2statesMap = entry.getValue();
				String name = currNode.getName();
				String currentState = name2statesMap.get(name);

				EvolNode parent = currNode.getParent();
				if (parent == null) {
					if (!Objects.equals(currentState, "0")) {
						String stateTrans = " 0 --> " + currentState;
						String temp = name + stateTrans;
						stringJoiner.add(temp);

						nodeName2stringBuilder.get(name).append("$ ").append(key.concat(stateTrans));
					}
				} else {
					String parState = name2statesMap.get(parent.getName());
					if (!Objects.equals(currentState, parState)) {
						String stateTrans = " " + parState + " --> " + currentState;
						String temp = name + stateTrans;
						stringJoiner.add(temp);
						nodeName2stringBuilder.get(name).append("$ ").append(key.concat(stateTrans));
					}
				}
			});

			outputSummarys.add(stringJoiner.toString());
		}

		try {
			FileUtils.writeLines(new File(outputSummaryByPosition), outputSummarys);
		} catch (IOException e) {
			e.printStackTrace();
		}

		outputSummarys.clear();
		outputSummarys.add("NodeName\tcounts\tevents");
		EvolNodeUtil.recursiveIterateTreeIF(root, currNode -> {
			String name = currNode.getName();
			StringBuilder sb = nodeName2stringBuilder.get(name);
			String string = sb.toString();
			int countMatches = StringUtils.countMatches(string, '$');
			outputSummarys.add(name + "\t" + countMatches + "\t" + string);
		});

		try {
			FileUtils.writeLines(new File(outputSummaryByNode), outputSummarys);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
