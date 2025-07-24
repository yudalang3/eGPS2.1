package module.multiseq.alignment.view.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import msaoperator.io.seqFormat.MSAFormatSuffixName;
import module.multiseq.alignment.DataFileSuffixNames;

public class SaveFilterFasta extends FileFilter implements MSAFormatSuffixName{
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String s = f.getName();

		return s.endsWith(".fas") || s.endsWith(".FASTA");
	}

	@Override
	public String getDescription() {
		return "FASTA (*.fas)";
	}

	@Override
	public String getDefaultFormatName() {
		return DataFileSuffixNames.FASTA_DEFAULT_SUFFIX;
	}

}
