package module.remnant.datapanel;

import java.awt.Color;

/**
 * Pls see the website for information!
 * http://www.java2s.com/Code/Java/Swing-JFC/BevelBorder.htm annotation:
 * yudalang
 */
public class MyBevelBorder extends javax.swing.border.BevelBorder {
		private static final long serialVersionUID = 2615078212570654956L;

		public MyBevelBorder(int arg0) {
			super(arg0);
		}

		protected void paintLoweredBevel(java.awt.Component c, java.awt.Graphics g, int x, int y, int width,
				int height) {
			Color oldColor = g.getColor();
			int h = height;
			int w = width;

			g.translate(x, y);

			g.setColor(getShadowInnerColor(c));
			g.drawLine(w - 1, 1, w - 1, h - 2); // vertical, new
			g.drawLine(1, h - 1, w - 1, h - 1); // horizontal, new

			g.setColor(getShadowOuterColor(c));
			g.drawLine(w - 2, 2, w - 2, h - 3); // vertical, new
			g.drawLine(2, h - 2, w - 2, h - 2); // horizontal, new

			g.setColor(getHighlightOuterColor(c));
			g.drawLine(1, 0, w - 1, 0); // horizontal, new
			g.drawLine(0, 0, 0, h - 1); // vertical, new

			g.setColor(getHighlightInnerColor(c));
			g.drawLine(2, 1, w - 2, 1); // horizontal, new
			g.drawLine(1, 1, 1, h - 2); // vertical, new

			// g.setColor(getShadowInnerColor(c));
			// g.drawLine(w-1, 1, w-1, h-2); // vertical, new
			// g.drawLine(1, 0, w-1, 0); // horizontal
			//
			// g.setColor(getShadowOuterColor(c));
			// g.drawLine(w-2, 2, w-2, h-3); // vertical, new
			// g.drawLine(2, 1, w-2, 1); // horizontal
			//
			// g.setColor(getHighlightOuterColor(c));
			// g.drawLine(1, h-1, w-1, h-1); // horizontal
			// g.drawLine(0, 0, 0, h-1); // vertical, new
			//
			// g.setColor(getHighlightInnerColor(c));
			// g.drawLine(2, h-2, w-2, h-2); // horizontal
			// g.drawLine(1, 1, 1, h-2); // vertical, new

			///////////// Original BevelBorder /////////////////
			// g.setColor(getShadowInnerColor(c)); // Original BevelBorder
			// g.drawLine(0, 0, 0, h-1); // vertical
			// g.drawLine(1, 0, w-1, 0); // horizontal
			//
			// g.setColor(getShadowOuterColor(c)); // Original BevelBorder
			// g.drawLine(1, 1, 1, h-2); // vertical
			// g.drawLine(2, 1, w-2, 1); // horizontal
			//
			// g.setColor(getHighlightOuterColor(c)); // Original BevelBorder
			// g.drawLine(1, h-1, w-1, h-1); // horizontal
			// g.drawLine(w-1, 1, w-1, h-2); // vertical
			//
			// g.setColor(getHighlightInnerColor(c)); // Original BevelBorder
			// g.drawLine(2, h-2, w-2, h-2); // horizontal
			// g.drawLine(w-2, 2, w-2, h-3); // vertical

			g.translate(-x, -y);
			g.setColor(oldColor);
		}
	}