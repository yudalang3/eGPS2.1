package module.pill.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GraphicProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	List<Double> pnodesOval = new ArrayList<>();
	List<RoundRectangle2D.Double> pnodesRect = new ArrayList<>();
	List<Path2D> pnodesCustom = new ArrayList<>();

	List<Double> pmembraneOval = new ArrayList<>();
	List<RoundRectangle2D.Double> pmembraneRect = new ArrayList<>();

	List<Double> pnucleusOval = new ArrayList<>();
	List<RoundRectangle2D.Double> pnucleusRect = new ArrayList<>();
	List<Line2D.Double> pnucleusDNA = new ArrayList<>();
	Line2D.Double currentCleusDNA = new Line2D.Double();

	List<Line2D.Double> pfreeArrows = new ArrayList<>();
	Line2D.Double currentArrow = new Line2D.Double();

	Double currentNucleusOval = new Double();
	Double currentMembraneOval = new Double();
	Double currentNodesOval = new Double();

	RoundRectangle2D.Double currentNucleusRect = new RoundRectangle2D.Double();
	RoundRectangle2D.Double currentMembraneRect = new RoundRectangle2D.Double();
	RoundRectangle2D.Double currentNodesRect = new RoundRectangle2D.Double();
	public Path2D currentNodesCustom = new Path2D.Double();
	AffineTransform nodeCustomAT = AffineTransform.getTranslateInstance(0, 0);

	public static final int DEFAULT_SIZE = 30;
	int pnodeHalfWidth = DEFAULT_SIZE;
	int pnodeHalfHeight = DEFAULT_SIZE;

	// https://www.zhihu.com/question/26744174
	public static Color pnodeColorOri = Color.decode("#4363d8");
	public static Color pnodeColor = new Color(pnodeColorOri.getRed(), pnodeColorOri.getGreen(), pnodeColorOri.getBlue(), 230);
	public static Color pmembraneColor = Color.decode("#469990");
	public static Color pnuclearColor = Color.decode("#f58231");
	public static Color pnuclearDNAColor = Color.decode("#e6194B");
	public static Color parrowColor = Color.black;

	private static final BasicStroke thickStokeLine = new BasicStroke(2.0f);

	public void clearAll() {
		pnodesOval.clear();
		pnodesRect.clear();
		pmembraneOval.clear();
		pmembraneRect.clear();
		pnucleusOval.clear();
		pnucleusRect.clear();
		pfreeArrows.clear();
		pnucleusDNA.clear();
		pnodesCustom.clear();
	}

	public boolean hasGraphicsElements() {
		if (!pnodesOval.isEmpty()) {
			return true;
		}
		if (!pnodesRect.isEmpty()) {
			return true;
		}
		if (!pmembraneOval.isEmpty()) {
			return true;
		}
		if (!pmembraneRect.isEmpty()) {
			return true;
		}
		if (!pnucleusOval.isEmpty()) {
			return true;
		}
		if (!pnucleusRect.isEmpty()) {
			return true;
		}
		if (!pfreeArrows.isEmpty()) {
			return true;
		}

		if (!pnucleusDNA.isEmpty()) {
			return true;
		}
		if (!pnodesCustom.isEmpty()) {
			return true;
		}

		return false;
	}

	public void drawAll(Graphics2D g, int width) {

		int xx = width - 100;
		int hh = 10;

		g.setColor(pnodeColor);
		g.fillRoundRect(xx, hh, 25, 10, 5, 5);
		g.drawString("pNodes", xx + 30, hh + 8);

		int index = 1;
		for (Double double1 : pnodesOval) {
			g.fill(double1);

			float yyy = (float) (double1.y);
			float xxx = (float) (double1.x);
			g.drawString(String.valueOf(index), xxx, yyy);
			index++;
		}
		for (RoundRectangle2D.Double double1 : pnodesRect) {
			g.fill(double1);

			float yyy = (float) (double1.y);
			float xxx = (float) (double1.x);
			g.drawString(String.valueOf(index), xxx, yyy);
			index++;
		}

		for (Path2D double1 : pnodesCustom) {
			g.fill(double1);

			Rectangle2D bounds2d = double1.getBounds2D();
			float yyy = (float) (bounds2d.getY());
			float xxx = (float) (bounds2d.getX());
			g.drawString(String.valueOf(index), xxx, yyy);
			index++;
		}

		g.setColor(pmembraneColor);
		g.fillRoundRect(xx, hh + 20, 25, 10, 5, 5);
		g.drawString("pMembrane", xx + 30, hh + 8 + 20);

		Stroke oldStorke = g.getStroke();
		g.setStroke(thickStokeLine);
		for (Double double1 : pmembraneOval) {
			g.draw(double1);
		}
		for (RoundRectangle2D.Double double1 : pmembraneRect) {
			g.draw(double1);
		}
		g.setStroke(oldStorke);

		g.setColor(pnuclearColor);
		g.fillRoundRect(xx, hh + 20 * 2, 25, 10, 5, 5);
		g.drawString("pNuclear", xx + 30, hh + 8 + 20 * 2);
		for (Double double1 : pnucleusOval) {
			g.draw(double1);
		}
		for (RoundRectangle2D.Double double1 : pnucleusRect) {
			g.draw(double1);
		}

		g.setColor(pnuclearDNAColor);
		g.fillRoundRect(xx, hh + 20 * 3, 25, 10, 5, 5);
		g.drawString("pNuclearDNA", xx + 30, hh + 8 + 20 * 3);

		for (Line2D.Double double1 : pnucleusDNA) {
			Stroke stroke = g.getStroke();
			g.setStroke(thickStokeLine);
			g.draw(double1);

			g.setStroke(stroke);
		}

		g.setColor(parrowColor);
		g.fillRoundRect(xx, hh + 20 * 4, 25, 10, 5, 5);
		g.drawString("pFreeArrow", xx + 30, hh + 8 + 20 * 4);
		for (Line2D.Double double1 : pfreeArrows) {
			ArrowHead.arrowDrawer(g, double1);
		}
		g.setColor(Color.black);
	}

	public void drawCurrent(Graphics2D g, PaintingMode paintingMode) {
		switch (paintingMode) {
		case P_MEMBRANE_OVAL:
			g.setColor(GraphicProperties.pmembraneColor);
			if (currentMembraneOval.getMinX() < 0 || currentMembraneOval.getMinY() < 0) {

			} else {
				g.draw(currentMembraneOval);
			}

			break;
		case P_MEMBRANE_RECT:
			g.setColor(GraphicProperties.pmembraneColor);
			g.draw(currentMembraneRect);
			break;
		case P_NUCLEUS_OVAL:
			g.setColor(GraphicProperties.pnuclearColor);
			g.draw(currentNucleusOval);
			break;
		case P_NUCLEUS_RECT:
			g.setColor(GraphicProperties.pnuclearColor);
			g.draw(currentNucleusRect);
			break;
		case P_NUCLEUS_DNA:
			g.setColor(GraphicProperties.pnuclearDNAColor);
			g.draw(currentCleusDNA);
			break;
		case P_FREEARROW_SEGM:
			g.setColor(GraphicProperties.parrowColor);
			ArrowHead.arrowDrawer(g, currentArrow);
			break;
		case P_NODES_OVAL:
			if (currentNodesOval.width > 1) {
				if (currentNodesOval.getCenterX() < 10 || currentNodesOval.getCenterY() < 10) {

				} else {
					g.setColor(GraphicProperties.pnodeColor);
					g.fill(currentNodesOval);
				}
			}
			break;
		case P_NODES_RECT:
			if (currentNodesRect.width > 1) {
				if (currentNodesRect.getCenterX() < 10 || currentNodesRect.getCenterY() < 10) {

				} else {
					g.setColor(GraphicProperties.pnodeColor);
					g.fill(currentNodesRect);
				}
			}
			break;
		case P_NODES_CUSTOM:
			g.setColor(GraphicProperties.pnodeColor);
			g.fill(nodeCustomAT.createTransformedShape(currentNodesCustom));
			break;
		default:
			break;
		}

	}

	public void deleteGrobs(Point2D point, PaintingMode paintingMode) {
		switch (paintingMode) {
		case P_MEMBRANE_OVAL:
			delete(pmembraneOval, point);
			break;
		case P_MEMBRANE_RECT:
			delete(pmembraneRect, point);
			break;
		case P_NUCLEUS_OVAL:
			delete(pnucleusOval, point);
			break;
		case P_NUCLEUS_RECT:
			delete(pnucleusRect, point);
			break;
		case P_NUCLEUS_DNA:
			delete(pnucleusDNA, point);
			break;
		case P_FREEARROW_SEGM:
			delete(pfreeArrows, point);
			break;
		case P_NODES_OVAL:
			delete(pnodesOval, point);
			break;
		case P_NODES_RECT:
			delete(pnodesRect, point);
			break;
		default:
			break;
		}
	}

	private void delete(List<? extends Shape> shape, Point2D point) {
		int keyPointID = -1;
		int size = shape.size();

		for (int i = 0; i < size; i++) {
			Shape currentShape = shape.get(i);
			if (currentShape instanceof Line2D.Double) {
				Line2D.Double dd1 = (Line2D.Double) currentShape;
				double ptLineDist = dd1.ptSegDist(point);
				if (ptLineDist < 10) {
					keyPointID = i;
					break;
				}
			} else {
				if (currentShape.contains(point)) {
					keyPointID = i;
					break;
				}
			}
		}

		if (keyPointID > -1) {
			// has point in
			shape.remove(keyPointID);
		}

	}

	public void removeGrobAccording2Eraser(Point point) {
		delete(pmembraneOval, point);
		delete(pmembraneRect, point);
		delete(pnucleusOval, point);
		delete(pnucleusRect, point);
		delete(pnucleusDNA, point);
		delete(pfreeArrows, point);
		delete(pnodesOval, point);
		delete(pnodesRect, point);
	}

}
