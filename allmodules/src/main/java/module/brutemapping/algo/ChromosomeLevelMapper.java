package module.brutemapping.algo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

import fasta.io.FastaReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import utils.string.EGPSStringUtil;
import module.ambigbse.DNAComplement;
import module.brutemapping.ImportDataBean;

public class ChromosomeLevelMapper implements BiPredicate<String, String> {
	private ImportDataBean importDataInformation;
	Map<String, Integer> subjectMappedRet = new LinkedHashMap<>();

	private List<String> output = new LinkedList<>();

	private BufferedWriter writer;
	private StringBuilder outputBuilder = new StringBuilder();
	private List<String> onlyIncludeKeySeqeunces;
	private List<String> excludeSequenceNames;
	private Entry<String, String> queryEntry;

	private DNAComplement dnaComplement = new DNAComplement();

	public ChromosomeLevelMapper() {
		super();
	}

	public void setImportDataInformation(ImportDataBean importDataInformation) {
		this.importDataInformation = importDataInformation;
	}

	public void run() throws IOException {
		writer = null;
		output.clear();
		subjectMappedRet.clear();

		String genomeFastaPath = importDataInformation.getGenomeFastaPath();
		String onlyIncludeKeySeqeunce = importDataInformation.getOnlyIncludeKeySeqeunce();

		if (onlyIncludeKeySeqeunce != null) {
			String a = onlyIncludeKeySeqeunce.toUpperCase();
			String[] splits = EGPSStringUtil.split(a, ';');
			onlyIncludeKeySeqeunces = Arrays.asList(splits);
		}
		String excludeSequenceName = importDataInformation.getExcludeSequenceName();
		if (excludeSequenceName != null) {
			excludeSequenceName = excludeSequenceName.toUpperCase();
			String[] splits = EGPSStringUtil.split(excludeSequenceName, ';');
			excludeSequenceNames = Arrays.asList(splits);
		}
		String queryFastaPath = importDataInformation.getQueryFastaPath();

		String outputBedPath = importDataInformation.getOutputBedPath();
		if (Strings.isNullOrEmpty(outputBedPath)) {
			System.err.println("You may forget the set the output bed path");
		} else {
			writer = new BufferedWriter(new FileWriter(outputBedPath));
		}

		List<String> lines = FileUtils.readLines(new File(queryFastaPath), StandardCharsets.US_ASCII);
		Iterator<String> iterator2 = lines.iterator();
		while (iterator2.hasNext()) {
			if (iterator2.next().startsWith("#")) {
				iterator2.remove();
			}
		}
		LinkedHashMap<String, String> querySequence = FastaReader.readFastaSequence(lines);

		MutableInt numOfSubjectSeuqnces = new MutableInt();

		Stopwatch stopwatch = Stopwatch.createStarted();

		output.add("Number of subject sequence is : " + numOfSubjectSeuqnces.toString());


		for (Entry<String, String> queryEntry : querySequence.entrySet()) {
			output.add("Now process the sequence: " + queryEntry.getKey());
			this.queryEntry = queryEntry;
			FastaReader.readAndProcessFastaPerEntry(genomeFastaPath, this);

		}

		// 停止计时并获取总耗时
		stopwatch.stop();

		String outputTotalCountFilePath = importDataInformation.getOutputTotalCountFilePath();
		if (Strings.isNullOrEmpty(outputTotalCountFilePath)) {
			output.add("Total count is: ");
			DecimalFormat formatter = new DecimalFormat("#,###");
			for (Entry<String, Integer> entry : subjectMappedRet.entrySet()) {
				String format = formatter.format(entry.getValue());
				output.add(entry.getKey() + "  ===>  " + format);

			}
		} else {
			try (BufferedWriter br = new BufferedWriter(new FileWriter(outputTotalCountFilePath))) {
				for (Entry<String, Integer> entry : subjectMappedRet.entrySet()) {
					br.write(entry.getKey());
					br.write("\t");
					br.write(entry.getValue().toString());
					br.write("\n");
				}
			}
		}

		output.add("Execution time: " + stopwatch.elapsed(TimeUnit.SECONDS) + " sec");

		if (writer != null) {
			writer.close();
		}
	}

	private int processOneChrom(Entry<String, String> queryEntry, String subKey, String subValue)
			throws IOException {

		String DNAseq = subValue;
		KMPMatcher kmpMatcher = new KMPMatcher(DNAseq);

		String query = queryEntry.getValue();
		int totalMappedCount = 0;
		int ret = -1;

		int queryLength = query.length();
		while (true) {
			ret = kmpMatcher.kmpSearch(query, ret + 1);
			if (ret == -1) {
				break;
			}

			totalMappedCount++;
			if (writer != null) {
				outputBuilder.setLength(0);
				outputBuilder.append(subKey);
				outputBuilder.append("\t");
				// 重要 ret 是 0-based 结果
				// 输出结果应该是 0-based所以不用变
				outputBuilder.append(ret);
				outputBuilder.append("\t");
				outputBuilder.append(ret + queryLength);
				outputBuilder.append("\t+");
				writer.write(outputBuilder.toString());
				writer.write("\n");
			}
		}

		// 反向互补链
		String queryReverseComplement = dnaComplement.getReverseComplement(query);
		if (importDataInformation.isNeedReverseComplement()) {
			ret = -1;
			while (true) {
				ret = kmpMatcher.kmpSearch(queryReverseComplement, ret + 1);
				if (ret == -1) {
					break;
				}

				totalMappedCount++;

				if (writer != null) {
					outputBuilder.setLength(0);
					outputBuilder.append(subKey);
					outputBuilder.append("\t");
					outputBuilder.append(ret);
					outputBuilder.append("\t");
					outputBuilder.append(ret + queryLength);
					outputBuilder.append("\t-");
					writer.write(outputBuilder.toString());
					writer.write("\n");
				}
			}
		}

		if (output.size() < 100) {
			output.add(subKey + "  ===>  " + totalMappedCount);
		}


		return totalMappedCount;
	}

	public List<String> getOutput() {
		return output;
	}

	@Override
	public boolean test(String name, String value) {
		String upperCase = name.toUpperCase();
		// The include key sequence

		if (isInclude(upperCase)) {
			// 一定要处理
		} else {
			// The exclude key sequence
			if (isExclude(upperCase)) {
				// 这种情况不用处理
				return false;
			}
		}
		try {
//			System.out.println(name);
//			System.out.flush();
			int count = processOneChrom(queryEntry, name, value);
			String key = name;
			Integer tempCount = subjectMappedRet.get(key);
			if (tempCount == null) {
				tempCount = 0;
				subjectMappedRet.put(key, count);
			} else {
				subjectMappedRet.put(key, tempCount + count);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean isExclude(String upperCase) {
		if (excludeSequenceNames == null) {
			return false;
		}
		if (excludeSequenceNames.isEmpty()) {
			return false;
		}

		for (String string : excludeSequenceNames) {
			if (upperCase.contains(string)) {
				return true;
			}
		}
		return false;
	}

	private boolean isInclude(String upperCase) {
		if (onlyIncludeKeySeqeunces == null) {
			return false;
		}
		if (onlyIncludeKeySeqeunces.isEmpty()) {
			return false;
		}

		for (String string : onlyIncludeKeySeqeunces) {
			if (upperCase.contains(string)) {
				return true;
			}
		}
		return false;
	}

}
