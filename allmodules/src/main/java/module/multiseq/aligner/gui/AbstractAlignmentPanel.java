package module.multiseq.aligner.gui;

import javax.swing.JPanel;

import module.multiseq.aligner.MultipleSeqAlignerMain;

public abstract class AbstractAlignmentPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	protected MultipleSeqAlignerMain alignmentMain;

	public AbstractAlignmentPanel(MultipleSeqAlignerMain alignmentMain) {
		this.alignmentMain = alignmentMain;
	}

}
