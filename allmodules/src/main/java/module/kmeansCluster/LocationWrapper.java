package module.kmeansCluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

//wrapper class
public class LocationWrapper implements Clusterable {
	private double[] points;
	private Location location;

	public LocationWrapper(Location location) {
     this.location = location;
     this.points = new double[] { location.getX(), location.getY() };
 }

	public Location getLocation() {
		return location;
	}

	@Override
	public double[] getPoint() {
		return points;
	}
	
	public static void main(String[] args) {
		// we have a list of our locations we want to cluster. create a      
		List<Location> locations = new ArrayList<>();
		locations.add(new Location(1, 2));
		locations.add(new Location(2, 3));
		locations.add(new Location(5, 6));
		locations.add(new Location(8, 4));
		locations.add(new Location(9, 10));
		List<LocationWrapper> clusterInput = new ArrayList<LocationWrapper>(locations.size());
		for (Location location : locations)
		    clusterInput.add(new LocationWrapper(location));
	
		// initialize a new clustering algorithm. 
		// we use KMeans++ with 10 clusters and 10000 iterations maximum.
		// we did not specify a distance measure; the default (euclidean distance) is used.
		KMeansPlusPlusClusterer<LocationWrapper> clusterer = new KMeansPlusPlusClusterer<LocationWrapper>(2, 30);
		List<CentroidCluster<LocationWrapper>> clusterResults = clusterer.cluster(clusterInput);

		// output the clusters
		System.out.println("clusterResults.size() is\t" + clusterResults.size());
		for (int i=0; i<clusterResults.size(); i++) {
		    System.out.println("Cluster " + i);
		    for (LocationWrapper locationWrapper : clusterResults.get(i).getPoints())
		        System.out.println(locationWrapper.getLocation());
		    System.out.println();
		}
		EuclideanDistance euclideanDistance = new EuclideanDistance();
	}
}

class Location {
	double x;
	double y;

	
	
	public Location(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}