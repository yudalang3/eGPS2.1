package module.evolview.phylotree.visualization.primary.swing;

import java.awt.Dimension;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import evoltree.txtdisplay.ReflectGraphicNode;

public class FastestLeftRectangularLayoutWithSizeCalculator {

	FastGraphicsProperties properties = new FastGraphicsProperties();

	double longestLineageLength = 0;
	int leafSize = 0;
	double oneLeafHeightUnitLength = 0;
	int currentLeafIndex = 0;
	private double widthRatio;

	public <T extends EvolNode> void calculateTree(ReflectGraphicNode<T> root, Dimension dim) {

		if (longestLineageLength == 0) {
			root.setLength(0);
			preIterate2getStatics(root, 0);

			if (longestLineageLength <= 0) {
				// the preIterate2getStatics method will increase leaf size
				leafSize = 0;
				// 说明每个枝长都是0
				EvolNodeUtil.recursiveIterateTreeIF(root, node -> {
					node.setLength(1);
				});
				root.setLength(0);
				preIterate2getStatics(root, 0);
			}
		}
		// 不计算根的长度
		currentLeafIndex = 0;
		double calculateWidth = dim.getWidth() - properties.leftHorizontalBlankLength
				- properties.rightHorizontalBlankLength;
		double calculateHeight = dim.getHeight() - properties.verticalBlankLength - properties.verticalBlankLength;

		oneLeafHeightUnitLength = calculateHeight / (root.getSize() - 1);
		widthRatio = calculateWidth / longestLineageLength;

		// x坐标还是用原先的代码去绘制
		iterateTree2assignLocation(root, 0);
		// 下面我们重新覆盖一下y坐标

		iterateToCalculateLeafYLocation(root, 0);

		EvolNodeUtil.recursiveIterateTreeLF(root, node -> {
			int childCount = node.getChildCount();
			double sum = 0;
			if (childCount > 0) {
				for (int j = 0; j < childCount; j++) {
					ReflectGraphicNode<T> child = EvolNodeUtil.getChildrenAt(node, j);
					sum += child.getYSelf();
				}

				sum /= childCount;
				node.setYSelf(sum);
				node.setYParent(sum);
			}

		});
	}

	private <T extends EvolNode> void iterateToCalculateLeafYLocation(ReflectGraphicNode<T> node,
																	  int previousYAxisLocation) {
		int childCount = node.getChildCount();

		if (childCount == 0) {
			double yAxisLocation = previousYAxisLocation * oneLeafHeightUnitLength + properties.verticalBlankLength;
			node.setYSelf(yAxisLocation);
			node.setYParent(yAxisLocation);
			
			node.setDoubleVariable(previousYAxisLocation);
			return;
		}

		for (int i = 0; i < childCount; i++) {

			ReflectGraphicNode<T> child = EvolNodeUtil.getChildrenAt(node, i);
			int childSize = child.getSize();

			iterateToCalculateLeafYLocation(child, previousYAxisLocation);
			previousYAxisLocation += childSize;
		}

	}

	@SuppressWarnings("unchecked")
	private <T extends EvolNode> ReflectGraphicNode<T> getButtomMostLeafNode(ReflectGraphicNode<T> node) {
		ReflectGraphicNode<T> ret = node;

		while (ret.getChildCount() > 0) {
			ret = (ReflectGraphicNode<T>) ret.getLastChild();
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <T extends EvolNode> ReflectGraphicNode<T> getTopMostLeafNode(ReflectGraphicNode<T> node) {
		ReflectGraphicNode<T> ret = node;

		while (ret.getChildCount() > 0) {
			ret = (ReflectGraphicNode<T>) ret.getFirstChild();
		}
		return ret;
	}

	private void preIterate2getStatics(EvolNode node, double cumulatedLength) {
		double length = node.getLength();
		int childCount = node.getChildCount();

		double lengthPlusThisNode = cumulatedLength + length;
		if (childCount == 0) {
			leafSize++;
			if (lengthPlusThisNode > longestLineageLength) {
				longestLineageLength = lengthPlusThisNode;
			}
		} else {
			for (int i = 0; i < childCount; i++) {
				EvolNode child = node.getChildAt(i);
				preIterate2getStatics(child, lengthPlusThisNode);
			}
		}

	}

	@SuppressWarnings("unchecked")
	protected <T extends EvolNode> void iterateTree2assignLocation(ReflectGraphicNode<T> node, double cumulatedLength) {
		double lineageLength = cumulatedLength + node.getLength();

		double xx = widthRatio * lineageLength + properties.leftHorizontalBlankLength;

		node.setXSelf(xx);

		ReflectGraphicNode<T> parent = (ReflectGraphicNode<T>) node.getParent();
		if (parent == null) {
			node.setXParent(xx - 10);
		} else {
			node.setXParent(parent.getXSelf());
		}

		node.setDoubleVariable(lineageLength);
		int childCount = node.getChildCount();

		if (childCount > 0) {

			for (int i = 0; i < childCount; i++) {
				ReflectGraphicNode<T> child = EvolNodeUtil.getChildrenAt(node, i);
				iterateTree2assignLocation(child, lineageLength);

			}
		}

		// System.out.println(node.getName() + "\t" + node.getXSelf() + "\t" +
		// node.getYSelf());
	}

	public FastGraphicsProperties getProperties() {
		return properties;
	}

}
