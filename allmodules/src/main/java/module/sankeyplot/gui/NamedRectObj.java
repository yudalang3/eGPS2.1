package module.sankeyplot.gui;

import egps2.frame.gui.interacive.RectObj;
import egps2.utils.common.model.datatransfer.ThreeTuple;

public class NamedRectObj extends RectObj{
	
	private String name;
	private ThreeTuple<Integer, Integer, Integer> color;
	private boolean selected = false;
	
	public NamedRectObj(double x, double y, double w, double h) {
		super(x, y, w, h);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ThreeTuple<Integer, Integer, Integer> getColor() {
		return color;
	}

	public void setColor(ThreeTuple<Integer, Integer, Integer> color) {
		this.color = color;
	}

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

	@Override
	public int getCursorType() {
		return 0;
	}

	@Override
	public void adjustPaintings(double d, double e) {
		
	}
	
}