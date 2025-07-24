package module.symbol2id;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import utils.string.EGPSStringUtil;
import analysis.AbstractAnalysisAction;
import tsv.io.KitTable;
import tsv.io.TSVReader;

public class TranslateCalculator extends AbstractAnalysisAction {

	private String needToTranslateFilePath;
	List<String> strings4output = Lists.newArrayList();
	private int indexOfGeneSymbol = 0;

	@Override
	public void doIt() throws Exception {

		List<String> output = Lists.newArrayList();
		// 读取 tsv文件
		List<String> lines = FileUtils.readLines(new File(inputPath), StandardCharsets.UTF_8);
		Iterator<String> iterator = lines.iterator();
		String[] headers = EGPSStringUtil.split(iterator.next(), '\t');
		Map<String, String> hashMap = new HashMap<>();
		int totalEntry = 0;
		while (iterator.hasNext()) {
			totalEntry++;
			String next = iterator.next();
			String[] line = EGPSStringUtil.split(next, '\t');
			// index 8和10 是哈希的 key; 18 是value
			String hgncID = line[0];
			String geneSymbol = line[1];
			String alias_symbol = line[8];
			String prev_symbol = line[10];
			String entrezID = line[18];
			String ensembl_gene_id = line[19];
//			if (alias_symbol.indexOf('|') > -1) {
//				String[] temps = EGPSStringUtil.split(next, '|');
//				for (String string : temps) {
//					hashMap.put(string, entrezID);
//				}
//			} else {
//				hashMap.put(alias_symbol, entrezID);
//			}
//			hashMap.put(prev_symbol, entrezID);

//			if (hashMap.get(geneSymbol) != null) {
//				System.out.println(geneSymbol + "\t deplicated.");
//			}
//			hashMap.put(geneSymbol, entrezID);
			if (hashMap.get(hgncID) != null) {
				if (hgncID.isEmpty()) {
					System.out.println(hgncID + "\t deplicated.\t" + geneSymbol);
				} else {
					output.add(hgncID + "\t deplicated.\t" + geneSymbol);
				}

			}
			hashMap.put(hgncID, geneSymbol);
		}

		// 假如要查询的基因symbol为
		List<String> asList = Arrays.asList("A1BG", "A1CF");
		for (String string : asList) {
			String string2 = hashMap.get(string);
			// do some thing here
		}

		for (String string : output) {
			System.err.println(string);
		}

		System.out.println(hashMap.size() + "\ttotal is: " + totalEntry);

	}

	public void symbol2entrezID() throws IOException {
		if (Strings.isNullOrEmpty(needToTranslateFilePath)) {
			throw new IllegalArgumentException("Please set the file path for translation.");
		}

		if (Strings.isNullOrEmpty(inputPath)) {
			throw new IllegalArgumentException(
					"Please set the file path for reference, download from HGNC in tsv format.");
		}
		if (Strings.isNullOrEmpty(outputPath)) {
			throw new IllegalArgumentException("Please set the file path for output.");
		}

		KitTable tsvTextFile = TSVReader.readTsvTextFile(inputPath);
		List<List<String>> contents = tsvTextFile.getContents();
		Map<String, String> hashMap = new HashMap<>();
		for (List<String> list : contents) {
			String hgncID = list.get(0);
			String geneSymbol = list.get(1);
			String alias_symbol = list.get(8);
			String prev_symbol = list.get(10);
			String entrezID = list.get(18);
			String ensembl_gene_id = list.get(19);

			hashMap.put(geneSymbol, entrezID);

			if (alias_symbol.indexOf('|') > -1) {
				String[] temps = EGPSStringUtil.split(alias_symbol, '|');
				for (String string : temps) {
					if (hashMap.get(string) == null) {
						hashMap.put(string, entrezID);
					}
				}
			} else {
				if (hashMap.get(alias_symbol) == null) {
					hashMap.put(alias_symbol, entrezID);
				}
			}

			if (!prev_symbol.isEmpty() && hashMap.get(prev_symbol) != null) {
				// System.out.println(prev_symbol + "\t deplicated.");
			} else {
				hashMap.put(prev_symbol, entrezID);
			}

		}

		//
		int correctTranslated = 0;
		int failedTOTranslated = 0;
		tsvTextFile = TSVReader.readTsvTextFile(needToTranslateFilePath, true);
		contents = tsvTextFile.getContents();
		String outputFilePath = outputPath;
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFilePath));
		bufferedWriter.write("geneSymbol\tentrezID\n");
		for (List<String> list : contents) {
			String string = list.get(indexOfGeneSymbol);
			String targetStr = hashMap.get(string);
			if (targetStr == null) {
				targetStr = "NA";
				failedTOTranslated++;
			} else {
				correctTranslated++;
			}
			bufferedWriter.write(string);
			bufferedWriter.write("\t");
			bufferedWriter.write(targetStr);
			bufferedWriter.write("\n");
		}

		bufferedWriter.close();

		strings4output.clear();
		strings4output.add("Total need to translated: " + contents.size());
		strings4output.add("Failed to translated: " + failedTOTranslated);
		strings4output.add("Correct to translated: " + correctTranslated);
	}

	public void setNeedToTranslateFilePath(String needToTranslateFilePath) {
		this.needToTranslateFilePath = needToTranslateFilePath;
	}

	public void setIndexOfGeneSymbol(int indexOfGeneSymbol) {
		this.indexOfGeneSymbol = indexOfGeneSymbol;
	}

	public static void main(String[] args) throws Exception {
		String path = "C:\\Users\\yudal\\Documents\\project\\geneNamesGetter\\hgnc_complete_set.txt";
		TranslateCalculator runner = new TranslateCalculator();
		runner.setInputPath(path);
		runner.setOutputPath("C:\\Users\\yudal\\Documents\\project\\SYF_scRNA\\rds\\name.after.trans.tsv");
		runner.setNeedToTranslateFilePath(
				"C:\\Users\\yudal\\Documents\\project\\SYF_scRNA\\rds\\name.needTo.trans.tsv");
		runner.symbol2entrezID();

	}
}
