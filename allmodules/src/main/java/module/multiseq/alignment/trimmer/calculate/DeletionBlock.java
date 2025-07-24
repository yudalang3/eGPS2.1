package module.multiseq.alignment.trimmer.calculate;

public class DeletionBlock {
	public int startAlignPos = -1; // 0-based, inclusive
	public int endAlignPos = -1; // 0-based, exclusive
	public int refBaseIndexAfterDeletionBlocks = -1; // 0-based, inclusive
}

