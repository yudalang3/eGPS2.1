package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceElement;

public class MouseListenerGeneStructure extends AbstractMouseListener {

    private boolean startDrag;
    private boolean endDrag;
    private boolean moveDrag;
    private int trackHeight = 200;

    public MouseListenerGeneStructure(BrowserPanel genomeMain) {

        super(genomeMain);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        TrackGeneStructure virusGeomeStructure = genomeMain.getVirusGeomeStructure();

        AbstractTrackTooltiper shape = virusGeomeStructure.getShape();
        Double rectangle = virusGeomeStructure.getRectangle();
        Double startDragBlock = new Double(rectangle.x, rectangle.y, 5, rectangle.height);
        Double endDragBlock = new Double(rectangle.x + rectangle.width - 2, rectangle.y, 5,
                rectangle.height);
        Point point = e.getPoint();
        if (shape.contains(point.getX(), point.getY())) {
            startDrag = false;
            endDrag = false;
            moveDrag = false;
            genomeMain.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else if (startDragBlock.contains(point)) {
            startDrag = true;
            genomeMain.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        } else if (endDragBlock.contains(point)) {
            endDrag = true;
            genomeMain.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        } else if (rectangle.contains(point)) {
            startDrag = false;
            endDrag = false;
            moveDrag = true;
            genomeMain.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
            startDrag = false;
            endDrag = false;
            moveDrag = false;
            genomeMain.setCursor(Cursor.getDefaultCursor());
        }
        if (moveBlankArea(point, virusGeomeStructure)) {
            virusGeomeStructure.setHighlightShow(true);
            genomeMain.updateUI();
        } else {
            virusGeomeStructure.setHighlightShow(false);
            genomeMain.updateUI();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        TrackGeneStructure virusGeomeStructure = genomeMain.getVirusGeomeStructure();
        virusGeomeStructure.setHighlightShow(false);
        genomeMain.updateUI();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        TrackGeneStructure virusGeomeStructure = genomeMain.getVirusGeomeStructure();

        GeneDrawingLengthCursor drawProperties = virusGeomeStructure.getDrawProperties();
        CalculatorGeneStructure locationCalculator = (CalculatorGeneStructure) virusGeomeStructure
                .getLocationCalculator();

        if (startDrag) {
            int caculatorDrawLocation = locationCalculator.caculatorDrawLocation(e.getPoint(),
                    virusGeomeStructure.getWidth(), virusGeomeStructure.getHeight());

            int drawEnd = drawProperties.getDrawEnd();

            if (caculatorDrawLocation >= drawEnd) {
                endDrag = true;
                startDrag = false;
            } else {
                drawProperties.setDrawStart(caculatorDrawLocation);
            }
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.repaint();
        } else if (endDrag) {
            int caculatorDrawLocation = locationCalculator.caculatorDrawLocation(e.getPoint(),
                    virusGeomeStructure.getWidth(), virusGeomeStructure.getHeight());

            int drawStart = drawProperties.getDrawStart();

            if (caculatorDrawLocation <= drawStart) {
                startDrag = true;
                endDrag = false;
            } else {
                drawProperties.setDrawEnd(caculatorDrawLocation);
            }
            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.repaint();
        } else if (moveDrag) {
            if (startMovePoint == null) {
                return;
            }
            int startDrawLocation = locationCalculator.caculatorDrawLocation( startMovePoint,
                    virusGeomeStructure.getWidth(), virusGeomeStructure.getHeight());
            int endDrawLocation = locationCalculator.caculatorDrawLocation( e.getPoint(),
                    virusGeomeStructure.getWidth(), virusGeomeStructure.getHeight());
            int movingDistance = endDrawLocation - startDrawLocation;
            int drawStart = drawProperties.getDrawStart();
            int drawEnd = drawProperties.getDrawEnd();

            int originalDistance = drawEnd - drawStart;
            int newStart = drawStart + movingDistance;
            int minValue = drawProperties.getMinValue();

            int newEnd = drawEnd + movingDistance;
            int maxValue = drawProperties.getMaxValue();
            if (newStart <= minValue) {
                drawProperties.setDrawStart(minValue);
                drawProperties.setDrawEnd(minValue + originalDistance);
            } else if (newEnd >= maxValue) {
                drawProperties.setDrawStart(maxValue - originalDistance);
                drawProperties.setDrawEnd(maxValue);
            } else {
                drawProperties.setDrawStart(newStart);
                drawProperties.setDrawEnd(newEnd);
            }
            startMovePoint = e.getPoint();

            genomeMain.getController().getLeftGenomeBrowser().reSetValue();
            genomeMain.repaint();
        }
    }

    private Point startMovePoint;

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (moveDrag) {
            startMovePoint = e.getPoint();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        TrackGeneStructure virusGeomeStructure = genomeMain.getVirusGeomeStructure();
        setTrack(virusGeomeStructure);
        super.mouseClicked(e);
        List<DrawingPropertySequenceElement> paintVirusStrainsInfoAll = virusGeomeStructure.getPaintVirusStrainsInfoAll();
        for (DrawingPropertySequenceElement paintVirusStrainsInfo : paintVirusStrainsInfoAll) {
            Double rectangle = paintVirusStrainsInfo.getRectangle();
            if (rectangle.contains(point.x, point.y)) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Component component = e.getComponent();
                    PopupMenuGeneStructure popupMenu = new PopupMenuGeneStructure(genomeMain);
                    JPopupMenu popup = popupMenu.getViewGenePopupMenu(paintVirusStrainsInfo.getStartPose(), paintVirusStrainsInfo.getEndPose());
                    popup.show(component, e.getX(), e.getY());
                }
                break;
            }
        }


//        for (PaintVirusStrainsInfo secondPaintBlock : secondPaintBlocks) {
//            Rectangle2D.Double rectangle = secondPaintBlock.getRectangle();
//            if (rectangle.contains(point.x, point.y)) {
//                if (SwingUtilities.isRightMouseButton(e)) {
//                    Component component = e.getComponent();
//                    VirusGenomeStructurePopupMenu popupMenu = new VirusGenomeStructurePopupMenu(genomeMain);
//                    JPopupMenu popup = popupMenu.getViewGenePopupMenu(secondPaintBlock.getStartPose(), secondPaintBlock.getEndPose());
//                    popup.show(component, e.getX(), e.getY());
//                }
//                break;
//            }
//        }
    }
    
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    	
    	TrackGeneStructure virusGeomeStructure = genomeMain.getVirusGeomeStructure();

