package module.mutationpre.model;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class DrawMutationElement {

	public Color color = Color.black;
	
	public String name;
	
	public boolean selected = false;
	
	public Rectangle rectangle = new Rectangle();
	public Point rotationPoint = new Point();
	
	/**
	 * 高亮的一些type, 如果是 -1的话，那就是 consensus
	 */
	public byte highlight = 0;
	
	private static int IDindex = 0;
	private final int ID;
	
	public DrawMutationElement() {
		ID = ++IDindex;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DrawMutationElement other = (DrawMutationElement) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	
}
