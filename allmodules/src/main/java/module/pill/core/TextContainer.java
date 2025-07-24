package module.pill.core;

import egps2.frame.gui.EGPSSwingUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextContainer extends JDialog {

	private static final long serialVersionUID = 2954281531610720446L;

	private JTextArea jTextArea = null;

	private int height = 0;

	private java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");

	public TextContainer(Frame owner, String title, boolean modal) {
		super(owner, title, modal);

		jTextArea = new JTextArea();

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(jTextArea), BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("Copy to clipboard");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EGPSSwingUtil.copyToClipboard(jTextArea.getText());
				TextContainer.this.dispose();
			}
		});
		btnNewButton_1.requestFocusInWindow();
		panel.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Close");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TextContainer.this.dispose();
			}
		});
		panel.add(btnNewButton);
	}



	public void showText(DrawingPanelSkeletonMaker pickPanel) {
		int width = pickPanel.getWidth();
		int height = pickPanel.getHeight();

		StringBuilder sBuilder = new StringBuilder();
		GraphicProperties graphicProperties = pickPanel.getGraphicProperties();

		this.height = height;

		sBuilder.append("width <- ").append(String.valueOf(width)).append(";\n");
		sBuilder.append("height <- ").append(String.valueOf(height)).append(";\n");

		generate_pFreeArrow_codes(sBuilder, graphicProperties, true);
		generate_pNode_codes(sBuilder, graphicProperties, true);

		sBuilder.append("##############################\n## call function here:\n##############################\n");
		sBuilder.append("pathIllustrator <-");

		boolean hasContent = false;
		if (generate_pMembrane_codes(sBuilder, graphicProperties)) {
			hasContent = true;
		}
		if (generate_pNucleus_codes(sBuilder, graphicProperties)) {
			hasContent = true;
		}
		if (generate_pFreeArrow_codes(sBuilder, graphicProperties, false)) {
			hasContent = true;
		}
		if (generate_pNode_codes(sBuilder, graphicProperties, false)) {
			hasContent = true;
		}

		if (hasContent) {
			int length = CONSTANTS.END_PATHWAY_COMP_STRING.length();
			int allLen = sBuilder.length();
			sBuilder.delete(allLen - length, allLen);
			sBuilder.append(
					"\npathIllustrator + \n    pScaleCoord(oriWidth = width, oriHeight = height, destWidth = 5, destHeight = ");
			double destW = 5d * height / width;
			
			sBuilder.append(df.format(destW));
			sBuilder.append(")");
			jTextArea.setText(sBuilder.toString());
		} else {
			jTextArea.setText("\n\n    You do not have draw any pathway content, please draw something.");
		}
	}

	private boolean generate_pFreeArrow_codes(StringBuilder sBuilder, GraphicProperties graphicProperties,
			boolean dataFrame) {
		List<Double> pfreeArrows = graphicProperties.pfreeArrows;
		if (pfreeArrows.isEmpty()) {
			return false;
		}

		if (dataFrame) {
			sBuilder.append("\n## curvature = 0 for direct line, 1 and -1 for the rectangular style.\n");
			sBuilder.append("## arrowLen is the length of the end point triangle size.\n");
			sBuilder.append("df_freeIndicator <- rbind(\n");

			Iterator<Double> iterator = pfreeArrows.iterator();
			while (iterator.hasNext()) {
				Double double1 = iterator.next();
				// c(6.2,1.45,6.6,1.45,0,0.1);
				sBuilder.append("    c(");
				sBuilder.append(double1.x1).append(",").append(this.height - double1.y1).append(",");
				sBuilder.append(double1.x2).append(",").append(this.height - double1.y2);
				sBuilder.append(",").append("0 ");
				sBuilder.append(",").append("0.1");

				if (iterator.hasNext()) {
					sBuilder.append("),\n");
				} else {
					sBuilder.append(")\n");
				}
			}
			sBuilder.append(")\n");
			sBuilder.append("colnames(df_freeIndicator) <- c('x1','y1','x2','y2','curvature','arrowLen')\n");
		} else {
			sBuilder.append("  edges(free_indicator_df = df_freeIndicator)");
			sBuilder.append(CONSTANTS.END_PATHWAY_COMP_STRING);
		}

		return true;
	}

	private boolean generate_pNode_codes(StringBuilder sBuilder, GraphicProperties graphicProperties,
			boolean dataFrame) {
		List<Ellipse2D.Double> pnodeOval = graphicProperties.pnodesOval;
		List<java.awt.geom.RoundRectangle2D.Double> pnodeRect = graphicProperties.pnodesRect;

		List<RectangularShape> list = new ArrayList<>();
		list.addAll(pnodeOval);
		list.addAll(pnodeRect);

		if (list.isEmpty()) {
			return false;
		}

		if (dataFrame) {
			sBuilder.append("df_node <- rbind(\n");
			String shapeName;
			int index = 1;
			int size = list.size();
			for (RectangularShape shape : list) {
				if (shape instanceof Ellipse2D.Double) {
					shapeName = CONSTANTS.SHAPE_NAME_ELLIPSE;
				} else {
					shapeName = CONSTANTS.SHAPE_NAME_ROUND_RECT;
				}
				int x = (int) shape.getX();
				// The height is special
				int y = this.height - (int) shape.getY() - (int) shape.getHeight();
				int w = (int) shape.getWidth();
				int h = (int) shape.getHeight();

				// list('Lipoxygenases',5,4.1,0.4,0.6,0.2,c(0.5, 1.1), 'roundRect'),

				sBuilder.append("    list('").append(index).append("', ");
				sBuilder.append("x = ").append(x).append(", y = ").append(y);
				sBuilder.append(", w = ").append(w).append(", h = ").append(h);
				sBuilder.append(", ratio = 0");
				sBuilder.append(", tp=c(0.5, 0.5), '");

				if (size == index) {
					sBuilder.append(shapeName).append("')\n");
				} else {
					sBuilder.append(shapeName).append("'),\n");
				}

				index++;
			}

			sBuilder.append(")\n");
			sBuilder.append(
					"colnames(df_node) <- c(\"label\",\"x\",\"y\",\"width\",\"height\",\"inner_extension_ratio\",\"text_position\", \"shape\")\n");
		} else {
			sBuilder.append("  nodes(data = df_node)");
			sBuilder.append(CONSTANTS.END_PATHWAY_COMP_STRING);
		}

		return true;
	}

	/**
	 * 
	 * @param sBuilder
	 * @param graphicProperties
	 * @return true: has string generated; false: do nothing.
	 */
	private boolean generate_pMembrane_codes(StringBuilder sBuilder, GraphicProperties graphicProperties) {
		List<Ellipse2D.Double> pmembraneOval = graphicProperties.pmembraneOval;
		List<java.awt.geom.RoundRectangle2D.Double> pmembraneRect = graphicProperties.pmembraneRect;

		List<RectangularShape> list = new ArrayList<>();
		list.addAll(pmembraneOval);
		list.addAll(pmembraneRect);

		if (list.isEmpty()) {
			return false;
		}

		String shapeName;

		Iterator<RectangularShape> iterator = list.iterator();
		while (iterator.hasNext()) {
			RectangularShape shape = iterator.next();
			if (shape instanceof Ellipse2D.Double) {
				shapeName = CONSTANTS.SHAPE_NAME_ELLIPSE;
			} else {
				shapeName = CONSTANTS.SHAPE_NAME_ROUND_RECT;
			}
			int x = (int) shape.getX();
			// The height is special
			int y = this.height - (int) shape.getY() - (int) shape.getHeight();
			int w = (int) shape.getWidth();
			int h = (int) shape.getHeight();
			sBuilder.append("  membrane(shape = '").append(shapeName).append("', ");
			sBuilder.append("x = ").append(x).append(", y = ").append(y);
			sBuilder.append(", w = ").append(w).append(", h = ").append(h).append(")");

			sBuilder.append(CONSTANTS.END_PATHWAY_COMP_STRING);

		}

		return true;
	}

	private boolean generate_pNucleus_codes(StringBuilder sBuilder, GraphicProperties graphicProperties) {
		List<Ellipse2D.Double> pmembraneOval = graphicProperties.pnucleusOval;
		List<java.awt.geom.RoundRectangle2D.Double> pmembraneRect = graphicProperties.pnucleusRect;

		List<RectangularShape> list = new ArrayList<>();
		list.addAll(pmembraneOval);
		list.addAll(pmembraneRect);

		List<Double> pnucleusDNA = graphicProperties.pnucleusDNA;
		if (list.isEmpty() && pnucleusDNA.isEmpty()) {
			return false;
		}

		String shapeName;
		for (RectangularShape shape : list) {
			if (shape instanceof Ellipse2D.Double) {
				shapeName = CONSTANTS.SHAPE_NAME_ELLIPSE;
			} else {
				shapeName = CONSTANTS.SHAPE_NAME_ROUND_RECT;
			}
			int x = (int) shape.getX();
			// The height is special
			int y = this.height - (int) shape.getY() - (int) shape.getHeight();
			int w = (int) shape.getWidth();
			int h = (int) shape.getHeight();
			sBuilder.append("  nuclearEnvelop(shape = '").append(shapeName).append("', ");
			sBuilder.append("x = ").append(x).append(", y = ").append(y);
			sBuilder.append(", w = ").append(w).append(", h = ").append(h).append(")");
			sBuilder.append(CONSTANTS.END_PATHWAY_COMP_STRING);
		}

		for (Double double1 : pnucleusDNA) {
			int x = (int) double1.getX1();
			// The height is special
			int y = this.height - (int) double1.getY1();
			sBuilder.append("  nuclearDNA(x = ").append(x).append(", ");
			sBuilder.append("y = ").append(y).append(",numOfRepeats = 1)");
			sBuilder.append(CONSTANTS.END_PATHWAY_COMP_STRING);
		}

		return true;
	}
	
	public JTextArea getjTextArea() {
		return jTextArea;
	}
}
