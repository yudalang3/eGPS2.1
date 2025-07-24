package module.mutationpre.model;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class DrawPropertiesMutation {
	
	// 这里是弧度
	public double degree = Math.toRadians(-60);
	
	public boolean showPositionCoordinate = false;
	public double positionCoordinateDegree = Math.toRadians(-90);
	
	public List<DrawConnectingLineElement> listofConnectingLineElements = new ArrayList<>();
	
	public List<DrawVariantsProperty> listOfDrawVariants = new ArrayList<>();
	
	public int leftMostX;

	public Font mutationFont;

	public int drawWidth;

}
