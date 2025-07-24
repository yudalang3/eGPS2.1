package module.evolview.gfamily.work.gui.browser;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimers;

/**
 * 引物短带跳转
 *
 * @Author: mhl
 */
public class PopupMenuPrimers {
    private BrowserPanel genomeMain;

    public PopupMenuPrimers(BrowserPanel genomeMain) {
        this.genomeMain = genomeMain;
    }

    public JPopupMenu getViewGenePopupMenu(DrawingPropertyPrimers primer) {
        JPopupMenu viewGenePopupMenu = new JPopupMenu();
        int fStart = primer.getFStart();
        int fEnd = primer.getFEnd();
        int rStart = primer.getRStart();
        int rEnd = primer.getREnd();
        JMenuItem viewAmplifiedFragmentMenuItem = new JMenuItem("View amplified fragment");
        viewAmplifiedFragmentMenuItem.addActionListener(e -> {
            GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
            drawProperties.setDrawStart(fStart);
            drawProperties.setDrawEnd(rEnd + 1);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });

        viewGenePopupMenu.add(viewAmplifiedFragmentMenuItem);

        JMenuItem viewForwardPrimerMenuItem = new JMenuItem("View forward primer");
        viewForwardPrimerMenuItem.addActionListener(e -> {
            GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
            drawProperties.setDrawStart(fStart);
            drawProperties.setDrawEnd(fEnd + 1);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
        viewGenePopupMenu.add(viewForwardPrimerMenuItem);
        JMenuItem viewReversePrimerMenuItem = new JMenuItem("View reverse primer");
        viewReversePrimerMenuItem.addActionListener(e -> {
            GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
            drawProperties.setDrawStart(rStart);
            drawProperties.setDrawEnd(rEnd + 1);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
        viewGenePopupMenu.add(viewReversePrimerMenuItem);


        JMenuItem copyForwardPrimerMenuItem = new JMenuItem("Copy forward primer");
        copyForwardPrimerMenuItem.addActionListener(e -> {
            String forward = primer.getForward();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss = new StringSelection(forward);
            clipboard.setContents(ss, null);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
        viewGenePopupMenu.add(copyForwardPrimerMenuItem);

        JMenuItem copyReversePrimerMenuItem = new JMenuItem("Copy reverse primer");
        copyReversePrimerMenuItem.addActionListener(e -> {
            String reverse = primer.getReverse();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss = new StringSelection(reverse);
            clipboard.setContents(ss, null);

            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
        viewGenePopupMenu.add(copyReversePrimerMenuItem);


        return viewGenePopupMenu;
    }
}
