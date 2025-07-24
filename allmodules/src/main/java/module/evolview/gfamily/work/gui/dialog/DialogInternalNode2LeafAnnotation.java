package module.evolview.gfamily.work.gui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import graphic.engine.guibean.ColorIcon;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNode2LeafAnno;
import module.evolview.gfamily.work.model.tree.AnnotationsProperties;
import module.evolview.gfamily.work.model.tree.GraphicsNode;

@SuppressWarnings("serial")
public class DialogInternalNode2LeafAnnotation extends AbstarctShowAnnotationDialog {

	private JPanel parameterPanel;
	private JButton colorChangeButton;
	private NodeAnnotationDialogContainer nodeAnnotationDialog;


	public DialogInternalNode2LeafAnnotation(NodeAnnotationDialogContainer nodeAnnotationDialog, String nodeName,
			PhylogeneticTreePanel mainPhylogeneticTreePanel) {
		super(mainPhylogeneticTreePanel);
		super.userObject = nodeName;
		this.nodeAnnotationDialog = nodeAnnotationDialog;
	}

	@Override
	public JComponent getViewJPanel() {
		return createPeripheryAnnotationJPanel();
	}

	private JPanel createPeripheryAnnotationJPanel() {
		if (parameterPanel == null) {
			parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			TitledBorder titleBorder = new TitledBorder(null, "Parameters", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null);
			
			Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
			titleBorder.setTitleFont(defaultTitleFont);
			parameterPanel.setBorder(titleBorder);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 0.5;

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			JLabel shcolor = new JLabel("Shape color:");
			shcolor.setFont(defaultFont);
			parameterPanel.add(shcolor, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			colorChangeButton = new JButton();
			Color bg = new Color(204, 232, 207, 100);
			
			colorChangeButton.setIcon(new ColorIcon(bg));

			colorChangeButton.setPreferredSize(new Dimension(35, 25));
			colorChangeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					ColorIcon icon = (ColorIcon) colorChangeButton.getIcon();
					Color newColor = JColorChooser.showDialog(nodeAnnotationDialog, "Choose Color",
							icon.getColor());
					if (newColor != null) {
						icon.setColor(newColor);
						configurateAnnotationProperty2preview();
						nodeAnnotationDialog.updatePhylogeneticTreePanel();
					}
				}
			});
			parameterPanel.add(colorChangeButton, gridBagConstraints);
		}

		return parameterPanel;

	}


	@Override
	public void configurateAnnotationProperty2preview() {
		AnnotationsProperties currentAnnotationsProperties = nodeAnnotationDialog.getCurrentAnnotationsProperties();
		currentAnnotationsProperties.clearAllAnnotation();

		GraphicsNode curreNode = nodeAnnotationDialog.getAVirtureRoot();
		
		Color lineColor = ((ColorIcon) colorChangeButton.getIcon() ).getColor();

		DrawPropInternalNode2LeafAnno drawPropInternalNode2LeafAnno = new DrawPropInternalNode2LeafAnno(lineColor,
				curreNode);

		currentAnnotationsProperties.getInternalNode2LeafAnnos().add(drawPropInternalNode2LeafAnno);
	}

}
