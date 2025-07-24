package module.evolview.gfamily.work.gui.render;

public class CustomizedRenderRecord {
	
	int nodeID;
	
	boolean isSelf;
	
	String hexFormatColor;

	/**
	 * @return the nodeID
	 */
	public int getNodeID() {
		return nodeID;
	}

	/**
	 * @param nodeID the nodeID to set
	 */
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	/**
	 * @return the hexFormatColor
	 */
	public String getHexFormatColor() {
		return hexFormatColor;
	}

	/**
	 * @param hexFormatColor the hexFormatColor to set
	 */
	public void setHexFormatColor(String hexFormatColor) {
		this.hexFormatColor = hexFormatColor;
	}

	/**
	 * @return the isSelf
	 */
	public boolean isSelf() {
		return isSelf;
	}

	/**
	 * @param isSelf the isSelf to set
	 */
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	
	

}
