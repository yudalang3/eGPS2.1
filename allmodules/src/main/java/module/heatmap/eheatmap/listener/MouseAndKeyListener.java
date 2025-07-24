package module.heatmap.eheatmap.listener;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import egps2.panels.dialog.SwingDialog;
import egps2.frame.gui.interacive.RectAdjustMent;
import egps2.utils.common.model.filefilter.FileFilterTxt;
import egps2.UnifiedAccessPoint;
import evoltree.struct.EvolNode;
import module.heatmap.eheatmap.AbstrctEHeatmapPaintPanel;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.gui.EheatmapAddToExistedAnnoDialog;
import module.heatmap.eheatmap.gui.EheatmapGenerateAnnoDialog;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.AnnotationSelection;
import module.heatmap.eheatmap.model.selection.AnnotationSelectionJudger;
import module.heatmap.eheatmap.model.selection.CellSelection;
import module.heatmap.eheatmap.model.selection.CellSelectionJudger;
import module.heatmap.eheatmap.model.selection.ColNameSelectionJudger;
import module.heatmap.eheatmap.model.selection.NameSelection;
import module.heatmap.eheatmap.model.selection.RowNameSelectionJudger;
import module.heatmap.eheatmap.model.selection.TreeNodeSelectionJudger;
import module.heatmap.eheatmap.tree.GraphcsNode;
import module.heatmap.images.HeatmapImageObtainer;

public class MouseAndKeyListener extends MouseAdapter implements KeyListener {

	private final HeatmapController heamapCont;
	private Double dragStart;
	private final AbstrctEHeatmapPaintPanel paintPanel;

	private CellSelectionJudger cellSelectionJudger = new CellSelectionJudger();
	private RowNameSelectionJudger rowNameSelectionJudger = new RowNameSelectionJudger();
	private ColNameSelectionJudger colNameSelectionJudger = new ColNameSelectionJudger();
	private TreeNodeSelectionJudger treeSelectionJudger = new TreeNodeSelectionJudger();
	private AnnotationSelectionJudger annotationSelectionJudger = new AnnotationSelectionJudger();

	private JPopupMenu aPopMenu;
	private JMenuItem setGapItem;
	private JMenuItem addNewAnnotationItem;
	private JMenuItem clearRowAnnotationItem;
	private JMenuItem clearColAnnotationItem;
	private JMenuItem clearRowGapItem;
	private JMenuItem clearColGapItem;
	private JMenuItem addToExistedAnno;

	private boolean ifInteractiveDragAvailable = false;
	private RectAdjustMent selectedRectAdjustMent;
	private JMenuItem outputClusterInfor;

	public MouseAndKeyListener(HeatmapController heamapCont, AbstrctEHeatmapPaintPanel paintPanel) {
		this.heamapCont = heamapCont;
		this.paintPanel = paintPanel;


		initMenuItems();
		
	}

