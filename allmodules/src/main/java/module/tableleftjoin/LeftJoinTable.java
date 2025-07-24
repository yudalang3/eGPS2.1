package module.tableleftjoin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import utils.string.EGPSStringUtil;
import analysis.AbstractAnalysisAction;
import tsv.io.KitTable;
import tsv.io.TSVReader;

public class LeftJoinTable extends AbstractAnalysisAction {

	private String refernecedTablePath = "";
	/**
	 * 0-based
	 */
	private int queryColumnIndex = 0;
	/**
	 * 1-based
	 */
	private int referenceColumnIndex = 0;
	private boolean queryFileHasHeader = false;
	private boolean refFileHasHeader = false;

	List<String> strings4output = Lists.newArrayList();

	@Override
	public void doIt() throws Exception {
		Stopwatch stopwatch = Stopwatch.createStarted();

		strings4output.clear();
		KitTable query = TSVReader.readTsvTextFile(inputPath, queryFileHasHeader);

		Map<String, Integer> querysName2lineIndex = new HashMap<>();

		List<String> originalLines = query.getOriginalLines();
		int index = 0;
		for (List<String> entry : query.getContents()) {
			String string = entry.get(queryColumnIndex);
			if (querysName2lineIndex.get(string) != null) {
				strings4output.add("Query has repeat, only keep one entry: ".concat(string));
			}
			querysName2lineIndex.put(string, index);
			index++;
		}

		HashSet<String> querySet = new HashSet<>(querysName2lineIndex.keySet());

		strings4output.add("The number of entries for querying is: " + querysName2lineIndex.size());

		List<String> outputs = new ArrayList<>();
		StringBuilder sBuilder = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(refernecedTablePath))) {
			String readLine = null;
			if (refFileHasHeader) {
				br.readLine();
			}
			while ((readLine = br.readLine()) != null) {
				String[] splits = EGPSStringUtil.split(readLine, '\t');
				String string = splits[referenceColumnIndex];
				Integer integer = querysName2lineIndex.get(string);
				if (integer != null) {
					String string2 = originalLines.get(integer);
					sBuilder.setLength(0);
					sBuilder.append(string2);
					sBuilder.append("\t");
					sBuilder.append(readLine);
					outputs.add(sBuilder.toString());
					querySet.remove(string);
				}

			}
		}

		strings4output.add("The number of obtained records is: " + outputs.size());

		if (querySet.size() > 3) {
			Iterator<String> iterator = querySet.iterator();
			StringJoiner stringJoiner = new StringJoiner(",");
			stringJoiner.add(iterator.next());
			stringJoiner.add(iterator.next());
			stringJoiner.add(iterator.next());
			strings4output.add("The query not obtained: " + stringJoiner.toString());
		} else if (querySet.size() > 0) {
			strings4output.add("The query not obtained: " + querySet.toString());
		}

		FileUtils.writeLines(new File(outputPath), outputs);

		// 停止计时并获取总耗时
		stopwatch.stop();

		strings4output.add("Take time of executing ms : " + stopwatch.elapsed().toMillis());

	}

	public String getRefernecedTablePath() {
		return refernecedTablePath;
	}

	public void setRefernecedTablePath(String refernecedTablePath) {
		this.refernecedTablePath = refernecedTablePath;
	}

	public int getQueryColumnIndex() {
		return queryColumnIndex;
	}

	public void setQueryColumnIndex(int queryColumnIndex) {
		this.queryColumnIndex = queryColumnIndex;
	}

	public int getReferenceColumnIndex() {
		return referenceColumnIndex;
	}

	public void setReferenceColumnIndex(int referenceColumnIndex) {
		this.referenceColumnIndex = referenceColumnIndex;
	}

	public boolean isQueryFileHasHeader() {
		return queryFileHasHeader;
	}

	public void setQueryFileHasHeader(boolean queryFileHasHeader) {
		this.queryFileHasHeader = queryFileHasHeader;
	}

	public boolean isRefFileHasHeader() {
		return refFileHasHeader;
	}

	public void setRefFileHasHeader(boolean refFileHasHeader) {
		this.refFileHasHeader = refFileHasHeader;
	}

	public static void main(String[] args) throws Exception {
		LeftJoinTable leftJoinTable = new LeftJoinTable();

		leftJoinTable.setInputPath(
				"C:\\Users\\yudal\\Documents\\project\\WntEvolution\\DataBase\\EnsemblDB\\Wnt_cds\\searchResults\\2.matched.genes.tsv");
		leftJoinTable.setOutputPath(
				"C:\\Users\\yudal\\Documents\\project\\WntEvolution\\DataBase\\EnsemblDB\\Wnt_cds\\searchResults\\3.matched.genes.entries.tsv");
//		leftJoinTable.setRefernecedTablePath(
//				"C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Ensembl.filterd.cdna.info.tsv");
		leftJoinTable.setRefernecedTablePath(
				"C:\\Users\\yudal\\Documents\\project\\WntEvolution\\DataBase\\EnsemblDB\\Wnt_cds\\blastDB\\subject.cds.info.tsv");
		leftJoinTable.setReferenceColumnIndex(8);
		leftJoinTable.setQueryColumnIndex(0);

		leftJoinTable.doIt();

		List<String> strings4output2 = leftJoinTable.strings4output;
		for (String string : strings4output2) {
			System.out.println(string);
		}

	}

}
