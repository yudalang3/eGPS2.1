package module.evolview.gfamily.work.gui.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JToolTip;

@SuppressWarnings("serial")
public abstract class AbstractBarPlot extends AbstractTrackPanel {

	public AbstractBarPlot(BrowserPanel genomeMain) {
		super(genomeMain);
	}

	@Override
	public boolean contains(int x, int y) {
		if (super.contains(x, y)) {
			if (shape instanceof TooltiperAlleleFreq) {
				return true;
			}
		}
		return false;
	}

	@Override
	public JToolTip createToolTip() {
		
		JToolTip tip = new BlankToolTip();
		Dimension preferredSize = tip.getPreferredSize();
		preferredSize.setSize(220, 60);
		tip.setPreferredSize(preferredSize);
		tip.setComponent(this);

		return tip;
		// 下面这个其实属于定制的功能了，应该移除
		
//		MutationFrequencyTooltip mutationFrequencyTooltip = dataModel.getMutationFrequencyTooltip();
//		if (mutationFrequencyTooltip == null) {
//			JToolTip tip = new BlankToolTip();
//			Dimension preferredSize = tip.getPreferredSize();
//			preferredSize.setSize(220, 60);
//			tip.setPreferredSize(preferredSize);
//			tip.setComponent(this);
//
//			return tip;
//		}
//		JToolTip tip = new BarPlotToolTip();
//		
//		return tip;
	}

	 class BlankToolTip extends JToolTip{
		private static final long serialVersionUID = 1L;
		
		public BlankToolTip() {
			setBackground(Color.white);
			
			setLayout(new BorderLayout());
			JLabel jLabel = new JLabel("The feature will coming soon...");
			add(jLabel, BorderLayout.CENTER);
		}
	}
}
