package module.evolview.gfamily.work.gui.tree.annotation;

public enum MarkStyle {

	SidewardCladeAnno("Sideward"), 
	INternalNodeInsitu("In situ"), 
	InternalNode2leaf("Node to leaf"),
	LeafNameAnno("Leaf names");

	public final String name;

	private MarkStyle(String name) {
		this.name = name;
	}
	
	public static String[] getMarkStyleItems() {
		String[] comboBoxLinageMarkStyleItems = { SidewardCladeAnno.name, 
				INternalNodeInsitu.name, 
				InternalNode2leaf.name,
				LeafNameAnno.name};
		return comboBoxLinageMarkStyleItems;
	}

	public static MarkStyle getMarkStyleFormIndex(int index) {
		MarkStyle ret = null;

		switch (index) {
		case 0:
			ret = SidewardCladeAnno;
			break;
		case 1:
			ret = INternalNodeInsitu;
			break;
		case 2:
			ret = InternalNode2leaf;
			break;

		default:
			ret = LeafNameAnno;
			break;
		}

		return ret;
	}
}
