package module.remnant.treeoperator.reconAlgo;

import java.util.ArrayList;
import java.util.List;

import module.remnant.treeoperator.DefaultNode;
import module.remnant.treeoperator.NodeEGPSv1;
import module.remnant.treeoperator.TreeUtility;

/**
 * Ref: Naruya Saitou, 2018, Introduction to Evolutionary Genomics (Second
 * Edition). Springer International Publishing
 * 
 * (Published on Oct 10, 2018)
 * 
 * https://www.springer.com/cn/book/9783319926414#aboutAuthors
 * https://link.springer.com/content/pdf/10.1007%2F978-1-4471-5304-7.pdf
 * 
 * @author haipeng
 */
public class SwiftNJ implements TreeReconMethod {

	public SwiftNJ() {
		
		
	}

	@Override
	public NodeEGPSv1 tree(double[][] dist) {
		return tree(dist, null);
	}

	@Override
	public NodeEGPSv1 tree(double[][] dist, String[] OTUnames) {

		final int n = dist.length + 1; // the number of OTUs

		List<NodeEGPSv1> listOfLeafs = new ArrayList<NodeEGPSv1>(n);

		for (int i = 0; i < n; i++) {
			NodeEGPSv1 node = new DefaultNode();
			if (OTUnames == null) {
				node.setName("OTU" + (i + 1));
				node.setLeafName("OTU" + (i + 1));
			} else {
				node.setName(OTUnames[i]);
				node.setLeafName(OTUnames[i]);
			}

			node.setSize(1);
			listOfLeafs.add(node);
		}

		// If nonSuppressedOTUs is true, the OTU will be searched.
		boolean[] nonSuppressedOTUs = new boolean[n];
		for (int i = 0; i < n; i++) {
			nonSuppressedOTUs[i] = true;
		}

		boolean[][] nonSuppressedCells = new boolean[dist.length][];
		for (int i = 0; i < dist.length; i++) {
			nonSuppressedCells[i] = new boolean[i + 1];
			for (int j = 0; j <= i; j++) {
				nonSuppressedCells[i][j] = true;
			}
		}

		// To remove zero-distances
		tree_Step01(dist, OTUnames, nonSuppressedOTUs, listOfLeafs);

		NodeEGPSv1 root = null;
		while (true) {

			int numOfUnsuppressedOTUs = getNumOfUnsuppressedOTUs(nonSuppressedOTUs);
			if (numOfUnsuppressedOTUs == 4) {
				root = tree_Step06(n, dist, nonSuppressedOTUs, nonSuppressedCells, listOfLeafs);

				break;
			}
			else if (numOfUnsuppressedOTUs == 3) {
				
				root = tree_For_Size03(n, dist, nonSuppressedOTUs, nonSuppressedCells, listOfLeafs);

				break;
			}
			else if (numOfUnsuppressedOTUs == 2) {
				
				root = tree_For_Size02(n, dist, nonSuppressedOTUs, nonSuppressedCells, listOfLeafs);

				break;
			}
			else if (numOfUnsuppressedOTUs == 1) {
				
				NodeEGPSv1 nodeI = listOfLeafs.get(0);

				while (nodeI.getParent() != null) {
					nodeI = nodeI.getParent();
				}
				
				root = nodeI;

				break;
			}
			
			// the OTU pair, x and y01, with the minimum distance D[x, y01].
			int[] x_y01 = tree_Step02(dist, nonSuppressedOTUs, nonSuppressedCells);
			int x = x_y01[0];
			int y01 = x_y01[1];
			
			if (x == 6 && y01 == 7) {
				System.out.print("");
			}

			// two distances, D[x,y02] and D[x,y03] which are the second and the third minimum
			int[] y02_y03 = tree_Step03(n, x, y01, dist, nonSuppressedOTUs, nonSuppressedCells);
			int y02 = y02_y03[0];
			int y03 = y02_y03[1];

			// System.out.println("Global minimum = " + getDist(x, y01, dist) + " " + x + "
			// " + y01 + " : two more " + y02 + " " + y03);

			if (tree_Step04(x, y01, y02, y03, dist)) {

				double[] branchLengths = tree_Step05a(x, y01, y02, y03, dist, nonSuppressedOTUs, nonSuppressedCells);
				joint(x, branchLengths[0], y01, branchLengths[1], listOfLeafs);

			} else {
				tree_Step05b(x, y01, y02, y03, dist, nonSuppressedOTUs, nonSuppressedCells);
			}
		}

		return root;
	}

