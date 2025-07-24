package module.heatmap.eheatmap.util;

public class FactorHandler{
	int globalIndex;
	int[] gapLocations;
	int limit;
	
	public FactorHandler(int globalIndex, int[] gapLocations, int limit) {
		super();
		this.globalIndex = globalIndex;
		this.gapLocations = gapLocations;
		this.limit = limit;
	}
	
}