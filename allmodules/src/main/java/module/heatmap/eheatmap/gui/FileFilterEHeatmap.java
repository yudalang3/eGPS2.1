package module.heatmap.eheatmap.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileFilterEHeatmap extends FileFilter {
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String s = f.getName();

		return s.endsWith(".eheatmap") || s.endsWith(".EHEATMAP");
	}

	@Override
	public String getDescription() {
		return "EHEATMAP (*.eheatmap)";
	}

}
