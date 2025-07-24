package module.evolview.gfamily.work.model.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * 应该是线的加粗等。
 *
 */
public class LineLabel extends JComponent {
	
	private static final long serialVersionUID = 8941278275477509628L;
	
	// before journal of heredity
 //   protected Border focusBorder = BorderFactory.createDashedBorder(new Color(0,0,182,155), (float)7.0, (float)5.0);
//	protected Border focusBorder = BorderFactory.createDashedBorder(Color.GREEN, (float)3.0, (float)1.0);
	protected Border focusBorder = BorderFactory.createDashedBorder(new Color(0,0,182,155), (float)1.5, (float)2.0, (float)0.5, true);
    protected Dimension dimension = new Dimension(10, 10);
    
    private static LineLabel instance = null;
    
	public static LineLabel getInstance() {
		if(instance == null) {
			instance = new LineLabel();
		}
		return instance;
	}
	
    public Component getRendLabel() {
        setOpaque(false);
    	setBorder(focusBorder);
        setPreferredSize(dimension);
        repaint();
        return this;
    }

    @Override
	public void paint(Graphics g) {
        getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
    }
    
}
