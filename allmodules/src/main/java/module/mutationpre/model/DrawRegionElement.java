package module.mutationpre.model;

import java.awt.Color;
import java.awt.Point;

import org.apache.commons.lang3.tuple.Pair;

public class DrawRegionElement {

	public Pair<Point, Integer> location;
	public Color color = Color.black;
	public String label;
	
	public int downMovedDistance = 0;

}
