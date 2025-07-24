package primary.struct.display.drawer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.google.common.collect.Lists;

import egps2.utils.common.util.SaveUtil;
import egps2.frame.gui.EGPSMainGuiUtil;
import graphic.engine.colors.EGPSColors;
import primary.struct.display.data.Gene;
import primary.struct.display.data.GeneData;

@SuppressWarnings("serial")
public class DraggableRectangleDrawer extends JPanel {
	// 矩形的信息
	private List<Rectangle[]> rectangleLists = Lists.newArrayList();
	private List<String> geneNames = Lists.newArrayList();
	private List<Integer> geneLength = Lists.newArrayList();
	private List<Color[]> colorLists = Lists.newArrayList();

	private Point selectedRectangleIndex = new Point(-1, -1);
	private Point previousMousePoint; // 上一个鼠标位置

	final int yTop = 60;
	final int geneVerticalSpaceGap = 80;

	final int startXLocation = 300;
	int geneHeight = 60;

//    public DraggableRectangleDrawer() {
//
//		Map<String, Gene> genes = new HashMap<>();
//		Gene gene = new Gene();
//		gene.setColor(Arrays.asList("#FFC1CC", "#00A6ED", "#228B22", "#FFFDD0"));
//		gene.setStart(Arrays.asList(1, 237, 238, 275, 276));
//		gene.setEnd(Arrays.asList(237, 238, 275, 276, 352));
//		genes.put("Gene1", gene);
//
//		GeneData geneData = new GeneData();
//		geneData.setGenes(genes);
//		this(geneData);
//	}

	public DraggableRectangleDrawer(GeneData geneData, int geneHeight) {
		this.geneHeight = geneHeight;

		Map<String, Gene> genes = geneData.getGenes();

		int vertivalIndex = 0;
		for (Entry<String, Gene> gene : genes.entrySet()) {
			String geneName = gene.getKey();
			Gene value = gene.getValue();
			List<Integer> start = value.getStart();
			List<Integer> end = value.getEnd();
			List<String> color = value.getColor();

			int size = start.size();
			Rectangle[] rectangles = new Rectangle[size];
			Color[] colors = new Color[size];
			for (int i = 0; i < size; i++) {
				Integer startPos = start.get(i);
				Integer endPos = end.get(i);
				String colrStr = color.get(i);
				Color decode = EGPSColors.parseColor(colrStr);

				int xx = startXLocation + startPos;
				int yy = yTop + vertivalIndex * geneVerticalSpaceGap;
				int width = endPos - startPos;
				int height = geneHeight;

				Rectangle rectangle = new Rectangle(xx, yy, width, height);
				rectangles[i] = rectangle;
				colors[i] = decode;
			}
			rectangleLists.add(rectangles);
			colorLists.add(colors);
			geneNames.add(geneName);
			geneLength.add(value.getLength().get(0));

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
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 得到一支画笔
		Graphics2D g2d = (Graphics2D) g;
		// 设置抗锯齿，否则绘制字符串有明显的锯齿效果
		EGPSMainGuiUtil.setupHighQualityRendering(g2d);
		// 绘制每个矩形
		Iterator<Color[]> colorIterator = colorLists.iterator();
		Iterator<String> geneNamesIterator = geneNames.iterator();
		Iterator<Rectangle[]> rectanglesIterator = rectangleLists.iterator();
		Iterator<Integer> geneLengthIterator = geneLength.iterator();

		FontMetrics fontMetrics = g2d.getFontMetrics();
		int ascent = fontMetrics.getAscent();
		// 这里是核心代码，一个 while 循环，直接把矩形draw
		while (rectanglesIterator.hasNext()) {
			Rectangle[] rectangles = rectanglesIterator.next();
			Color[] colors = colorIterator.next();
			String geneName = geneNamesIterator.next();
			Integer geneLength = geneLengthIterator.next();

			Rectangle firstRect = rectangles[0];


			g2d.setColor(Color.black);
			// 直接绘制字符串，就这么简单

			int stringWidth = fontMetrics.stringWidth(geneName);

			int y = firstRect.y + (firstRect.height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

			g2d.drawString(geneName, startXLocation - stringWidth - 15, y);

			final int heightOfLength = 10;
			Rectangle rectangle = new Rectangle(firstRect);
			rectangle.setLocation(startXLocation, rectangle.y + geneHeight / 2 - heightOfLength / 2);
			rectangle.width = geneLength;
			rectangle.height = heightOfLength;
			g2d.fill(rectangle);

			for (int i = 0; i < rectangles.length; i++) {
				g2d.setColor(colors[i]); // 绘制之前设置颜色
				// 直接draw一个矩形即可
				g2d.fillRect(rectangles[i].x, rectangles[i].y, rectangles[i].width, rectangles[i].height);
			}
		}

	}

	private void showPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem exportItem = new JMenuItem("Export as");

		exportItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SaveUtil().saveData(DraggableRectangleDrawer.this);
			}
		});

		popupMenu.add(exportItem);
		popupMenu.show(this, e.getX(), e.getY());
	}

	public static void main(String[] args) {

		String json1 = "{\"gene1\":{\"length\":[100],\"start\":[1],\"end\":[50],\"color\":[\"#E63946\"]},\"gene3\":{\"length\":[3000],\"start\":[51,101],\"end\":[100,150],\"color\":[\"#2A9D8F\",\"#F4A261\"]}}";
		String json2 = "{\"gene1\":{\"length\":[250],\"start\":[1,10,101,200],\"end\":[8,56,152,230],\"color\":[\"#E63946\",\"#457B9D\",\"#2A9D8F\",\"#F4A261\"]},\"gene2\":{\"length\":[350],\"start\":[1,180,210,261],\"end\":[150,200,250,300],\"color\":[\"#E63946\",\"#457B9D\",\"#2A9D8F\",\"#F4A261\"]},\"gene3\":{\"length\":[300],\"start\":[51,101,200],\"end\":[100,150,231],\"color\":[\"#2A9D8F\",\"#F4A261\",\"#1D3557\"]}}";
		System.out.println(json2);

		GeneData geneData = new GeneData(
				json2);

		JFrame frame = new JFrame("Draggable Rectangle Drawer");
		DraggableRectangleDrawer panel = new DraggableRectangleDrawer(geneData, 30);

		frame.add(panel);
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
