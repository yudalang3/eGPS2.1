package module.evolview.gfamily.work.gui.tree;

public abstract class RectObj implements RectAdjustMent{
	public double x;
	public double y;
	public double w;
	public double h;
	
	protected boolean available;
	protected double dx;
	protected double dy;

	public RectObj(double x, double y, double w, double h) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public RectObj() {}

	@Override
	public boolean contains(double x, double y) {
		double x0 = this.x;
		double y0 = this.y;
		return (x >= x0 && y >= y0 && x < x0 + w && y < y0 + h);
	}

	@Override
	public boolean isAvailable() {
		return available;
	}

	@Override
	public String toString() {
		return x + "\t" + y + "\t" + w + "\t" + h;
	}

	public void setFirstPressedPoint(double newX,double newY) {
		dx = this.x - newX;
		dy = this.y - newY;
	}
	@Override
	public abstract void adjustPaintings(double d, double e) ;
}
