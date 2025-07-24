package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;

public class AbstractMouseListener extends MouseAdapter {
    protected int trackWidth = LocationCalculator.BLINK_LEFT_SPACE_LENGTH;

    protected BrowserPanel genomeMain;

    
    protected AbstractTrackPanel track;

    /**
     * 右键点击左边空白区域 , 显示/隐藏当前Track
     *
     * @Author: mhl
     */
    public void setTrack(AbstractTrackPanel track) {
        this.track = track;
    }

    public AbstractMouseListener(BrowserPanel genomeMain) {
        this.genomeMain = genomeMain;
   
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int maxDrawLocation = genomeMain.getWidth() - LocationCalculator.BLINK_RIGHT_SPACE_LENGTH;
        Point point = e.getPoint();
        if (point.x < LocationCalculator.BLINK_LEFT_SPACE_LENGTH) {
            genomeMain.setInTheInterface(false);
        } else if (point.x > maxDrawLocation) {
            genomeMain.setInTheInterface(false);
        } else {
            genomeMain.setInTheInterface(true);
            genomeMain.setMoveLocation(point.x);
            
            //画了东西的宽度
            double drawLength=maxDrawLocation-LocationCalculator.BLINK_LEFT_SPACE_LENGTH;
            int posLength=genomeMain.getDrawProperties().getDrawEnd()-genomeMain.getDrawProperties().getDrawStart();
            double cellwidth=drawLength/(double)posLength;//一个核苷酸所占宽度
            genomeMain.getDrawProperties().setMousePos((int)(genomeMain.getDrawProperties().getDrawStart()+(point.x-LocationCalculator.BLINK_LEFT_SPACE_LENGTH)/cellwidth));

        }
        genomeMain.updateUI();
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {//右键点击左边空白区域 , 显示/隐藏当前Track
        Point point = e.getPoint();
        if (moveBlankArea(point, track)) {
            if (SwingUtilities.isRightMouseButton(e)) {
                Component component = e.getComponent();
                HideTrackPopupMenu popupMenu = new HideTrackPopupMenu(genomeMain);
                JPopupMenu popup = popupMenu.setTracksHidePopupMenu();
                popup.show(component, e.getX(), e.getY());
            }
        }
    }
    
    protected JMenuItem getViewPosJMenuItem() {
    	JMenuItem viewPosMenuItem = new JMenuItem("View this position");
        viewPosMenuItem .addActionListener(e -> {
        	
            GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
          
			int start = 1;
			int end = 29903;
			if (drawProperties.getMousePos() < 101) {
				start = 1;
				end = drawProperties.getMousePos() + 100;
			} else if (drawProperties.getMousePos() > 29803) {
				start = drawProperties.getMousePos() - 100;
				end = 29903;
			} else {
				start = drawProperties.getMousePos() - 100;
				end =drawProperties.getMousePos()+100;
            }
            drawProperties.setDrawStart(start);
            drawProperties.setDrawEnd(end + 1);
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.updateUI();
        });
        return viewPosMenuItem;
    }

    /**
     * 判断point是否在左边空白区域.
     *
     * @Author: mhl
     * @Date Created on: 2020-07-03 16:22
     */
    public boolean moveBlankArea(Point point, AbstractTrackPanel track) {
    	Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, trackWidth, track.getHeight());
        if (rectangle2d.contains(point.x, point.y)) {
            return true;
        }
        return false;
    }
}
