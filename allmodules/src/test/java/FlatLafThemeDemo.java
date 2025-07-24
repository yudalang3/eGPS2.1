import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;

public class FlatLafThemeDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 默认主题
            FlatLightLaf.setup();

            JFrame frame = new JFrame("FlatLaf Theme Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new FlowLayout());

            JButton btnLight = new JButton("Light Theme");
            JButton btnDark = new JButton("Dark Theme");
            JButton btnIntelliJ = new JButton("IntelliJ Theme");
            JButton btnDarcula = new JButton("Darcula Theme");

            btnLight.addActionListener(e -> switchTheme(new FlatLightLaf(), frame));
            btnDark.addActionListener(e -> switchTheme(new FlatDarkLaf(), frame));
            btnIntelliJ.addActionListener(e -> switchTheme(new FlatIntelliJLaf(), frame));
            btnDarcula.addActionListener(e -> switchTheme(new FlatDarculaLaf(), frame));

            frame.add(btnLight);
            frame.add(btnDark);
            frame.add(btnIntelliJ);
            frame.add(btnDarcula);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void switchTheme(LookAndFeel laf, JFrame frame) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
