package module.multiseq.alignment.view.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

import egps2.frame.gui.EGPSMainGuiUtil;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

@SuppressWarnings("serial")
public class ViewAreaSequenceNameJPanel extends JPanel implements MouseListener, MouseMotionListener {


	private SequenceDataForAViewer sequenceData;

	private VisulizationDataProperty alignmentViewPort;

	private SequenceJShap shape;

	private boolean toolTipOn = true;

	private AlignmentViewMain alignmentViewMain;


	public ViewAreaSequenceNameJPanel(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewMain = alignmentViewMain;
		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();
		this.sequenceData = alignmentViewPort.getSequenceData();

		shape = new SequenceNameJColumn2D(this);
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public VisulizationDataProperty getAlignmentViewPort() {
		return alignmentViewPort;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (sequenceData == null) {
			return;
		}
		int height = getHeight();
		int width = getWidth();
		if ((height == 0) || (width == 0)) {
			return; // If not need to show, return
		}
		Graphics2D g2 = (Graphics2D) g;
		
		EGPSMainGuiUtil.setupHighQualityRendering(g2);
		g2.setFont(alignmentViewPort.getFont());
		g2.setColor(alignmentViewPort.getColor());
		draw(g2, alignmentViewPort.getStartSeq(), alignmentViewPort.getEndSeq(), alignmentViewPort.getStartRes(),
				alignmentViewPort.getEndRes());
		
		g2.dispose();
	}

	private void draw(Graphics2D g2, int startSeq, int endSeq, int startRes, int endRes) {
		int yOffset = 0;

		List<SequenceI> dataSequences = sequenceData.getPaintSequences();

		for (SequenceI sequenceI : dataSequences) {
			if (sequenceI == null) {
				continue;
			}
			yOffset += alignmentViewPort.getCharHeight();
			drawSequenceName(g2, sequenceI, yOffset);
		}

		if (shape != null) {
			shape.repaint();
		}
	}

	private void drawSequenceName(Graphics2D g2, SequenceI sequence, int yOffset) {

		String seqName = sequence.getSeqName();

		if (seqName == null) {
			return; // fix for racecondition
		}

		g2.drawString(seqName, 10, yOffset);

	}

	@Override
	public boolean contains(int x, int y) {
		boolean inShape = false;
		if (toolTipOn) {
			if (shape == null) {
				setToolTipText(null);
			} else {
				synchronized (shape) {
					if (shape.contains(x, y)) {
						setToolTipText(shape.getTipText());
					} else {
						setToolTipText(null);
					}
				}
			}
		} else {
			setToolTipText(null);
		}
		return super.contains(x, y);
	}

	/**
	 * Tures on or off the tooltip.
	 * 
	 * @param option - true if the value information is shown in tooltip. false if
	 *               no tooltip.
	 */
	public void setToolTipOn(boolean option) {
		toolTipOn = option;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() == 2) {
			alignmentViewPort.getSelectionElements().clear();
			alignmentViewMain.repaint();
			return;
		}

		Point point = e.getPoint();

		shape.assignValuesIfMousePointMeetShap(alignmentViewPort.getSelectionElements(), point.x, point.y);

		alignmentViewMain.repaint();

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (shape.contains(e.getPoint().getX(), e.getPoint().getY())) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

	}

}
