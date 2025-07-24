package module.remnant.treeoperator;

public class DefaultBranch implements Branch{
	private int mutation;
	
    private double length = 0.0;
    private double weight=1.0e0;
    private String name;
    private int size;
    private Object obj;
    
    
    // maybe this strategy will in the cost of running time
    /*
    private float branchWidth = 1.6f;
    private Color branchColor = Color.BLACK;
	private int branchStyle = 0;
	private BasicStroke branchStroke = new BasicStroke(1.6f);   
    */
    

	public int  getSize(){ return size;}
    public void setSize(int k){ size = k;}
    public void setLength(double a){length=a;}
    public double getLength(){return length;}
    public void   setWeight(double weight){this.weight=weight;}
    public double getWeight(){return weight;}
    public void   setUserObject(Object a){obj = a;}
    public Object getUserObject(){return obj;}
    
    public void setName(String name) {
    	this.name=name;
    }
    
    public String getName(){
    	return name;
    }
    
    public String toString() {
        return null;
    }
    
    public void clear() {
        setLength(0.0e0);
        setWeight(1.0e0);
        setName(null);
        setSize(0);
    }
    
    /**
     *	 shadow clone a node
     */
    public DefaultBranch getClone(){
        DefaultBranch newBranch = new DefaultBranch();
        newBranch.setLength( getLength());
        newBranch.setName( getName());
        newBranch.setSize( getSize());
        return newBranch;
    }
    
	@Override
	public void setMutation(int mutation) {
		this.mutation = mutation;
	}
	
	@Override
	public int getMutation() {
		return mutation;
	}
}

