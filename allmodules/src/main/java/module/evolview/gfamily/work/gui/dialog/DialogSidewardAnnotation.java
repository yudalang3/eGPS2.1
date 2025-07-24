package module.evolview.gfamily.work.gui.dialog;

import java.awt.BasicStroke;
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
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropOutterSidewardAnno;
import module.evolview.gfamily.work.gui.tree.annotation.SidewardNodeAnnotation;
import module.evolview.gfamily.work.model.tree.AnnotationsProperties;
import module.evolview.gfamily.work.model.tree.GraphicsNode;

@SuppressWarnings("serial")
public class DialogSidewardAnnotation extends AbstarctShowAnnotationDialog {

	private JComboBox<String> lineWidthComboBox;
	private JButton lineColorButton;
	private JComboBox<String> lineStyleComboBox;
	private JTextField rdTextField;
	private JButton textColorButton;
	private JComboBox<String> rdTFsty;
	private JComboBox<String> rdTFfont;
	private JComboBox<String> rdTFFontStyle;
	private JComboBox<String> rdTFFontsize;
	private NodeAnnotationDialogContainer nodeAnnotationDialog;
	private JPanel parameterPanel;

	public DialogSidewardAnnotation(NodeAnnotationDialogContainer nodeAnnotationDialog, String nodeName,
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

			ActionListener refreshActionListener = e -> {
				configurateAnnotationProperty2preview();
				nodeAnnotationDialog.updatePhylogeneticTreePanel();
			};

			parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			TitledBorder border = new TitledBorder(null, "Parameters", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null);
			
			Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
			border.setTitleFont(defaultTitleFont);
			parameterPanel.setBorder(border);

			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 0.5;
			JLabel shape = new JLabel("Line width:");
			shape.setFont(defaultFont);
			parameterPanel.add(shape, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			lineWidthComboBox = new JComboBox<String>();
			lineWidthComboBox.setFont(defaultFont);
			lineWidthComboBox.setPreferredSize(new Dimension(120, 25));

			for (int i = 1; i < 11; i++) {
				lineWidthComboBox.addItem(String.valueOf(i));
			}
			lineWidthComboBox.setSelectedIndex(2);
			lineWidthComboBox.addActionListener(refreshActionListener);
			parameterPanel.add(lineWidthComboBox, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			JLabel shsize = new JLabel("Line color:");
			shsize.setFont(defaultFont);
			parameterPanel.add(shsize, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;

			lineColorButton = new JButton();
			lineColorButton.setIcon(new ColorIcon(Color.RED));

			lineColorButton.setPreferredSize(new Dimension(35, 25));
			lineColorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					ColorIcon icon = (ColorIcon) lineColorButton.getIcon();
					Color newColor = JColorChooser.showDialog(nodeAnnotationDialog, "Choose Color", icon.getColor());
					if (newColor != null) {
						icon.setColor(newColor);
						refreshActionListener.actionPerformed(null);
					}
				}
			});
			parameterPanel.add(lineColorButton, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			JLabel shcolor = new JLabel("Line style:");
			shcolor.setFont(defaultFont);
			parameterPanel.add(shcolor, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			lineStyleComboBox = new JComboBox<String>();
			lineStyleComboBox.setPreferredSize(new Dimension(120, 25));
			lineStyleComboBox.addItem("Round");
			lineStyleComboBox.addItem("Butt");
			lineStyleComboBox.setFont(defaultFont);
			lineStyleComboBox.addActionListener(refreshActionListener);
			parameterPanel.add(lineStyleComboBox, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			JLabel annoText = new JLabel("Anno text:");
			annoText.setFont(defaultFont);
			parameterPanel.add(annoText, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			rdTextField = new JTextField();
			rdTextField.setFont(defaultFont);
			rdTextField.setPreferredSize(new Dimension(120, 25));
			rdTextField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					refreshActionListener.actionPerformed(null);

				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					refreshActionListener.actionPerformed(null);

				}

				@Override
				public void changedUpdate(DocumentEvent e) {
				}
			});
			parameterPanel.add(rdTextField, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 4;
			JLabel annoTextColor = new JLabel("Text color:");
			annoTextColor.setFont(defaultFont);
			parameterPanel.add(annoTextColor, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 4;
			textColorButton = new JButton();

			textColorButton.setIcon(new ColorIcon(Color.BLACK));

			textColorButton.setPreferredSize(new Dimension(35, 25));
			textColorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ColorIcon icon = (ColorIcon) lineColorButton.getIcon();
					Color newColor = JColorChooser.showDialog(nodeAnnotationDialog, "Choose Color", icon.getColor());

					if (newColor != null) {
						icon.setColor(newColor);
						refreshActionListener.actionPerformed(null);
					}
				}
			});
			parameterPanel.add(textColorButton, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			JLabel annoTextfont = new JLabel("Text style:");
			annoTextfont.setFont(defaultFont);
			parameterPanel.add(annoTextfont, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 5;
			rdTFsty = new JComboBox<String>();
			rdTFsty.setFont(defaultFont);
			rdTFsty.setPreferredSize(new Dimension(120, 25));
			rdTFsty.addItem("Horizontal");
			rdTFsty.addItem("Vertical");
			rdTFsty.addActionListener(refreshActionListener);
			parameterPanel.add(rdTFsty, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 6;
			JLabel annoTextfontSize = new JLabel("Text font:");
			annoTextfontSize.setFont(defaultFont);
			parameterPanel.add(annoTextfontSize, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 6;
			String[] fontlist = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			rdTFfont = new JComboBox<String>(fontlist);
			rdTFfont.setFont(defaultFont);
			rdTFfont.addActionListener(refreshActionListener);
			rdTFfont.setPreferredSize(new Dimension(120, 25));
			parameterPanel.add(rdTFfont, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 7;
			JLabel mrcasylt = new JLabel("Font style:");
			mrcasylt.setFont(defaultFont);
			parameterPanel.add(mrcasylt, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 7;
			rdTFFontStyle = new JComboBox<String>();
			rdTFFontStyle.setPreferredSize(new Dimension(120, 25));
			rdTFFontStyle.addItem("Plain");
			rdTFFontStyle.addItem("Bold");
			rdTFFontStyle.addItem("Italic");
			rdTFFontStyle.addActionListener(refreshActionListener);
			rdTFFontStyle.setFont(defaultFont);
			parameterPanel.add(rdTFFontStyle, gridBagConstraints);

			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 8;
			JLabel rdfs = new JLabel("Font size:");
			rdfs.setFont(defaultFont);
			parameterPanel.add(rdfs, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 8;
			rdTFFontsize = new JComboBox<String>();
			rdTFFontsize.setPreferredSize(new Dimension(120, 25));
			rdTFFontsize.addItem("10");
			rdTFFontsize.addItem("11");
			rdTFFontsize.addItem("12");
			rdTFFontsize.addItem("13");
			rdTFFontsize.addItem("14");
			rdTFFontsize.addItem("15");
			rdTFFontsize.addItem("16");
			rdTFFontsize.addItem("17");
			rdTFFontsize.addItem("18");
			rdTFFontsize.addItem("19");
			rdTFFontsize.addItem("20");
			rdTFFontsize.addActionListener(refreshActionListener);
			rdTFFontsize.setFont(defaultFont);
			parameterPanel.add(rdTFFontsize, gridBagConstraints);

		}

		return parameterPanel;
	}

	private DrawPropOutterSidewardAnno getAnnotationProprty(GraphicsNode graphicsNode) {
		SidewardNodeAnnotation nodeAnno = new SidewardNodeAnnotation();
		if (lineStyleComboBox.getSelectedItem().toString().equals("Round")) {
			nodeAnno.setLineStroke(new BasicStroke(Integer.parseInt(lineWidthComboBox.getSelectedItem().toString()),
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		} else {
			nodeAnno.setLineStroke(new BasicStroke(Integer.parseInt(lineWidthComboBox.getSelectedItem().toString()),
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		}

		Color color = ((ColorIcon) lineColorButton.getIcon()).getColor();
		nodeAnno.setLineColor(color);
		nodeAnno.setTextString(rdTextField.getText().trim());
		nodeAnno.setTextColor(((ColorIcon) textColorButton.getIcon()).getColor());
		nodeAnno.setDrawTextHorizontal(rdTFsty.getSelectedItem().toString().equalsIgnoreCase("Horizontal"));

		Font textFont = null;
		if (rdTFFontStyle.getSelectedItem().toString().equals("Plain")) {
			textFont = new Font((String) rdTFfont.getSelectedItem(), Font.PLAIN,
					new Integer((String) rdTFFontsize.getSelectedItem()));
		} else if (rdTFFontStyle.getSelectedItem().toString().equals("Bold")) {
			textFont = new Font((String) rdTFfont.getSelectedItem(), Font.BOLD,
					new Integer((String) rdTFFontsize.getSelectedItem()));
		} else {
			textFont = new Font((String) rdTFfont.getSelectedItem(), Font.ITALIC,
					new Integer((String) rdTFFontsize.getSelectedItem()));
		}

		nodeAnno.setTextFont(textFont);

		DrawPropOutterSidewardAnno drawPropOutterSidewardAnno = new DrawPropOutterSidewardAnno(nodeAnno, graphicsNode);
		drawPropOutterSidewardAnno.setJust4annotationNot4lineageTypeTrue();

		return drawPropOutterSidewardAnno;
	}

	@Override
	public void configurateAnnotationProperty2preview() {
		AnnotationsProperties currentAnnotationsProperties = nodeAnnotationDialog.getCurrentAnnotationsProperties();
		currentAnnotationsProperties.clearAllAnnotation();

		DrawPropOutterSidewardAnno drawPropOutterSidewardAnno = getAnnotationProprty(
				nodeAnnotationDialog.getAVirtureRoot());
		currentAnnotationsProperties.getOutterSidewardAnnos().add(drawPropOutterSidewardAnno);
	}

}
