package cli.tools;

import com.google.common.base.Strings;
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
 * Utility class to replace spaces in phylogenetic tree node names with underscores.
 *
 * This class reads Newick format tree data from a file, replaces spaces in node names
 * with underscores, and outputs the modified tree structure.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class NodeNames4Space4Underline {
    
    /**
     * Recursively replaces spaces with underscores in node names of a phylogenetic tree.
     * 
     * @param decode the root node of the phylogenetic tree
     */
    private static void nodeNameChange(EvolNode decode) {
        EvolNodeUtil.recursiveIterateTreeIF(decode, node -> {
            String name = node.getName();
            if (!Strings.isNullOrEmpty(name)){
                String replace = name.replace(' ', '_');
                node.setName(replace);
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
            System.out.println("Usage: java NodeNames4Space4Underline /input/file/path");
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
                nodeNameChange(decode);
                String encode = phyloTreeEncoderDecoder.encode(decode);
                System.out.println(encode);
            } catch (Exception e) {
                System.out.println("The provided file is not in NWK format.");
                throw new RuntimeException(e);
            }
        }
    }
}