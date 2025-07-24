package module.evoltrepipline;

import java.awt.Font;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import egps2.UnifiedAccessPoint;

/**
 * @ClassName: AbstructPrefShowContent.java
 * @author ydl,mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-01-11 10:57
 * @modify 2024-04-02
 * 已经几乎改得面目全非了
 * 
 */
public abstract class AbstractPrefShowContent extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -8575222937280149555L;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	
//	protected final String elementKeyName = "Attribute";
//	protected final String elementValueName = "Value";

	/**
	 * Put back the right side of each node <code>JPanel</code>
	 * 
	 * @author mhl
	 * 
	 * @Date Created on: 2019-01-11 16:35
	 * 
	 */
	public abstract JPanel getViewJPanel();

	/**
	 * Save user preference setting parameters.
	 * 
	 * @author yudalang, mhl, change the value
	 * 已经改变很多了，和原来的地方都不一样了。
	 */
	public abstract void saveParameter(Map<String,String> parameters);

	/**
	 * Check whether user input is legitimate.If there is no text input in
	 * <code>JPanel</code>, it returns true by default.
	 * 
	 * @author mhl
	 * 
	 */
	public abstract boolean checkParameters(JTree jTree);

}
