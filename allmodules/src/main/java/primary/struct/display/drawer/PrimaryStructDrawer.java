package primary.struct.display.drawer;

import blast.parse.BlastHspRecord;
import com.google.common.collect.Lists;
import egps2.frame.gui.EGPSMainGuiUtil;
import egps2.utils.common.util.SaveUtil;
import graphic.engine.colors.EGPSColors;
import org.apache.commons.lang3.tuple.Pair;
import primary.struct.display.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class PrimaryStructDrawer extends JPanel {
    private Point selectedRectangleIndex = new Point(-1, -1);
    private Point previousMousePoint; // 上一个鼠标位置

    // 矩形的信息
    private List<Rectangle[]> rectangleLists = Lists.newArrayList();
    private List<String[]> eachGeneDomains = Lists.newArrayList();
    private List<Color[]> colorLists = Lists.newArrayList();

    private List<String> geneNames = Lists.newArrayList();
    private List<Integer> geneLength = Lists.newArrayList();
    private List<Rectangle> geneBackgroundRects = Lists.newArrayList();

    // Connecting lines
    private List<BlastHspRecord> listOneConnectingLinesFirst2Second = Lists.newArrayList();
    private List<BlastHspRecord> listOneConnectingLinesSecond2Third = Lists.newArrayList();
    final Color connectingFillingColor1;
    private List<BlastHspRecord> listTwoConnectingLinesFirst2Second = Lists.newArrayList();
    private List<BlastHspRecord> listTwoConnectingLinesSecond2Third = Lists.newArrayList();
    final Color connectingFillingColor2;

    // visual parameters
    final int startYLocation = 50;
    final int geneVerticalSpaceGap = 80;

    final int startXLocation = 200;
    final int geneHeight = 20;
    final int geneBackgroundHeight = 4;

    private PairwiseHomologyData pairwiseHomologyData;

    public PrimaryStructDrawer() {
        Color[] colors = EGPSColors.getPredined9999PercPeopleReconColors();
        connectingFillingColor1 = new Color(1,1,250,30);
        connectingFillingColor2 = new Color(250,1,1,30);
//        connectingFillingColor1 = new Color(199,237,204,80);
//        connectingFillingColor2 = new Color(190,190,190,80);
//        connectingFillingColor1 = EGPSColors.newColorWithAlpha(colors[0], 60);
//        connectingFillingColor2 = EGPSColors.newColorWithAlpha(colors[2], 60);
    }

    public PrimaryStructDrawer(GeneDataV2 geneData) {
        this();
        configData(geneData);

    }

//    private void nextPage() {
//        if (pairwiseHomologyData == null){
//            return;
//        }
//
//    }
    public void configData(PairwiseHomologyData pairwiseHomologyData) throws IOException {
        this.pairwiseHomologyData = pairwiseHomologyData;
        GeneDataV2 run = pairwiseHomologyData.initialize();

        this.configData(run);
        String blastFmt6ResultPath = pairwiseHomologyData.getBlastFmt6ResultPath();
        if (blastFmt6ResultPath != null && !blastFmt6ResultPath.isEmpty()  ){
            Pair<List<BlastHspRecord>, List<BlastHspRecord>> connectingLines = pairwiseHomologyData.getConnectingLines4blastp();
            this.configData(connectingLines.getLeft(), connectingLines.getRight());
        }
        String diamondFmt6ResultPath = pairwiseHomologyData.getDiamondFmt6ResultPath();
        if (diamondFmt6ResultPath != null && !diamondFmt6ResultPath.isEmpty()  ){
            Pair<List<BlastHspRecord>, List<BlastHspRecord>> connectingLines2 = pairwiseHomologyData.getConnectingLines4diamond();
            this.configData2(connectingLines2.getLeft(), connectingLines2.getRight());
        }


    }

    private void configData(List<BlastHspRecord> l1, List<BlastHspRecord> l2) {
        listOneConnectingLinesFirst2Second = l1;
        listOneConnectingLinesSecond2Third = l2;
    }
    private void configData2(List<BlastHspRecord> left, List<BlastHspRecord> right) {
        listTwoConnectingLinesFirst2Second = left;
        listTwoConnectingLinesSecond2Third = right;
    }

    private void configData(GeneDataV2 geneData) {

        rectangleLists.clear();
        eachGeneDomains.clear();
        colorLists.clear();
        geneNames.clear();
        geneLength.clear();
        geneBackgroundRects.clear();
        listOneConnectingLinesFirst2Second.clear();
        listOneConnectingLinesSecond2Third.clear();

        listTwoConnectingLinesFirst2Second.clear();
        listTwoConnectingLinesSecond2Third.clear();


        Map<String, GeneV2> genes = geneData.getGenes();

        int vertivalIndex = 0;
        for (Entry<String, GeneV2> gene : genes.entrySet()) {
            String geneName = gene.getKey();
            GeneV2 value = gene.getValue();
            List<DomainV2> domains = value.getDomains();

            int size = domains.size();
            Rectangle[] rectangles = new Rectangle[size];
            String[] domainNames = new String[size];
            Color[] colors = new Color[size];

            Rectangle geneBackground = new Rectangle();
            int yy = startYLocation + vertivalIndex * geneVerticalSpaceGap;
            geneBackground.setLocation(startXLocation, yy + geneHeight / 2 - geneBackgroundHeight / 2);
            geneBackground.width = value.getLength();
            geneBackground.height = geneBackgroundHeight;
            for (int i = 0; i < size; i++) {
                DomainV2 domainV2 = domains.get(i);
                int startPos = domainV2.getStart();
                int endPos = domainV2.getEnd();
                Color decode = domainV2.getColor();

                int xx = startXLocation + startPos;

                int width = endPos - startPos;
                int height = geneHeight;

                Rectangle rectangle = new Rectangle(xx, yy, width, height);
                rectangles[i] = rectangle;
                colors[i] = decode;
                domainNames[i] = domainV2.getName();
            }
            rectangleLists.add(rectangles);
            eachGeneDomains.add(domainNames);
            colorLists.add(colors);
            geneNames.add(geneName);
            geneLength.add(value.getLength());
            geneBackgroundRects.add(geneBackground);

            vertivalIndex++;
        }


        setBackground(Color.white);
        // 添加鼠标事件监听器
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录按下时的鼠标位置
                previousMousePoint = e.getPoint();

                // 检查鼠标按下的位置是否在某个矩形内
                int index1 = 0;
                for (Rectangle[] rectangles : rectangleLists) {
                    for (int i = 0; i < rectangles.length; i++) {
                        if (rectangles[i].contains(previousMousePoint)) {
                            selectedRectangleIndex.setLocation(index1, i);
                            break;
                        }
                    }
                    index1++;
                }


                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopupMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // 释放鼠标时取消选中
                selectedRectangleIndex.setLocation(-1, -1);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // 如果有矩形被选中，拖拽时更新其位置
                if (selectedRectangleIndex.x != -1) {
                    Point currentPoint = e.getPoint();
                    int dx = currentPoint.x - previousMousePoint.x;
                    int dy = currentPoint.y - previousMousePoint.y;

                    // 更新被选中矩形的位置
                    Rectangle[] rectangles = rectangleLists.get(selectedRectangleIndex.x);
                    rectangles[selectedRectangleIndex.y].translate(dx, dy);

                    // 更新鼠标位置
                    previousMousePoint = currentPoint;

                    // 重新绘制
                    repaint();
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        // gee the longest rectangle with length
        int maxLength = 0;
        for (Rectangle integer : geneBackgroundRects){
            if (integer.width > maxLength){
                maxLength = integer.width;
            }
        }

        int bb = geneBackgroundRects.size()  *(geneHeight + geneVerticalSpaceGap);
        setPreferredSize(new Dimension(maxLength, startYLocation + bb));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 得到一支画笔
        Graphics2D g2d = (Graphics2D) g;
        // 设置抗锯齿，否则绘制字符串有明显的锯齿效果
        EGPSMainGuiUtil.setupHighQualityRendering(g2d);


        // draw a legend for gene length
        drawGeneLengthAxis(g2d);

        // draw the homologous region
        drawConnectingLines(g2d);

        // 绘制每个矩形
        drawAllGenes(g2d);

        drawConnectingLegend(g2d);

    }

    private void drawAllGenes(Graphics2D g2d) {
        Iterator<Color[]> colorIterator = colorLists.iterator();
        Iterator<Rectangle[]> rectanglesIterator = rectangleLists.iterator();
        Iterator<String[]> domainNamesIterator = eachGeneDomains.iterator();

        Iterator<Integer> geneLengthIterator = geneLength.iterator();
        Iterator<String> geneNamesIterator = geneNames.iterator();
        Iterator<Rectangle> geneBackgroundRectsIterator = geneBackgroundRects.iterator();

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int ascent = fontMetrics.getAscent();
        // 这里是核心代码，一个 while 循环，直接把矩形draw
        while (rectanglesIterator.hasNext()) {
            Rectangle[] rectangles = rectanglesIterator.next();
            Color[] colors = colorIterator.next();
            String geneName = geneNamesIterator.next();
            Integer geneLength = geneLengthIterator.next();
            String[] domainName = domainNamesIterator.next();
            Rectangle geneBackgroundRect = geneBackgroundRectsIterator.next();


            // 直接绘制字符串，就这么简单

            int stringWidth = fontMetrics.stringWidth(geneName);

            int y = geneBackgroundRect.y + (geneBackgroundRect.height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

            g2d.setColor(Color.black);
            g2d.drawString(geneName, startXLocation - stringWidth - 15, y);

            g2d.fill(geneBackgroundRect);

            for (int i = 0; i < rectangles.length; i++) {
                g2d.setColor(colors[i]); // 绘制之前设置颜色
                // 直接draw一个矩形即可
                Rectangle rectangle = rectangles[i];
                g2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                g2d.setColor(Color.black);
                // 想 annotation直接draw就好了，不是很简单吗？ 去乱七八糟的ggplot2 一大堆的什么优雅，我觉得我这个很优雅
                //                                                             我想怎么改就这么改，细节都能调整！！！！
                stringWidth = fontMetrics.stringWidth(domainName[i]);
                g2d.drawString(domainName[i], (float) (rectangle.x + 0.5 * rectangle.width - 0.5 * stringWidth ), rectangle.y + rectangle.height / 2 + ascent / 2);
            }

            // The rectangle may visually overlap, so paint the legend left side
            Set<String> domainNames = new HashSet<>();int yy = geneBackgroundRect.y + geneHeight + geneHeight;
            int xShift = 0;
            for (int i = 0; i < rectangles.length; i++) {
                if (domainNames.add(domainName[i])) {
                    g2d.setColor(colors[i]);

                    g2d.fillRect(10 + xShift, yy + - 20, 30, 20);
                    g2d.setColor(Color.black);
                    g2d.drawString(domainName[i], 10 + xShift, yy - ascent / 2) ;
                    xShift += 20;
                }
            }
//            int yShift = 0;
//            for (int i = 0; i < rectangles.length; i++) {
//                if (domainNames.add(domainName[i])) {
//                    g2d.setColor(colors[i]);
//
//                    yShift += 20;
//                    g2d.fillRect(10, yy + yShift - 20, 50, 20);
//                    g2d.setColor(Color.black);
//                    g2d.drawString(domainName[i], 16, yy + yShift- ascent / 2) ;
//                }
//            }


        }
    }

    private void drawGeneLengthAxis(Graphics2D g2d) {
        g2d.setColor(Color.black);
        int yy = startYLocation - 20;
        int numOfTips = 0;
        int maxGeneLength = geneLength.stream().max(Integer::compareTo).orElse(0);

        while (true) {
            int ll = numOfTips * 100;
            if (ll >= maxGeneLength){
                break;
            }
            int xx = ll + startXLocation;
            g2d.drawString(ll + "", xx - 8, yy + 10);
            g2d.drawLine(xx, yy, xx, yy - 6);
            numOfTips ++;
        }
        g2d.drawLine(startXLocation, yy, startXLocation + numOfTips * 100, yy);
    }

    private void drawConnectingLegend(Graphics2D g2d) {

        int xx = getWidth() - 400;
        int yy = getHeight() - 100;
        Rectangle rectangle1 = new Rectangle(xx, yy + 10, 130, 20);
        Rectangle rectangle2 = new Rectangle(xx, yy + 40, 130, 20);

        g2d.setColor(connectingFillingColor1);

        g2d.fill(rectangle1);
        g2d.setColor(connectingFillingColor2);
        g2d.fill(rectangle2);

        g2d.setColor(Color.black);
        g2d.drawString("Blast homologous region", rectangle1.x + rectangle1.width + 5, rectangle1.y + rectangle1.height / 2);
        g2d.drawString("Diamond homologous region", rectangle2.x + rectangle1.width + 5, rectangle2.y + rectangle2.height / 2);
    }

    private void drawConnectingLines(Graphics2D g2d) {
        g2d.setColor(connectingFillingColor1);
        _drawConnectingLines(g2d, listOneConnectingLinesFirst2Second, listOneConnectingLinesSecond2Third);
        g2d.setColor(connectingFillingColor2);
        _drawConnectingLines(g2d, listTwoConnectingLinesFirst2Second, listTwoConnectingLinesSecond2Third);
    }

    private void _drawConnectingLines(Graphics2D g2d, List<BlastHspRecord> listTwoConnectingLinesFirst2Second, List<BlastHspRecord> listTwoConnectingLinesSecond2Third) {

        for (BlastHspRecord record : listTwoConnectingLinesFirst2Second) {
            Rectangle first = geneBackgroundRects.getFirst();
            Rectangle second = geneBackgroundRects.get(1);
            int qstart = record.getQstart();
            int qend = record.getQend();
            int sstart = record.getSstart();
            int send = record.getSend();
            // 计算梯形四个顶点坐标
            Polygon polygon = getPolygon(first, qstart, qend, second, sstart, send);

            g2d.fill(polygon);

        }
        for (BlastHspRecord record : listTwoConnectingLinesSecond2Third) {
            Rectangle second = geneBackgroundRects.get(1);
            Rectangle third = geneBackgroundRects.get(2);
            int qstart = record.getQstart();
            int qend = record.getQend();
            int sstart = record.getSstart();
            int send = record.getSend();
            Polygon polygon = getPolygon(second, sstart, send, third, qstart, qend);
            g2d.fill(polygon);
        }
    }

    private Polygon getPolygon(Rectangle first, int qstart, int qend, Rectangle second,int sstart , int send ) {
        int halfHeight = geneHeight / 2;
        int x1 = first.x + qstart;
        int y1 = first.y + halfHeight;
        int x2 = first.x + qend;
        int y2 = y1;

        int x3 = second.x + send;
        int y3 = second.y - halfHeight;
        int x4 = second.x + sstart;
        int y4 = y3;

// 创建梯形多边形
        Polygon polygon = new Polygon();
        polygon.addPoint(x1, y1);
        polygon.addPoint(x2, y2);
        polygon.addPoint(x3, y3);
        polygon.addPoint(x4, y4);
        return polygon;
    }

    private void showPopupMenu(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();

        {
            JMenuItem exportItem = new JMenuItem("Export as");
            exportItem.addActionListener(e1 -> new SaveUtil().saveData(PrimaryStructDrawer.this));
            popupMenu.add(exportItem);
        }
        {
            JMenuItem exportItem = new JMenuItem("Next page");
            exportItem.addActionListener(e1 -> {

            });
            popupMenu.add(exportItem);
        }


        popupMenu.show(this, e.getX(), e.getY());
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Draggable Rectangle Drawer");
        PrimaryStructDrawer panel = new PrimaryStructDrawer();

        frame.add(panel);
        frame.setSize(1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}
