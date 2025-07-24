package module.evolview.gfamily.work;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;

import evoltree.struct.util.EvolNodeUtil;
import module.evoltre.mutation.IMutation4Rec;
import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;
import module.evolview.gfamily.work.model.tree.CGBID;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.Node4BasicNCov19;

/**
 * 放置一些树操作的快捷方法。
 *
 * 1)注意这个静态类的有些方法不是线程安全的。多个方法会同用一个静态全局变量！这个时候用起来要注意一下！
 * 
 * 2)也要注意如果树的枝长是负的，我们是没有处理过的。如果有要求枝长不能为负数，那么要自己处理一下！
 * 
 * 3)注意有些方法不是针对多叉树的，只是适用于二叉树。所以要看清楚！
 *
 * @createdDate Sep 30, 2020
 * @lastModifiedDate Sep 30, 2020
 * @author ydl,yjn
 */
public class CGBNodeUtil {

	/**
	 * 防止初始化！这里的方法全部是静态方法。不需要初始化！ 模仿的Math类的写法！
	 * 
	 */
	private CGBNodeUtil() {

	}

	/**
	 * 字面意思
	 */
	public static List<EvolNode> getLeavesByRecursive(EvolNode rootNode) {
		List<EvolNode> leaves = new ArrayList<>(8192);
		recursive2getLeafNumber(rootNode, leaves);
		return leaves;
	}

	/**
	 * 字面意思
	 */
	public static List<EvolNode> getLeavesByStackIteration(EvolNode rootNode) {
		List<EvolNode> leaves = new ArrayList<>(8192);

		EvolNodeUtil.iterateByLevelTraverse(rootNode, node -> {
			if (node.getChildCount() == 0) {
				leaves.add(node);
			}
		});
		return leaves;
	}

	private static void recursive2getLeafNumber(EvolNode node, List<EvolNode> leaves) {
		int childCount2 = node.getChildCount();
		if (childCount2 == 0) {
			leaves.add(node);
		} else {
			int childCount = node.getChildCount();
			for (int j = 0; j < childCount; j++) {
				recursive2getLeafNumber(node.getChildAt(j), leaves);
			}
		}

	}

	/**
	 * 
	 * Reroot method write by Gao feng!
	 */
	public static EvolNode rerootGF(EvolNode root, EvolNode reNode) {
		if (reNode.getParent() == root) {
			return null;
		}

		EvolNode newRoot = shadowCloneOneNode(root);
		double dist = 0.5 * reNode.getLength();
		reNode.setLength(dist);

		EvolNode sib = new ArrayBasedNode();
		sib.setLength(dist);

		EvolNode nodPar = reNode.getParent();

		reNode.removeAllParent();
		// add sibling of node
		while (nodPar.getChildCount() > 0) {
			EvolNode tmpChild = nodPar.getChildAt(0);
			tmpChild.removeAllParent();
			sib.addChild(tmpChild);
		}

		EvolNode nextNode = creNode(root, nodPar);
		if (nextNode == null) {
			if (sib.getChildCount() == 1) {
				sib = sib.getChildAt(0);
				sib.setLength(dist);
			}
		} else {
			sib.addChild(nextNode);
		}

		newRoot.addChild(sib);
		newRoot.addChild(reNode);

		return newRoot;
	}

	private static EvolNode creNode(EvolNode root, EvolNode node) {

		if (root == node || node == null) {
			return null;
		}

		EvolNode nodPar = node.getParent();
		node.removeAllParent();
		// add sibling of node
		while (nodPar.getChildCount() > 0) {
			EvolNode tmpChild = nodPar.getChildAt(0);
			tmpChild.removeAllParent();
			node.addChild(tmpChild);
		}

		EvolNode nextNode = creNode(root, nodPar);
		if (nextNode == null) {
			if (node.getChildCount() == 1) {
				double nextBraLen = node.getLength();
				node = node.getChildAt(0);
				node.setLength(node.getLength() + nextBraLen);
			}
		} else {
			node.addChild(nextNode);
		}

		return node;
	}

