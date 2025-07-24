package module.sequencelogo.makesequencelogo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Triple;

/**
 *
 * @ClassName: MakeDNAorRNASeqLogo
 * @Description: 绘制sequence logo的核心代码
 * @author zjw
 * @date 2024-04-26 05:45:28
 */
public class MakeSequenceLogo extends JPanel {

	private MakeSequenceLogoParameter parameter;

	public MakeSequenceLogo(MakeSequenceLogoParameter parameter) {
		this.parameter = parameter;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//将背景设置为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> NFB = parameter.getNFB();

		boolean isDefaultFontSize = parameter.getIsSeqLogoFontSizeDefault();
		int seqLength = NFB.getLeft();
		int fontSize;
		if (isDefaultFontSize) {
			//如果选择默认大小，需要在这里设置合适的字体大小
			int[] fontSizeArrays = new int[400];
			for (int i = 1; i < 401; i++) {
				fontSizeArrays[i - 1] = i;
			}
			fontSize = getAppropriateFontSize(fontSizeArrays, seqLength, g);
			parameter.setSeqLogoFontSize(fontSize);
		}else {
			fontSize = parameter.getSeqLogoFontSize();
		}

		//在已经设置好sequence logo字体大小后，需要设置sequence logo的字体类型
		setSeqLogoFontType();
		//在已经设置好sequence logo字体大小后，需要设置当前字号的字体的宽度和高度
		setSeqLogoFontWidthAndHeight(g);
		//设置每一个字母需要调整的位置
		setAdjustedPositionOfEachLetter();

		/**initialOrdinate
		 * 画sequence logo的本体
		 */
		List<LinkedHashMap<Character, ArrayList<Double>>> coordinateAndBitesOfPerBaseList = CalculateCoordinate.calculateCoordinate(parameter);
		drawSequenceLogo(coordinateAndBitesOfPerBaseList, g);

		/**
		 * 画坐标轴，以及坐标轴的标注，刻度线，刻度标度等
		 */
		LinkedHashMap<Integer, ArrayList<Double>> abscissaAxisCoordinates = CalculateAxisCoordinate.calculateAbscissaCoordinate(seqLength, parameter);
		LinkedHashMap<Integer, ArrayList<Double>> verticalAxisCoordinates = CalculateAxisCoordinate.calculateVerticalAxisCoordinate(parameter);
		drawCoordinateAxis(abscissaAxisCoordinates, verticalAxisCoordinates, g);
	}

	/**
	 *
	 * @MethodName: drawSequenceLogo
	 * @Description: 绘制sequence logo的主体部分
	 * @author zjw
	 * @param coordinateAndBitesOfPerBaseList
	 * @param g void
	 * @date 2024-05-06 04:55:33
	 */
	private void drawSequenceLogo(List<LinkedHashMap<Character, ArrayList<Double>>> coordinateAndBitesOfPerBaseList, Graphics g) {

		int sequenceType = parameter.getSequenceType();
		Graphics2D g2d = (Graphics2D) g.create();

		if(sequenceType == 0) {
			drawNucleotideSequenceLogo(coordinateAndBitesOfPerBaseList, g2d);
		}else if (sequenceType == 1) {
			drawProteinSequenceLogo(coordinateAndBitesOfPerBaseList, g2d);
		}
	}

