package module.multiseq.alignment.view.gui;

import java.awt.Graphics2D;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;

public abstract class AbstructSequenceColor {

	public abstract void drawSequence(Graphics2D g2, SequenceI sequence, BasicSequenceData sequenceData, int xOffset,
			int yOffset, int charWidth, int charHeight);
}
