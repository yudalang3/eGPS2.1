package module.heatmap.eheatmap.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import egps2.UnifiedAccessPoint;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.distance.AvgDotProduct;
import module.heatmap.eheatmap.model.distance.EuclideanDist;
import module.heatmap.eheatmap.model.distance.ManhattanDist;
import module.heatmap.eheatmap.model.distance.PearsonsCor;
import module.heatmap.eheatmap.model.distance.SpearmansCor;
import module.heatmap.eheatmap.tree.ClusterParameter;
import egps2.panels.reusablecom.ParameterInitialized;

public class LeftHierarchicalClusteringPanel0319 extends JPanel implements ParameterInitialized {
private static final long serialVersionUID = 3039454555907503423L;
	
	private final EheatmapMain eheatmapMain;
	private HeatmapController controller;

	private JButton jButtonColor;

	private JComboBox<String> jComboBoxElement;
	private JComboBox<String> jComboBoxDistance;
	private JComboBox<String> jComboBoxMethod;
	private JSpinner jSpinnerWidth;
	
    final String persCor = "Pearson correlation";
    final String eucliDist = "Euclidean distance";
    final String manhattanDist = "Manhattan distance";
    final String spearRankCor = "Spearman rank correlation";

	private ChangeListener jSpinnerWidthChangeListener;

	public LeftHierarchicalClusteringPanel0319(EheatmapMain main) {
		this.eheatmapMain = main;
		initComponents();
		
		jSpinnerWidthChangeListener = e -> {
			doClustering();
		};
		
	}

