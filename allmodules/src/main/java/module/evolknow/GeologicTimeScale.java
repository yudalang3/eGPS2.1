package module.evolknow;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import utils.string.EGPSStringUtil;

/**
 * https://en.wikipedia.org/wiki/Geologic_time_scale
 */
public class GeologicTimeScale {

	EonsTimeScale eonsTimeScale = new EonsTimeScale();
	ErasTimeScale erasTimeScale = new ErasTimeScale();
	PeriodsTimeScale periodsTimeScale = new PeriodsTimeScale();


	public EonsTimeScale getEonsTimeScale() {
		return eonsTimeScale;
	}

	public ErasTimeScale getErasTimeScale() {
		return erasTimeScale;
	}

	public PeriodsTimeScale getPeriodsTimeScale() {
		return periodsTimeScale;
	}

	/**
	 * 注意，为了简化开发过程 plotArea是图绘制的区域，不包括字符串注释符
	 * 
	 * @param g2d
	 * @param plotArea
	 * @param farthestTimeMYA
	 */
	public void paintThe4legend(Graphics2D g2d, Rectangle plotArea, int farthestTimeMYA) {
		TimeScaleEntity xianshengZhouEntity = eonsTimeScale.getEntities()[0];
		if (farthestTimeMYA > xianshengZhouEntity.getStartMya()) {
			String format = EGPSStringUtil.format("Sorry, the time scale {} is too far from now. Max is {}",
					farthestTimeMYA, xianshengZhouEntity.getStartMya());
			g2d.setColor(Color.black);
			g2d.drawString(format, plotArea.x, (int) plotArea.getMaxY());
			return;
		}
		
		final int rightTimeUnitGap = 15;

		int eachYHeight = plotArea.height / 3;

		// draw string

		FontMetrics fontMetrics = g2d.getFontMetrics();
		int ascent = fontMetrics.getAscent();
		{
			g2d.setColor(xianshengZhouEntity.getColor());
			g2d.fill3DRect(plotArea.x, plotArea.y + eachYHeight + eachYHeight, plotArea.width, eachYHeight, true);
			g2d.setColor(Color.black);
			g2d.drawString("Eons", plotArea.x + plotArea.width + rightTimeUnitGap,
					(int) (plotArea.y + eachYHeight * 2.5 + ascent / 2));
			int stringWidth = fontMetrics.stringWidth(xianshengZhouEntity.getName());
			g2d.drawString(xianshengZhouEntity.getName(), plotArea.x + (plotArea.width - stringWidth) / 2 + 5,
					(int) (plotArea.y + eachYHeight * 2.5 + ascent / 2));

		}
		{
			g2d.drawString("Eras", plotArea.x + plotArea.width + rightTimeUnitGap,
					(int) (plotArea.y + eachYHeight * 1.5 + ascent / 2));
			TimeScaleEntity[] entities = erasTimeScale.getEntities();
			for (TimeScaleEntity timeScaleEntity : entities) {
				float startMya = timeScaleEntity.getStartMya();
				float endMya = timeScaleEntity.getEndMya();
				if (endMya > farthestTimeMYA) {
					continue;
				}

				float startMyaRatio = startMya / farthestTimeMYA;
				float endMyaRatio = endMya / farthestTimeMYA;
				// trans to paint ratio
				startMyaRatio = 1 - startMyaRatio;
				endMyaRatio = 1 - endMyaRatio;

				startMyaRatio = startMyaRatio < 0 ? 0 : startMyaRatio;

				int startLocation = (int) (plotArea.x + startMyaRatio * plotArea.width);
				int endLocation = (int) (plotArea.x + endMyaRatio * plotArea.width);

				g2d.setColor(timeScaleEntity.getColor());
				g2d.fillRect(startLocation, plotArea.y + eachYHeight, endLocation - startLocation, eachYHeight);

				g2d.setColor(Color.black);
				int stringWidth = fontMetrics.stringWidth(timeScaleEntity.getName());
				g2d.drawString(timeScaleEntity.getName(), (startLocation + endLocation - stringWidth) / 2,
						(int) (plotArea.y + eachYHeight + (ascent + eachYHeight) / 2));

			}
		}

		{
			g2d.fill3DRect(plotArea.x, plotArea.y, plotArea.width, eachYHeight, true);
			g2d.drawString("Periods", plotArea.x + plotArea.width + rightTimeUnitGap,
					(int) (plotArea.y + eachYHeight * 0.5 + ascent / 2));
			TimeScaleEntity[] entities = periodsTimeScale.getEntities();
			for (TimeScaleEntity timeScaleEntity : entities) {
				float startMya = timeScaleEntity.getStartMya();
				float endMya = timeScaleEntity.getEndMya();
				if (endMya > farthestTimeMYA) {
					continue;
				}

				float startMyaRatio = startMya / farthestTimeMYA;
				float endMyaRatio = endMya / farthestTimeMYA;
				// trans to paint ratio
				startMyaRatio = 1 - startMyaRatio;
				endMyaRatio = 1 - endMyaRatio;

				startMyaRatio = startMyaRatio < 0 ? 0 : startMyaRatio;

				int startLocation = (int) (plotArea.x + startMyaRatio * plotArea.width);
				int endLocation = (int) (plotArea.x + endMyaRatio * plotArea.width);

				g2d.setColor(timeScaleEntity.getColor());
				int width = endLocation - startLocation;
				g2d.fillRect(startLocation, plotArea.y, width, eachYHeight);

				g2d.setColor(Color.black);
				String name = timeScaleEntity.getName();
				int stringWidth = fontMetrics.stringWidth(name);
				if (stringWidth > width) {
					name = name.substring(0, 2);
					stringWidth = fontMetrics.stringWidth(name);
				}
				g2d.drawString(name, (startLocation + endLocation - stringWidth) / 2,
						(int) (plotArea.y	 + (ascent + eachYHeight) / 2));

			}

		}
	}
}