	/**
	 *
	 * @MethodName: drawNucleotideSequenceLogo
	 * @Description: 绘制DNA or RNA sequence logo
	 * @author zjw
	 * @param coordinateAndBitesOfPerBaseList
	 * @param g2d void
	 * @date 2024-05-06 05:09:53
	 */
	private void drawNucleotideSequenceLogo(
			List<LinkedHashMap<Character, ArrayList<Double>>> coordinateAndBitesOfPerBaseList, Graphics2D g2d) {

		Font seqLogoFontType = parameter.getSeqLogoFontType();
		Color blackishGreen = new Color(0, 128, 0);

		for (Map<Character, ArrayList<Double>> coordinateOfBases : coordinateAndBitesOfPerBaseList) {

			for (Entry<Character, ArrayList<Double>> entry : coordinateOfBases.entrySet()) {

				String base = entry.getKey().toString();
				ArrayList<Double> coordinate = entry.getValue();
				double bite = coordinate.get(2);

				// 如果占的比例过小，就不用绘制了
				if (coordinate.get(2) < 0.01) {
					bite = 0;
				}

				if (bite > 0) {
					AffineTransform originalTransform = g2d.getTransform();
					Shape s = getShape(g2d, coordinate, base, seqLogoFontType);;

					if ("A".equals(base)) {
						g2d.setPaint(Color.red);
					} else if ("G".equals(base)) {
						g2d.setPaint(Color.orange);
					} else if ("C".equals(base)) {
						g2d.setPaint(Color.blue);
					} else if ("T".equals(base)) {
						g2d.setPaint(blackishGreen);
					} else if ("U".equals(base)) {
						g2d.setPaint(Color.green);
					}
					g2d.fill(s);
					g2d.draw(s);
					// 恢复为原始的坐标系统和缩放
					g2d.translate(-coordinate.get(0), -coordinate.get(1));
					g2d.setTransform(originalTransform);
				}
			}
		}
	}

	/**
	 *
	 * @MethodName: drawProteinSequenceLogo
	 * @Description: 绘制蛋白质sequence logo
	 * @author zjw
	 * @param coordinateAndBitesOfPerBaseList
	 * @param g2d void
	 * @date 2024-05-06 05:10:24
	 */
	private void drawProteinSequenceLogo(
			List<LinkedHashMap<Character, ArrayList<Double>>> coordinateAndBitesOfPerBaseList, Graphics2D g2d) {

		Font seqLogoFontType = parameter.getSeqLogoFontType();
		Color color1 = new Color(0, 0, 200);
		Color color2 = new Color(200, 0, 200);
		Color color3 = new Color(200, 0, 0);
		Color color4 = new Color(0, 0, 0);
		Color color5 = new Color(0, 200, 0);

		for (Map<Character, ArrayList<Double>> coordinateOfBases : coordinateAndBitesOfPerBaseList) {

			for (Entry<Character, ArrayList<Double>> entry : coordinateOfBases.entrySet()) {

				String base = entry.getKey().toString();
				ArrayList<Double> coordinate = entry.getValue();
				double bite = coordinate.get(2);

				// 如果占的比例过小，就不用绘制了
				if (coordinate.get(2) < 0.01) {
					bite = 0;
				}

				if (bite > 0) {
					AffineTransform originalTransform = g2d.getTransform();
					Shape s;
					if(base.equals("Q")) {
						Font seqLogoFontTypeOfQ = parameter.getSeqLogoFontTypeOfQ();
						s = getShape(g2d, coordinate, base, seqLogoFontTypeOfQ);
					}else {
						s = getShape(g2d, coordinate, base, seqLogoFontType);
					}

					if (base.equals("R") || base.equals("H") || base.equals("K")) {
						g2d.setPaint(color1);
					} else if (base.equals("Q") || base.equals("N")) {
						g2d.setPaint(color2);
					} else if (base.equals("D") || base.equals("E")) {
						g2d.setPaint(color3);
					} else if (base.equals("I") || base.equals("L") || base.equals("M") || base.equals("A")
							|| base.equals("V") || base.equals("F") || base.equals("P") || base.equals("W")) {
						g2d.setPaint(color4);
					} else if (base.equals("G") || base.equals("C") || base.equals("T") || base.equals("S")
							|| base.equals("Y")) {
						g2d.setPaint(color5);
					}
					g2d.fill(s);
					g2d.draw(s);
					// 恢复为原始的坐标系统和缩放
					g2d.translate(-coordinate.get(0), -coordinate.get(1));
					g2d.setTransform(originalTransform);
				}
			}
		}
	}

