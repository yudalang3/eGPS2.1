package module.scountmerger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import utils.string.EGPSStringUtil;
import analysis.AbstractAnalysisAction;

public class MergeCountFiles extends AbstractAnalysisAction {

	String fileSuffix = null;
	boolean shouldExportTPM = true;

	List<String> output4console = Lists.newArrayList();

	@Override
	public void doIt() throws Exception {

		output4console.clear();
		File file = new File(inputPath);
		if (!file.isDirectory()) {
			throw new InputMismatchException("The input path is not a directory: ".concat(inputPath));
		}
		File[] listFiles = file.listFiles();
		Arrays.sort(listFiles);

//		列	名称
//		1	GeneID
//		2	Chr
//		3	Start
//		4	End
//		5	Strand
//		6	Length
//		7	SampleID
		final int INDEX_SampleID = 6;
		final int INDEX_LENGTH = 5;
		final int INDEX_GENEID = 0;
		List<Integer> lengthList = null;
		List<String> geneIDList = null;
		List<String> sampleNames = Lists.newArrayList();
		List<List<Integer>> sampleCountsList = Lists.newArrayList();
		for (File inputFile : listFiles) {
			String fileName = inputFile.getName();
			if (!fileName.endsWith(fileSuffix)) {
				continue;
			}

			String substringBefore = StringUtils.substringBefore(fileName, fileSuffix);
			sampleNames.add(substringBefore);

			List<String> lines = FileUtils.readLines(inputFile, StandardCharsets.UTF_8);

			List<Integer> sampleGeneCounts = Lists.newArrayList();
			Iterator<String> iterator = lines.iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				if (next.charAt(0) == '#') {
					iterator.remove();
					continue;
				}

				String[] splits = EGPSStringUtil.split(next, '\t');
				if (next.startsWith("Geneid")) {
					iterator.remove();
					continue;
				}

				Integer valueOf = Integer.valueOf(splits[INDEX_SampleID]);
				sampleGeneCounts.add(valueOf);

			}
			sampleCountsList.add(sampleGeneCounts);

			output4console.add(fileName + " total line number: " + lines.size());

			if (lengthList == null) {
				lengthList = Lists.newArrayList();
				geneIDList = Lists.newArrayList();
				for (String line : lines) {
					String[] splits = EGPSStringUtil.split(line, '\t');
					lengthList.add(Integer.parseInt(splits[INDEX_LENGTH]));
					geneIDList.add(splits[INDEX_GENEID]);
				}
			}

		}

		// output to file

		File outFile = new File(outputPath);
		if (outFile.isDirectory()) {
			throw new InputMismatchException("You need to input a file path not a directory.");
		}

		int sampleSize = sampleCountsList.size();
		long[] totalMappedReadsArray = new long[sampleSize];

		try (BufferedWriter br = new BufferedWriter(new FileWriter(outFile))) {
			br.write("GeneID");
			br.write("\tLength");
			for (String sampleNames2 : sampleNames) {
				br.write("\t");
				br.write(sampleNames2);
			}
			br.write('\n');
			//
			int size = geneIDList.size();
			for (int i = 0; i < size; i++) {
				String geneID = geneIDList.get(i);
				Integer length = lengthList.get(i);

				br.write(geneID);
				br.write('\t');
				br.write(length.toString());

				int index = 0;
				for (List<Integer> sampleCount : sampleCountsList) {
					Integer expr = sampleCount.get(i);
					totalMappedReadsArray[index] += expr;

					br.write('\t');
					br.write(expr.toString());
					index++;
				}
				br.write('\n');
			}
		}

		// TPM
		if (!shouldExportTPM) {
			return;
		}

//		### 计算 TPM 公式
//		 #TPM (Transcripts Per Kilobase Million)  每千个碱基的转录每百万映射读取的Transcripts
//		  counts2TPM <- function(count=count, efflength=efflen){
//		    RPK <- count/(efflength/1000)   #每千碱基reads (Reads Per Kilobase) 长度标准化
//		    PMSC_rpk <- sum(RPK)/1e6        #RPK的每百万缩放因子 (“per million” scaling factor ) 深度标准化
//		    RPK/PMSC_rpk              
//		  }  
		output4console.add("TotalMappedReads: " + Arrays.toString(totalMappedReadsArray));
		int size = geneIDList.size();

		// total mapped results 不是原来的mapped results，而是现在的
//		for (int i = 0; i < sampleSize; i++) {
//			totalPMSC_rpkArray[i] = totalMappedReadsArray[i] / (double) 1e6;
//		}
		for (int i = 0; i < sampleSize; i++) {
			totalMappedReadsArray[i] = 0;
		}
		for (int i = 0; i < size; i++) {
			Integer length = lengthList.get(i);
			double effLength = length / 1000.0;
			int index = 0;
			for (List<Integer> sampleCount : sampleCountsList) {
				Integer expr = sampleCount.get(i);

				double RPK = expr / effLength;
				totalMappedReadsArray[index] += RPK;
				index++;
			}

		}

		output4console
				.add("TotalMappedReads After scalling with effect length: " + Arrays.toString(totalMappedReadsArray));
		double[] totalPMSC_rpkArray = new double[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			totalPMSC_rpkArray[i] = totalMappedReadsArray[i] / (double) 1e6;
		}
		output4console.add("per million scaling factor :  " + Arrays.toString(totalPMSC_rpkArray));

		try (BufferedWriter br = new BufferedWriter(new FileWriter(outputPath.concat("matrix.tpm.tsv")))) {
			br.write("GeneID");
			for (String sampleNames2 : sampleNames) {
				br.write("\t");
				br.write(sampleNames2);
			}
			br.write('\n');
			//

			for (int i = 0; i < size; i++) {
				String geneID = geneIDList.get(i);
				Integer length = lengthList.get(i);
				double effLength = length / 1000.0;
				br.write(geneID);

				int index = 0;
				for (List<Integer> sampleCount : sampleCountsList) {
					Integer expr = sampleCount.get(i);

					double RPK = expr / effLength;
					double norValue = RPK / totalPMSC_rpkArray[index];

					br.write('\t');
					br.write(String.valueOf(norValue));
					index++;
				}
				br.write('\n');
			}
		}
	}

}
