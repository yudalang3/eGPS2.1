package module.multiseq.alignment.view.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import msaoperator.io.seqFormat.MSAFormatSuffixName;
import module.multiseq.alignment.DataFileSuffixNames;

public class SaveFilterPhylip extends FileFilter implements MSAFormatSuffixName{
	

	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String s = f.getName();

		return s.endsWith(".nex") || s.endsWith(".NEX");
	}

	@Override
	public String getDescription() {
		return "NEXUS (*.nex)";
	}

	@Override
	public String getDefaultFormatName() {
		return DataFileSuffixNames.NEXUS_DEFAULT_SUFFIX;
	}

}
