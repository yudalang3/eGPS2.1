package module.heatmap.eheatmap.util;

public class FactorCalculator {

	public final static int getIncreasedAddingFactor(FactorHandler f,int lowerDownIndex) {
		if (f.globalIndex == f.limit) {
			return f.limit;
		}
		int tt = f.gapLocations[f.globalIndex];
		if (lowerDownIndex == tt) {
			return ++ f.globalIndex;
		}else {
			return f.globalIndex;
		}
		
	}
}
