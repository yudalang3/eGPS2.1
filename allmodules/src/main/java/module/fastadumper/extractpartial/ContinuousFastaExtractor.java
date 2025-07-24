package module.fastadumper.extractpartial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;

import analysis.AbstractAnalysisAction;

public class ContinuousFastaExtractor extends AbstractAnalysisAction {
	int totalSequence = 0;
	int desiredSequenceNumber = 0;

	private List<String> targetStringSets;
	private boolean keepAllSearchStrings = false;

	List<String> outputList4console = new ArrayList<>();
	private List<String> originalLines;

	public ContinuousFastaExtractor() {

	}

	public void setTargetStringSets(List<String> targetStringSets) {
		this.targetStringSets = targetStringSets;
	}

	public void setKeepAllSearchStrings(boolean keepAllSearchStrings) {
		this.keepAllSearchStrings = keepAllSearchStrings;
	}

	@Override
	public void doIt() throws Exception {

		if (targetStringSets == null) {
			throw new InputMismatchException("Please input the target string set first.");
		}

		outputList4console.clear();

		outputList4console.add("TargetString need to search is: " + targetStringSets.size());

		int sz = 10 * 1024 * 1024;
		BufferedWriter bufferedOutputStream = new BufferedWriter(new FileWriter(outputPath), sz);

		try (BufferedReader br = new BufferedReader(new FileReader(inputPath), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				if (readLine.isEmpty()) {
					continue;
				}
				if (readLine.charAt(0) == '>') {
					totalSequence++;

					String seq = br.readLine();

					if (isMatch(readLine, seq)) {
						desiredSequenceNumber++;

						bufferedOutputStream.write(readLine);
						bufferedOutputStream.write("\n");
						// 下一行一定有
						bufferedOutputStream.write(seq);
						bufferedOutputStream.write("\n");
					}

				}
			}
		}

		bufferedOutputStream.close();

		outputList4console.add("Desired sequence is: " + desiredSequenceNumber);
		outputList4console.add("Total sequence is: " + totalSequence);

		if (!keepAllSearchStrings) {
			if (targetStringSets.size() > 0) {
				outputList4console.add("The remaining target strings are:");

				for (String string : targetStringSets) {

					String str = getOriginalLine(string);
					outputList4console.add(str);
				}
			}
		}

	}

	private String getOriginalLine(String string) {
		if (originalLines != null) {
			for (String line : originalLines) {
				if (line.contains(string)) {
					return line;
				}
			}

		}
		return string;
	}

	private boolean isMatch(String readLine, String seq) {
		Iterator<String> iterator = targetStringSets.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			if (readLine.contains(next)) {
				if (keepAllSearchStrings) {

				} else {
					iterator.remove();
				}
				return true;
			}
		}
		return false;
	}

	public List<String> getOutputList4console() {
		return outputList4console;
	}

	public void setTargetStringContents(List<String> originalLines) {
		this.originalLines = originalLines;

	}

}
