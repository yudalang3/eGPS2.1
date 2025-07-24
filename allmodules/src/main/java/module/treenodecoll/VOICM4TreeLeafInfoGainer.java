package module.treenodecoll;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;
import org.apache.commons.compress.utils.Lists;

import com.google.common.base.Strings;

import egps2.panels.dialog.SwingDialog;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.phylogeny.DefaultPhyNode;
import module.evoltreio.TreeParser4Evoltree;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.bean.VoiceExampleGenerator;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class VOICM4TreeLeafInfoGainer extends AbstractGuiBaseVoiceFeaturedPanel {

	private GuiMain guiMain;

	private VoiceExampleGenerator generator;
	private ParamsAssignerAndParser4TreeNodeInfoCollector parameterMap = new ParamsAssignerAndParser4TreeNodeInfoCollector();

	public VOICM4TreeLeafInfoGainer(GuiMain guiMain) {
		this.guiMain = guiMain;
		
		generator = parameterMap.getExampleGenerator();
	}

	@Override
	public String getExampleText() {
		String example = generator.generateExample(parameterMap.getRequiredParams());
		return example;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		// no need, as the getExampleText method is implemented
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		TreeLeafObtainerImportInfoBean bean = parameterMap.generateTreeFromKeyValue(o);

		List<String> output = Lists.newArrayList();
		output.add("Start to read the tree from input ...");
		guiMain.setText4Console(output);

		TreeParser4Evoltree treeParser4Evoltree = new TreeParser4Evoltree();
		DefaultPhyNode phyloTree = null;
		try {
			Optional<DefaultPhyNode> tree = treeParser4Evoltree.getTree(bean);
			if (!tree.isPresent()) {
				SwingDialog.showErrorMSGDialog("Parse error", "Input parameter error, please check.");
				return;
			}
			phyloTree = tree.get();
		} catch (Exception e) {
			e.printStackTrace();
			SwingDialog.showErrorMSGDialog("Parse error", "Input parameter error, please check.");
			return;
		}

		EvolNodeUtil.initializeSize(phyloTree);

		guiMain.setText4Console(output);

		int ladderize = bean.getLadderize();
		if (ladderize == 1) {
			output.add("Ladderize the tree, start to ladderize up.");
			guiMain.recordTheLadderizeFunction();
			EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(phyloTree, false);
		} else if (ladderize == 2) {
			output.add("Ladderize the tree, start to ladderize down.");
			guiMain.recordTheLadderizeFunction();

			EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(phyloTree, true);
		} else {
			output.add("Do not need to ladderize.");
			guiMain.setText4Console(output);
		}

		output.add("The number of OTU is ".concat(String.valueOf(phyloTree.getSize())));

		output.add("Successfully executed.");
		output.add("The header names are:");

		List<DefaultPhyNode> targetNodes = null;

		int collectNodeType = bean.getCollectNodeType();
		if (collectNodeType == 1) {
			// internal node
			List<DefaultPhyNode> tmp = new LinkedList<>();
			EvolNodeUtil.recursiveIterateTreeIF(phyloTree, node -> {
				if (node.getChildCount() > 0) {
					tmp.add(node);
				}
			});
			targetNodes = tmp;
		} else if (collectNodeType == 2) {
			targetNodes = EvolNodeUtil.getLeavesByRecursive(phyloTree);
		} else {
			List<DefaultPhyNode> tmp = new LinkedList<>();
			EvolNodeUtil.recursiveIterateTreeIF(phyloTree, node -> {
				tmp.add(node);
			});
			targetNodes = tmp;
		}

		int index = 0;
		for (DefaultPhyNode node : targetNodes) {
			output.add(node.getName());
			if (index > 5) {
				break;
			}
			index++;
		}
		List<String> outputContent = new LinkedList<>();
		outputContent.add("NodeType\tName");
		for (DefaultPhyNode node : targetNodes) {
			String name = node.getName();
			String type = null;
			if (node.getChildCount() == 0) {
				type = "ExternalNode";
			}else {
				type = "InternalNode";
			}
			
			outputContent.add(type + "\t" +name);
		}
		guiMain.setText4Console(outputContent);

		guiMain.recordTheExportFunction();

		if (Strings.isNullOrEmpty(bean.getExportPath())) {
			return;
		}

		try {
			Files.write(Paths.get(bean.getExportPath()), outputContent, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
