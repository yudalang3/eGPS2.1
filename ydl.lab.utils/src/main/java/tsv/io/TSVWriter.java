package tsv.io;

import com.google.common.collect.Lists;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

public class TSVWriter {

	public static void write(Map<String, List<String>> key2ListMap, String tsvFile) throws IOException {

		List<StringBuilder> output = Lists.newArrayList();

		StringJoiner header = new StringJoiner("\t");

		Iterator<Entry<String, List<String>>> iterator = key2ListMap.entrySet().iterator();
		{
			Entry<String, List<String>> entry = iterator.next();
			String key = entry.getKey();
			header.add(key);

			List<String> value = entry.getValue();


			int size = value.size();
            for (String string : value) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string).append("\t");

                output.add(stringBuilder);
            }
		}
		while (iterator.hasNext()) {
			Entry<String, List<String>> entry = iterator.next();
			String key = entry.getKey();
			header.add(key);

			List<String> value = entry.getValue();
			int size = value.size();
			for (int i = 0; i < size; i++) {
				String string = value.get(i);

				StringBuilder stringBuilder = output.get(i);
				stringBuilder.append(string).append("\t");
			}
		}

		try (BufferedWriter br = new BufferedWriter(new FileWriter(tsvFile))) {
			br.write(header.toString());
			br.write("\n");
			for (StringBuilder sb : output) {
				sb.deleteCharAt(sb.length() - 1);
				String string = sb.toString();
				br.write(string);
				br.write("\n");
			}
		}

	}


	public static void main(String[] args) throws IOException {
		// Read the tsv file
		KitTable tsvTextFile1 = TSVReader.readTsvTextFile("input.tsv");
		KitTable tsvTextFile2 = TSVReader.readTsvTextFile("input.tsv", false);
		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap("input.tsv");

		// Write the tsv file
		TSVWriter.write(asKey2ListMap, "output.tsv");
		// Or you can directly write
		try (BufferedWriter br = new BufferedWriter(new FileWriter("output.tsv"))) {
			br.write("");
		}
	}

}
