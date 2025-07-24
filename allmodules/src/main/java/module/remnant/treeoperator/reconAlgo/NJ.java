package module.remnant.treeoperator.reconAlgo;

import java.util.ArrayList;
import java.util.List;

import module.remnant.treeoperator.DefaultNode;
import module.remnant.treeoperator.NodeEGPSv1;

public class NJ implements TreeReconMethod {
	static boolean debug;

	/**
	 * Distance matrix should be of the format x1 x2 x3 x4 x5 x6
	 *
	 * where x1 is the distance between sequence 1 and 2
	 */
	@Override
	public NodeEGPSv1 tree(double[][] dist) {
		return tree(dist, null);
	}

	/* the algorithm */
	@Override
	public NodeEGPSv1 tree(double[][] dist, String[] OTUnames) {
		final int maxOTU = dist.length + 2; /* maximum number of OTU */
		int nOTU; /* No. of OTU (oeprational taxonomic unit) */
		int[] parent = new int[maxOTU * 2]; /* parent OTU of each node */
		double[] branch = new double[maxOTU * 2]; /* branch length of each node */

		final int n = dist.length + 1;
		List newTree = new ArrayList();

		String otu_node_i, otu_node_j; /* node or OTU designation */
		int[] node_id = new int[maxOTU]; /* node ID starting from nOTU+1 */

		String[][] nameOTU = new String[maxOTU][]; /* name of each OTU */
		int iOTU, jOTU; /* incremental parameters for nOTU */

		double distance; /* distance value to be read */
		double tempmin; /* temporary minimum distance */
		int mini, minj; /* temporary minimum i & j OTU */
		double[][] mat = new double[maxOTU][maxOTU]; /* distance matrix */
		int[] use = new int[maxOTU]; /* OTU usage vector */
		/* 0 = do not use OTU; 1 = use OTU */

		int nc; /* count the main for loop */

		/* specific to NJ method (transformed from PASCAL program) */
		int nnode = 0; /* = nc + nOTU */
		int i, j, jun, lastnode;
		double Sij; /* Sij value */
		double[] R = new double[maxOTU]; /* sum of Dij for all j */
		double[] mean = new double[maxOTU]; /* ? */
		double FOTU, SUMD, SS;
		double dmin, dio, djo, bri, brj, da;
		int[] L = new int[4]; /* OTU ID of the last 3-OTU tree */
		double[] LB = new double[4]; /* branch lengths of the last 3-OTU tree */

		int ncomma; /* number of comma (used for Newark format) */

		/* read and write OTU names and Global initialization */
		nOTU = dist.length + 1;
		// if (debug) System.out.println("Number of OTUs = " + nOTU);

		for (iOTU = 1; iOTU <= nOTU; ++iOTU) {
			mat[iOTU][iOTU] = 0.0; /* initialization of diagonal elements */
			use[iOTU] = 1; /* initialization of use vector */
			node_id[iOTU] = iOTU; /* initialization of node_id */
			mean[iOTU] = 0.0; /* initialization */

		}
		FOTU = nOTU; /* from integer to real */
		for (i = 1; i <= nOTU * 2 - 3; ++i)
			parent[i] = 0; /* initialization; OTU 0 is parent for all nodes */

		/* fu's initilization */
		for (i = 0; i < nOTU; i++) {
			NodeEGPSv1 node = new DefaultNode();
			if (OTUnames == null) {
				node.setName("OTU" + (i + 1));
				node.setLeafName("OTU" + (i + 1));
			} else {
				node.setName(OTUnames[i]);
				node.setLeafName(OTUnames[i]);
			}

			newTree.add(node);
		}
		/* read distance matrix */

		// if (debug) System.out.println("The distance matrix:");
		for (jOTU = 2; jOTU <= nOTU; ++jOTU) {
			for (iOTU = 1; iOTU <= jOTU - 1; ++iOTU) {
				mat[iOTU][jOTU] = dist[jOTU - 2][iOTU - 1];
				mat[jOTU][iOTU] = dist[jOTU - 2][iOTU - 1]; /* symmetrization */
				// if (debug) System.out.print( mat[iOTU][jOTU]+" ");
			}
			// if (debug) System.out.println(" ");
		}

		/* main loop */
		for (nc = 1; nc <= nOTU - 3; ++nc) {
			nnode = nc + nOTU;
			// if (debug) System.out.print( "Node " + nnode + " :" );

			/* Local initialization */
			SUMD = 0.0;
			for (j = 2; j <= nOTU; ++j)
				for (i = 1; i <= j - 1; ++i) {
					mat[j][i] = mat[i][j];
					SUMD = SUMD + mat[i][j];
				}
			tempmin = 99999999.0;
			for (i = 1; i <= nOTU; ++i) {
				R[i] = 0.0;
				for (j = 1; j <= nOTU; ++j)
					R[i] = R[i] + mat[i][j];
			}

			mini = minj = 0; // added by fu
			/* Compute Sij values and find the smallest one */
			for (jOTU = 2; jOTU <= nOTU; ++jOTU) {
				if (use[jOTU] == 1) {
					for (iOTU = 1; iOTU <= jOTU - 1; ++iOTU) {
						if (use[iOTU] == 1) {
							SS = (FOTU - 2.0) * mat[iOTU][jOTU] - R[iOTU] - R[jOTU];
							Sij = (SS * 0.5 + SUMD) / (FOTU - 2.0);
							if (Sij < tempmin) {
								tempmin = Sij;
								mini = iOTU;
								minj = jOTU;
							}
						}
					}
				}
			}

			/* Compute branch lengths and print the results */
			dmin = mat[mini][minj];
			dio = (R[mini] - dmin) / (FOTU - 2.0);
			djo = (R[minj] - dmin) / (FOTU - 2.0);
			bri = (dmin + dio - djo) * 0.5;
			brj = dmin - bri;
			bri = bri - mean[mini];
			brj = brj - mean[minj];

			if (node_id[mini] > nOTU)
				otu_node_i = "node";
			else
				otu_node_i = "OTU ";
			if (node_id[minj] > nOTU)
				otu_node_j = "node";
			else
				otu_node_j = "OTU ";

			// if (debug) System.out.println( otu_node_i+ node_id[mini] +" ("+ bri +") "+
			// otu_node_j+" "+node_id[minj]+" ("+ brj + ")");

			parent[node_id[mini]] = nnode;
			parent[node_id[minj]] = nnode;
			branch[node_id[mini]] = bri;
			branch[node_id[minj]] = brj;

			/* fu's addition */
			NodeEGPSv1 node = new DefaultNode();

			for (int kk = 0; kk < 2; kk++) {
				int ii = node_id[mini] - 1;
				if (kk == 1)
					ii = node_id[minj] - 1;

				NodeEGPSv1 child = (NodeEGPSv1) newTree.get(ii);
				node.addChild(child);
			}
			newTree.add(node);
			node.setName("OTU " + newTree.size());

			/* re-initialization */
			FOTU = FOTU - 1.0;
			mean[mini] = dmin * 0.5;
			use[minj] = 0;
			node_id[mini] = nnode;

			/* compute new distances */
			for (j = 1; j <= nOTU; ++j) {
				if (use[j] == 1) {
					da = (mat[mini][j] + mat[minj][j]) * 0.5;
					if (mini < j)
						mat[mini][j] = da;
					else if (mini > j)
						mat[j][mini] = da;
				}
			}
			for (j = 1; j <= nOTU; ++j) {
				mat[minj][j] = 0.0;
				mat[j][minj] = 0.0;
			}
		} /* end of the main for loop */

		/* The last cycle */
		lastnode = nnode + 1;
		if (debug)
			System.out.print("Node " + lastnode + "(last node): ");
		jun = 1;
		for (i = 1; i <= nOTU; ++i)
			if (use[i] == 1) {
				L[jun] = i;
				++jun;
			}
		LB[1] = (mat[L[1]][L[2]] + mat[L[1]][L[3]] - mat[L[2]][L[3]]) * 0.5;
		LB[2] = mat[L[1]][L[2]] - LB[1];
		LB[3] = mat[L[1]][L[3]] - LB[1];

		NodeEGPSv1 last = new DefaultNode();
		last.setName("OTU " + (newTree.size() + 1));
		for (i = 1; i <= 3; ++i) {
			LB[i] = LB[i] - mean[L[i]];
			if (node_id[L[i]] > nOTU)
				otu_node_i = "node ";
			else
				otu_node_i = "OTU ";

			if (debug)
				System.out.print(otu_node_i + node_id[L[i]] + " (" + LB[i] + " )");
			branch[node_id[L[i]]] = LB[i];

			/*
			 * this part is added by Fu Note that the last node is a trifurcation
			 */

			NodeEGPSv1 child = (NodeEGPSv1) newTree.get(node_id[L[i]] - 1);
			last.addChild(child);
		}
		newTree.add(last);

		/* end of computation */

		/* print branch lengths */
		// if(debug)System.out.println("\n== Node length list (Node 0 is the last node)
		// ==");
		for (i = 1; i <= nOTU * 2 - 3; ++i) {
			// if (debug) System.out.println("branch "+i+" ->"+ parent[i]+" ("+branch[i]+"
			// )");
			NodeEGPSv1 node = (NodeEGPSv1) newTree.get(i - 1);

			branch[i] = (branch[i] < 0 ? 0 : branch[i]); // Added by Haipeng

			node.getBranch().setLength(branch[i]); // fu's addition
		}
		
		//added by yudalang :trifurcation should be a bifurcation
		NodeEGPSv1 firstChild = last.removeChild(0);
		double length = firstChild.getBranch().getLength();
		double halfLen = 0.5 * length;
		firstChild.getBranch().setLength(halfLen);
		last.getBranch().setLength(halfLen);

		NodeEGPSv1 root = new DefaultNode();
		root.addChild(firstChild);
		root.addChild(last);

		return root;
	}

