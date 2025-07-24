package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPaintBar;

public class MouseListenerAlleleFreq extends AbstractMouseListener {

	public MouseListenerAlleleFreq(BrowserPanel genomeMain) {
		super(genomeMain);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		TrackAlleleFreq genomeBarPlot = genomeMain.getGenomeBarPlot();
		AbstractTrackTooltiper shape = genomeBarPlot.getShape();
		Point point = e.getPoint();
		if (shape.contains(point.getX(), point.getY())) {
			genomeMain.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			genomeMain.setCursor(Cursor.getDefaultCursor());
		}

		if (moveBlankArea(point, genomeBarPlot)) {
			genomeBarPlot.setHighlightShow(true);
		} else {
			genomeBarPlot.setHighlightShow(false);
		}

		genomeMain.updateUI();
	}

	@Override
	public void mouseExited(MouseEvent e) {

		genomeMain.getGenomeBarPlot().setHighlightShow(false);

		genomeMain.updateUI();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		TrackAlleleFreq genomeBarPlot = genomeMain.getGenomeBarPlot();
		setTrack(genomeBarPlot);
		super.mouseClicked(e);

		Point point = e.getPoint();

		DrawingPropertyPaintBar genomePaintBar = genomeBarPlot.getPaintBar();
		if (genomePaintBar == null) {
			return;
		}
		GeneFamilyController controller = genomeMain.getController();

		TooltiperAlleleFreq shape = (TooltiperAlleleFreq) genomeBarPlot.getShape();

		if (shape.contains(point.getX(), point.getY())) {
			int pos = shape.getPos();
			//Ncov2019GenomeDrawProperties drawProperties = genomeBarPlot.getDrawProperties();
			//int selectPostion = drawProperties.getSelectPostion() == pos ? -1 : pos;
			
			controller.letPhyloTreeHighlightNodesAndLines(pos);
//			controller.getLeftColorSchemePanel().letCustomizedRadioButtonBeSelected();
			// 现在改成了单向操作，点击后只会染色，然后将染色状态改成自定义。不需要再点一下恢复染色，所以这里去掉了选中位置设置。
//			drawProperties.setSelectPostion(selectPostion);
		}

//        List<Ncov2019GenomePaintLine> paintLines = genomePaintBar.getNcov2019GenomePaintLine();
//        for (Ncov2019GenomePaintLine paintLine : paintLines) {
//            Double rect = paintLine.getRect();
//            if (rect.contains(point.getX(), point.getY())) {
//                int pos = paintLine.getPos();
//                Ncov2019GenomeDrawProperties drawProperties = genomeBarPlot.getDrawProperties();
//                int selectPostion = drawProperties.getSelectPostion() == pos ? -1 : pos;
//                controller.letPhyloTreeHighlightNodesAndLines(selectPostion);
//                drawProperties.setSelectPostion(selectPostion);
//                break;
//            }
//        }

		if ((e.getX() > LocationCalculator.BLINK_LEFT_SPACE_LENGTH)
				& (e.getX() < (genomeMain.getGenomeBarPlot().getWidth() - LocationCalculator.BLINK_RIGHT_SPACE_LENGTH))) {
			if (SwingUtilities.isRightMouseButton(e)) {
				Component component = e.getComponent();
				JPopupMenu popup = new JPopupMenu();
				popup.add(getViewPosJMenuItem());
				popup.show(component, e.getX(), e.getY());
			}
			genomeMain.updateUI();

		}
	}
}
