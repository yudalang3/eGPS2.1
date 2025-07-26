package api.rpython;

import com.google.common.base.Objects;
import evoltree.phylogeny.DefaultPhyNode;
import evoltree.phylogeny.NWKInternalCoderDecoder;
import evoltree.phylogeny.NWKLeafCoderDecoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;
import evoltree.struct.util.EvolNodeUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

/**
 * High-level API for manipulating evolutionary trees (EvolTree).
 * Provides methods to extract node information from phylogenetic trees in Newick format.
 */
public class EvolTreeManipulator {

	/**
	 * Get node names from a phylogenetic tree file
	 * @param tree_path Path to the tree file in Newick format
	 * @param targetHTU Target internal node name to start search from (null for root)
	 * @param getOTU Whether to include operational taxonomic units (leaf nodes)
	 * @param getHTU Whether to include hierarchical taxonomic units (internal nodes)
	 * @return Array of node names matching the criteria
	 * @throws Exception If there's an error reading or parsing the tree file
	 */
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

	/**
	 * Get a string representation of this class
	 * @return Class name with a greeting message
	 */
	public String getString() {
		return "Hello, this is: ".concat(this.getClass().getName());
	}


}