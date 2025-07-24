package cli.tools;

import evoltree.phylogeny.DefaultPhyNode;
import evoltree.phylogeny.PhyloTreeEncoderDecoder;
import evoltree.struct.EvolNode;
import evoltree.struct.util.EvolNodeUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class to remove internal node names from phylogenetic trees.
 *
 * This class reads Newick format tree data from a file, removes names from
 * internal nodes, and outputs the modified tree structure.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class RemoveInternalNodeNames {
    /**
     * Removes names from internal nodes in a phylogenetic tree.
     * 
     * @param decode the root node of the phylogenetic tree
     */
    private static void removeInternalNodeNames(EvolNode decode) {
        EvolNodeUtil.recursiveIterateTreeIF(decode, node -> {
            if (node.getChildCount() > 0) { // Check if the node is an internal node
                node.setName(""); // Remove the name of the internal node
            }
        });
    }

    /**
     * The entry point of the application.
     * 
     * @param args command line arguments: [0] input file path
     * @throws IOException if there is an error reading the input file
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java RemoveInternalNodeNames /input/file/path");
            return;
        }

        Path filePath = Paths.get(args[0]);
        // Check if the provided path is a directory and exists
        if (Files.isDirectory(filePath)) {
            System.out.println("Please input a path");
            return;
        }

        PhyloTreeEncoderDecoder phyloTreeEncoderDecoder = new PhyloTreeEncoderDecoder();
        // Get all contents of file

        List<String> contents = Files.readAllLines(filePath);
        for (String line : contents) {
            try {
                DefaultPhyNode decode = phyloTreeEncoderDecoder.decode(line);
                removeInternalNodeNames(decode);
                String encode = phyloTreeEncoderDecoder.encode(decode);
                System.out.println(encode);
            } catch (Exception e) {
                System.out.println("The provided file is not in NWK format.");
                throw new RuntimeException(e);
            }
        }
    }
}