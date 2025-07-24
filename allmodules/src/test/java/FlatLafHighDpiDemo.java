import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;

public class FlatLafHighDpiDemo {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FlatLaf DPI Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, UIScale.scale(20), UIScale.scale(20)));

            for (int i = 1; i <= 3; i++) {
                JButton btn = new JButton("Button " + i);
                // 设置圆角属性
                btn.putClientProperty("JButton.buttonType", "roundRect");
                frame.add(btn);
            }

            JLabel label = new JLabel("UIScale example: " + UIScale.scale(50));
            frame.add(label);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
