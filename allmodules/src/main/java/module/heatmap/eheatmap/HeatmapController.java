package module.heatmap.eheatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import evoltree.struct.io.PrimaryNodeTreeDecoder;
import org.apache.commons.io.FileUtils;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.model.datatransfer.FourTuple;
import egps2.utils.common.util.EGPSPrintUtilities;
import egps2.utils.common.util.SaveUtil;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.model.Adjuster;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.tree.BasicInternalCoderDecoder;
import module.heatmap.eheatmap.tree.BasicLeafCoderDecoder;
import module.heatmap.eheatmap.tree.GraphcsNode;
import module.heatmap.eheatmap.util.AnnotationManipulater;

public class HeatmapController {
	private EheatmapMain view;
	private DataModel dataModel;
	private ParameterModel paraModel;
	private AbstrctEHeatmapPaintPanel jPanel;

	private Adjuster adjuster = new Adjuster();

	/**
	 * 为了给 zoom in 和zoom out，可以做这一个存储变量。因为很多时候JPanel是有了才会变的
	 */
	private double paintPanelWidth = 500;
	/**
	 * 为了给 zoom in 和zoom out，可以做这一个存储变量。因为很多时候JPanel是有了才会变的
	 */
	private double paintPanelHeight = 500;

	public HeatmapController(EheatmapMain testview) {
		this.view = testview;
	}

	public void setDataModel(DataModel testmodel) {
		this.dataModel = testmodel;
	}

	public void setParaModel(ParameterModel paraModel) {
		this.paraModel = paraModel;
	}

	public void clearParameters() {
		dataModel.setRowOrderMaping(null);
		dataModel.setColOrderMaping(null);
	}

	public void strongestRefreshHeatmapAndRecoverDim() {
		clearParameters();
		Dimension size = jPanel.getSize();
		paintPanelWidth = size.getWidth();
		paintPanelHeight = size.getHeight();
		PaintingLocationParas pars = autoConfigPainatingParas(size.getHeight(), size.getWidth());
		jPanel.setAreaLocationPar(pars);

		SwingUtilities.invokeLater(() -> {
			view.refresh();
		});

		view.reInitializeLeftAllTaskPanes();

	}

	public void strongestRefreshHeatmap(double height, double width) {
		clearParameters();// 0315_0946

		PaintingLocationParas pars = autoConfigPainatingParas(height, width);
		jPanel.setAreaLocationPar(pars);
		SwingUtilities.invokeLater(() -> {
			view.refresh();
		});
		view.reInitializeLeftAllTaskPanes();// 0315_0946
	}

	public void weakestRefreshHeatmap() {
		SwingUtilities.invokeLater(() -> {
			view.refresh();
		});
	}

	public void weakestRefreshHeatmapForInteractiveDrag(boolean colTree, boolean rowTree) {
		paraModel.setShouldCalculateColTreeLocation(colTree);
		paraModel.setShouldCalculateRowTreeLocation(rowTree);

		SwingUtilities.invokeLater(() -> {
			view.refresh();
		});
	}

	public void weakestRefreshHeatmapForGaps() {

		jPanel.reSerCellWidthAndHeightForGapDisplay();
		paraModel.setShouldCalculateColTreeLocation(true);
		paraModel.setShouldCalculateRowTreeLocation(true);

		SwingUtilities.invokeLater(() -> {
			view.refresh();
		});
		view.reInitializeLeftAllTaskPanes();
	}

	/**
	 * YDL: This process should not include in the swing running thread! This
	 * process maybe time consumed!
	 * 
	 * @param height
	 * @param width
	 * @return
	 */
	public PaintingLocationParas autoConfigPainatingParas(double height, double width) {
		paraModel.setShouldCalculateRowTreeLocation(true);
		paraModel.setShouldCalculateColTreeLocation(true);

		return adjuster.autoConfigPainatingParas(height, width, paraModel, dataModel, this);
	}

	public boolean canZoomIn() {
		return paintPanelHeight < 3000;
	}

	public boolean canZoomOut() {
		return paintPanelHeight > 400;
	}

	public void zoomIn() {
		paintPanelWidth += 30;
		paintPanelHeight += 30;

		strongestRefreshHeatmap(paintPanelHeight, paintPanelWidth);
	}

	public void zoomOut() {
		paintPanelWidth -= 30;
		paintPanelHeight -= 30;

		strongestRefreshHeatmap(paintPanelHeight, paintPanelWidth);
	}

	public void printViewPanel() {
		EGPSPrintUtilities.printComponent(jPanel);
	}

	public void saveViewPanelAs() {
		new SaveUtil().saveData(jPanel);
	}

	public JComponent getViewPanel() {
		return view;
	}

	public void setPaintJPanel(AbstrctEHeatmapPaintPanel jPanel) {
		this.jPanel = jPanel;
	}

	public boolean canSetFont() {

		if (paraModel == null) {
			return false;
		}
		return paraModel.hasSelectionElemets();
	}

	public void setModuleFont(Font newFont) {
		paraModel.setDefaultFont(newFont);
		weakestRefreshHeatmap();
	}

	public void transposeData() {
		dataModel.transposeData();
		strongestRefreshHeatmapAndRecoverDim();
	}

	public void transformData(int wayOfTransform, int p1, double e) {
		dataModel.transformData(wayOfTransform, p1, e);
		strongestRefreshHeatmapAndRecoverDim();
	}

