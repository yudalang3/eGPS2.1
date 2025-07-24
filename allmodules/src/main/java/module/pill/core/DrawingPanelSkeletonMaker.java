package module.pill.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import egps2.frame.gui.EGPSSwingUtil;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import module.pill.graphics.KEGGNotationDrawer;
import module.pill.graphics.PathwayDrawer;
import module.pill.images.ImageUtils;

public class DrawingPanelSkeletonMaker extends JPanel {
	private static final Logger logger = LoggerFactory.getLogger(DrawingPanelSkeletonMaker.class);

	private static final long serialVersionUID = -9172512258793454429L;

	private Image image;

	private int keyPointID = -1;

	private GraphicProperties graphicProperties = new GraphicProperties();
	// 1 points; 2 round rectangle; 3 ellipse; 4 line
	private PaintingMode paintingMode = PaintingMode.P_NODES_OVAL;
	private final Point2D.Double startPoint = new Point2D.Double();

	private SkeletonMaker centerController;

	private boolean isEraserToggled = false;
	private boolean isImageMaskToggled = false;

	private Cursor createCustomCursor;

	private boolean gridState = false;
	private final KEGGNotationDrawer keggNotationDrawer = new KEGGNotationDrawer();
	private PathwayDrawer pathwayDrawer = new PathwayDrawer();

	private XSLFSlide firstSlide;

	class MouseActionAndWheel extends MouseAdapter {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (isEraserToggled) {
				return;
			}

			int wheelRotation = e.getWheelRotation();

			int scrollAmount = e.getScrollAmount();

			int changeValue = (-1) * wheelRotation * scrollAmount;

