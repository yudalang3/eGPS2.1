package module.multiseqview.view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JPanel;

import egps2.frame.gui.EGPSMainGuiUtil;
import module.multiseqview.SequenceStructureViewController;
import module.multiseqview.model.GraphicsBean;
import module.multiseqview.model.paint.AlongSequenceDirection;
import module.multiseqview.model.paint.CrossSequenceDirectionPaintObj;
import module.multiseqview.model.paint.PaintingData;
import module.multiseqview.model.paint.RectPaintObj;

@SuppressWarnings("serial")
public class PaintingPanel extends JPanel {

	final int totalNumberOfPoints = 100;
	final int interval = 10;

	int roundRectArcSize = 8;
	private final GeneralPath generalPath = new GeneralPath();

	PaintingData paintingData;

	private SequenceStructureViewController controller;
	private int currentWidth;
	private int currentHeight;

	public PaintingPanel(SequenceStructureViewController sequenceStructureViewController) {
		Objects.requireNonNull(sequenceStructureViewController);

		this.controller = sequenceStructureViewController;

		setBackground(Color.white);
		PaintingListener listener = new PaintingListener(sequenceStructureViewController, this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		EGPSMainGuiUtil.setupHighQualityRendering(g2d);

		if (paintingData == null) {
			controller.recalculateAndRefresh();
			EGPSMainGuiUtil.drawLetUserImportDataString(g2d);
			return;
		}

		if (currentWidth < getWidth() || currentHeight < getHeight()) {
			controller.recalculateAndRefresh();
			return;
		}

		drawGraphics(g2d);

		int xx = 160;
		int yy = currentHeight - 30;

//		Color lightgray = Color.lightGray;
//		g2d.setColor(new Color(lightgray.getRed(),lightgray.getGreen(), lightgray.getBlue(), 100));
//		g2d.fillRoundRect(xx - 10, yy - 18, 120, 25, 10, 10);
//		
//		g2d.setColor(Color.black);
//		g2d.drawString("Show control panel", xx, yy);

		g2d.dispose();
	}

	private void drawGraphics(Graphics2D g2d) {
		FontMetrics fontMetrics = g2d.getFontMetrics();

		List<AlongSequenceDirection> alongSequenceDirections = paintingData.getAlongSequenceDirections();

		final int drawStringX = 23;
		GraphicsBean graphicsBean = controller.getGraphicsBean();

		for (AlongSequenceDirection alongSequenceDirection : alongSequenceDirections) {
			int x = alongSequenceDirection.getX();
			int y = alongSequenceDirection.getY();
			int width = alongSequenceDirection.getWidth();
			int height = alongSequenceDirection.getHeight();
			int yy = y + height / 2;

			g2d.setColor(Color.black);
			g2d.drawLine(x, yy, x + width, yy);
			g2d.setFont(graphicsBean.getSequenceFont());
			g2d.drawString(alongSequenceDirection.getName(), drawStringX, yy);

			List<RectPaintObj> listRectPaintObjs = alongSequenceDirection.getListRectPaintObjs();

			g2d.setFont(graphicsBean.getNameFont());
			for (RectPaintObj rectPaintObj : listRectPaintObjs) {
				double x2 = rectPaintObj.getX();
				double width2 = rectPaintObj.getWidth();

				g2d.setColor(rectPaintObj.getColor());
//				g2d.fillRoundRect((int) x2, y, (int) width2, height, roundRectArcSize, roundRectArcSize);
				g2d.fill3DRect((int) x2, y, (int) width2, height, true);

				g2d.setColor(Color.black);
				g2d.drawRoundRect((int) x2, y, (int) width2, height, roundRectArcSize, roundRectArcSize);

				float x3 = (float) (x2 + 0.5 * width2 - 0.5 * fontMetrics.stringWidth(rectPaintObj.getName()));
//				float y2 = (float) (y + (height + fontMetrics.getAscent()) / 2 );
				float y2 = (float) (y - 6);
				g2d.drawString(rectPaintObj.getName(), x3, y2);
			}

			List<CrossSequenceDirectionPaintObj> crossObjs = alongSequenceDirection
					.getListcCrossSequenceDirectionPaintObjs();

			for (CrossSequenceDirectionPaintObj crossObj : crossObjs) {

				// drawCrossLinkedRegionsWithLines(g2d,crossObj);
				drawCrossLinkedRegionsWithCurves(g2d, crossObj);

			}
		}

	}

	private void drawCrossLinkedRegionsWithCurves(Graphics2D g2d, CrossSequenceDirectionPaintObj crossObj) {
		GraphicsBean graphicsBean = controller.getGraphicsBean();

		double wanQv = graphicsBean.getCurveRatio() * (crossObj.yP3 - crossObj.yP2);

		generalPath.reset();
		generalPath.moveTo(crossObj.xP1, crossObj.yP1);
		generalPath.lineTo(crossObj.xP2, crossObj.yP2);

		generalPath.curveTo(crossObj.xP2, crossObj.yP2 + wanQv, crossObj.xP3, crossObj.yP3 - wanQv, crossObj.xP3,
				crossObj.yP3);
		generalPath.lineTo(crossObj.xP4, crossObj.yP4);

		generalPath.curveTo(crossObj.xP4, crossObj.yP4 - wanQv, crossObj.xP1, crossObj.yP1 + wanQv, crossObj.xP1,
				crossObj.yP1);

		generalPath.closePath();

		g2d.setColor(graphicsBean.getCrossLinkedRegion());
		g2d.fill(generalPath);

	}

	private void drawCrossLinkedRegionsWithLines(Graphics2D g2d, CrossSequenceDirectionPaintObj crossObj) {
		generalPath.reset();
		generalPath.moveTo(crossObj.xP1, crossObj.yP1);
		generalPath.lineTo(crossObj.xP2, crossObj.yP2);
		generalPath.lineTo(crossObj.xP3, crossObj.yP3);
		generalPath.lineTo(crossObj.xP4, crossObj.yP4);
		generalPath.closePath();

		g2d.setColor(Color.lightGray);
		g2d.fill(generalPath);
//		g2d.draw(generalPath);

	}

	public void setPaintingData(PaintingData paintingData, int width, int height) {
		this.paintingData = paintingData;
		this.currentWidth = width;
		this.currentHeight = height;
	}

	public Optional<PaintingData> getPaintingData() {
		return Optional.ofNullable(paintingData);
	}
}
