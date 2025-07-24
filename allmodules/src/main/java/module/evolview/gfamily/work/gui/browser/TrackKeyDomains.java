package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D.Double;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import module.evolview.gfamily.work.beans.TrackMultipleDomainBean;
import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyKeyDomains;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;

/**
 * 绘制ElementStructure信息,可能会有多行 这里类名还是叫做 KeyDomain 但是它可以充当任何 Element Structure
 * 不单单可以表示KeyDomain
 */
@SuppressWarnings("serial")
public class TrackKeyDomains extends AbstractTrackPanel {

	private static final Logger logger = LoggerFactory.getLogger(TrackKeyDomains.class);

	private CalculatorKeyDomains calculatorElementStructure;
	private DrawingPropertyKeyDomains paintElements;
	private int prefHeight = 40;

	public TrackKeyDomains(BrowserPanel genomeMain, TrackMultipleDomainBean drawingPropertyElementModelItem) {
		super(genomeMain);
		
		int size = drawingPropertyElementModelItem.getMultiTracksOfSeqElements().size();
		prefHeight = size * 60;
		
		calculatorElementStructure = new CalculatorKeyDomains(genomeMain, drawingPropertyElementModelItem);

		MouseListenerKeyDomains structureMouseListener = new MouseListenerKeyDomains(genomeMain);
		shape = new TooltiperKeyDomains(this);
		addMouseMotionListener(structureMouseListener);
		addMouseListener(structureMouseListener);
	}

	public CalculatorKeyDomains getCalculatorElementStructure() {
		return calculatorElementStructure;
	}

	@Override
	public void initialize() {
		paintElements = calculatorElementStructure.calculatePaintingLocations(drawProperties, getWidth(), getHeight());
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int width = getWidth();
		int panelHeight = getHeight();
		if (isHighlight) {
			g2.setBackground(backgroundColor);
			g2.clearRect(0, 0, width, panelHeight);
		}
		if (paintElements == null) {
			return;
		}

		Color oldColor = g2.getColor();
		FontMetrics fontMetrics = g2.getFontMetrics();
		
		// 大多数情况下这个 paintingLocations 只有一个
		List<DrawingPropertySequenceModel> paintingLocations = paintElements.getGenomePaintElementItems();
		int size = paintingLocations.size();
		
		for (int i = 0; i < size; i++) {
			DrawingPropertySequenceModel paintElementItem = paintingLocations.get(i);
			List<DrawingPropertyElement> paintElements = paintElementItem.getGenomeElementModels();

			if (!paintElements.isEmpty()) {
				Double roundRect = paintElements.get(0).getRoundRect();
				double centerY = roundRect.getCenterY();
				g2.setColor(Color.lightGray);
				g2.drawLine(LocationCalculator.BLINK_LEFT_SPACE_LENGTH, (int) centerY,
						width - LocationCalculator.BLINK_RIGHT_SPACE_LENGTH, (int) centerY);
				
			}
			

			for (DrawingPropertyElement paintElement : paintElements) {
				g2.setColor(paintElement.getColor());
				Double roundRect = paintElement.getRoundRect();
				g2.fill(roundRect);

				// 绘制名称
				String name = paintElement.getName();
				int x1 = -1;
				int stringWidth = fontMetrics.stringWidth(name);
				int stringHeight = fontMetrics.getHeight();

				float fontYLocation = (float) (roundRect.getY()
						+ 0.5 * (roundRect.getHeight() + fontMetrics.getAscent()));
				/**
				 * 这里绘制圆角矩形的名称，根据字符串宽度和矩形的宽度对显式的内容做一些调整
				 */
				g2.setColor(Color.white);
				if (roundRect.width > stringWidth) {
					x1 = (int) (roundRect.getX() + 0.5 * roundRect.width - 0.5 * stringWidth);
					g2.drawString(name, x1, fontYLocation);
				} else if (roundRect.width < stringWidth) {
					String[] split = name.split("\\s+");
					StringBuffer buffer = new StringBuffer();
					for (int j = 0; j < split.length; j++) {
						buffer.append(split[j].substring(0, 1));
					}
					String newName = buffer.toString();
					stringWidth = fontMetrics.stringWidth(newName);
					if (roundRect.width > stringWidth) {
						x1 = (int) (roundRect.getX() + 0.5 * roundRect.width - 0.5 * stringWidth + 1);
						g2.drawString(newName, x1, fontYLocation);
					}
				}

				Rectangle2D.Double aDouble = new Rectangle2D.Double(x1, fontYLocation - stringHeight, stringWidth,
						stringHeight);
				paintElement.setRectangle(aDouble);

			}

		}

		g2.setColor(oldColor);

		// 这段代码作用，有些时候 subTrack会伸出去，需要将其遮住。不信可以注释掉，查看效果。
		// 但是千万不要删除
		Color tempBackgroundColor = isHighlight ? backgroundColor : getBackground();
		g2.setColor(tempBackgroundColor);
		Rectangle2D.Double aDouble = new Rectangle2D.Double();
		aDouble.setRect(0, 0, LocationCalculator.BLINK_LEFT_SPACE_LENGTH, panelHeight);
		g2.fill(aDouble);
		aDouble.setRect(width - LocationCalculator.BLINK_RIGHT_SPACE_LENGTH, 0,
				LocationCalculator.BLINK_RIGHT_SPACE_LENGTH, panelHeight);
		g2.fill(aDouble);

		g2.setColor(oldColor);
		for (DrawingPropertySequenceModel paintElementItem : paintingLocations) {
			// 绘制前面的名字
			String sequenceElementModelName = paintElementItem.getSequenceElementModelName();
			if (sequenceElementModelName != null) {
				Point subTrackLocator = paintElementItem.getSubTrackLocator();
				g2.drawString(sequenceElementModelName, subTrackLocator.x, subTrackLocator.y + 3);
			}
		}

		/**
		 * 这是绘制整个 Track 的 说明字符串
		 */
//        g2.drawString(paintElements.getTrackName(), paintElements.getTrackNameXLocation(), paintElements.getTrackNameYLocation());
		g2.drawString(paintElements.getTrackName(), paintElements.getTrackNameXLocation(),
				paintElements.getTrackNameYLocation());

	}

	public DrawingPropertyKeyDomains getPaintElements() {
		return paintElements;
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(32767, prefHeight);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(10, prefHeight);
	}

}
