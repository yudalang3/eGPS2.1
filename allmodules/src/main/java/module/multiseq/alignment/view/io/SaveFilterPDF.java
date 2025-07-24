package module.multiseq.alignment.view.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import msaoperator.io.seqFormat.MSAFormatSuffixName;

public abstract class SaveFilterPDF extends FileFilter implements MSAFormatSuffixName {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String s = f.getName();

		return s.endsWith(".pdf") || s.endsWith(".PDF");
	}

	@Override
	public String getDefaultFormatName() {
		return "pdf";
	}

	public abstract SaveFilterPDFDescription getDefaultFormatDescription();

}
