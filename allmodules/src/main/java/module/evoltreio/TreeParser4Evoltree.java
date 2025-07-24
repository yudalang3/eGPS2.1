package module.evoltreio;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;

import evoltree.phylogeny.DefaultPhyNode;
import evoltree.phylogeny.NWKInternalCoderDecoder;
import evoltree.phylogeny.NWKLeafCoderDecoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;
import module.evolview.gfamily.work.NodeUtils;

public class TreeParser4Evoltree {

	private DefaultPhyNode getTree(String nwkStr, boolean removeBlank) throws Exception {
		PrimaryNodeTreeDecoder<DefaultPhyNode> treeDecoderWithMap = new PrimaryNodeTreeDecoder<>(
				new NWKLeafCoderDecoder<DefaultPhyNode>(), new NWKInternalCoderDecoder<DefaultPhyNode>());

		DefaultPhyNode root = treeDecoderWithMap.decode(nwkStr);
		return root;
	}

	private Optional<DefaultPhyNode> getTreeFromTreeTableFormat(String treePath) throws Exception {
		List<String> input3 = FileUtils.readLines(new File(treePath), StandardCharsets.UTF_8);
		DefaultPhyNode rootNode = (DefaultPhyNode) NodeUtils.parseTableTree(input3);
		return Optional.of(rootNode);
	}

	public Optional<DefaultPhyNode> getTree(EvolTreeImportInfoBean object) throws Exception {
		boolean removeWhitespace = object.isRemoveWhitespace();
		Optional<DefaultPhyNode> optRoot = null;
		if (!Strings.isNullOrEmpty(object.getInput_nwk_string())) {
			optRoot = Optional.of(getTree(object.getInput_nwk_string(), removeWhitespace));
		} else if (!Strings.isNullOrEmpty(object.getInput_nwk_path())) {
			File file = new File(object.getInput_nwk_path());

			List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

			StringBuilder sBuilder = new StringBuilder();
			for (String string : lines) {
				sBuilder.append(string);
			}
			String nwkStr = sBuilder.toString();
			optRoot = Optional.of(getTree(nwkStr, removeWhitespace));
		} else {
			optRoot = getTreeFromTreeTableFormat(object.getInput_tableLike_path());
		}

		return optRoot;
	}

}
