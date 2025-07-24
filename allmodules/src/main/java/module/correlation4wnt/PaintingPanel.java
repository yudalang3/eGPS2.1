package module.correlation4wnt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.util.FastMath;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeBasedTable;

import graphic.engine.DefaultLinerColorMapper;
import graphic.engine.EGPSDrawUtil;
import graphic.engine.EGPSStrokeUtil;
import graphic.engine.GradientColorHolder;
import egps2.frame.gui.EGPSMainGuiUtil;
import graphic.engine.legend.CategloryLegendPainter;
import graphic.engine.legend.LinerColorLegendPainter;

@SuppressWarnings("serial")
public class PaintingPanel extends JPanel implements MouseListener, MouseWheelListener {

	DataModel dataModel;

	Rectangle2D.Double rectangleDrawUtil = new Rectangle2D.Double();
	Ellipse2D.Double circularDrawUtil = new Ellipse2D.Double();

	LinerColorLegendPainter linerColorLegendPainter;

	public PaintingPanel() {
		addMouseListener(this);
		addMouseWheelListener(this);

		setBackground(Color.white);

	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		EGPSMainGuiUtil.setupHighQualityRendering(g2d);

		draw(g2d);
		g.dispose();

	}

	protected void draw(Graphics2D g2d) {
		if (dataModel == null) {
			EGPSMainGuiUtil.drawLetUserImportDataString(g2d);
			return;
		}
		int width2 = getWidth();
		int height2 = getHeight();

		int matrixRegionSize = (int) (width2 * 0.4);
		int start = (int) (width2 * 0.2);
		Rectangle matrixRegion = new Rectangle(start, 80, matrixRegionSize, matrixRegionSize);

		Font namesFont = new Font("Arial", Font.PLAIN, 14);
		Rectangle2D rightNamesRegion = new Rectangle2D.Double(matrixRegion.getMaxX(), matrixRegion.getMinY(), 30,
				matrixRegion.getHeight());
		Rectangle2D rightAnnotationDrawArea = new Rectangle2D.Double(rightNamesRegion.getMaxX() + 80,
				rightNamesRegion.getMinY(), 15, matrixRegion.getHeight());
		Rectangle2D topNamesRegion = new Rectangle2D.Double(matrixRegion.getX(), matrixRegion.getY(), matrixRegionSize,
				matrixRegionSize);
		Rectangle2D colorMappingLegendRegion = new Rectangle2D.Double(rightNamesRegion.getMaxX() + 150, 80, 30, 300);
		Rectangle2D annotationLegendRegion = new Rectangle2D.Double(rightNamesRegion.getMaxX() + 150,
				colorMappingLegendRegion.getMaxY() + 36, 30, 300);
		Point2D intersectLinesStartPoint = new Point2D.Double(start - 60, matrixRegion.getY() + 100);
		

		DefaultLinerColorMapper mapper = dataModel.mapper;
		GradientColorHolder gradientColorHolder = mapper.getGradientColorHolder();
		TreeBasedTable<String, String, Double> table = dataModel.table;

		linerColorLegendPainter = new LinerColorLegendPainter(gradientColorHolder, 1, -1, colorMappingLegendRegion);

		List<String> names = dataModel.names;

		List<Triple<String, String, Color>> listOfRightAnnotation = dataModel.listOfRightAnnotation;
		
		
		Set<Pair<String, Color>> listOfItems = Sets.newLinkedHashSet();
		for (Triple<String, String, Color> triple : listOfRightAnnotation) {
			listOfItems.add(Pair.of(triple.getMiddle(), triple.getRight()));
		}
		
		
		CategloryLegendPainter categloryLegendPainter = new CategloryLegendPainter(
				(int) annotationLegendRegion.getWidth(), annotationLegendRegion,
				listOfItems);

		int nameSize = names.size();

		// Draw the cells
		double cellWidth = matrixRegion.getWidth() / (nameSize);
		double cellHeight = matrixRegion.getHeight() / (nameSize);

		for (int i = 0; i < nameSize; i++) {
			for (int j = 0; j < i; j++) {
				Double value = table.get(names.get(i), names.get(j));
				Color mappedColor = mapper.mapColor(value);

				double xx = matrixRegion.getX() + cellWidth * i;
				double yy = matrixRegion.getY() + cellHeight * j;

				double ratio = FastMath.abs(value);

				g2d.setColor(Color.gray);
				rectangleDrawUtil.setFrame(xx, yy, cellWidth, cellHeight);
				g2d.draw(rectangleDrawUtil);

				g2d.setColor(mappedColor);
				double centerX = rectangleDrawUtil.getCenterX();
				double centerY = rectangleDrawUtil.getCenterY();
				rectangleDrawUtil.setFrameFromCenter(centerX, centerY, centerX + 0.5 * ratio * cellWidth,
						centerY + 0.5 * ratio * cellHeight);
				g2d.fill(rectangleDrawUtil);

			}
		}

		g2d.setColor(Color.black);
		g2d.setFont(namesFont);
		double halfFontHeight = 0.5 * g2d.getFontMetrics().getHeight();
		final int stringGap = 7;
		for (int i = 0; i < nameSize; i++) {
			float yy = (float) (matrixRegion.getY() + cellHeight * (i + 0.5) + halfFontHeight);
			float xx = (float) rightNamesRegion.getX() + stringGap;
			g2d.drawString(names.get(i), xx, yy);
		}

		double radians60 = FastMath.toRadians(60);
		for (int i = 0; i < nameSize; i++) {
			float yy = (float) (topNamesRegion.getY() - stringGap);
			float xx = (float) (topNamesRegion.getX() + cellWidth * (i + 0.5) + stringGap);

			g2d.rotate(-radians60, xx, yy);
			g2d.drawString(names.get(i), xx, yy);
			g2d.rotate(radians60, xx, yy);
		}

		// 斜线
		Map<String, Point2D> names2diagnalLocation = Maps.newHashMap();
		for (int i = 0; i < nameSize; i++) {
			String string = names.get(i);

			double xx = matrixRegion.getX() + cellWidth * i;
			double yy = matrixRegion.getY() + cellHeight * i;
			rectangleDrawUtil.setFrame(xx, yy, cellWidth, cellHeight);
			double centerX = rectangleDrawUtil.getCenterX();
			double centerY = rectangleDrawUtil.getCenterY();

			names2diagnalLocation.put(string, new Point2D.Double(centerX, centerY));
		}

		// 图例
		linerColorLegendPainter.painting(g2d);

		//

		List<HashBasedTable<String, String, Double>> connectingLineRelationships = dataModel.connectingLineRelationships;
		Stroke oldStroke = g2d.getStroke();
		for (HashBasedTable<String, String, Double> iterator : connectingLineRelationships) {
			Map<String, Map<String, Double>> intersecttionEntites = iterator.rowMap();
			int index = 0;

			Set<Entry<String, Map<String, Double>>> entrySet = intersecttionEntites.entrySet();
			int size = entrySet.size();
			int intersectLineSpace = 80;
			{
				double xStart = matrixRegion.getX();
				double xEnd = xStart + 0.9 * matrixRegion.getWidth();

				intersectLineSpace = (int) ((xEnd - xStart) / size);

			}

			FontMetrics fontMetrics = g2d.getFontMetrics();
			for (Entry<String, Map<String, Double>> row2namesMap : entrySet) {

				double xx = intersectLinesStartPoint.getX() + index * intersectLineSpace;
				double yy = intersectLinesStartPoint.getY() + index * intersectLineSpace;

				Point2D.Double startPoint = new Point2D.Double(xx, yy);

				Map<String, Double> values = row2namesMap.getValue();
				String entity = row2namesMap.getKey();
				g2d.setColor(Color.gray);
				for (Entry<String, Double> diagValue : values.entrySet()) {
					Point2D dignalNameLocation = names2diagnalLocation.get(diagValue.getKey());

					Double value = diagValue.getValue();
					Line2D.Double double1 = new Line2D.Double(startPoint, dignalNameLocation);
					BasicStroke stroke = EGPSStrokeUtil.getStroke(value * 5);
					g2d.setStroke(stroke);
					g2d.draw(double1);
				}

				int entityRadius = 8;
				circularDrawUtil.setFrame(xx - entityRadius, yy - entityRadius, entityRadius + entityRadius,
						entityRadius + entityRadius);

				g2d.setStroke(oldStroke);
				g2d.setColor(Color.blue);
				g2d.draw(circularDrawUtil);
				g2d.setColor(Color.magenta);
				g2d.fill(circularDrawUtil);

				g2d.setColor(Color.black);
				g2d.drawString(entity,
						(float) (circularDrawUtil.getCenterX() - fontMetrics.stringWidth(entity) - entityRadius),
						(float) (circularDrawUtil.getMaxY() + 5));

				index++;
			}
		}
		g2d.setStroke(oldStroke);

		// Diagonal elements
		for (int i = 0; i < nameSize; i++) {
			String string = names.get(i);

			double xx = matrixRegion.getX() + cellWidth * i;
			double yy = matrixRegion.getY() + cellHeight * i;
			rectangleDrawUtil.setFrame(xx, yy, cellWidth, cellHeight);

			double centerX = rectangleDrawUtil.getCenterX();
			double centerY = rectangleDrawUtil.getCenterY();

			int circularSize = (int) (cellWidth * 0.2);
			if (circularSize < 2) {
				circularSize = 2;
			}
			circularDrawUtil.setFrameFromCenter(centerX, centerY, centerX + circularSize, centerY + circularSize);
			g2d.setColor(Color.blue);
			g2d.draw(circularDrawUtil);
			g2d.setColor(Color.magenta);
			g2d.fill(circularDrawUtil);
			names2diagnalLocation.put(string, new Point2D.Double(centerX, centerY));
		}

		int index = 0;
		for (Triple<String, String, Color> annotation : listOfRightAnnotation) {
			g2d.setColor(annotation.getRight());
			rectangleDrawUtil.setFrame(rightAnnotationDrawArea.getX(),
					rightAnnotationDrawArea.getY() + index * cellHeight, rightAnnotationDrawArea.getWidth(),
					cellHeight);
			g2d.fill(rectangleDrawUtil);
			index++;
		}

		g2d.setColor(Color.black);
		// 箭头
		EGPSDrawUtil.drawArrow(g2d,
				new Line2D.Double(rightAnnotationDrawArea.getX() - 12, rightAnnotationDrawArea.getY(),
						rightAnnotationDrawArea.getX() - 12, rightAnnotationDrawArea.getMaxY()),
				20);
		// 图例
		categloryLegendPainter.paint(g2d);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean rightMouseButton = SwingUtilities.isRightMouseButton(e);
		if (rightMouseButton) {
//			getScrollPane().revalidate();
			repaint();
		} else {

		}

	}

	private JScrollPane getScrollPane() {
		// 获取父组件
		Container parent = getParent();

		while (parent != null) {
			if (parent instanceof JScrollPane) {
				return (JScrollPane) parent; // 转换并返回
			}
			parent = parent.getParent(); // 向上移动到父组件
		}
		return null; // 未找到 JScrollPane
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
	public void mouseWheelMoved(MouseWheelEvent e) {

	}

}