	/**
	 * Search all N(N-1)/2 pairwise distances, and if zero distances are found for
	 * sequences i and j, these are joined as neighbors, and OTU with larger or
	 * later number of suffix (j) is suppressed from later searches. This
	 * zero-distance checking should be considered as the initial screening process,
	 * and it can be applied to any distance matrix method. Although the total
	 * number of OTUs compared in the later section may be smaller than N, we will
	 * keep to use the same symbol N in later parts, because of suppression
	 * operation mentioned above.
	 * 
	 * @param dist
	 * @param OTUnames
	 * @param suppressedCells
	 */
	private void tree_Step01(double[][] dist, String[] OTUnames, boolean[] nonSuppressedOTUs, List<NodeEGPSv1> listOfLeafs) {

		int numOfRows = dist.length;
		for (int i = 0; i < numOfRows; i++) {
			
			int leafIndexI = i + 1;
			int numOfCols = leafIndexI;
			
			for (int j = 0; j < numOfCols; j++) {
				
				double distI_J = getDist(leafIndexI, j, dist);
				if (nonSuppressedOTUs[leafIndexI] && distI_J == 0) {

					joint(leafIndexI, 0, j, 0, listOfLeafs);

					nonSuppressedOTUs[leafIndexI] = false;

					break;
				}
			}
		}
	}

	/**
	 * Search all N(N-1)/2 pairwise nonzero distances, and find the OTU pair, x and
	 * y01, with the minimum distance D[x, y01]. This OTU pair is the neighbor
	 * candidate.
	 * 
	 * @param dist
	 * @param OTUnames
	 * @param suppressedCells
	 * @param listOfLeafs
	 */
	private int[] tree_Step02(double[][] dist, boolean[] nonSuppressedOTUs, boolean[][] nonSuppressedCells) {

		int[] x_y01 = new int[2]; // neighbor candidate

		double minNonzeroDistance = Double.MAX_VALUE;

		int numOfRows = dist.length;
		for (int i = 0; i < numOfRows; i++) {
			
			int leafIndexI = i + 1;
			int  numOfCols = leafIndexI;

			if (nonSuppressedOTUs[leafIndexI]) {
				for (int j = 0; j < numOfCols; j ++) {

					double distI_J = getDist(leafIndexI, j, dist);
					
					if (nonSuppressedOTUs[j] && getNonSupressedCellStatus(leafIndexI, j, nonSuppressedCells) && distI_J > 0) {
						if (distI_J < minNonzeroDistance) {
							minNonzeroDistance = distI_J;
							x_y01[0] = j;
							x_y01[1] = leafIndexI;
						}
					}
				}
			}

		}

		return x_y01;
	}

