package module.multiseq.alignment.view.gui;

import java.awt.Graphics2D;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.model.SequenceBaseColor;
import msaoperator.alignment.sequence.SequenceComponentRatio;

public class HighlightPolymorphlicSitesSequenceJPanel extends AbstructSequenceColor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1842801759966677760L;

	@Override
	public void drawSequence(Graphics2D g2, SequenceI sequence, BasicSequenceData sequenceData, int xOffset,
			int yOffset, int charWidth, int charHeight) {

		char[] seq = sequence.getSeq();

		if (seq == null) {
			return; // fix for racecondition
		}
		List<SequenceComponentRatio> ratios = sequenceData.getRatio();

		for (int i = 0; i < seq.length; i++) {
			String base = String.valueOf(seq[i]);
			int stringLen = (int) g2.getFontMetrics().getStringBounds(base, g2).getWidth();
			int start = charWidth / 2 - stringLen / 2;
			SequenceComponentRatio sequenceRatio = ratios.get(i);

			int XPos = i * charWidth + xOffset;

			int percentage = sequenceRatio.getPercentage();

			if (percentage != 100) {
				String maxString = base.toUpperCase();
				g2.setBackground(SequenceBaseColor.getBaseColor(maxString));

				g2.clearRect(XPos, yOffset - charHeight, charWidth, charHeight);
			}

//			if (base == null || "".equals(base)) {
//				base = "-";
//			}
			//y参数要yOffset要做一点调整值，这样字可以落在方框比较中心的位置，否则g显示的时候会被遮挡
			g2.drawString(base, start + XPos, (int)(yOffset-charHeight*0.15));
		}

	}
	
	@Override
	public String toString() {
		return "Highlight polymorphic sites";
	}

}
