package module.evolview.phylotree.visualization.primary.swing;

import java.awt.Dimension;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import evoltree.txtdisplay.ReflectGraphicNode;

public class FastestLeftRectangularLayoutCalculator {

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
				EvolNodeUtil.recursiveIterateTreeIF(root, node ->{
					node.setLength(1);
				});
				root.setLength(0);
				preIterate2getStatics(root, 0);
			}
		}
		// 不计算根的长度
		currentLeafIndex = 0;
		double calculateWidth = dim.getWidth() - properties.leftHorizontalBlankLength - properties.rightHorizontalBlankLength;
		double calculateHeight = dim.getHeight() - properties.verticalBlankLength - properties.verticalBlankLength;
		
		oneLeafHeightUnitLength = calculateHeight / (leafSize - 1);
		widthRatio = calculateWidth / longestLineageLength;

		iterateTree2assignLocation(root, 0);
		
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

			double yy = 0.0;
			for (int i = 0; i < childCount; i++) {
				ReflectGraphicNode<T> child = EvolNodeUtil.getChildrenAt(node, i);
				iterateTree2assignLocation(child, lineageLength);

				if (i == 0 || i == (childCount - 1)) {
					yy += child.getYSelf();					
				}
			}
			
			yy *= 0.5;

			node.setYSelf(yy);
			node.setYParent(yy);
		} else {
			// This is leaf
			double yy = properties.verticalBlankLength + currentLeafIndex * oneLeafHeightUnitLength;
			
			node.setYSelf(yy);
			node.setYParent(yy);

			currentLeafIndex++;
		}
		
	}
	
	
	public FastGraphicsProperties getProperties() {
		return properties;
	}
	
}
