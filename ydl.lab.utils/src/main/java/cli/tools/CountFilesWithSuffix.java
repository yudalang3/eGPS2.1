package cli.tools;

import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.nio.file.*;

/**
 * Utility class to count files with a specific suffix in a given directory.
 *
 * This class provides a command-line interface to count files that end with a
 * specified suffix within a directory (not recursive).
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class CountFilesWithSuffix {

    /**
     * The entry point of the application.
     * 
     * @param args Command line arguments: [0] suffix, [1] folder path
     */
    public static void main(String[] args) {
        // Check if the correct number of arguments have been provided
        if (args.length != 2) {
            System.out.println("Usage: java CountFilesWithSuffix <suffix> <folder path>");
            return;
        }

        String suffix = args[0];
        Path folderPath = Paths.get(args[1]);

        // Check if the provided path is a directory and exists
        if (!Files.isDirectory(folderPath)) {
            System.out.println("The provided path is not a valid directory.");
            return;
        }

        MutableInt countOfFiles = new MutableInt(0);

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*" + suffix)) {
			for (Path entry : stream) {
				// Only count files, ignore directories that match the pattern
				if (Files.isRegularFile(entry)) {
                    countOfFiles.increment();
                }
			}
		} catch (IOException | DirectoryIteratorException e) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			// DirectoryIteratorException could be thrown if an I/O error occurs while
			// iterating over the directory.
            System.err.println("An error occurred while traversing the directory: " + e.getMessage());
        }

		// Print the result
		System.out.printf("Number of files ending with '%s': %d%n", suffix, countOfFiles.intValue());
    }
}