	private void initComponents() {
		
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[] {15, 15, 15, 32, 38, 15, 39, 35, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblElement = new JLabel("Element");
		lblElement.setFont(globalFont);
		GridBagConstraints gbc_lblElement = new GridBagConstraints();
		gbc_lblElement.anchor = GridBagConstraints.WEST;
		gbc_lblElement.insets = new Insets(0, 0, 5, 5);
		gbc_lblElement.gridx = 0;
		gbc_lblElement.gridy = 0;
		add(lblElement, gbc_lblElement);
		
		jComboBoxElement = new JComboBox<>();
		jComboBoxElement.setFont(globalFont);
		jComboBoxElement.addItem("No cluster");
		jComboBoxElement.addItem("Rows");
		jComboBoxElement.addItem("Columns");
		jComboBoxElement.addItem("Both");
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		add(jComboBoxElement, gbc_comboBox);
		
		JLabel lblDistance = new JLabel("Distance metric");
		lblDistance.setFont(globalFont);
		GridBagConstraints gbc_lblDistance = new GridBagConstraints();
		gbc_lblDistance.anchor = GridBagConstraints.WEST;
		gbc_lblDistance.insets = new Insets(0, 0, 5, 5);
		gbc_lblDistance.gridx = 0;
		gbc_lblDistance.gridy = 1;
		add(lblDistance, gbc_lblDistance);
		
		jComboBoxDistance = new JComboBox<>();
		jComboBoxDistance.addItem(persCor);
		jComboBoxDistance.addItem(eucliDist);
		jComboBoxDistance.addItem(manhattanDist);
		jComboBoxDistance.addItem(spearRankCor);
		//jComboBoxDistance.setSelectedIndex(1);
		jComboBoxDistance.setFont(globalFont);
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridwidth = 2;
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_1.gridx = 0;
		gbc_comboBox_1.gridy = 2;
		add(jComboBoxDistance, gbc_comboBox_1);
		
		JLabel lblNewLabel = new JLabel("Linkage method");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 3;
		add(lblNewLabel, gbc_lblNewLabel);
		
		jComboBoxMethod = new JComboBox<String>();
		jComboBoxMethod.addItem("Average");
		jComboBoxMethod.addItem("Complete");
		jComboBoxMethod.addItem("Single");
		jComboBoxMethod.setFont(globalFont);
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_2.gridwidth = 2;
		gbc_comboBox_2.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_2.gridx = 0;
		gbc_comboBox_2.gridy = 4;
		add(jComboBoxMethod, gbc_comboBox_2);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 5;
		add(separator, gbc_separator);
		
		JLabel lblLineColor = new JLabel("Line color");
		lblLineColor.setFont(globalFont);
		GridBagConstraints gbc_lblLineColor = new GridBagConstraints();
		gbc_lblLineColor.anchor = GridBagConstraints.WEST;
		gbc_lblLineColor.insets = new Insets(0, 0, 5, 5);
		gbc_lblLineColor.gridx = 0;
		gbc_lblLineColor.gridy = 6;
		add(lblLineColor, gbc_lblLineColor);
		
		jButtonColor = new JButton("Col");
		jButtonColor.setFont(globalFont);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 6;
		add(jButtonColor, gbc_btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Line width");
		lblNewLabel_1.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 7;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		jSpinnerWidth = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		jSpinnerWidth.setFont(globalFont);
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 7;
		add(jSpinnerWidth, gbc_spinner);
	}

	@Override
	public void initializeParameters() {
		
		removeListeners();
		
		ParameterModel paraModel = getController().getParaModel();
		ClusterParameter clusterPara = paraModel.getClusterPara();

		SwingUtilities.invokeLater(() -> {
			jButtonColor.setBackground(clusterPara.getColor());
			jButtonColor.setForeground(clusterPara.getColor());
		});
		
		boolean ifPaintColTree = paraModel.isIfPaintColTree();
		boolean ifPaintRowTree = paraModel.isIfPaintRowTree();
		
		if (ifPaintRowTree) {
			if (ifPaintColTree) {
				jComboBoxElement.setSelectedIndex(3);
			} else {
				jComboBoxElement.setSelectedIndex(1);
			}
		} else {
			if (ifPaintColTree) {
				jComboBoxElement.setSelectedIndex(2);
			} else {
				jComboBoxElement.setSelectedIndex(0);
			}
		}
		
		jSpinnerWidth.setValue(( int )clusterPara.getLineWidth());
		
		if (ifPaintColTree || ifPaintRowTree) {
			setAllElementEnabled(true);
		}else {
			setAllElementEnabled(false);
		}
		
		addListeners();
		
	}
	
	@Override
	public void removeListeners() {
		removeJcomboBoxActionListeners(jComboBoxElement);
		removeJcomboBoxActionListeners(jComboBoxDistance);
		removeJcomboBoxActionListeners(jComboBoxMethod);
		
		jSpinnerWidth.removeChangeListener(jSpinnerWidthChangeListener);
		
		ActionListener[] actionListeners = jButtonColor.getActionListeners();
		for (ActionListener actionListener : actionListeners) {
			jButtonColor.removeActionListener(actionListener);
		}
		
	}

	private void removeJcomboBoxActionListeners(JComboBox<String> jComboBox) {
		ActionListener[] actionListeners = jComboBox.getActionListeners();
		for (ActionListener actionListener : actionListeners) {
			jComboBox.removeActionListener(actionListener);
		}
		
	}

	@Override
	public void addListeners() {
		
		jComboBoxElement.addActionListener(e ->{
			doClustering();
		});
		jComboBoxDistance.addActionListener(e ->{
			doClustering();
		});
		jComboBoxMethod.addActionListener(e ->{
			doClustering();
		});
		
		jSpinnerWidth.addChangeListener(jSpinnerWidthChangeListener );
		
		
		
		jButtonColor.addActionListener(e ->{
			SwingUtilities.invokeLater(() ->{
	    		EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButtonColor);
	    		dialog.setCallBackEvent( () ->{
	    			doClustering();
	    		});
	    		dialog.setVisible(true);
	    	});
		});
		
	}
	
	private HeatmapController getController() {
		if (controller == null) {
			controller = (HeatmapController) eheatmapMain.getController();
		}
		return controller;
	}
	
	private void doClustering() {
		ClusterParameter clusterPara = getController().getParaModel().getClusterPara();
		
		// "No cluster", "Rows", "Columns", "Both"
		int selectedIndex = jComboBoxElement.getSelectedIndex();

		boolean ifPaintCol = false;
		boolean ifPaintRow = false;

		switch (selectedIndex) {

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
			setAllElementEnabled(false);
			ParameterModel paraModel = getController().getParaModel();
			paraModel.setIfPaintRowTree(ifPaintRow);
			paraModel.setIfPaintColTree(ifPaintCol);
			paraModel.setIfRowTreeFormExternalFile(false);
			getController().strongestRefreshHeatmapAndRecoverDim();
			return;
		}
		
		setAllElementEnabled(true);

		int lineWidth = (int) jSpinnerWidth.getValue();
		Color lineColor = jButtonColor.getBackground();
		clusterPara.setLineWidth(lineWidth);
		clusterPara.setColor(lineColor.getRed(),lineColor.getGreen(),lineColor.getBlue());
		// "pearson correlation", "euclidean distance", "manhattan distance", "average
		// dot product", "spearman rank correlation" }
		// final String dotProduct = "average dot product";

		String str = (String) jComboBoxDistance.getSelectedItem();

		switch (str) {
		case persCor:
			clusterPara.setPairwiseDistance(new PearsonsCor());
			break;
		case eucliDist:
			clusterPara.setPairwiseDistance(new EuclideanDist());
			break;
		case manhattanDist:
			clusterPara.setPairwiseDistance(new ManhattanDist());
			break;
		case spearRankCor:
			clusterPara.setPairwiseDistance(new SpearmansCor());
			break;
		default:
			clusterPara.setPairwiseDistance(new AvgDotProduct());
			break;
		}

		// final String persCor = "Average";
		final String complete = "Complete";
		final String single = "Single";
		str = (String) jComboBoxMethod.getSelectedItem();

		switch (str) {
		case complete:
			clusterPara.setClusterLinkageType(2);
			break;
		case single:
			clusterPara.setClusterLinkageType(1);
			break;
		default:
			clusterPara.setClusterLinkageType(0);
			break;
		}

		ParameterModel paraModel = getController().getParaModel();
		paraModel.setIfPaintRowTree(ifPaintRow);
		paraModel.setIfPaintColTree(ifPaintCol);
		paraModel.setIfRowTreeFormExternalFile(false);
		getController().strongestRefreshHeatmapAndRecoverDim();
		
		eheatmapMain.invokeTheFeatureMethod(6);
	}

	private void setAllElementEnabled(boolean b) {
		jComboBoxDistance.setEnabled(b);
		jComboBoxMethod.setEnabled(b);
		jSpinnerWidth.setEnabled(b);
		jButtonColor.setEnabled(b);
	}
}
