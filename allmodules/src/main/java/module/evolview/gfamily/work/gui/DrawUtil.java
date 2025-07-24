package module.evolview.gfamily.work.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.util.List;

import egps2.utils.common.model.datatransfer.TwoTuple;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public class DrawUtil {

	public static AffineTransform affineTransform = new AffineTransform();
	
	public static final int size = 7;

	/**
	 * 旋转
	 * 
	 * @param root
	 * @param rotateDegree
	 */
	public static void rotationTransform(GraphicsNode root, int rotateDegree, double currentWidth,
			double currentHeight) {

		double radians = Math.toRadians(rotateDegree);
		affineTransform.setToRotation(radians, 0.5 * currentWidth, 0.5 * currentHeight);

		Point2D.Double src = new Point2D.Double();
		Point2D.Double dest = new Point2D.Double();

		TreeOperationUtil.recursiveIterateTreeIF(root, node -> {

			src.setLocation(node.getXParent(), node.getYParent());
			affineTransform.transform(src, dest);
			node.setXParent(dest.getX());
			node.setYParent(dest.getY());

			src.setLocation(node.getXSelf(), node.getYSelf());
			affineTransform.transform(src, dest);
			node.setXSelf(dest.getX());
			node.setYSelf(dest.getY());
			double angle = node.getAngleIfNeeded() - rotateDegree;
			// System.out.println(node.getAngleIfNeeded() +" "+angle);
			node.setAngleIfNeeded(angle);

		});

	}

	public static BasicStroke getLineStroke(float thickness) {

		float[] dash3 = { 4f, 4f, 4f };
		BasicStroke ret = new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash3, 2f);
		return ret;
	}

	public static void drawRootTip(Graphics2D g2d, GraphicsNode node, TreeLayoutProperties treeLayoutProperties) {
		if (treeLayoutProperties.isShowRoot()) {

			g2d.setColor(Color.gray);
			double x1 = node.getXParent();
			double x2 = node.getXSelf();
			double y1 = node.getYParent();
			double y2 = node.getYSelf();
			Double line = new Double(x1, y1, x2, y2);
			
			g2d.setStroke(new BasicStroke(2f));
			g2d.draw(line);

		}

	}

	public static void drawStringForDebug(Graphics2D g2d, String string, float x1, float y1, float x2, float y2) {

		int stringWidth = g2d.getFontMetrics().stringWidth(string);
		float xx = (float) (0.5 * (x1 + x2) - 0.5 * stringWidth);
		float yy = (float) (0.5 * (y1 + y2) - 5);

		g2d.drawString(string, xx, yy);
	}

	/**
	 * 给定一个坐标，在附近绘制一个圆圈。 现在用来绘制叶子节点的大圆圈。
	 * 
	 * @param g2d
	 * @param x2
	 * @param y2
	 * @param circleRadicus
	 */
	public static void drawRoundClassifiedColor(Graphics2D g2d, int x2, int y2, int circleRadicus) {

		if (circleRadicus <= 0) {
			return;
		}

		int diameter = circleRadicus + circleRadicus;
		g2d.fillOval((x2 - circleRadicus), (y2 - circleRadicus), diameter, diameter);

	}

	public static void drawStringForSimpleDebug(Graphics2D g2d, String string, float x, float y) {
		g2d.drawString(string, x, y);

	}

	public static void paintSelectedRectangularSignatureSolidLine(Graphics2D g2d, double x2, double y2) {
		int xx = (int) x2;
		int yy = (int) y2;
		// g2d.setColor(new Color(112, 142, 173));
//		g2d.setColor(new Color(0,0,182,155));
//		g2d.setColor(new Color(255, 0, 0, 155));

//		Color redColor = Color.decode("#a20a0a");
		g2d.setColor(new Color(235, 10, 10, 150));

//		Stroke oldStroke = g2d.getStroke();
//		BasicStroke dashedStroke = new BasicStroke(1.5f);
//		g2d.setStroke(dashedStroke);

//		g2d.drawOval(xx - size, yy - size, 2 * size, 2 * size);
		g2d.fillOval(xx - size, yy - size, 2 * size, 2 * size);
//		g2d.drawRect(xx - size, yy - size, 2 * size, 2 * size);
//		g2d.fillRect(xx - size, yy - size, 2 * size, 2 * size);
//		g2d.setStroke(oldStroke);

	}

	public static void paintSelectedInterestedNode(Graphics2D g2d, double x2, double y2) {
		int xx = (int) x2;
		int yy = (int) y2;

		g2d.setColor(new Color(36, 150, 221, 150));


		g2d.fillOval(xx - size, yy - size, 2 * size, 2 * size);


	}
	
	/**
	 * 画谱系标记的图例
	 * 
	 * @param list
	 * @param g2d
	 */
	public static void drawLegend4Annotations(double currentWidth, double currentHeight,
			List<TwoTuple<String, Color>> list, Graphics2D g2d) {
		int indexOfLegend = 0;
		final int legendLeftXLocation = (int) (currentWidth - 75);
		final int legnedTopYLocation = 10;
		// Should greater than 10
		final int legendHightInterval = 20;
		final int legendRectWidth = 15;

		int totalWidth4outterRect = 0;
		FontMetrics fontMetrics = g2d.getFontMetrics();
		for (TwoTuple<String, Color> entry : list) {
			g2d.setColor(entry.second);
			int yLocation = legnedTopYLocation + indexOfLegend * legendHightInterval;
			int height = legendHightInterval - 10;
			g2d.fillRect(legendLeftXLocation, yLocation, legendRectWidth, height);

			g2d.setColor(Color.black);

			g2d.drawRect(legendLeftXLocation, yLocation, legendRectWidth, height);
			g2d.drawString(entry.first, legendLeftXLocation + legendRectWidth + 10, yLocation + height);
			indexOfLegend++;

			int stringWidth = fontMetrics.stringWidth(entry.first);
			if (stringWidth > totalWidth4outterRect) {
				totalWidth4outterRect = stringWidth;
			}

		}
		g2d.drawRect(legendLeftXLocation - 10, legnedTopYLocation - 5, 40 + totalWidth4outterRect,
				indexOfLegend * legendHightInterval);

	}

}
