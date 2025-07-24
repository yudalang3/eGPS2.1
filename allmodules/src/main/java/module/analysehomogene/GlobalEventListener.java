package module.analysehomogene;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GlobalEventListener implements AWTEventListener {
    @Override
    public void eventDispatched(AWTEvent event) {

		Toolkit toolkit = Toolkit.getDefaultToolkit();

		System.out.println(event + "\tEvent received: " + event);

		boolean isSystemGenerated;
		try {
			isSystemGenerated = isSystemGenerated(event);
			System.out.println("Is system generated? " + isSystemGenerated);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	private static boolean isSystemGenerated(AWTEvent event) throws Exception {
		// 获取 AWTEvent 类的 isSystemGenerated 字段
		Field field = AWTEvent.class.getDeclaredField("isSystemGenerated");
		field.setAccessible(true); // 设置可访问
		return field.getBoolean(event); // 获取字段值
	}

    public static void main(String[] args) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
		long eventMask = AWTEvent.MOUSE_EVENT_MASK;
//		long eventMask = AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK
//				| AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK
//				| AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.ACTION_EVENT_MASK | AWTEvent.ADJUSTMENT_EVENT_MASK
//				| AWTEvent.ITEM_EVENT_MASK | AWTEvent.TEXT_EVENT_MASK | AWTEvent.INPUT_METHOD_EVENT_MASK
//				| AWTEvent.PAINT_EVENT_MASK | AWTEvent.INVOCATION_EVENT_MASK | AWTEvent.HIERARCHY_EVENT_MASK
//				| AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK;
		toolkit.addAWTEventListener(new GlobalEventListener(), eventMask);

        // 创建一个简单的窗口来触发事件
		JFrame frame = new JFrame("Test Frame");
		frame.add(new JButton("Click me"));
        frame.setSize(300, 200);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}