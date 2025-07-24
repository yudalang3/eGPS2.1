package module.evolview.gfamily.work.gui.browser;

import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.calculator.browser.LocationCalculator;

/**
 * 隐藏/显示track
 *
 * @Author: mhl
 */
public class HideTrackPopupMenu {
	private BrowserPanel genomeMain;

	public HideTrackPopupMenu(BrowserPanel genomeMain) {
		this.genomeMain = genomeMain;

	}

	/**
	 * 隐藏/显示指定track
	 *
	 * @Author: mhl
	 */

	public JPopupMenu setTrackHidePopupMenu(AbstractTrackPanel track) {
		JPopupMenu viewGenePopupMenu = new JPopupMenu();
		JMenuItem viewAmplifiedFragmentMenuItem = new JMenuItem("View amplified fragment");
		viewAmplifiedFragmentMenuItem.addActionListener(e -> {
			track.setVisible(!track.isVisible());
			refreshGUI();
		});
		viewGenePopupMenu.add(viewAmplifiedFragmentMenuItem);
		return viewGenePopupMenu;
	}

	/**
	 * 显示或隐藏track
	 *
	 * @Author: mhl
	 */
	public JPopupMenu setTracksHidePopupMenu() {
		JPopupMenu viewGenePopupMenu = new JPopupMenu();
		JMenuItem structureMenuItem = new JMenuItem(LocationCalculator.GENOMESTRUCTURE);
		structureMenuItem.addActionListener(e -> {
			TrackGeneStructure track = genomeMain.getVirusGeomeStructure();
			track.setVisible(!track.isVisible());
			refreshGUI();
		});
		viewGenePopupMenu.add(structureMenuItem);

		JMenuItem elementtructureMenuItem = new JMenuItem(LocationCalculator.KEYDOMAINS);
		elementtructureMenuItem.addActionListener(e -> {
			TrackKeyDomains track = genomeMain.getElementStructure();
			track.setVisible(!track.isVisible());
			refreshGUI();
		});
		viewGenePopupMenu.add(elementtructureMenuItem);

		JMenuItem barPlotMenuItem = new JMenuItem(LocationCalculator.SNPFREQUENCY);
		barPlotMenuItem.addActionListener(e -> {
			TrackAlleleFreq track = genomeMain.getGenomeBarPlot();
			track.setVisible(!track.isVisible());
			refreshGUI();
		});
		viewGenePopupMenu.add(barPlotMenuItem);

		JMenuItem seqIdentityMenuItem = new JMenuItem(LocationCalculator.COVSEQIDENTITY);
		seqIdentityMenuItem.addActionListener(e -> {
			TrackSeqSimilarity track = genomeMain.getGenomePolylinePlot();
			track.setVisible(!track.isVisible());
			refreshGUI();
		});
		viewGenePopupMenu.add(seqIdentityMenuItem);

		JMenuItem alignmentMenuItem = new JMenuItem(LocationCalculator.COVALIGNMENT);
		alignmentMenuItem.addActionListener(e -> {
			TrackAlignment track = genomeMain.getGenomerAlignment();
			track.setVisible(!track.isVisible());
			refreshGUI();
		});
		viewGenePopupMenu.add(alignmentMenuItem);
		JMenuItem primerMenuItem = new JMenuItem(LocationCalculator.PRIMERSETS);
		primerMenuItem.addActionListener(e -> {
			TrackPrimers track = genomeMain.getGenomePrimers();
			track.setVisible(!track.isVisible());
			refreshGUI();
		});
		viewGenePopupMenu.add(primerMenuItem);
		viewGenePopupMenu.addSeparator();
		JMenuItem viewAllMenuItem = new JMenuItem("Show all");
		viewAllMenuItem.addActionListener(e -> {
			List<AbstractTrackPanel> tracks = genomeMain.getTracks();
			for (AbstractTrackPanel track : tracks) {
				track.setVisible(true);
			}
			refreshGUI();
		});
		viewGenePopupMenu.add(viewAllMenuItem);
		viewGenePopupMenu.addSeparator();
		JMenuItem refreshMutationFreq = new JMenuItem("Refresh mutation freq");
		// refreshMutationFreq.setIcon(SarsCov2Icons.get("Export.png"));
		refreshMutationFreq.setToolTipText("Refresh the mutation freq tarck in the genome browser.");
		refreshMutationFreq.addActionListener(e -> {
			GeneFamilyController controller = genomeMain.getController();
//			TreePopupMenu treePopupMenu = controller.getTreeLayoutProperties().getTreePopupMenu();
//			treePopupMenu.refreshMutationFrequency();

		});
		viewGenePopupMenu.add(refreshMutationFreq);

		return viewGenePopupMenu;
	}

	private void refreshGUI() {
		SwingUtilities.invokeLater(() -> {
			BrowserPanel selectedBrowserPanel = genomeMain;
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			drawProperties.setReCalculator(true);
			
			genomeMain.updateUI();
		});
	}
}
