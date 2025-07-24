import com.jidesoft.swing.JideBoxLayout;
import javax.swing.*;
import java.awt.*;

public class JideBoxLayoutTest {

    public static void main(String[] args) {
        // 在事件调度线程中创建图形界面
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // 创建主窗口
        JFrame frame = new JFrame("JideBoxLayout Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);

        // 创建面板并设置 JideBoxLayout 布局（水平方向）
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS, 20)); // 水平排列，等价于参数 (panel, 0, 6)

        // 添加第一个按钮：FIX
        JButton button = new JButton("FIX");
        button.setPreferredSize(new Dimension(60, 60));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.add(button, JideBoxLayout.FIX); // 固定大小

        // 添加第二个按钮：FLEX
        button = new JButton("FLEX");
        button.setPreferredSize(new Dimension(120, 60));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.add(button, JideBoxLayout.FLEXIBLE); // 可伸缩但优先级低

        // 添加第三个按钮：VARY
        button = new JButton("VARY");
        button.setPreferredSize(new Dimension(120, 60));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.add(button, JideBoxLayout.VARY); // 可伸缩且优先级高

        // 将面板加入到窗口中
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}