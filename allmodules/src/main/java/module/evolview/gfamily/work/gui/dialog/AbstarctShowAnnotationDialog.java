package module.evolview.gfamily.work.gui.dialog;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;


@SuppressWarnings("serial")
public abstract class AbstarctShowAnnotationDialog extends DefaultMutableTreeNode {
	
	protected PhylogeneticTreePanel controller;
	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	public AbstarctShowAnnotationDialog(PhylogeneticTreePanel phyloTreePanel) {
		this.controller = phyloTreePanel;
	}

	/**
	 * Put back the right side of each node <code>JPanel</code>
	 * 
	 * @author mhl
	 * 
	 * @Date Created on: 2019-04-25 09:02
	 * 
	 */
	public abstract JComponent getViewJPanel();


	public abstract void configurateAnnotationProperty2preview();

}
