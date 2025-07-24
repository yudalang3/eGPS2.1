/**
 * 
 */
package module.homoidentify.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import utils.string.EGPSStringUtil;
import tsv.io.TSVReader;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

public class AnnotateGeneWithSpeciesPanel extends DIYToolSubTabModuleFace {

	private String inputTblFile;
	private String inputSpeciesInfoPath;
	private String pepInfoFile;
	private String assemblyColumn;
	private String scientificColumn;
	private String commonColumn;

	List<String> outputs = Lists.newArrayList();
	private String outputCuratedPath;

	public AnnotateGeneWithSpeciesPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("filtered.hmmer.tblout.file", "", "Input filtered hmmer results file path. Necessary");
		mapProducer.addKeyValueEntryBean("input.species.info.path", "", "Input species information file path. Necessary");
		mapProducer.addKeyValueEntryBean("species.assembly.column.name", "Ensembl Assembly",
				"Species assembly column name. Necessary");
		mapProducer.addKeyValueEntryBean("species.scientific.column.name", "Scientific name",
				"Species scientific column name. Necessary");
		mapProducer.addKeyValueEntryBean("species.common.column.name", "Common name",
				"Species common column name. Optional");
		mapProducer.addKeyValueEntryBean("input.pep.info.path", "", "Input pep information file path, produced in remnant Batch Peptides Operator. Necessary");
		
		mapProducer.addKeyValueEntryBean("output.curated.path", "", "Output file1. Necessary");
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		inputTblFile = quickParseString("filtered.hmmer.tblout.file");
		inputSpeciesInfoPath = quickParseString("input.species.info.path");
		
		assemblyColumn = quickParseString("species.assembly.column.name");
		scientificColumn = quickParseString("species.scientific.column.name");

		commonColumn = para.get("$species.common.column.name");

		pepInfoFile = quickParseString("input.pep.info.path");

		outputCuratedPath = quickParseString("output.curated.path");

		Stopwatch stopwatch = Stopwatch.createStarted();

