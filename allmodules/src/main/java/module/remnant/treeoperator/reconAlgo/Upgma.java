package module.remnant.treeoperator.reconAlgo;

import java.util.ArrayList;
import java.util.List;

import cern.jet.random.engine.MersenneTwister64;
import egps2.utils.common.math.RandomUtil;
import module.remnant.treeoperator.DefaultNode;
import module.remnant.treeoperator.NodeEGPSv1;
import module.remnant.treeoperator.io.TreeCoder;
import module.remnant.treeoperator.util.GenealogyAnalyzer;

/**
 * https://en.wikipedia.org/wiki/UPGMA
 * Following is a bad example
 * http://www.nmsr.org/upgma.htm
 */
public class Upgma implements TreeReconMethod{
	protected MersenneTwister64 random = RandomUtil.getMersenneTwister64();
	
	protected GenealogyAnalyzer ga = new GenealogyAnalyzer();
	protected List<int[]> listOfJoints = new ArrayList<int[]>(100);

	protected int currentSize = -1;
	protected double[][] currentDistance = null;
	protected double[][] spaceForDistance = null;
	
	@Override
	public NodeEGPSv1 tree( double[][] dist ){
        return tree(dist, null);
    }
	
	// The values in distance won't be changed.
	@Override
	public NodeEGPSv1 tree(double[][] distance, String[] names) {
        final int n = distance.length + 1;
        
        currentSize = n;
        currentDistance  = initialDistanceSpace(n);
        spaceForDistance = initialDistanceSpace(n);
        for (int i = 0; i < currentSize - 1; i ++) {
        	int length = i + 1;
        	double[] sourceRow = distance[i];
        	double[] targetRow = currentDistance[i];
            for (int j = 0; j < length; j ++) {
            	targetRow[j] = sourceRow[j];
            }
        } 	
        
        List currentLeaves = new ArrayList(n);
        
        for(int i = 0; i < n; i ++) {
            DefaultNode node = new DefaultNode();
            node.setSize(1);
            if( names == null ) {
                node.setName("Seq"+(i+1));
                node.setLeafName("Seq"+(i+1));
            }
            else {
                node.setName(names[i]);
                node.setLeafName(names[i]);
            }
            currentLeaves.add(node);
        }
        
        int join[] = new int[2];
        
        for(int nn = n, next = n + 1; nn > 1; nn --, next ++) {
        	DefaultNode nextNode = new DefaultNode();
            nextNode.setName("Seq" + next);
            double minLength = getJoin(nn, currentDistance, join, currentLeaves);
            
            int minJoin = join[0];
            int maxJoin = join[1];
            if (minJoin > maxJoin) {
            	minJoin = join[1];
                maxJoin = join[0];
            }
            
            NodeEGPSv1 a = (NodeEGPSv1)(currentLeaves.get(minJoin));
            NodeEGPSv1 b = (NodeEGPSv1)(currentLeaves.get(maxJoin+1));// ...It must be "get(maxJoin+1)" +1 here
            
            nextNode.addChild(a);
            nextNode.addChild(b);
            nextNode.setSize(a.getSize() + b.getSize());
            
            // Note: It's not necessary to set lengthes here.
             a.getBranch().setLength(minLength / 2 - ga.getTMRCA(a));
             b.getBranch().setLength(minLength / 2 - ga.getTMRCA(b));
             double aSize = a.getSize();
             double bSize = b.getSize();
             double weight = aSize / (aSize + bSize);
             
            recomputeDistance(currentSize, currentDistance, minJoin, maxJoin, weight, listOfJoints);
            
            double[][] temp = currentDistance;
            currentDistance = spaceForDistance;
            spaceForDistance = temp;
            currentSize --;
            
            //printDistanceMatrix(currentDistance, currentSize);

            currentLeaves.set(minJoin, nextNode);
            currentLeaves.remove(maxJoin+1);
        }
        NodeEGPSv1 root = ((NodeEGPSv1)(currentLeaves.get(0)));
        
        return root;
	}
	
	protected double[][] initialDistanceSpace(int n) {
		int length = n - 1;
        double[][] distance = new double[length][];
        for (int j = 0; j < length; j ++) distance[j] = new double[j + 1];
        
        return distance;
    }
	