	/**
	 * Search N-2 distances of D[x,i]'s (i != y01), and find two distances, D[x,y02]
	 * and D[x,y03] which are the second and the third minimum distances among the
	 * N-2 D[x,i]'s. The first minimum distance was already found as D[x,y01]. We do
	 * not care about tie situations. All three distances may be identical.
	 * 
	 * According to Cycle 4B, suppressed cells will not be considered here. (Page 425)
	 * 
	 * According to Cycle 5, the global minimum is D(7, 1). However, this minimum
	 * has been searched in Cycle 3a but do not find the neighbor. Thus, to search
	 * the second and the third minimums should be conducted with y01.
	 * 
	 * Special note: In Cycle 4a, the global minimum is D(1, 8), and two more OTUs is 7 & 4.
	 * The pair (1, 7) has been suppressed because (1, 7) are not neighbors. Based on this example
	 * and the logic of the method, the search for the second and the third minimums does not consider suppressed cells.
	 * Therefore, in this case we have D(1, 7) < D(1, 8) < D(1, 4). That is
	 * The second minimum < the global minimum < the third minimum.
	 * 
	 * @param x
	 * @param y01
	 * @param dist
	 * @param OTUnames
	 * @param nonSuppressedCells
	 * @param listOfLeafs
	 * @return y02 and y03
	 */
	private int[] tree_Step03(int numOTUs, int x, int y01, double[][] dist, boolean[] nonSuppressedOTUs, boolean[][] nonSuppressedCells) {

		double minNonzeroDistance02 = Double.MAX_VALUE;
		int y02 = -1;
		
		int numOfCols = x;
		if (nonSuppressedOTUs[x]) {
			for (int j = 0; j < numOfCols; j++) {

				if (j != y01) {
					double currentDist = getDist(x, j, dist);
					if (nonSuppressedOTUs[j] && currentDist > 0) {
						if (currentDist < minNonzeroDistance02) {
							minNonzeroDistance02 = currentDist;
							y02 = j;
						}
					}
				}
				
			}
		}
		
		int numOfRows = dist.length;
		/*
		// original version
		for (int i = x; i < numOTUs - 1; i ++) {

			if (nonSuppressedOTUs[i + 1] && i + 1 != y01) {

					double currentDist = getDist(i + 1, x, dist);
					if (nonSuppressedOTUs[x] && currentDist > 0) {
						if (currentDist < minNonzeroDistance02) {
							minNonzeroDistance02 = currentDist;
							y02 = i + 1;
						}
					}
			}

		}
		*/
		
		// Fast version
		for (int i = x + 1; i < numOTUs; i ++) {

			if (nonSuppressedOTUs[i] && i != y01) {

					double currentDist = getDist(i, x, dist);
					if (nonSuppressedOTUs[x] && currentDist > 0) {
						if (currentDist < minNonzeroDistance02) {
							minNonzeroDistance02 = currentDist;
							y02 = i;
						}
					}
			}

		}

		double minNonzeroDistance03 = Double.MAX_VALUE;
		int y03 = -1;

		if (nonSuppressedOTUs[x]) {
			for (int j = 0; j < numOfCols; j++) {

				if (j != y01 && j != y02) {
					double currentDist = getDist(x, j, dist);
					if (nonSuppressedOTUs[j] && currentDist > 0) {
						if (currentDist < minNonzeroDistance03) {
							minNonzeroDistance03 = currentDist;
							y03 = j;
						}
					}
				}
				
			}
		}

		/*
		// Original version
		for (int i = x; i < numOTUs - 1; i ++) {

			if (nonSuppressedOTUs[i + 1] && i + 1 != y01 && i + 1 != y02) {

					double currentDist = getDist(i + 1, x, dist);
					if (nonSuppressedOTUs[x] && currentDist > 0) {
						if (currentDist < minNonzeroDistance03) {
							minNonzeroDistance03 = currentDist;
							y03 = i + 1;
						}
					}
			}

		}
		*/
		
		// fast version
		for (int i = x + 1; i < numOTUs; i ++) {

			if (nonSuppressedOTUs[i] && i != y01 && i != y02) {

					double currentDist = getDist(i, x, dist);
					if (nonSuppressedOTUs[x] && currentDist > 0) {
						if (currentDist < minNonzeroDistance03) {
							minNonzeroDistance03 = currentDist;
							y03 = i;
						}
					}
			}

		}

		int[] y02_y03 = new int[2];
		y02_y03[0] = y02;
		y02_y03[1] = y03;

		return y02_y03;

	}

	private double getDist(int x, int y, double[][] dist) {
		
		if (x > y) {
			return dist[x - 1][y];
		} else if (x < y) {
			return dist[y - 1][x];
		} else { // x == y
			return 0;
		}
	}
	
	private boolean getNonSupressedCellStatus(int x, int y, boolean[][] nonSuppressedCells) {

		if (x > y) {
			return nonSuppressedCells[x - 1][y];
		} else {
			return nonSuppressedCells[y - 1][x];
		}
	}
	
	private void setNonSupressedCellStatus(int x, int y, boolean[][] nonSuppressedCells, boolean nonSuppressed) {
		
		if (x > y) {
			nonSuppressedCells[x - 1][y] = nonSuppressed;
		} else {
			nonSuppressedCells[y - 1][x] = nonSuppressed;
		}
	}

	/**
	 * Compare four OTUs, x, y01, y02, and y03 using the four-point metric to examine
	 * if the neighbor candidate OTU pair x and y1 are really neighbors among the 
	 * four OTUs:
	 * 
	 * @param x
	 * @param y01
	 * @param y02
	 * @param y03
	 * @param dist
	 * @return
	 */
	private boolean tree_Step04(int x, int y01, int y02, int y03, double[][] dist) {

		double D_x_y01 = getDist(x, y01, dist);
		double D_x_y02 = getDist(x, y02, dist);
		double D_x_y03 = getDist(x, y03, dist);

		double D_y01_y02 = getDist(y01, y02, dist);
		double D_y01_y03 = getDist(y01, y03, dist);
		double D_y02_y03 = getDist(y02, y03, dist);

		double value01 = D_x_y01 + D_y02_y03;

		///////////////////////////////////////////////////////////////////////////////////////////
		// These two inequalities are not correct in the poster of Prof. Saitou, due to typerror.
		// There is the same mistake in Equation17.37, Page421.
		// They are corrected according to Figure 1, and examples in Page424-425
		///////////////////////////////////////////////////////////////////////////////////////////
		
		// return ((D_x_y01 + D_y02_y03 <= D_x_y02 + D_y01_y03) && (D_x_y01 + D_y02_y03 <= D_x_y03 + D_y01_y02));
		
		return ((value01 <= D_x_y02 + D_y01_y03) && (value01 <= D_x_y03 + D_y01_y02));

	}

