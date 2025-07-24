package module.evolview.phylotree.visualization;

import egps2.utils.common.util.EGPSPrintUtilities;
import evoltree.tanglegram.QuickPairwiseTreeComparator;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;
import evoltree.swingvis.OneNodeDrawer;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class QuickTanglegramCLI {

	static JLabel jLabel = new JLabel();

	static OneNodeDrawer<EvolNode> drawer1 = (g2d, node) -> {
		int xSelf = (int) node.getXSelf();
		int ySelf = (int) node.getYSelf();
		int xParent = (int) node.getXParent();
		if (node.getChildCount() == 0) {
			g2d.drawString(node.getReflectNode().getName(), xSelf + 5, ySelf + 5);
		}

		String lenString = String.valueOf(node.getLength());
		int xx = (xSelf + xParent - g2d.getFontMetrics().stringWidth(lenString)) / 2;
		int yy = ySelf - 5;

		g2d.drawString(lenString, xx, yy);

	};
	static OneNodeDrawer<EvolNode> drawer2 = (g2d, node) -> {
		int xSelf = (int) node.getXSelf();
		int ySelf = (int) node.getYSelf();
		int xParent = (int) node.getXParent();
		if (node.getChildCount() == 0) {
			FontMetrics fontMetrics = g2d.getFontMetrics();
			String name = node.getReflectNode().getName();
			int stringWidth = fontMetrics.stringWidth(name);
			g2d.drawString(name, xSelf - 5 - stringWidth, ySelf + 5);
		}

		String lenString = String.valueOf(node.getLength());
		int xx = (xSelf + xParent - g2d.getFontMetrics().stringWidth(lenString)) / 2;
		int yy = ySelf - 5;

		g2d.drawString(lenString, xx, yy);
	};

	public static void main(String[] args) throws Exception {
		if (args.length == 3) {
			runWithTwoTree(args[0], args[1], args[2]);
		} else if (args.length == 4) {
			runWithTwoTreeAndOutgroup(args[0], args[1], args[2], args[3]);
		} else {
			System.err.println(
					"Usage: java -cp needed.jar egps.remnant.evoltree.graphics.QuickCompareTwoTreeCLI tree1.nwk tree2.nwk 1200,800 [reroot name] ");

			return;
		}

	}

	private static void runWithTwoTree(String string, String string2, String dim) throws Exception {
		TreeDecoder treeDecoder = new TreeDecoder();
		/**
		 * 用括号标记法可以编码一个进化树，标准的nwk(nh)格式并没有定义内节点的名字，我们这里可以定义，并且传递给name属性
		 */
		String line1 = FileUtils.readFileToString(new File(string), StandardCharsets.UTF_8);
		String line2 = FileUtils.readFileToString(new File(string2), StandardCharsets.UTF_8);
		EvolNode rootNode = treeDecoder.decode(line1);
		EvolNode rootNode2 = treeDecoder.decode(line2);

		String[] split = dim.split(",");
		Dimension dim2 = new Dimension(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		JPanel plotTree = QuickPairwiseTreeComparator.plotTree(rootNode, rootNode2, jLabel.getFont(), dim2, drawer1,
				drawer2);
		EGPSPrintUtilities.saveAsPDF(plotTree, dim2, new File("pairwiseTreeComparator.pdf"));
	}

	private static void runWithTwoTreeAndOutgroup(String string, String string2, String dim, String outgroup)
			throws Exception {
		TreeDecoder treeDecoder = new TreeDecoder();
		/**
		 * 用括号标记法可以编码一个进化树，标准的nwk(nh)格式并没有定义内节点的名字，我们这里可以定义，并且传递给name属性
		 */
		String line1 = FileUtils.readFileToString(new File(string));
		String line2 = FileUtils.readFileToString(new File(string2));
		EvolNode rootNode = treeDecoder.decode(line1);
		EvolNode rootNode2 = treeDecoder.decode(line2);

		String[] split = dim.split(",");
		Dimension dim2 = new Dimension(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		JPanel plotTree = QuickPairwiseTreeComparator.plotTree(rootNode, rootNode2, jLabel.getFont(), dim2, drawer1,
				drawer2, outgroup);
		EGPSPrintUtilities.saveAsPDF(plotTree, dim2, new File("pairwiseTreeComparator.pdf"));

	}

}
