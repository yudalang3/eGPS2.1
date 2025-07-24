package module.evolview.gfamily.work.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.parser.FastaParser;
import module.multiseq.alignment.trimmer.calculate.PositionConvertor;

public class AlignmentGetSequence {

	private SequenceI refSequence = null;
	private List<SequenceI> altSequence = new ArrayList<>();

	private PositionConvertor positionConvertor = new PositionConvertor();

	public List<SequenceI> returnDNASequence(int start, int end) {
		List<SequenceI> returnedSeq = new ArrayList<>();

		int alignStart = positionConvertor.convertRefGenomePos2AlignPos(start);
		int alignEnd = positionConvertor.convertRefGenomePos2AlignPos(end - 1);

		SequenceI refSeq = new Sequence(refSequence.getSeqName(), refSequence.getSeqAsString(alignStart, alignEnd + 1));
		refSeq.setStartRes(start);
		refSeq.setEndRes(end);
		returnedSeq.add(refSeq);

		for (int i = 0; i < altSequence.size(); i++) {
			SequenceI altSeq = new Sequence(altSequence.get(i).getSeqName(),
					altSequence.get(i).getSeqAsString(alignStart, alignEnd + 1));
			altSeq.setStartRes(start);
			altSeq.setEndRes(end);
			returnedSeq.add(altSeq);
		}

		return returnedSeq;
	}
	public void setGetSequenceByLocal(File alignmentFile) throws Exception {
		FastaParser fastaParser = new FastaParser(alignmentFile);
		fastaParser.parse();
		BasicSequenceData basicSequenceData = fastaParser.getSeqElements();

		List<SequenceI> sequences = basicSequenceData.getDataSequences();
		initialize(sequences);
	}
	public void setSequenceWithOnlyOneEntry(SequenceI sequence) throws Exception {
		List<SequenceI> sequences = Arrays.asList(sequence);
		initialize(sequences);
	}

	private void initialize(List<SequenceI> sequences) {
		int size = sequences.size();
		if (size == 0) {
			throw new IllegalArgumentException("Do not have sequences.");
		} else if (size == 1) {
			refSequence = sequences.get(0);
		} else {
			refSequence = sequences.get(0);
			sequences.remove(0);
			altSequence = sequences;
		}

		positionConvertor.setData(refSequence);
	}

	public SequenceI getRefSequence() {
		return refSequence;
	}

	public List<SequenceI> getAltSequence() {
		return altSequence;
	}


}
