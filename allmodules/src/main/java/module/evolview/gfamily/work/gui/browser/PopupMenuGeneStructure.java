package module.evolview.gfamily.work.gui.browser;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 当点击VirusGenomeStructure Block时,弹出Menu跳转当前Block起始区域
 *
 * @Author: mhl
 * @Date Created on: 2020-05-22 13:58
 */
public class PopupMenuGeneStructure {
    private BrowserPanel genomeMain;

    public PopupMenuGeneStructure(BrowserPanel genomeMain) {
        this.genomeMain = genomeMain;
    }

    public JPopupMenu getViewGenePopupMenu(int startPose, int endPose) {
        JPopupMenu viewGenePopupMenu = new JPopupMenu();

        JMenuItem viewGeneMenuItem = new JMenuItem("View this gene");
        //  viewGene.setToolTipText("View this gene.");
        viewGeneMenuItem.addActionListener(e -> {
            GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
            drawProperties.setDrawStart(startPose);
            drawProperties.setDrawEnd(endPose);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });

        viewGenePopupMenu.add(viewGeneMenuItem);
        return viewGenePopupMenu;
    }
}
