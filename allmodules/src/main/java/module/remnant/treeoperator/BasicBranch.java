package module.remnant.treeoperator;

public class BasicBranch implements GBranch{
	
	double length = 0;
	
	public BasicBranch(double len) {
		length = len;
	}

	public BasicBranch() {}

	@Override
	public double getLength() {
		return length;
	}
	@Override
	public void setLength(double length) {
		this.length = length;
	}
}
