package module.multiseq.alignment.view.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JPanel;

import egps2.frame.gui.EGPSMainGuiUtil;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.model.AlignmentDrawProperties;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

public class ViewAreaSequenceJPanel extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4732487659571963212L;

	/**
	 * If true, the value information of plot will show via tooltip. true is
	 * default.
	 */
	private boolean toolTipOn = true;

	protected SequenceDataForAViewer sequenceData;// 所有Sequcence信息集合

	protected VisulizationDataProperty alignmentViewPort;// 界面属性获取

	private SequenceJShap shape;


	private AlignmentViewMain alignmentViewMain;

	private Point2D dragStart = null;

	private AlignmentDrawProperties alignmentDrawProperties;

	private Rectangle2D dragRect;

	public ViewAreaSequenceJPanel(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewMain = alignmentViewMain;

		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();

		this.alignmentDrawProperties = alignmentViewMain.getAlignmentDrawProperties();

		this.sequenceData = alignmentViewPort.getSequenceData();

		this.shape = new SequenceJColumn2D(this);

		setBackground(Color.white);

		addMouseListener(this);

		addMouseMotionListener(this);

	}

	public VisulizationDataProperty getAlignmentViewPort() {

		return alignmentViewPort;
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
						//System.out.println(shape.getTipText());
						// setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					} else {
						setToolTipText(null);
						// setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}
		} else {
			setToolTipText(null);
		}
		return super.contains(x, y);
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

		int charWidth = alignmentViewPort.getCharWidth();

		int charHeight = alignmentViewPort.getCharHeight();

		draw(g2, charWidth, charHeight);

		/**
		 * The implementation schema:
		 * 
		 * <pre>
		 * 
		 *  （displayedStartRes, displayedStartSeq）
		 *       |--------------------------------------------------|
		 *       |                                                  |
		 *       |             column number                        |
		 *       |       (x,y) |----------|                         |
		 *       |             |          |                         |
		 *       |             |          |  row number             |
		 *       |             |          |                         |
		 *       |             |          |                         |
		 *       |             |----------|                         |
		 *       |                                                  |
		 *       |                                                  |
		 *       |                                                  |
		 *       |                                                  |
		 *       |--------------------------------------------------|
		 * </pre>
		 */
		
		List<UserSelectedViewElement> selectionElements = alignmentViewPort.getSelectionElements();
		for (UserSelectedViewElement selectionElement : selectionElements) {
			int displayedStartRes = alignmentViewPort.getStartRes();
			int displayedStartSeq = alignmentViewPort.getStartSeq();
			int xPos = (selectionElement.getxPos() - displayedStartRes) * charWidth;
			int yPos = (selectionElement.getyPos() - displayedStartSeq) * charHeight;
			int selectWidth = selectionElement.getSelectRowNum() * charWidth;
			int selectHeight = selectionElement.getSelectColumnNum() * charHeight;
			g2.setColor(Color.red);
			g2.drawRect(xPos, yPos, selectWidth, selectHeight);
		}
		
		
		if (shape != null) {
			shape.repaint();
		}
		g2.dispose();
	}

	protected void draw(Graphics2D g2, int charWidth, int charHeight) {
		int yOffset = 0;

		List<SequenceI> dataSequences = sequenceData.getPaintSequences();

		for (SequenceI sequence : dataSequences) {

			yOffset += charHeight;

			alignmentDrawProperties.getSequenceBackgroundColor().drawSequence(g2, sequence, sequenceData, 0, yOffset,
					charWidth, charHeight);
		}

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

		if (!e.isControlDown()) {
			alignmentViewPort.getSelectionElements().clear();
		}
		Point point = e.getPoint();

		shape.assignValuesIfMousePointMeetShap(alignmentViewPort.getSelectionElements(), point.x, point.y);

		alignmentViewMain.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

		dragStart = new Point2D.Double(e.getPoint().getX(), e.getPoint().getY());

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("mouseReleased...");

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// System.out.println("mouseEntered...");
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragStart == null) {// ensure the dragStart be assigned !
			return;
		}
		double x1 = Math.min(dragStart.getX(), e.getPoint().getX());
		double y1 = Math.min(dragStart.getY(), e.getPoint().getY());
		double x2 = Math.max(dragStart.getX(), e.getPoint().getX());
		double y2 = Math.max(dragStart.getY(), e.getPoint().getY());

		dragRect = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);

		List<UserSelectedViewElement> selectionElements = alignmentViewPort.getSelectionElements();
		//if (!e.isControlDown()) {
			selectionElements.clear();
		//}
		
		shape.assignValuesIfMousePointMeetShaps(selectionElements, dragRect);

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (shape.contains(e.getPoint().getX(), e.getPoint().getY())) {
			setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
