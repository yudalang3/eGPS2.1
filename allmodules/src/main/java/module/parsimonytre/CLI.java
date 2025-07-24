package module.parsimonytre;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;

import utils.string.EGPSStringUtil;
import utils.EGPSUtil;
import tsv.io.TSVReader;
import evoltree.struct.util.EvolNodeUtil;
import module.evolview.phylotree.visualization.primary.swing.FastSingleTreeVisualizer;
import evoltree.swingvis.OneNodeDrawer;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;
import module.parsimonytre.algo.Node4SankoffAlgo;
import module.parsimonytre.algo.SankoffAlgoRunner;
import module.parsimonytre.algo.StateAfterMutation;

/**
 * <pre>
 * 请你帮我用JAVA的库 commons-cli，编写一个命令行的启动类。
 * 我有三个参数，第一个是 -t    --tree 意义是 必需参数，内容为进化树的路径；
 * 第二个是 -ls  --leafStates 意义是 必需参数，叶子节点的状态，输入格式为一个tsv文件，详见<a href="https://www.yuque.com/u21499046/egpsdoc/pr5qx32gnxa53p36">...</a>；
 * 第三个是 -p --preferStateOfRoot 意义是 可选参数，当出现多个相同score的简约分数时优先选取的状态
 * 第四个是 -o --output 意义是 必需参数，输出进化树文件的路径
 * 帮我写一个这样的命令行参数解析类。
 *
 * </pre>
 */
public class CLI {

	public static void main(String[] args) {
		// 创建 Options 对象
		Options options = new Options();

		// 添加 tree 参数
		Option tree = new Option("t", "tree", true, "path to the phylogenetic tree file.");
		tree.setRequired(true);
		options.addOption(tree);

		// 添加 leafStates 参数
		Option leafStates = new Option("ls", "leafStates", true, "path to the leaf states TSV file.");
		leafStates.setRequired(true);
		options.addOption(leafStates);

		// 添加 preferStateOfRoot 参数
		Option preferStateOfRoot = new Option("p", "preferStateOfRoot", true,
				"preferred state of root when multiple states have the same score. Enter the leaf name.");
		preferStateOfRoot.setRequired(false);
		options.addOption(preferStateOfRoot);

		// 添加 output 参数
		Option output = new Option("o", "output", true, "path to the output phylogenetic tree file.");
		output.setRequired(true);
		options.addOption(output);

		{
			Option display = new Option("d", "display", false, "Whether display the results in the GUI.");
			display.setRequired(false);
			options.addOption(display);
		}

		{
			Option display = new Option("ln", "leafName", true, "The Column name for the phylogenetic tree leaf name.");
			display.setRequired(true);
			options.addOption(display);
		}

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			// 解析命令行参数
			cmd = parser.parse(options, args);

			// 获取参数值
			String treeFilePath = cmd.getOptionValue("tree");
			String leafStatesFilePath = cmd.getOptionValue("leafStates");
			String preferStateOfRootValue = cmd.getOptionValue("preferStateOfRoot");
			String outputFilePath = cmd.getOptionValue("output");
			String leafName = cmd.getOptionValue("leafName");
			boolean displayOption = cmd.hasOption("display");

			// 打印参数值
			System.out.println("Tree file: " + new File(treeFilePath).getName());
			System.out.println("Leaf states file: " + new File(leafStatesFilePath).getName());
			System.out.println("leafName: " + leafName);
			System.out.println("Preferred state of root: "
					+ (preferStateOfRootValue != null ? preferStateOfRootValue : "not specified"));
			System.out.println("Output file: " + outputFilePath);
			System.out.println("--------------------------------------------------------------------");

			List<String> outputs = run(treeFilePath, leafStatesFilePath, outputFilePath, preferStateOfRootValue, displayOption, leafName, Collections.emptySet());
			for (String s : outputs) {
				System.out.println(s);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 解析失败，打印帮助信息
			formatter.printHelp(EGPSUtil.getCLIUtilityName(CLI.class), options);
		}
	}

