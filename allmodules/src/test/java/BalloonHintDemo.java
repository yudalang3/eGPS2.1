import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.*;
import java.util.EnumSet;

import static javax.swing.SwingConstants.*;

public class BalloonHintDemo {

    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FlatLaf 3.6 BalloonBorder Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new FlowLayout());

            JButton btnLeft = new JButton("Hint LEFT");
            JButton btnTop = new JButton("Hint TOP");
            JButton btnRight = new JButton("Hint RIGHT");
            JButton btnBottom = new JButton("Hint BOTTOM");

            frame.add(btnLeft);
            frame.add(btnTop);
            frame.add(btnRight);
            frame.add(btnBottom);

            // 各方向显示不同箭头方向的 BalloonHint
            btnLeft.addActionListener(e -> showBalloonHint(btnLeft, LEFT, "<html><b>LEFT</b><br>箭头指向左侧</html>"));
            btnTop.addActionListener(e -> showBalloonHint(btnTop, TOP, "<html><b>TOP</b><br>箭头指向上侧</html>"));
            btnRight.addActionListener(e -> showBalloonHint(btnRight, RIGHT, "<html><b>RIGHT</b><br>箭头指向右侧</html>"));
            btnBottom.addActionListener(e -> showBalloonHint(btnBottom, BOTTOM, "<html><b>BOTTOM</b><br>箭头指向下侧</html>"));

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * 在某个组件旁边显示带箭头的 Balloon 提示
     */
    static void showBalloonHint(Component owner, int direction, String htmlText) {
        JPanel hintPanel = createBalloonPanel(direction, htmlText);

        // 计算位置
        Point screenPt = owner.getLocationOnScreen();
        int gap = UIScale.scale(8);
        int x = screenPt.x;
        int y = screenPt.y;

        Dimension pref = hintPanel.getPreferredSize();
        switch (direction) {
            case LEFT -> x -= pref.width + gap;
            case TOP -> y -= pref.height + gap;
            case RIGHT -> x += owner.getWidth() + gap;
            case BOTTOM -> y += owner.getHeight() + gap;
        }

        // PopupFactory 弹出
        PopupFactory pf = PopupFactory.getSharedInstance();
        Popup popup = pf.getPopup(owner, hintPanel, x, y);
        popup.show();

        // 自动关闭 5s 后消失
        Timer autoClose = new Timer(5000, e -> popup.hide());
        autoClose.setRepeats(false);
        autoClose.start();
    }

    /**
     * 创建一个带箭头+阴影的 Balloon Panel
     */
    static JPanel createBalloonPanel(int direction, String htmlText) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            public Insets getInsets() {
                // 让内容不贴边
                return UIScale.scale(new Insets(10, 15, 10, 15));
            }
        };
        panel.setOpaque(false); // 由 BalloonBorder 绘制背景
        panel.setBorder(new BalloonBorder(direction,
                UIManager.getColor("Component.borderColor"),
                new Color(255, 255, 255), // 背景
                new Color(0, 0, 0, 80)    // 阴影
        ));

        JLabel label = new JLabel(htmlText);
        panel.add(label, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Got it");
        closeBtn.setFocusable(false);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIScale.scale(10), 0));
        btnPanel.setOpaque(false);
        btnPanel.add(closeBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        return panel;
    }

    /**
     * 带箭头的气泡边框 + FlatDropShadowBorder 阴影
     */
    static class BalloonBorder extends AbstractBorder {

        // 基础尺寸参数（可调）
        private static final int ARC = 12;        // 圆角
        private static final int ARROW_SIZE = 10; // 箭头边长
        private static final int ARROW_XY = 20;   // 箭头离起始边缘距离
        private static final int SHADOW_SIZE = 8; // 阴影厚度

        private final int direction;
        private final Color borderColor;
        private final Color backgroundColor;
        private final FlatDropShadowBorder shadowBorder;

        BalloonBorder(int direction, Color borderColor, Color backgroundColor, Color shadowColor) {
            this.direction = direction;
            this.borderColor = borderColor;
            this.backgroundColor = backgroundColor;

            // 计算阴影 inset，方向不同需要给箭头留空间
            Insets shadowInsets = UIScale.scale(new Insets(
                    SHADOW_SIZE + (direction == TOP ? ARROW_SIZE : 0),
                    SHADOW_SIZE + (direction == LEFT ? ARROW_SIZE : 0),
                    SHADOW_SIZE + (direction == BOTTOM ? ARROW_SIZE : 0),
                    SHADOW_SIZE + (direction == RIGHT ? ARROW_SIZE : 0)
            ));

            // FlatLaf 3.6 阴影新构造：颜色 + inset + 透明度
            shadowBorder = new FlatDropShadowBorder(shadowColor, shadowInsets, 0.5f);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int arrow = UIScale.scale(ARROW_SIZE);
            int shadow = UIScale.scale(SHADOW_SIZE);
            int arc = UIScale.scale(ARC);

            int top = arc + shadow;
            int left = arc + shadow;
            int bottom = arc + shadow;
            int right = arc + shadow;

            switch (direction) {
                case LEFT -> left += arrow;
                case TOP -> top += arrow;
                case RIGHT -> right += arrow;
                case BOTTOM -> bottom += arrow;
            }
            return new Insets(top, left, bottom, right);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arrow = UIScale.scale(ARROW_SIZE);
                int arc = UIScale.scale(ARC);
                int shadow = UIScale.scale(SHADOW_SIZE);
                int xy = UIScale.scale(ARROW_XY);

                // 先画阴影（FlatDropShadowBorder 内部自己绘制渐变阴影）
                shadowBorder.paintBorder(c, g2, x, y, width, height);

                // 计算气泡本体的矩形区域
                int bx = x + shadow;
                int by = y + shadow;
                int bw = width - shadow * 2;
                int bh = height - shadow * 2;

                // 根据方向调整箭头位置
                Shape balloon = createBalloonShape(bw, bh, arc, xy, arrow, direction);

                g2.translate(bx, by);

                // 填充背景
                g2.setColor(backgroundColor);
                g2.fill(balloon);

                // 绘制边框线
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(UIScale.scale(1f)));
                g2.draw(balloon);

            } finally {
                g2.dispose();
            }
        }

        /**
         * 创建气泡形状（矩形 + 箭头）
         */
        private Shape createBalloonShape(int width, int height, int arc, int xy, int arrow, int dir) {
            Shape rect;
            Shape arrowShape;
            switch (dir) {
                case LEFT -> {
                    rect = new RoundRectangle2D.Float(arrow, 0, width - 1 - arrow, height - 1, arc, arc);
                    arrowShape = new Polygon(
                            new int[]{arrow, 0, arrow},
                            new int[]{xy, xy + arrow, xy + arrow * 2},
                            3
                    );
                }
                case TOP -> {
                    rect = new RoundRectangle2D.Float(0, arrow, width - 1, height - 1 - arrow, arc, arc);
                    arrowShape = new Polygon(
                            new int[]{xy, xy + arrow, xy + arrow * 2},
                            new int[]{arrow, 0, arrow},
                            3
                    );
                }
                case RIGHT -> {
                    rect = new RoundRectangle2D.Float(0, 0, width - 1 - arrow, height - 1, arc, arc);
                    int x = width - 1 - arrow;
                    arrowShape = new Polygon(
                            new int[]{x, x + arrow, x},
                            new int[]{xy, xy + arrow, xy + arrow * 2},
                            3
                    );
                }
                case BOTTOM -> {
                    rect = new RoundRectangle2D.Float(0, 0, width - 1, height - 1 - arrow, arc, arc);
                    int y = height - 1 - arrow;
                    arrowShape = new Polygon(
                            new int[]{xy, xy + arrow, xy + arrow * 2},
                            new int[]{y, y + arrow, y},
                            3
                    );
                }
                default -> throw new IllegalArgumentException("Invalid direction");
            }

            Area area = new Area(rect);
            area.add(new Area(arrowShape));
            return area;
        }
    }
}
