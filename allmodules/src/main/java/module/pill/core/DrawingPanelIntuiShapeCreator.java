package module.pill.core;

import egps2.frame.gui.EGPSSwingUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawingPanelIntuiShapeCreator extends JPanel {

	final static int NUM_OF_ROWS = 51;
	final static int NUM_OF_COLS = 51;
	final static int CENTER_X_INDEX = 25;
	final static int CENTER_Y_INDEX = 25;
	final static Color colorOfLine = Color.decode("#D3D3D3");
	boolean[][] gridsBooleans = new boolean[NUM_OF_ROWS][NUM_OF_COLS];
	private int hInterval;
	private int vInterval;

	private int currentHieght = 0;
	private int currentWidth = 0;

	BasicStroke crossBasicStroke = new BasicStroke(3.0f);

	private Image image;

	class MyMouseActions extends MouseAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int xx = (int) Math.floor(x / hInterval);
			int yy = (int) Math.floor(y / vInterval);

			if (xx >= NUM_OF_ROWS || x < 0) {
				return;
			}
			if (yy >= NUM_OF_COLS || y < 0) {
				return;
			}

			boolean b = gridsBooleans[xx][xx];
			gridsBooleans[xx][yy] = !b;

			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseDragged(e);
		}

	}

	public DrawingPanelIntuiShapeCreator() {
		MyMouseActions action = new MyMouseActions();
		addMouseMotionListener(action);
		addMouseListener(action);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				panelResizedAction();
			}
		});
	}

	private void panelResizedAction() {
		double height2 = getHeight();
		double width2 = getWidth();

		currentHieght = (int) height2;
		currentWidth = (int) width2;
		this.hInterval = (int) Math.floor(width2 / NUM_OF_COLS);
		this.vInterval = (int) Math.floor(height2 / NUM_OF_ROWS);

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		int height2 = this.currentHieght;
		int width2 = this.currentWidth;
		if (height2 == 0) {
			panelResizedAction();
		}

		int centerX = width2 / 2;
		int centerY = height2 / 2;
		
		if (image != null) {
			int imageWidth = image.getWidth(this);
			int imageHeight = image.getHeight(this);
			g2d.drawImage(image, centerX - imageWidth / 2, centerY - imageHeight / 2, this);
		}

		g2d.setColor(Color.magenta);

		g2d.fill3DRect(CENTER_X_INDEX * hInterval, CENTER_Y_INDEX * vInterval, hInterval, vInterval, true);
//		Stroke oldStoke = g2d.getStroke();
//		g2d.setStroke(crossBasicStroke);
//		g2d.drawLine(centerX, centerY - 15, centerX, centerY + 15);
//		g2d.drawLine(centerX - 15, centerY, centerX + 15, centerY);
//
//		g2d.setStroke(oldStoke);

		g2d.setColor(colorOfLine);
		int horizontalW = hInterval * NUM_OF_COLS;
		
		for (int i = 0; i <= NUM_OF_ROWS; i++) {
			int tempY = i * vInterval;
			g2d.drawLine(0, tempY, horizontalW, tempY);
//				g2d.drawString(String.valueOf(i + 1), 0, tempY);
		}

		int verticalH = vInterval * NUM_OF_ROWS;
		// draw the column lines
		for (int i = 0; i <= NUM_OF_COLS; i++) {
			int tempX = i * hInterval;
			g2d.drawLine(tempX, 0, tempX, verticalH);
//			g2d.drawString(String.valueOf(i + 1), tempX, verticalH);
		}

		g2d.setColor(Color.black);
		for (int i = 0; i < NUM_OF_ROWS; i++) {
			for (int j = 0; j < NUM_OF_COLS; j++) {
				if (gridsBooleans[i][j]) {

					int xx = i * hInterval;
					int yy = j * vInterval;
					g2d.fill3DRect(xx, yy, hInterval, vInterval, true);
				}
			}
		}

		g2d.setColor(Color.white);
	}

	public void clearAll() {
		for (int i = 0; i < NUM_OF_ROWS; i++) {
			for (int j = 0; j < NUM_OF_COLS; j++) {
				gridsBooleans[i][j] = false;
			}
		}

		repaint();
	}

	public void loadImageFrom() {
		try {
			image = EGPSSwingUtil.getImageFromClipboard();

			if (image == null) {
				JOptionPane.showMessageDialog(this, "You need to copy a image.");
				return;
			}

			repaint();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveCurrentShape(String string) {

		if (string.isEmpty()) {
			EGPSSwingUtil.promote(3, "Title required", "Please enter the title in the Text field.", this);
			return;
		}

		List<Integer> xInteger = new ArrayList<>();
		List<Integer> yInteger = new ArrayList<>();

		// Need to use a special iteration strategy
		for (int j = 0; j < NUM_OF_COLS; j++) {
			for (int i = CENTER_X_INDEX; i < NUM_OF_ROWS; i++) {
				if (gridsBooleans[i][j]) {
					xInteger.add(i - CENTER_X_INDEX);
					yInteger.add(j - CENTER_Y_INDEX);

				}
			}
		}
		for (int j = NUM_OF_COLS - 1; j >= 0; j--) {
			for (int i = 0; i < CENTER_X_INDEX; i++) {
				if (gridsBooleans[i][j]) {
					xInteger.add(i - CENTER_X_INDEX);
					yInteger.add(j - CENTER_Y_INDEX);

				}
			}
		}

		GraphicsNodeShape nodeShape = new GraphicsNodeShape();
		nodeShape.width = getNumbersInRange(xInteger);
		nodeShape.height = getNumbersInRange(yInteger);
		nodeShape.name = string;

		nodeShape.xInteger = xInteger;
		nodeShape.yInteger = yInteger;

		File dir = new File(CONSTANTS.SHAPE_FILE_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File file = new File(CONSTANTS.SHAPE_FILE_DIR.concat(string));
		ConfigFileMaintainer.writeObjectToFile(nodeShape, file, this);

//		MiscellaneousUtil.promot(8, "Save success", "Stay if you continue to create shapes, otherwise close the interface", this);

	}

	private Integer getNumbersInRange(List<Integer> list) {
		int size = list.size();
		int max = -1;
		int min = 99999999;
		for (int i = 0; i < size; i++) {
			Integer integer = list.get(i);
			if (integer > max) {
				max = integer;
			} else if (integer < min) {
				min = integer;
			}
		}
		return max - min;
	}

}
