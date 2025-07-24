package module.heatmap.eheatmap.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class OpenFilterTree extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
			return true;
		} else {
			if (f.getName().endsWith(".nex") || f.getName().endsWith(".nexus") || f.getName().endsWith(".nhx") 
					|| f.getName().endsWith(".nwk") || f.getName().endsWith(".tree") || f.getName().endsWith(".tre")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Tree format (*.nex,*.nexus,*.nhx,*.nwk,*.tree,*.tre)";
	}
}