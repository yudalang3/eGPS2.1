package api.rpython;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestJFrame {

    public static void test1(String content) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("JPype Swing @ Notebook");
            f.setSize(400, 300);

            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            jPanel.add(new JLabel("Hello from Java Swing!"), BorderLayout.CENTER);
            jPanel.add(new JLabel("What you say is: " + content), BorderLayout.SOUTH);

            f.add(jPanel);

            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        });
    }

    public static byte[] test_picture(int width, int height) {
        return renderToBytes(width,height);
    }

    // 直接返回 PNG 编码的字节数组
    private static byte[] renderToBytes(int width, int height)  {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 背景
        g2d.setColor(new Color(200, 240, 255));
        g2d.fillRect(0, 0, width, height);

        int cx = width / 2;
        int cy = height / 2;

        // 龟壳
        g2d.setColor(new Color(34, 139, 34));
        g2d.fill(new Ellipse2D.Double(cx - 60, cy - 40, 120, 80));

        // 头
        g2d.setColor(new Color(85, 170, 85));
        g2d.fill(new Ellipse2D.Double(cx - 20, cy - 80, 40, 40));

        // 四腿
        g2d.fill(new Ellipse2D.Double(cx - 80, cy - 30, 30, 30));
        g2d.fill(new Ellipse2D.Double(cx + 50, cy - 30, 30, 30));
        g2d.fill(new Ellipse2D.Double(cx - 80, cy +  0, 30, 30));
        g2d.fill(new Ellipse2D.Double(cx + 50, cy +  0, 30, 30));

        // 尾巴
        g2d.fill(new Ellipse2D.Double(cx - 10, cy + 40, 20, 20));

        // 壳纹理
        g2d.setColor(new Color(0, 100, 0));
        g2d.setStroke(new BasicStroke(2f));
        g2d.draw(new Ellipse2D.Double(cx - 60, cy - 40, 120, 80));
        g2d.drawLine(cx, cy - 40, cx, cy + 40);
        g2d.drawLine(cx - 60, cy, cx + 60, cy);

        // 眼睛
        g2d.setColor(Color.WHITE);
        g2d.fill(new Ellipse2D.Double(cx - 10, cy - 75, 10, 10));
        g2d.fill(new Ellipse2D.Double(cx + 0,  cy - 75, 10, 10));
        g2d.setColor(Color.BLACK);
        g2d.fill(new Ellipse2D.Double(cx - 7, cy - 72, 5, 5));
        g2d.fill(new Ellipse2D.Double(cx + 3, cy - 72, 5, 5));

        g2d.dispose();

        // 写到内存
        ByteArrayOutputStream bass = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", bass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bass.toByteArray();
    }
}
