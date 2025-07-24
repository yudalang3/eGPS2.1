package module.evoltre.rinterface;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Objects;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.phylogeny.DefaultPhyNode;
import evoltree.phylogeny.NWKInternalCoderDecoder;
import evoltree.phylogeny.NWKLeafCoderDecoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;

public class API4R {

	public String[] getNodeNames(String tree_path, String targetHTU, boolean getOTU, boolean getHTU) throws Exception {

		PrimaryNodeTreeDecoder<DefaultPhyNode> treeCoder = new PrimaryNodeTreeDecoder<>(
				new NWKLeafCoderDecoder<DefaultPhyNode>(), new NWKInternalCoderDecoder<DefaultPhyNode>());

		String nwkString = FileUtils.readFileToString(new File(tree_path), StandardCharsets.UTF_8);
		DefaultPhyNode decode = treeCoder.decode(nwkString);

		DefaultPhyNode searchRoot = null;
		if (targetHTU == null) {
			searchRoot = decode;
		} else {
			Optional<DefaultPhyNode> searchRootNode = EvolNodeUtil.searchNodeWithReturn(decode, node -> {
				if (Objects.equal(targetHTU, node.getName())) {
					return true;
				}
				return false;
			});
			if (searchRootNode.isEmpty()) {
				throw new InputMismatchException("Sorry, the targetHTU not found.");
			}
			searchRoot = searchRootNode.get();
		}

		List<String> output = new ArrayList<>();
		EvolNodeUtil.recursiveIterateTreeIF(searchRoot, node -> {

			boolean isLeaf = node.getChildCount() == 0;
			if (isLeaf) {
				if (getOTU) {
					output.add(node.getName());
				}
			} else {
				if (getHTU) {
					output.add(node.getName());
				}
			}
		});

		return output.toArray(new String[0]);
	}

	public String getString() {
		// 返回一个字符串
		return "Hello, World!";
	}

}
