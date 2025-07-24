package evoltree.phylogeny;

import evoltree.struct.TreeCoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;

/**
 * Encoder/Decoder for complete phylogenetic trees in Newick (NWK) format.
 *
 * This class provides methods to encode and decode entire phylogenetic trees
 * using appropriate leaf and internal node coders/decoders.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class PhyloTreeEncoderDecoder {

    /**
     * Converts a phylogenetic tree to its Newick format string representation.
     * 
     * @param tree the root node of the tree to encode
     * @return the Newick format string
     */
    public String encode(DefaultPhyNode tree) {
        TreeCoder<DefaultPhyNode> coder = new TreeCoder<>(new NWKLeafCoderDecoder<DefaultPhyNode>(), new NWKInternalCoderDecoder<DefaultPhyNode>());
        return coder.code(tree);
    }

    /**
     * Parses a Newick format string into a phylogenetic tree structure.
     * 
     * @param line the Newick format string
     * @return the root node of the parsed tree
     * @throws Exception if there is an error during parsing
     */
    public DefaultPhyNode decode(String line) throws Exception {
        PrimaryNodeTreeDecoder<DefaultPhyNode> decoder = new PrimaryNodeTreeDecoder<>(new NWKLeafCoderDecoder<DefaultPhyNode>(), new NWKInternalCoderDecoder<DefaultPhyNode>());
        return decoder.decode(line);
    }
}
