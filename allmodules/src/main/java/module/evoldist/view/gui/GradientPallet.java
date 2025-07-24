package module.evoldist.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GradientPallet extends JPanel{
	private int[] pallet;
	
	private float[] dist = { 0.0f, 0.5f, 1.0f };
	private Color[] colors;
	
	final int x = 5;
	final int y = 7;
	final int width = 20;
	final int height = 80;
	private LinearGradientPaint linearGradientPaint;
	
	public GradientPallet() {
		exampleCorlorPattern();
		this.setPreferredSize(new Dimension(80, 110));
		this.setBackground(Color.WHITE);
	}
	
	private int[] makeGradientPallet(float[] dist, Color[] colors) {
		BufferedImage image = new BufferedImage(100, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		Point start = new Point(0, 0);
		Point end = new Point(99, 0);
		
		LinearGradientPaint linearGradientPaint = new LinearGradientPaint(start, end, dist, colors);
		g2.setPaint(linearGradientPaint);
		g2.fillRect(0, 0, 100, 1);
		g2.dispose();

		makeGradiantPaintForPanel();
		
		int width = image.getWidth(null);
		int[] pallet = new int[width];
		PixelGrabber pg = new PixelGrabber(image, 0, 0, width, 1, pallet, 0, width);
		try {
			pg.grabPixels();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return pallet;
	}

	private void makeGradiantPaintForPanel() {
		

		Point start = new Point(x, y);
		Point end = new Point(x, y + height);

		MultipleGradientPaint.CycleMethod cycleMethod = MultipleGradientPaint.CycleMethod.NO_CYCLE;
		// need to reverse colors
		linearGradientPaint = new LinearGradientPaint(start, end, dist, reverseArray(colors),
				cycleMethod);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2  = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		
		g2.setPaint(linearGradientPaint);
		g2.fill(new RoundRectangle2D.Double(x, y, width, height, 10, 10));
		g2.setPaint(Color.black);
		g2.setFont(this.getFont().deriveFont(12.0f));
		g2.drawString("High", x + width + 10, y + 20);
		g2.drawString("Low", x + width + 10, y + height - 10);
		g2.dispose();
		
	}
	
	private Color[] reverseArray(Color[] arr) {
		List<Color> list = Arrays.asList(arr);
		Collections.reverse(list);
		return (Color[]) list.toArray();
	}

	public void exampleCorlorPattern() {
		dist = new float[] { 0.0f, 0.5f, 1.0f };
		colors = new Color[]{ Color.WHITE,Color.YELLOW,Color.RED };

		pallet = makeGradientPallet(dist, colors);
	}
	
	/**
	 * @param style : 1. BLUE WHITE RED 2.GREEN BLACK RED 3. WHITE YELLOW RED  
	 */
	public void changeCorlorPattern(int style) {
		if (style == 1) {
			colors = new Color[]{ Color.BLUE,Color.WHITE,Color.RED };
		}else if (style == 2) {
			colors = new Color[]{ Color.GREEN,Color.BLACK,Color.RED };
		}else {
			colors = new Color[]{ Color.WHITE,Color.YELLOW,Color.RED };
		}
		pallet = makeGradientPallet(dist,colors);
	}
	
	
	public Color getColorFromPallet(double v) {
		if (v < 0f || v > 1f) {
			throw new IllegalArgumentException("Parameter outside of expected range");
		}
		
		if (pallet == null) {
			exampleCorlorPattern();
		}
		
		int i = (int) (pallet.length * v);
		int max = pallet.length - 1;
		int index = Math.min(Math.max(i, 0), max);
		return new Color(pallet[index]);
	}
	
//	public static void main(String s[]) {
//		JFrame f = new JFrame("");
//		f.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				System.exit(0);
//			}
//		});
//		JPanel applet = new GradientPallet();
//		f.getContentPane().add(applet);
//		f.setSize(100, 150);
//		f.setVisible(true);
//	}
}