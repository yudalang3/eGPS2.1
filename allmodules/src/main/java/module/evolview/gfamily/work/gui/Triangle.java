package module.evolview.gfamily.work.gui;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class Triangle {
    private Point2D.Double p1;
    private Point2D.Double p2;
    private Point2D.Double p3;

    private int sideLength = 8;

    private GeneralPath path;

    // 使用三个点构建一个三角形
    public Triangle(Point2D.Double p1) {
        this.p1 = p1;
        double newLocatioX = sideLength / 2;

        double newLocationY = Math.sqrt(sideLength * sideLength - newLocatioX * newLocatioX);

        this.p2 = new Point2D.Double(p1.x - newLocatioX, p1.y - newLocationY);

        this.p3 = new Point2D.Double(p1.x + newLocatioX, p1.y - newLocationY);

        this.path = buildPath();
    }

    // 使用三个点构建一个三角形
    public Triangle(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.path = buildPath();
    }

    // 绘制三角形边
    public void draw(Graphics2D g2d) {
        g2d.draw(path);
    }

    // 填充三角形
    public void fill(Graphics2D g2d) {
        g2d.fill(path);
    }

    // 创建三角形外形的路径
    private GeneralPath buildPath() {
        path = new GeneralPath();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.closePath();

        return path;
    }
}