	/**
	 * shadow copy the input node!
	 * 
	 * @author yudalang
	 */
	public static EvolNode copyTheTree(EvolNode root) {
		EvolNode newRoot = shadowCloneOneNode(root);
		copyTheNode(newRoot, root);
		return newRoot;
	}

	public static EvolNode shadowCloneOneNode(EvolNode node) {
		ArrayBasedNode ret = new ArrayBasedNode();
		ret.setLength(node.getLength());
		ret.setID(node.getID());
		ret.setName(node.getName());
		return ret;
	}

	private static void copyTheNode(EvolNode newNode, EvolNode oldNode) {
		for (int i = 0; i < oldNode.getChildCount(); i++) {
			EvolNode oldChild = oldNode.getChildAt(i);
			EvolNode newChild = shadowCloneOneNode(oldChild);
			newNode.addChild(newChild);

			copyTheNode(newChild, oldChild);
			// System.out.println(newNode + " -> " + newChild);
		}
	}

	/**
	 * Compute the deeps of given tree!<br>
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
	public static int getTreeDepth(EvolNode root) {
		int ret = 0;
		EvolNode tmp = root;

		while (tmp.getChildCount() != 0) {
			ret++;
			tmp = tmp.getFirstChild();
		}
		return ret;
	}

	/**
	 * 不知道这个方法，还有没有用，这个complement指的是，先意会一下吧
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param graphicsNode
	 * @return
	 */
	public static List<EvolNode> getComplementNodes(EvolNode graphicsNode) {
		List<EvolNode> ret = new ArrayList<>();
		iterate2getComplementNodes(ret, graphicsNode);
		return ret;
	}

	private static void iterate2getComplementNodes(List<EvolNode> ret, EvolNode graphicsNode) {
		List<EvolNode> siblings = getSiblings(graphicsNode);
		for (EvolNode tt : siblings) {
			ret.add(tt);
		}

		if (graphicsNode.getParentCount() != 0) {
			iterate2getComplementNodes(ret, graphicsNode.getParent());
		}
	}

	/**
	 * 字面意思,没有考虑多个parent，不过大部分情况都不需要！
	 */
	public static List<EvolNode> getSiblings(EvolNode node) {

		if (node.getParentCount() == 0) {
			return Collections.emptyList();
		}

		List<EvolNode> ret = new ArrayList<>();

		EvolNode parent = node.getParent();
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			EvolNode child = parent.getChildAt(i);
			if (child != node) {
				ret.add(child);
			}
		}
		return ret;
	}

	/**
	 * 得到所有的Children，他会自动正确处理。
	 * 
	 * @title getAllChildren
	 * @createdDate 2020-10-10 17:06
	 * @lastModifiedDate 2020-10-10 17:06
	 * @author "yudalang"
	 * 
	 * @param node
	 * @return void
	 */
	public static EvolNode[] getAllChildren(EvolNode node) {
		int childCount = node.getChildCount();
		EvolNode[] ret = new EvolNode[childCount];
		for (int i = 0; i < childCount; i++) {
			ret[i] = node.getChildAt(i);
		}
		return ret;
	}

