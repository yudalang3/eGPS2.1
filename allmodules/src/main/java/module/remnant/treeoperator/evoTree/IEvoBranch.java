package module.remnant.treeoperator.evoTree;

public interface IEvoBranch {
	/** set branch length*/
	void setLength(double length);
	/** get branch length*/
	double getLength();
	/** get evolutional time represented by this branch*/
	double getTime();
	/** set evolutional time represented by this branch*/
	void setTime(double time);
	/** set mutation count*/
	void setMutation(int mutation);
	/** get mutation count*/
	int getMutation();
	
	void setName(String name);
	String getName();

}
