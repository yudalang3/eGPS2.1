package module.parsimonytre;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D.Double;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFrame;

import com.google.common.collect.Sets;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import utils.EGPSGuiUtil;
import utils.string.EGPSStringUtil;
import tsv.io.KitTable;
import tsv.io.TSVReader;
import module.analysehomogene.gui.CommonProcedureUtil;
import evoltree.struct.util.EvolNodeUtil;
import module.evolview.phylotree.visualization.primary.swing.FastSingleTreeVisualizer;
import evoltree.swingvis.OneNodeDrawer;
import module.evolview.phylotree.visualization.primary.swing.SingleTreePaintingPanel;
import evoltree.phylogeny.DefaultPhyNode;
import evoltree.struct.EvolNode;
import module.evolview.phylotree.visualization.graphics.struct.CollapseProperty;
import graphic.engine.EGPSDrawUtil;

public class WatchNodeStatesWithChange {

	private String pathOfLeafSates = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\bench_wntComp_geneFamily\\2.selection.pattern\\9model\\leafStates_blast_speciesRets.txt";
	private String pathOfInferredSates = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\bench_wntComp_geneFamily\\2.selection.pattern\\9model\\strict_blast_result_inferStates.nwk";
	private String pathOfPhylogeneticTree = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\DataBase\\9Model_species_Curated_data\\20240201_modelSpeciesStatis\\9_model_species_evolution_named.nwk.txt";

	private String outputTheSummaryEvoutionEvents = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\bench_wntComp_geneFamily\\3.the_exists_ofWntComps\\leafStates_blast_speciesRets_inferedEvolution_events.tsv";

	/**
	 * 0 for all;
	 */
	protected int indexOfPositionOneBased = 0;

	protected boolean interactiveWatch = false;
	private String leafIDColumnName = "leafName";
	private HashSet<String> excludeStatesColumns;
	private Optional<String> outputOneFigurePathOption;
	private String ancestralStatesPath;

    public WatchNodeStatesWithChange() {
        outputOneFigurePathOption = Optional.empty();
    }

    public void setInteractiveWatch(boolean interactiveWatch) {
		this.interactiveWatch = interactiveWatch;
	}

	public void setPathOfLeafSates(String pathOfLeafSates) {
		this.pathOfLeafSates = pathOfLeafSates;
	}

	public void setPathOfInferredSates(String pathOfInferredSates) {
		this.pathOfInferredSates = pathOfInferredSates;
	}

	public void setPathOfPhylogeneticTree(String pathOfPhylogeneticTree) {
		this.pathOfPhylogeneticTree = pathOfPhylogeneticTree;
	}

	public void setOutputTheSummaryEvoutionEvents(String outputTheSummaryEvoutionEvents) {
		this.outputTheSummaryEvoutionEvents = outputTheSummaryEvoutionEvents;
	}

	public void setIndexOfPositionOneBased(int indexOfPositionOneBased) {
		this.indexOfPositionOneBased = indexOfPositionOneBased;
	}