	@SuppressWarnings("deprecation")
	private void initMenuItems() {
		setGapItem = new JMenuItem("Add gap");
		setGapItem.setToolTipText("Add gaps around the selected areas!");
		setGapItem.setIcon(HeatmapImageObtainer.get("split.png"));
		setGapItem.addActionListener((evt) -> {

			List<CellSelection> cellSelections = heamapCont.getParaModel().getCellSelections();
			CellSelection first = cellSelections.get(0);
			CellSelection last = cellSelections.get(cellSelections.size() - 1);

			int hhFirst = first.getJj();
			int hhLast = last.getJj() + 1;
			int vvFirst = first.getIi();
			int vvLast = last.getIi() + 1;

			execgapOperation(hhFirst, hhLast, vvFirst, vvLast);
			aPopMenu.show(false);

			ParameterModel paraModel = heamapCont.getParaModel();
			paraModel.setHasMouseDragEvent(false);
		});

		addNewAnnotationItem = new JMenuItem("New annotaion");
		addNewAnnotationItem.setIcon(HeatmapImageObtainer.get("Annotation.png"));
		addNewAnnotationItem.setToolTipText("Add new annotaions for the heatmap!");
		addNewAnnotationItem.addActionListener((evt) -> {
			String ss;
			List<NameSelection> rowNameSelections = heamapCont.getParaModel().getRowNameSelections();
			if (rowNameSelections.isEmpty()) {
				ss = "column";
			} else {
				ss = "row";
			}

			SwingUtilities.invokeLater(() -> {
				EheatmapGenerateAnnoDialog tt = new EheatmapGenerateAnnoDialog(heamapCont, ss);
				tt.setVisible(true);

				aPopMenu.show(false);
			});

			ParameterModel paraModel = heamapCont.getParaModel();
			paraModel.setHasMouseDragEvent(false);
		});

		clearRowAnnotationItem = new JMenuItem("Clear row annotaion");
		clearRowAnnotationItem.addActionListener((evt) -> {

			ParameterModel paraModel = heamapCont.getParaModel();
			AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
			annotaionParaObj.clearRowAnnotations();

			if (annotaionParaObj.ifRowAnnotationEmpty() && annotaionParaObj.ifColAnnotationEmpty()) {
				paraModel.setIfPaintAnnotationLegend(false);
			}
			aPopMenu.show(false);
			heamapCont.strongestRefreshHeatmapAndRecoverDim();
		});

		clearColAnnotationItem = new JMenuItem("Clear col annotaion");
		clearColAnnotationItem.addActionListener((evt) -> {
			ParameterModel paraModel = heamapCont.getParaModel();
			AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
			annotaionParaObj.clearColAnnotations();

			if (annotaionParaObj.ifRowAnnotationEmpty() && annotaionParaObj.ifColAnnotationEmpty()) {
				paraModel.setIfPaintAnnotationLegend(false);
			}
			aPopMenu.show(false);
			heamapCont.strongestRefreshHeatmapAndRecoverDim();
		});

		clearRowGapItem = new JMenuItem("Clear row gaps");
		clearRowGapItem.addActionListener((evt) -> {
			heamapCont.getParaModel().setvGapLocations(new int[0]);
			aPopMenu.show(false);
			heamapCont.strongestRefreshHeatmapAndRecoverDim();
		});

		clearColGapItem = new JMenuItem("Clear col gaps");
		clearColGapItem.addActionListener((evt) -> {
			heamapCont.getParaModel().sethGapLocations(new int[0]);
			aPopMenu.show(false);
			heamapCont.strongestRefreshHeatmapAndRecoverDim();
		});

		addToExistedAnno = new JMenuItem("Add to existed annotations");
		addToExistedAnno.addActionListener((evt) -> {
			String ss;
			List<NameSelection> rowNameSelections = heamapCont.getParaModel().getRowNameSelections();
			if (rowNameSelections.isEmpty()) {
				ss = "column";
			} else {
				ss = "row";
			}

			SwingUtilities.invokeLater(() -> {
				EheatmapAddToExistedAnnoDialog tt = new EheatmapAddToExistedAnnoDialog(heamapCont, ss);
				tt.setVisible(true);

				aPopMenu.show(false);
			});

			ParameterModel paraModel = heamapCont.getParaModel();
			paraModel.setHasMouseDragEvent(false);
		});
		outputClusterInfor = new JMenuItem("Output cluster infor.");
		outputClusterInfor.addActionListener((evt) -> {

			ParameterModel paraModel = heamapCont.getParaModel();
			if (paraModel.getSelectedGraphcsNode() == null) {
				return;
			}

			saveClusterInfo(paraModel.getSelectedGraphcsNode().getGraphcsNode());
			paraModel.setHasMouseDragEvent(false);
		});

	}
	