	/**
	 * If the above inequalities are both satisfied, we determine that OTUs x and
	 * y01 are neighbors. Estimate external branch lengths (BL) between OTUs x and
	 * y01 and the adjacent node A as: BL[x, A] = D[x,y01]/2 + {D[x,y02] + D[x,y03]
	 * - D[y01,y02] - D[y01,y03]}/4; BL[y01,A] = D[x,y01] - BL[x,A]
	 * 
	 * If OTUsxory1 are involved in previously suppressed OTUs because of
	 * nonneighbors, release it from suppression. Suppress the OTU whose
	 * external branch length was longer among the two. If the number of
	 * unsuppressed OTUs becomes 4, go to step (6), otherwise go to step (2).
	 * 
	 * @param x
	 * @param y01
	 * @param y02
	 * @param y03
	 * @param dist
	 * @param nonSuppressedOTUs
	 * @return
	 */
	private double[] tree_Step05a(int x, int y01, int y02, int y03, double[][] dist, boolean[] nonSuppressedOTUs, boolean[][] nonSuppressedCells) {

		double D_x_y01 = getDist(x, y01, dist);
		double D_x_y02 = getDist(x, y02, dist);
		double D_x_y03 = getDist(x, y03, dist);
		double D_y01_y02 = getDist(y01, y02, dist);
		double D_y01_y03 = getDist(y01, y03, dist);

		double BL_x_anc = D_x_y01 * 0.5 + (D_x_y02 + D_x_y03 - D_y01_y02 - D_y01_y03) * 0.25;
		BL_x_anc = (BL_x_anc < 0 ? 0 : BL_x_anc);

		///////////////////////////////////////////////////////////////////////////////////////////
		// BL[y1,A] is not correct in the poster of Prof. Saitou, due to typerror.
		// They are corrected according to Figure 1.
		// In the poster, it is BL[x,A] = ...... = D[x,y1] - BL[x ,A]
		//         It should be BL[x,A] = ...... = D[x,y1] - BL[y1,A]
		// 
		// Or see Eq17.38b in Page422. This equation is correct.
		///////////////////////////////////////////////////////////////////////////////////////////
		double BL_y01_anc = D_x_y01 - BL_x_anc;
		BL_y01_anc = (BL_y01_anc < 0 ? 0 : BL_y01_anc);

		final int n_minusOne = dist.length; // the number of OTUs - 1
		
		for (int yy = 0; yy < n_minusOne; yy ++) {
			
			if (yy != x && yy != y01) { // i != j
				if (! getNonSupressedCellStatus(x, yy, nonSuppressedCells)) {
					
					setNonSupressedCellStatus(x, yy, nonSuppressedCells, true);
				}
			}
		}
		
		for (int xx = 0; xx < n_minusOne; xx ++) {
			if (xx != x && xx != y01) { // i != j
				if (! getNonSupressedCellStatus(xx, y01, nonSuppressedCells)) {
					
					setNonSupressedCellStatus(xx, y01, nonSuppressedCells, true);
				}
			}
		}

		if (BL_x_anc > BL_y01_anc) {
			nonSuppressedOTUs[x] = false;
		} else {
			nonSuppressedOTUs[y01] = false;
		}

		double[] branchLengths = new double[2];
		branchLengths[0] = BL_x_anc;
		branchLengths[1] = BL_y01_anc;

		return branchLengths;
	}

	/**
	 * If at least one of the above inequalities are not satisfied, we suppress the
	 * use of OTU pair x and y01 in later searches. If the number of unsuppressed
	 * OTUs becomes 4, go to Step6. Otherwise go to Step2.
	 * 
	 * @param x
	 * @param y01
	 * @param y02
	 * @param y03
	 * @param dist
	 * @param nonSuppressedOTUs
	 */
	private void tree_Step05b(int x, int y01, int y02, int y03, double[][] dist, boolean[] nonSuppressedOTUs, boolean[][] nonSuppressedCells) {

		if (x > y01) {
			nonSuppressedCells[x - 1][y01] = false;
		} else {
			nonSuppressedCells[y01 - 1][x] = false;
		}

	}

