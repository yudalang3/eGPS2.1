package module.sequencelogo.makesequencelogo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * 
 * @ClassName: ReadJustSequencesFile
 * @Description: 读取输入文件格式是比对好的序列，格式如下：
 * <pre>
 * ACGG
 * AGGG
 * CGGA
 * CCTT
 * <pre>
 * @author zjw
 * @date 2024-04-28 03:26:41
 */
public class ReadJustSequencesFile {

	public static LinkedHashMap<String, String> readJustSequencesFile(File inputFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		return readJustSequencesFileWorkCode(bufferedReader);
	}

	private static LinkedHashMap<String, String> readJustSequencesFileWorkCode(BufferedReader bufferedReader)
			throws IOException {
		LinkedHashMap<String, String> sequences = new LinkedHashMap<>();
		String line;

		int i = 1;
		while ((line = bufferedReader.readLine()) != null) {
			if (!line.equals("")) {
				String upCaseString = line.toUpperCase();
				sequences.put("seq" + i, upCaseString);
				i++;
			}
		}
		bufferedReader.close();
		return sequences;
	}

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\evolgen_zhang\\Desktop\\tttt.txt");
		LinkedHashMap<String, String> sequences = readJustSequencesFile(file);
		System.out.println(sequences);
	}

}
