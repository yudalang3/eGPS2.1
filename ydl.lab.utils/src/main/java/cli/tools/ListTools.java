package cli.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class to list available command-line tools.
 *
 * This class maintains a registry of available CLI tools and their descriptions,
 * and provides a way to display them to the user.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class ListTools {
	record FileEntry(String name, String description) {
	}

	static List<FileEntry> listOfFiles = new ArrayList<>();

	/**
	 * The entry point of the application.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		addEntry(ClipboardPathNormalized.class.getName(), "Convert the file path C:\\a\\b\\c.txt to /");
		addEntry(CountFilesWithSuffix.class.getName(), "Count the files with certain suffix.");
		addEntry(ListFilesWithSuffix.class.getName(), "List the names of files in a directory that end with a specified suffix and generate a TSV file.");

		System.out.println("Current available programs are:");

		// Use a more compact approach with streams and forEach
		AtomicInteger index = new AtomicInteger(1);
		listOfFiles.forEach(entry -> {
			System.out.println(index.getAndIncrement() + "\t" + entry.name() + "\t" + entry.description());
		});
	}

	/**
	 * Adds a tool entry to the list of available tools.
	 * 
	 * @param name the fully qualified class name of the tool
	 * @param description a description of the tool's functionality
	 */
	private static void addEntry(String name, String description) {
		listOfFiles.add(new FileEntry(name, description));
	}
}
