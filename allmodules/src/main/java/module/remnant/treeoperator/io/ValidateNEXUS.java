package module.remnant.treeoperator.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import module.remnant.datapanel.data.DataFormat;
import module.remnant.datapanel.data.DataFormatError;
import msaoperator.DataforamtInfo;
import module.remnant.datapanel.data.validation.FormatDetect;


public class ValidateNEXUS implements FormatDetect{
	private File inputFile;

	public ValidateNEXUS(File inputFile) {
		this.inputFile = inputFile;
	}

	@Override
	public DataforamtInfo getFileFormat() {
		try {
			return judgeInputFile();
		} catch (Exception e) {
			return new DataforamtInfo(false,DataFormatError.NEXUS_FILE_ERROR);
		}
	}

	private DataforamtInfo judgeInputFile() throws IOException {
		boolean ifTree = false;
		boolean ifSeqs = false;
		
		BufferedReader bufr = new BufferedReader(new FileReader(inputFile));
		String line = bufr.readLine();
		line = line.trim();
		if (! line.equalsIgnoreCase("#NEXUS")) {
			bufr.close();
			return new DataforamtInfo(false,DataFormatError.NEXUS_FILE_ERROR);
		}
		
		int lineNum = 1;
		while ((line = bufr.readLine()) != null) {
			
			line = line.trim();
			if (line.startsWith("begin tree")) {
				ifTree = true;
			}
			
			if (line.startsWith("begin data")) {
				ifSeqs = true;
			}
			
			if (lineNum == 200) {
				break;
			}
			lineNum ++;
		}
		
		bufr.close();
		
		if (ifTree) {
			if (ifSeqs) {
				return new DataforamtInfo(true,DataFormat.NEXUS_BOTH);
			}else {
				return new DataforamtInfo(true,DataFormat.NEXUS_TREE);
			}
		}else {
			if (ifSeqs) {
				return new DataforamtInfo(true,DataFormat.NEXUS_SEQ);
			}else {
				bufr.close();
				return new DataforamtInfo(false,DataFormatError.NEXUS_FILE_CONTENTERROR);
			}
		}
		
		
	}

	@Override
	public boolean detectFormat(List<String> strings) {
		if (strings.get(0).startsWith("#NEXUS")) {
			return true;
		}
		return false;
	}

}




