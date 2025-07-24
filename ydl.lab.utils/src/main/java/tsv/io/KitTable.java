package tsv.io;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

/**
 * This is the table bean class.
 */
public class KitTable {

	/**
	 * contents per row, for example
	 */
	private List<List<String>> contents;
	/**
	 * original lines
	 */
	private List<String> originalLines;
	/**
	 * header name list
	 */
	private List<String> headerNames;
	// file path
	private String path;
	
	public KitTable() {
		contents = Lists.newArrayList();
		headerNames = Lists.newArrayList();
		originalLines = Lists.newArrayList();
	}

	public int getNumOfRows() {
		return contents.size();
	}
	
	public int getNumOfColum() {
		return headerNames.size();
	}

	public List<List<String>> getContents() {
		return contents;
	}

	public void setContents(List<List<String>> contents) {
		this.contents = contents;
	}

	public List<String> getOriginalLines() {
		return originalLines;
	}

	public void setOriginalLines(List<String> originalLines) {
		this.originalLines = originalLines;
	}

	public List<String> getHeaderNames() {
		return headerNames;
	}

	public void setHeaderNames(List<String> headerNames) {
		this.headerNames = headerNames;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		if (path != null) {
			sBuilder.append("Path: ").append(path).append("\n");
		}
		
		sBuilder.append("Header: ").append(headerNames.toString()).append("\n");
		sBuilder.append("Number of cols: ").append(getNumOfColum()).append("\n");
		sBuilder.append("Number of row: ").append(getNumOfRows()).append("\n");
		
		return sBuilder.toString();
	}

	public void save2file(String path) throws IOException {

		List<String> outputLines = new LinkedList<>();

		if (headerNames != null && !headerNames.isEmpty()) {
			StringJoiner stringJoiner = new StringJoiner("\t");
			for (String string : headerNames) {
				stringJoiner.add(string);
			}
			outputLines.add(stringJoiner.toString());
		}

		for (List<String> list : contents) {
			StringJoiner stringJoiner = new StringJoiner("\t");
			for (String string : list) {
				stringJoiner.add(string);
			}
			outputLines.add(stringJoiner.toString());
		}

		FileUtils.writeLines(new File(path), outputLines);
	}
}