        GeneDrawingLengthCursor drawProperties = virusGeomeStructure.getDrawProperties();
        int drawStart = drawProperties.getDrawStart();
        int drawEnd = drawProperties.getDrawEnd();
    	
    	int movingDistance = e.getScrollAmount() *100;
    	
    	if (movingDistance > drawEnd - drawStart) {
    		movingDistance = e.getScrollAmount();
		}
    	
    	if (e.getWheelRotation() < 0) {
			// 放大
    		movingDistance = - movingDistance;
		}else {
			//缩小
		}
    	
        
        int originalDistance = drawEnd - drawStart;
        int newStart = drawStart - movingDistance;
        int newEnd = drawEnd + movingDistance;
        
        int minValue = drawProperties.getMinValue();
        int maxValue = drawProperties.getMaxValue();
        
        //处理新坐标的长和宽
        if (newEnd < newStart) {
        	int temp = newEnd;
        	newEnd = newStart;
        	newStart = temp;
		}
        if (newEnd - newStart < 20) {
        	newEnd = newStart + 20;
		}
        
        //处理边界
        if (newStart <= minValue) {
            drawProperties.setDrawStart(minValue);
            drawProperties.setDrawEnd(minValue + originalDistance);
        } else if (newEnd >= maxValue) {
            drawProperties.setDrawStart(maxValue - originalDistance);
            drawProperties.setDrawEnd(maxValue);
        } else {
            drawProperties.setDrawStart(newStart);
            drawProperties.setDrawEnd(newEnd);
        }
        
       

        genomeMain.getController().getLeftGenomeBrowser().reSetValue();
        genomeMain.repaint();
    }
}
