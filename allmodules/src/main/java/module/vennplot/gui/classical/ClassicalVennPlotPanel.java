package module.vennplot.gui.classical;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Arrays;

import egps2.UnifiedAccessPoint;
import module.vennplot.model.ClassicalParameterModel;
import module.vennplot.model.SetItem;
import module.vennplot.util.AdjustAndCalculate;

/**
 * 
 */
public abstract class ClassicalVennPlotPanel {

	protected int r;

	protected double intersectionLength;
	
	protected Font defaultNameFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();


	public abstract void paint(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos);

	public void setCircleR(int r) {
		this.r = r;
	}

	public void setIntersectionLength(double intersectionLength) {

		this.intersectionLength = intersectionLength;
	}

	public int getMaxCount(SetItem[] setItems) {

		int maxCount = Arrays.asList(setItems).stream().mapToInt(setItem -> setItem.getSetLists().length).max()
				.getAsInt();

//		int maxCount = Integer.MIN_VALUE;
//
//		for (SetItem setItem : setItems) {
//			int count = setItem.getSetLists().length;
//
//			if (count > maxCount) {
//				maxCount = count;
//			}
//		}
		return maxCount;

	}

}
