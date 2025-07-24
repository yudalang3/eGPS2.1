package module.evolview.gfamily.work.gui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.model.enums.TreeLayoutEnum;


/**
 *  Rectangular / Circular / Spiral / Slant / 和 Radial 布局切换按钮
 * 
 */
@SuppressWarnings("serial")
public class TreeLayoutSwitcher extends BaseCtrlPanel{

	private JToggleButton radialCladogram = new JToggleButton();
	private JToggleButton slopeCladogram = new JToggleButton();
	private JToggleButton spiralPhyloWithAlphaPhylogram = new JToggleButton();
	private JToggleButton circularPhylogram = new JToggleButton();
	protected JToggleButton standardPhylogram = new JToggleButton();
	protected JToggleButton prevButton;

	public TreeLayoutSwitcher(CtrlTreeLayoutPanel leftTreeLayoutPanel) {

		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		ButtonGroup group = new ButtonGroup();

		standardPhylogram.setToolTipText("Rectangular layout");
		standardPhylogram.setIcon(IconObtainer.get("standardPladogram.png"));
		standardPhylogram.setSelected(true);
		prevButton = standardPhylogram;
		standardPhylogram.addActionListener(e -> {
			if (standardPhylogram.isSelected() && prevButton != standardPhylogram) {
				prevButton = standardPhylogram;
				leftTreeLayoutPanel.showCardLayout(TreeLayoutEnum.RECTANGULAR_LAYOUT);
			}
		});

		add(standardPhylogram);
		group.add(standardPhylogram);

		circularPhylogram.setToolTipText("Circular Layout");
		circularPhylogram.setIcon(IconObtainer.get("circularPhylo.png"));
		circularPhylogram.addActionListener(e -> {
			if (circularPhylogram.isSelected() && prevButton != circularPhylogram) {
				prevButton = circularPhylogram;
				leftTreeLayoutPanel.showCardLayout(TreeLayoutEnum.CIRCULAR_LAYOUT);
			}
		});

		add(circularPhylogram);
		group.add(circularPhylogram);

		spiralPhyloWithAlphaPhylogram.setToolTipText("Spiral layout");
		spiralPhyloWithAlphaPhylogram.setIcon(IconObtainer.get("sprialAlpha.png"));
		spiralPhyloWithAlphaPhylogram.addActionListener(e -> {
			if (spiralPhyloWithAlphaPhylogram.isSelected() && prevButton != spiralPhyloWithAlphaPhylogram) {
				prevButton = spiralPhyloWithAlphaPhylogram;
				leftTreeLayoutPanel.showCardLayout(TreeLayoutEnum.SPRIAL_LAYOUT);
			}
		});
		add(spiralPhyloWithAlphaPhylogram);
		group.add(spiralPhyloWithAlphaPhylogram);

		slopeCladogram.setToolTipText("Slant layout");
		slopeCladogram.setIcon(IconObtainer.get("slopeCladogram.png"));
		slopeCladogram.addActionListener(e -> {
			if (slopeCladogram.isSelected() && prevButton != slopeCladogram) {
				prevButton = slopeCladogram;
				leftTreeLayoutPanel.showCardLayout(TreeLayoutEnum.SLOPE_LAYOUT);
			}
		});

		add(slopeCladogram);
		group.add(slopeCladogram);

		radialCladogram.setToolTipText("Radial layout");
		radialCladogram.setIcon(IconObtainer.get("radicalClado.png"));
		radialCladogram.addActionListener(e -> {
			if (radialCladogram.isSelected() && prevButton != radialCladogram) {
				prevButton = radialCladogram;
				leftTreeLayoutPanel.showCardLayout(TreeLayoutEnum.RADICAL_LAYOUT);
			}
		});

		add(radialCladogram);
		group.add(radialCladogram);
		
		Border emptyBorder = BorderFactory.createEmptyBorder(5,7,5,7);
		Component[] components = getComponents();
		for (Component component : components) {
			JToggleButton bb = (JToggleButton) component;
			bb.setBorder(emptyBorder);
		}

	}

	public void enableTreeLayoutSwitcherButtons(boolean enable) {
		radialCladogram.setEnabled(enable);
		slopeCladogram.setEnabled(enable);
		spiralPhyloWithAlphaPhylogram.setEnabled(enable);
		circularPhylogram.setEnabled(enable);
		standardPhylogram.setEnabled(enable);
	}


}
