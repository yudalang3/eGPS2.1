package module.multiseq.alignment.view.gui.leftcontrol;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import egps2.UnifiedAccessPoint;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.model.SequenceLayout;

public class AlignmentLayoutLeftPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7497228842851798536L;

	private AlignmentViewMain aVMian;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	private JRadioButton interLeaved;

	private JRadioButton continuous;

	public AlignmentLayoutLeftPanel(AlignmentViewMain alignmentViewMain) {
		this.aVMian = alignmentViewMain;
		setLayout(new BorderLayout());
		Box initBox = Box.createVerticalBox();
		initLayout(initBox);
		add(initBox, BorderLayout.WEST);

	}

	private void initLayout(Box baseBox) {

		JPanel displayJPane = new JPanel(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;

		continuous = getContinuous();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		displayJPane.add(continuous, gridBagConstraints);

		interLeaved = getInterLeaved();

		interLeaved.setFont(defaultFont);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		displayJPane.add(interLeaved, gridBagConstraints);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(interLeaved);
		buttonGroup.add(continuous);

		continuous.setSelected(true);

		baseBox.add(displayJPane);

	}

	public JRadioButton getContinuous() {
		if (continuous == null) {
			continuous = new JRadioButton("Continuous");
			continuous.setFont(defaultFont);
			continuous.setFocusPainted(false);
			continuous.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					aVMian.getAlignmentDrawProperties().setMyLayout(SequenceLayout.CONTINUOUS);
					aVMian.setAlignmentLayout();
					aVMian.invokeTheFeature(0);
				}
			});
		}
		return continuous;
	}

	private JRadioButton getInterLeaved() {
		if (interLeaved == null) {

			interLeaved = new JRadioButton("InterLeaved");
			interLeaved.setFont(defaultFont);
			interLeaved.setFocusPainted(false);
			interLeaved.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					aVMian.getAlignmentDrawProperties().setMyLayout(SequenceLayout.INTERLEAVED);
					aVMian.setAlignmentLayout();
					
					aVMian.invokeTheFeature(0);
				}
			});
		}
		return interLeaved;
	}

}