	/**
	 * Apply the four-point metric to the remaining unsuppressed four OTUs, and
	 * determine two neighbors simulataneously. Estimate four external branches. The
	 * internal branch between nodes A and B are computed as follows: BL[A,B] =
	 * {D[x,y02] + D[y01,y03] + D[x, y03] + D[y01,y02]}/4 - {D[x,y01] +
	 * D[y02,y03]}/2
	 * 
	 * @param dist
	 * @param nonSuppressedOTUs
	 */
	private NodeEGPSv1 tree_Step06(int numOTUs, double[][] dist, boolean[] nonSuppressedOTUs, boolean[][] nonSuppressedCells, List<NodeEGPSv1> listOfLeafs) {

		// the OTU pair, x and y01, with the minimum distance D[x, y01].
		int[] x_y01 = tree_Step02(dist, nonSuppressedOTUs, nonSuppressedCells);
		int x = x_y01[0];
		int y01 = x_y01[1];

		// two distances, D[x,y02] and D[x,y03] which are the second and the third minimum
		int[] y02_y03 = tree_Step03(numOTUs, x, y01, dist, nonSuppressedOTUs, nonSuppressedCells);
		int y02 = y02_y03[0];
		int y03 = y02_y03[1];

		// System.out.println("Global minimum = " + getDist(x, y01, dist) + " " + x + "
		// " + y01 + " : two more " + y02 + " " + y03);

		double[] branchLengths01 = tree_Step05a(x, y01, y02, y03, dist, nonSuppressedOTUs, nonSuppressedCells);
		NodeEGPSv1 node01 = joint(x, branchLengths01[0], y01, branchLengths01[1], listOfLeafs);

		double[] branchLengths02 = tree_Step05a(y02, y03, x, y01, dist, nonSuppressedOTUs, nonSuppressedCells);
		NodeEGPSv1 node02 = joint(y02, branchLengths02[0], y03, branchLengths02[1], listOfLeafs);

		double D_x_y01 = getDist(x, y01, dist);
		double D_x_y02 = getDist(x, y02, dist);
		double D_x_y03 = getDist(x, y03, dist);
		double D_y01_y02 = getDist(y01, y02, dist);
		double D_y01_y03 = getDist(y01, y03, dist);
		double D_y02_y03 = getDist(y02, y03, dist);

		// {Dx_y2 + Dy1_y3 + Dx_y3 + Dy1_y2}/4 - {Dx_y1 + Dy2_y3}/2
		double lengthOfAB = (D_x_y02 + D_y01_y03 + D_x_y03 + D_y01_y02) * 0.25 - (D_x_y01 + D_y02_y03) * 0.5;

		node01.getBranch().setLength(lengthOfAB * 0.5);
		node02.getBranch().setLength(lengthOfAB * 0.5);

		NodeEGPSv1 root = new DefaultNode();
		root.addChild(node01);
		root.addChild(node02);
		root.setSize(node01.getSize() + node02.getSize());

		return root;
	}
	
	/**
	 * For the number of unsuppressed OTUs is 3.
	 * 
	 * @param numOTUs
	 * @param dist
	 * @param nonSuppressedOTUs
	 * @param nonSuppressedCells
	 * @param listOfLeafs
	 * @return
	 */
	private NodeEGPSv1 tree_For_Size03(int numOTUs, double[][] dist, boolean[] nonSuppressedOTUs, boolean[][] nonSuppressedCells, List<NodeEGPSv1> listOfLeafs) {

		// the OTU pair, x and y01, with the minimum distance D[x, y01].
		int[] x_y01 = tree_Step02(dist, nonSuppressedOTUs, nonSuppressedCells);
		int x = x_y01[0];
		int y01 = x_y01[1];
		
		double minNonzeroDistance02 = Double.MAX_VALUE;
		int y02 = -1;
		
		int numOfCols = x;
		if (nonSuppressedOTUs[x]) {
			for (int j = 0; j < numOfCols; j++) {

				if (j != y01) {
					double currentDist = getDist(x, j, dist);
					if (nonSuppressedOTUs[j] && currentDist > 0) {
						if (currentDist < minNonzeroDistance02) {
							minNonzeroDistance02 = currentDist;
							y02 = j;
						}
					}
				}
				
			}
		}
		
		int numOfRows = dist.length;
		
		// Fast version
		for (int i = x + 1; i < numOTUs; i ++) {

			if (nonSuppressedOTUs[i] && i != y01) {

					double currentDist = getDist(i, x, dist);
					if (nonSuppressedOTUs[x] && currentDist > 0) {
						if (currentDist < minNonzeroDistance02) {
							minNonzeroDistance02 = currentDist;
							y02 = i;
						}
					}
			}

		}

		double[] branchLengths01 = tree_Step05a(x, y01, y02, y02, dist, nonSuppressedOTUs, nonSuppressedCells);
		NodeEGPSv1 node01 = joint(x, branchLengths01[0], y01, branchLengths01[1], listOfLeafs);

		double D_x_y01 = getDist(x, y01, dist);
		double D_x_y02 = getDist(x, y02, dist);
		double D_y01_y02 = getDist(y01, y02, dist);
		
		double lengthOf_A_Y2 = (D_x_y02 + D_y01_y02 - D_x_y01) * 0.5;
		lengthOf_A_Y2 = (lengthOf_A_Y2 < 0? 0 : lengthOf_A_Y2);
		
		double lengthOf_x_Root   = branchLengths01[0] + lengthOf_A_Y2 * 0.5;
		double lengthOf_y02_Root =                      lengthOf_A_Y2 * 0.5;
		
		NodeEGPSv1 root = joint(x, lengthOf_x_Root, y02, lengthOf_y02_Root, listOfLeafs);

		return root;
	}
	
