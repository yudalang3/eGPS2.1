package module.evolview.gfamily.work.gui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import graphic.engine.guibean.ColorIcon;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.annotation.CommonShape;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNodeInsituAnno;
import module.evolview.model.tree.AnnotationsProperties;
import module.evolview.model.tree.GraphicsNode;

@SuppressWarnings("serial")
public class DialogInternalNodeInsituAnnotation extends AbstarctShowAnnotationDialog {

	private JPanel parameterPanel;
	private JComboBox<String> shst;
	private JComboBox<String> sizeCombo;
	private JButton shapeColorButton;
	private JTextField annoTextField;
	private JButton annoTextColbtn;
	private JComboBox<String> fontComboBox;
	private JComboBox<String> mrcaFontstyle;
	private JComboBox<String> annoTextFontSize;
	private NodeAnnotationDialogContainer nodeAnnotationDialog;
	
	private final String rectangleString = "Rectangle";
	private final String circleString = "Circle";
	private final String triangleString = "Triangle";


	public DialogInternalNodeInsituAnnotation(NodeAnnotationDialogContainer nodeAnnotationDialog, String nodeName,
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
			
			ActionListener refreshActionListener = e ->{
				configurateAnnotationProperty2preview();
				nodeAnnotationDialog.updatePhylogeneticTreePanel();
			};
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
			JLabel shape = new JLabel("Shape style:");
			shape.setFont(defaultFont);
			parameterPanel.add(shape, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			shst = new JComboBox<String>();
			shst.setPreferredSize(new Dimension(120, 25));
			shst.addItem(rectangleString);
			shst.addItem(circleString);
			shst.addItem(triangleString);
			shst.addActionListener(refreshActionListener);
			shst.setFont(defaultFont);
			parameterPanel.add(shst, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			JLabel shsize = new JLabel("Shape size:");
			shsize.setFont(defaultFont);
			parameterPanel.add(shsize, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			sizeCombo = new JComboBox<String>();
			sizeCombo.setPreferredSize(new Dimension(120, 25));
			for (int i = 5; i < 30; i++) {
				sizeCombo.addItem(i+"");
			}
			sizeCombo.setSelectedItem("20");
			sizeCombo.addActionListener(refreshActionListener);
			sizeCombo.setFont(defaultFont);
			parameterPanel.add(sizeCombo, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			JLabel shcolor = new JLabel("Shape color:");
			shcolor.setFont(defaultFont);
			parameterPanel.add(shcolor, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			shapeColorButton = new JButton();
			
			shapeColorButton.setIcon(new ColorIcon(new Color(255, 0, 0, 100)));
			shapeColorButton.setPreferredSize(new Dimension(35, 25));
			shapeColorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ColorIcon icon = (ColorIcon) shapeColorButton.getIcon();
					Color newColor = JColorChooser.showDialog(nodeAnnotationDialog, "Choose Color", icon.getColor());
					if (newColor != null) {
						icon.setColor(newColor);
						refreshActionListener.actionPerformed(null);
					}
				}
			});
			parameterPanel.add(shapeColorButton, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			JLabel annoText = new JLabel("Anno text:");
			annoText.setFont(defaultFont);
			parameterPanel.add(annoText, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			annoTextField = new JTextField();
			annoTextField.setFont(defaultFont);
			annoTextField.setPreferredSize(new Dimension(120, 25));
			annoTextField.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					refreshActionListener.actionPerformed(null);
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					refreshActionListener.actionPerformed(null);
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {}
			});
			parameterPanel.add(annoTextField, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 4;
			JLabel annoTextColor = new JLabel("Text color:");
			annoTextColor.setFont(defaultFont);
			parameterPanel.add(annoTextColor, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 4;
			annoTextColbtn = new JButton();
			
			annoTextColbtn.setIcon(new ColorIcon(Color.GREEN));
			
			annoTextColbtn.setPreferredSize(new Dimension(35, 25));
			annoTextColbtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					ColorIcon icon = (ColorIcon) annoTextColbtn.getIcon();
					Color newColor = JColorChooser.showDialog(nodeAnnotationDialog, "Choose Color", icon.getColor());
					if (newColor != null) {
						icon.setColor(newColor);
						refreshActionListener.actionPerformed(null);
					}
				}
			});
			parameterPanel.add(annoTextColbtn, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			JLabel annoTextfont = new JLabel("Text font:");
			annoTextfont.setFont(defaultFont);
			parameterPanel.add(annoTextfont, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 5;
			String[] fontlist = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			fontComboBox = new JComboBox<String>(fontlist);
			fontComboBox.setFont(defaultFont);
			fontComboBox.setPreferredSize(new Dimension(120, 25));
			fontComboBox.addActionListener(refreshActionListener);
			parameterPanel.add(fontComboBox, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 6;
			JLabel mrcasylt = new JLabel("Font style:");
			mrcasylt.setFont(defaultFont);
			parameterPanel.add(mrcasylt, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 6;
			mrcaFontstyle = new JComboBox<String>();
			mrcaFontstyle.setPreferredSize(new Dimension(120, 25));
			mrcaFontstyle.addItem("Plain");
			mrcaFontstyle.addItem("Bold");
			mrcaFontstyle.addItem("Italic");
			mrcaFontstyle.addActionListener(refreshActionListener);
			mrcaFontstyle.setFont(defaultFont);
			parameterPanel.add(mrcaFontstyle, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 7;
			JLabel annoTextfontSize = new JLabel("Font size:");
			annoTextfontSize.setFont(defaultFont);
			parameterPanel.add(annoTextfontSize, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 7;
			annoTextFontSize = new JComboBox<String>();
			annoTextFontSize.setPreferredSize(new Dimension(120, 25));
			annoTextFontSize.addItem("10");
			annoTextFontSize.addItem("11");
			annoTextFontSize.addItem("12");
			annoTextFontSize.addItem("13");
			annoTextFontSize.addItem("14");
			annoTextFontSize.addItem("15");
			annoTextFontSize.addItem("16");
			annoTextFontSize.addItem("17");
			annoTextFontSize.addItem("18");
			annoTextFontSize.addItem("19");
			annoTextFontSize.addItem("20");
			annoTextFontSize.addActionListener(refreshActionListener);
			annoTextFontSize.setFont(defaultFont);
			parameterPanel.add(annoTextFontSize, gridBagConstraints);
		}
		return parameterPanel;
	}

	private DrawPropInternalNodeInsituAnno getDrawProperty(GraphicsNode graphicsNode) {
		Font textFont = null;
		if (mrcaFontstyle.getSelectedItem().toString().equals("Plain")) {
			textFont = new Font((String) fontComboBox.getSelectedItem(), Font.PLAIN,
					new Integer((String) annoTextFontSize.getSelectedItem()));
		} else if (mrcaFontstyle.getSelectedItem().toString().equals("Bold")) {
			textFont = new Font((String) fontComboBox.getSelectedItem(), Font.BOLD,
					new Integer((String) annoTextFontSize.getSelectedItem()));
		} else {
			textFont = new Font((String) fontComboBox.getSelectedItem(), Font.ITALIC,
					new Integer((String) annoTextFontSize.getSelectedItem()));
		}
		
		Color textColor = ((ColorIcon)annoTextColbtn.getIcon() ).getColor();
		String annoTextString = annoTextField.getText().trim();
		
		Color shapeColor = ((ColorIcon)shapeColorButton.getIcon() ).getColor();
		
		String shapeName = shst.getSelectedItem().toString();
		CommonShape shape = null;
		if (shapeName.equalsIgnoreCase(rectangleString)) {
			shape = CommonShape.RECTANGE;
		}else if (shapeName.equalsIgnoreCase(circleString)) {
			shape = CommonShape.ELLIPSE;
		}else {
			shape = CommonShape.TRIANGLE;
		}
		int shapeSize = Integer.parseInt(sizeCombo.getSelectedItem().toString());

		DrawPropInternalNodeInsituAnno drawPropInternalNodeInsituAnno = new DrawPropInternalNodeInsituAnno(graphicsNode);
		drawPropInternalNodeInsituAnno.setShapParameter(shapeSize, shape, shapeColor);
		drawPropInternalNodeInsituAnno.setFontParameter(textFont, annoTextString, textColor);
		return drawPropInternalNodeInsituAnno;
	}

	@Override
	public void configurateAnnotationProperty2preview() {
		AnnotationsProperties currentAnnotationsProperties = nodeAnnotationDialog.getCurrentAnnotationsProperties();
		currentAnnotationsProperties.clearAllAnnotation();
		
		DrawPropInternalNodeInsituAnno drawPropInternalNodeInsituAnno = getDrawProperty(nodeAnnotationDialog.getAVirtureRoot());
		currentAnnotationsProperties.getInternalNode2LeafInsituAnnos().add(drawPropInternalNodeInsituAnno);
		
	}

}