	public void run() throws Exception {

		EvolNode root = CommonProcedureUtil.getRootNode(pathOfPhylogeneticTree);

		Map<String, Map<String, String>> position2_name2stateMap_map = new LinkedHashMap<>();

		{
//			leafName	ZNRF3	AXIN1	NDP	IFT140	DKK1	CREBBP	LRP6	TLE1	BTRC	KREMEN1	TRABD2A	SFRP1	CER1	LGR5	NKD1	LEF1	PORCN	GSK3B	NOTUM	TBP	DVL2	WNT3A	WIF1	TNKS	IPO11	RAPGEF5	RNF43	BCL9	HSPG2	ROR1	SMARCA4	SOST	FZD4	PPP2CA	CSNK1A1	CTNNB1	APC	WLS	RSPO1	CDC73	VANGL1
//			Homo sapiens	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1
//			Pongo abelii	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1

			Map<String, List<String>> positionAsKey = TSVReader.readAsKey2ListMap(pathOfLeafSates);

			for (String excludeStatesColumn : excludeStatesColumns) {
				positionAsKey.remove(excludeStatesColumn);
			}
			List<String> leafName = positionAsKey.remove(leafIDColumnName);

			for (Entry<String, List<String>> iterable_element : positionAsKey.entrySet()) {
				String key = iterable_element.getKey();
				List<String> values = iterable_element.getValue();
				Map<String, String> hash = new HashMap<>();
				int index = 0;

				for (String string : values) {
					String name = leafName.get(index);
					hash.put(name, string);
					index++;
				}
				position2_name2stateMap_map.put(key, hash);
			}
		}

		{

//			position	Root	Embryophyta	Opisthokonta	Bilateria	Protostomia|Ecdysozoa	Vertebrata|Gnathostomata	Tetrapoda	Primates	rootParsimonyScore	correspondingState
//			ZNRF3	0	0	0	0	0	1	1	1	[1, 3]	0,1
//			AXIN1	0	0	0	0	0	1	1	1	[1, 3]	0,1
//			NDP	0	0	0	0	0	1	1	1	[1, 3]	0,1
			// 第一列是 postion的名称，最后两列是 特定的两列，总结的时候不需要
			KitTable tsvTextFile = TSVReader.readTsvTextFile(pathOfInferredSates);
			// 这一步应该有一个 Check 步骤，不然结果出问题都不知道

			List<String> headerNames = tsvTextFile.getHeaderNames();
			// check the
			if (!Objects.equals("position", headerNames.getFirst())) {
				throw new InputMismatchException(
						"It seems the input $inferred.node.states.path file is not the desired file.\nPlease input the correct file.");
			}
			int numOfCols = headerNames.size();
			int numOfPositions = numOfCols - 2;
			List<List<String>> contents = tsvTextFile.getContents();
			for (List<String> rowList : contents) {
				String positionStr = rowList.getFirst();
				HashMap<String, String> hashMap = new HashMap<>();
				// 注意这里要 -2
				for (int i = 1; i < numOfPositions; i++) {
					String phyloNodeName = headerNames.get(i);
					String value = rowList.get(i);
					hashMap.put(phyloNodeName, value);
				}
				Map<String, String> map = position2_name2stateMap_map.get(positionStr);
				if (map == null) {
					position2_name2stateMap_map.put(positionStr, hashMap);
				} else {
					map.putAll(hashMap);
				}

			}
		}

//		for (Entry<String, Map<String, String>> entry : position2_name2stateMap_map.entrySet()) {
//			System.out.println(entry);
//		}

		ArrayList<Entry<String, Map<String, String>>> arrayList = new ArrayList<>(
				position2_name2stateMap_map.entrySet());

		if (indexOfPositionOneBased > 0) {

			Entry<String, Map<String, String>> entry = arrayList.get(indexOfPositionOneBased - 1);
			String key = entry.getKey();
			String title = "Position " + key + " node status.";
			Map<String, String> name2statesMap = entry.getValue();

			EvolNodeUtil.iterateByLevelTraverse(root, node -> node.setLength(1));
			OneNodeDrawer<EvolNode> drawer = (g2d, grNode) -> {
				EvolNode reflectNode = grNode.getReflectNode();

				int xSelf = (int) grNode.getXSelf();
				int ySelf = (int) grNode.getYSelf();
//				g2d.fillRect(xSelf - 4, ySelf - 4, 8, 8);

				String currentState = name2statesMap.get(reflectNode.getName());
				if (currentState != null) {
					g2d.drawString(currentState, xSelf + 5, ySelf);
				} else {
					throw new InputMismatchException(
							reflectNode.getName() + "\tnot found please check your input states.");
				}

				if (reflectNode.getChildCount() != 0){
//					g2d.drawString(reflectNode.getName(), xSelf + 5, ySelf);
				}

				if (Objects.equals("0", currentState)) {
//					g2d.drawString(reflectNode.getName(), xSelf + 5, ySelf);
				}

				// draw state transition
				EvolNode parent = reflectNode.getParent();
				int xParent = (int) grNode.getXParent();
				if (parent == null) {
					if (!Objects.equals(currentState, "0")) {
						String temp = "0 --> " + currentState;
						g2d.drawString(temp, (xParent + xSelf) / 2, ySelf + 15);
					}
				} else {
					String parState = name2statesMap.get(parent.getName());
					if (!Objects.equals(currentState, parState)) {
						String temp = parState + " --> " + currentState;
						g2d.drawString(temp, (xParent + xSelf) / 2, ySelf + 15);
					}
				}

			};
			Consumer<Graphics2D> beforeDrawingProcedure = graphics2D -> {
				graphics2D.setColor(Color.BLACK);
				graphics2D.drawString(title, 10, 20);
			};

			FastSingleTreeVisualizer fastSingleTreeVisualizer = new FastSingleTreeVisualizer();
			fastSingleTreeVisualizer.alwaysOnTop = false;
			SingleTreePaintingPanel<EvolNode> plotTree = fastSingleTreeVisualizer.plotTree(root, title,
					new Dimension(1200, 600), drawer,beforeDrawingProcedure);
		} else {
			if (interactiveWatch) {
				// is interactiveWatch if this is the visualize remnant
				if (outputOneFigurePathOption.isPresent()) {
					plotWithOutputPath(outputOneFigurePathOption.get(), position2_name2stateMap_map, root);
				} else {
					plotAll(arrayList, position2_name2stateMap_map, root);
				}
			}
			summarizeAll(arrayList, root);
		}

	}

