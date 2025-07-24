package module.evoltre.mutation;

public interface IMutation extends Comparable<IMutation>{
	
	public static final char LAST_POSITION_INSERTION_SPLIT_CHAR = '`';
	
	int getPosition();
	void setPosition(int position);

	String getAncestralState();
	void setAncestralState(String ancestralState);

	String getDerivedState();
	void setDerivedState(String derivedState);
	
	/**
	 * 
	 * 整个突变的全貌
	 * 主要是针对Indel而言，SNP就一个原位的信息而已。
	 * Indel有可能片段很长，我用...来表示。如果想要查看全部的信息，那么要调用这个方法。
	 *  
	 * @title getFullInformation
	 * @createdDate 2021-03-04 10:35
	 * @lastModifiedDate 2021-03-04 10:35
	 * @author yudalang
	 * @since 1.7
	 *   
	 * @return String
	 */
	String getFullInformation();

	
	int getLastPositionInsertLength();
	
	void setLastPositionInsertLength(int insertLen);
}