//	/**
//	 * <pre>
//	 * 改装修改的李老师的MainAnalysis中的同名方法，使用GraphicsNode，将树的innerVirusRoot传入，将为零的枝合并
//	 * 
//	 * innerVirusRoot：传入树的没有外群的那个根。（虽然可能和初次传入的node一样，但node会迭代改变，所以不能和node并为一个参数）
//	 * node：传入的要处理的树的根。（虽然可能初次传入时也是innerVirusRoot，但内部迭代会改变）
//	 * threshold4ZeroBranch：判断阈值
//	 * isJudgeLength：为true时进行枝长为0的合并；为false时，进行突变数为0的合并
//	 * </pre>
//	 * 
//	 * @title mergeZeroBranchLengthAccordingToTree
//	 * @createdDate 2021-02-03 16:11
//	 * @lastModifiedDate 2021-02-03 16:11
//	 * @author yjn
//	 * @since 1.7
//	 * 
//	 * @param innerVirusRoot
//	 * @param node
//	 * @param threshold4ZeroBranch
//	 * @param isJudgeLength
//	 * @return void
//	 */
//	public static void mergeZeroBranchLengthAccordingToTree(GraphicsNode innerVirusRoot, GraphicsNode node,
//			double threshold4ZeroBranch, boolean isJudgeLength) {
//		GraphicsNode root = (GraphicsNode) innerVirusRoot.getParent();
//		GraphicsNode root_hcov19 = innerVirusRoot;
//
//		int numOfGrandChildren = node.getChildCount();
//
//		List<GraphicsNode> listOfGrandChildren = new ArrayList<>(numOfGrandChildren);
//		for (int i = 0; i < numOfGrandChildren; i++) {
//			listOfGrandChildren.add((GraphicsNode) node.getChildAt(i));
//		}
//
//		double branchLength;
//		if (isJudgeLength) {
//			branchLength = node.getLength();
//		} else {
////			branchLength = node.getMutations().size();
//		}
//
//		if (numOfGrandChildren > 0 && branchLength < threshold4ZeroBranch && node != root_hcov19 && node != root) {
//
//			int numOfZeroChild = 0;
//			int numOfNonZeroChild = 0;
//
//			for (int i = 0; i < numOfGrandChildren; i++) {
//				GraphicsNode grandChild = (GraphicsNode) node.getChildAt(i);
//
//				double grandChildBranchLength;
//				if (isJudgeLength) {
//					grandChildBranchLength = grandChild.getLength();
//				} else {
////					grandChildBranchLength = grandChild.getMutations().size();
//				}
//
//				if (grandChildBranchLength < threshold4ZeroBranch) {
//					numOfZeroChild++;
//				} else {
//					numOfNonZeroChild++;
//				}
//			}
//
//			if (numOfNonZeroChild == 0) {
//				// All grand child is 0 in branch length.
//
//				GraphicsNode parent = (GraphicsNode) node.getParent();
//				int numOfChildrenOfParent = parent.getChildCount();
//
//				int numOfZeroSisterOfNode = 0;
//				for (int i = 0; i < numOfChildrenOfParent; i++) {
//					GraphicsNode sisterOfNode = (GraphicsNode) parent.getChildAt(i);
//					double sisterOfNodeBranchLength;
//					if (isJudgeLength) {
//						sisterOfNodeBranchLength = sisterOfNode.getLength();
//					} else {
////						sisterOfNodeBranchLength = sisterOfNode.getMutations().size();
//					}
//
//					if (sisterOfNodeBranchLength < threshold4ZeroBranch) {
//						numOfZeroSisterOfNode++;
//					}
//				}
//
//				if (numOfZeroSisterOfNode == numOfChildrenOfParent) {
//
//					parent.removeChild(node);
//
//					for (int i = numOfGrandChildren - 1; i >= 0; i--) {
//						GraphicsNode grandChild = (GraphicsNode) node.getChildAt(i);
//
//						node.removeChild(grandChild);
//						parent.addChild(grandChild);
//					}
//				}
//
//			} else {
//				if ((numOfZeroChild == 0) || (numOfZeroChild == 1 && numOfNonZeroChild > 0)) {
//
//					// System.out.println("Removed a zero branch: node.size = " + node.getSize() + "
//					// length = " + branchLength);
//
//					GraphicsNode parent = (GraphicsNode) node.getParent();
//					parent.removeChild(node);
//
//					List<GraphicsNode> listOfGrandChild = new ArrayList<>(numOfGrandChildren);
//					for (int i = numOfGrandChildren - 1; i >= 0; i--) {
//						GraphicsNode grandChild = (GraphicsNode) node.getChildAt(i);
//
//						node.removeChild(grandChild);
//						parent.addChild(grandChild);
//
//						listOfGrandChild.add(grandChild);
//					}
//
//				} else {
//					GraphicsNode parent = (GraphicsNode) node.getParent();
//
//					for (int i = numOfGrandChildren - 1; i >= 0; i--) {
//						GraphicsNode grandChild = (GraphicsNode) node.getChildAt(i);
//						double grandChildBranchLength;
//						if (isJudgeLength) {
//							grandChildBranchLength = grandChild.getLength();
//						} else {
////							grandChildBranchLength = grandChild.getMutations().size();
//						}
//
//						if (grandChildBranchLength >= threshold4ZeroBranch) {
//							// Non-zero branch
//
//							node.removeChild(grandChild);
//							parent.addChild(grandChild);
//						}
//					}
//
//				}
//			}
//
//		}
//
//		for (int i = 0; i < numOfGrandChildren; i++) {
//			mergeZeroBranchLengthAccordingToTree(innerVirusRoot, listOfGrandChildren.get(i), threshold4ZeroBranch,
//					isJudgeLength);
//		}
//
//	}

	/**
	 * <pre>
	 * 改装修改的李老师的MainAnalysis中的同名方法，使用EvolNode，将树的innerVirusRoot传入，将为零的枝合并
	 * 
	 * node：传入的要处理的树的根。
	 * threshold4ZeroBranch：判断阈值
	 * 
	 * </pre>
	 * 
	 * @title mergeZeroBranchLengthAccordingToTree
	 * @createdDate 2021-02-03 16:11
	 * @lastModifiedDate 2021-02-03 16:11
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param innerVirusRoot
	 * @param node
	 * @param threshold4ZeroBranch
	 * @return void
	 */
	public static void mergeZeroBranchLengthAccordingToTree(EvolNode node, double threshold4ZeroBranch) {

		int numOfGrandChildren = node.getChildCount();

		List<EvolNode> listOfGrandChildren = new ArrayList<>(numOfGrandChildren);
		for (int i = 0; i < numOfGrandChildren; i++) {
			listOfGrandChildren.add(node.getChildAt(i));
		}

		double branchLength = node.getLength();

		if (numOfGrandChildren > 0 && branchLength < threshold4ZeroBranch && node.getParentCount() != 0) {

			int numOfZeroChild = 0;
			int numOfNonZeroChild = 0;

			for (int i = 0; i < numOfGrandChildren; i++) {
				EvolNode grandChild = node.getChildAt(i);

				double grandChildBranchLength;

				grandChildBranchLength = grandChild.getLength();

				if (grandChildBranchLength < threshold4ZeroBranch) {
					numOfZeroChild++;
				} else {
					numOfNonZeroChild++;
				}
			}

			if (numOfNonZeroChild == 0) {
				// All grand child is 0 in branch length.

				EvolNode parent = node.getParent();
				int numOfChildrenOfParent = parent.getChildCount();

				int numOfZeroSisterOfNode = 0;
				for (int i = 0; i < numOfChildrenOfParent; i++) {
					EvolNode sisterOfNode = parent.getChildAt(i);
					double sisterOfNodeBranchLength;

					sisterOfNodeBranchLength = sisterOfNode.getLength();

					if (sisterOfNodeBranchLength < threshold4ZeroBranch) {
						numOfZeroSisterOfNode++;
					}
				}

				if (numOfZeroSisterOfNode == numOfChildrenOfParent) {

					parent.removeChild(node);

					for (int i = numOfGrandChildren - 1; i >= 0; i--) {
						EvolNode grandChild = node.getChildAt(i);

						node.removeChild(grandChild);
						parent.addChild(grandChild);
					}
				}

			} else {
				if ((numOfZeroChild == 0) || (numOfZeroChild == 1 && numOfNonZeroChild > 0)) {

					// System.out.println("Removed a zero branch: node.size = " + node.getSize() + "
					// length = " + branchLength);

					EvolNode parent = node.getParent();
					parent.removeChild(node);

					List<EvolNode> listOfGrandChild = new ArrayList<>(numOfGrandChildren);
					for (int i = numOfGrandChildren - 1; i >= 0; i--) {
						EvolNode grandChild = node.getChildAt(i);

						node.removeChild(grandChild);
						parent.addChild(grandChild);

						listOfGrandChild.add(grandChild);
					}

				} else {
					EvolNode parent = node.getParent();

					for (int i = numOfGrandChildren - 1; i >= 0; i--) {
						EvolNode grandChild = node.getChildAt(i);
						double grandChildBranchLength;

						grandChildBranchLength = grandChild.getLength();

						if (grandChildBranchLength >= threshold4ZeroBranch) {
							// Non-zero branch

							node.removeChild(grandChild);
							parent.addChild(grandChild);
						}
					}

				}
			}

		}

		for (int i = 0; i < numOfGrandChildren; i++) {
			mergeZeroBranchLengthAccordingToTree(listOfGrandChildren.get(i), threshold4ZeroBranch);
		}

	}

	public static Optional<GraphicsNode> pickOneNode(GraphicsNode root, String targetCGBID) {

		Optional<Node4BasicNCov19> ret = pickOneNode((Node4BasicNCov19) root, targetCGBID);

		return Optional.ofNullable((GraphicsNode) ret.get());
	}

	public static Optional<Node4BasicNCov19> pickOneNode(Node4BasicNCov19 root, String targetCGBID) {
		Pair<Integer, Integer> splitCGBIDString = splitCGBIDString(targetCGBID);
		final int first = splitCGBIDString.getLeft();
		final int second = splitCGBIDString.getRight();
		MutableObject<Node4BasicNCov19> ret = new MutableObject<>();

		EvolNodeUtil.searchNodeWithReturn(root, node -> {

			Node4BasicNCov19 temp = (Node4BasicNCov19) node;
			if (cgbIDequals(first, second, node)) {

				ret.setValue(temp);
				return true;
			} else {
				return false;
			}
		});

		return Optional.ofNullable(ret.getValue());
	}

	public static Optional<Node4BasicNCov19> pickOneNodeAccording2AccOrCGB(Node4BasicNCov19 root, String targetCGBID) {
		MutableObject<Node4BasicNCov19> ret = new MutableObject<>();
		EvolNodeUtil.searchNodeWithReturn(root, node -> {

			if (targetCGBID.equals(node.getName())) {
				ret.setValue(node);
				return true;
			} else {
				return false;
			}
		});

		if (ret.getValue() != null) {
			return Optional.ofNullable(ret.getValue());
		}

		if (targetCGBID.startsWith("CGB")) {
			Pair<Integer, Integer> splitCGBIDString = splitCGBIDString(targetCGBID);
			final int first = splitCGBIDString.getLeft();
			final int second = splitCGBIDString.getRight();

			EvolNodeUtil.searchNodeWithReturn(root, node -> {

				if (cgbIDequals(first, second, node)) {

					ret.setValue(node);
					return true;
				} else {
					return false;
				}
			});

		}

		return Optional.ofNullable(ret.getValue());
	}

	/**
	 * 
	 * 它会处理好，剩下一个sibling的情况。
	 * 
	 * 注意：这里的inferMutaiton from two state after mutations list
	 * 不是严格相等的，也就是说N->A不算一个突变。
	 * yudalang: 因为程序没有用严格相等的参数导致了一个处理模糊碱基的逻辑错误,现在有点好奇当初不知道为什么没有执行严格的相等
	 * 
	 * @title deleteOneNode
	 * @createdDate 2021-03-05 15:26
	 * @lastModifiedDate 2021-03-05 15:26
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param node2remove
	 * @return void
	 */

	private static List<IMutation4Rec> inferMutationsForBinaryForkRemoveOneNode(Node4BasicNCov19 parent,
			List<IMutation4Rec> mutOfParent, List<IMutation4Rec> mutsOfSister) throws ParseException {

//		List<StateAfterMutation> fromStates = null;
//		WholeGenomeRecover0304 wholeGenomeRecover2 = getWholeGenomeRecover();
//		if (parent == null) {
//			fromStates = wholeGenomeRecover2.getRootStatesAfterMutations();
//		} else {
//			Node4BasicNCov19 grandParent = (Node4BasicNCov19) parent.getParent();
//			if (grandParent == null) {
//				fromStates = wholeGenomeRecover2.getRootStatesAfterMutations();
//			} else {
//				List<IMutation4Rec> allMutationsFromRoot = NodeMutationOperator.getAllMutationsFromRoot(grandParent);
//				fromStates = wholeGenomeRecover2.processAllMutation(allMutationsFromRoot);
//			}
//
//		}
//
//		List<StateAfterMutation> toStates = MutationOperator.deepCloneListOfStateAfterMutation(fromStates);
//		for (IMutation4Rec m : mutOfParent) {
//			wholeGenomeRecover2.processOneMutation(m, toStates);
//		}
//		for (IMutation4Rec m : mutsOfSister) {
//			wholeGenomeRecover2.processOneMutation(m, toStates);
//		}
//
//		
//		return getStatesDissector().inferMutationsOfTwoListsOfStateAfterMutationsStrictly(fromStates, toStates,
//				wholeGenomeRecover2.getRefGenomeSequence());
		return null;
	}

	public static GraphicsNode convertBasicNode2graphicsNode(Node4BasicNCov19 node) {
		Function<Node4BasicNCov19, GraphicsNode> generateNewRoot = root -> {
			GraphicsNode newroot = new GraphicsNode();
			newroot.setID(root.getID());

			newroot.setName(root.getName());

			newroot.setLength(root.getLength());

			newroot.setCgbIDFirst(root.getCgbIDFirst());
			newroot.setCgbIDLast(root.getCgbIDLast());

			newroot.setSize(root.getSize());
			return newroot;
		};
		GraphicsNode convertNode = EvolNodeUtil.convertNode(node, generateNewRoot);

		return convertNode;
	}




	/**
	 * 
	 * @param one : date should latter
	 * @param two : date should earlier
	 * @return
	 */
	public static int diffDays(Date one, Date two) {
		long difference = (one.getTime() - two.getTime()) / 86400000;
		return (int) difference;
	}

