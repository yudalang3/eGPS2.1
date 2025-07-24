package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class AbstractTrackPanel extends JPanel {
    protected final static Color backgroundColor = new Color(204, 255, 204);
    protected final GeneDrawingLengthCursor drawProperties;
    protected final BrowserPanel genomeMain;
    
    //在子类中会被初始化
    protected AbstractTrackTooltiper shape;
    protected boolean toolTipOn = true;
    //鼠标挪到一个track，这个track可以显示高亮
    protected boolean isHighlight;

    public AbstractTrackPanel(BrowserPanel genomeMain) {
        this.genomeMain = genomeMain;
        this.drawProperties = genomeMain.getDrawProperties();
    }

    public AbstractTrackTooltiper getShape() {
        return shape;
    }

    public BrowserPanel getGenomeMain() {
        return genomeMain;
    }

    public GeneDrawingLengthCursor getDrawProperties() {
        return drawProperties;
    }


    
    @Override
    public boolean contains(int x, int y) {
        if (toolTipOn) {
            if (shape == null) {
                setToolTipText(null);
            } else {
                synchronized (shape) {
                    if (shape.contains(x, y)) {
                        setToolTipText(shape.getToolTip());
                        //return true;
                    } else {
                        setToolTipText(null);
                    }
                }
            }
        } else {
            setToolTipText(null);
        }
        return super.contains(x, y);
    }

    public abstract void initialize();

    public void setHighlightShow(boolean isHighlight) {
        this.isHighlight = isHighlight;
    }
}
