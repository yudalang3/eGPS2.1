package module.mutationpre.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DrawPropertiesAxis {
	
	public String titleString = "Spike";
	public Point point = new Point(100,100);
	
	public int drawWidth = 500;
	public int tickMarkVerticalHeight = 10;
	
	public List<Point> tickMarkList = new ArrayList<>();
	
	public List<String> labels = new ArrayList<>();
	public Font titleFont;
	public Color titleColor;
	public Font labelFont;
	public float axisLineWidth;
	
	

}
