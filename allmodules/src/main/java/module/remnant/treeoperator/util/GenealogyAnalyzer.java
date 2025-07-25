/*
 * GenealogyAnalyzer.java
 *
 * Created on February 19, 2004, 1:47 PM
 */

package module.remnant.treeoperator.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import module.remnant.treeoperator.NodeEGPSv1;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * @author  haipeng
 * @author  yudalang
 * @date 2024-04-30 正好20年，这次是重新修正
 * 
 */
public class GenealogyAnalyzer implements java.io.Serializable, Cloneable {
	private static final long serialVersionUID = -5443890227583589322L;
	public static int INITIAL_MAX_SAMPLE_SIZE = 1000;
    public static final int INITIAL_SIZE = 0;
    public static final int X_SIZE = -2;
    
    protected NodeEGPSv1 root;
    protected List<NodeEGPSv1> branchs;
    protected List<NodeEGPSv1> leafs;
    
    protected boolean branchLengthsUpdated = false;
    protected double[] branchLengths = new double[INITIAL_MAX_SAMPLE_SIZE];
    
    protected Distribution dis;
    protected double[][] alleleFreq;
    

    /** Creates a new instance of GenealogyAnalyzer */
    public GenealogyAnalyzer() {
        branchs = new ArrayList<>(INITIAL_MAX_SAMPLE_SIZE * 2);
        leafs = new ArrayList<>(INITIAL_MAX_SAMPLE_SIZE);
        
        dis = new Distribution();
    }
    
    public GenealogyAnalyzer(NodeEGPSv1 root) {
        this();
        setGenealogy(root);
    }
    
    protected double an(int n) {
        double result = 0;
        for (int i = 1; i < n; i ++) result += (double) 1.0 / (double)i;
        return result;
    }
    
    protected void clear() {
        branchs.clear();
        leafs.clear();
    }
    
    protected void clearBranchLengths() {
    	int length = branchLengths.length;
        for (int i = 0; i < length; i ++) branchLengths[i] = 0;
        branchLengthsUpdated = false;
    }
    
    public NodeEGPSv1 copyTheTree() {
        NodeEGPSv1 newRoot = root.getClone();
        copyTheNode(newRoot, root);
        
        return newRoot;
    }
    
    protected void copyTheNode(NodeEGPSv1 newNode, NodeEGPSv1 oldNode) {
        for (int i = 0; i < oldNode.getChildCount(); i ++) {
            NodeEGPSv1 oldChild = oldNode.getChildAt(i);
            NodeEGPSv1 newChild = oldChild.getClone();
            newNode.addChild(newChild);
            
            copyTheNode(newChild, oldChild);
            
            // System.out.println(newNode + " -> " + newChild);
        }
    }
    
    /**
     * The list will be changed during the next call!
     *
     * If size == -1, all of nodes will be given.
     */
    public List getBranchsWithSize(int size) {
        branchs.clear();
        getBranchsWithSize(getRoot(), branchs, size);
        return branchs;
    }
    
    /**
     * If size == -1, all nodes will be added into branchs.
     */
    protected void getBranchsWithSize(NodeEGPSv1 node, List<NodeEGPSv1> branchs, int size) {
    	int nodeSize = node.getSize();
    	
        if (nodeSize == size) {
            branchs.add(node);
        }
        else {
        	if (size == -1 && node != getRoot()) {
        		branchs.add(node);
        	}
        	else {
        		if (size == X_SIZE && node != getRoot()) {
        			if (nodeSize > 2) {
        				branchs.add(node);
        			}
            	}
        	}
        }
        
        for (int i = 0; i < node.getChildCount(); i ++) {
            getBranchsWithSize(node.getChildAt(i), branchs, size);
        }
    }
    
    protected void getLeavesOfTree(NodeEGPSv1 node, List<NodeEGPSv1> branchs) {
        int childCount = node.getChildCount();
        
        if (childCount == 0) branchs.add(node);
        
        for (int i = 0; i < childCount; i ++) {
            getLeavesOfTree(node.getChildAt(i), branchs);
        }
    }
    
    public List<NodeEGPSv1> getLeaves() {
        leafs.clear();
        getLeavesOfTree(getRoot(), leafs);
        return leafs;
    }
    
    public List<NodeEGPSv1> getLeavesOfDescendants(NodeEGPSv1 node) {
        leafs.clear();
        getLeavesOfTree(node, leafs);
        return leafs;
    }
    
    /**
     * The total tree length will be given if size == -1
     */
    public double getLengthOfBranchs(int size) {
    	if (! branchLengthsUpdated) {
    		initialBranchLengths(root.getSize());
            updateBranchLength();
            
            branchLengthsUpdated = true;
    	}
    	
        if (size <= 0) {
        	
            return branchLengths[0];
        }
        else {
            return branchLengths[size];
        }
    }
    
    public double getLengthOfTree() {
        return getLengthOfBranchs(-1);
    }
    
    public int getNumOfAlleles() {
        return getNumOfAlleles(getRoot());
    }
    
    public int getNumOfAlleles(NodeEGPSv1 root) {
        updateAllele(root);
        return alleleFreq[0].length;
    }
    
    public double[][] getAlleleFreq() {
        return getAlleleFreq(getRoot());
    }
    
    public double[][] getAlleleFreq(NodeEGPSv1 root) {
        updateAllele(root);
        return alleleFreq;
    }
    
