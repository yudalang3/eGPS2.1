package module.evoltre.mutation;

public interface IMutation4Rec extends IMutation {

	boolean isRecFlag() ;

	void setRecFlag(boolean rec) ;
	
	int getMutationSize() ;

	/**
	 * 这个MutationSize就是突变所在的节点的size，也就是叶子数量
	 * @param mutationSize
	 */
	void setMutationSize(int mutationSize);
}
