package module.treetanglegram;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.util.EvolTreeOperator;
import evoltree.swingvis.OneNodeDrawer;
import evoltree.tanglegram.QuickPairwiseTreeComparator;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;

public class PaintingPanelPreparer {
	OneNodeDrawer<EvolNode> drawer1 = (g2d, node) -> {
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
	OneNodeDrawer<EvolNode> drawer2 = (g2d, node) -> {
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

	public JPanel loadTab(File nwk1, File nwk2, Font font, Dimension dim, String outgroup) throws Exception {
		TreeDecoder treeDecoder = new TreeDecoder();
		/**
		 * 用括号标记法可以编码一个进化树，标准的nwk(nh)格式并没有定义内节点的名字，我们这里可以定义，并且传递给name属性
		 */
		String line1 = FileUtils.readFileToString(nwk1, StandardCharsets.UTF_8);
		String line2 = FileUtils.readFileToString(nwk2, StandardCharsets.UTF_8);
		EvolNode root1 = treeDecoder.decode(line1);
		EvolNode root2 = treeDecoder.decode(line2);
		
		System.out.println(EvolNodeUtil.getLeaves(root1).size() + "\t" + EvolNodeUtil.getLeaves(root2).size());

		JPanel plotTree = null;
		if (outgroup == null || outgroup.isEmpty()) {
			plotTree = QuickPairwiseTreeComparator.plotTree(root1, root2, font, dim, drawer1, drawer2);
		} else {
			try {
				root1 = EvolTreeOperator.setRootAt(root1, outgroup);
				root2 = EvolTreeOperator.setRootAt(root2, outgroup);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			EvolNodeUtil.ladderizeNode(root1, true);
			EvolNodeUtil.ladderizeNode(root2, true);


			plotTree = QuickPairwiseTreeComparator.plotTree(root1, root2, font, dim, drawer1, drawer2, outgroup);
		}
		return plotTree;
	}
}