    public int getNumOfMutations() {
        return getNumOfMutations(-1);
    }
    
    /**
     * The total mutations will be given if size == -1
     */    
    public int getNumOfMutations(int size) {
        return getNumOfMutations(getRoot(), size);
    }
    
    public int getNumOfMutations(NodeEGPSv1 root, int size) {
        int mut = 0;
        int childCount = root.getChildCount();
        if (childCount == 0) {
            // return 0;
        }
        else {
            for (int i = 0; i < childCount; i ++) {
                NodeEGPSv1 child = root.getChildAt(i);
                int childSize = child.getSize();
                if (childSize == size || size == -1) {
                    mut += child.getBranch().getMutation();
                }
                mut += getNumOfMutations(child, size);
            }
        }
        
        return mut;
    }
    
    public double getTheta() {
        int sampleSize = getSampleSize();
        int K = getNumOfMutations();
        double theta = K / an(sampleSize);
        return theta;
    }
    
    public double getTMRCA() {
        if (root.getChildCount() == 0) {
            return 0;
        }
        else {
            // the length of root should not be counted.
            // Sometimes, the length of the root could be non-zero, unless
            // the root is a real root for a completed tree
            NodeEGPSv1 child = root.getChildAt(0);
            double tmrca = child.getBranch().getLength() + getTMRCA(child);
            return tmrca;
        }
    }
    
    public double getTMRCA(NodeEGPSv1 node) {
        double tmrca = 0;
        if (node.getChildCount() != 0) {
            NodeEGPSv1 child = node.getChildAt(0);
            tmrca = child.getBranch().getLength() + getTMRCA(child);
        }
        
        return tmrca;
    }
    
    /**
     * The list will be changed during the next call!
     *
     * If size == -1, all of nodes will be given.
     */
    public List<NodeEGPSv1> getNodesWithSize(int size) {
        return getBranchsWithSize(size);
    }
    
    public List<NodeEGPSv1> getNodes() {
        return getBranchsWithSize(-1);
    }
    
    public double getPi() {
        double pi = 0;
        int sampleSize = getSampleSize();
        for (int i = 1; i < sampleSize; i ++) {
            pi += (sampleSize - i) * i * getNumOfMutations(i);
        }
        pi = pi * 2.0 / (double)(sampleSize * (sampleSize - 1));
        return pi;
    }
    
    public NodeEGPSv1 getRoot() {
        return root;
    }
    
    public int getSampleSize() {
        return root.getSize();
    }
    
    public void initialSize() {
        initialSize(getRoot());
    }
    /**
     * @see NodeEGPSv1#setSize(int)
     * @author yudalang
     */
    protected void initialSize(NodeEGPSv1 node) {
        if (node.getChildCount() == 0)
            node.setSize(1);
        else {
            int size = 0;
            for (int i = 0; i < node.getChildCount(); i ++) {
                NodeEGPSv1 child = node.getChildAt(i);
                initialSize(child);
                size += child.getSize();
            }
            node.setSize(size);
        }
    }
    
    public void initialBranchLengths(int n) {
        if (n > INITIAL_MAX_SAMPLE_SIZE) {
        	
        	INITIAL_MAX_SAMPLE_SIZE = n;
        	branchLengths = new double[n];
        }
    }
    
    public void setGenealogy(NodeEGPSv1 root) {
        setGenealogy(root, false);
    }
    
    public void setGenealogy(NodeEGPSv1 root, boolean initialSize) {
        this.root = root;
        clear();
        if (initialSize) initialSize();
        
        branchLengthsUpdated = false;
    }
    
    protected void updateBranchLength() {
        clearBranchLengths();
        NodeEGPSv1 root = getRoot();
        for (int i = 0; i < root.getChildCount(); i ++) {
            updateBranchLength(root.getChildAt(i));
        }
        branchLengthsUpdated = true;
    }
    
    protected void updateBranchLength(NodeEGPSv1 node) {
        int size = node.getSize();
        if (size < getSampleSize()) {
            double length = node.getBranch().getLength();
            branchLengths[size] += length;
            branchLengths[0] += length; // total tree length
        }
        
        for (int i = 0; i < node.getChildCount(); i ++) {
            updateBranchLength(node.getChildAt(i));
        }
    }
    
    protected void updateAllele(NodeEGPSv1 root){
        dis.clear();
        dis.setFactor(1);
        int nextAllele = INITIAL_SIZE;
        for (int i = 0; i < root.getChildCount(); i ++) {
            allele(root.getChildAt(i), nextAllele);
        }
        alleleFreq = dis.getFrequency();
    }
    
    /**
     *	find the number of alleles and frequencies under the infinite site model
     */
    protected void allele(NodeEGPSv1 a, int startingSize) {
        int mutation = a.getBranch().getMutation();
        int allele = startingSize;
        if (mutation > 0) { // we don't need to consider the number of mutations under the infinite site model
            allele = sizeAfterMutation(allele);
        }
        
        if(a.getChildCount() == 0) {
            dis.put(allele);
        }
        else{
            for(int i = 0; i < a.getChildCount(); i ++) {
                allele(a.getChildAt(i), allele);
            }
        }
    }
    
    /**
     * Mutate an allele under the infinite site model
     */
    protected int sizeAfterMutation(int size) {
        MersenneTwister rng = new MersenneTwister(8888);
        double ran = rng.nextDouble();
        size = (int)(Integer.MAX_VALUE * ran);
        return size;
    }
}
