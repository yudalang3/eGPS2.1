/**
 * 
 */
package module.heatmap.eheatmap.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.math.matrix.GeneralMatrixOp;
import egps2.utils.common.model.datatransfer.FourTuple;
import egps2.UnifiedAccessPoint;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.tree.cluster.KmeansPlusPlus;
import egps2.panels.reusablecom.ParameterInitialized;

public class LeftKMeansClusteringPanel extends JPanel implements ParameterInitialized {

	private static final long serialVersionUID = -1291627900176804283L;

	private final EheatmapMain eheatmapMain;
	private HeatmapController controller;

	private JButton jButtonReArrangeOrders;

	private final String rowAnnotationName = "KMeans row clusters";
	private final String colAnnotationName = "KMeans col clusters";

	private JButton jButtonBoth;
	private JButton jButtonColumn;
	private JButton jButtonRow;
	private JComboBox<String> jComboBoxDistance;

	private JSpinner jSpinnerNumber;

	private JSpinner jSpinnerMaxIteration;
	private GridBagConstraints gridBagConstraints_1;
	private GridBagConstraints gridBagConstraints_2;
	private GridBagConstraints gridBagConstraints_3;
	private GridBagConstraints gridBagConstraints_4;
	private GridBagConstraints gridBagConstraints_5;
	private GridBagConstraints gridBagConstraints_6;

	public LeftKMeansClusteringPanel(EheatmapMain main) {
		setBorder(BorderFactory.createEmptyBorder());
		this.eheatmapMain = main;
		initComponents();
	}