	// The case tested is from https://en.wikipedia.org/wiki/Neighbor_joining

//    public static void main(String[] args) {
//    	int n = 5;
//    	int length = n - 1;
//        double[][] distance = new double[length][];
//        
//        double[] temp0 = {5.0d};
//        double[] temp1 = {9.0d, 10.0d};
//        double[] temp2 = {9.0d, 10.0d, 8.0d};
//        double[] temp3 = {8.0d,  9.0d, 7.0d, 3.0d};
//        distance[0] = temp0;
//        distance[1] = temp1;
//        distance[2] = temp2;
//        distance[3] = temp3;
//        
//        String[] names = {"a", "b", "c", "d", "e"};
//        NJ nj = new NJ();
//        Node root = nj.tree(distance, names);
//        
//        System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//    }

//    public static void main(String[] args) {
//    	int n = 10;
//		int length = n - 1;
//	    double[][] distance = new double[length][];
//	    
//	    double[] temp0 = {0.0516d};
//	    double[] temp1 = {0.0550d, 0.0031d};
//	    double[] temp2 = {0.0483d, 0.0221d, 0.0253d};
//	    double[] temp3 = {0.0582d, 0.0651d, 0.0685d, 0.0549d};
//	    double[] temp4 = {0.0094d, 0.0416d, 0.0450d, 0.0384d, 0.0549d};
//	    double[] temp5 = {0.0125d, 0.0584d, 0.0619d, 0.0551d, 0.0651d, 0.0157d};
//	    double[] temp6 = {0.0284d, 0.0687d, 0.0722d, 0.0654d, 0.0754d, 0.0317d, 0.0285d};
//	    double[] temp7 = {0.0925d, 0.1221d, 0.1259d, 0.1185d, 0.1370d, 0.0820d, 0.0786d, 0.0927d};
//	    double[] temp8 = {0.1921d, 0.2183d, 0.2228d, 0.2054d, 0.2309d, 0.1798d, 0.1795d, 0.1833d, 0.1860d};
//	    distance[0] = temp0;
//	    distance[1] = temp1;
//	    distance[2] = temp2;
//	    distance[3] = temp3;
//	    distance[4] = temp4;
//	    distance[5] = temp5;
//	    distance[6] = temp6;
//	    distance[7] = temp7;
//	    distance[8] = temp8;
//	    
//	    String[] names = {"seq01", "seq02", "seq03", "seq04", "seq05", "seq06", "seq07", "seq08", "seq09", "seq10"};
//        NJ nj = new NJ();
//        Node root = nj.tree(distance, names);
//        
//        TreeUtility util = new TreeUtility();
//        // root = util.rootAtMidPoint(root);
//        
//        root = util.setRootAt(root, "seq10");
//        
//        System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//    }

//    public static void main(String[] args) {
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
//		NJ nj = new NJ();
//		Node root = nj.tree(distance, names);
//
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}

//  public static void main(String[] args) {
//	  int n = 8;
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
//		NJ nj = new NJ();
//		Node root = nj.tree(distance, names);
//		System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//	}
}