	public static List<String> run(String treeFilePath, String leafStatesFilePath, String outputFilePath,
			String preferStateOfRootValue, boolean display, String leafID,Set<String> excludeStatesFileCols) throws Exception {

		List<String> outputs = new ArrayList<>();

		Map<String, StateAfterMutation> preferRootState = new HashMap<>();
		if (preferStateOfRootValue!= null) {
			List<String> lines = FileUtils.readLines(new File(preferStateOfRootValue), StandardCharsets.US_ASCII);
			if (lines.size() != 2) {
				throw new IllegalArgumentException("The input prefer root state should has two line");
			}
			String[] positions = EGPSStringUtil.split(lines.get(0), '\t');
			String[] stateStrs = EGPSStringUtil.split(lines.get(1), '\t');
			int length = positions.length;
			for (int i = 1; i < length; i++) {
				String state = stateStrs[i];
				String posStr = positions[i];
				preferRootState.put(posStr, generateState(state));
			}
		}

		String treeStr = FileUtils.readFileToString(new File(treeFilePath), StandardCharsets.UTF_8);

		TreeDecoder decode = new TreeDecoder();
		EvolNode root = decode.decode(treeStr);

		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
		writer.write("position");
		EvolNodeUtil.iterateByLevelTraverse(root, node -> {
			if (node.getChildCount() > 0) {
				String name = node.getName();
				try {
					writer.write("\t");
					writer.write(name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		writer.write("\t");
		writer.write("rootParsimonyScore");
		writer.write("\t");
		writer.write("correspondingState");
		writer.write("\t");
		writer.write("ifTie");

		writer.write("\n");

		Consumer<Node4SankoffAlgo> writeInternalNodeStatesConsumer = node -> {
			if (node.getChildCount() > 0) {
				StateAfterMutation chosenChar = node.getChosenChar();
				try {
					writer.write("\t");
					writer.write(chosenChar.getStringOfOneStateAfterMutation());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(leafStatesFilePath);
		List<String> leafNameColumn = asKey2ListMap.remove(leafID);
		if (leafNameColumn == null) {
			throw new InputMismatchException(
					"Input leaf states file must contains the column : ".concat(leafID));
		}

		Set<Entry<String, List<String>>> position2statesEntriesSets = asKey2ListMap.entrySet();
		outputs.add("Input number of site is: " + position2statesEntriesSets.size());

		List<EvolNode> leaves = EvolNodeUtil.getLeaves(root);
		// check if the leafNameColumn contains all the leaf names
		for (EvolNode evolNode : leaves) {
			String name = evolNode.getName();
			if (!leafNameColumn.contains(name)) {
				String str = "Please check your input leaf states file, following names not found: \""
						+ name + "\"";
				outputs.add(str);
				throw new InputMismatchException(str);
			}
		}

//		Node4SankoffAlgo rootRet = null;

		for (Entry<String, List<String>> entry : position2statesEntriesSets) {
			String position = entry.getKey();
			if (excludeStatesFileCols.contains(position)){
				continue;
			}

			List<String> valueOfStates = entry.getValue();

			Map<String, StateAfterMutation> name2stateMap = new HashMap<>();


			Iterator<String> iterator = leafNameColumn.iterator();
			for (String string : valueOfStates) {
				String name = iterator.next();
				StateAfterMutation stateAfterMutation = generateState(string);
				if (string.length() > 1) {
					stateAfterMutation.setInsertionState(string.substring(1));
				}
				name2stateMap.put(name, stateAfterMutation);
			}

			HashMap<Integer, StateAfterMutation> leaf2stateMap = new HashMap<>();
			for (EvolNode evolNode : leaves) {
				String name = evolNode.getName();

				StateAfterMutation value = name2stateMap.get(name);
				if (value == null) {
					throw new InputMismatchException(
							"Please check your input leaf name, following names not found: \"" + name + "\"");
				}
				leaf2stateMap.put(evolNode.getID(), value);
			}

			StateAfterMutation preferenceState = preferRootState.get(position);

			outputs.add("------------------------------------------------------");
			String rootStr = preferenceState == null ? "NULL" : preferenceState.getStringOfOneStateAfterMutation();
			outputs.add("Current position is\t" + position + "\t prefer root state is\t"
					+ rootStr);

			HashSet<StateAfterMutation> allLeafStates = new HashSet<>(leaf2stateMap.values());
			if (!allLeafStates.contains(preferenceState)) {
				preferenceState = null;
				outputs.add("--Warning: Please check you prefer state, for it is not exists in the leaf states.");
				System.out.print("--All leaf states are: ");
				for (StateAfterMutation evolNode : allLeafStates) {
					System.out.print(evolNode.getStringOfOneStateAfterMutation());
					System.out.print("\t");
				}
				System.out.println();
			}

			Node4SankoffAlgo rootRet = SankoffAlgoRunner.quickRunAlgo(root, leaf2stateMap, preferenceState);

			writer.write(position);
			EvolNodeUtil.iterateByLevelTraverse(rootRet, writeInternalNodeStatesConsumer);

			if (display) {
				displayTheNode(rootRet);
			}

			writer.write("\t");
			int[] minParsimonyScore = rootRet.getMinParsimonyScore();
			writer.write(Arrays.toString(minParsimonyScore));
			writer.write("\t");

			StateAfterMutation[] index2stateMapRelationship = rootRet.getPrepareFactor()
					.getIndex2stateMapRelationship();
			int length = index2stateMapRelationship.length;
			writer.write(index2stateMapRelationship[0].getStringOfOneStateAfterMutation());
			for (int i = 1; i < length; i++) {
				writer.write(',');
				writer.write(index2stateMapRelationship[i].getStringOfOneStateAfterMutation());
			}

			writer.write("\t");
			writer.write(String.valueOf(isTie(minParsimonyScore)));
			writer.write("\n");
		}

		writer.close();

		return outputs;
	}

	private static boolean isTie(int[] minParsimonyScore) {
		if (minParsimonyScore == null || minParsimonyScore.length == 0) {
			return false; // 空数组或null不构成平局
		}

		// Step 1: 找到最小值
		int minValue = Integer.MAX_VALUE;
		for (int score : minParsimonyScore) {
			if (score < minValue) {
				minValue = score;
			}
		}

		// Step 2: 计算最小值出现的次数
		int count = 0;
		for (int score : minParsimonyScore) {
			if (score == minValue) {
				count++;
			}
		}

		// Step 3: 判断是否有多于一个的最小值
		return count > 1;
	}

	private static StateAfterMutation generateState(String string) {
		return new StateAfterMutation(string.charAt(0), 1);
	}

	private static void displayTheNode(Node4SankoffAlgo root) {
		EvolNodeUtil.recursiveIterateTreeIF(root, node -> {node.setLength(1);});

		OneNodeDrawer<Node4SankoffAlgo> drawer = (g2d, grNode) -> {
			int xSelf = (int) grNode.getXSelf();
			int ySelf = (int) grNode.getYSelf();
			g2d.fillRect(xSelf, ySelf, 2, 2);
			Node4SankoffAlgo reflectNode = grNode.getReflectNode();
			g2d.drawString(reflectNode.getChosenChar().getStringOfOneStateAfterMutation(), xSelf + 5, ySelf);
		};
		new FastSingleTreeVisualizer().plotTree(root, "Title", new Dimension(1200, 600), drawer);
	}
}
