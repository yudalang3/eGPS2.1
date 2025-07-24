package module.chorddiagram;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.swing.JScrollPane;

import org.apache.commons.io.FileUtils;

import egps2.utils.common.util.SaveUtil;
import egps2.frame.ModuleFace;
import module.chorddiagram.work.PaintingPanel;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class MainFace extends ModuleFace{
	
	private PaintingPanel paintingPanel;
	private VoiceImporter4ChordDiagram voiceImporter4ChordDiagram;

	MainFace(IModuleLoader moduleLoader) {
		super(moduleLoader);
		
		paintingPanel = new PaintingPanel();

		voiceImporter4ChordDiagram = new VoiceImporter4ChordDiagram(this);

		JScrollPane jScrollPane = new JScrollPane(paintingPanel);
		add(jScrollPane, BorderLayout.CENTER);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public void changeToThisTab() {
		
	}


	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		voiceImporter4ChordDiagram.doUserImportAction();
		
	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		new SaveUtil().saveData(paintingPanel, getClass());
		
	}

	@Override
	public void initializeGraphics() {
		importData();
		
	}

	@Override
	public String[] getFeatureNames() {
		return null;
	}

	public void loadData(String file1, String file2, int numOfColumn) throws IOException {
		List<String> strings1 = FileUtils.readLines(new File(file1), StandardCharsets.UTF_8);
		List<String> strings2 = FileUtils.readLines(new File(file2), StandardCharsets.UTF_8);
		paintingPanel.getPlotDataModel().parseData(strings1, strings2, numOfColumn);
		paintingPanel.repaint();
	}

}
