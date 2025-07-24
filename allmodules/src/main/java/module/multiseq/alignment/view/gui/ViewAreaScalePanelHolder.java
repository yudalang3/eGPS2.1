package module.multiseq.alignment.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import egps2.frame.gui.EGPSMainGuiUtil;
import module.multiseq.alignment.view.AlignmentViewMain;

public class ViewAreaScalePanelHolder extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3563111061656433822L;
	private VisulizationDataProperty alignmentViewPort;
	private AlignmentViewMain alignmentViewMain;

	private SequenceJShap shape;

	public ViewAreaScalePanelHolder(AlignmentViewMain alignmentViewMain) {

		this.alignmentViewMain = alignmentViewMain;

		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();

		shape = new ScalePanelHolder2D(this);

		FontMetrics fm = getFontMetrics(alignmentViewPort.getFont());

		setPreferredSize(new Dimension(10, fm.getDescent()));
		setLayout(new BorderLayout());

		addMouseListener(this);

		addMouseMotionListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		EGPSMainGuiUtil.setupHighQualityRendering(g2);
		int width = getWidth();

		int height = getHeight();

		g2.setFont(alignmentViewPort.getFont());

		g2.setColor(Color.white);

		g2.fillRect(0, 0, width, height);
		if (alignmentViewPort.getColor() == null) {
			g2.setColor(Color.black);

		} else {
			g2.setColor(alignmentViewPort.getColor());

		}

		int startRes = alignmentViewPort.getStartRes();

		int endRes = alignmentViewPort.getEndRes();

		int charWidth = alignmentViewPort.getCharWidth();

		int charHeight = alignmentViewPort.getCharHeight();
		FontMetrics fm = getFontMetrics(alignmentViewPort.getFont());
		int yOf = fm.getDescent();
		for (int i = startRes; i < endRes; i++) {
			int x = (i - startRes) * charWidth;
			int y = charHeight - yOf;
			int posOffset = (i + 1);
			if (posOffset % 10 == 0) {
				g2.drawString(String.valueOf(posOffset), x, y);

				g2.drawLine(x + charWidth / 2, y + 2, x + charWidth / 2, y + yOf * 2);
			} else if (posOffset % 5 == 0) {
				g2.drawLine(x + charWidth / 2, y + yOf, x + charWidth / 2, y + yOf * 2);
			}

		}
		setPreferredSize(new Dimension(width, yOf + charHeight));
		if (shape != null) {
			shape.repaint();
		}
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			alignmentViewPort.clearUserSelections();
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

	public VisulizationDataProperty getAlignmentViewPort() {
		return alignmentViewPort;
	}

}
