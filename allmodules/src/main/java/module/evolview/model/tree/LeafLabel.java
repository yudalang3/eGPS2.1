package module.evolview.model.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

public class LeafLabel extends JComponent {
	
	private static final long serialVersionUID = 8941278275477509628L;
	private String text;
	private boolean isSelected;
	private boolean isVisible;
    protected int baseline;
    
    // before journal of heredity
 //   protected Border focusBorder = BorderFactory.createDashedBorder(new Color(0,0,182,155), (float)10.0, (float)2.0);
	protected Border focusBorder = BorderFactory.createDashedBorder(new Color(0,0,182,155), (float)1.5, (float)5.0, (float)2.0, true);
	 
    //protected Border focusBorder = BorderFactory.createDashedBorder(Color.GREEN, (float)7.0, (float)2.0);
    
    protected FontMetrics metrics;
    protected Dimension dimension = new Dimension();
    
    private static LeafLabel instance = null;
    
	public static LeafLabel getInstance() {
		if(instance == null) {
			instance = new LeafLabel();
		}
		return instance;
	}
	

    @Override
	public void paint(Graphics g) {
        Insets insets = getInsets();
        if(isSelected) {
            if(text != null) {
                getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
               // g.setColor(Color.RED);
               // g.fillRect(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
            } 
        }
        if(isVisible) {
            g.setColor(getForeground());
            g.setFont(super.getFont());
            g.drawString(text, insets.left, baseline);
        }
    }

}