	private void initComponents() {

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		setLayout(new GridLayout(0, 1, 0, 0));

		JPanel bottomPanel = new JPanel(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridy = 0;
		JLabel jLabelDistance = new JLabel("Distance metric");
		jLabelDistance.setFont(globalFont);
		bottomPanel.add(jLabelDistance, gridBagConstraints);

		gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.gridwidth = 2;
		gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_1.gridx = 0;
		gridBagConstraints_1.gridy = 1;
		gridBagConstraints_1.insets = new Insets(5, 5, 5, 5);

		jComboBoxDistance = new JComboBox<String>();
		jComboBoxDistance.setSize(new Dimension(10, 20));
		jComboBoxDistance.setFont(globalFont);
		jComboBoxDistance.addItem("Euclidean distance");
		jComboBoxDistance.addItem("Manhattan distance");
		jComboBoxDistance.addItem("Chebyshev distance");
		bottomPanel.add(jComboBoxDistance, gridBagConstraints_1);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		JLabel jLabelarrange = new JLabel("Re arrange order");
		jLabelarrange.setFont(globalFont);
		bottomPanel.add(jLabelarrange, gridBagConstraints);

		gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_6.insets = new Insets(5, 5, 5, 0);
		gridBagConstraints_6.anchor = GridBagConstraints.WEST;
		gridBagConstraints_6.gridx = 1;
		gridBagConstraints_6.gridy = 2;
		gridBagConstraints_6.gridx = 1;
		jButtonReArrangeOrders = new JButton("set");
		jButtonReArrangeOrders.setToolTipText("<html><body>If there are either row or column kmeans annotions here,"
				+ "<br> click this button will have effect！<br>"
				+ "Note: if there are Hierarchical clusters, that takes priority!" + "</body></html>");
		jButtonReArrangeOrders.setFont(globalFont);
		bottomPanel.add(jButtonReArrangeOrders, gridBagConstraints_6);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		JLabel jLabelNumber = new JLabel("Number of clusters");
		jLabelNumber.setFont(globalFont);
		bottomPanel.add(jLabelNumber, gridBagConstraints);

		gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_4.insets = new Insets(5, 5, 5, 0);
		gridBagConstraints_4.anchor = GridBagConstraints.WEST;
		gridBagConstraints_4.gridx = 1;
		gridBagConstraints_4.gridy = 3;
		gridBagConstraints_4.gridx = 1;
		jSpinnerNumber = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
		jSpinnerNumber.setFont(globalFont);
		bottomPanel.add(jSpinnerNumber, gridBagConstraints_4);
		this.add(bottomPanel);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		JLabel jLabelMax = new JLabel("Maximun iterations");
		jLabelMax.setFont(globalFont);
		bottomPanel.add(jLabelMax, gridBagConstraints);

		gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_5.insets = new Insets(5, 5, 5, 0);
		gridBagConstraints_5.anchor = GridBagConstraints.WEST;
		gridBagConstraints_5.gridx = 1;
		gridBagConstraints_5.gridy = 4;
		jSpinnerMaxIteration = new JSpinner(new SpinnerNumberModel(200, 50, 1000, 50));
		jSpinnerMaxIteration.setFont(globalFont);
		bottomPanel.add(jSpinnerMaxIteration, gridBagConstraints_5);

		JPanel topPanel = new JPanel(new GridBagLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder());
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.gridy = 0;

		jButtonRow = new JButton("Execute row clustering");
		jButtonRow.setFont(globalFont);
		topPanel.add(jButtonRow, gridBagConstraints6);

		gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_2.gridx = 0;
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.gridy = 1;
		jButtonColumn = new JButton("Execute column clustering");
		jButtonColumn.setFont(globalFont);
		topPanel.add(jButtonColumn, gridBagConstraints_2);

		gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_3.gridx = 0;
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.gridy = 2;
		jButtonBoth = new JButton("Execute both clustering");
		jButtonBoth.setFont(globalFont);
		topPanel.add(jButtonBoth, gridBagConstraints_3);
		this.add(topPanel);
	}

	@Override
	public void initializeParameters() {
		removeListeners();

		addListeners();
	}

	@Override
	public void removeListeners() {
		removeButtonListeners(jButtonRow);
		removeButtonListeners(jButtonColumn);
		removeButtonListeners(jButtonBoth);
		removeButtonListeners(jButtonReArrangeOrders);

	}

	private void removeButtonListeners(JButton button) {
		ActionListener[] actionListeners = button.getActionListeners();
		for (ActionListener actionListener : actionListeners) {
			button.removeActionListener(actionListener);
		}

	}

	@Override
	public void addListeners() {

		jButtonRow.addActionListener((e) -> {
			kmeansCluElementActionPerformed(1);
		});
		jButtonColumn.addActionListener((e) -> {
			kmeansCluElementActionPerformed(2);
		});
		jButtonBoth.addActionListener((e) -> {
			kmeansCluElementActionPerformed(3);
			eheatmapMain.invokeTheFeatureMethod(7);
		});

		jButtonReArrangeOrders.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				boolean hasClusterInfo = false;
				AnnotaionParaObj annotaionParaObj = getController().getParaModel().getAnnotaionParaObj();
				DataModel dataModel = getController().getDataModel();

				FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> rowAnnoParas = annotaionParaObj
						.getRowAnnoParas();
				List<String> rowAnnoNames = rowAnnoParas.first;
				for (int i = 0; i < rowAnnoNames.size(); i++) {
					if (rowAnnoNames.get(i).equalsIgnoreCase(rowAnnotationName)) {
						byte[] bs = rowAnnoParas.third.get(i);
						int[] sortReturnIndexes = sortReturnIndexes(bs);
						dataModel.setRowOrderMaping(sortReturnIndexes);
						hasClusterInfo = true;
						break;
					}

				}
				FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> colAnnoParas = annotaionParaObj
						.getColAnnoParas();
				List<String> colAnnoNames = colAnnoParas.first;
				for (int i = 0; i < colAnnoNames.size(); i++) {
					if (colAnnoNames.get(i).equalsIgnoreCase(colAnnotationName)) {
						byte[] bs = colAnnoParas.third.get(i);
						int[] sortReturnIndexes = sortReturnIndexes(bs);
						dataModel.setColOrderMaping(sortReturnIndexes);
						hasClusterInfo = true;
						break;
					}

				}

				if (hasClusterInfo) {
					getController().weakestRefreshHeatmap();
				} else {
					SwingDialog.showErrorMSGDialog("Sorroy", "There is no annotaion information!");
				}

			}
		});
	}

	/**
	 * // "No cluster", "Rows", "Columns", "Both" 0 1 2
	 * 
	 * @param index
	 */
	private void kmeansCluElementActionPerformed(int index) {

		// "pearson correlation", "euclidean distance", "manhattan distance", "average
		// dot product", "spearman rank correlation" }
		// final String dotProduct = "average dot product";
		// final String persCor = "pearson correlation";
		// final String eucliDist = "euclidean distance";
		final String manhattanDist = "manhattan distance";
		final String chebyshevDistance = "chebyshev distance";
		// final String spearRankCor = "spearman rank correlation";

		DistanceMeasure distanceMeasure = new EuclideanDistance();
		String str = (String) jComboBoxDistance.getSelectedItem();

		switch (str) {
		case chebyshevDistance:
			distanceMeasure = new ChebyshevDistance();
			break;
		case manhattanDist:
			distanceMeasure = new ManhattanDistance();
			break;
		default:
			break;
		}

		int numOfClu = (int) jSpinnerNumber.getValue();
		int maxIteration = (int) jSpinnerMaxIteration.getValue();

		boolean ifPaintCol = false;
		boolean ifPaintRow = false;

		// "No cluster", "Rows", "Columns", "Both"
		switch (index) {

		case 1:
			ifPaintRow = true;
			break;
		case 2:
			ifPaintCol = true;
			break;
		case 3:
			ifPaintRow = true;
			ifPaintCol = true;
			break;
		default:
			break;
		}

		ParameterModel paraModel = getController().getParaModel();
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();

		KmeansPlusPlus kmeansPlusPlus = new KmeansPlusPlus(numOfClu, maxIteration, distanceMeasure);
		DataModel dataModel = getController().getDataModel();

		if (ifPaintRow) {
			double[][] dataMatrix = dataModel.getDataMatrix();
			kmeansPlusPlus.setMatrix(dataMatrix);
			byte[] doClustering = kmeansPlusPlus.doClustering();

			annotaionParaObj.addARowAnnotation(rowAnnotationName, doClustering);

			if (!paraModel.isIfPaintRowAnnotation()) {
				paraModel.setIfPaintRowAnnotation(true);
			}
		}

		if (ifPaintCol) {

			double[][] dataMatrix = dataModel.getDataMatrix();
			kmeansPlusPlus.setMatrix(GeneralMatrixOp.transpose(dataMatrix));
			byte[] doClustering = kmeansPlusPlus.doClustering();
			annotaionParaObj.addAColAnnotation(colAnnotationName, doClustering);
			if (!paraModel.isIfPaintColAnnotation()) {
				paraModel.setIfPaintColAnnotation(true);
			}
		}
		if (ifPaintRow || ifPaintCol) {
			if (!paraModel.isIfPaintAnnotationLegend()) {
				paraModel.setIfPaintAnnotationLegend(true);
			}
		}
		getController().strongestRefreshHeatmapAndRecoverDim();
	}

	private HeatmapController getController() {
		if (controller == null) {
			controller = (HeatmapController) eheatmapMain.getController();
		}
		return controller;
	}

	int[] sortReturnIndexes(byte[] bs) {
		final int size = bs.length;

		final int[] result = new int[size];
		for (int i = 0; i < size; i++)
			result[i] = i;

		boolean sorted;
		do {
			sorted = true;
			int bubble = result[0];
			for (int i = 0; i < size - 1; i++) {
				if (bs[bubble] > bs[result[i + 1]]) {
					result[i] = result[i + 1];
					result[i + 1] = bubble;
					sorted = false;
				} else {
					bubble = result[i + 1];
				}
			}
		} while (!sorted);

		return result;
	}
}
