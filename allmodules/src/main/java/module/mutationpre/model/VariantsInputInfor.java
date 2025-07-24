package module.mutationpre.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class VariantsInputInfor {

	Collection<Integer> paintedPositions;
	
	Set<Integer> intesectionPositionSet;
	
	List<VariantInfo> variants = new ArrayList<>();
	
	int rotationDegree  = 0;
	int positionCoordinateRotationDegree = 90;
	boolean showPositionCoordinate = false;

	public int mutationInputType = 0;
	
}
