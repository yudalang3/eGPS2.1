/**
 * 
 */
package module.benchensdownloader.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;

import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class SpeciesCognitionPanel extends DIYToolSubTabModuleFace {

	public SpeciesCognitionPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("URL", "https://ftp.ensembl.org/pub/release-112/fasta/",
				"The entry must be start with the protocal, supported is http/https/ftp/file/ws\n#For local file, must be file: protocal\n# wrong:  C:/a/c/v/a.txt\n# correct: file:C:/a/c/v/a.txt");
		mapProducer.addKeyValueEntryBean("output.file", "",
				"Current RegExp string is\n\"<td><a href=\\\"(.*?)/\\\">(.*?)/</a></td>\"\n# The output file path to store the results.");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {

		String inputURLPath = null;
		String outputFilePath = null;

		String string = para.get("$URL");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the URL file path.");
		} else {
			inputURLPath = string;
		}

		string = para.get("$output.file");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the output file path.");
		} else {
			outputFilePath = string;
		}

		run(inputURLPath, outputFilePath);
	}

	private void run(String inputURLPath, String outputFilePath) throws Exception {
		List<String> outputList = new LinkedList<>();

		// 要访问的URL
		URL url = new URL(inputURLPath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

		// 正则表达式匹配文件夹链接
		Pattern dirPattern = Pattern.compile("<td><a href=\"(.*?)/\">(.*?)/</a></td>");
		String line;

		int count = 0;
		// 读取每一行，寻找文件夹链接
		while ((line = reader.readLine()) != null) {
			Matcher matcher = dirPattern.matcher(line);
			if (!matcher.find()) {
				continue;
			}

			String dirName = matcher.group(1);
			String speciesName = matcher.group(2);
			if (dirName.isEmpty()) {
				continue;
			}
			outputList.add(inputURLPath + dirName + "\t" + speciesName);
			count++;

		}

		reader.close();

		FileUtils.writeLines(new File(outputFilePath), outputList);

		setText4Console(Arrays.asList("Found " + count + " counts."));
	}

}
