package module.remnant.treeoperator;


public interface Branch extends GBranch{
	void clear();

	void setWeight(double weight);

	double getWeight();

	void setMutation(int mutation);

	int getMutation();

	void setSize(int size);

	int getSize();

	void setUserObject(Object obj);

	Object getUserObject();

	void setName(String name);

	String getName();
}