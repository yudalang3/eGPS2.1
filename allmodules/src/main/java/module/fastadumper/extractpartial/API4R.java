package module.fastadumper.extractpartial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class API4R {

	public String[] fastadumper_partialMatch(String fastaPath, String[] entries, String outPath, boolean keepSearch)
			throws Exception {


		List<String> searchStrings = new ArrayList<>(Arrays.asList(entries));

		ContinuousFastaExtractor continuousFastaExtractor = new ContinuousFastaExtractor();
		continuousFastaExtractor.setInputPath(fastaPath);
		continuousFastaExtractor.setTargetStringSets(searchStrings);

		continuousFastaExtractor.setKeepAllSearchStrings(keepSearch);

		continuousFastaExtractor.setOutputPath(outPath);

		continuousFastaExtractor.doIt();

		List<String> outputList4console = continuousFastaExtractor.outputList4console;
//		for (String string : outputList4console) {
//			System.out.println(string);
//		}

		return outputList4console.toArray(new String[outputList4console.size()]);
	}

}
