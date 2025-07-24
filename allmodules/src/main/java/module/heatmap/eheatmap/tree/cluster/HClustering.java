package module.heatmap.eheatmap.tree.cluster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import evoltree.struct.EvolNode;
import module.heatmap.eheatmap.tree.GraphcsNode;
import module.heatmap.eheatmap.tree.TreeMethod;

/**
 * This is a bad example, be caution about resource on the Internet!
 * http://www.nmsr.org/upgma.htm
 * 
 * 
 * @author Yudalang
 */
public class HClustering implements TreeMethod {


	/**
	 * The content is: 0 1 3,4 These are the indexes! for two groups to joint!
	 */
	private List<int[]> listOfJoints = new ArrayList<int[]>(100);

	private int currentSize = -1;
	private double[][] currentDistance = null;
	private double[][] spaceForDistance = null;
	private int totalNodes = 0;

	/**
	 * 0 UPGMA
	 * 1 single
	 * 2 complete
	 */
	private int linkageMethodIndex = 0;
	
	public void setLinkageMethodIndex(int linkageMethodIndex) {
		this.linkageMethodIndex = linkageMethodIndex;
	}

	@Override
	public GraphcsNode tree(double[][] distance, String[] names) {
		final int n = distance.length + 1;

		//System.out.println("linkageMethodIndex\t"+linkageMethodIndex);
		currentSize = n;
		currentDistance = initialDistanceSpace(n);
		spaceForDistance = initialDistanceSpace(n);
		for (int i = 0; i < currentSize - 1; i++) {
			int length = i + 1;
			double[] sourceRow = distance[i];
			double[] targetRow = currentDistance[i];
			for (int j = 0; j < length; j++) {
				targetRow[j] = sourceRow[j];
			}
		}
		List<GraphcsNode> currentNodes = new LinkedList<>();
		for (int i = 0; i < n; i++) {
			totalNodes++;
			GraphcsNode node = new GraphcsNode(totalNodes);
			node.setSize(1);
			node.setName(names[i]);
			node.setOrignalOrderInMatrix(i);
			currentNodes.add(node);
		}

		int twoJointIndexes[] = new int[2];

		for (int nn = n, next = n + 1; nn > 1; nn--, next++) {
			totalNodes++;
			GraphcsNode nextNode = new GraphcsNode(totalNodes);
			nextNode.setName("Seq" + next);
			double minLength = getJoin(nn, currentDistance, twoJointIndexes, currentNodes);

			int minJoinIndex = twoJointIndexes[0];
			int maxJoinIndex = twoJointIndexes[1];
			if (minJoinIndex > maxJoinIndex) {
				minJoinIndex = twoJointIndexes[1];
				maxJoinIndex = twoJointIndexes[0];
			}

			GraphcsNode a = currentNodes.get(minJoinIndex);
			/** ...It must be "get(maxJoin+1)" +1 here
			 * Because the input matrix is not includes diagonal elements!
			 */
			GraphcsNode b = currentNodes.get(maxJoinIndex + 1);

			nextNode.addChild(a);
			nextNode.addChild(b);
			nextNode.setSize(a.getSize() + b.getSize());

			// Note: It's not necessary to set lengthes here.
			a.setLength(0.5 * minLength - getTMRCA(a));
			b.setLength(0.5 * minLength - getTMRCA(b));
			double aSize = a.getSize();
			double bSize = b.getSize();
			double weight = aSize / (aSize + bSize);

			recomputeDistance(currentSize, currentDistance, minJoinIndex, maxJoinIndex, weight, listOfJoints);

			double[][] temp = currentDistance;
			currentDistance = spaceForDistance;
			spaceForDistance = temp;
			currentSize--;

			//printDistanceMatrix(currentDistance, currentSize);

			currentNodes.set(minJoinIndex, nextNode);
			currentNodes.remove(maxJoinIndex + 1);
		}
		
		return currentNodes.get(0);
	}

