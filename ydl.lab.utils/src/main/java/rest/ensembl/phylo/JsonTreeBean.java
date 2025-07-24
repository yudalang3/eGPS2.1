package rest.ensembl.phylo;

public class JsonTreeBean {

	String type;
	int rooted;
	String id;
	
	TreeBean tree;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRooted() {
		return rooted;
	}

	public void setRooted(int rooted) {
		this.rooted = rooted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TreeBean getTree() {
		return tree;
	}

	public void setTree(TreeBean tree) {
		this.tree = tree;
	}
	
	
	
}
