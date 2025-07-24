package module.mutationpre.model;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

public class DrawPropertiesConnectingLine {
	
	public List<DrawConnectingLineElement> listOfMutationElement;
	
	
	public double curvature = 0.6;


	public float connectingLineWidth;


	public Color connectingLineColor;


	public Collection<Integer> allPaintedPositions;


	public List<ConnectingRegionRender> listOfConnectingRegionRenders;

}
