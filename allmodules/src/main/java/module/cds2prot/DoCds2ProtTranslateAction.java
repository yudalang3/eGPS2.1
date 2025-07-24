package module.cds2prot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import utils.EGPSFileUtil;
import geneticcodes.GeneticCode;
import geneticcodes.IGeneticCode;
import fasta.io.FastaReader;

public class DoCds2ProtTranslateAction {

	String inputFilePath;
	String geneticodeName = IGeneticCode.GeneticCodeTableNames[0];

	private List<String> outputList = new ArrayList<>();

	public List<String> getOutputList() {
		return outputList;
	}

	public void doIt() throws Exception {

		outputList.clear();

		GeneticCode gCondes = GeneticCode.geneticCodeFactory(geneticodeName);

		String outputPath = EGPSFileUtil.appendAdditionalStr2path(inputFilePath, "nn2aa.prot");
		BufferedWriter bufferedOutputStream = new BufferedWriter(new FileWriter(outputPath));

		LinkedHashMap<String, String> fastaDNASequence = FastaReader.readFastaDNASequence(new File(inputFilePath));

		for (Entry<String, String> entry : fastaDNASequence.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();

			String pep = gCondes.translateNotConsiderOther(value);

			bufferedOutputStream.write(">");
			bufferedOutputStream.write(name);
			bufferedOutputStream.write("\n");
			bufferedOutputStream.write(pep);
			bufferedOutputStream.write("\n");

		}

		bufferedOutputStream.close();

		outputList.add("Successfully translated.");
		outputList.add("Output location is: ".concat(outputPath));

	}


}