			// -1 是放大, 1是缩小
			graphicProperties.pnodeHalfWidth = graphicProperties.pnodeHalfWidth + changeValue;
			graphicProperties.pnodeHalfHeight = graphicProperties.pnodeHalfHeight + changeValue;
			switch (paintingMode) {
			case P_NODES_OVAL:
				double x = e.getX();
				double y = e.getY();
				graphicProperties.currentNodesOval.setFrameFromCenter(x, y, x + graphicProperties.pnodeHalfWidth,
						y + graphicProperties.pnodeHalfHeight);
				centerController.checkSpinner(graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight,
						graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight);
				repaint();
				break;
			case P_NODES_RECT:
				x = e.getX();
				y = e.getY();
				graphicProperties.currentNodesRect.setFrameFromCenter(x, y, x + graphicProperties.pnodeHalfWidth,
						y + graphicProperties.pnodeHalfHeight);
				centerController.checkSpinner(graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight,
						graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight);
				repaint();
				break;
			default:
				break;
			}

		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if (isEraserToggled) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					centerController.restoreEraser2disabledState();
				}
				return;
			}

			double x = e.getX();
			double y = e.getY();

			// 点击鼠标左键
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (paintingMode == PaintingMode.P_NODES_OVAL) {
					int h = graphicProperties.pnodeHalfHeight;
					int w = graphicProperties.pnodeHalfWidth;
					graphicProperties.pnodesOval.add(new Double(x - w, y - h, 2 * w, 2 * h));

				} else if (paintingMode == PaintingMode.P_NODES_RECT) {
					int h = graphicProperties.pnodeHalfHeight;
					int w = graphicProperties.pnodeHalfWidth;
					graphicProperties.pnodesRect
							.add(new RoundRectangle2D.Double(x - w, y - h, 2 * w, 2 * h, w / 4, h / 4));

				} else if (paintingMode == PaintingMode.P_NODES_CUSTOM) {
					Path2D custom = graphicProperties.currentNodesCustom;

					Shape created = graphicProperties.nodeCustomAT.createTransformedShape(custom);
					graphicProperties.pnodesCustom.add((Path2D) created);

				}
			} // 点击鼠标右键
			else if (e.getButton() == MouseEvent.BUTTON3) {
				deleteGrobs((Point2D) e.getPoint());
			}

			// check the mouse wheel and spinner
			centerController.checkSpinner(graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight,
					graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight);

			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isEraserToggled) {
				return;
			}

			switch (paintingMode) {
			case P_MEMBRANE_RECT:
				RoundRectangle2D.Double currentMembraneRect = graphicProperties.currentMembraneRect;
				if (currentMembraneRect.width > 9) {
					graphicProperties.pmembraneRect.add(currentMembraneRect);
					graphicProperties.currentMembraneRect = new RoundRectangle2D.Double();
				}
				break;
			case P_MEMBRANE_OVAL:
				Double currentMembraneOval = graphicProperties.currentMembraneOval;
				if (currentMembraneOval.width > 9) {
					graphicProperties.pmembraneOval.add(currentMembraneOval);
					graphicProperties.currentMembraneOval = new Double();
				}
				break;
			case P_NUCLEUS_RECT:
				RoundRectangle2D.Double currentNucleusRect = graphicProperties.currentNucleusRect;
				if (currentNucleusRect.width > 9) {
					graphicProperties.pnucleusRect.add(currentNucleusRect);
					graphicProperties.currentNucleusRect = new RoundRectangle2D.Double();
				}
				break;
			case P_NUCLEUS_OVAL:
				Double currentNucleusOval = graphicProperties.currentNucleusOval;
				if (currentNucleusOval.width > 9) {
					graphicProperties.pnucleusOval.add(currentNucleusOval);
					graphicProperties.currentNucleusOval = new Double();
				}
				break;
			case P_NUCLEUS_DNA:
				Line2D.Double currentDNA_line = graphicProperties.currentCleusDNA;
				if (Math.abs(currentDNA_line.x1 - currentDNA_line.x2) > 9) {
					graphicProperties.pnucleusDNA.add(currentDNA_line);
					graphicProperties.currentCleusDNA = new Line2D.Double();
				}
				break;

			case P_FREEARROW_SEGM:
				Line2D.Double currentArrow = graphicProperties.currentArrow;
				double x = currentArrow.getX1() - currentArrow.getX2();
				double y = currentArrow.getY1() - currentArrow.getY2();
				double length = Math.sqrt(x * x + y * y);
				if (length > 1) {
					graphicProperties.pfreeArrows.add(currentArrow);
					graphicProperties.currentArrow = new Line2D.Double();
				}
				break;
			default:
				break;
			}

			repaint();
		}

		private void deleteGrobs(Point2D point) {
			graphicProperties.deleteGrobs(point, paintingMode);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (isEraserToggled) {
				return;
			}

			keyPointID = -1;

			switch (paintingMode) {
			case P_NODES_OVAL:
				// 按下鼠标,判断是不是点击了关键点
				List<Double> pnodesOval = graphicProperties.pnodesOval;
				int size = pnodesOval.size();
				for (int i = 0; i < size; i++) {
					if (pnodesOval.get(i).contains((Point2D) e.getPoint())) {
						keyPointID = i;
						break;
					}
				}
				break;
			case P_NODES_RECT:
				// 按下鼠标,判断是不是点击了关键点
				List<RoundRectangle2D.Double> pnodesRect = graphicProperties.pnodesRect;
				size = pnodesRect.size();
				for (int i = 0; i < size; i++) {
					if (pnodesRect.get(i).contains((Point2D) e.getPoint())) {
						keyPointID = i;
						break;
					}
				}
				break;
			case P_NODES_CUSTOM:
				// 按下鼠标,判断是不是点击了关键点
				List<Path2D> pnucleusCustom = graphicProperties.pnodesCustom;
				size = pnucleusCustom.size();
				for (int i = 0; i < size; i++) {
					if (pnucleusCustom.get(i).contains((Point2D) e.getPoint())) {
						keyPointID = i;
						break;
					}
				}
				break;
			case P_MEMBRANE_OVAL:
				List<Double> pmembraneOval = graphicProperties.pmembraneOval;
				size = pmembraneOval.size();
				for (int i = 0; i < size; i++) {
					if (pmembraneOval.get(i).contains((Point2D) e.getPoint())) {
						keyPointID = i;
						break;
					}
				}
				break;
			case P_MEMBRANE_RECT:
				List<RoundRectangle2D.Double> pmembraneRect = graphicProperties.pmembraneRect;
				size = pmembraneRect.size();
				for (int i = 0; i < size; i++) {
					if (pmembraneRect.get(i).contains((Point2D) e.getPoint())) {
						keyPointID = i;
						break;
					}
				}
				break;
			case P_NUCLEUS_OVAL:
				List<Double> pnucleusOval = graphicProperties.pnucleusOval;
				size = pnucleusOval.size();
				for (int i = 0; i < size; i++) {
					if (pnucleusOval.get(i).contains((Point2D) e.getPoint())) {
						keyPointID = i;
						break;
					}
				}
				break;
			case P_NUCLEUS_RECT:
				List<RoundRectangle2D.Double> pnucleusRect = graphicProperties.pnucleusRect;
				size = pnucleusRect.size();
				for (int i = 0; i < size; i++) {
					if (pnucleusRect.get(i).contains((Point2D) e.getPoint())) {
						keyPointID = i;
						break;
					}
				}
				break;
			case P_NUCLEUS_DNA:
				List<Line2D.Double> pnucleusDNA = graphicProperties.pnucleusDNA;
				size = pnucleusDNA.size();
				for (int i = 0; i < size; i++) {
					if (pnucleusDNA.get(i).ptSegDist((Point2D) e.getPoint()) < 10) {
						keyPointID = i;
						break;
					}
				}
				break;
			//
			default:

				break;
			}

			Point point = e.getPoint();
			startPoint.setLocation(point);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point point = e.getPoint();

			if (isEraserToggled) {
				graphicProperties.removeGrobAccording2Eraser(point);
				repaint();
				return;
			}

			switch (paintingMode) {
			case P_NODES_OVAL:
				// 鼠标拖动关键点
				if (keyPointID != -1) {
					Double double1 = graphicProperties.pnodesOval.get(keyPointID);

					double h = double1.getHeight();
					double w = double1.getWidth();
					double xDeviation = point.getX() - startPoint.getX();
					double yDeviation = point.getY() - startPoint.getY();

					int type = getCursor().getType();
					switch (type) {
					case Cursor.HAND_CURSOR:
						double1.setFrame(double1.getX() + xDeviation, double1.getY() + yDeviation, w, h);
						break;
					case Cursor.N_RESIZE_CURSOR:
						double newHeight = double1.height - yDeviation;
						double1.height = newHeight;
						double newY = double1.y + yDeviation;
						double1.y = newY;
						break;
					case Cursor.E_RESIZE_CURSOR:
						double newWidth = double1.width + xDeviation;
						double1.width = newWidth;
						break;
					case Cursor.S_RESIZE_CURSOR:
						newHeight = double1.height + yDeviation;
						double1.height = newHeight;
						break;
					case Cursor.W_RESIZE_CURSOR:
						newWidth = double1.width - xDeviation;
						double1.width = newWidth;
						double newX = double1.x + xDeviation;
						double1.x = newX;
						break;
					default:
						break;
					}

					startPoint.setLocation(point);
					graphicProperties.currentNodesOval.width = 0;
				}
				break;
			case P_NODES_RECT:
				if (keyPointID != -1) {
					RoundRectangle2D.Double double1 = graphicProperties.pnodesRect.get(keyPointID);
					double h = double1.getHeight();
					double w = double1.getWidth();
					double xDeviation = point.getX() - startPoint.getX();
					double yDeviation = point.getY() - startPoint.getY();

					int type = getCursor().getType();
					switch (type) {
					case Cursor.HAND_CURSOR:
						double1.setFrame(double1.getX() + xDeviation, double1.getY() + yDeviation, w, h);
						break;
					case Cursor.N_RESIZE_CURSOR:
						double newHeight = double1.height - yDeviation;
						double1.height = newHeight;
						double newY = double1.y + yDeviation;
						double1.y = newY;
						break;
					case Cursor.E_RESIZE_CURSOR:
						double newWidth = double1.width + xDeviation;
						double1.width = newWidth;
						break;
					case Cursor.S_RESIZE_CURSOR:
						newHeight = double1.height + yDeviation;
						double1.height = newHeight;
						break;
					case Cursor.W_RESIZE_CURSOR:
						newWidth = double1.width - xDeviation;
						double1.width = newWidth;
						double newX = double1.x + xDeviation;
						double1.x = newX;
						break;
					default:
						break;
					}

					startPoint.setLocation(point);
					graphicProperties.currentNodesRect.width = 0;
				}
				break;
			case P_NODES_CUSTOM:
				if (keyPointID != -1) {
					Path2D double1 = graphicProperties.pnodesCustom.get(keyPointID);

					Rectangle2D bounds2d = double1.getBounds2D();

					double xDeviation = point.getX() - startPoint.getX();
					double yDeviation = point.getY() - startPoint.getY();
					double xx = bounds2d.getCenterX() + xDeviation;
					double yy = bounds2d.getCenterY() + yDeviation;

//					graphicProperties.nodeCustomAT.scale(2, 2);
					graphicProperties.nodeCustomAT.setToTranslation(xx, yy);
					Shape createTransformedShape = graphicProperties.nodeCustomAT
							.createTransformedShape(graphicProperties.currentNodesCustom);

					graphicProperties.pnodesCustom.set(keyPointID, (Path2D) createTransformedShape);

					startPoint.setLocation(point);
				}
				break;
			case P_FREEARROW_SEGM:
				if (e.isShiftDown()) {
					double xDist = Math.abs(startPoint.x - point.x);
					double yDist = Math.abs(startPoint.y - point.y);
					if (xDist > 0 && Math.abs(Math.log(yDist / xDist)) < 2) {
						double xDeviation = point.x - startPoint.x;
						double yDeviation = point.y - startPoint.y;
						double yAddition = 0;

						if (xDeviation > 0) {
							if (yDeviation > 0) {
								yAddition = xDeviation;
							} else {
								yAddition = -xDeviation;
							}
						} else {
							if (point.y - startPoint.y > 0) {
								yAddition = -xDeviation;
							} else {
								yAddition = xDeviation;
							}
						}

//						System.out.printf("xDeviation is %f, yDeviation is %f, yAddition is %f \n", xDeviation, yDeviation , yAddition);
						graphicProperties.currentArrow.setLine(startPoint.x, startPoint.y, startPoint.x + xDeviation,
								startPoint.y + yAddition);
					} else if (xDist > yDist) {
						graphicProperties.currentArrow.setLine(startPoint.x, startPoint.y, point.x, startPoint.y);
					} else {
						graphicProperties.currentArrow.setLine(startPoint.x, startPoint.y, startPoint.x, point.y);
					}
				} else {
					graphicProperties.currentArrow.setLine(startPoint, point);
				}
				break;
			case P_MEMBRANE_OVAL:
				if (keyPointID == -1) {
					graphicProperties.currentMembraneOval.setFrameFromDiagonal(startPoint, point);
				} else {
					Double double1 = graphicProperties.pmembraneOval.get(keyPointID);
					double h = double1.getHeight();
					double w = double1.getWidth();
					double xDeviation = point.getX() - startPoint.getX();
					double yDeviation = point.getY() - startPoint.getY();

					int type = getCursor().getType();
					switch (type) {
					case Cursor.HAND_CURSOR:
						double1.setFrame(double1.getX() + xDeviation, double1.getY() + yDeviation, w, h);
						break;
					case Cursor.N_RESIZE_CURSOR:
						double newHeight = double1.height - yDeviation;
						double1.height = newHeight;
						double newY = double1.y + yDeviation;
						double1.y = newY;
						break;
					case Cursor.E_RESIZE_CURSOR:
						double newWidth = double1.width + xDeviation;
						double1.width = newWidth;
						break;
					case Cursor.S_RESIZE_CURSOR:
						newHeight = double1.height + yDeviation;
						double1.height = newHeight;
						break;
					case Cursor.W_RESIZE_CURSOR:
						newWidth = double1.width - xDeviation;
						double1.width = newWidth;
						double newX = double1.x + xDeviation;
						double1.x = newX;
						break;
					default:
						break;
					}

					graphicProperties.currentMembraneOval.width = 0;
					startPoint.setLocation(point);
				}
				break;
			case P_MEMBRANE_RECT:
				if (keyPointID == -1) {
					graphicProperties.currentMembraneRect.setFrameFromDiagonal(startPoint, point);
				} else {
					RoundRectangle2D.Double double1 = graphicProperties.pmembraneRect.get(keyPointID);
					double h = double1.getHeight();
					double w = double1.getWidth();
					double xDeviation = point.getX() - startPoint.getX();
					double yDeviation = point.getY() - startPoint.getY();
					double1.setFrame(double1.getX() + xDeviation, double1.getY() + yDeviation, w, h);

					graphicProperties.currentMembraneRect.width = 0;
					startPoint.setLocation(point);
				}
				break;
			case P_NUCLEUS_OVAL:
				if (keyPointID == -1) {
					graphicProperties.currentNucleusOval.setFrameFromDiagonal(startPoint, point);
				} else {
					Double double1 = graphicProperties.pnucleusOval.get(keyPointID);
					double h = double1.getHeight();
					double w = double1.getWidth();
					double xDeviation = point.getX() - startPoint.getX();
					double yDeviation = point.getY() - startPoint.getY();

					int type = getCursor().getType();
					switch (type) {
					case Cursor.HAND_CURSOR:
						double1.setFrame(double1.getX() + xDeviation, double1.getY() + yDeviation, w, h);
						break;
					case Cursor.N_RESIZE_CURSOR:
						double newHeight = double1.height - yDeviation;
						double1.height = newHeight;
						double newY = double1.y + yDeviation;
						double1.y = newY;
						break;
					case Cursor.E_RESIZE_CURSOR:
						double newWidth = double1.width + xDeviation;
						double1.width = newWidth;
						break;
					case Cursor.S_RESIZE_CURSOR:
						newHeight = double1.height + yDeviation;
						double1.height = newHeight;
						break;
					case Cursor.W_RESIZE_CURSOR:
						newWidth = double1.width - xDeviation;
						double1.width = newWidth;
						double newX = double1.x + xDeviation;
						double1.x = newX;
						break;
					default:
						break;
					}

					graphicProperties.currentNucleusOval.width = 0;
					startPoint.setLocation(point);
				}
				break;
			case P_NUCLEUS_RECT:
				if (keyPointID == -1) {
					graphicProperties.currentNucleusRect.setFrameFromDiagonal(startPoint, point);
				} else {
					RoundRectangle2D.Double double1 = graphicProperties.pnucleusRect.get(keyPointID);
					double h = double1.getHeight();
					double w = double1.getWidth();
					double xDeviation = point.getX() - startPoint.getX();
					double yDeviation = point.getY() - startPoint.getY();
					double1.setFrame(double1.getX() + xDeviation, double1.getY() + yDeviation, w, h);

					graphicProperties.currentNucleusRect.width = 0;
					startPoint.setLocation(point);
				}
				break;
			default:
				// NUCLEAR DNA
				if (keyPointID == -1) {
					graphicProperties.currentCleusDNA.setLine(startPoint.x, startPoint.y, point.x, startPoint.y);
				} else {
					Line2D.Double double1 = graphicProperties.pnucleusDNA.get(keyPointID);

					double xx = point.getX() - startPoint.getX();
					double yy = point.getY() - startPoint.getY();

					double1.setLine(double1.getX1() + xx, double1.getY1() + yy, double1.getX2() + xx,
							double1.getY2() + yy);

					Point2D p1 = graphicProperties.currentCleusDNA.getP1();
					graphicProperties.currentCleusDNA.setLine(p1, p1);

					startPoint.setLocation(point);
				}
				break;
			}
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {

			requestFocusInWindow();
			if (isEraserToggled) {
				return;
			}

			double x = e.getX();
			double y = e.getY();
			keyPointID = -1;

			switch (paintingMode) {
			case P_NODES_OVAL:
				List<Double> pnodesOval = graphicProperties.pnodesOval;
				int index = 0;
				for (Double double1 : pnodesOval) {
					int cursorType = getCursorType(x, y, double1.getBounds());
					if (cursorType != Cursor.DEFAULT_CURSOR) {
						setCursor(Cursor.getPredefinedCursor(cursorType));
						keyPointID = index;
						graphicProperties.currentNodesOval.width = 0;
						break;
					}
					index++;
				}

				if (keyPointID == -1) {
					// No change
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

					graphicProperties.currentNodesOval.setFrameFromCenter(x, y, x + graphicProperties.pnodeHalfWidth,
							y + graphicProperties.pnodeHalfHeight);
				}

				break;
			case P_NODES_RECT:
				List<RoundRectangle2D.Double> pnodesRect = graphicProperties.pnodesRect;
				index = 0;
				for (RoundRectangle2D.Double double1 : pnodesRect) {
					int cursorType = getCursorType(x, y, double1.getBounds());
					if (cursorType != Cursor.DEFAULT_CURSOR) {
						setCursor(Cursor.getPredefinedCursor(cursorType));
						keyPointID = index;
						graphicProperties.currentNodesRect.width = 0;
						break;
					}
					index++;
				}

				if (keyPointID == -1) {
					// No change
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

					graphicProperties.currentNodesRect.setFrameFromCenter(x, y, x + graphicProperties.pnodeHalfWidth,
							y + graphicProperties.pnodeHalfHeight);
				}

				break;
			case P_NODES_CUSTOM:
				graphicProperties.nodeCustomAT.setToTranslation(x, y);
				break;
			case P_MEMBRANE_OVAL:
				List<Double> pmembraneOval = graphicProperties.pmembraneOval;
				index = 0;
				for (Double double1 : pmembraneOval) {
					int cursorType = getCursorType(x, y, double1.getBounds());
					if (cursorType != Cursor.DEFAULT_CURSOR) {
						setCursor(Cursor.getPredefinedCursor(cursorType));
						keyPointID = index;
						graphicProperties.currentMembraneOval.width = 0;
						break;
					}
					index++;
				}
				if (keyPointID == -1) {
					// No change
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				break;
			case P_NUCLEUS_OVAL:
				List<Double> pnucleusOval = graphicProperties.pnucleusOval;
				index = 0;
				for (Double double1 : pnucleusOval) {
					int cursorType = getCursorType(x, y, double1.getBounds());
					if (cursorType != Cursor.DEFAULT_CURSOR) {
						setCursor(Cursor.getPredefinedCursor(cursorType));
						keyPointID = index;
						graphicProperties.currentMembraneOval.width = 0;
						break;
					}
					index++;
				}
				if (keyPointID == -1) {
					// No change
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				break;
			default:
				break;
			}
			repaint();

		}
	}

	class SkeletonMakerKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			int changeValue = 4;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				graphicProperties.pnodeHalfWidth = graphicProperties.pnodeHalfWidth - changeValue;
				break;
			case KeyEvent.VK_D:
				graphicProperties.pnodeHalfWidth = graphicProperties.pnodeHalfWidth + changeValue;
				break;
			case KeyEvent.VK_W:
				graphicProperties.pnodeHalfHeight = graphicProperties.pnodeHalfHeight - changeValue;
				break;
			case KeyEvent.VK_S:
				graphicProperties.pnodeHalfHeight = graphicProperties.pnodeHalfHeight + changeValue;
				break;

			default:
				break;
			}

			switch (paintingMode) {
			case P_NODES_OVAL:
				graphicProperties.currentNodesOval.height = graphicProperties.pnodeHalfHeight * 2;
				graphicProperties.currentNodesOval.width = graphicProperties.pnodeHalfWidth * 2;
				repaint();
				break;
			case P_NODES_RECT:
				graphicProperties.currentNodesRect.height = graphicProperties.pnodeHalfHeight * 2;
				graphicProperties.currentNodesRect.width = graphicProperties.pnodeHalfWidth * 2;
				repaint();
				break;
			default:
				break;
			}

			centerController.checkSpinner(graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight,
					graphicProperties.pnodeHalfWidth, graphicProperties.pnodeHalfHeight);
		}
