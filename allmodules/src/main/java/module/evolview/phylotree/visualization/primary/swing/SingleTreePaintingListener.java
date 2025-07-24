package module.evolview.phylotree.visualization.primary.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.*;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.util.SaveUtil;
import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;
import evoltree.struct.EvolNode;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.txtdisplay.ReflectGraphicNode;

public class SingleTreePaintingListener extends MouseAdapter {

	private final SingleTreePaintingPanel<? extends EvolNode> paintingPanel;
	ReflectGraphicNode<? extends EvolNode> selectedNode;
	private JPopupMenu popup;
	private final Line2D.Double repeatLine = new Line2D.Double();

	private boolean whetherHeightScaleOnMouseWheel = true;
	private boolean whetherWidthScaleOnMouseWheel = false;
	private JCheckBoxMenuItem heightZoom;
	private JCheckBoxMenuItem widthZoom;
	private Point origin;

	private int zoomLevel = 50;

	private Consumer<ReflectGraphicNode<? extends EvolNode>> clickCallBackAction;

	public SingleTreePaintingListener(SingleTreePaintingPanel<? extends EvolNode> paintingPanel) {
		super();
		this.paintingPanel = paintingPanel;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			getJPopupMenu().show(e.getComponent(), e.getX(), e.getY());
		}

		ReflectGraphicNode<? extends EvolNode> root = paintingPanel.getRoot();
		Optional<ReflectGraphicNode> evolNode = EvolNodeUtil.searchNodeWithReturn( root, node -> ifPointLocatedInNode(e.getPoint(), node));
		if (evolNode.isPresent()){
			selectedNode = (ReflectGraphicNode<? extends EvolNode>) evolNode.get();
			System.out.println(selectedNode);
			System.out.println("------------------------------------------------------------");
			if (clickCallBackAction != null){
				new Thread(()->{
					clickCallBackAction.accept(selectedNode);
				}).start();

			}
		}
	}

	private boolean ifPointLocatedInNode(Point point, ReflectGraphicNode node) {
		double xSelf = node.getXSelf();
		double ySelf = node.getYSelf();
		double distanceSq = point.distanceSq(xSelf, ySelf);

		if (distanceSq < 10 * 10) {
			return true;
		}

		double xParent = node.getXParent();
		double yParent = node.getYParent();

		repeatLine.setLine(xSelf, ySelf, xParent, yParent);
		double distance = repeatLine.ptSegDist(point);

		if (distance <= 1.5) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		origin = new Point(e.getPoint());
		if (e.isPopupTrigger()) {
			getJPopupMenu().show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int scrollAmount = -1 * Math.abs(zoomLevel) * e.getUnitsToScroll();

		int heightScrollAmout = whetherHeightScaleOnMouseWheel ? scrollAmount : 0;
		int widthScrollAmout = whetherWidthScaleOnMouseWheel ? scrollAmount : 0;
		paintingPanel.continuouslyZoomInOrOut(heightScrollAmout, widthScrollAmout, e.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		moveTheDrawingArea(e);
	}

	protected JPopupMenu getJPopupMenu() {
		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();
		Font defaultTitleFont = launchProperty.getDefaultTitleFont();
		Font defaultFont = launchProperty.getDefaultFont();

		if (popup == null) {
			popup = new JPopupMenu();
			JMenuItem refreshMenu = new JMenuItem("Refresh");
			refreshMenu.setFont(defaultFont);
			refreshMenu.addActionListener(e -> {
				paintingPanel.repaint();
			});

			JMenuItem fitFrameRefreshMenu = new JMenuItem("Fit Frame Refresh");
			fitFrameRefreshMenu.setFont(defaultFont);
			fitFrameRefreshMenu.addActionListener(e -> {
				paintingPanel.fitFrameRefresh();
			});
			popup.add(fitFrameRefreshMenu);
			popup.add(refreshMenu);

			heightZoom = new JCheckBoxMenuItem("Height zoom");
			heightZoom.setFont(defaultFont);
			widthZoom = new JCheckBoxMenuItem("Width zoom");
			widthZoom.setFont(defaultFont);

			// 为每个复选框添加 ActionListener
			heightZoom.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					whetherHeightScaleOnMouseWheel = heightZoom.isSelected();
				}
			});

			widthZoom.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					whetherWidthScaleOnMouseWheel = widthZoom.isSelected();
				}
			});

			popup.addSeparator();
			popup.add(heightZoom);
			popup.add(widthZoom);
			{
				JMenuItem menuItem = new JMenuItem("Zoom");
				menuItem.setFont(defaultFont);
				menuItem.addActionListener(e -> {
					/// //////////////////////////////////////////
					// 创建SpinnerNumberModel：初始值0，最小值-10000，最大值10000，步长5
					SpinnerNumberModel model = new SpinnerNumberModel(zoomLevel, -10000, 10000, 5);
					JSpinner spinner = new JSpinner(model);

					// 设置JSpinner只能输入整数
					JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#");
					spinner.setEditor(editor);

					// 创建对话框显示JSpinner
					int result = JOptionPane.showConfirmDialog(
							paintingPanel.getFrame(),
							new Object[]{"Set value to zoom:", spinner},
							"Input zoom value",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE
					);

					if (result == JOptionPane.OK_OPTION) {
						int zoomValue = (Integer) spinner.getValue();
						zoomLevel = zoomValue;
						int scrollAmount = 10 * zoomLevel;

						int heightScrollAmout = whetherHeightScaleOnMouseWheel ? scrollAmount : 0;
						int widthScrollAmout = whetherWidthScaleOnMouseWheel ? scrollAmount : 0;
						paintingPanel.continuouslyZoomInOrOut(heightScrollAmout, widthScrollAmout, origin);
					}
				});
				popup.add(menuItem);
			}

			popup.addSeparator();
			JMenuItem exportFigureMenu = new JMenuItem("Export figure");
			exportFigureMenu.setFont(defaultFont);
			exportFigureMenu.addActionListener(e -> {
				new SaveUtil().saveData(paintingPanel);
			});
			popup.add(exportFigureMenu);

		}


		heightZoom.setSelected(whetherHeightScaleOnMouseWheel);
		widthZoom.setSelected(whetherWidthScaleOnMouseWheel);

		return popup;
	}

	private void moveTheDrawingArea(MouseEvent arg0) {
		if (origin == null){
			return;
		}
		JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, paintingPanel);
		if (viewPort == null) {
			SwingDialog.showErrorMSGDialog("Internal error", "The tree panel should be add in the JScrollPanel.");
			return;
		}

		int deltaX = origin.x - arg0.getX();
		int deltaY = origin.y - arg0.getY();

		Rectangle viewArea = viewPort.getViewRect();
		viewArea.x += deltaX;
		viewArea.y += deltaY;

		paintingPanel.scrollRectToVisible(viewArea);

		UnifiedAccessPoint.getInstanceFrame().showTipsOnBottomStatusBar("Press Ctrl key with drag to select nodes.");
	}

	public void setClickCallBackAction(Consumer<ReflectGraphicNode<? extends EvolNode>> clickCallBackAction) {
		this.clickCallBackAction = clickCallBackAction;
	}
}
