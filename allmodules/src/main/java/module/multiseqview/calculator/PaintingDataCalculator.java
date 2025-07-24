package module.multiseqview.calculator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.multiseqview.model.Data4SequenceStructureView;
import module.multiseqview.model.Sequence4import;
import module.multiseqview.model.StructureElement;
import module.multiseqview.model.paint.AlongSequenceDirection;
import module.multiseqview.model.paint.CrossSequenceDirectionPaintObj;
import module.multiseqview.model.paint.PaintingData;
import module.multiseqview.model.paint.RectPaintObj;


public class PaintingDataCalculator {
	private int upBlinkLength = 80;
	private int downBlinkLength = 50;
	private int leftBlinkLength = 100;
	private int rightBlinkLength = 50;
	private int intervalMargin = 20;
	private int maxSequenceHeight = 30;

	private Data4SequenceStructureView simulateData;
	private double raito;
	private List<AlongSequenceDirection> lisaAlongSequenceDirections;

	public PaintingData getPatingData(int width, int height,Data4SequenceStructureView simulateData) {

		double avaliableWidth = width - leftBlinkLength - rightBlinkLength;
		double avaliableHeight = height - upBlinkLength - downBlinkLength;

		this.simulateData =simulateData;
		List<Sequence4import> sequences = simulateData.getSequences();
		int size = sequences.size();

		double verticalInterval = avaliableHeight / size;
		int eachSequenceHeight = (int) (verticalInterval - intervalMargin);

		if (eachSequenceHeight > maxSequenceHeight) {
			eachSequenceHeight = maxSequenceHeight;
		}

		int largestSequenceLen = 0;

		lisaAlongSequenceDirections = new ArrayList<AlongSequenceDirection>();

		for (int i = 0; i < size; i++) {
			AlongSequenceDirection alongSequenceDirection = new AlongSequenceDirection();
			alongSequenceDirection.setX(leftBlinkLength);
			alongSequenceDirection.setY((int) (verticalInterval * i + upBlinkLength));
			
			alongSequenceDirection.setHeight(eachSequenceHeight);
			lisaAlongSequenceDirections.add(alongSequenceDirection);
			Sequence4import sequence = sequences.get(i);
			alongSequenceDirection.setName(sequence.getName());

			int length = sequence.getLength();
			if (largestSequenceLen < length) {
				largestSequenceLen = length;
			}
		}

		raito = avaliableWidth / largestSequenceLen;

		calculateListOfAlongSequenceDirections();

		PaintingData ret = new PaintingData();
		ret.setAlongSequenceDirections(lisaAlongSequenceDirections);

		return ret;
	}

	/**
	 * 这是后面移动所需要的计算，
	 * 也就是说是一个相对轻量级的过程，只要吧一些坐标替换就行了
	 */
	public void calculateListOfAlongSequenceDirections() {
		
		List<Sequence4import> sequences = simulateData.getSequences();
		int size = sequences.size();
		
		Map<String, RectPaintObj> previousMap = null;
		for (int i = 0; i < size; i++) {
			AlongSequenceDirection alongSequenceDirection = lisaAlongSequenceDirections.get(i);
			Sequence4import sequence = sequences.get(i);

			List<RectPaintObj> listRectPaintObjs = new ArrayList<RectPaintObj>();

			int paintingWidth = (int) (sequence.getLength() * raito);

			Map<String, RectPaintObj> currentMap = new HashMap<>();
			List<StructureElement> structureElementLists = sequence.getStructureElementLists();

//			StructureElement qq = structureElementLists.get(0);
//			System.out.println(qq.getStart() +"   "+qq.getEnd());
			for (StructureElement structureElement : structureElementLists) {
				int start = structureElement.getStart();
				int end = structureElement.getEnd();

				RectPaintObj rectPaintObj = new RectPaintObj();
				rectPaintObj.setX(alongSequenceDirection.getX() + raito * start);
				rectPaintObj.setWidth((end - start) * raito);
				rectPaintObj.setName(structureElement.getName());
				rectPaintObj.setColor(Color.decode(structureElement.getColorString()));
				listRectPaintObjs.add(rectPaintObj);

				currentMap.put(structureElement.getName(), rectPaintObj);
			}

			List<CrossSequenceDirectionPaintObj> listcCrossSequenceDirectionPaintObjs = new ArrayList<>();
			if (i > 0) {
				// 配置纵向曲线
				AlongSequenceDirection previousAlongSequenceDirection = lisaAlongSequenceDirections.get(i - 1);
				for (RectPaintObj structureElement : listRectPaintObjs) {
					RectPaintObj tt = previousMap.get(structureElement.getName());
					if (tt != null) {
						CrossSequenceDirectionPaintObj crossSequenceDirectionPaintObj = new CrossSequenceDirectionPaintObj();
						crossSequenceDirectionPaintObj.xP1 = tt.getX();
						crossSequenceDirectionPaintObj.yP1 = previousAlongSequenceDirection.getY() + previousAlongSequenceDirection.getHeight();
//						crossSequenceDirectionPaintObj.yP1 = previousAlongSequenceDirection.getY() + 0.5 *previousAlongSequenceDirection.getHeight();
						
						crossSequenceDirectionPaintObj.xP2 = tt.getX() + tt.getWidth();
						crossSequenceDirectionPaintObj.yP2 = crossSequenceDirectionPaintObj.yP1;

						crossSequenceDirectionPaintObj.xP3 = structureElement.getX() + structureElement.getWidth();
						crossSequenceDirectionPaintObj.yP3 = alongSequenceDirection.getY();
//						crossSequenceDirectionPaintObj.yP3 = alongSequenceDirection.getY() + 0.5 *previousAlongSequenceDirection.getHeight();
						
						crossSequenceDirectionPaintObj.xP4 = structureElement.getX();
						crossSequenceDirectionPaintObj.yP4 = crossSequenceDirectionPaintObj.yP3;

						listcCrossSequenceDirectionPaintObjs.add(crossSequenceDirectionPaintObj);
					}
				}
			}

			previousMap = currentMap;

			alongSequenceDirection.setWidth(paintingWidth);
			alongSequenceDirection.setListRectPaintObjs(listRectPaintObjs);
			alongSequenceDirection.setListcCrossSequenceDirectionPaintObjs(listcCrossSequenceDirectionPaintObjs);
		}
		
	}

	
	public void setIntervalMargin(int intervalMargin) {
		this.intervalMargin = intervalMargin;
	}
}