	// Recompute the distance matrix
    protected void recomputeDistance(int currentSize, double[][] currentDis, final int joinColIndex, final int joinRowIndex, double weight, List<int[]> listOfJoints) {
    	int newMatrixSize = currentSize - 1;
    	boolean hasInreRow = false;
    	boolean belowRemovedNodeRow = false;
    	
    	double xmin = Double.MAX_VALUE;
    	listOfJoints.clear();
    	
    	for (int i = 0; i < newMatrixSize - 1; i ++) {
    		if (i == joinColIndex - 1) {
    			//This flag means find the joint cell row (with the smaller index) in currentDis
    			hasInreRow = true;
    		}
    		else {
    			if (i == joinRowIndex) {
        			//This flag means find the joint cell row (with the larger index) in currentDis
        			belowRemovedNodeRow = true;
        		}
    		}
    		
    		int length = i + 1;
    		double[] targetRow = spaceForDistance[i];
    		boolean hasInreCol = false;
        	boolean rightRemovedNodeCol = false;
        	
    		for (int j = 0; j < length; j ++) {
    			if (j == joinColIndex) {
        			//This flag means find the joint cell column (with the smaller index) in currentDis
    				hasInreCol = true;
        		}
    			else {
    				if (j == joinRowIndex + 1) {
            			//This flag means find the joint cell column (with the larger index) in currentDis
        				rightRemovedNodeCol = true;
            		}
    			}
    			
    			double updatedDist = - Double.MAX_VALUE;
    			if (hasInreRow) {
    				updatedDist = weight * currentDis[joinColIndex - 1][j] + (1 - weight) * currentDis[joinRowIndex][j];
    				targetRow[j] = updatedDist;
    			}
    			else {
    				if (belowRemovedNodeRow) {
    					if (hasInreCol) {
    						updatedDist = weight * currentDis[i + 1][joinColIndex] + (1 - weight) * currentDis[i + 1][joinRowIndex + 1];
    						targetRow[j] = updatedDist;
    					}
    					else {
    						if (! rightRemovedNodeCol) {
    							updatedDist = currentDis[i + 1][j];
    							targetRow[j] = updatedDist;
    						}
    						else {
    							updatedDist = currentDis[i + 1][j + 1];
    							targetRow[j] = updatedDist;
    						}
    					}
    				}
    				else {
    					if (hasInreCol) {
    						updatedDist = weight * currentDis[i][joinColIndex] + (1 - weight) * currentDis[joinRowIndex][i + 1];
    						targetRow[j] = updatedDist;
    					}
    					else {
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
    
    public double getJoin(int size, double[][] dis, int[] join, List currentLeaves) {
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
        	chosenIndex = (int)(random.nextDouble() * numOfEqualPairs);
        }
        int[] chosen = listOfJoints.get(chosenIndex);
        join[0] = chosen[0];
        join[1] = chosen[1];
        
        return xmin;
    }
    
    private void printDistanceMatrix(double[][] matrix, int size) {
    	for (int i = 0; i < size - 1; i ++) {
        	int length = i + 1;
        	double[] targetedRow = matrix[i];
            for (int j = 0; j < length; j ++) {
            	double d = targetedRow[j];
            	System.out.print(d + " ");
            }
            System.out.println();
        }
    	System.out.println();
    }
    
//    public static void main(String[] args) {
////    	int n = 7;
////    	int length = n - 1;
////        double[][] distance = new double[length][];
////        
////        double[] temp0 = {19.0d};
////        double[] temp1 = {27.0d, 31.0d};
////        double[] temp2 = {8.0d, 18.0d, 26.0d};
////        double[] temp3 = {33.0d, 36.0d, 41.0d, 31.0d};
////        double[] temp4 = {18.0d, 1.0d, 32.0d, 17.0d, 35.0d};
////        double[] temp5 = {13.0d, 13.0d, 29.0d, 14.0d, 28.0d, 12.0d};
////        distance[0] = temp0;
////        distance[1] = temp1;
////        distance[2] = temp2;
////        distance[3] = temp3;
////        distance[4] = temp4;
////        distance[5] = temp5;
////        
////        Upgma upgma = new Upgma();
////        Node root = upgma.tree(distance, null).getRoot();
////        System.out.println();
//        
//        int n = 5;
//    	int length = n - 1;
//        double[][] distance = new double[length][];
//        
//        double[] temp0 = {17.0d};
//        double[] temp1 = {21.0d, 30.0d};
//        double[] temp2 = {31.0d, 34.0d, 28.0d};
//        double[] temp3 = {23.0d, 21.0d, 39.0d, 43.0d};
//        distance[0] = temp0;
//        distance[1] = temp1;
//        distance[2] = temp2;
//        distance[3] = temp3;
//        
//        String[] names = {"a", "b", "c", "d", "e"};
//        Upgma upgma = new Upgma();
//        Node root = upgma.tree(distance, names);
//        
//        System.out.println(egps.remnant.phylogenetictree.io.TreeCoder.code(root));
//    }
    
    public static void main(String[] args) {
    	int n = 7;
		int length = n - 1;
		double[][] distance = new double[length][];

		double[] temp0 = { 19 };
		double[] temp1 = { 27, 31 };
		double[] temp2 = { 8, 18, 26 };
		double[] temp3 = { 33,36,41,31 };
		double[] temp4 = { 18,1,32,17,35 };
		double[] temp5 = { 13,13,29,14,28,12 };
		distance[0] = temp0;
		distance[1] = temp1;
		distance[2] = temp2;
		distance[3] = temp3;
		distance[4] = temp4;
		distance[5] = temp5;

		String[] names = { "Turtle ElegantJTable", "Man B", "Tuna C", "Chiken D", "Moth E","Monkey F","Dog G" };
		Upgma upgma = new Upgma();
		NodeEGPSv1 root = upgma.tree(distance, names);

		System.out.println(TreeCoder.code(root));
	}
    
}

