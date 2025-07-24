package module.fastadumper.extractmatch;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import fasta.io.FastaReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

public class FastaDumperRunner {
	private String inputPath;
	private final String inputPathString = "$inputPath=";

	private String fileSuffix;
	private final String fileSuffixString = "$fileSuffix=";

	private boolean removeGap;
	private final String removeGapString = "$removeGap=";

	private String outputFile;
	private final String outputFileStrng = "$outputFile=";

	private static Pattern pattern2replace = Pattern.compile("-");

	private VOICM4FastaDumper voicm4FastaDumper;

	private class FastaTargetInfo {
		int positionStartInclusive;
		int positionEndExclusive;
		String targetName;
		
		@Override
		public String toString() {
			return positionStartInclusive + "\t" + positionEndExclusive + "\t" + targetName;
		}
	}

	public FastaDumperRunner() {

	}

	public FastaDumperRunner(VOICM4FastaDumper voicm4FastaDumper) {
		this.voicm4FastaDumper = voicm4FastaDumper;
	}

	public void run(List<String> inputStrings) throws IOException {
		Map<String, FastaTargetInfo> seqOfInterestMap = new HashMap<>();
		for (String string : inputStrings) {
			if (string.startsWith(inputPathString)) {
				inputPath = string.substring(inputPathString.length());
				continue;
			}

			if (string.startsWith(fileSuffixString)) {
				fileSuffix = string.substring(fileSuffixString.length());
				continue;
			}
			if (string.startsWith(removeGapString)) {
				removeGap = BooleanUtils.toBoolean(string.substring(removeGapString.length()));
				continue;
			}
			if (string.startsWith(outputFileStrng)) {
				outputFile = string.substring(outputFileStrng.length());
				continue;
			}
			FastaTargetInfo fastaTargetInfo = new FastaTargetInfo();
			String[] split = string.split("\t");
			if (split == null || split.length == 0) {
				throw new IllegalArgumentException("Sorrry please input the correct format.\n");
			}
			String originalSeqName = split[0];
			if (split.length == 1) {
				fastaTargetInfo.targetName = originalSeqName;
				fastaTargetInfo.positionStartInclusive = 0;
				fastaTargetInfo.positionEndExclusive = Integer.MAX_VALUE;
			} else if (split.length == 2) {
				fastaTargetInfo.targetName = originalSeqName;
				// one-based to 0-based
				fastaTargetInfo.positionStartInclusive = Integer.parseInt(split[1]) - 1;
				fastaTargetInfo.positionEndExclusive = Integer.MAX_VALUE;
			} else if (split.length == 3) {
				fastaTargetInfo.targetName = originalSeqName;
				fastaTargetInfo.positionStartInclusive = Integer.parseInt(split[1]) - 1;
				fastaTargetInfo.positionEndExclusive = Integer.parseInt(split[2]);
			} else {
				// 4
				fastaTargetInfo.positionStartInclusive = Integer.parseInt(split[1]) - 1;
				fastaTargetInfo.positionEndExclusive = Integer.parseInt(split[2]);
				fastaTargetInfo.targetName = split[3];
			}
			seqOfInterestMap.put(originalSeqName, fastaTargetInfo);

		}
		// Check the input
		if (seqOfInterestMap.isEmpty()) {
			throw new IllegalArgumentException("Sorry, please enter your extract records.\n");
		}
		if (inputPath == null) {
			throw new IllegalArgumentException("Sorry, please enter your input path.\n");
		} else {
			if (!new File(inputPath).exists()) {
				throw new IllegalArgumentException("Sorry, your input path is not exists.\n");
			}
		}
		if (outputFile == null || Strings.isNullOrEmpty(outputFile)) {
			throw new IllegalArgumentException("Sorry, please enter your outputFile path.\n");
		} else {
			if (new File(outputFile).exists()) {
				outputContent("Overwriting the original file: ".concat(outputFile));
			}
		}
		//

		File inputFile = new File(inputPath);
		boolean directory = inputFile.isDirectory();

		int numOfSequences2extract = seqOfInterestMap.size();
		int finalFound = 0;

		File[] listFiles = null;

		if (directory) {
			listFiles = inputFile.listFiles();
		} else {
			listFiles = new File[] { inputFile };
		}

		Arrays.sort(listFiles);
		List<String> outputStrings = new LinkedList<>();

		outputContent("Total files is : " + listFiles.length);

		for (File oneFastaFile : listFiles) {
			if (oneFastaFile.getName().startsWith(".")) {
				outputContent("Ignore : " + oneFastaFile.getAbsolutePath());
				continue;
			}
			if (oneFastaFile.isDirectory()) {
				outputContent("Ignore directory : " + oneFastaFile.getAbsolutePath());
				continue;
			}

			if (directory && !oneFastaFile.getName().endsWith(fileSuffix)) {
				// 如果是 dir ， 以 fileSuffix结尾的才会处理
				outputContent("Ignore : " + oneFastaFile.getName());
				continue;
			}

			LinkedHashMap<String, String> readFastaDNASequence = FastaReader.readFastaDNASequence(oneFastaFile);

			Set<Entry<String, String>> entrySet = readFastaDNASequence.entrySet();
			Iterator<Entry<String, String>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				// 在找到的同时去掉这个选项
				FastaTargetInfo pair = seqOfInterestMap.remove(key);
				if (pair == null) {
					continue;
				}
				outputContent("Find sequence " + key + " , in the file " + oneFastaFile.getName());
				
				if (pair.targetName == null) {
					outputStrings.add(">".concat(key));
				}else {
					outputStrings.add(">".concat(pair.targetName));
				}
				String value = entry.getValue();
				String replacedString = null;
				if (removeGap) {
					replacedString = pattern2replace.matcher(value).replaceAll("");
				} else {
					replacedString = value;
				}
				
				int startIn = pair.positionStartInclusive;
				int endEx = pair.positionEndExclusive;
				int lengthOfSequence = replacedString.length();
				if (endEx > lengthOfSequence) {
					endEx = lengthOfSequence;
				}
				if (startIn >= lengthOfSequence || endEx <= 0) {
					throw new IllegalArgumentException("Your input position is not correct: ".concat(pair.toString()));
				}
				outputStrings.add(replacedString.substring(startIn, endEx));

				finalFound++;
			}

			if (seqOfInterestMap.isEmpty()) {
				break;
			}
		}

		File output4results = new File(outputFile);
		FileUtils.writeLines(output4results, outputStrings);

		outputContent("Finished process... \nSummary number of sequences need to extract is " + numOfSequences2extract
				+ ", final found is " + finalFound);
		if (!seqOfInterestMap.isEmpty()) {
			outputContent("Following sequences can not found in the fasta: ");
			for (String string : seqOfInterestMap.keySet()) {
				outputContent(string + " cannot found");
			}
		}
		
		outputContent("The total process finished.");
	}

	private void outputContent(String str) {
		if (voicm4FastaDumper != null) {
			voicm4FastaDumper.setContent4progress(str);
		} else {
			System.out.println(str);
		}
	}

}
