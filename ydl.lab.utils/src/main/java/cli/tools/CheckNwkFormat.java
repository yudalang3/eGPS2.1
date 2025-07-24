package cli.tools;

import evoltree.struct.TreeDecoder;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Utility class to check if a file is in Newick (NWK) format.
 *
 * This class attempts to parse each line of the input file using a TreeDecoder
 * to verify if it conforms to the Newick format specification.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class CheckNwkFormat {

    /**
     * Main method to check if a file is in Newick (NWK) format.
     * 
     * @param args command line arguments (single parameter: input file path)
     * @throws IOException if there is an error reading the file
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java CheckNwkFormat /input/file/path");
            return;
        }

        Path filePath = Paths.get(args[0]);
        // Check if the provided path is a directory and exists
        if (Files.isDirectory(filePath)) {
            System.out.println("Please input a path");
            return;
        }

        TreeDecoder treeDecoder = new TreeDecoder();
        // Get all contents of file

        List<String> contents = Files.readAllLines(filePath);
        for (String line : contents) {
            try {
                treeDecoder.decode(line);
            } catch (Exception e) {
                System.out.println("The provided file is not in NWK format.");
                throw new RuntimeException(e);
            }
        }

        System.out.println("Success!");
    }
}