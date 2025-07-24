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
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import msaoperator.alignment.sequence.SequenceComponentRatio;

public class ViewAreaSequenceAnnotationJPanel extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6986851970359905589L;

	private VisulizationDataProperty alignmentViewPort;

	private SequenceDataForAViewer sequenceData;

	private SequenceJShap shape;
	private List<SequenceComponentRatio> ratios;
	/**
	 * If true, the value information of plot will show via tooltip. true is
	 * default.
	 */
	protected boolean toolTipOn = true;


	private AlignmentViewMain alignmentViewMain;

	public List<SequenceComponentRatio> getRatios() {

		return ratios;

	}

	public ViewAreaSequenceAnnotationJPanel(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewMain = alignmentViewMain;
		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();
		this.sequenceData = alignmentViewPort.getSequenceData();
		setBackground(Color.white);
		shape = new SequenceAnnotation2D(this);
		addMouseListener(this);
		addMouseMotionListener(this);

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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		EGPSMainGuiUtil.setupHighQualityRendering(g2);
		
		g2.setFont(alignmentViewPort.getFont());
		g2.setColor(alignmentViewPort.getColor());
		g2.setBackground(Color.white);
		int height = getHeight();

		int charWidth = alignmentViewPort.getCharWidth();
		int charHeight = alignmentViewPort.getCharHeight();

		int tempHeight = height - charHeight;

		int size = alignmentViewPort.getTotalSequenceCount();

		g2.drawLine(0, tempHeight, charWidth * alignmentViewPort.getLength(), tempHeight);

		ratios = sequenceData.getRatio();
		int index = 0;
		for (SequenceComponentRatio sequenceRatio : ratios) {
			Character base = sequenceRatio.getBase();
			Integer maxValue = sequenceRatio.getMaxValue();
			int x = charWidth * index;
			if (base == null || maxValue == null) {
				g2.fillRect(x, tempHeight, charWidth, 0);
				g2.drawString("-", x, height);
				index++;
				continue;
			}
			int newHeight = tempHeight * maxValue / size;

			int y = tempHeight - newHeight;

			g2.fillRect(x, y, charWidth, newHeight);

			g2.drawString(base.toString(), x, height);

			index++;

		}

		if (shape != null) {

			shape.repaint();
		}
		
		g2.dispose();
	}

	public VisulizationDataProperty getAlignmentViewPort() {

		return alignmentViewPort;
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
