package api.rpython;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for demonstrating Java Swing GUI components and image rendering capabilities.
 * This class provides methods to create simple GUI windows and render graphics to byte arrays.
 */
public class TestJFrame {

    /**
     * Create and display a simple Swing window with a message
     * @param content The message content to display in the window
     */
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

    /**
     * Generate a PNG image of a turtle and return it as a byte array
     * @param width Width of the image
     * @param height Height of the image
     * @return Byte array containing the PNG-encoded image data
     */
    public static byte[] test_picture(int width, int height) {
        return renderToBytes(width,height);
    }

    /**
     * Render a turtle image to a PNG byte array
     * @param width Width of the image
     * @param height Height of the image
     * @return Byte array containing the PNG-encoded image data
     */
    private static byte[] renderToBytes(int width, int height)  {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2d.setColor(new Color(200, 240, 255));
        g2d.fillRect(0, 0, width, height);

        int cx = width / 2;
        int cy = height / 2;

        // Turtle shell
        g2d.setColor(new Color(34, 139, 34));
        g2d.fill(new Ellipse2D.Double(cx - 60, cy - 40, 120, 80));

        // Head
        g2d.setColor(new Color(85, 170, 85));
        g2d.fill(new Ellipse2D.Double(cx - 20, cy - 80, 40, 40));

        // Four legs
        g2d.fill(new Ellipse2D.Double(cx - 80, cy - 30, 30, 30));
        g2d.fill(new Ellipse2D.Double(cx + 50, cy - 30, 30, 30));
        g2d.fill(new Ellipse2D.Double(cx - 80, cy +  0, 30, 30));
        g2d.fill(new Ellipse2D.Double(cx + 50, cy +  0, 30, 30));

        // Tail
        g2d.fill(new Ellipse2D.Double(cx - 10, cy + 40, 20, 20));

        // Shell pattern
        g2d.setColor(new Color(0, 100, 0));
        g2d.setStroke(new BasicStroke(2f));
        g2d.draw(new Ellipse2D.Double(cx - 60, cy - 40, 120, 80));
        g2d.drawLine(cx, cy - 40, cx, cy + 40);
        g2d.drawLine(cx - 60, cy, cx + 60, cy);

        // Eyes
        g2d.setColor(Color.WHITE);
        g2d.fill(new Ellipse2D.Double(cx - 10, cy - 75, 10, 10));
        g2d.fill(new Ellipse2D.Double(cx + 0,  cy - 75, 10, 10));
        g2d.setColor(Color.BLACK);
        g2d.fill(new Ellipse2D.Double(cx - 7, cy - 72, 5, 5));
        g2d.fill(new Ellipse2D.Double(cx + 3, cy - 72, 5, 5));

        g2d.dispose();

        // Write to memory
        ByteArrayOutputStream bass = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", bass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bass.toByteArray();
    }
}