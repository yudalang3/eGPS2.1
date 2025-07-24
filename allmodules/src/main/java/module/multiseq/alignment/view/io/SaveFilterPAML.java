package module.multiseq.alignment.view.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import msaoperator.io.seqFormat.MSAFormatSuffixName;
import module.multiseq.alignment.DataFileSuffixNames;

public class SaveFilterPAML extends FileFilter implements MSAFormatSuffixName{
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String s = f.getName();

		return s.endsWith(".nuc") || s.endsWith(".NUC");
	}

	@Override
	public String getDescription() {
		return "PAML (*.nuc)";
	}

	@Override
	public String getDefaultFormatName() {
		return DataFileSuffixNames.PAML_DEFAULT_SUFFIX;
	}

}
