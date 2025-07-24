package module.fastatools.rename;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Stopwatch;

import analysis.AbstractAnalysisAction;

public class ContinuousFastaRenamer extends AbstractAnalysisAction {
	int totalSequence = 0;
	int desiredSequenceNumber = 0;

	private List<Pair<String, String>> targetStringSets;
	private boolean keepAllSearchStrings = false;

	private List<String> outputList4console = new ArrayList<>();

	public ContinuousFastaRenamer() {

	}


	public void setTargetStringSets(List<Pair<String, String>> targetStringSets) {
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

		getOutputList4console().clear();

		getOutputList4console().add("TargetString need to search is: " + targetStringSets.size());

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

					Optional<String> match = isMatch(readLine, seq);
					if (match.isPresent()) {
						desiredSequenceNumber++;

						bufferedOutputStream.write(">");
						bufferedOutputStream.write(match.get());
						bufferedOutputStream.write("\n");
						// 下一行一定有
						bufferedOutputStream.write(seq);
						bufferedOutputStream.write("\n");
					}

				}
			}
		}

		bufferedOutputStream.close();

		getOutputList4console().add("Desired sequence is: " + desiredSequenceNumber);
		getOutputList4console().add("Total sequence is: " + totalSequence);

		if (!keepAllSearchStrings) {
			if (targetStringSets.size() > 0) {
				getOutputList4console().add("The remaining target strings are:");
				for (Pair<String, String> string : targetStringSets) {
					getOutputList4console().add(string.toString());
				}
			}
		}

	}

	private Optional<String> isMatch(String readLine, String seq) {
		Iterator<Pair<String, String>> iterator = targetStringSets.iterator();
		while (iterator.hasNext()) {
			Pair<String, String> next = iterator.next();
			if (readLine.contains(next.getKey())) {
				if (keepAllSearchStrings) {

				} else {
					iterator.remove();
				}
				return Optional.of(next.getValue());
			}
		}
		return Optional.empty();
	}

	public static void main(String[] args) throws Exception {

		ContinuousFastaRenamer fastaConcatenator = new ContinuousFastaRenamer();
		fastaConcatenator
				.setInputPath("C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Ensembl.all.filtered.cdna.fa");
		fastaConcatenator.setOutputPath("C:\\Users\\yudal\\Documents\\project\\WntEvolution\\human_NDP_cDNA.fa");

		Stopwatch stopwatch = Stopwatch.createStarted();

		fastaConcatenator.doIt();

		// 停止计时并获取总耗时
		stopwatch.stop();

		// 输出执行时间，可以选择不同的时间单位
		System.out.println("任务执行时间: " + stopwatch.elapsed().toMillis() + " 毫秒");
	}


	public List<String> getOutputList4console() {
		return outputList4console;
	}


	public void setOutputList4console(List<String> outputList4console) {
		this.outputList4console = outputList4console;
	}

}
