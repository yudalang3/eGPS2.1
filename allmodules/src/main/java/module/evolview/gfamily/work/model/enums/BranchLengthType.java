package module.evolview.gfamily.work.model.enums;

public enum BranchLengthType {
	

	DIVERGENCE(0),
	MUTATION_COUNT(1);
	
	private BranchLengthType(int index) {
		this.index = index;
	}

	int index;
	String name;
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	
	public BranchLengthType getBranchLengthType(int index){
		switch (index) {
		case 0:
			
			break;

		default:
			break;
		}
		
		return DIVERGENCE;
	}
}
