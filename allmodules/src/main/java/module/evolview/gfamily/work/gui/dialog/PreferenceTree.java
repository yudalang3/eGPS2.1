package module.evolview.gfamily.work.gui.dialog;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import egps2.builtin.modules.IconObtainer;

/**
* @author YFQ
* @date Nov 9, 2018 10:12:38 AM  
*/
public class PreferenceTree extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 3598843710998599211L;
	private Color defaultBackground = getBackground();
	private ImageIcon preferenceIcon = IconObtainer.get("preference.png");

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
	   
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	    
	    super.getTreeCellRendererComponent(tree, value, selected, expanded,
	      leaf, row, hasFocus);

		/*setForeground(Color.RED);  
		setTextSelectionColor(Color.yellow);
		setBackgroundSelectionColor(Color.WHITE);*/
		setBackgroundNonSelectionColor(defaultBackground);
		setIcon(preferenceIcon);

		return this;
   }
}