	/**
	 * For the number of unsuppressed OTUs is 2.
	 * 
	 * @param numOTUs
	 * @param dist
	 * @param nonSuppressedOTUs
	 * @param nonSuppressedCells
	 * @param listOfLeafs
	 * @return
	 */
	private NodeEGPSv1 tree_For_Size02(int numOTUs, double[][] dist, boolean[] nonSuppressedOTUs, boolean[][] nonSuppressedCells, List<NodeEGPSv1> listOfLeafs) {

		// the OTU pair, x and y01, with the minimum distance D[x, y01].
		int[] x_y01 = tree_Step02(dist, nonSuppressedOTUs, nonSuppressedCells);
		int x = x_y01[0];
		int y01 = x_y01[1];

		double D_x_y01 = getDist(x, y01, dist);
		
		double lengthOfLeaf = D_x_y01 * 0.5;
		
		NodeEGPSv1 root = joint(x, lengthOfLeaf, y01, lengthOfLeaf, listOfLeafs);

		return root;
	}

	/**
	 * Note: There are only leafs in the listOfLeafs. The ancestral nodes do not appear in the list. They can be traced through the leafs.
	 *       Accordingly, the branch length will be adjusted. 
	 * 
	 * @param leafIndexI
	 * @param branchLengthI
	 * @param leafIndexJ
	 * @param branchLengthJ
	 * @param listOfLeafs
	 * @return
	 */
	private NodeEGPSv1 joint(int leafIndexI, double branchLengthI, int leafIndexJ, double branchLengthJ, List<NodeEGPSv1> listOfLeafs) {

		NodeEGPSv1 nodeI = listOfLeafs.get(leafIndexI);
		NodeEGPSv1 nodeJ = listOfLeafs.get(leafIndexJ);

		while (nodeI.getParent() != null) {
			// To adjust the branch length
			branchLengthI -= nodeI.getBranch().getLength();
			nodeI = nodeI.getParent();
		}

		while (nodeJ.getParent() != null) {
			// To adjust the branch length
			branchLengthJ -= nodeJ.getBranch().getLength();
			nodeJ = nodeJ.getParent();
		}

		branchLengthI = (branchLengthI < 0 ? 0 : branchLengthI);
		branchLengthJ = (branchLengthJ < 0 ? 0 : branchLengthJ);

		nodeI.getBranch().setLength(branchLengthI);
		nodeJ.getBranch().setLength(branchLengthJ);

		NodeEGPSv1 parent = new DefaultNode();
		parent.addChild(nodeI);
		parent.addChild(nodeJ);
		parent.setSize(nodeI.getSize() + nodeJ.getSize());

		return parent;
	}

	private int getNumOfUnsuppressedOTUs(boolean[] nonSuppressedOTUs) {

		int length = nonSuppressedOTUs.length;

		int count = 0;
		for (int i = 0; i < length; i++) {
			if (nonSuppressedOTUs[i]) {
				count++;
			}
		}

		return count;
	}
	