//	private static WholeGenomeRecover0304 getWholeGenomeRecover() {
//		if (wholeGenomeRecover == null) {
//			wholeGenomeRecover = new WholeGenomeRecover0304();
//		}
//		return wholeGenomeRecover;
//	}
//	
//	public static StatesDissector getStatesDissector() {
//		if (statesDissector == null) {
//			statesDissector = new StatesDissector();
//		}
//		return statesDissector;
//	}

	/**
	 * 给定两个INDEX，然后查看一个节点的CGBID是否符合。 注意firstID一定要小于 nextID。 如果是比较一个叶子，例如
	 * CGB120303，那么第二个值用 Node4BasicNCov19.DEFAULT_CGBID_INDEX 填充。
	 * 
	 * 有时候你可能需要调用 {@link #splitCGBIDString(String)} 一下来得到它的index。
	 * 
	 * @param firstID
	 * @param nextID
	 * @param node
	 * @return
	 */
	public static boolean cgbIDequals(int firstID, int nextID, Node4BasicNCov19 node) {
		if (node == null) {
			return false;
		}

		if (firstID == node.getCgbIDFirst() && nextID == node.getCgbIDLast()) {
			return true;
		}

		return false;
	}

	public static boolean cgbIDequals(Node4BasicNCov19 node1, Node4BasicNCov19 node2) {
		Objects.requireNonNull(node1);
		Objects.requireNonNull(node2);
		if (node1.getCgbIDFirst() == node2.getCgbIDFirst() && node1.getCgbIDLast() == node2.getCgbIDLast()) {
			return true;
		}

		return false;
	}

	public static Pair<Integer, Integer> splitCGBIDString(String str) {
		int indexOf = str.indexOf('.');
		if (indexOf > -1) {
			return Pair.of(new Integer(str.substring(3, indexOf)), new Integer(str.substring(indexOf + 1)));
		} else {
			return Pair.of(new Integer(str.substring(3)), CGBID.DEFAULT_CGBID_INDEX);
		}
	}

	public static void main(String[] args) {
//		double genomoWideMutationRatePerSitePerYear = 8.2671E-04;
//		double mutationRatePerGenomePerDay = genomoWideMutationRatePerSitePerYear  * (29800 - 100 + 1) / 365;
//		double reciprocalMutationRatePerGenomePerDay = 1 / mutationRatePerGenomePerDay;
//		
//		System.out.println(reciprocalMutationRatePerGenomePerDay);

//		Pair<Integer, Integer> splitCGBIDString = splitCGBIDString("CGB133453.11113");
//		System.out.println(splitCGBIDString.getLeft());
//		System.out.println(splitCGBIDString.getRight());

		pickOneNode(null, "CGB133453");
	}

}
