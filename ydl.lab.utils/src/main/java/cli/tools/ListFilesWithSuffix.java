package cli.tools;

import org.apache.commons.compress.utils.Lists;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Utility class to list files with a specific suffix in a given directory.
 *
 * This class provides functionality to list files ending with a specified suffix
 * within a directory and optionally write the results to a TSV file.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class ListFilesWithSuffix {

    /**
     * The entry point of the application.
     * 
     * @param args Command line arguments: [0] suffix, [1] folder path, [2] optional output file path
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ListFilesWithSuffix <suffix> <folder path> [output file path]");
            return;
        }

        String suffix = args[0];
        Path folderPath = Paths.get(args[1]);

        String outputFilePath = args.length > 2 ? args[2] : null;
        List<String> outputs = Lists.newArrayList();
        outputs.add("FileName");

        // Check if the provided path is a directory and exists
        if (!Files.isDirectory(folderPath)) {
            System.out.println("The provided path is not a valid directory.");
            return;
        }


		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*" + suffix)) {
			for (Path entry : stream) {
				if (Files.isRegularFile(entry)) {
                    outputs.add(entry.getFileName().toString());
                }
			}
		} catch (IOException | DirectoryIteratorException e) {
            System.err.println("An error occurred while traversing the directory: " + e.getMessage());
        }

		// Print the result
		System.out.printf("Number of files ending with '%s': %d%n", suffix, outputs.size());
        if (outputFilePath != null){
            // output the outputs to a file
            try {
                Files.write(Paths.get(outputFilePath), outputs);
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }else {
            for (String output : outputs){
                System.out.println(output);
            }
        }
    }
}