	public ParameterModel getParaModel() {
		return paraModel;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public AbstrctEHeatmapPaintPanel getPaintJPanel() {
		return jPanel;
	}

	public void highlitOneClickElement(Point2D point) {
		// paraModel.setOneMouseHasClick(true);
		weakestRefreshHeatmap();
	}

	public void reSetAllParameters() {
		paraModel.sethGapLocations(new int[0]);
		paraModel.setvGapLocations(new int[0]);

		paraModel.getAnnotaionParaObj().clear();
		strongestRefreshHeatmapAndRecoverDim();
	}

	public void displayAnnotaions(String string) {
		AnnotationManipulater annotationGenerater = new AnnotationManipulater();
		annotationGenerater.generateColAnnotaion(string, paraModel);
		annotationGenerater.generateRowAnnotaion(string, paraModel);

		if (!paraModel.isIfPaintAnnotationLegend()) {
			paraModel.setIfPaintAnnotationLegend(true);
		}
		strongestRefreshHeatmapAndRecoverDim();
	}

	public void displayAnnotaions(String string, Color[] colors, String[] caseNames) {
		AnnotationManipulater annotationGenerater = new AnnotationManipulater();
		annotationGenerater.generateColAnnotaion(string, colors, caseNames, paraModel);
		annotationGenerater.generateRowAnnotaion(string, colors, caseNames, paraModel);

		if (!paraModel.isIfPaintAnnotationLegend()) {
			paraModel.setIfPaintAnnotationLegend(true);
		}
		strongestRefreshHeatmapAndRecoverDim();
	}

	public void replaceAllString(File inputFile) {

		Map<String, String> mapHM = new HashMap<String, String>();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				String[] split = line.split("\\s+|,");
				mapHM.put(split[0], split[1]);
			}

		} catch (IOException io) {
			// e.printStackTrace();
		}

		String[] colNames = dataModel.getColNames();
		for (int i = 0; i < colNames.length; i++) {
			String destName = mapHM.get(colNames[i]);
			if (destName != null) {
				colNames[i] = destName;
			}
		}
		String[] rowNames = dataModel.getRowNames();
		for (int i = 0; i < rowNames.length; i++) {
			String destName = mapHM.get(rowNames[i]);
			if (destName != null) {
				rowNames[i] = destName;
			}
		}
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> colAnnoParas = annotaionParaObj
				.getColAnnoParas();
		List<String> first = colAnnoParas.first;
		for (int i = 0; i < first.size(); i++) {
			String destName = mapHM.get(first.get(i));
			if (destName != null) {
				first.set(i, destName);
			}
		}
		List<String[]> fourth = colAnnoParas.fourth;
		for (String[] strings : fourth) {
			for (int i = 0; i < strings.length; i++) {
				String destName = mapHM.get(strings[i]);
				if (destName != null) {
					strings[i] = destName;
				}
			}
		}
		FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> rowAnnoParas = annotaionParaObj
				.getRowAnnoParas();
		List<String> first2 = rowAnnoParas.first;
		for (int i = 0; i < first2.size(); i++) {
			String destName = mapHM.get(first2.get(i));
			if (destName != null) {
				first2.set(i, destName);
			}
		}
		List<String[]> fourth2 = rowAnnoParas.fourth;
		for (String[] strings : fourth2) {
			for (int i = 0; i < strings.length; i++) {
				String destName = mapHM.get(strings[i]);
				if (destName != null) {
					strings[i] = destName;
				}
			}
		}
	}

	public void incorporateTreeInformationOnleft(File inputFile) {
		BasicInternalCoderDecoder<GraphcsNode> basicInternalCoderDecoder = new BasicInternalCoderDecoder<>();
		BasicLeafCoderDecoder<GraphcsNode> basicLeafCoderDecoder = new BasicLeafCoderDecoder<>();
		PrimaryNodeTreeDecoder<GraphcsNode> treeDecoderWithMap = new PrimaryNodeTreeDecoder<>(basicInternalCoderDecoder, basicLeafCoderDecoder);
		
		EvolNode node = null;
		try {
			String line = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
			node = treeDecoderWithMap.decode(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<EvolNode> leaves = EvolNodeUtil.getLeaves(node);
		
		if (leaves.size() != paraModel.getNumOfRows()) {
			SwingDialog.showErrorMSGDialog("Data error", "Your input tree don't contain right tree!");
			return;
		}

		for (EvolNode gNode : leaves) {
			GraphcsNode basicNode = (GraphcsNode) gNode;
			int originalIndexForRows = dataModel.getOriginalIndexForRows(basicNode.getName());

			if (originalIndexForRows == -1) {
				SwingDialog.showErrorMSGDialog("Data error", "Your input tree's leaf name " + basicNode.getName()
						+ " don't have corresponding row name!");
				return;
			}

			basicNode.setOrignalOrderInMatrix(originalIndexForRows);
		}

		paraModel.setRowTreeRootNode((GraphcsNode) node);
		paraModel.setIfPaintRowTree(true);
		paraModel.setIfRowTreeFormExternalFile(true);
		strongestRefreshHeatmapAndRecoverDim();

	}

	public Font getCurrentFontStatus() {

		int size = paraModel.getRowNameSelections().size();

		if (size > 0) {
			return paraModel.getRowNameFont();
		}

		return paraModel.getColNameFont();
	}

	public double getPaintPanelWidth() {
		return paintPanelWidth;
	}

	public double getPaintPanelHeight() {
		return paintPanelHeight;
	}


}
