package graphic.engine.guirelated;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

public class GradientColorHolder implements Cloneable{
	
	private float[] dist;
	private Color[] colors;
	private int[] pallet;

	@Override
	protected GradientColorHolder clone() throws CloneNotSupportedException {
		GradientColorHolder ret = new GradientColorHolder();
		ret.dist = this.dist.clone();
		ret.colors = this.colors.clone();
		ret.pallet = this.pallet.clone();
		
		return ret;
	}
	public GradientColorHolder() {
		setDefaultColorScheme();
	}
	
	public void setDefaultColorScheme() {
		dist = new float[] { 0.0f, 0.5f, 1.0f };
		colors = new Color[]{ Color.blue,Color.white,Color.red};
		pallet = makeGradientPallet(dist, colors);
	}
	
	public void setColorScheme(float[] dist,Color[] colors) {
		this.dist = dist;
		this.colors = colors;
		pallet = makeGradientPallet(dist, colors);
	}
	private int[] makeGradientPallet(float[] dist, Color[] colors) {
		
		//我不知道为什么，这里的颜色要反一下才行
		
		
		BufferedImage image = new BufferedImage(100, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		Point start = new Point(0, 0);
		Point end = new Point(99, 0);
		
		
		
		LinearGradientPaint linearGradientPaint = new LinearGradientPaint(start, end, dist, colors);
		g2.setPaint(linearGradientPaint);
		g2.fillRect(0, 0, 100, 1);
		g2.dispose();

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
	
	public Color getColorFromPallet(double v) {
		if (v < 0f || v > 1f) {
			throw new IllegalArgumentException("Parameter outside of expected range");
		}
		int length = pallet.length;
		int i = (int) (length * v);
		return new Color(pallet[ length - 1 - i]);
	}
	
	public float[] getDist() {
		return dist;
	}
	public Color[] getColors() {
		return colors;
	}

}
