package module.pill.graphics;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.RectangularShape;

/**
 * 我现在用JAVA在Swing中绘制两个矩形之间的连线，但是我发现直接连线的话无法直接得到比较美观的结果。 因为这些线条是直接连接到两个矩形中心的。
 * 而我们需要一个能够连接到矩形附近的比较美观的地方，而且是自动的。没有有比较好的算法？ 例如假设我已经有两个矩形了
 * rect1和rect2，如果连接一个箭头呢。 矩形的位置会变，rect2可能在各个位置，要求美观大方。
 * 
 * <br>
 * 竟然还是GPT3.5效果最好
 */
public class ArrowLineDrawerByGPT35 {

    private static final int ARROW_SIZE = 10;
	private static final int INHIBITION_SIZE = 8;
	private static final int OFF_SET = 4;

	private RectangularShape rect1;
	private RectangularShape rect2;


	public void setRects(RectangularShape rect1, RectangularShape rect2) {
		this.rect1 = rect1;
		this.rect2 = rect2;
	}

	protected void draw(Graphics2D g2d, boolean activate, String string4drawing) {

		// 计算矩形1的边缘点
		Point rect1EdgePoint = getEdgePoint(rect1, rect2.getCenterX(), rect2.getCenterY());

		// 计算矩形2的边缘点
		Point rect2EdgePoint = getEdgePoint(rect2, rect1.getCenterX(), rect1.getCenterY());

		// 绘制连线
		g2d.drawLine(rect1EdgePoint.x, rect1EdgePoint.y, rect2EdgePoint.x, rect2EdgePoint.y);

		// 绘制箭头
		if (activate) {
			drawArrow(g2d, rect1EdgePoint.x, rect1EdgePoint.y, rect2EdgePoint.x, rect2EdgePoint.y);
		} else {
			drawInhibitor(g2d, rect1EdgePoint.x, rect1EdgePoint.y, rect2EdgePoint.x, rect2EdgePoint.y);
		}

		if (string4drawing != null) {
			FontMetrics fontMetrics = g2d.getFontMetrics();
			double centerX = (rect1EdgePoint.getX() + rect2EdgePoint.getX()) * 0.5;
			double centerY = (rect1EdgePoint.getY() + rect2EdgePoint.getY()) * 0.5;

			int stringWidth = fontMetrics.stringWidth(string4drawing);
			int stringHeight = fontMetrics.getHeight();

			// 计算绘制字符串的起始位置
			int startX = (int) (centerX - stringWidth / 2);
			int startY = (int) (centerY + stringHeight / 2);

			// 绘制字符串
			g2d.drawString(string4drawing, startX, startY);
		}
	}

	private void drawInhibitor(Graphics2D g2d, int startX, int startY, int endX, int endY) {
		double angle = Math.atan2(endY - startY, endX - startX);
		double radianOfBend = Math.PI / 2;
		int x1 = (int) (endX - INHIBITION_SIZE * Math.cos(angle - radianOfBend));
		int y1 = (int) (endY - INHIBITION_SIZE * Math.sin(angle - radianOfBend));
		int x2 = (int) (endX - INHIBITION_SIZE * Math.cos(angle + radianOfBend));
		int y2 = (int) (endY - INHIBITION_SIZE * Math.sin(angle + radianOfBend));

		g2d.drawLine(endX, endY, x1, y1);
		g2d.drawLine(endX, endY, x2, y2);

	}

	/**
	 * 注意这里返回的点坐标是 rect 这个矩形位置的 endPoint而不是target那里的point
	 * 
	 * @param rect
	 * @param targetX
	 * @param targetY
	 * @return
	 */
	private Point getEdgePoint(RectangularShape rect, double targetX, double targetY) {
		// 计算矩形中心到目标点的向量
		double centerX = rect.getCenterX();
		double centerY = rect.getCenterY();
		double dx = targetX - centerX;
		double dy = targetY - centerY;
		double halfWidth = 0.5 * rect.getWidth() + OFF_SET;
		double halfHeight = 0.5 * rect.getHeight() + OFF_SET;

		// 计算矩形边缘与连线的交点
		int x, y;
		if (Math.abs(dx) > Math.abs(dy)) {
			// 交点在左右边缘
			x = (int) (centerX + (dx > 0 ? halfWidth : -halfWidth));
			y = (int) (centerY + dy * halfWidth / Math.abs(dx));
		} else {
			// 交点在上下边缘
			x = (int) (centerX + dx * halfHeight / Math.abs(dy));
			y = (int) (centerY + (dy > 0 ? halfHeight : -halfHeight));
		}

		return new Point(x, y);
	}


    private void drawArrow(Graphics2D g2d, int startX, int startY, int endX, int endY) {
        double angle = Math.atan2(endY - startY, endX - startX);
		double radianOfBend = Math.PI / 4;
		int x1 = (int) (endX - ARROW_SIZE * Math.cos(angle - radianOfBend));
		int y1 = (int) (endY - ARROW_SIZE * Math.sin(angle - radianOfBend));
		int x2 = (int) (endX - ARROW_SIZE * Math.cos(angle + radianOfBend));
		int y2 = (int) (endY - ARROW_SIZE * Math.sin(angle + radianOfBend));

		g2d.fillPolygon(new int[] { endX, x1, x2 }, new int[] { endY, y1, y2 }, 3);
    }

}
