package module.multiseq.alignment.trimmer.calculate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import egps2.EGPSProperties;
import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.parser.FastaParser;

public class AlignmentTrimmer {

	// startAlignPos: 0-based, inclusive
	private int startAlignPos;
	// endAlignPos: 0-based, inclusive
	private int endAlignPos;
	private BasicSequenceData basicSequenceData;

	BasicSequenceData trimAlignment(String refSeqName, int startPos, int endPos, BasicSequenceData basicSequenceData) {
		this.basicSequenceData = basicSequenceData;
		List<SequenceI> sequences = basicSequenceData.getDataSequences();
		List<String> seqNames = basicSequenceData.getSequenceNames();
		int numOfOTU = seqNames.size();

		// Get ref sequence
		int indexOfRefSequence = -1;
		for (int i = 0; i < numOfOTU; i++) {
			String string = seqNames.get(i);
			if (refSeqName.equalsIgnoreCase(string)) {
				indexOfRefSequence = i;
				break;
			}
		}
		// Check
		if (indexOfRefSequence == -1) {
			throw new IllegalArgumentException("Your input String for reference genome is incorrect!");
		}

		SequenceI refSeq = sequences.get(indexOfRefSequence);

		PositionConvertor positionConvertor = new PositionConvertor();
		positionConvertor.setData(refSeq);

		/**
		 * <pre>
		 *                    ElegantJTable      T      C       G     ElegantJTable      T     C    G
		 *                    1      2      3      [4     5      6]    7    8   1-based
		 *                    0      1      2      [3     4      5     6)   7   0-based
		 * </pre>
		 */
		// startAlignPos: 0-based, inclusive
		startAlignPos = positionConvertor.convertRefGenomePos2AlignPos(startPos - 1);
		// endAlignPos: 0-based, inclusive
		endAlignPos = positionConvertor.convertRefGenomePos2AlignPos(endPos - 1);

		List<SequenceI> seqElements = new ArrayList<SequenceI>();
		for (SequenceI element : sequences) {
			seqElements.add(element.getSequenceI(startAlignPos, endAlignPos + 1));
		}
		BasicSequenceData retSequenceData = new BasicSequenceData(seqElements);

		return retSequenceData;
	}

	/**
	 * 1-based ,左闭右闭，都是需要保留的，输出的目录是同一个，文件名固定!
	 * 
	 * @param refSeqName    参考序列的名称
	 * @param startPos      起始位置，1 based
	 * @param endPos        终止位置, 1 based
	 * @param inputFilePath 输入文件的路径
	 * @return
	 * @throws Exception
	 */
	BasicSequenceData trimAlignment(String refSeqName, int startPos, int endPos, String inputFilePath)
			throws Exception {
		// Check
		Objects.requireNonNull(refSeqName);
		Objects.requireNonNull(inputFilePath);
		if (startPos < 1 || endPos < startPos) {
			throw new IllegalArgumentException();
		}
		// Load data
		FastaParser fastaParser = new FastaParser(new File(inputFilePath));
		fastaParser.parse();
		BasicSequenceData basicSequenceData = fastaParser.getSeqElements();

		return trimAlignment(refSeqName, startPos, endPos, basicSequenceData);
	}

	/**
	 * 
	 * 1-based ,左闭右闭，都是需要保留的，输出的目录是同一个，文件名固定!
	 * 
	 * 先trim数据，然后将trim后的数据写入到fasta文件，这个fasta文件名的后缀是 ".trimmed.fas"！ 注意这里的fasta文件是
	 * 连续型的fasta文件！就是说是:
	 * 
	 * <pre>
	 * >seqName1
	 * ATCGCTACGT一整条，不会换行！
	 * </pre>
	 * 
	 * @param refSeqName    参考序列的名称
	 * @param startPos      起始位置，1 based
	 * @param endPos        终止位置, 1 based
	 * @param inputFilePath 输入文件的路径
	 * @return 参考基因组序列non-gap 总数
	 * @throws Exception
	 */
	public int trimAlignmentAndWrite2FileWithSuffix(String refSeqName, int startPos, int endPos, String inputFilePath)
			throws Exception {

		int ret = 0;
		BasicSequenceData trimAlignment = trimAlignment(refSeqName, startPos, endPos, inputFilePath);

		BufferedWriter bfWriter = new BufferedWriter(new FileWriter(inputFilePath + ".trimmed.fas"));

		List<SequenceI> dataSequences = trimAlignment.getDataSequences();
		for (SequenceI sequenceI : dataSequences) {
			bfWriter.write(">");
			String seqName = sequenceI.getSeqName();
			bfWriter.write(seqName);
			bfWriter.write("\n");
			bfWriter.write(sequenceI.getSeqAsString());
			bfWriter.write("\n");

			if (seqName.equalsIgnoreCase(refSeqName)) {
				ret = sequenceI.getLength();
			}
		}

		bfWriter.close();

		return ret;
	}

	public int trimAlignmentAndWrite2FileWithSuffixAndOutputFile(String refSeqName, int startPos, int endPos,
			String inputFilePath, String outFilePath) throws Exception {

		int ret = 0;
		BasicSequenceData trimAlignment = trimAlignment(refSeqName, startPos, endPos, inputFilePath);

		BufferedWriter bfWriter = new BufferedWriter(new FileWriter(outFilePath));

		List<SequenceI> dataSequences = trimAlignment.getDataSequences();
		for (SequenceI sequenceI : dataSequences) {
			bfWriter.write(">");
			String seqName = sequenceI.getSeqName();
			bfWriter.write(seqName);
			bfWriter.write("\n");
			bfWriter.write(sequenceI.getSeqAsString());
			bfWriter.write("\n");

			if (seqName.equalsIgnoreCase(refSeqName)) {
				ret = sequenceI.getLength();
			}
		}

		bfWriter.close();

		return ret;
	}

	public static void main(String[] args) throws Exception {
		// Please see the full usage and validate process in Junit4 class

		AlignmentTrimmer alignmentTrimmer = new AlignmentTrimmer();
		alignmentTrimmer.trimAlignmentAndWrite2FileWithSuffix("Human-CoV-2019", 1, 2,
				EGPSProperties.PROPERTIES_DIR + "/bioData/aligned.6.virus.refined.fas");
		alignmentTrimmer.trimAlignmentAndWrite2FileWithSuffix("NC_045512", 101, 29903 - 100 - 1,
				"G:\\BaiduNetdiskDownload\\SARS-COV-2\\产生真实的数据\\v7\\aligned.Bat.SARS.CoV2.fasta");
		alignmentTrimmer.trimAlignmentAndWrite2FileWithSuffix("NC_045512", 101, 29903 - 100 - 1,
				"G:\\BaiduNetdiskDownload\\SARS-COV-2\\产生真实的数据\\v7\\aligned.Bat.Pangolin.SARS.CoV2.fasta");
	}
}
