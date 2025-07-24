package module.multiseq.alignment.view.gui;

import java.awt.Graphics2D;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.model.SequenceBaseColor;


public class HighlightAllSitesSequenceJPanel extends AbstructSequenceColor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6990283181965688379L;

	@Override
	public void drawSequence(Graphics2D g2, SequenceI sequence, BasicSequenceData sequenceData, int xOffset,
			int yOffset, int charWidth, int charHeight) {

		char[] seq = sequence.getSeq();

		if (seq == null) {
			return; // fix for racecondition
		}

		for (int i = 0; i < seq.length; i++) {
			String base = String.valueOf(seq[i]);
			int stringLen = (int) g2.getFontMetrics().getStringBounds(base, g2).getWidth();
			int start = charWidth / 2 - stringLen / 2;
			int XPos = i * charWidth;
			String maxString = base.toUpperCase();
			g2.setBackground(SequenceBaseColor.getBaseColor(maxString));
			
//			if (maxString.contentEquals(SequenceBaseColor.ElegantJTable.getMessage())) {
//				g2.setBackground(SequenceBaseColor.ElegantJTable.getColor());
//			} else if (maxString.contentEquals(SequenceBaseColor.C.getMessage())) {
//				g2.setBackground(SequenceBaseColor.C.getColor());
//			} else if (maxString.contentEquals(SequenceBaseColor.G.getMessage())) {
//				g2.setBackground(SequenceBaseColor.G.getColor());
//			} else if (maxString.contentEquals(SequenceBaseColor.T.getMessage())) {
//				g2.setBackground(SequenceBaseColor.T.getColor());
//			} else if (maxString.contentEquals(SequenceBaseColor.U.getMessage())) {
//				g2.setBackground(SequenceBaseColor.U.getColor());
//			} else {
//				g2.setBackground(SequenceBaseColor.OTHES.getColor());
//			}

			//y参数要“yOffset - charHeight+5”加一点调整值，否则g显示的时候会被遮挡
			g2.clearRect(XPos + xOffset, yOffset - charHeight, charWidth, charHeight);

//			if (base == null || "".equals(base)) {
//				base = "-";
//			}
			//y参数要yOffset要做一点调整值，这样字可以落在方框比较中心的位置，否则g显示的时候会被遮挡
			g2.drawString(base, start + XPos + xOffset, (int)(yOffset-charHeight*0.15));
		}
		
	
		
	}
	
	@Override
	public String toString() {
		return "Highlight all sites";
	}

}
