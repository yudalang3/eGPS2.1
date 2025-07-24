package module.treebuilder.fromdist;

public class PLDistMatrix2PhyloTree extends PLGeneticDistsFile2Phylotree {

	public PLDistMatrix2PhyloTree(String[] names, double[][] distanceMatrix) {
		this.names = names;
		this.distanceMatrix = distanceMatrix;
	}

	@Override
	public int processNext() throws Exception {
		switch (processIndex) {
		case 1:
			assignParameters();
			processIndex++;
			break;
		case 2:
			runningBuildTree();
			processIndex++;
			break;
		default:
			return PROGRESS_FINSHED;
		}
		return processIndex;
	}

}