		outputs.clear();
		perform();

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);
	}

	private List<String> perform() throws Exception {

		if (UnifiedAccessPoint.isGULaunched()) {
			UnifiedAccessPoint.getInstanceFrame().onlyRefreshButtomStatesBar(computationalModuleFace,
					"Read Hmmer Filtered Results", 100);
		}

		ArrayList<AnnotateRecord> hmmerFilteredResults = readHmmerFilteredResults();

		if (UnifiedAccessPoint.isGULaunched()) {
			UnifiedAccessPoint.getInstanceFrame().onlyRefreshButtomStatesBar(computationalModuleFace,
					"Check species info. with peptide info.", 100);
		}

		checkSpeciesInfoWithPepInfo(hmmerFilteredResults);

		if (UnifiedAccessPoint.isGULaunched()) {
			UnifiedAccessPoint.getInstanceFrame().onlyRefreshButtomStatesBar(computationalModuleFace,
					"Output the annotate records", 100);
		}

		try (BufferedWriter br = new BufferedWriter(new FileWriter(outputCuratedPath))) {
			Iterator<AnnotateRecord> iterator = hmmerFilteredResults.iterator();
			AnnotateRecord first = iterator.next();
			br.write(first.getHeader());
			br.write("\n");
			br.write(first.toString());
			br.write("\n");
			while (iterator.hasNext()) {
				AnnotateRecord annotateRecord = iterator.next();
				br.write(annotateRecord.toString());
				br.write("\n");
			}

		}


		return outputs;

	}

	private ArrayList<AnnotateRecord> readHmmerFilteredResults() throws IOException {
		ArrayList<AnnotateRecord> newArrayList = Lists.newArrayList();
		// 0: target name
		// 2: query name
		LineIterator lineIterator = FileUtils.lineIterator(new File(inputTblFile), "UTF-8");
		lineIterator.next();
		while (lineIterator.hasNext()) {
			String next = lineIterator.next();
			String[] line = EGPSStringUtil.split(next, '\t');
			String string1 = line[0];
			String[] split = EGPSStringUtil.split(string1, '|');
			String string = split[0];
			// gene: so is 5
			String geneName = string.substring(5);
			String pepName = split[1];
			String queryName = line[2];

			AnnotateRecord annotateRecord = new AnnotateRecord();
			annotateRecord.setGeneName(geneName);
			annotateRecord.setPepName(pepName);
			annotateRecord.setQueryName(queryName);
			newArrayList.add(annotateRecord);

		}
		lineIterator.close();
		return newArrayList;
	}

	private void checkSpeciesInfoWithPepInfo(List<AnnotateRecord> listOfAnnot) throws IOException {

		Map<String, AnnotateRecord> pepName2Annotate = new HashMap<>();

		/*
		 * 重要提醒：这些Record 的key pepName会有重复，所以要注意 listOfAnnot的数量会大于 pepName2Annotate的数量。
		 */
		for (AnnotateRecord annotateRecord : listOfAnnot) {
			String pepName = annotateRecord.getPepName();
			// 注意它会覆盖
			pepName2Annotate.put(pepName, annotateRecord);
		}


		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(inputSpeciesInfoPath);
		Map<String, MutablePair<String, String>> assembly2nameMap = new LinkedHashMap<>();

		List<String> list1 = asKey2ListMap.get(assemblyColumn);
		if (list1 == null) {
			throw new IllegalArgumentException(assemblyColumn + "\tcolumn name not exists in the species info. file.");
		}

		List<String> list2 = asKey2ListMap.get(scientificColumn);
		if (list2 == null) {
			throw new IllegalArgumentException(
					scientificColumn + "\tcolumn name not exists in the species info. file.");
		}

		int size = list1.size();
		for (int i = 0; i < size; i++) {
			String string1 = list1.get(i);
			String string2 = list2.get(i);
			MutablePair<String, String> of = MutablePair.of(string2, null);
			assembly2nameMap.put(string1, of);
		}

		if (commonColumn != null) {
			List<String> list3 = asKey2ListMap.get(commonColumn);
			if (list3 == null) {
				throw new IllegalArgumentException(
						commonColumn + "\tcolumn name not exists in the species information file.");
			}

			Collection<MutablePair<String, String>> values = assembly2nameMap.values();
			int index = 0;
			for (Pair<String, String> pair : values) {
				String string = list3.get(index);
				index++;
				pair.setValue(string);
			}
		}

		Set<String> sequencesNotHasAssemblyInfo = new HashSet<>();

		// geneName pepName transcript assembly chromosome start end strand
		LineIterator lineIterator = FileUtils.lineIterator(new File(pepInfoFile), "UTF-8");
		lineIterator.next();
		while (lineIterator.hasNext()) {
			String next = lineIterator.next();
			String[] line = EGPSStringUtil.split(next, '\t');
			String assemblyName = line[3];
			String pepName = line[1];
			if (assembly2nameMap.containsKey(assemblyName)) {

			} else {
				sequencesNotHasAssemblyInfo.add(assemblyName);
			}

			AnnotateRecord annotateRecord = pepName2Annotate.get(pepName);
			if (annotateRecord != null) {
				annotateRecord.setAssemblyName(assemblyName);
			}
		}


		if (!sequencesNotHasAssemblyInfo.isEmpty()) {
			outputs.add("Following assembly exists in pep info. but not species info.");
			outputs.add(sequencesNotHasAssemblyInfo.toString());
			outputs.add("Following assembly exists in species info. but not pep info.");

			Set<String> keySet = assembly2nameMap.keySet();
			Collection<String> subtract = CollectionUtils.subtract(keySet, sequencesNotHasAssemblyInfo);
			outputs.add(subtract.toString());

			setText4Console(outputs);

			throw new InputMismatchException(
					"Please check your species file and the pep information file. See console for details.");
		}

		for (AnnotateRecord annotateRecord : listOfAnnot) {
			AnnotateRecord annotateRecord2 = pepName2Annotate.get(annotateRecord.getPepName());
			String assemblyName = annotateRecord2.getAssemblyName();

			MutablePair<String, String> mutablePair = assembly2nameMap.get(assemblyName);

			Objects.requireNonNull(mutablePair, assemblyName.concat(" not found in the species info."));

			annotateRecord.setAssemblyName(assemblyName);
			annotateRecord.setScientificName(mutablePair.getKey());
			annotateRecord.setCommonName(mutablePair.getValue());
		}

		lineIterator.close();
	}

}
