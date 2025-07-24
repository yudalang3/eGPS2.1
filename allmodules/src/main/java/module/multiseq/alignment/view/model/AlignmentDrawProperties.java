package module.multiseq.alignment.view.model;

import module.multiseq.alignment.view.gui.AbstructSequenceColor;
import module.multiseq.alignment.view.gui.NoColorSequenceJPanel;

public class AlignmentDrawProperties {

	private String myLayout = SequenceLayout.CONTINUOUS;
	private AbstructSequenceColor sequenceBackgroundColor = new NoColorSequenceJPanel();

	public String getMyLayout() {
		return myLayout;
	}

	public void setMyLayout(String myLayout) {
		this.myLayout = myLayout;
	}

	public void setSequenceBackgroundColor(AbstructSequenceColor sequenceBackgroundColor) {

		this.sequenceBackgroundColor = sequenceBackgroundColor;
	}

	public AbstructSequenceColor getSequenceBackgroundColor() {
		return sequenceBackgroundColor;
	}
}
