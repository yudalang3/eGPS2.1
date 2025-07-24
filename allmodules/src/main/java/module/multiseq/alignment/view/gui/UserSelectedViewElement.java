package module.multiseq.alignment.view.gui;

/**
 * The implementation schema:
 * 
 * <pre>
 * 
 *  （displayedStartRes, displayedStartSeq）
 *       |--------------------------------------------------|
 *       |                                                  |
 *       |             column number                        |
 *       |       (x,y) |----------|                         |
 *       |             |          |                         |
 *       |             |          |  row number             |
 *       |             |          |                         |
 *       |             |          |                         |
 *       |             |----------|                         |
 *       |                                                  |
 *       |                                                  |
 *       |                                                  |
 *       |                                                  |
 *       |--------------------------------------------------|
 * </pre>
 * 
 * @title SelectionElement
 * @createdDate 2020-12-22 14:35
 * @lastModifiedDate 2020-12-22 14:35
 * @author yudalang
 * @since 1.7
 *
 */
public class UserSelectedViewElement {

	/** The horizontal start position of the selected Seqs */
	private int xPos;

	/** The starting position of the Residues is selected */
	private int yPos;

	// number of Seqs selected per row
	private int selectRowNum;

	// number of Seqs selected per column
	private int selectColumnNum;

	/**
	 * @return the {@link #xPos}
	 */
	public int getxPos() {
		return xPos;
	}

	/**
	 * @param xPos the {@link #xPos} to set
	 */
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	/**
	 * @return the {@link #yPos}
	 */
	public int getyPos() {
		return yPos;
	}

	/**
	 * @param yPos the {@link #yPos} to set
	 */
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	/**
	 * @return the {@link #selectRowNum}
	 */
	public int getSelectRowNum() {
		return selectRowNum;
	}

	/**
	 * @param selectRowNum the {@link #selectRowNum} to set
	 */
	public void setSelectRowNum(int selectRowNum) {
		this.selectRowNum = selectRowNum;
	}

	/**
	 * @return the {@link #selectColumnNum}
	 */
	public int getSelectColumnNum() {
		return selectColumnNum;
	}

	/**
	 * @param selectColumnNum the {@link #selectColumnNum} to set
	 */
	public void setSelectColumnNum(int selectColumnNum) {
		this.selectColumnNum = selectColumnNum;
	}

	@Override
	public String toString() {
		return "The horizontal start position of each row is " + (xPos + 1)
				+ ". The starting position of the Sequence is " + (yPos + 1) + ", width is " + selectColumnNum + " and height is " + selectRowNum;
	}

}
