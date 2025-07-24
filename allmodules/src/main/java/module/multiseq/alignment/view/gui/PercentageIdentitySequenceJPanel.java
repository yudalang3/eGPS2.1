package module.multiseq.alignment.view.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.alignment.sequence.SequenceComponentRatio;

public class PercentageIdentitySequenceJPanel extends AbstructSequenceColor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8445330225362713623L;

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
			Character base2 = sequenceRatio.getBase();
			int XPos = i * charWidth + xOffset;

			if (base2 == seq[i]) {
				int percentage = sequenceRatio.getPercentage();
				if (percentage > 80) {
					g2.setBackground(new Color(100, 100, 255));
				} else if (percentage > 60) {
					g2.setBackground(new Color(153, 153, 255));
				} else if (percentage > 40) {
					g2.setBackground(new Color(204, 204, 255));
				} else {
					g2.setBackground(Color.white);
				}

				g2.clearRect(XPos , yOffset - charHeight, charWidth, charHeight);
			}

//			if (base == null || "".equals(base)) {
//				base = "-";
//			}
			//y参数要yOffset要做一点调整值，这样字可以落在方框比较中心的位置，否则g显示的时候会被遮挡
			g2.drawString(base, start + XPos,(int)(yOffset-charHeight*0.15));
		}

	}
	
	@Override
	public String toString() {
		return "By percentage identity";
	}

}