	//////////////////////////////////////////////////////////////////
	// Codes below are written for other people, which will not be used in eGPS.
	// So it should be commented in eGPS, but please do not delete them.
	// -- Haipeng Li
	//////////////////////////////////////////////////////////////////
//	protected void tree(String fileName) {
//		try {
//			FileReader fileReader = new FileReader(fileName);
//	        LineNumberReader in = new LineNumberReader(fileReader);
//	        List<String> lineList = new ArrayList<String>(1000);
//
//	        String line;
//	        while ((line = in.readLine()) != null) {
//	            line = line.trim();
//	            if (line.length() > 0) {
//	                lineList.add(line);
//	            }
//	        }
//	        fileReader.close();
//	        
//	        int sampleSize = lineList.size();
//	        String[] OTUnames = new String[ sampleSize ];
//	        for (int i = 0; i < sampleSize; i ++) {
//	        	line = lineList.get(i);
//	        	StringTokenizer tokenizer = new StringTokenizer(line);
//	        	OTUnames[i] = tokenizer.nextToken();
//	        }
//	        
//	        double[][] distMatrix = new double[ sampleSize - 1 ][];
//	        for (int lineIndex = 1; lineIndex < sampleSize; lineIndex ++) {
//	        	
//	        	double[] array = new double[lineIndex];
//	        	line = lineList.get(lineIndex);
//	        	StringTokenizer tokenizer = new StringTokenizer(line);
//	        	tokenizer.nextToken(); // seqName
//	        	
//	        	for (int i = 0; i < lineIndex; i ++) {
//	        		array[i] = Double.parseDouble( tokenizer.nextToken() );
//	        	}
//	        	
//	        	distMatrix[ lineIndex - 1 ] = array;
//	        }
//	        
//	        Node root = tree(distMatrix, OTUnames);
//	        
//	        System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void main(String[] args) {
//		if (args.length != 0) {
//			System.out.println("How to use it:");
//			System.out.println("java SwiftNJ inputFileName");
//			System.out.println();
//		}
//		else {
//			// String fileName = args[0];
//			String fileName = "D:\\JavaDev\\SwiftNJ\\distMatrix.txt";
//			
//			SwiftNJ nj = new SwiftNJ();
//			nj.tree(fileName);
//		}
//	}
	

	 public static void main(String[] args) {
	 int n = 10;
	 int length = n - 1;
	 double[][] distance = new double[length][];
	
	 double[] temp0 = {0.0516d};
	 double[] temp1 = {0.0550d, 0.0031d};
	 double[] temp2 = {0.0483d, 0.0221d, 0.0253d};
	 double[] temp3 = {0.0582d, 0.0651d, 0.0685d, 0.0549d};
	 double[] temp4 = {0.0094d, 0.0416d, 0.0450d, 0.0384d, 0.0549d};
	 double[] temp5 = {0.0125d, 0.0584d, 0.0619d, 0.0551d, 0.0651d, 0.0157d};
	 double[] temp6 = {0.0284d, 0.0687d, 0.0722d, 0.0654d, 0.0754d, 0.0317d, 0.0285d};
	 double[] temp7 = {0.0925d, 0.1221d, 0.1259d, 0.1185d, 0.1370d, 0.0820d, 0.0786d, 0.0927d};
	 double[] temp8 = {0.1921d, 0.2183d, 0.2228d, 0.2054d, 0.2309d, 0.1798d, 0.1795d, 0.1833d, 0.1860d};
	 distance[0] = temp0;
	 distance[1] = temp1;
	 distance[2] = temp2;
	 distance[3] = temp3;
	 distance[4] = temp4;
	 distance[5] = temp5;
	 distance[6] = temp6;
	 distance[7] = temp7;
	 distance[8] = temp8;
	
	 String[] names = {"seq01", "seq02", "seq03", "seq04", "seq05", "seq06",
	 "seq07", "seq08", "seq09", "seq10"};
	 SwiftNJ nj = new SwiftNJ();
	 NodeEGPSv1 root = nj.tree(distance, names);
	
	 TreeUtility util = new TreeUtility();
	 root = util.rootAtMidPoint(root);
	
	 // root = util.setRootAt(root, "seq10");
	
	 }

//	public static void main(String[] args) {
//		int n = 11;
//		int length = n - 1;
//		double[][] distance = new double[length][];
//
//		double[] temp1  = { 0.00000d };
//		double[] temp2  = { 0.00299d, 0.00299d };
//		double[] temp3  = { 0.02395d, 0.02395d, 0.02096d };
//		double[] temp4  = { 0.02395d, 0.02395d, 0.02096d, 0.00000d };
//		double[] temp5  = { 0.04491d, 0.04491d, 0.04192d, 0.03892d, 0.03892d };
//		double[] temp6  = { 0.11677d, 0.11677d, 0.11377d, 0.11078d, 0.11078d, 0.07784d };
//		double[] temp7  = { 0.05988d, 0.05988d, 0.05689d, 0.05389d, 0.05389d, 0.01497d, 0.07485d };
//		double[] temp8  = { 0.05988d, 0.05988d, 0.05689d, 0.05389d, 0.05389d, 0.01497d, 0.07485d, 0.00000d };
//		double[] temp9  = { 0.06587d, 0.06587d, 0.06287d, 0.05988d, 0.05988d, 0.03293d, 0.08982d, 0.02994d, 0.02994d };
//		double[] temp10 = { 0.18563d, 0.18563d, 0.18263d, 0.17365d, 0.17365d, 0.15269d, 0.15863d, 0.15269d, 0.15269d, 0.15868d };
//		
//		distance[0 ] = temp1;
//		distance[1 ] = temp2;
//		distance[2 ] = temp3;
//		distance[3 ] = temp4;
//		distance[4 ] = temp5;
//		distance[5 ] = temp6;
//		distance[6 ] = temp7;
//		distance[7 ] = temp8;
//		distance[8 ] = temp9;
//		distance[9 ] = temp10;
//
//		String[] names = { "seq01", "seq02", "seq03", "seq04", "seq05", "seq06", "seq07", "seq08", "seq09", "seq10", "seq11" };
//		SwiftNJ nj = new SwiftNJ();
//		Node root = nj.tree(distance, names);
//
//		System.out.println("SwiftNJ");
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}
	
