package module.tsvtools;

import java.io.BufferedReader;
import java.io.FileReader;

import utils.string.EGPSStringUtil;

public class FirstColumnRenamer {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: $egps2 edu.sinh.beauty.unisoft.remnant.tsvtools.FirstColumnRenamer input.file 1");
			System.err.println("1 is the field to keep, the default separator is space key");
			return;
		}

		String inputFile = args[0];
		int int1 = Integer.parseInt(args[1]) - 1;

		StringBuilder stringBuilder = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				String[] splits = EGPSStringUtil.split(readLine, '\t');
				String firstCol = splits[0];
				String[] splitsOfFirst = EGPSStringUtil.split(firstCol, ' ');
				splits[0] = splitsOfFirst[int1];

				stringBuilder.setLength(0);

				for (String string : splits) {
					stringBuilder.append(string);
					stringBuilder.append("\t");
				}
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				String string = stringBuilder.toString();
				System.out.println(string);

			}
		}
	}

}
