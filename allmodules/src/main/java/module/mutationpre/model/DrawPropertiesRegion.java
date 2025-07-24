package module.mutationpre.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DrawPropertiesRegion {

	public Point point = new Point(100, 150);
	public int height = 30;

	/**
	 * Following two array list should has the same size.
	 */
	public List<DrawRegionElement> regions = new ArrayList<>();
	
	public List<DrawConnectingLineElement> listOfConnectingLineElements = new ArrayList<>();
	
	
	public Font regionLabelFont;
	public Color regionLabelColor;
	public int roundRectangularCurvature;
	public boolean hasRegionBorder;
	public Color regionBorderColor;
	public float regionBorderLineWidth;
	public boolean regionShowMutationVerticalLine;

}
