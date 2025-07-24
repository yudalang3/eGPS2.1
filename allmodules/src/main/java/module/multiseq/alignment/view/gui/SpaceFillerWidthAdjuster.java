package module.multiseq.alignment.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import module.multiseq.alignment.view.AlignmentViewMain;

public class SpaceFillerWidthAdjuster extends JPanel implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6233032125869718701L;

	boolean active = false;

	int oldX = 0;

	private AlignmentViewMain alignmentViewMain;

	public SpaceFillerWidthAdjuster(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewMain = alignmentViewMain;
		FontMetrics fm = getFontMetrics(alignmentViewMain.getAlignmentViewPort().getFont());
		setPreferredSize(new Dimension(100, fm.getDescent()));
		setLayout(new BorderLayout());
		setBackground(Color.white);
		// setBorder(BorderFactory.createRaisedBevelBorder());
		setBorder(new MatteBorder(0, 0, 0, 1, Color.black));
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		active = true;
		int dif = e.getX() - oldX;
		final int newWidth = getPreferredSize().width + dif;

		// System.out.println("e.getX() " + e.getX() + " oldX: " + oldX + " dif: " + dif
		// + " newWidth: " + newWidth);

		if ((newWidth > 30) || (dif > 0)) {

			setPreferredSize(new Dimension(newWidth, getPreferredSize().height));

			alignmentViewMain.repaint();
		}

		oldX = e.getX();

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override

	public void mousePressed(MouseEvent e) {
		oldX = e.getX();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		active = false;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		active = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		active = false;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (active) {
			setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
		}
	}

}
