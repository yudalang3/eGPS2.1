package module.sequencelogo.extractMEMEinformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @ClassName: ProcessMEMEChipOutfile
 * @Description: 将MEMEChip输出的.meme文件处理成每个motif一个频率矩阵文件
 * @author zjw
 * @date 2024-04-28 10:23:33
 */
public class ProcessMEMEChipOutfile {

	public static void processMEMEChipOutfile(String outputFloder, String inputFilename, String tempOutFolder,
			String tempOutMotifHeader) {

		String alphabet = "";
		String separator = System.getProperty("file.separator");
		try {
			List<String> inputFileLines = FileUtils.readLines(new File(inputFilename));
			for (String line : inputFileLines) {
				if (line.startsWith("ALPHABET")) {
					alphabet = line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String nucleotides = alphabet.substring(alphabet.indexOf('=') + 1).trim();
		// 每一个motif频率文件的第一行
		StringJoiner firstLineOfFile = new StringJoiner("\t");
		firstLineOfFile.add("position");
		for (int i = 0; i < nucleotides.length(); i++) {
			char c = nucleotides.charAt(i);
			String s = String.valueOf(c);
			firstLineOfFile.add(s);
		}

		LinkedHashMap<String, List<String>> motifMap = getMotifMatrices(inputFilename, tempOutMotifHeader);
		String outputFolderPath = outputFloder + tempOutFolder;
		File folder = new File(outputFolderPath);
		if (folder.exists() && folder.isDirectory()) {
			deleteFolder(folder);
		}
		folder.mkdirs();

		// 生成每一个motif的频率文件
		for (Entry<String, List<String>> entry : motifMap.entrySet()) {
			String oneMotifOutputPath = outputFloder + tempOutFolder + separator + entry.getKey() + ".txt";
			entry.getValue().add(0, firstLineOfFile.toString());
			try {
				FileUtils.writeLines(new File(oneMotifOutputPath), entry.getValue());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @MethodName: getMotifMatrices
	 * @Description: 得到每一个motif的频率矩阵
	 * @author zjw
	 * @param inputFilename
	 * @return LinkedHashMap<String,List<String>>
	 * @date 2024-04-28 02:59:34
	 */
	private static LinkedHashMap<String, List<String>> getMotifMatrices(String inputFilename,
			String tempOutMotifHeader) {

		LinkedHashMap<String, List<String>> motifMap = new LinkedHashMap<>();
		Pattern pattern = Pattern.compile("w=\\s*(\\d+)");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
			String line;
			while ((line = reader.readLine()) != null) {

				if (line.startsWith("MOTIF")) {
					// 得到motif的名字
					String motifName = line.split("\\s+")[2];

					// 得到motif的序号
					int motifNumber = Integer.parseInt(line.split("\\s+")[1]);

					line = reader.readLine();
					line = reader.readLine();
					int seqLength = 0;
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						String wValue = matcher.group(1);
						// 得到motif的长度
						seqLength = Integer.parseInt(wValue);
					}

					List<String> motifMatrices = new ArrayList<>();
					for (int i = 0; i < seqLength; i++) {
						line = reader.readLine();
						line.trim();
						StringBuilder lineOfFrequency = new StringBuilder();
						lineOfFrequency.append(i + 1).append("\t");
						lineOfFrequency.append(line);
						motifMatrices.add(lineOfFrequency.toString());
					}

					StringBuilder modifiedMotifName = new StringBuilder();
					modifiedMotifName.append(tempOutMotifHeader);
					modifiedMotifName.append(motifNumber).append("-");
					modifiedMotifName.append(motifName);
					motifMap.put(modifiedMotifName.toString(), motifMatrices);
				}
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return motifMap;
	}

	/**
	 * 
	 * @MethodName: deleteFolder
	 * @Description: 删除文件夹以及文件夹下的所有文件
	 * @author zjw
	 * @param folder void
	 * @date 2024-04-28 10:21:52
	 */
	public static void deleteFolder(File folder) {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					deleteFolder(file);
				}
			}
		}
		folder.delete();
	}

	public static void main(String args[]) {
		String inputFilePath = "C:\\Users\\evolgen_zhang\\Desktop\\test\\combined.meme";
		String outputFloderString = "C:\\Users\\evolgen_zhang\\Desktop\\test\\";
		String tempOutFolder = "processed_output";
		String tempOutMotifHeader = "motif_";
		processMEMEChipOutfile(outputFloderString, inputFilePath, tempOutFolder, tempOutMotifHeader);
	}
}
