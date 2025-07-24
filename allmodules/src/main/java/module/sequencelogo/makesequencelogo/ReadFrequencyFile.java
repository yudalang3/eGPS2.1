package module.sequencelogo.makesequencelogo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * 
 * @ClassName: ReadFrequencyFile
 * @Description: 读取序列的频率文件，格式如下：
 * <pre>
 * position	ElegantJTable	C	G	T
 * 1	  0.677430	  0.085656	  0.000000	  0.236914	
 * 2	  0.000000	  0.000340	  0.999660	  0.000000	
 * 3	  0.000000	  0.000000	  1.000000	  0.000000
 * 4	  0.784840	  0.040109	  0.175051	  0.000000
 * <pre>
 * @author zjw
 * @date 2024-04-28 03:30:06
 */
public class ReadFrequencyFile {

	public static LinkedHashMap<Integer, LinkedHashMap<Character, Double>> readFrequencyFile(File inputFile)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		return readFrequencyFileWorkCode(bufferedReader);
	}
	
	private static LinkedHashMap<Integer, LinkedHashMap<Character, Double>> readFrequencyFileWorkCode(BufferedReader bufferedReader) throws IOException{
		
		String line;
		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> frequenceOfBasesPerPosition = new LinkedHashMap<Integer, LinkedHashMap<Character, Double>>();

		line = bufferedReader.readLine();
		String[] bases = line.split("\t");
		while ((line = bufferedReader.readLine()) != null) {
			if (!line.equals("")) {
				String[] spliter = line.split("\t");
				LinkedHashMap<Character, Double> frequenceOfBases = new LinkedHashMap<Character, Double>();
				for (int i = 1; i < spliter.length; i++) {
					frequenceOfBases.put(Character.valueOf(bases[i].charAt(0)), Double.parseDouble(spliter[i]));
				}
				frequenceOfBasesPerPosition.put(Integer.parseInt(spliter[0]), frequenceOfBases);
			}
		}
		
		bufferedReader.close();
		return frequenceOfBasesPerPosition;
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File("E:\\task\\seqLogo\\meme-chip\\test\\motif_1-AGGARG-MEME-1.txt");
		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> frequenceOfBasesPerPosition=readFrequencyFile(file);
		System.out.println(frequenceOfBasesPerPosition);
	}
	
}
