package module.multiseq.alignment.view.gui.leftcontrol;

import java.awt.BorderLayout;
import java.awt.Color;
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
import module.multiseq.alignment.view.gui.HighlightAllSitesSequenceJPanel;
import module.multiseq.alignment.view.gui.HighlightAllSitesSequenceJPanel4AAProperties;
import module.multiseq.alignment.view.gui.HighlightMonomorphicSitesSequenceJPanel;
import module.multiseq.alignment.view.gui.HighlightPolymorphlicSitesSequenceJPanel;
import module.multiseq.alignment.view.gui.NoColorSequenceJPanel;
import module.multiseq.alignment.view.gui.PercentageIdentitySequenceJPanel;
import module.multiseq.alignment.view.model.AlignmentDrawProperties;

public class AlignmentColorSchemeLeftPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7497228842851798536L;

	private AlignmentViewMain aVMian;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	private JRadioButton noColor;
	private JRadioButton highlightPolymorphicSitesColor;
	private JRadioButton highlightAllSitesColor;
	private JRadioButton highlightMonomorphicSitesColor;
	private JRadioButton percentageIdentityColor;
	private JRadioButton highlightAllSitesColor4AAProterties;

	public AlignmentColorSchemeLeftPanel(AlignmentViewMain alignmentViewMain) {
		this.aVMian = alignmentViewMain;
		setLayout(new BorderLayout());
		setBackground(new Color(240, 240, 240));
		Box initBox = Box.createVerticalBox();
		initLayout(initBox);

		add(initBox, BorderLayout.WEST);

	}

	private void initLayout(Box baseBox) {
		JPanel layoutBox = new JPanel(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		JRadioButton noColor = getNoColor();

		JRadioButton highlightPolymorphicSitesColor = getHighlightPolymorphicSitesColor();

		JRadioButton highlightAllSitesColor = getHighlightAllSitesColor();

		JRadioButton highlightMonomorphicSitesColor = getHighlightMonomorphicSitesColor();

		JRadioButton percentageIdentityColor = getPercentageIdentityColor();

		JRadioButton highlightAllSitesColor4AAProperties = getHighlightAllSitesColor4AAProperties();

		noColor.setSelected(true);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(noColor);
		buttonGroup.add(highlightPolymorphicSitesColor);
		buttonGroup.add(highlightAllSitesColor);
		buttonGroup.add(highlightMonomorphicSitesColor);
		buttonGroup.add(percentageIdentityColor);
		buttonGroup.add(highlightAllSitesColor4AAProperties);

		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		layoutBox.add(noColor, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		layoutBox.add(highlightPolymorphicSitesColor, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		layoutBox.add(highlightAllSitesColor, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		layoutBox.add(highlightMonomorphicSitesColor, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		layoutBox.add(percentageIdentityColor, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		layoutBox.add(highlightAllSitesColor4AAProperties, gridBagConstraints);

		baseBox.add(layoutBox);

	}

	public JRadioButton getNoColor() {
		if (noColor == null) {
			noColor = new JRadioButton("No color");
			noColor.setFont(defaultFont);
			noColor.setFocusPainted(false);
			noColor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					JPanel tabbedPanel = aVMian.getRightTabbedPanel();
					if (tabbedPanel.getComponentCount() > 0) {

						AlignmentDrawProperties aDrawProperties = aVMian.getAlignmentDrawProperties();

						aDrawProperties.setSequenceBackgroundColor(new NoColorSequenceJPanel());

						aVMian.repaint();
					}
				}
			});
		}
		return noColor;
	}

	private JRadioButton getHighlightPolymorphicSitesColor() {
		if (highlightPolymorphicSitesColor == null) {

			highlightPolymorphicSitesColor = new JRadioButton("Highlight polymorphic sites");
			highlightPolymorphicSitesColor.setFont(defaultFont);
			highlightPolymorphicSitesColor.setFocusPainted(false);
			highlightPolymorphicSitesColor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JPanel tabbedPanel = aVMian.getRightTabbedPanel();
					if (tabbedPanel.getComponentCount() > 0) {

						AlignmentDrawProperties aDrawProperties = aVMian.getAlignmentDrawProperties();

						aDrawProperties.setSequenceBackgroundColor(new HighlightPolymorphlicSitesSequenceJPanel());

						aVMian.repaint();
						
						aVMian.invokeTheFeature(1);
					}
				}
			});
		}
		return highlightPolymorphicSitesColor;
	}

	private JRadioButton getHighlightAllSitesColor() {
		if (highlightAllSitesColor == null) {

			highlightAllSitesColor = new JRadioButton("Highlight all sites");
			highlightAllSitesColor.setFont(defaultFont);
			highlightAllSitesColor.setFocusPainted(false);
			highlightAllSitesColor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JPanel tabbedPanel = aVMian.getRightTabbedPanel();
					if (tabbedPanel.getComponentCount() > 0) {

						AlignmentDrawProperties aDrawProperties = aVMian.getAlignmentDrawProperties();

						aDrawProperties.setSequenceBackgroundColor(new HighlightAllSitesSequenceJPanel());

						aVMian.repaint();
						aVMian.invokeTheFeature(1);
					}
				}
			});
		}
		return highlightAllSitesColor;
	}

	private JRadioButton getHighlightAllSitesColor4AAProperties() {
		if (highlightAllSitesColor4AAProterties == null) {

			highlightAllSitesColor4AAProterties = new JRadioButton("Highlight according to amino acid properties");
			highlightAllSitesColor4AAProterties.setToolTipText(
					"Negatively charged is green, positively charged is blue, neutral is gray, and non-polar is white.");
			highlightAllSitesColor4AAProterties.setFont(defaultFont);
			highlightAllSitesColor4AAProterties.setFocusPainted(false);
			highlightAllSitesColor4AAProterties.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JPanel tabbedPanel = aVMian.getRightTabbedPanel();
					if (tabbedPanel.getComponentCount() > 0) {

						AlignmentDrawProperties aDrawProperties = aVMian.getAlignmentDrawProperties();

						aDrawProperties.setSequenceBackgroundColor(new HighlightAllSitesSequenceJPanel4AAProperties());

						aVMian.repaint();
						aVMian.invokeTheFeature(1);
					}
				}
			});
		}
		return highlightAllSitesColor4AAProterties;
	}

	private JRadioButton getHighlightMonomorphicSitesColor() {
		if (highlightMonomorphicSitesColor == null) {

			highlightMonomorphicSitesColor = new JRadioButton("Highlight monomorphic sites");
			highlightMonomorphicSitesColor.setFont(defaultFont);
			highlightMonomorphicSitesColor.setFocusPainted(false);
			highlightMonomorphicSitesColor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JPanel tabbedPanel = aVMian.getRightTabbedPanel();
					if (tabbedPanel.getComponentCount() > 0) {
						AlignmentDrawProperties aDrawProperties = aVMian.getAlignmentDrawProperties();

						aDrawProperties.setSequenceBackgroundColor(new HighlightMonomorphicSitesSequenceJPanel());
						aVMian.repaint();
						aVMian.invokeTheFeature(1);
					}
				}
			});
		}
		return highlightMonomorphicSitesColor;
	}

	private JRadioButton getPercentageIdentityColor() {
		if (percentageIdentityColor == null) {

			percentageIdentityColor = new JRadioButton("By percentage identity");
			percentageIdentityColor.setFont(defaultFont);
			percentageIdentityColor.setFocusPainted(false);
			percentageIdentityColor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JPanel tabbedPanel = aVMian.getRightTabbedPanel();
					if (tabbedPanel.getComponentCount() > 0) {

						AlignmentDrawProperties aDrawProperties = aVMian.getAlignmentDrawProperties();

						aDrawProperties.setSequenceBackgroundColor(new PercentageIdentitySequenceJPanel());

						aVMian.repaint();
						aVMian.invokeTheFeature(1);
					}
				}
			});
		}
		return percentageIdentityColor;
	}
}
