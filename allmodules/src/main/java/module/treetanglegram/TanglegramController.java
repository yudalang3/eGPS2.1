package module.treetanglegram;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.JPanel;

import egps2.utils.common.util.SaveUtil;
import egps2.Authors;

public class TanglegramController {
	
	private boolean savable = false;
	
	private final TanglegramMain main;

	public TanglegramController(TanglegramMain tanglegramMain) {
		main = tanglegramMain;
	}

	public String[] getTeamAndAuthors() {
		String[] info = new String[3];

		info[0] = "EvolGen";
		info[1] = Authors.YUDALANG + ","+ Authors.LIHAIPENG;
		info[2] = "http://www.picb.ac.cn/evolgen/";
		return info;
	}

	public boolean isSaveable() {
		return savable;
	}

	public void saveViewPanelAs() {
		new SaveUtil().saveData(getMain().getRightJPanel());
	}


	public void loadTab(File nwk1, File nwk2, Font nameFont , String outgroup) {
		
		Dimension paintingPanelDim = main.getPaintingPanelDim();
		JPanel loadTab = null;
		try {
			loadTab = new PaintingPanelPreparer().loadTab(nwk1, nwk2, nameFont , paintingPanelDim, outgroup);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		main.setRightJPanel(loadTab);
		
		savable = true;
	}
	
	
	public TanglegramMain getMain() {
		return main;
	}


}
