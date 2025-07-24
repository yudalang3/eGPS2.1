package module.chorddiagram.work;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.compress.utils.Lists;

import egps2.frame.gui.EGPSMainGuiUtil;
import graphic.engine.guicalculator.BlankArea;
import egps2.frame.gui.dialog.DialogMoreInfoGenerator;

public class PaintingPanel extends JPanel implements MouseListener {

    OneChordDiagramDrawer drawer = new OneChordDiagramDrawer();

	PlotDataModel plotDataModel = new PlotDataModel();

	BlankArea blankArea = new BlankArea(20, 20, 20, 20);

    public PaintingPanel() {
        setBackground(Color.white);
        // TODO 帮我写一个鼠标的监听：要求是当用户的鼠标放置在某个实体上，然后显示该实体的名称。
        //  以 tooltip的形式显示。当用户右键点击实体时，可以弹出一个对话框，显示该实体的详细信息。当用户左键点击的时候，高亮这个实体
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 在这里进行绘图操作


        Graphics2D g2d = (Graphics2D) g;
		EGPSMainGuiUtil.setupHighQualityRendering(g2d);

		List<ChordDiagram4WntBean> chordData = plotDataModel.getChordData();
		if (chordData.isEmpty()) {
            return;
        }

		int size = chordData.size();
		int numberOfColumn = plotDataModel.getNumberOfColumn();
		int numOfRows = (int) Math.ceil((double) size / numberOfColumn);
		
		int aviWidth = getWidth() - blankArea.getLeft() - blankArea.getRight();
		int aviHeight = getHeight() - blankArea.getTop() - blankArea.getBottom();
		int eachWidth = aviWidth / numberOfColumn;
		int eachHeight = aviHeight / numOfRows;
		int index = 0;
		outerLoop: // 定义一个标签
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numberOfColumn; j++) {
				if (index == size) {
					break outerLoop; // 跳出两层循环
				}
				int xx = blankArea.getLeft() + eachWidth * j;
				int yy = blankArea.getTop() + eachHeight * i;

				ChordDiagram4WntBean chordDiagram = chordData.get(index);
				Rectangle rectangle = new Rectangle(xx, yy, eachWidth, eachHeight);
				drawChord(g2d, chordDiagram, rectangle);
				index++;
			}
		}


    }

    private void drawChord(Graphics2D g2d, ChordDiagram4WntBean chordDiagram, Rectangle rectangle) {
//		
//		Stroke oldStroke = g2d.getStroke();
//	     // 创建一个自定义的虚线 Stroke
//        float[] dashPattern = {10, 5}; // 10 像素的线段，5 像素的空白
//        Stroke dashedStroke = new BasicStroke(
//				0.5f, // 线宽
//            BasicStroke.CAP_BUTT,   // 线段末端样式
//				BasicStroke.JOIN_ROUND, // 拐角样式
//            10,                     // 折线拐角的限制长度
//            dashPattern,            // 虚线模式
//            0                       // 偏移量
//        );
//
//        // 应用虚线样式
//        g2d.setStroke(dashedStroke);
//        g2d.draw(rectangle);
//        g2d.setStroke(oldStroke);

		drawer.draw(g2d, chordDiagram, rectangle);
		g2d.drawString(chordDiagram.getName(), rectangle.x, rectangle.y);

    }

	public PlotDataModel getPlotDataModel() {
		return plotDataModel;
	}


    @Override
    public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			this.repaint();
		} else {
			Point point = e.getPoint();
			
			List<ChordDiagram4WntBean> chordData = plotDataModel.getChordData();
			if (chordData.isEmpty()) {
	            return;
	        }
			boolean isSearched = false;

			int size = chordData.size();
			int numberOfColumn = plotDataModel.getNumberOfColumn();
			int numOfRows = (int) Math.ceil((double) size / numberOfColumn);
			
			int aviWidth = getWidth() - blankArea.getLeft() - blankArea.getRight();
			int aviHeight = getHeight() - blankArea.getTop() - blankArea.getBottom();
			int eachWidth = aviWidth / numberOfColumn;
			int eachHeight = aviHeight / numOfRows;
			int index = 0;
			outerLoop: // 定义一个标签
			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < numberOfColumn; j++) {
					if (index == size) {
						break outerLoop; // 跳出两层循环
					}
					int xx = blankArea.getLeft() + eachWidth * j;
					int yy = blankArea.getTop() + eachHeight * i;


					Rectangle rectangle = new Rectangle(xx, yy, eachWidth, eachHeight);
					if (rectangle.contains(point)) {
						isSearched = true;
						break outerLoop;
					}
					index++;
				}
			}
			
			if (isSearched) {
				ChordDiagram4WntBean chordDiagram = chordData.get(index);
				ArrayList<String> list = Lists.newArrayList();
				String name = chordDiagram.getName();
				list.add(name);
				List<BioInteraction> interactionList = chordDiagram.getInteractionList();
				for (BioInteraction interaction : interactionList) {
					list.add(interaction.toString());
				}

				DialogMoreInfoGenerator dialog = new DialogMoreInfoGenerator(list);
				dialog.setTitle("Interaction line information");
				dialog.setVisible(true);
			}

		}
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
