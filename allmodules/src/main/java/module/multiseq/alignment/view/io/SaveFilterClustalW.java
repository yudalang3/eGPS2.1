package module.multiseq.alignment.view.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import msaoperator.io.seqFormat.MSAFormatSuffixName;
import module.multiseq.alignment.DataFileSuffixNames;

public class SaveFilterClustalW extends FileFilter implements MSAFormatSuffixName{
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String s = f.getName();

		return s.endsWith(".aln") || s.endsWith(".ALN");
	}

	@Override
	public String getDescription() {
		return "CLUSTALW (*.aln)";
	}

	@Override
	public String getDefaultFormatName() {
		return DataFileSuffixNames.CLUSTALW_DEFAULT_SUFFIX;
	}

}
