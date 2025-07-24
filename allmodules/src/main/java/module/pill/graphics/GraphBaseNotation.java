package module.pill.graphics;

import java.awt.Color;
import java.awt.geom.RectangularShape;

/**
 * https://www.genome.jp/kegg/kegg3.html <br>
 * https://www.kegg.jp/kegg/xml/docs/
 */
public class GraphBaseNotation {

	/**
	 * KEGG identifier (kid) is a unique identifier
	 * https://www.genome.jp/kegg/kegg3.html
	 */
	private String identifier;
	/**
	 * 这个是entry自己的id，用来识别entry。和KEGG的定义无关，来自KGML文件
	 */
	private int id;

	private String name;
	private RectangularShape paintingShape;

	/**
	 * 前景就是线条
	 */
	private Color fgColor;
	/**
	 * 背景就是填充
	 */
	private Color bgColor;

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RectangularShape getPaintingShape() {
		return paintingShape;
	}

	public void setPaintingShape(RectangularShape paintingShape) {
		this.paintingShape = paintingShape;
	}

	public Color getFgColor() {
		return fgColor;
	}

	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
