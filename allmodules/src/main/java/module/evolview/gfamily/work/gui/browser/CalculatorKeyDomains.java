package module.evolview.gfamily.work.gui.browser;

import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import module.evolview.gfamily.work.beans.TrackMultipleDomainBean;
import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyKeyDomains;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;

/**
 * 需要注意的是当绘制ElementStructure的位置信息的时候,包含起始位置(左闭右闭原则)
 *
 * @author mhl
 */
public class CalculatorKeyDomains implements LocationCalculator<DrawingPropertyKeyDomains> {

	private final double blinkTopSpaceLength = 15 + BLINKTOPSPACELENGTH; // 顶部留白
	private final int subTrackHeight = 20; // subtrack的高度
	private final int subTrackRegionDist = 7; // 每个 subTrack 的间距
	
	private List<DrawingPropertySequenceModel> genomeElementModelItems ;
	private GeneStructureInfo geneStructureInfo;
	
	private int roundRectArcSize = 8; //圆矩形的 圆润程度

	public CalculatorKeyDomains(BrowserPanel genomeMain, TrackMultipleDomainBean drawingPropertyElementModelItem) {
		geneStructureInfo = genomeMain.getGeneStructureInfo();
		initialData(drawingPropertyElementModelItem);
		
	}

	/**
	 * 初始化绘制ElementStructure图形的数据
	 * @param genomeElementModelItem 
	 */
	public void initialData(TrackMultipleDomainBean genomeElementModelItem) {
		
		genomeElementModelItems = ImmutableList.copyOf(genomeElementModelItem.getMultiTracksOfSeqElements());
	}

	public List<DrawingPropertySequenceModel> getGenomeElementModelItems() {
		return genomeElementModelItems;
	}

	public DrawingPropertyKeyDomains calculatePaintingLocations(GeneDrawingLengthCursor drawProperties,
			int width, int height) {
		
		int charHeight = drawProperties.getCharHeight();
		int seqStructElementHeight = subTrackHeight;
		
		int drawStart = drawProperties.getDrawStart();
		int drawEnd = drawProperties.getDrawEnd();
		double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;
		int showCountlength = drawEnd - drawStart;
		double xRange = availableWidth / showCountlength;

		int index = 0;
		for (DrawingPropertySequenceModel genomeElementModelItem : genomeElementModelItems) {
			List<DrawingPropertyElement> resultElementModels = new ArrayList<>();

			List<DrawingPropertyElement> genomeElementModels = genomeElementModelItem.getOrigianlSeqElementModels();
			double subTrackYAxis = blinkTopSpaceLength + index * subTrackHeight + subTrackRegionDist * index;
			for (DrawingPropertyElement genomeElementModel : genomeElementModels) {
				DrawingPropertyElement resultElementModel = genomeElementModel.cloneThis();

				RoundRectangle2D.Double rectangle = new RoundRectangle2D.Double();
				int startLocation = resultElementModel.getStartPosition();
				int endLocation = resultElementModel.getEndPositoin() + 1;
				if (startLocation > drawEnd || endLocation < drawStart) {
					continue;
				}
				
				double x1 = startLocation >= drawStart ? BLINK_LEFT_SPACE_LENGTH + (startLocation - drawStart) * xRange
						: BLINK_LEFT_SPACE_LENGTH - (drawStart - startLocation) * xRange;
				double lineWidth = (endLocation - startLocation) * xRange;
				
				rectangle.setRoundRect(x1, subTrackYAxis, lineWidth, seqStructElementHeight, roundRectArcSize, roundRectArcSize);
				resultElementModel.setRoundRect(rectangle);
				resultElementModels.add(resultElementModel);
			}
			genomeElementModelItem.setGenomeElementModels(resultElementModels);
			genomeElementModelItem.setSubTrackLocator( new Point(15, (int) (subTrackYAxis + 0.5 * seqStructElementHeight)));
			index++;
		}

		DrawingPropertyKeyDomains paintElements = new DrawingPropertyKeyDomains();
		paintElements.setTrackName(SEQENCE_ELEMENT_TRACK_NAME);
		paintElements.setTrackNameXLocation(7);
		paintElements.setTrackNameYLocation(BLINKTOPSPACELENGTH);
		paintElements.setGenomePaintElementItems(genomeElementModelItems);
		return paintElements;
	}

}
