package module.treeconveop.gui;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import evoltree.struct.io.PrimaryNodeTreeDecoder;
import org.apache.commons.io.FileUtils;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import egps2.frame.ComputationalModuleFace;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.phylogeny.DefaultPhyNode;
import evoltree.phylogeny.NWKInternalCoderDecoder;
import evoltree.phylogeny.NWKLeafCoderDecoder;
import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeCoder;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class SubTreeExtractorPanel extends DIYToolSubTabModuleFace {


	public SubTreeExtractorPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.phylogenetic.file", "",
				"Input phylogentic tree file in nwk format. Necessary");

		mapProducer.addKeyValueEntryBean("leaf.entries", "",
				"input target entries, user can directly input or paste entries. Or input one file path. Necessary\n# leaf.entries=file/to/path\n# leaf.entries = \n# a\n# b\n# c");

		mapProducer.addKeyValueEntryBean("output.file", "", "Output file. Necessary");
	}


	@Override
	protected void execute(Map<String, String> para) throws Exception {

	}


	@Override
	protected boolean whetherExecuteWithString2ListMap() {
		return true;
	}

	@Override
	protected void executeWithList(Map<String, LinkedList<String>> para) throws Exception {
		String inputTableFile = null;
		List<String> entries = null;
		String outputFile = null;

		LinkedList<String> list = para.get("$input.phylogenetic.file");
		String string = getStringAfterEqualStr(list.get(0));
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $input.phylogenetic.file ");
		} else {
			inputTableFile = string;
		}

		list = para.get("$output.file");
		string = getStringAfterEqualStr(list.get(0));
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $output.file ");
		} else {
			outputFile = string;
		}

		list = para.get("$leaf.entries");
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Plese input the $leaf.entries ");
		} else {
			int size = list.size();
			if (size > 1) {
				entries = list.subList(1, size);
			} else {
				String filePath = getStringAfterEqualStr(list.get(0));
				File file = new File(filePath);
				if (!file.exists()) {
					throw new IllegalArgumentException(
							"Plese input file of the $entries NOT exists: ".concat(filePath));
				}
				entries = FileUtils.readLines(file, StandardCharsets.UTF_8);
			}

		}

		Stopwatch stopwatch = Stopwatch.createStarted();

		List<String> outputs = new LinkedList<>();

		perform(inputTableFile, entries, outputFile);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);

	}

	private void perform(String inputTableFile, List<String> entries, String outputFile) throws Exception {

		String nwkStr = FileUtils.readFileToString(new File(inputTableFile), StandardCharsets.UTF_8);

		PrimaryNodeTreeDecoder<DefaultPhyNode> treeDecoderWithMap = new PrimaryNodeTreeDecoder<>(
				new NWKLeafCoderDecoder<DefaultPhyNode>(), new NWKInternalCoderDecoder<DefaultPhyNode>());

		DefaultPhyNode root = treeDecoderWithMap.decode(nwkStr);
		List<DefaultPhyNode> leaves = EvolNodeUtil.getLeaves(root);
		HashSet<String> hashSet = new HashSet<>(entries);
		int matchedCount = 0;
		for (DefaultPhyNode leaf : leaves) {
			String name = leaf.getName();
			if (hashSet.contains(name)) {
				leaf.setDoubleVariable(11);
				matchedCount++;
			}
		}
		if (matchedCount == 0) {
			throw new InputMismatchException("Sorry, no leaf name matchs. Please check your input entries.");
		}

		iterateTree(root);

		EvolNode newRoot = extractTree(root);

		TreeCoder<EvolNode> treeCoder = new TreeCoder<EvolNode>();
		String code = treeCoder.code(newRoot);

		FileUtils.write(new File(outputFile), code, StandardCharsets.UTF_8);

	}

	private EvolNode extractTree(DefaultPhyNode node) {
		if (node.getBootstrapValue() < 10) {
			return null;
		}

		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			EvolNode child = node.getChildAt(i);
			EvolNode retNode = extractTree((DefaultPhyNode) child);
			if (retNode != null) {
				node.addChild(retNode);
			}
		}

		ArrayBasedNode ret = new ArrayBasedNode();
		ret.setLength(node.getLength());
		ret.setID(node.getID());
		ret.setName(node.getName());
		return ret;
	}

	private boolean iterateTree(EvolNode node) {

		int childCount = node.getChildCount();

		if (childCount == 0) {
			return node.getDoubleVariable() > 10;
		}

		boolean ret = false;
		for (int i = 0; i < childCount; i++) {
			EvolNode child = node.getChildAt(i);
			boolean iterateTree = iterateTree(child);
			if (iterateTree) {
				ret = true;
			}
		}

		return ret;
	}

	@Override
	public String getTabName() {
		return new String("Subtree extractor");
	}

	@Override
	public String getShortDescription() {
		return new String("Import phylogenetic tree and extractor the sub tree.");
	}
}