	private Shape getShape(Graphics2D g2d, ArrayList<Double> coordinate, String base, Font fontType) {

		double seqLogoFontHeight = parameter.getSeqLogoFontHeight();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.translate(coordinate.get(0), coordinate.get(1));
		char[] chs = base.toCharArray();
		FontRenderContext frc = g2d.getFontRenderContext();
		GlyphVector pgv = fontType.layoutGlyphVector(frc, chs, 0, 1, 0);
		AffineTransform st = AffineTransform.getScaleInstance(1,
				(seqLogoFontHeight / pgv.getVisualBounds().getHeight()) * coordinate.get(2));
		g2d.transform(st);
		GlyphVector v = fontType
				.createGlyphVector(getFontMetrics(fontType).getFontRenderContext(), base);
		Shape s = v.getOutline();

		return s;
	}

	/**
	 *
	 * @MethodName: drawCoordinateAxis
	 * @Description: 绘制坐标轴，以及坐标轴的标注，刻度线，刻度标度等
	 * @author zjw
	 * @param abscissaAxisCoordinates
	 * @param verticalAxisCoordinates
	 * @param g void
	 * @date 2024-05-06 05:13:06
	 */
	private void drawCoordinateAxis(LinkedHashMap<Integer, ArrayList<Double>> abscissaAxisCoordinates,
									LinkedHashMap<Integer, ArrayList<Double>> verticalAxisCoordinates, Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(3.0f));
		Font coordinateAxisFont = parameter.getCoordinateAxisFontType();
		g2d.setFont(coordinateAxisFont);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		drawVerticalAxis(g2d, verticalAxisCoordinates);
		drawAbscissaAxis(g2d, abscissaAxisCoordinates);
	}

	/**
	 *
	 * @MethodName: drawVerticalAxis
	 * @Description: 绘制纵坐标轴，以及纵坐标轴的标注，刻度线，刻度标度等
	 * @author zjw
	 * @param g2d
	 * @param verticalAxisCoordinates void
	 * @date 2024-05-06 05:13:50
	 */
	private void drawVerticalAxis(Graphics2D g2d, LinkedHashMap<Integer, ArrayList<Double>> verticalAxisCoordinates) {

		int tickLength = parameter.tickLength;
		// 绘制纵坐标轴
		ArrayList<Double> verticalAxisCoordinate = verticalAxisCoordinates.get(0);
		double x1_verticalAxis = verticalAxisCoordinate.get(0);
		double y1_verticalAxis = verticalAxisCoordinate.get(1);
		double x2_verticalAxis = verticalAxisCoordinate.get(2);
		double y2_verticalAxis = verticalAxisCoordinate.get(3);
		Line2D.Double verticalAxis = new Line2D.Double(x1_verticalAxis, y1_verticalAxis, x2_verticalAxis, y2_verticalAxis);
		g2d.draw(verticalAxis);

		// 绘制纵坐标轴的标注
		String label;
		if (verticalAxisCoordinates.size() > 4) {
			label = "Bits";
		} else {
			label = "Probability";
		}
		g2d.rotate(-Math.PI / 2);// 将纵坐标轴的标注逆时针旋转90°
		double labelWidth = g2d.getFontMetrics().stringWidth(label);
		double labelHeight = g2d.getFontMetrics().getHeight();
		double x_verticalAxisLabel = -( y1_verticalAxis +  y2_verticalAxis) / 2 - labelWidth / 2;
		double y_verticalAxisLabel = x1_verticalAxis - tickLength - labelHeight * 1.5;
		g2d.translate(x_verticalAxisLabel, y_verticalAxisLabel);
		g2d.drawString(label, 0, 0);
		g2d.translate(-x_verticalAxisLabel, -y_verticalAxisLabel);
		g2d.rotate(Math.PI / 2);

		verticalAxisCoordinates.remove(0);

		String[] verticalAxisScale = { "0", "0.5", "1.0", "1.5", "2.0" };
		int i = 0;
		for (Entry<Integer, ArrayList<Double>> entry : verticalAxisCoordinates.entrySet()) {
			ArrayList<Double> coordinate = entry.getValue();
			double x1 = coordinate.get(0);
			double y1 = coordinate.get(1);
			double x2 = coordinate.get(2);
			double y2 = coordinate.get(3);
			// 纵坐标轴的刻度线
			Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
			g2d.draw(line);

			// 绘制纵坐标刻度数值
			String text = verticalAxisScale[i];
			int textWidth = g2d.getFontMetrics().stringWidth(text);
			int textHeight = g2d.getFontMetrics().getHeight();
			double x0 = x1 - tickLength - textWidth - textHeight / 4;
			double y0 = y1 + textHeight / 4;
			g2d.translate(x0, y0);
			g2d.drawString(text, 0, 0);
			g2d.translate(-x0, -y0);

			i++;
		}
	}

	/**
	 *
	 * @MethodName: drawAbscissaAxis
	 * @Description: 绘制横坐标轴，以及横坐标轴的刻度线，刻度标度等
	 * @author zjw
	 * @param g2d
	 * @param abscissaAxisCoordinates void
	 * @date 2024-05-06 05:14:24
	 */
	private void drawAbscissaAxis(Graphics2D g2d, LinkedHashMap<Integer, ArrayList<Double>> abscissaAxisCoordinates) {
		int tickLength = parameter.tickLength;
		boolean abscissaAngle = parameter.getAbscissaAngle();

		// 绘制横坐标轴
		ArrayList<Double> abscissaAxisCoordinate = abscissaAxisCoordinates.get(0);
		double x1_abscissaAxis = abscissaAxisCoordinate.get(0);
		double y1_abscissaAxis = abscissaAxisCoordinate.get(1);
		double x2_abscissaAxis = abscissaAxisCoordinate.get(2);
		double y2_abscissaAxis = abscissaAxisCoordinate.get(3);
		Line2D.Double abscissaAxis = new Line2D.Double(x1_abscissaAxis, y1_abscissaAxis, x2_abscissaAxis, y2_abscissaAxis);
		g2d.draw(abscissaAxis);

		abscissaAxisCoordinates.remove(0);

		int i = 1;
		for (Entry<Integer, ArrayList<Double>> entry : abscissaAxisCoordinates.entrySet()) {
			ArrayList<Double> coordinate = entry.getValue();
			double x1 = coordinate.get(0);
			double y1 = coordinate.get(1);
			double x2 = coordinate.get(2);
			double y2 = coordinate.get(3);
			// 横坐标轴的刻度线
			Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
			g2d.draw(line);

			// 绘制横坐标刻度数值
			String text = Integer.toString(i);
			int textWidth = g2d.getFontMetrics().stringWidth(text);
			int textHeight = g2d.getFontMetrics().getHeight();

			if (abscissaAngle) {
				// 这个是不将横坐标刻度数值旋转，横坐标刻度数值是垂直的
				double x0 = x1 - textWidth / 2;
				double y0 = y1 + tickLength + textHeight;
				g2d.translate(x0, y0);
				g2d.drawString(text, 0, 0);
				g2d.translate(-x0, -y0);
			} else {
				// 这个是将横坐标刻度数值进行向左旋转90度,横坐标刻度数值是水平的
				g2d.rotate(-Math.PI / 2, x1, y1 + tickLength + textWidth / 2);
				double x0 = x1 - textHeight / 2;
				double y0 = y1 + tickLength + textWidth;
				g2d.translate(x0, y0);
				g2d.drawString(text, 0, 0);
				g2d.translate(-x0, -y0);
				g2d.rotate(Math.PI / 2, x1, y1 + tickLength + textWidth / 2);
			}

			i++;
		}
	}

	/**
	 *
	 * @MethodName: getAppropriateFontSize
	 * @Description: 选择默认sequence logo的字体大小时，需要根据此时面板的大小选择合适的字体大小
	 * @author zjw
	 * @param fontSizeArray
	 * @param seqLength
	 * @param g
	 * @return int
	 * @date 2024-05-06 09:57:53
	 */
	private int getAppropriateFontSize(int[] fontSizeArray, int seqLength, Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		double panelWidth = parameter.getDrawingBoardWidth();
		double panelHeight = parameter.getdrawingBoardHeight();
		int orderOfFontSize = fontSizeArray.length - 1;
		double logoWidth = Double.MAX_VALUE;
		double logoHeight = Double.MAX_VALUE;

		while (logoWidth > panelWidth || logoHeight > panelHeight) {
			int fontSize = fontSizeArray[orderOfFontSize];
			Font font = new Font("Consolas", Font.BOLD, fontSize);
			char[] chs2 = "C".toCharArray();
			FontRenderContext frc2 = g2d.getFontRenderContext();
			GlyphVector pgv2 = font.layoutGlyphVector(frc2, chs2, 0, 1, 0);
			double fontHeight = pgv2.getVisualBounds().getHeight();
			double fontWidth = g2d.getFontMetrics(font).stringWidth("C");

			logoWidth = seqLength * fontWidth + 170;
			logoHeight = 2.0 * fontHeight + 170;
			orderOfFontSize--;
		}

		return fontSizeArray[orderOfFontSize];
	}

	/**
	 *
	 * @MethodName: setSeqLogoFontType
	 * @Description: 设置sequence logo的字体类型
	 * @author zjw
	 * @date 2024-05-06 10:51:05
	 */
	private void setSeqLogoFontType() {
		parameter.setSeqLogoFontType();
		parameter.setSeqLogoFontTypeOfQ();
	}

	/**
	 *
	 * @MethodName: setSeqLogoFontWidthAndHeight
	 * @Description: 设置sequence logo的选择的字体的大小的宽度和高度
	 * @author zjw
	 * @param g
	 * @date 2024-05-06 10:31:30
	 */
	private void setSeqLogoFontWidthAndHeight(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();

		Font seqLogoFontType = parameter.getSeqLogoFontType();
		char[] charArray = "A".toCharArray();
		FontRenderContext frc = g2d.getFontRenderContext();
		GlyphVector pgv = seqLogoFontType.layoutGlyphVector(frc, charArray, 0, 1, 0);
		double seqLogoFontWidth = g2d.getFontMetrics(seqLogoFontType).stringWidth("A");
		double seqLogoFontHeight = pgv.getVisualBounds().getHeight();
		parameter.setSeqLogoFontWidth(seqLogoFontWidth);
		parameter.setSeqLogoFontHeight(seqLogoFontHeight);
	}

	/**
	 *
	 * @MethodName: setAdjustedPositionOfEachLetter
	 * @Description: 设置每一个字母需要调整的位置
	 * @author zjw void
	 * @date 2024-05-08 04:27:16
	 */
	private void setAdjustedPositionOfEachLetter() {
		double seqLogoFontHeight = parameter.getSeqLogoFontHeight();
		Font seqLogoFontType = parameter.getSeqLogoFontType();
		Font seqLogoFontTypeOfQ = parameter.getSeqLogoFontTypeOfQ();
		HashMap<Character, Double> adjustedPositionOfEachLetter = new HashMap<>();
		FontRenderContext frc = new FontRenderContext(null, true, true);
		for (char c = 'A'; c <= 'Z'; c++) {
			String base = String.valueOf(c);
			GlyphVector pgv;
			if(c == 'Q') {
				pgv = seqLogoFontTypeOfQ.layoutGlyphVector(frc, base.toCharArray(), 0, 1, 0);
			}else {
				pgv = seqLogoFontType.layoutGlyphVector(frc, base.toCharArray(), 0, 1, 0);
			}
			AffineTransform st = AffineTransform.getScaleInstance(1,
					seqLogoFontHeight / pgv.getVisualBounds().getHeight());
			Shape s = pgv.getOutline();
			Shape transformedShape = st.createTransformedShape(s);
			Rectangle2D transformedBounds = transformedShape.getBounds2D();
			double adjustHeight = transformedBounds.getY() + transformedBounds.getHeight();
			adjustedPositionOfEachLetter.put(c, adjustHeight);
		}

		parameter.setAdjustedPositionOfEachLetter(adjustedPositionOfEachLetter);
	}
}