package module.pill.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GraphicsNodeShape implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int width = 0;
	public int height = 0;
	
	public List<Integer> xInteger = new ArrayList<>();
	public List<Integer> yInteger = new ArrayList<>();
	
	public String name;
	
}