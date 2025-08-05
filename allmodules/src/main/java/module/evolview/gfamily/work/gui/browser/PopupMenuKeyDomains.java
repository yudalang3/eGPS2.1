package module.evolview.gfamily.work.gui.browser;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import egps2.panels.dialog.SwingDialog;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.model.AlignmentGetSequence;
import msaoperator.alignment.sequence.SequenceI;

/**
 * 当点击ElementStructure Block时,弹出Menu,操作对应区域数据
 *
 * @Author: mhl
 */
public class PopupMenuKeyDomains {
    private BrowserPanel genomeMain;
    public static final String STARTWITH = "PF";
	private JPopupMenu viewGenePopupMenu;

    public PopupMenuKeyDomains(BrowserPanel genomeMain) {
        this.genomeMain = genomeMain;
    }

    public JPopupMenu getViewGenePopupMenu(DrawingPropertyElement genomeElementModel) {
        if (viewGenePopupMenu != null) {
			return viewGenePopupMenu;
		}
        
        viewGenePopupMenu = new JPopupMenu();
        
        JMenuItem viewPosMenuItem = new JMenuItem("Display information");
        viewPosMenuItem .addActionListener(e -> {
        	StringBuilder sb = new StringBuilder();
        	sb.append("Name: ").append(genomeElementModel.getName()).append("\n");
        	sb.append("Gene: ").append(genomeElementModel.getGene()).append("\n");
        	sb.append("Start position: ").append(genomeElementModel.getStartPosition()).append("\n");
        	sb.append("End position: ").append(genomeElementModel.getEndPositoin()).append("\n");
        	
        	JTextArea jTextArea = new JTextArea(sb.toString());
           SwingDialog.QuickWrapperJCompWithDialog(jTextArea, "Information");
        });
        viewGenePopupMenu.add(viewPosMenuItem);
        
        
        JMenuItem copyMenuItem = new JMenuItem("Copy amino acid seq");
        copyMenuItem.addActionListener(e -> {
            int start = genomeElementModel.getStartPosition();
            int end = genomeElementModel.getEndPositoin();
            
            //
            Optional<AlignmentGetSequence> alignmentSequence = genomeMain.getGeneData().getAlignmentSequence();
            if (!alignmentSequence.isPresent()) {
				throw new InputMismatchException("Not the needed");
			}
			AlignmentGetSequence sequence = alignmentSequence.get();

            List<SequenceI> proteinSeqs = sequence.returnDNASequence(start - 1, end);
            if (proteinSeqs == null || proteinSeqs.size() == 0) {
                return;
            }

            SequenceI sequenceI = proteinSeqs.get(0);
            //TODO 要注意 remove gap
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String vc = sequenceI.getSeqAsString();
            StringSelection ss = new StringSelection(vc);
            clipboard.setContents(ss, null);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
        viewGenePopupMenu.add(copyMenuItem);

        String id = genomeElementModel.getName();
        JMenuItem jumpMenuItem = new JMenuItem(id);
        jumpMenuItem.addActionListener(e -> {
//            try {
//                browse(genomeElementModel.getWebsite());
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
//        viewGenePopupMenu.add(jumpMenuItem);


        JMenuItem domainMenuItem = new JMenuItem("View this domain");
        domainMenuItem.addActionListener(e -> {
            GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
            int start = genomeElementModel.getStartPosition();
            int end = genomeElementModel.getEndPositoin();
            drawProperties.setDrawStart(start);
            drawProperties.setDrawEnd(end + 1);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
        viewGenePopupMenu.add(domainMenuItem);
        

        return viewGenePopupMenu;
    }


}
