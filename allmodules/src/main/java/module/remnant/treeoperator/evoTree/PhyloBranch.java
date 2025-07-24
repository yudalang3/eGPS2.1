package module.remnant.treeoperator.evoTree;

public class PhyloBranch implements IEvoBranch{
	
	private double time;
	private double length;
	private int mutaionCount;
	private String name;

	@Override
	public void setLength(double length) {
		this.length = length;
	}

	@Override
	public double getLength() {
		return length;
	}

	@Override
	public double getTime() {
		return time;
	}

	@Override
	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public void setMutation(int mutation) {
		this.mutaionCount = mutation;
	}

	@Override
	public int getMutation() {
		return mutaionCount;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
