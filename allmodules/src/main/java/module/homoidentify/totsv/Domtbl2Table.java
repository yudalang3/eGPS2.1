package module.homoidentify.totsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import analysis.AbstractAnalysisAction;

public class Domtbl2Table extends AbstractAnalysisAction {

	@Override
	public void doIt() throws Exception {

		int sz = 10 * 1024 * 1024;
		BufferedWriter bufferedOutputStream = new BufferedWriter(new FileWriter(getOutputPath()), sz);

		bufferedOutputStream.write(
				"target name	accession	tlen	query name	accession	qlen	E-value	full score	full bias	#	of	c-Evalue	i-Evalue	dom score	dom bias	hmm coord from	hmm coord to	ali coord from	ali coord to	env coord from	env coord to	acc\n");

		int numValue = 23 - 1;
		try (BufferedReader br = new BufferedReader(new FileReader(getInputPath()), sz)) {
			String readLine = null;
			int number = 0;
			while ((readLine = br.readLine()) != null) {
				number++;
				if (readLine.charAt(0) == '#') {
					continue;
				}
				// 使用 StringUtils.split 来拆分字符串，记得看它的API
				String[] parts = StringUtils.split(readLine, " ");

				bufferedOutputStream.write(parts[0]);
				for (int i = 1; i < numValue; i++) {
					bufferedOutputStream.write("\t");
					bufferedOutputStream.write(parts[i]);
				}
				bufferedOutputStream.write("\n");
			}

		}

		bufferedOutputStream.close();

	}

}