	private void printDistanceMatrix(double[][] matrix, int size) {
		for (int i = 0; i < size - 1; i++) {
			int length = i + 1;
			double[] targetedRow = matrix[i];
			for (int j = 0; j < length; j++) {
				double d = targetedRow[j];
				System.out.print(d + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	// Recompute the distance matrix
	protected void recomputeDistance(int currentSize, double[][] currentDis, final int joinColIndex,
			final int joinRowIndex, double weight, List<int[]> listOfJoints) {
		int newMatrixSize = currentSize - 1;
		boolean hasInreRow = false;
		boolean belowRemovedNodeRow = false;

		double xmin = Double.MAX_VALUE;
		listOfJoints.clear();

		for (int i = 0; i < newMatrixSize - 1; i++) {
			if (i == joinColIndex - 1) {
				// This flag means find the joint cell row (with the smaller index) in
				// currentDis
				hasInreRow = true;
			} else {
				if (i == joinRowIndex) {
					// This flag means find the joint cell row (with the larger index) in currentDis
					belowRemovedNodeRow = true;
				}
			}

			int length = i + 1;
			double[] targetRow = spaceForDistance[i];
			boolean hasInreCol = false;
			boolean rightRemovedNodeCol = false;

			for (int j = 0; j < length; j++) {
				if (j == joinColIndex) {
					// This flag means find the joint cell column (with the smaller index) in
					// currentDis
					hasInreCol = true;
				} else {
					if (j == joinRowIndex + 1) {
						// This flag means find the joint cell column (with the larger index) in
						// currentDis
						rightRemovedNodeCol = true;
					}
				}

				double updatedDist = -Double.MAX_VALUE;
				if (hasInreRow) {
//					updatedDist = weight * currentDis[joinColIndex - 1][j] + (1 - weight) * currentDis[joinRowIndex][j];
					updatedDist = getUpdatedDistance1(weight,currentDis,joinColIndex,j,joinRowIndex);
					targetRow[j] = updatedDist;
				} else {
					if (belowRemovedNodeRow) {
						if (hasInreCol) {
//							updatedDist = weight * currentDis[i + 1][joinColIndex]
//									+ (1 - weight) * currentDis[i + 1][joinRowIndex + 1];
							
							updatedDist = getUpdatedDistance2(weight,currentDis,i,joinColIndex,joinRowIndex);
							targetRow[j] = updatedDist;
						} else {
							if (!rightRemovedNodeCol) {
								updatedDist = currentDis[i + 1][j];
								targetRow[j] = updatedDist;
							} else {
								updatedDist = currentDis[i + 1][j + 1];
								targetRow[j] = updatedDist;
							}
						}
					} else {
						if (hasInreCol) {
//							updatedDist = weight * currentDis[i][joinColIndex]
//									+ (1 - weight) * currentDis[joinRowIndex][i + 1];
							updatedDist = getUpdatedDistance3(weight,currentDis,i,joinColIndex,joinRowIndex);
							targetRow[j] = updatedDist;
						} else {
							updatedDist = currentDis[i][j];
							targetRow[j] = updatedDist;
						}
					}
				}
				hasInreCol = false;

				if (updatedDist < xmin) {
					xmin = updatedDist;
					listOfJoints.clear();
				}
				if (updatedDist == xmin) {
					int[] temp = new int[2];
					temp[0] = j;
					temp[1] = i; // i >= j
					listOfJoints.add(temp);
				}
			}
			hasInreRow = false;
		}
	}

	private double getUpdatedDistance3(double weight, double[][] currentDis, int i, int joinColIndex,
			int joinRowIndex) {
		double ret = 0;
		if (linkageMethodIndex == 0) {
			ret = weight * currentDis[i][joinColIndex]
					+ (1 - weight) * currentDis[joinRowIndex][i + 1];
		}else if (linkageMethodIndex == 1) {
			ret = Math.min(currentDis[i][joinColIndex], currentDis[joinRowIndex][i + 1]);
		}else {
			ret = Math.max(currentDis[i][joinColIndex], currentDis[joinRowIndex][i + 1]);
		}

		return ret;
	}

	private double getUpdatedDistance2(double weight, double[][] currentDis, int i, int joinColIndex,
			int joinRowIndex) {
				
		double ret = 0;
		if (linkageMethodIndex == 0) {
			
			ret = weight * currentDis[i + 1][joinColIndex]
					+ (1 - weight) * currentDis[i + 1][joinRowIndex + 1];
		}else if (linkageMethodIndex == 1) {
			ret = Math.min(currentDis[i + 1][joinColIndex], currentDis[i + 1][joinRowIndex + 1]);
		}else {
			ret = Math.max(currentDis[i + 1][joinColIndex], currentDis[i + 1][joinRowIndex + 1]);
		}

		return ret;
	}

	private double getUpdatedDistance1(double weight, double[][] currentDis, int joinColIndex, int j,
			int joinRowIndex) {

		double ret = 0;
		if (linkageMethodIndex == 0) {
			
			ret = weight * currentDis[joinColIndex - 1][j] + (1 - weight) * currentDis[joinRowIndex][j];
		}else if (linkageMethodIndex == 1) {
			ret = Math.min(currentDis[joinColIndex - 1][j], currentDis[joinRowIndex][j]);
		}else {
			ret = Math.max(currentDis[joinColIndex - 1][j], currentDis[joinRowIndex][j]);
		}

		return ret;
	}


	/**
	 * 
	 * @param size
	 * @param dis
	 * @param join
	 * @param currentNodes
	 * @return
	 */
	private double getJoin(int size, double[][] dis, int[] join, List<GraphcsNode> currentNodes) {
		double xmin = Double.MAX_VALUE;

		if (listOfJoints.isEmpty()) {
			int sizeMinusOne = size - 1;
			for (int i = 0; i < sizeMinusOne; i++) {
				int length = i + 1;
				double[] targetedRow = dis[i];
				for (int j = 0; j < length; j++) {
					double d = targetedRow[j];
					if (d < xmin) {
						xmin = d;
						listOfJoints.clear();
					}
					if (d == xmin) {
						int[] temp = new int[2];
						temp[0] = j;
						temp[1] = i; // i >= j
						listOfJoints.add(temp);
					}
				}
			}
		} else {
			int[] temp = listOfJoints.get(0);
			int j = temp[0];
			int i = temp[1];
			xmin = dis[i][j];
		}

		// choose randomly a pair from the pairs with the smallest distance
		int numOfEqualPairs = listOfJoints.size();
		int chosenIndex = 0;
		if (numOfEqualPairs > 1) {
			// 用JDK最基础的随机数
			chosenIndex = (int) ( Math.random() * numOfEqualPairs);
		}
		int[] chosen = listOfJoints.get(chosenIndex);
		join[0] = chosen[0];
		join[1] = chosen[1];

		return xmin;
	}

	private double[][] initialDistanceSpace(int n) {
		int length = n - 1;
		double[][] distance = new double[length][];
		for (int j = 0; j < length; j++)
			distance[j] = new double[j + 1];

		return distance;
	}

	public double getTMRCA(EvolNode node) {
		double tmrca = 0;
		if (node.getChildCount() != 0) {
			EvolNode child = node.getChildAt(0);
			tmrca = child.getLength() + getTMRCA(child);
		}

		return tmrca;
	}

}
