package module.multiseq.alignment.trimmer.calculate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.parser.FastaParser;

/**
 * PositionConvertor: convert AlignPos to RefGenomePos PositionConvertor:
 * convert RefGenomePos to AlignPos Input is 0 based
 */
public class PositionConvertor {
	protected final char indel = '-';

	protected int alignLength = -1; // with indels
	protected int refGenomeLength = -1; // No indels

	protected List<DeletionBlock> listOfDeletionBlocks = new ArrayList<DeletionBlock>(10);

	public void setData(SequenceI alignedRefGenome) {

		String seqStr = alignedRefGenome.getSeqAsString();
		setData(seqStr);
	}

	public void setData(String alignedRefGenome) {
		int refGenomePos = 0;

		alignLength = alignedRefGenome.length();

		for (int alignPos = 0; alignPos < alignLength;) {

			char consideredSite = alignedRefGenome.charAt(alignPos);
			if (consideredSite == indel) {
				DeletionBlock deletionBlock = new DeletionBlock();
				deletionBlock.startAlignPos = alignPos; // 0-based, inclusive

				for (; alignPos < alignLength; alignPos++) {

					char nextSite = alignedRefGenome.charAt(alignPos);
					if (nextSite != indel) {
						deletionBlock.endAlignPos = alignPos; // 0-based, exclusive
						deletionBlock.refBaseIndexAfterDeletionBlocks = refGenomePos; // 0-based, inclusive

						refGenomePos++;
						alignPos++;
						break;
					}

				}

				listOfDeletionBlocks.add(deletionBlock);
			} else {
				alignPos++;

				refGenomePos++;
			}

		}

		refGenomeLength = refGenomePos;

		if (listOfDeletionBlocks.size() > 0) {
			DeletionBlock lastDeletionBlock = listOfDeletionBlocks.get(listOfDeletionBlocks.size() - 1);

			if (lastDeletionBlock.endAlignPos == -1) {
				lastDeletionBlock.endAlignPos = alignLength;
			}
		}
	}

	/**
	 * 0-based.
	 */
	public int convertAlignPos2RefGenomePos(int alignPos) {
		if (alignPos < 0 || alignPos >= alignLength) {
			System.out.print("");
			throw new IllegalArgumentException();
		}

		int numOfDeletionBlocks = listOfDeletionBlocks.size();
		if (numOfDeletionBlocks == 0) {
			return alignPos;
		}

		int refGenomePos = alignPos;
		if (alignPos < listOfDeletionBlocks.get(0).endAlignPos && listOfDeletionBlocks.get(0).startAlignPos == 0) {
			refGenomePos = -1;
			return refGenomePos;
		}
		for (int blockIndex = 0; blockIndex < numOfDeletionBlocks; blockIndex++) {

			DeletionBlock deletionBlock = listOfDeletionBlocks.get(blockIndex);
			if (deletionBlock.startAlignPos <= alignPos && alignPos < deletionBlock.endAlignPos) {

				int removedLength = alignPos - deletionBlock.startAlignPos + 1;
				refGenomePos -= removedLength;

				break;
			}

			if (deletionBlock.endAlignPos <= alignPos) {

				int removedLength = deletionBlock.endAlignPos - deletionBlock.startAlignPos;
				refGenomePos -= removedLength;
			} else {
				// alignPos < deletionBlock.startPos
				break;
			}
		}

		return refGenomePos;
	}

	/**
	 * 0-based. ydl: both input and out put is 0 based!
	 */
	public int convertRefGenomePos2AlignPos(int refGenomePos) {
		if (refGenomePos < 0 || refGenomePos >= refGenomeLength) {
			throw new IllegalArgumentException("Current pos: " + refGenomePos + " genome length:" + refGenomeLength);
		}

		int numOfDeletionBlocks = listOfDeletionBlocks.size();
		if (numOfDeletionBlocks == 0) {
			return refGenomePos;
		}

		int alignPos = refGenomePos;

		// refGenomePos won't be located within a gap.
		for (int blockIndex = 0; blockIndex < numOfDeletionBlocks; blockIndex++) {

			DeletionBlock deletionBlock = listOfDeletionBlocks.get(blockIndex);
			if (deletionBlock.refBaseIndexAfterDeletionBlocks == -1) {
				break;
			}
			if (deletionBlock.refBaseIndexAfterDeletionBlocks <= refGenomePos) {
				int gapLength = deletionBlock.endAlignPos - deletionBlock.startAlignPos;
				alignPos += gapLength;
			} else {
				// alignPos < deletionBlock.startPos
				break;
			}
		}

		return alignPos;
	}

	public static void main(String[] args) throws Exception {
//		String fileName = "C:\\Users\\Evolgen\\Desktop\\SARS-COV 2019\\6 viruses\\translate_old_AY278741.1\\aligned.29558-29674.fasta";
		String fileName = "C:\\Users\\Evolgen\\Desktop\\SARS-COV 2019\\6 viruses\\aligned.6.virus.refined3.fas";
		File file = new File(fileName);
		FastaParser fastaParser = new FastaParser(file);
		fastaParser.parse();
		BasicSequenceData basicSequenceData = fastaParser.getSeqElements();
		List<SequenceI> sequenceIS = basicSequenceData.getDataSequences();
		SequenceI refSequence = sequenceIS.get(0);
		sequenceIS.remove(0);
		List<SequenceI> altSequence = sequenceIS;

		PositionConvertor positionConvertor = new PositionConvertor();
		positionConvertor.setData(refSequence);

//		for (int i = 3060; i < 3080; i++) {
//			int alignPos = positionConvertor.convertRefGenomePos2AlignPos(i);
//			System.out.println(i + " " + alignPos);
//		}

		for (int i = 100; i < 200; i++) {
			int refPos = positionConvertor.convertAlignPos2RefGenomePos(i);
			System.out.println(i + " " + refPos);
		}

	}
}