//			System.out.printf("Keycode is %d, modifier is %d.\n",e.getKeyCode() , e.getModifiers());
	}

	public DrawingPanelSkeletonMaker(SkeletonMaker locationPicker) {
		super();
		this.centerController = locationPicker;
		MouseActionAndWheel mouseAction = new MouseActionAndWheel();

		this.addMouseListener(mouseAction);
		this.addMouseMotionListener(mouseAction);
		this.addMouseWheelListener(mouseAction);
		this.addKeyListener(new SkeletonMakerKeyAdapter());
	}

	@Override
	protected void paintComponent(Graphics gs) {
		super.paintComponent(gs);

		Graphics2D g = (Graphics2D) gs;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);

		int height = getHeight();
		int width = getWidth();

		boolean showNotation = true;

		if (firstSlide != null) {
			Graphics2D graphics = (Graphics2D) g.create();
			List<XSLFShape> shapes = firstSlide.getShapes();
			
			for (XSLFShape shape : shapes) {
				if (shape instanceof XSLFTextBox) {
					XSLFTextBox textBox = (XSLFTextBox) shape;
					String text = textBox.getText();
					if (text.startsWith("S2")) {
						textBox.setLineColor(Color.magenta);
					}
				}
			}

			firstSlide.draw(graphics);
			showNotation = false;
		}

		if (gridState) {
			int heightInt = (int) Math.floor(height / 100);
			int widthInt = (int) Math.floor(width / 100);

			g.setColor(Color.gray);
			Stroke stroke = g.getStroke();
			float[] dash1 = { 2f, 0f, 2f };
			BasicStroke bs1 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash1, 2f);
			g.setStroke(bs1);
			// Draw y axis
			for (int i = 1; i <= heightInt; i++) {
				int hh = i * 100;

				g.drawLine(0, hh, width, hh);
				g.drawString(String.valueOf(i * 100), 5, hh - 5);
			}

			// Draw x axis
			for (int i = 0; i <= widthInt; i++) {
				int ww = i * 100;
				g.drawLine(ww, 0, ww, height);
				g.drawString(String.valueOf(i * 100), ww + 5, 10);
			}

			g.setStroke(stroke);
		}
		if (image != null && !isImageMaskToggled) {
			g.drawImage(image, 0, 0, null);
		}

		if (graphicProperties.hasGraphicsElements()) {
			graphicProperties.drawAll(g, getWidth());
			showNotation = false;
		}

		if (pathwayDrawer.isHasInputFile()) {
			pathwayDrawer.draw(g, width, height);
			showNotation = false;
		}

		if (showNotation) {
			keggNotationDrawer.drawNotation(g);
		}

		if (!isEraserToggled) {
			graphicProperties.drawCurrent(g, paintingMode);
		}

		String ss = " Panel dim: " + String.valueOf(width) + " / " + String.valueOf(height);
		g.setColor(Color.black);
		g.drawString(ss, width - 150, height - 20);


	}

	public Dimension loadImage() {
		try {
			image = EGPSSwingUtil.getImageFromClipboard();

			if (image == null) {
				JOptionPane.showMessageDialog(this, "You need to copy a image.");
				return null;
			}

			int w = image.getWidth(null);
			int h = image.getHeight(null);

			repaint();

			return new Dimension(w, h);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void changeEllipseSize(int w, int h) {
		graphicProperties.pnodeHalfWidth = w;
		graphicProperties.pnodeHalfHeight = h;
	}

	/**
	 * 1 points; 2 round rectangle; 3 ellipse; 4 line
	 * 
	 * @param mode int
	 */
	public void switchToOtherMode(int mode) {

		switch (mode) {
		case 1:
			this.paintingMode = PaintingMode.P_NODES_OVAL;
			break;
		case 2:
			this.paintingMode = PaintingMode.P_NODES_RECT;
			break;
		case 9:
			this.paintingMode = PaintingMode.P_NODES_CUSTOM;
			break;
		case 3:
			this.paintingMode = PaintingMode.P_MEMBRANE_OVAL;
			break;
		case 4:
			this.paintingMode = PaintingMode.P_MEMBRANE_RECT;
			break;
		case 5:
			this.paintingMode = PaintingMode.P_NUCLEUS_OVAL;
			break;
		case 6:
			this.paintingMode = PaintingMode.P_NUCLEUS_RECT;
			break;
		case 7:
			this.paintingMode = PaintingMode.P_NUCLEUS_DNA;
			break;
		case 8:
			this.paintingMode = PaintingMode.P_FREEARROW_SEGM;
			break;

		default:
			this.paintingMode = PaintingMode.P_NODES_OVAL;
			break;
		}

	}

	public void clearAll() {
		graphicProperties.clearAll();
		firstSlide = null;
		repaint();

	}

	public GraphicProperties getGraphicProperties() {
		return graphicProperties;
	}

	public void setGraphicProperties(GraphicProperties graphicProperties) {
		this.graphicProperties = graphicProperties;
		repaint();
	}

	public void setEraserState(boolean state) {
		this.isEraserToggled = state;
		if (state) {
			if (createCustomCursor == null) {

				URL resource = ImageUtils.getResource("eraser.png");
				BufferedImage read = null;
				try {
					read = ImageIO.read(resource);
				} catch (IOException e) {
					e.printStackTrace();
				}
				createCustomCursor = Toolkit.getDefaultToolkit().createCustomCursor(read, new Point(10, 10), "eraser");
			}
			setCursor(createCustomCursor);
		} else {
			setCursor(Cursor.getDefaultCursor());
		}

		repaint();
	}

	public void setImageMaskState(boolean b) {
		this.isImageMaskToggled = b;
		repaint();
	}

	public void setGridState(boolean b) {
		this.gridState = b;
		repaint();
	}

	private int getCursorType(double x, double y, Rectangle rect) {
		int cursorType = Cursor.DEFAULT_CURSOR;

		int cornerSize = 9;

		if (rect.contains(x, y)) {
			// Check for corners
//			if (x <= rect.x + cornerSize && y <= rect.y + cornerSize) {
//				cursorType = Cursor.NW_RESIZE_CURSOR; // Top-left corner
//			} else if (x >= rect.x + rect.width - cornerSize && y <= rect.y + cornerSize) {
//				cursorType = Cursor.NE_RESIZE_CURSOR; // Top-right corner
//			} else if (x <= rect.x + cornerSize && y >= rect.y + rect.height - cornerSize) {
//				cursorType = Cursor.SW_RESIZE_CURSOR; // Bottom-left corner
//			} else if (x >= rect.x + rect.width - cornerSize && y >= rect.y + rect.height - cornerSize) {
//				cursorType = Cursor.SE_RESIZE_CURSOR; // Bottom-right corner
//			}
			// Check for sides
			if (x <= rect.x + cornerSize) {
				cursorType = Cursor.W_RESIZE_CURSOR; // Left side
			} else if (x >= rect.x + rect.width - cornerSize) {
				cursorType = Cursor.E_RESIZE_CURSOR; // Right side
			} else if (y <= rect.y + cornerSize) {
				cursorType = Cursor.N_RESIZE_CURSOR; // Top side
			} else if (y >= rect.y + rect.height - cornerSize) {
				cursorType = Cursor.S_RESIZE_CURSOR; // Bottom side
			} else {
				cursorType = Cursor.HAND_CURSOR; // Inside the rectangle
			}
		}

		return cursorType;
	}

	public void setKEGGInputFile(File file) {
		pathwayDrawer.setHasInputFile(file);
	}

	public void setSlide(XSLFSlide firstSlide) {
		this.firstSlide = firstSlide;
	}
}