	private void plotAll(List<Entry<String, Map<String, String>>> list,
			Map<String, Map<String, String>> position2_name2stateMap_map, EvolNode root) {

		MutableInt mutableInt = new MutableInt(-1);

		MutableObject<FastSingleTreeVisualizer> mutableObject = new MutableObject<>();

		EGPSGuiUtil.universalSimplestIDE(() -> {

			FastSingleTreeVisualizer value = mutableObject.getValue();
			if (value != null) {
				JFrame frame = value.frame;
				frame.setVisible(false);
			}
			int incrementAndGet = mutableInt.incrementAndGet();

			if (incrementAndGet == list.size()) {
				throw new IllegalArgumentException("At the end.");
			}
			Entry<String, Map<String, String>> entry = list.get(incrementAndGet);
			String key = entry.getKey();

			String title = "Position " + key + " states, id is " + incrementAndGet;
			OneNodeDrawer<EvolNode> drawer = (g2d, grNode) -> {

				Map<String, String> name2statesMap = entry.getValue();
				int xSelf = (int) grNode.getXSelf();
				int ySelf = (int) grNode.getYSelf();

				int xParent = (int) grNode.getXParent();

				g2d.fillRect(xSelf - 4, ySelf - 4, 8, 8);
				EvolNode reflectNode = grNode.getReflectNode();
				g2d.drawString(reflectNode.getName(), xSelf + 5, ySelf);
				String currentState = name2statesMap.get(reflectNode.getName());

//				if (currentState != null) {
//					g2d.drawString(currentState, xSelf + 5, ySelf + 15);
//				} else {
//					throw new InputMismatchException(
//							reflectNode.getName() + "\tnot found please check your input states.");
//				}

				EvolNode parent = reflectNode.getParent();
				if (parent == null) {
					if (!Objects.equals(currentState, "0")) {
						String temp = "0 --> " + currentState;
						g2d.drawString(temp, (xParent + xSelf) / 2, ySelf + 15);
					}
				} else {
					String parState = name2statesMap.get(parent.getName());
					if (!Objects.equals(currentState, parState)) {
						String temp = parState + " --> " + currentState;
						g2d.drawString(temp, (xParent + xSelf) / 2, ySelf + 15);
					}
				}

			};

			FastSingleTreeVisualizer fastSingleTreeVisualizer = new FastSingleTreeVisualizer();
			fastSingleTreeVisualizer.alwaysOnTop = false;
			SingleTreePaintingPanel<EvolNode> plotTree = fastSingleTreeVisualizer.plotTree(root, title,
					new Dimension(800, 600), drawer);

			mutableObject.setValue(fastSingleTreeVisualizer);

		});
	}