//	public static void main(String[] args) {
//		int n = 8;
//		int length = n - 1;
//		double[][] distance = new double[length][];
//
//		double[] temp1 = {1.1639227053989822};
//		double[] temp2 = {1.2450749734986863, 1.2043487537135298};
//		double[] temp3 = {1.14084361917565, 1.250296750156839, 1.3904203140078775};
//		double[] temp4 = {1.0217273959336455, 1.4145031102324044, 1.4650506155883536, 0.7976295040200498};
//		double[] temp5 = {1.1225336130640227, 1.030116700601212, 0.9142262284257272, 1.1793816524372298, 1.39255299900209};
//		double[] temp6 = {1.2462684921299854, 0.6883609452908238, 0.9509558346197838, 1.1782677015451197, 1.2074531793321126, 0.7863096387776374};
//		double[] temp7 = {0.6370331765361604, 1.0175456603655237, 1.676838634880002, 1.0903896177898802, 1.0085478993545594, 1.2952962544491755, 1.1944023318295123};
//		
//		distance[0 ] = temp1;
//		distance[1 ] = temp2;
//		distance[2 ] = temp3;
//		distance[3 ] = temp4;
//		distance[4 ] = temp5;
//		distance[5 ] = temp6;
//		distance[6 ] = temp7;
//
//		String[] names = { "seq01", "seq02", "seq03", "seq04", "seq05", "seq06", "seq07", "seq08" };
//		SwiftNJ nj = new SwiftNJ();
//		Node root = nj.tree(distance, names);
//
//		System.out.println("SwiftNJ");
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}
	
	
//	public static void main(String[] args) {
//		int n = 4;
//		int length = n - 1;
//		double[][] distance = new double[length][];
//
//		double[] temp1 = {1.1639227053989822};
//		double[] temp2 = {1.2450749734986863, 1.2043487537135298};
//		double[] temp3 = {1.14084361917565, 1.250296750156839, 1.3904203140078775};
//		
//		distance[0 ] = temp1;
//		distance[1 ] = temp2;
//		distance[2 ] = temp3;
//
//		String[] names = { "seq01", "seq02", "seq03", "seq04" };
//		SwiftNJ nj = new SwiftNJ();
//		Node root = nj.tree(distance, names);
//
//		System.out.println("SwiftNJ");
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}
	
//	public static void main(String[] args) {
//		int n = 3;
//		int length = n - 1;
//		double[][] distance = new double[length][];
//
//		double[] temp1 = {1.1639227053989822};
//		double[] temp2 = {1.2450749734986863, 1.2043487537135298};
//		
//		distance[0 ] = temp1;
//		distance[1 ] = temp2;
//
//		String[] names = { "seq01", "seq02", "seq03" };
//		SwiftNJ nj = new SwiftNJ();
//		Node root = nj.tree(distance, names);
//
//		System.out.println("SwiftNJ");
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}
	
//	public static void main(String[] args) {
//		int n = 4;
//		int length = n - 1;
//		double[][] distance = new double[length][];
//
//		double[] temp1 = {0};
//		double[] temp2 = {0, 0};
//		double[] temp3 = {0, 0, 0};
//		
//		distance[0 ] = temp1;
//		distance[1 ] = temp2;
//		distance[2 ] = temp3;
//
//		String[] names = { "seq01", "seq02", "seq03", "seq04" };
//		SwiftNJ nj = new SwiftNJ();
//		Node root = nj.tree(distance, names);
//
//		System.out.println("SwiftNJ");
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}
	
//	public static void main(String[] args) {
//		int n = 4;
//		int length = n - 1;
//		double[][] distance = new double[length][];
//
//		double[] temp1 = {0};
//		double[] temp2 = {1, 1};
//		double[] temp3 = {1, 1, 0};
//		
//		distance[0 ] = temp1;
//		distance[1 ] = temp2;
//		distance[2 ] = temp3;
//
//		String[] names = { "seq01", "seq02", "seq03", "seq04" };
//		SwiftNJ nj = new SwiftNJ();
//		Node root = nj.tree(distance, names);
//
//		System.out.println("SwiftNJ");
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}
}
