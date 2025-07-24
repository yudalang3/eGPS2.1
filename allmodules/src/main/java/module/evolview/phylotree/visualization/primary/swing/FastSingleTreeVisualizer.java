package module.evolview.phylotree.visualization.primary.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import evoltree.swingvis.OneNodeDrawer;
import evoltree.txtdisplay.ReflectGraphicNode;
import utils.EGPSFileUtil;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;
import utils.EGPSGuiUtil;

/**
 * 快速可视化 实现了EvolNode的进化树，调用API的时候只要将 根节点传入即可。
 *
 * @author yudalang
 *
 */
public class FastSingleTreeVisualizer {
	/**
	 * 是否显示JFrame窗口，默认为true。
	 */
	public boolean showJFrame = true;
	/**
	 * 窗口是否始终置顶，默认为true。
	 */
	public boolean alwaysOnTop = true;

	/**
	 * 用于显示树状图的JFrame对象。
	 */
	public JFrame frame;

	public FastSingleTreeVisualizer() {

	}


	/**
	 * 绘制并展示进化树。
	 *
	 * @param <T>                  泛型类型，需继承自EvolNode。
	 * @param root                 树的根节点。
	 * @param title                窗口标题。
	 * @param dim                  窗口尺寸。
	 * @param drawer               节点绘制器，在绘制每个节点时使用。
	 * @param beforeDrawingProcedure 绘制前的操作，可为空。
	 * @return                     返回绘制面板对象。
	 */
	public <T extends EvolNode> SingleTreePaintingPanel<T> plotTree(T root, String title, Dimension dim,
			OneNodeDrawer<T> drawer,
			Consumer<Graphics2D> beforeDrawingProcedure) {
		ReflectGraphicNode<T> reflectGraphicNode = new ReflectGraphicNode<>(root);

		SingleTreePaintingPanel<T> paintingPanel = new SingleTreePaintingPanel<>(reflectGraphicNode, drawer);
		paintingPanel.setBackground(Color.white);
		paintingPanel.setPreferredSize(dim);

		paintingPanel.setBeforeDrawingProcedure(beforeDrawingProcedure);

		SwingUtilities.invokeLater(() -> {
			frame = new JFrame(title);
			paintingPanel.setJFrame( frame);
			JScrollPane jScrollPane = new JScrollPane(paintingPanel);
			frame.add(jScrollPane);

			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(showJFrame);

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setAlwaysOnTop(alwaysOnTop);

		});
		
		return paintingPanel;
	}

	/**
	 * 绘制并展示进化树，不指定节点绘制器和绘制前操作。
	 *
	 * @param <T>                  泛型类型，需继承自EvolNode。
	 * @param root                 树的根节点。
	 * @param title                窗口标题。
	 * @param dim                  窗口尺寸。
	 * @return                     返回绘制面板对象。
	 */
	public <T extends EvolNode> SingleTreePaintingPanel<T> plotTree(T root, String title, Dimension dim) {
		return plotTree(root, title, dim, null);
	}

	/**
	 * 绘制并展示进化树，不指定绘制前操作。
	 *
	 * @param <T>                  泛型类型，需继承自EvolNode。
	 * @param root                 树的根节点。
	 * @param title                窗口标题。
	 * @param dim                  窗口尺寸。
	 * @param drawer               节点绘制器，在绘制每个节点时使用。
	 * @return                     返回绘制面板对象。
	 */
	public <T extends EvolNode> SingleTreePaintingPanel<T> plotTree(T root, String title, Dimension dim,
			OneNodeDrawer<T> drawer) {
		return plotTree(root, title, dim, drawer, null);
	}

	public static void main(String[] args) throws Exception {
		InputStream inputStream = FastSingleTreeVisualizer.class.getResourceAsStream("test.tree.txt");
		List<String> content = EGPSFileUtil.getContentFromInputStreamAsLines(inputStream);
		String line = content.get(3);

		File input = new File("/Users/yudalang/hugeDataRepo/sarsCov2fastaData/API_files/22_7July.out/a1/testBWT_algo/2_7.jpeg");

		TreeDecoder treeDecoder = new TreeDecoder();
		/**
		 * 用括号标记法可以编码一个进化树，标准的nwk(nh)格式并没有定义内节点的名字，我们这里可以定义，并且传递给name属性
		 */
		EvolNode rootNode = treeDecoder.decode(line);
		
//		BufferedImage picture = ImageIO.read(input);

		// 第一次调用这个API
//		JPanel plotTree = plotTree(rootNode, "Test", new Dimension(600, 600), (g2d, node) -> {
//			int xSelf = (int) node.getXSelf();
//			int ySelf = (int) node.getYSelf();
//			
//			 g2d.drawString(node.getReflectNode().getName(), xSelf , ySelf);
//		});

		SingleTreePaintingPanel<EvolNode> plotTree = new FastSingleTreeVisualizer().plotTree(rootNode, "Test",
				new Dimension(600, 600), (g2d, node) -> {
			int xSelf = (int) node.getXSelf();
			int ySelf = (int) node.getYSelf();
			// 第一次绘制
//			 g2d.drawString(node.getReflectNode().getName(), xSelf , ySelf);
			// 这个绘制你会觉得name的位置需要往右边移动
//			g2d.drawString(node.getReflectNode().getName(), xSelf + 5, ySelf);
			// 你又觉得可以往下移动一下
			if (node.getChildCount() == 0) {
				g2d.drawString(node.getReflectNode().getName(), xSelf + 5, ySelf + 5);
				
			}
			// 你觉得b这个分支需要高亮，这里简单处理，让它覆盖一次
			if ("Taxon ElegantJTable".equals(node.getName())) {
				Color oriColor = g2d.getColor();
				g2d.setColor(Color.green);
				g2d.drawString(node.getReflectNode().getName(), xSelf + 5, ySelf + 5);
				g2d.setColor(oriColor);
			}

			// 你想把线也高亮
			if ("Taxon B".equals(node.getName())) {
				Color oriColor = g2d.getColor();
				g2d.setColor(Color.green);
				int xParent = (int) node.getXParent();
				int yParent = (int) node.getYParent();
				g2d.drawLine(xSelf, ySelf + 3, xParent, yParent + 3);
				g2d.setColor(oriColor);
			}
			// 你想放一张图片

			if ("Taxon G".equals(node.getName())) {
				
//				g2d.drawImage(picture, xSelf + 60, ySelf - 26,null);
				//你发现图片默认大小不合适，要不改图片，要不拉伸
				//g2d.drawImage(picture, xSelf, ySelf, 50,50, null);
			}

		});

		// 上面就是最基本的绘图，类似于 R中原始base/Graphics包下面的plot函数
		// 导出矢量图只要吧Jpanel作为参数传到第三方库就好了

		// 什么你想要像ggplot2那样不要这么底层的方式来操作绘图？
		// 给予一定的指令来绘图？例如 转到ppt

		EGPSGuiUtil.universalSimplestIDE(() -> {

			SwingUtilities.invokeLater(() -> {
				plotTree.repaint();
			});
		});
	}

}