	private void plotWithOutputPath(String outputPath, Map<String, Map<String, String>> position2_name2stateMap_map,
			EvolNode root) {

		Map<String, String> preferRootState = new HashMap<>();
		if (ancestralStatesPath != null) {
			List<String> lines = null;
			try {
				lines = FileUtils.readLines(new File(ancestralStatesPath), StandardCharsets.US_ASCII);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (lines.size() != 2) {
				throw new IllegalArgumentException("The input prefer root state should has two line");
			}
			String[] positions = EGPSStringUtil.split(lines.get(0), '\t');
			String[] stateStrs = EGPSStringUtil.split(lines.get(1), '\t');
			int length = positions.length;
			for (int i = 1; i < length; i++) {
				String state = stateStrs[i];
				String posStr = positions[i];
				preferRootState.put(posStr, state);
			}
		}

		Table<String, String, String> posNameTable = HashBasedTable.create();

		// 遍历外层Map
		for (Entry<String, Map<String, String>> positionEntry : position2_name2stateMap_map.entrySet()) {
			String position = positionEntry.getKey();
			Map<String, String> name2StateMap = positionEntry.getValue();

			// 遍历内层Map
			for (Entry<String, String> nameEntry : name2StateMap.entrySet()) {
				String name = nameEntry.getKey();
				String state = nameEntry.getValue();

				// 将数据填入Table
				posNameTable.put(position, name, state);
			}
		}

		Map<String, Pair<List<String>, List<String>>> name2PairOfProduceAndExtinctList = Maps.newHashMap();

		EvolNodeUtil.recursiveIterateTreeIF(root, node -> {
			EvolNode parent = node.getParent();
			Pair<List<String>, List<String>> pairs = Pair.of(Lists.newArrayList(), Lists.newArrayList());
			String name = node.getName();
			if (parent != null) {
				Map<String, String> currentNodeStates = posNameTable.column(name);
				Map<String, String> parentNodeStates = posNameTable.column(parent.getName());
				Iterator<Entry<String, String>> currentIte = currentNodeStates.entrySet().iterator();
				Iterator<Entry<String, String>> parentIte = parentNodeStates.entrySet().iterator();
				while (currentIte.hasNext()) {
					Entry<String, String> currEntry = currentIte.next();
					Entry<String, String> parEntry = parentIte.next();
					if (!Objects.equals(currEntry.getKey(), parEntry.getKey())) {
						throw new InputMismatchException();
					}
					if (!Objects.equals(currEntry.getValue(), parEntry.getValue())) {
						String ancestralState = preferRootState.get(currEntry.getKey());
                        Objects.requireNonNull(ancestralState, currEntry.getKey() + "\t ancestral state is not assigned." );
						if (Objects.equals(ancestralState, parEntry.getValue())) {
							// production
							pairs.getLeft().add(currEntry.getKey());
						} else {
							// extinction
							pairs.getRight().add(currEntry.getKey());
						}
					}

				}
			} else {
				// root
				Map<String, String> currentNodeStates = posNameTable.column(name);
				for (Entry<String, String> entry : currentNodeStates.entrySet()) {
					String ancestralState = preferRootState.get(entry.getKey());
					if (!Objects.equals(ancestralState, entry.getValue())) {
						pairs.getLeft().add(entry.getKey());
					}
				}
			}

			name2PairOfProduceAndExtinctList.put(name, pairs);

		});

		// 定义虚线样式
		float[] dashPattern = { 5, 5 }; // 虚线和空白的长度
		BasicStroke dashedStroke = new BasicStroke(1, // 线宽
				BasicStroke.CAP_BUTT, // 端点样式
				BasicStroke.JOIN_MITER, // 拐角样式
				10.0f, // 折角的延展限制
				dashPattern, // 虚线模式
				0.0f // 起始偏移量
		);

		EvolNodeUtil.initializeSize(root);
		OneNodeDrawer<EvolNode> drawer = (g2d, grNode) -> {

			int xSelf = (int) grNode.getXSelf();
			int ySelf = (int) grNode.getYSelf();

			int xParent = (int) grNode.getXParent();
			FontMetrics fontMetrics = g2d.getFontMetrics();
			int height = fontMetrics.getAscent();

			g2d.fillRect(xSelf - 4, ySelf - 4, 8, 8);
			EvolNode reflectNode = grNode.getReflectNode();
			String name = reflectNode.getName();
			if (reflectNode.getChildCount() == 0) {
				g2d.drawString(reflectNode.getName(), xSelf + 5, ySelf + height / 2);
			} else {
				if (reflectNode.getSize() > 30) {
					int stringWidthOfName = fontMetrics.stringWidth(name);
					g2d.drawString(name, xSelf - stringWidthOfName - 5, ySelf + 20);
				}
			}

			Pair<List<String>, List<String>> pair = name2PairOfProduceAndExtinctList.get(name);
			int numOfProduce = pair.getLeft().size();
			int numOfExtinct = pair.getRight().size();

			int xMiddle = (xSelf + xParent) / 2;
			int yMiddle = ySelf;

			if (numOfProduce == 0 && numOfExtinct == 0) {
				return;
			}

			String produceName = String.valueOf(numOfProduce);
			String extinctName = String.valueOf(numOfExtinct);

			int gapSize = 2;

			Double roundRectangle1 = EGPSDrawUtil.getRoundRectangle(xMiddle - 30 - gapSize, yMiddle - 20, 30, 16, 5, 5);
			g2d.setColor(Color.blue);
			g2d.draw(roundRectangle1);

			g2d.setColor(Color.black);
			int stringWidth1 = fontMetrics.stringWidth(produceName);
			g2d.drawString(produceName, (float) (roundRectangle1.getCenterX() - 0.5 * stringWidth1),
					(float) (roundRectangle1.getMaxY() - 2));

			g2d.setColor(Color.red);
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(dashedStroke);
			Double roundRectangle2 = EGPSDrawUtil.getRoundRectangle(xMiddle + gapSize, yMiddle - 20, 30, 16, 5, 5);
			g2d.draw(roundRectangle2);
			g2d.setStroke(oldStroke);

			g2d.setColor(Color.black);
			int stringWidth2 = fontMetrics.stringWidth(extinctName);
			g2d.drawString(extinctName, (float) (roundRectangle2.getCenterX() - 0.5 * stringWidth2),
					(float) (roundRectangle2.getMaxY() - 2));

		};

		FastSingleTreeVisualizer fastSingleTreeVisualizer = new FastSingleTreeVisualizer();
		fastSingleTreeVisualizer.alwaysOnTop = false;

		Font font = new Font("Arial", Font.PLAIN, 16);
		Consumer<Graphics2D> beforeDrawingProcedure = g2d -> {
			g2d.setFont(font);
		};
		Supplier<EvolNode> createNodeFun = () -> {
			return new DefaultPhyNode();
		};
//		SingleTreePaintingPanel<EvolNode> plotTree = fastSingleTreeVisualizer.plotTree(root,
//				"The gene production and extinction progress", new Dimension(800, 600), drawer, beforeDrawingProcedure);
//		Map<String, CollapseProperty> collapseProportyMaps = plotTree.getProperties().collapseProportyMaps;

		HashSet<String> collapsedNodes = Sets.newHashSet(Arrays.asList("Sarcopterygii|400", "Sarcopterygii"));
		EvolNode truncateTreeWithCollapsedNode = TreeOperationUtil.truncateTreeWithCollapsedNode(root, collapsedNodes,
				createNodeFun);
		SingleTreePaintingPanel<EvolNode> plotTree = fastSingleTreeVisualizer.plotTree(truncateTreeWithCollapsedNode,
				"The gene production and extinction progress", new Dimension(800, 600), drawer, beforeDrawingProcedure);
		Map<String, CollapseProperty> collapseProportyMaps = plotTree.getProperties().collapsePropertyMaps;
		collapseProportyMaps.put("Sarcopterygii|400", new CollapseProperty());
		collapseProportyMaps.put("Sarcopterygii", new CollapseProperty());

		// output
		List<String> listOfStrings = Lists.newArrayList();
		listOfStrings.add("nodeName\tproductionEvent\tExtinctionEvent");
		for (Entry<String, Pair<List<String>, List<String>>> entry : name2PairOfProduceAndExtinctList.entrySet()) {
			String key = entry.getKey();
			Pair<List<String>, List<String>> value = entry.getValue();
			List<String> productionEvents = value.getLeft();
			List<String> extinctionEvent = value.getRight();
			if (productionEvents.isEmpty() && extinctionEvent.isEmpty()) {
				continue;
			}

			StringBuilder sBuilder = new StringBuilder();

			{
				StringJoiner sJoiner = new StringJoiner(";");
				for (String string : productionEvents) {
					sJoiner.add(string);
				}
				sBuilder.append(key).append("\t").append(sJoiner.toString());
			}
			{
				StringJoiner sJoiner = new StringJoiner(";");
				for (String string : extinctionEvent) {
					sJoiner.add(string);
				}
				sBuilder.append("\t").append(sJoiner.toString());
			}

			listOfStrings.add(sBuilder.toString());
		}

		try {
			FileUtils.writeLines(new File(outputPath), listOfStrings);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void summarizeAll(List<Entry<String, Map<String, String>>> list, EvolNode root) {

		List<String> outputSummary = new ArrayList<>();

		for (Entry<String, Map<String, String>> entry : list) {
			String key = entry.getKey();

			StringJoiner stringJoiner = new StringJoiner("\t");
			stringJoiner.add(key);

			EvolNodeUtil.recursiveIterateTreeIF(root, currNode -> {
				Map<String, String> name2statesMap = entry.getValue();
				String currentState = name2statesMap.get(currNode.getName());

				EvolNode parent = currNode.getParent();
				if (parent == null) {
					if (!Objects.equals(currentState, "0")) {
						String temp = currNode.getName() + "\t0 --> " + currentState;
						stringJoiner.add(temp);
					}
				} else {
					String parState = name2statesMap.get(parent.getName());
					if (!Objects.equals(currentState, parState)) {
						String temp = currNode.getName() + "\t" + parState + " --> " + currentState;
						stringJoiner.add(temp);
					}
				}
			});

			outputSummary.add(stringJoiner.toString());
		}

		try {
			FileUtils.writeLines(new File(outputTheSummaryEvoutionEvents), outputSummary);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLeafIDColumnName(String leafIDColumnName) {
		this.leafIDColumnName = leafIDColumnName;
	}

	public void setExcludeStatesColumns(HashSet<String> excludeStatesColumns) {
		this.excludeStatesColumns = excludeStatesColumns;
	}

	public void setOutputOneFigurePath(String outputOneFigurePath) {
		this.outputOneFigurePathOption = Optional.of(outputOneFigurePath);
	}

	public void setAncestralStatesPath(String ancestralStatesPath) {
		this.ancestralStatesPath = ancestralStatesPath;
	}
}
