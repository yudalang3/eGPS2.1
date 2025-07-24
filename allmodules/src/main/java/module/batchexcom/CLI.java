package module.batchexcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.google.common.collect.Lists;

import utils.EGPSUtil;
import tsv.io.TSVReader;

/**
 * batch execute the commands 输入一个脚本，{} 内的内容可以批量替换。
 */
public class CLI {

	public static void main(String[] args) {
		Options options = new Options();

		Option queryFasta = Option.builder("s").longOpt("script").desc("The script to running, like mv {a} {b}")
				.hasArg()
				.required().build();

		Option genomeFasta = Option.builder("para").longOpt("parametersFile").desc("Set the parameters file path")
				.hasArg()
				.required().build();


		options.addOption(queryFasta);
		options.addOption(genomeFasta);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);

			String ori_scripts = cmd.getOptionValue("script");
			System.out.println("The original script is:");
			System.out.println(ori_scripts);
			String parametersPath = cmd.getOptionValue("parametersFile");
			Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(parametersPath);
			
			ArrayList<Entry<String, List<String>>> newArrayList = Lists.newArrayList(asKey2ListMap.entrySet());
			List<String> firstValues = newArrayList.get(0).getValue();
			int size = firstValues.size();
			for (int i = 0; i < size; i++) {
				String scripts = ori_scripts;
				for (Entry<String, List<String>> entry : newArrayList) {
					String key = entry.getKey();
					String myKey = "{" + key + "}";
					
					String string = entry.getValue().get(i);

					scripts = scripts.replace(myKey, string);
				}
				System.out.println("The script after substituted is:");
				System.out.println(scripts);
				runOnece(scripts);

			}
			


		} catch (Exception e) {
			System.err.println(e.getMessage());
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			formatter.printHelp(cliUtilityName, options);
		}
	}

	private static void runOnece(String script) {

		// 在 Windows 上，可以将命令更改为 "cmd.exe", "/c", "dir"
		List<String> commands;
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			commands = Arrays.asList("cmd.exe", "/c", script);
		} else {
			commands = Arrays.asList("/bin/sh", "-c", script);
		}

		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		processBuilder.redirectErrorStream(true); // 合并标准输出和错误输出
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			int exitValue = process.waitFor();
			if (exitValue != 0) {
				System.err.println("Error happened.");
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
