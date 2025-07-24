package module.heatmap.eheatmap.tree.cluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.random.JDKRandomGenerator;


public class KmeansPlusPlus {

	private int k; int maxIterations;
	private DistanceMeasure distMeasure;
	private double[][] data;
	private JDKRandomGenerator jdkRandomGenerator = new JDKRandomGenerator();
	
	public KmeansPlusPlus(int k, int maxIterations, DistanceMeasure distMeasure) {
		this.k = k;
		this.maxIterations = maxIterations;
		this.distMeasure = distMeasure;
		
		jdkRandomGenerator.setSeed(100);
	}
	
	public void setMatrix(double[][] data) {
		this.data = data;
	}
	
	public void setK(int k) {
		this.k = k;
	}
	
	public byte[] doClustering() {
		
		// we have a list of our locations we want to cluster. create a
		int length = data.length;
		List<LocationWrapper> clusterInput = new ArrayList<LocationWrapper>(length);
			
		for (int i = 0; i < length; i++) {
			clusterInput.add(new LocationWrapper(data[i],i));
		}
		// initialize a new clustering algorithm.
		// we use KMeans++ with 10 clusters and 10000 iterations maximum.
		// we did not specify a distance measure; the default (euclidean distance) is
		// used.
		
		
		KMeansPlusPlusClusterer<LocationWrapper> clusterer = 
				new KMeansPlusPlusClusterer<LocationWrapper>(k, maxIterations,
				distMeasure,jdkRandomGenerator);
		List<CentroidCluster<LocationWrapper>> clusterResults = clusterer.cluster(clusterInput);
		// output the clusters
		byte[] ret = new byte[length];
		//System.out.println("clusterResults.size() is\t" + clusterResults.size());
		for (int i = 0; i < clusterResults.size(); i++) {
			
			CentroidCluster<LocationWrapper> centroidCluster = clusterResults.get(i);
			//System.out.println("Cluster " + i + "\t"+centroidCluster.getCenter());
			List<LocationWrapper> points = centroidCluster.getPoints();
			
			for (LocationWrapper locationWrapper : points) {
				ret[locationWrapper.getIndex()] = (byte) i;
			}
			
		}

		return ret;
	}
	
}


//wrapper class
class LocationWrapper implements Clusterable {
	private double[] values;
	private int index;
	
	public LocationWrapper(double[] values, int i) {
		this.values = values;
		index = i;
	}

	@Override
	public double[] getPoint() {
		return values;
	}
	
	public int getIndex() {
		return index;
	}
	
}
