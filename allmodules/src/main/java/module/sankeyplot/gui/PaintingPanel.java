package module.sankeyplot.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

import egps2.utils.common.model.datatransfer.ThreeTuple;
import egps2.utils.common.model.datatransfer.TwoTuple;
import egps2.UnifiedAccessPoint;
import egps2.frame.gui.EGPSMainGuiUtil;
import module.sankeyplot.SankeyPlotMain;
import module.sankeyplot.model.StringElements;

public class PaintingPanel extends JPanel {

	final int horizontalBlink = 40;

	final int indexOfTopLeftY = 0;
	final int indexOfBottomLeftY = 1;
	final int indexOfTopRightY = 2;
	final int indexOfBottomRightY = 3;

	int barWidth = 40;
	int topBlink = 40;
	double height;
	double width;
	int alpha = 110;

	final float[] dash1 = { 10.0f };
	final BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
			0.0f);

	NamedRectObj[][] rect2dForPainting;
	private int numOfTotalPaintingColumn;
	private Map<TwoTuple<String, String>, double[]>[] columnPaintingMap;
	private Map<String, ThreeTuple<Integer, Integer, Integer>> colorMap;

	private List<StringElements> inputData;
	private SankeyPlotMain sankeyPlotMain;

	private boolean ifInitialize = false;
	private RectColorSource rectColorSource = RectColorSource.Left;
	private boolean shouldRandomColor = true;
	private double curveRatio = 0.2;

	private Font nameFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private String[] setNames;

	public PaintingPanel(SankeyPlotMain sankeyPlotMain) {
		this.sankeyPlotMain = sankeyPlotMain;

		this.setBackground(Color.white);
	}

	public SankeyPlotMain getSankeyPlotMain() {
		return sankeyPlotMain;
	}

	public boolean getIfInitalized() {
		return ifInitialize;
	}

	public void initializeData() {
		height = getHeight() - 90;
		width = getWidth();

		// produceData = produceData();
		int numOfColumns = inputData.size();
		numOfTotalPaintingColumn = inputData.get(0).getEleStrings().length;
		rect2dForPainting = new NamedRectObj[numOfTotalPaintingColumn][];
		columnPaintingMap = new Map[numOfTotalPaintingColumn - 1];
		colorMap = new HashMap<>();

		for (int i = 0; i < numOfTotalPaintingColumn - 1; i++) {
			columnPaintingMap[i] = new HashMap<TwoTuple<String, String>, double[]>();
			prepareDataStructure1(i, i + 1, true);
			prepareDataStructure1(i + 1, i, false);
		}

		ifInitialize = true;
		shouldRandomColor = false;
	}

	private final ThreeTuple<Integer, Integer, Integer> randomColorTuple() {
		Random random = new Random();
		return new ThreeTuple<Integer, Integer, Integer>(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	private void prepareDataStructure1(int index1, int index2, boolean ifLeft) {

		int minIndex = Math.min(index1, index2);
		int totalSize = inputData.size();

		Collections.sort(inputData, new Comparator<StringElements>() {
			@Override
			public int compare(StringElements o1, StringElements o2) {
				String s11 = o1.getEleStrings()[index1];
				String s21 = o2.getEleStrings()[index1];
				if (s11.equalsIgnoreCase(s21)) {
					String s12 = o1.getEleStrings()[index2];
					String s22 = o2.getEleStrings()[index2];
					return s12.compareTo(s22);
				} else {
					return s11.compareTo(s21);
				}
			}

		});

		List<NamedRectObj> rectObjs = new ArrayList<NamedRectObj>();
		String oldValue = "";
		double yEach = (height - topBlink) / totalSize;

		for (int i = 0; i < totalSize; i++) {
			StringElements stringElements = inputData.get(i);
			String ss = stringElements.getEleStrings()[index1];
			if (!ss.equalsIgnoreCase(oldValue)) {
				// System.err.println(ss);
				NamedRectObj e = new NamedRectObj(0, topBlink + i * yEach, barWidth, 0);
				e.setName(ss);
				ThreeTuple<Integer, Integer, Integer> randomColorTuple = randomColorTuple();
				e.setColor(randomColorTuple);
				rectObjs.add(e);
				oldValue = ss;
			}
		}
		int size = rectObjs.size();
		NamedRectObj rectObj = rectObjs.get(size - 1);
		rectObj.h = height - rectObj.y;
		for (int i = size - 2; i > -1; i--) {
			rectObj = rectObjs.get(i);
			rectObj.h = rectObjs.get(i + 1).y - rectObj.y;
		}
		if (rect2dForPainting[index1] == null) {
			NamedRectObj[] array = rectObjs.toArray(new NamedRectObj[0]);
			rect2dForPainting[index1] = array;

			for (NamedRectObj namedRectObj : array) {
				colorMap.put(namedRectObj.getName(), namedRectObj.getColor());
			}
		}

		Map<TwoTuple<String, String>, LinkedList<Integer>> tMap = new HashMap<>();
		for (int i = 0; i < totalSize; i++) {
			StringElements stringElements = inputData.get(i);
			String[] eleStrings = stringElements.getEleStrings();

			String first = "";
			String second = "";
			if (ifLeft) {
				first = eleStrings[index1];
				second = eleStrings[index2];
			} else {
				first = eleStrings[index2];
				second = eleStrings[index1];
			}
			// System.out.println(first+"\t"+second);
			TwoTuple<String, String> key = new TwoTuple<String, String>(first, second);
			if (tMap.get(key) == null) {
				tMap.put(key, new LinkedList<Integer>());
			}

			tMap.get(key).add(i);
		}

		// System.out.println("-----------------");
		for (Map.Entry<TwoTuple<String, String>, LinkedList<Integer>> entry : tMap.entrySet()) {
			TwoTuple<String, String> key = entry.getKey();

			if (columnPaintingMap[minIndex].get(key) == null) {
				columnPaintingMap[minIndex].put(key, new double[4]);
			}

			double[] ds = columnPaintingMap[minIndex].get(key);
			LinkedList<Integer> value = entry.getValue();

			if (ifLeft) {
				ds[0] = topBlink + value.getFirst() * yEach;
				ds[1] = topBlink + value.getLast() * yEach + yEach;
			} else {
				ds[2] = topBlink + value.getFirst() * yEach;
				ds[3] = topBlink + value.getLast() * yEach + yEach;
			}

		}

	}

	public void setInputData(List<StringElements> inputData, String[] setNames) {
		this.inputData = inputData;
		this.setNames = setNames;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;

		if (!ifInitialize) {
			EGPSMainGuiUtil.setupHighQualityRendering(g2d);
			EGPSMainGuiUtil.drawLetUserImportDataString(g2d);
			return;
		}

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		double iterater = (width - 2 * horizontalBlink - barWidth) / (setNames.length - 1);
		g2d.setColor(Color.black);
		g2d.setFont(nameFont);
		// g2d.fill(new Rectangle2D.Double(horizontalBlink, 100, barWidth, 600));
		double topLeftX = 0;
		double nextTopLeftX = 0;
		for (int i = 0; i < numOfTotalPaintingColumn - 1; i++) {
			topLeftX = horizontalBlink + i * iterater + barWidth;
			nextTopLeftX = horizontalBlink + i * iterater + iterater;

			double wanQv = curveRatio * (nextTopLeftX - topLeftX);

			NamedRectObj[] rectObjs = rect2dForPainting[i];

			for (Map.Entry<TwoTuple<String, String>, double[]> entry : columnPaintingMap[i].entrySet()) {
				TwoTuple<String, String> key = entry.getKey();

				double[] ds = entry.getValue();

				GeneralPath generalPath = new GeneralPath();
				generalPath.moveTo(topLeftX, ds[indexOfTopLeftY]);
				// important

				generalPath.curveTo(topLeftX + wanQv, ds[indexOfTopLeftY], nextTopLeftX - wanQv, ds[indexOfTopRightY],
						nextTopLeftX, ds[indexOfTopRightY]);

				// generalPath.lineTo(nextTopLeftX, ds[indexOfTopRightY]);
				generalPath.lineTo(nextTopLeftX, ds[indexOfBottomRightY]);
				// important
				generalPath.curveTo(nextTopLeftX - wanQv, ds[indexOfBottomRightY], topLeftX + wanQv,
						ds[indexOfBottomLeftY], topLeftX, ds[indexOfBottomLeftY]);
				// generalPath.lineTo(topLeftX, ds[indexOfBottomLeftY]);
				generalPath.closePath();

				if (rectColorSource == RectColorSource.Left) {
					ThreeTuple<Integer, Integer, Integer> color = colorMap.get(key.first);
					g2d.setColor(new Color(color.first, color.second, color.third, alpha));
				} else if (rectColorSource == RectColorSource.Right) {
					ThreeTuple<Integer, Integer, Integer> color = colorMap.get(key.second);
					g2d.setColor(new Color(color.first, color.second, color.third, alpha));
				} else {
					ThreeTuple<Integer, Integer, Integer> color = colorMap.get(key.second);
					Color colorStart = new Color(color.first, color.second, color.third, alpha);
					ThreeTuple<Integer, Integer, Integer> color2 = colorMap.get(key.first);
					Color colorEnd = new Color(color2.first, color2.second, color2.third, alpha);

					GradientPaint gradient = new GradientPaint((float) topLeftX, (float) ds[indexOfTopLeftY], colorEnd,
							(float) nextTopLeftX, (float) ds[indexOfTopRightY], colorStart);
					g2d.setPaint(gradient);
				}
				g2d.fill(generalPath);
			}

			FontMetrics fontMetrics = g2d.getFontMetrics();
			for (NamedRectObj rectObj : rectObjs) {
				ThreeTuple<Integer, Integer, Integer> color = rectObj.getColor();
				Color color2 = new Color(color.first, color.second, color.third);
				g2d.setColor(color2);

				rectObj.x = topLeftX - barWidth;
				rectObj.w = barWidth;
				Rectangle2D.Double rect = new Rectangle2D.Double(rectObj.x, rectObj.y, barWidth, rectObj.h);
				g2d.fill(rect);

				if (rectObj.isSelected()) {
					Stroke oldStroke = g2d.getStroke();
					g2d.setColor(Color.blue);
					g2d.setStroke(dashedStroke);
					g2d.draw(rect);
					g2d.setStroke(oldStroke);
				}

				g2d.setColor(Color.black);
				int stringWidth = fontMetrics.stringWidth(rectObj.getName());
				float drawX = (float) (rectObj.x + 0.5 * rectObj.w - 0.5 * stringWidth);
				g2d.drawString(rectObj.getName(), drawX, (float) (rectObj.y + 0.5 * rectObj.h));
			}

		}

		topLeftX = horizontalBlink + (numOfTotalPaintingColumn - 1) * iterater + barWidth;
		NamedRectObj[] rectObjs = rect2dForPainting[numOfTotalPaintingColumn - 1];

		FontMetrics fontMetrics = g2d.getFontMetrics();
		for (NamedRectObj rectObj : rectObjs) {
			ThreeTuple<Integer, Integer, Integer> color = rectObj.getColor();
			Color color2 = new Color(color.first, color.second, color.third);
			g2d.setColor(color2);
			rectObj.x = topLeftX - barWidth;
			rectObj.w = barWidth;
			Rectangle2D.Double rect = new Rectangle2D.Double(rectObj.x, rectObj.y, barWidth, rectObj.h);
			g2d.fill(rect);

			if (rectObj.isSelected()) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setColor(Color.blue);
				g2d.setStroke(dashedStroke);
				g2d.draw(rect);
				g2d.setStroke(oldStroke);
			}
			g2d.setColor(Color.black);
			int stringWidth = fontMetrics.stringWidth(rectObj.getName());
			float drawX = (float) (rectObj.x + 0.5 * rectObj.w - 0.5 * stringWidth);
			g2d.drawString(rectObj.getName(), drawX, (float) (rectObj.y + 0.5 * rectObj.h));
		}

		g2d.setFont(titleFont);
		for (int i = 0; i < numOfTotalPaintingColumn; i++) {
			topLeftX = horizontalBlink + i * iterater + barWidth;
			nextTopLeftX = horizontalBlink + i * iterater + iterater;

			g2d.drawString(setNames[i], (float) (topLeftX - barWidth), (float) (height + 30));
		}

	}

	public void judgeIfSelectionsAndAsigneValues(Rectangle2D.Double rect) {
		if (rect2dForPainting == null) {
			return;
		}
		for (NamedRectObj[] namedRectObjs : rect2dForPainting) {
			for (NamedRectObj obj : namedRectObjs) {
				if (rect.intersects(obj.x, obj.y, obj.w, obj.h)) {
					obj.setSelected(true);
				}
			}
		}
	}

	public void clearSelection() {
		if (rect2dForPainting == null) {
			return;
		}
		for (NamedRectObj[] namedRectObjs : rect2dForPainting) {
			for (NamedRectObj namedRectObjs2 : namedRectObjs) {
				namedRectObjs2.setSelected(false);
			}
		}
	}

	public void setTransparent(int value) {
		alpha = value;
		sankeyPlotMain.weakUpdateGraphics();
	}

	public void setCurveRatio(double curveRatio) {
		this.curveRatio = curveRatio;
		sankeyPlotMain.weakUpdateGraphics();
	}

	public void setBarWidth(int value) {
		this.barWidth = value;
		sankeyPlotMain.weakUpdateGraphics();
	}

	public void setRectColorSource(RectColorSource rectColorSource) {
		this.rectColorSource = rectColorSource;
		sankeyPlotMain.weakUpdateGraphics();
	}

	public void randomColor() {
		shouldRandomColor = true;
		initializeData();
		sankeyPlotMain.weakUpdateGraphics();
	}

	public void setTitleFont(Font font) {
		this.titleFont = font;
		sankeyPlotMain.weakUpdateGraphics();
	}

	public void setNameFont(Font font) {
		this.nameFont = font;
		sankeyPlotMain.weakUpdateGraphics();
	}

	public void clear() {
		rect2dForPainting = null;
		columnPaintingMap = null;
		colorMap = null;
		ifInitialize = false;
	}

	public void updateGraphics() {
		sankeyPlotMain.weakUpdateGraphics();
	}

	public boolean ifContainsSelection() {

		if (rect2dForPainting == null) {
			return false;
		}
		for (NamedRectObj[] namedRectObjs : rect2dForPainting) {
			for (NamedRectObj obj : namedRectObjs) {
				if (obj.isSelected()) {
					return true;
				}
			}
		}
		return false;
	}

	public Color getCurrentRectColorStatus() {
		if (rect2dForPainting == null) {
			return null;
		}
		for (NamedRectObj[] namedRectObjs : rect2dForPainting) {
			for (NamedRectObj obj : namedRectObjs) {
				if (obj.isSelected()) {
					ThreeTuple<Integer, Integer, Integer> color = obj.getColor();
					return new Color(color.first, color.second, color.third);
				}
			}
		}

		return null;
	}

	public void setColor(Color newColor) {
		for (NamedRectObj[] namedRectObjs : rect2dForPainting) {
			for (NamedRectObj obj : namedRectObjs) {
				if (obj.isSelected()) {
					ThreeTuple<Integer, Integer, Integer> threeTuple = new ThreeTuple<Integer, Integer, Integer>(
							newColor.getRed(), newColor.getGreen(), newColor.getBlue());
					obj.setColor(threeTuple);
					colorMap.put(obj.getName(), threeTuple);
				}
			}
		}

		sankeyPlotMain.weakUpdateGraphics();

	}

}
