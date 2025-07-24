package module.heatmap.eheatmap.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;

import java.util.Queue;
import java.util.TreeMap;

public class TreeUtility {

	private double maxDepthInvokeBranchLen;

	/**
	 * Compute the depth - 1 of given tree!<br>
	 * <b>Note:</b> Not the longest deep nor does the shortest deep!
	 * 
	 * <pre>
	 * e.g. in this tree the deeps is 1.
	 *   |----leaf1
	 * --|
	 *   |------leaf2
	 * </pre>
	 *
	 * @author yudalang
	 */
	public int getTreeDeepsWithoutBranchLen(EvolNode root) {
		int ret = 0;
		EvolNode tmp = root;

		while (tmp.getChildCount() != 0) {
			ret++;
			tmp = tmp.getFirstChild();
		}
		return ret;
	}

	/**
	 * Compute the longest depth of given tree and without consider branch
	 * information!<br>
	 * 
	 * <pre>
	 * e.g. in this tree the deeps is 3.
	 *   |----leaf1
	 * --|		
	 *   |      |----leaf3
	 *   |------|
	 *   		|----leaf4
	 * </pre>
	 *
	 * @author yudalang
	 */
	public int getLongestDeepthWithoutBranchLen(EvolNode root) {
		int ret = 1;
		int childCount = root.getChildCount();
		if (childCount != 0) {
			int max = 0;
			for (int i = 0; i < childCount; i++) {
				EvolNode child = root.getChildAt(i);
				int tt = getLongestDeepthWithoutBranchLen(child);
				if (tt > max) {
					max = tt;
				}
			}
			ret += max;
		}
		return ret;
	}

	/**
	 * Recurrently add the branch length to get the maximum horizontal axis length
	 * required!
	 * 
	 * <pre>
	 *  e.g.
	 *     |------ 
	 *  ---|
	 *     |------
	 *  will return 9 "-"
	 * </pre>
	 * 
	 * @author yudalang
	 * @date 2018-12-14
	 */
	public double getLongestDepthInvokeBranch(EvolNode node) {
		maxDepthInvokeBranchLen = 0;
		getMaxXPositionInternal(node, 0);
		return maxDepthInvokeBranchLen;
	}

	private double getMaxXPositionInternal(EvolNode node, double xPosition) {
		int cc = node.getChildCount();
		if (cc == 0) {
			if (xPosition > maxDepthInvokeBranchLen) {
				maxDepthInvokeBranchLen = xPosition;
			}
		} else {
			for (int i = 0; i < cc; i++) {
				EvolNode child = node.getChildAt(i);
				double length = child.getLength();
				// System.out.println("length: " + length);
				// xPosition += length;
				getMaxXPositionInternal(child, xPosition + length);
			}
		}

		return maxDepthInvokeBranchLen;
	}

	public List<EvolNode> levelOrderTraversalOnBinaryTree(EvolNode rootNode) {
		EvolNode tmp;
		Queue<EvolNode> queue = new LinkedList<EvolNode>();

		List<EvolNode> ret = new ArrayList<EvolNode>();

		queue.offer(rootNode);
		while (!queue.isEmpty()) {
			tmp = queue.poll();

			int childCount = tmp.getChildCount();
			if (childCount == 0) {
				ret.add(tmp);
			} else {
				for (int i = 0; i < childCount; i++) {
					queue.offer(tmp.getChildAt(i));
				}
			}

		}
		return ret;
	}

	public void ladderizeUp(EvolNode currNode) {
		// deepth is the distance of passed nodes to root
		TreeMap<Integer, List<EvolNode>> mapOfNodesInSameDeepth = new TreeMap<Integer, List<EvolNode>>(
				new Comparator<Integer>() {
					@Override
					public int compare(Integer obj1, Integer obj2) {
						return obj2.compareTo(obj1);
//						return obj1.compareTo(obj2);
					}
				});

		ladderizeNode(currNode, mapOfNodesInSameDeepth);
	}

	private void ladderizeNode(EvolNode currentNode, Map<Integer, List<EvolNode>> mapOfNodesInSameDeepth) {
		mapOfNodesInSameDeepth.clear();
		int childCount = currentNode.getChildCount();

		// If current node is leaf or all children is leaf,don't need to ladderize!
		if (currentNode.getChildCount() == 0 || isAllNodesAreLeaf(currentNode)) {
			return;
		}
		
		if (currentNode.getSize() < 2) {
			EvolNodeUtil.initializeSize(currentNode);
		}

		EvolNode tempNode = null;
		// if two or more child have same num of leaves, then add them to one List<Node>
		for (int i = 0; i < childCount; i++) {
			tempNode = currentNode.getChildAt(i);
			
			int leafNum = tempNode.getSize();

			if (mapOfNodesInSameDeepth.containsKey(leafNum)) {
				List<EvolNode> cnodes = mapOfNodesInSameDeepth.get(leafNum);
				cnodes.add(tempNode);
				mapOfNodesInSameDeepth.put(leafNum, cnodes);
			} else {
				List<EvolNode> cnodes = new ArrayList<>(3);
				cnodes.add(tempNode);
				mapOfNodesInSameDeepth.put(leafNum, cnodes);
			}
		}

		currentNode.removeAllChild();

		Iterator<Entry<Integer, List<EvolNode>>> mapIterator = mapOfNodesInSameDeepth.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Entry<Integer, List<EvolNode>> cnEntry = mapIterator.next();
			List<EvolNode> childrenOfCurrentNode = cnEntry.getValue();
			for (int m = 0; m < childrenOfCurrentNode.size(); m++) {
				EvolNode cnTempNode = childrenOfCurrentNode.get(m);
				cnTempNode.removeAllParent();
				currentNode.addChild(cnTempNode);
			}
		}
		for (int i = 0; i < childCount; i++) {
			ladderizeNode(currentNode.getChildAt(i), mapOfNodesInSameDeepth);
		}
	}

	private boolean isAllNodesAreLeaf(EvolNode node) {
		boolean allIsLeaf = true;
		for (int i = 0; i < node.getChildCount(); i++) {
			if (node.getChildAt(i).getChildCount() != 0) {
				allIsLeaf = false;
				break;
			}
		}
		return allIsLeaf;
	}

	
	
	
}
