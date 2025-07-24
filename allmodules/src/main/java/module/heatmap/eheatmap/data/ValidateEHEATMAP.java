package module.heatmap.eheatmap.data;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ValidateEHEATMAP {
	private File inputFile;

	private boolean isSuccess;
	private String errorString;

	public ValidateEHEATMAP(File file) {
		this.inputFile = file;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public void getFileFormat() {

		boolean ifFirstLine = true;
		int numOfLineElements = 0;
		try {
			List<String> readLines = FileUtils.readLines(inputFile);
			if (readLines.size() < 2) {
				isSuccess = false;
				errorString = new String("The content is less than 2 lines, please check your format.");
				return;
			}

			for (String line : readLines) {
				line = line.trim();

				if (line.startsWith("###")) {
					break;
				}

				if (line.equals("") || line.startsWith("#")) {
					continue;
				}

				String[] split = line.split("\\s+|\\||,");
				if (ifFirstLine) {
					numOfLineElements = split.length;
					if (numOfLineElements < 2) {
						isSuccess = false;
						errorString = "The number of line elements less than 2, please check your format";
						return;
					}
					ifFirstLine = false;
				} else {
					if (numOfLineElements != split.length) {
						isSuccess = false;
						errorString = "The number of line elements not consist with header line.";

						return;
					}

					for (int i = 1; i < numOfLineElements; i++) {
						String ss = split[i];
						if (ss.equalsIgnoreCase("na")) {
							continue;
						} else {
							Double.parseDouble(ss);
						}
					}
				}
			}

		} catch (Exception e) {
			isSuccess = false;
			errorString = e.getMessage();
			return;
		}
		isSuccess = true;
	}

	public boolean detectFormat(List<String> strings) {
		if (strings.get(0).startsWith("##fileformat=eGPS heatmap")) {
			return true;
		}
		return false;
	}
}
