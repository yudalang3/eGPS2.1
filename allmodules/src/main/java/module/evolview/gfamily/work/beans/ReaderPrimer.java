package module.evolview.gfamily.work.beans;

import java.util.ArrayList;
import java.util.List;

import module.evolview.gfamily.work.gui.browser.PrimerUtil;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimers;
import module.evolview.gfamily.work.model.AlignmentGetSequence;
import msaoperator.alignment.sequence.SequenceI;

/**
 * 读取网络中引物文件(压缩/非压缩)
 *
 * @Author: mhl
 */
public class ReaderPrimer {
	private final int numberOfFieldsInTheFile = 7; // 文件中以空格分割后字段的数量

	public DrawingPropertyPrimerSet generateNcov2019GenomePaintPrimerSet(AlignmentGetSequence sequence,
			List<String> primerLines) {
		DrawingPropertyPrimerSet paintPrimerSet = new DrawingPropertyPrimerSet();
		List<DrawingPropertyPrimers> paintPrimers = new ArrayList<>();
		paintPrimerSet.setHeader(primerLines.get(0));
		int size = primerLines.size();
		for (int i = 1; i < size; i++) {
			String readLine = primerLines.get(i);
			if (readLine.trim().isEmpty()) {
				continue;
			}
			DrawingPropertyPrimers paintPrimer = new DrawingPropertyPrimers();
			String[] split = readLine.split("\t");
			if (split == null || split.length != numberOfFieldsInTheFile) {
				continue;
			}
			paintPrimer.setInstitution(split[0]);
			paintPrimer.setGene(split[1]);
			paintPrimer.setIndex(Integer.valueOf(split[2]));
			Integer fStart = Integer.valueOf(split[3]);
			Integer fEnd = Integer.valueOf(split[4]);
			Integer rStart = Integer.valueOf(split[5]);
			Integer rEnd = Integer.valueOf(split[6]);

			paintPrimer.setFStart(fStart);
			paintPrimer.setFEnd(fEnd);
			paintPrimer.setRStart(rStart);
			paintPrimer.setREnd(rEnd);

			List<SequenceI> sequenceIS = sequence.returnDNASequence(fStart - 1, fEnd);
//			sequence.ridGap(sequenceIS);
			SequenceI sequenceI = sequenceIS.get(0);
			paintPrimer.setForward(sequenceI.getSeqAsString());

			// sequence = dataModel.getSequence();
			sequenceIS = sequence.returnDNASequence(rStart - 1, rEnd);
//			sequence.ridGap(sequenceIS);
			sequenceI = sequenceIS.get(0);
			String seqAsString = sequenceI.getSeqAsString();

			char[] chars = seqAsString.toCharArray();
			int length = chars.length;
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < length; j++) {
				String s = String.valueOf(chars[j]);
				sb.append(PrimerUtil.forwardPrimersToreversePrimers(s));
			}
			paintPrimer.setReverse(sb.toString());
			paintPrimers.add(paintPrimer);
		}
		paintPrimerSet.setPrimers(paintPrimers);
		return paintPrimerSet;
	}

}
