package module.multiseqview.model.paint;

import java.util.List;

public class AlongSequenceDirection {
	
	int x;
	int y;
	int height;
	int width;
	
	String name;
	
	List<RectPaintObj> listRectPaintObjs;
	
	List<CrossSequenceDirectionPaintObj> listcCrossSequenceDirectionPaintObjs;
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public List<RectPaintObj> getListRectPaintObjs() {
		return listRectPaintObjs;
	}

	public void setListRectPaintObjs(List<RectPaintObj> listRectPaintObjs) {
		this.listRectPaintObjs = listRectPaintObjs;
	}

	public List<CrossSequenceDirectionPaintObj> getListcCrossSequenceDirectionPaintObjs() {
		return listcCrossSequenceDirectionPaintObjs;
	}

	public void setListcCrossSequenceDirectionPaintObjs(
			List<CrossSequenceDirectionPaintObj> listcCrossSequenceDirectionPaintObjs) {
		this.listcCrossSequenceDirectionPaintObjs = listcCrossSequenceDirectionPaintObjs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
