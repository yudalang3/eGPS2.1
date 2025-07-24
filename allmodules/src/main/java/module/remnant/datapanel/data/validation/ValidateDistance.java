package module.remnant.datapanel.data.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import egps2.utils.common.math.MatrixElementUtil;
import module.remnant.datapanel.data.DataFormat;
import module.remnant.datapanel.data.DataFormatError;
import module.webmsaoperator.webIO.DistMatrixTextInput;
import msaoperator.DataforamtInfo;

public class ValidateDistance implements FormatDetect,CommonValidate {
	
	private File inputFile;
	public ValidateDistance(File file) {
		this.inputFile = file;
	}

	@Override
	public DataforamtInfo getFileFormat() {
		try {
			new DistMatrixTextInput(inputFile).readInData();
			return judgeInputFile();
		} catch (Exception e) {
			return new DataforamtInfo(false,DataFormatError.GENETIC_DISTERROR);
		}
	}

	private DataforamtInfo judgeInputFile() throws IOException {
		int rowIndex = 0;
		
		String str = null;
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		while ((str = br.readLine()) != null) {
			str = str.trim();
			if (str.length() == 0 || str.startsWith("#")) {
				continue;
			}
			rowIndex++;
			if (rowIndex == 1) {

				String[] firstLineElements = str.split("\\s+");
				if (firstLineElements.length == 0) {
					br.close();
					return new DataforamtInfo(false,DataFormatError.GENETIC_DISTERROR);
				}
			} else {
				int i = rowIndex - 2; // when rowIndex = 2 , this is the first line
				String[] rowSplits = str.split("\\s+");
				 //System.out.println(Arrays.toString(rowSplits)+"\t"+i);

				double[] aRow = new double[i + 1];
				for (int j = 0; j <= i; j++) {

					try {
						aRow[j] = Double.parseDouble(rowSplits[j + 1]);
					} catch (NumberFormatException e) {
						br.close();
						return new DataforamtInfo(false,DataFormatError.GENETIC_DISTERROR);
					}
				}

				//System.out.println(Arrays.toString(aRow));
			}

		}
	      
		br.close();
		
		return new DataforamtInfo(true,DataFormat.GENETIC_DIST);
	}

	@Override
	public boolean detectFormat(List<String> strings) {
		String firstLine = strings.get(0);
		if (!firstLine.startsWith("    ")) {
			return false;
		}
		if (!MatrixElementUtil.isInt(firstLine.trim())) {
			return false;
		}
		
		
		if (!ifElementsFromSecondAllAreDouble(strings.get(1).split("\t"))) {
			return false;
		}
		
		return true;
	}

	private boolean ifElementsFromSecondAllAreDouble(String[] split) {
		int length = split.length;
		for (int i = 1; i < length; i++) {
			if (!MatrixElementUtil.isDoubleCompiledRegex(split[i])) {
				return false;
			}
		}
		return true;
	}
	

}
