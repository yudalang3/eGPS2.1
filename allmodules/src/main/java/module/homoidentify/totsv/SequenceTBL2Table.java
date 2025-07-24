package module.homoidentify.totsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Lists;

import analysis.AbstractAnalysisAction;

public class SequenceTBL2Table extends AbstractAnalysisAction {

	private final List<String> outputs = Lists.newArrayList();
	private double eValueCutOff = 1e-6;
	private double scoreCutOff = -1;

	@Override
	public void doIt() throws Exception {

		int sz = 10 * 1024 * 1024;
		BufferedWriter bufferedOutputStream = new BufferedWriter(new FileWriter(getOutputPath()), sz);

		bufferedOutputStream.write("target name");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("accessionTarget");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("query name");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("accessionQuery");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("full seq E-value");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("full seq score");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("full seq bias");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("best1domain E-value");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("best1domain score");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("best1domain bias");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. exp");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. reg");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. clu");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. ov");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. env");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. dom");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. rep");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. inc");bufferedOutputStream.write("\t");
		bufferedOutputStream.write("Dom. est. description\n");

		final int indexOfFullSeqEvalue = 4;
		final int indexOfFullSeqScore = 5;


		try (BufferedReader br = new BufferedReader(new FileReader(getInputPath()), sz)) {
			String readLine;
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '#') {
					continue;
				}
				// 使用 StringUtils.split 来拆分字符串，记得看它的API
				String[] parts = StringUtils.split(readLine, " ");
				double fullEvalue = Double.parseDouble(parts[indexOfFullSeqEvalue]);

				if (fullEvalue >= eValueCutOff) {
					continue;
				}

				if (scoreCutOff > -1) {
					double fullScore = Double.parseDouble(parts[indexOfFullSeqScore]);
					if (fullScore <= scoreCutOff) {
						continue;
					}
				}

				String result = StringUtils.join(parts, '\t');


				bufferedOutputStream.write(result);
				bufferedOutputStream.write("\n");
			}

		}

		bufferedOutputStream.close();


	}

	public List<String> getOutputs() {
		return outputs;
	}

	public void setCutOf(double eValueCutOff, double scoreCutOff) {
		this.eValueCutOff = eValueCutOff;
		this.scoreCutOff = scoreCutOff;

	}
}
