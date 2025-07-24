import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;

public class FlatLafPopupDemo {
    public static void main(String[] args) {
        FlatIntelliJLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FlatLaf Popup Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new FlowLayout());

            JButton btn = new JButton("Show Popup Hint");
            frame.add(btn);

            btn.addActionListener(e -> showFlatPopup(btn));

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    static void showFlatPopup(Component owner) {
        JPanel popupContent = new JPanel(new BorderLayout());
        popupContent.setBorder(BorderFactory.createEmptyBorder(
                UIScale.scale(10), UIScale.scale(15), UIScale.scale(10), UIScale.scale(15)));
        popupContent.setBackground(UIManager.getColor("Panel.background"));
        popupContent.add(new JLabel("<html><b>FlatLaf Popup!</b><br>带阴影和圆角的提示</html>"), BorderLayout.CENTER);

        // FlatLaf 3.x 阴影&圆角属性
        popupContent.putClientProperty("Popup.dropShadowPainted", true);
        popupContent.putClientProperty("Popup.borderCornerRadius", UIScale.scale(12));

        Point pt = owner.getLocationOnScreen();
        int x = pt.x + owner.getWidth() + 10;
        int y = pt.y;

        Popup popup = PopupFactory.getSharedInstance().getPopup(owner, popupContent, x, y);
        popup.show();

        // 自动关闭
        new Timer(3000, e -> popup.hide()).start();
    }
}