	public void saveClusterInfo(GraphcsNode graphcsNode) {

		Preferences pref = Preferences.userNodeForPackage(this.getClass());
		String lastPath = pref.get("lastPath", "");

		SwingUtilities.invokeLater(() -> {
			// swing thread
			JFileChooser jfc = null;
			if (lastPath.length() > 0) {
				jfc = new JFileChooser(lastPath);
			} else {
				jfc = new JFileChooser();
			}

			jfc.setDialogTitle("Save the results as ... ");

			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);

			jfc.addChoosableFileFilter(new FileFilterTxt());

			if (jfc.showSaveDialog(UnifiedAccessPoint.getInstanceFrame()) == JFileChooser.APPROVE_OPTION) {
				String ext = "txt";
				File selectedF = jfc.getSelectedFile();

				if (selectedF.exists()) {
					int res = JOptionPane.showConfirmDialog(UnifiedAccessPoint.getInstanceFrame(),
							"File exists, confirm to overlap?", "Warning", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (res != JOptionPane.OK_OPTION) {
						return;
					}
				} else {
					selectedF = new File(jfc.getSelectedFile().getPath() + "." + ext);
				}

				try {
					// Construct BufferedReader from FileReader
					BufferedWriter br = new BufferedWriter(new FileWriter(selectedF));

					iterateToWriteClustetInfor(graphcsNode, br);
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				jfc.setCurrentDirectory(selectedF.getParentFile());
				pref.put("lastPath", selectedF.getParent());

				SwingDialog.showInfoMSGDialog("Information", "Output successfully !");
			}
			// swing thread
		});

	}
	
	private void iterateToWriteClustetInfor(EvolNode graphcsNode, BufferedWriter br) throws IOException {
		int childCount = graphcsNode.getChildCount();

		if (childCount == 0) {
			br.write(graphcsNode.getName());
			br.newLine();
		} else {
			for (int i = 0; i < childCount; i++) {
				EvolNode childAt = graphcsNode.getChildAt(i);
				iterateToWriteClustetInfor(childAt, br);
			}
		}

	}

	private void execgapOperation(int hhFirst, int hhLast, int vvFirst, int vvLast) {
		PaintingLocationParas locationParas = paintPanel.getLocationParas();

		int[] hh = null;
		if (hhFirst == 0) {
			if (hhLast < locationParas.getNumOfCols()) {
				hh = new int[] { hhLast };
			} else {
				hh = new int[] {};
			}
		} else {
			if (hhLast < locationParas.getNumOfCols()) {
				hh = new int[] { hhFirst, hhLast };
			} else {
				hh = new int[] { hhFirst };
			}
		}

		int[] vv = null;
		if (vvFirst == 0) {
			if (vvLast < locationParas.getNumOfRows()) {
				vv = new int[] { vvLast };
			} else {
				vv = new int[] {};
			}
		} else {
			if (vvLast < locationParas.getNumOfRows()) {
				vv = new int[] { vvFirst, vvLast };
			} else {
				vv = new int[] { vvFirst };
			}
		}

		int[] hGapLocations = heamapCont.getParaModel().gethGapLocations();
		int[] vGapLocations = heamapCont.getParaModel().getvGapLocations();

		heamapCont.getParaModel().sethGapLocations(mergeArrays(hGapLocations, hh));
		heamapCont.getParaModel().setvGapLocations(mergeArrays(vGapLocations, vv));
		// heamapCont.strongestRefreshHeatmapAndRecoverDim();
		heamapCont.weakestRefreshHeatmapForGaps();

	}

	public int[] mergeArrays(int[] arr1, int[] arr2) {
		int i = 0, j = 0, k = 0;
		int n1 = arr1.length;
		int n2 = arr2.length;
		int len = n1 + n2;
		int[] arr3 = new int[len];
		// Traverse both array
		while (i < n1 && j < n2) {
			// Check if current element of first
			// array is smaller than current element
			// of second array. If yes, store first
			// array element and increment first array
			// index. Otherwise do same with second array
			if (arr1[i] < arr2[j]) {
				arr3[k++] = arr1[i++];
			} else if (arr1[i] == arr2[j]) {
				// Don't do anything!
				if (i + 1 == n1) {
					j++;
				} else {
					i++;
				}
			} else {
				arr3[k++] = arr2[j++];
			}
		}

		// Store remaining elements of first array
		while (i < n1) {
			arr3[k++] = arr1[i++];
		}

		// Store remaining elements of second array
		while (j < n2) {
			arr3[k++] = arr2[j++];
		}

		int numOfRemovedValues = 0;
		for (int l = len - 1; l > 0; l--) {
			if (arr3[l] == 0) {
				numOfRemovedValues++;
			} else {
				break;
			}
		}

		int[] res = new int[len - numOfRemovedValues];
		System.arraycopy(arr3, 0, res, 0, res.length);
		return res;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D point = e.getPoint();
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() == 3) {

			} else if (e.getClickCount() == 2) {

			} else {
				eventsForLeftClick(point);
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {

			boolean empty = heamapCont.getParaModel().getCellSelections().isEmpty();
			if (empty) {
				// No preview selections
				rightClickedEventsForNoDragedRectObj(point);
			} else {
				if (whetherNewSelectionLocatedInExistedObjs(point)) {

				} else {
					
				}
			}
		}
		heamapCont.weakestRefreshHeatmap();

	}

	private boolean whetherNewSelectionLocatedInExistedObjs(Point2D point) {
		ParameterModel paraModel = heamapCont.getParaModel();
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		boolean ifColAnnotationEmpty = annotaionParaObj.ifColAnnotationEmpty();
		boolean ifRowAnnotationEmpty = annotaionParaObj.ifRowAnnotationEmpty();

		if (cellSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(), paraModel, point)) {
			List<CellSelection> cellSelections = cellSelectionJudger.getCellSelections();
			if (cellSelections.isEmpty()) {
				return false;
			}
			CellSelection cellSelection = cellSelections.get(0);
			List<CellSelection> cellSelectionsPre = paraModel.getCellSelections();
			if (cellSelectionsPre.contains(cellSelection)) {
				aPopMenu = new JPopupMenu();
				aPopMenu.add(setGapItem);
				aPopMenu.show(paintPanel, (int) point.getX(), (int) point.getY());
				return true;
			}
		} else if (rowNameSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {

			List<NameSelection> rowNameSelections = rowNameSelectionJudger.getRowNameSelections();
			if (rowNameSelections.isEmpty()) {
				return false;
			}
			NameSelection nameSelection = rowNameSelections.get(0);
			if (paraModel.getRowNameSelections().contains(nameSelection)) {
				aPopMenu = new JPopupMenu();
				aPopMenu.add(addNewAnnotationItem);
				if (!ifRowAnnotationEmpty) {
					aPopMenu.add(addToExistedAnno);
					aPopMenu.addSeparator();
				}
				aPopMenu.show(paintPanel, (int) point.getX(), (int) point.getY());
				return true;
			}

		} else if (colNameSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {
			List<NameSelection> colNameSelections = colNameSelectionJudger.getColNameSelections();

			if (colNameSelections.isEmpty()) {
				return false;
			}
			NameSelection nameSelection = colNameSelections.get(0);
			if (paraModel.getColNameSelections().contains(nameSelection)) {
				aPopMenu = new JPopupMenu();
				aPopMenu.add(addNewAnnotationItem);
				if (!ifColAnnotationEmpty) {
					aPopMenu.add(addToExistedAnno);
					aPopMenu.addSeparator();
				}
				aPopMenu.show(paintPanel, (int) point.getX(), (int) point.getY());
				return true;
			}

		}

		// YDL: tree selection and annotaion selection are useless

		return false;
	}

	private void rightClickedEventsForNoDragedRectObj(Point2D point) {
		clearForClickEvent();
		aPopMenu = new JPopupMenu();
		ParameterModel paraModel = heamapCont.getParaModel();

		if (cellSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(), paraModel, point)) {
			paraModel.setCellOneMouseHasClick(true);
			paraModel.getCellSelections().addAll(cellSelectionJudger.getCellSelections());

		} else if (rowNameSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {
			paraModel.setNameOneMouseHasClick(true);
			paraModel.getRowNameSelections().addAll(rowNameSelectionJudger.getRowNameSelections());
			aPopMenu.addSeparator();
			aPopMenu.add(addNewAnnotationItem);
		} else if (colNameSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {
			paraModel.setNameOneMouseHasClick(true);
			paraModel.getColNameSelections().addAll(colNameSelectionJudger.getColNameSelections());
			aPopMenu.addSeparator();
			aPopMenu.add(addNewAnnotationItem);
		} else if (treeSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(), paraModel,
				point)) {
			paraModel.setSelectedGraphcsNode(treeSelectionJudger.getGraphcsNode(),
					treeSelectionJudger.getIfRowSelected(), heamapCont.getDataModel());
			paraModel.setTreeOneMouseHasClick(true);
			paraModel.setNameOneMouseHasClick(true);

			aPopMenu.addSeparator();
			aPopMenu.add(addNewAnnotationItem);
			aPopMenu.addSeparator();
			aPopMenu.add(outputClusterInfor);

		} else if (annotationSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {
			List<AnnotationSelection> annotationSelections = annotationSelectionJudger.getAnnotationSelections();
			paraModel.setAnnotationSelections(annotationSelections);
		}

		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		if (!annotaionParaObj.ifColAnnotationEmpty()) {
			aPopMenu.addSeparator();
			aPopMenu.add(clearColAnnotationItem);
		}

		if (!annotaionParaObj.ifRowAnnotationEmpty()) {
			aPopMenu.addSeparator();
			aPopMenu.add(clearRowAnnotationItem);
		}
		int[] getvGapLocations = paraModel.getvGapLocations();
		if (getvGapLocations.length > 0) {
			aPopMenu.addSeparator();
			aPopMenu.add(clearRowGapItem);
		}

		int[] gethGapLocations = paraModel.gethGapLocations();
		if (gethGapLocations.length > 0) {
			aPopMenu.addSeparator();
			aPopMenu.add(clearColGapItem);
		}

		JMenuItem refreshMenuItem = new JMenuItem("Refresh");
		refreshMenuItem.addActionListener((evt) -> {
			heamapCont.strongestRefreshHeatmapAndRecoverDim();
		});
		aPopMenu.add(refreshMenuItem);
		
		if (aPopMenu.getComponentCount() > 0) {
			aPopMenu.addSeparator();
			aPopMenu.show(paintPanel, (int) point.getX(), (int) point.getY());
		}

	}

	private void eventsForLeftClick(Point2D point) {
		ParameterModel paraModel = heamapCont.getParaModel();
		clearForClickEvent();
//		我们把单选的去掉
		if (cellSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(), paraModel, point)) {
			paraModel.setCellOneMouseHasClick(true);
			paraModel.getCellSelections().addAll(cellSelectionJudger.getCellSelections());
		}else if (rowNameSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {
			paraModel.setNameOneMouseHasClick(true);
			paraModel.getRowNameSelections().addAll(rowNameSelectionJudger.getRowNameSelections());
		} else if (colNameSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {
			paraModel.setNameOneMouseHasClick(true);
			paraModel.getColNameSelections().addAll(colNameSelectionJudger.getColNameSelections());
		} else if (treeSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(), paraModel,
				point)) {
			paraModel.setSelectedGraphcsNode(treeSelectionJudger.getGraphcsNode(),
					treeSelectionJudger.getIfRowSelected(), heamapCont.getDataModel());
			paraModel.setTreeOneMouseHasClick(true);
			paraModel.setNameOneMouseHasClick(true);

		} else if (annotationSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, point)) {
			List<AnnotationSelection> annotationSelections = annotationSelectionJudger.getAnnotationSelections();
			paraModel.setAnnotationSelections(annotationSelections);
		}
	}

	private void clearForClickEvent() {
		ParameterModel paraModel = heamapCont.getParaModel();

		paraModel.getCellSelections().clear();
		paraModel.getColNameSelections().clear();
		paraModel.getRowNameSelections().clear();
		paraModel.getAnnotationSelections().clear();
		paraModel.clearSelectedGraphcsNode();

		paraModel.setCellOneMouseHasClick(false);
		paraModel.setNameOneMouseHasClick(false);
		paraModel.setNameOneMouseHasClick(false);
		paraModel.setTreeOneMouseHasClick(false);
	}

	@Override
	public void mousePressed(MouseEvent e) {

		paintPanel.requestFocus();

		Point point = e.getPoint();
		dragStart = new Double(point.getX(), point.getY());

		ifInteractiveDragAvailable = ifInteractiveDrag(point.getX(), point.getY());

		if (!ifInteractiveDragAvailable) {
			heamapCont.getParaModel().setCellOneMouseHasClick(false);
		}
		// System.err.println("mousePressed");
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (ifInteractiveDragAvailable || dragStart == null) {
			return;
		}

		Point point = e.getPoint();
		double x1 = Math.min(dragStart.getX(), point.getX());
		double y1 = Math.min(dragStart.getY(), point.getY());
		double x2 = Math.max(dragStart.getX(), point.getX());
		double y2 = Math.max(dragStart.getY(), point.getY());

		double w = x2 - x1;
		double h = y2 - y1;
		Rectangle2D.Double dragedRect = new Rectangle2D.Double(x1, y1, w, h);

		if (w < 0.05) {
			return;
		}
		//dragedRectObj = new RectObj(x1, y1, w, h);

		ParameterModel paraModel = heamapCont.getParaModel();
		clearForClickEvent();

		if (cellSelectionJudger.getAndJudgeSelectionForRegion(paintPanel.getLocationParas(), paraModel, dragedRect)) {
			paraModel.setHasMouseDragEvent(true);
			paraModel.getCellSelections().addAll(cellSelectionJudger.getCellSelections());
		} else if (rowNameSelectionJudger.getAndJudgeSelectionForRegion(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, dragedRect)) {
			paraModel.setHasMouseDragEvent(true);
			paraModel.getRowNameSelections().addAll(rowNameSelectionJudger.getRowNameSelections());
		} else if (colNameSelectionJudger.getAndJudgeSelectionForRegion(paintPanel.getLocationParas(),
				heamapCont.getDataModel(), paraModel, dragedRect)) {
			paraModel.setHasMouseDragEvent(true);
			paraModel.getColNameSelections().addAll(colNameSelectionJudger.getColNameSelections());
		} else if (treeSelectionJudger.getAndJudgeSelectionForRegion(paintPanel.getLocationParas(), paraModel,
				dragedRect)) {
			paraModel.setSelectedGraphcsNode(treeSelectionJudger.getGraphcsNode(),
					treeSelectionJudger.getIfRowSelected(), heamapCont.getDataModel());
			paraModel.setTreeOneMouseHasClick(true);
			paraModel.setNameOneMouseHasClick(true);
		}

		// YDL: I think there are no meaning to consider annotation multiple selection.
//		} else if (annotationSelectionJudger.getAndJudgeSelectionForOneClick(paintPanel.getLocationParas(),
//				heamapCont.getDataModel(), paraModel, point)) {
//			List<AnnotationSelection> annotationSelections = annotationSelectionJudger.getAnnotationSelections();
//			paraModel.setAnnotationSelections(annotationSelections);
//		}

		paintPanel.setDragRect(null);
		heamapCont.weakestRefreshHeatmap();
		// System.err.println("mouseReleased");
		dragStart = null;

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point point = e.getPoint();
		// ensure the dragStart be assigned !
		if (dragStart == null) {
			return;
		}

		if (ifInteractiveDragAvailable) {
			selectedRectAdjustMent.adjustPaintings(point.getX(), point.getY());
		} else {
			clearForClickEvent();

			double x1 = Math.min(dragStart.getX(), point.getX());
			double y1 = Math.min(dragStart.getY(), point.getY());
			double x2 = Math.max(dragStart.getX(), point.getX());
			double y2 = Math.max(dragStart.getY(), point.getY());

			paintPanel.setDragRect(new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1));
			heamapCont.weakestRefreshHeatmap();
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// System.err.println("mouseMoved");
		Point point = e.getPoint();
		ifInteractiveDrag(point.getX(), point.getY());
		
	}

	/**
	 * 如果鼠标所在的位置有相应的矩形，那么返回true，就是说可以控制！
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean ifInteractiveDrag(double x, double y) {
		PaintingLocationParas locationParas = paintPanel.getLocationParas();
		RectAdjustMent[] veriticalRects = locationParas.getVeriticalRects();
		RectAdjustMent[] horizontalRects = locationParas.getHorizontalRects();
		//RectAdjustMent[] legendAdjustRects = locationParas.getLegendAdjustRects();

		for (RectAdjustMent rectAdjustMent : veriticalRects) {
			boolean contains = rectAdjustMent.contains(x, y);
			if (contains) {
				paintPanel.setCursor(new Cursor(rectAdjustMent.getCursorType()));
				selectedRectAdjustMent = rectAdjustMent;
				return true;
			}
		}

		for (RectAdjustMent rectAdjustMent : horizontalRects) {
			boolean contains = rectAdjustMent.contains(x, y);
			if (contains) {
				paintPanel.setCursor(new Cursor(rectAdjustMent.getCursorType()));
				selectedRectAdjustMent = rectAdjustMent;
				return true;
			}
		}
		

		paintPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		selectedRectAdjustMent = null;
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		ParameterModel paraModel = heamapCont.getParaModel();
		List<AnnotationSelection> annotationSelections = paraModel.getAnnotationSelections();

		boolean notEmpty = !annotationSelections.isEmpty();
		// System.out.println(getClass()+"\treleased!\t"+e.getKeyChar()+"\t"+notEmpty);
		if (e.getKeyCode() == KeyEvent.VK_DELETE && notEmpty) {

			// System.out.println(getClass()+"\tReleased!");
			AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
			annotaionParaObj.clearAnnotations(annotationSelections);
			annotationSelections.clear();
			heamapCont.strongestRefreshHeatmapAndRecoverDim();
		}
	}

}
