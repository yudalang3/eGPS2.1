package module.heatmap.eheatmap.tree;

import java.awt.Color;

import module.heatmap.eheatmap.model.PairwiseDistance;
import module.heatmap.eheatmap.model.dist.AvgDotProduct;
import module.heatmap.eheatmap.tree.cluster.HClustering;

public class ClusterParameter implements Cloneable{
	
	private TreeMethod treeMethod;
	private PairwiseDistance pairwiseDistance;
	
	private int r = 0;
	private int g = 0;
	private int b = 0;
	private float lineWidth = 1;
	
	/**
	 * 0 UPGMA
	 * 1 single
	 * 2 complete
	 */
	private int clusterLinkageType = 0;
	
	@Override
	public ClusterParameter clone() throws CloneNotSupportedException {
		ClusterParameter ret = new ClusterParameter();
		ret.treeMethod = this.treeMethod;
		ret.pairwiseDistance = this.pairwiseDistance;
		ret.r = this.r;
		ret.g = this.g;
		ret.b = this.b;
		ret.lineWidth = this .lineWidth;
		return ret;
	}
	
	public int getClusterLinkageType() {
		return clusterLinkageType;
	}
	/**
	 * 0 UPGMA
	 * 1 single
	 * 2 complete
	 */
	public void setClusterLinkageType(int clusterLinkageType) {
		this.clusterLinkageType = clusterLinkageType;
	}

	public void setTreeMethod(TreeMethod treeMethod) {
		this.treeMethod = treeMethod;
	}
	
	public void setPairwiseDistance(PairwiseDistance pairwiseDistance) {
		this.pairwiseDistance = pairwiseDistance;
	}
	
	public TreeMethod getTreeMethod() {
		HClustering hClustering = new HClustering();
		hClustering.setLinkageMethodIndex(clusterLinkageType);
		return hClustering;
	}
	
	public PairwiseDistance getPairwiseDistance() {
		if (pairwiseDistance == null) {
			//Default
			pairwiseDistance = new AvgDotProduct();
		}
		
		return pairwiseDistance;
	}
	
	public float getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	public Color getColor() {
		return new Color(r, g, b);
	}
	
	public void setColor(int r,int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

}
