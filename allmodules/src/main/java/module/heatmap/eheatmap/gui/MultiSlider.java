package module.heatmap.eheatmap.gui;

import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSliderUI;

public class MultiSlider extends JComponent {
	public static void main(String[] args) { // main method just for showing a usage example
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
		
		JFrame f = new JFrame();
		final MultiSlider slider = new MultiSlider();
		slider.setValue(0, 80);
		slider.addValue(20);
		f.getContentPane().add(slider);
		f.setSize(200, 100);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	public MultiSlider() {
		super.setLayout(null);
		addSlider(0);
	}

	public void setValue(int slider, int value) {
		((JSlider) getComponent(slider)).setValue(value);
	}

	public void addValue(int value) {
		addSlider(value);
	}

	@Override
	public boolean isOptimizedDrawingEnabled() {
		return false;
	}

	@Override
	public void doLayout() {
		Insets i = getInsets();
		int x = i.left, y = i.top, width = getWidth() - x - i.right, height = getHeight() - y - i.bottom;
		for (int ix = 0, n = getComponentCount(); ix < n; ix++)
			getComponent(ix).setBounds(x, y, width, height);
	}

	class SubSlider extends JSlider {
		private SubSlider active;

		@Override
		protected void processMouseEvent(MouseEvent e) {
			SubSlider sl = getClosestSlider(e);
			if (e.getID() == MouseEvent.MOUSE_PRESSED)
				active = sl;
			else if (e.getID() == MouseEvent.MOUSE_RELEASED)
				active = null;
			if (e.getID() == MouseEvent.MOUSE_CLICKED) {
				if (sl == null && e.getClickCount() == 1)
					addSlider(e.getPoint());
				else if (sl != null && e.getClickCount() == 2) {
					removeSlider(sl);
					return;
				}
			}
			if (sl != null)
				sl.realProcessMouseEvent(e);
		}

		private void realProcessMouseEvent(MouseEvent e) {
			e.setSource(this);
			super.processMouseEvent(e);
		}

		@Override
		protected void processMouseMotionEvent(MouseEvent e) {
			if (e.getID() == MouseEvent.MOUSE_MOVED)
				toAllSliders(e);
			else {
				if (active == null)
					active = getClosestSlider(e);
				if (active != null)
					active.realProcessMouseMotionEvent(e);
			}
		}

		private void realProcessMouseMotionEvent(MouseEvent e) {
			e.setSource(this);
			super.processMouseMotionEvent(e);
		}
	}

	final void toAllSliders(MouseEvent e) {
		for (int ix = 0, n = getComponentCount(); ix < n; ix++)
			((SubSlider) getComponent(ix)).realProcessMouseMotionEvent(e);
	}

	public void removeSlider(SubSlider sl) {
		if (getComponentCount() <= 1)
			return;// must keep the last slider
		remove(sl);
		JSlider slider = (JSlider) getComponent(getComponentCount() - 1);
		slider.setOpaque(true);
		slider.setPaintTrack(true);
		revalidate();
		repaint();
	}

	final SubSlider getClosestSlider(MouseEvent e) {
		SubSlider s = (SubSlider) getComponent(0);
		BasicSliderUI bsUI = (BasicSliderUI) s.getUI();
		int value = bsUI.valueForXPosition(e.getX());
		if (Math.abs(s.getValue() - value) <= 1)
			return s;
		for (int ix = 1, n = getComponentCount(); ix < n; ix++) {
			s = (SubSlider) getComponent(ix);
			if (Math.abs(s.getValue() - value) <= 1)
				return s;
		}
		return null;
	}

	void addSlider(Point point) {
		BasicSliderUI bsUI = (BasicSliderUI) ((JSlider) getComponent(0)).getUI();
		addSlider(bsUI.valueForXPosition(point.x));
	}

	void addSlider(int value) {
		final JSlider slider = new SubSlider();
		slider.setFocusable(false);
		slider.setValue(value);
		if (getComponentCount() != 0) {
			slider.setOpaque(false);
			slider.setPaintTrack(false);
		}
		super.add(slider, 0);
		revalidate();
		repaint();
	}
}