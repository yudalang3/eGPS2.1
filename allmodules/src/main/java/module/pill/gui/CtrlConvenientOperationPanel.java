package module.pill.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import egps2.UnifiedAccessPoint;
import module.pill.core.DrawingPanelSkeletonMaker;
import module.pill.images.ImageUtils;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CtrlConvenientOperationPanel extends JPanel implements ActionListener{
	
	private DrawingPanelSkeletonMaker pickPanel;
	private JButton btnLoadingImageClip;
	private JButton btn_clearAll;
	private ItemListener eraserItemListner;
	private JToggleButton tglbtn_eraser;

	public CtrlConvenientOperationPanel(DrawingPanelSkeletonMaker drawingPanelSkeletonMaker) {
		this.pickPanel = drawingPanelSkeletonMaker;
		setLayout(new MigLayout("align center center"));
		
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		JLabel lblNewLabel = new JLabel("Import data");
		lblNewLabel.setFont(defaultFont);
		add(lblNewLabel, "cell 0 0 2 1");
		
		btnLoadingImageClip = new JButton("Load image from clipboard");
		btnLoadingImageClip.setFont(defaultFont);
		add(btnLoadingImageClip, "cell 0 1 3 1,growx");
		Icon icon  = ImageUtils.getIcon("clipboard-solid.png");
		btnLoadingImageClip.setIcon(icon);
		btnLoadingImageClip.setFocusable(false);
		btnLoadingImageClip.addActionListener(this);
		
		JLabel lblNewLabel_1 = new JLabel("Drawing parameters");
		lblNewLabel_1.setFont(defaultFont);
		add(lblNewLabel_1, "cell 0 2 3 1");
		
		icon = ImageUtils.getIcon("eraser.png");
		tglbtn_eraser = new JToggleButton("");
		tglbtn_eraser.setIcon(icon);
		eraserItemListner = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					pickPanel.setEraserState(true);
				} else {
					pickPanel.setEraserState(false);
				}
			}
		};
		tglbtn_eraser.addItemListener(eraserItemListner);
		tglbtn_eraser.setFocusable(false);
		add(tglbtn_eraser, "cell 0 3");
		
		JToggleButton tglbtn_grid = new JToggleButton("");
		icon = ImageUtils.getIcon("grid.png");
		tglbtn_grid.setIcon(icon);
		tglbtn_grid.setRequestFocusEnabled(false);
		ItemListener gridItemListner = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					pickPanel.setGridState(true);
				} else {
					pickPanel.setGridState(false);
				}
			}
		};
		tglbtn_grid.addItemListener(gridItemListner);
		add(tglbtn_grid, "cell 1 3");
		
		JToggleButton btn_imageMask = new JToggleButton("Image mask");
		btn_imageMask.setFont(defaultFont);
		btn_imageMask.setIcon(ImageUtils.getIcon("bx-mask.png"));
		btn_imageMask.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					pickPanel.setImageMaskState(true);
				} else {
					pickPanel.setImageMaskState(false);
				}
			}
		});
		add(btn_imageMask, "cell 2 3");
		
		btn_clearAll = new JButton("Clear all");
		btn_clearAll.setFont(defaultFont);
		btn_clearAll.addActionListener(this);
		btn_clearAll.setFocusable(false);
		add(btn_clearAll, "cell 0 4 3 1,growx");
		
	}
	
	public void restoreEraser2disabledState() {
		tglbtn_eraser.removeItemListener(eraserItemListner);
		tglbtn_eraser.setSelected(false);
		tglbtn_eraser.addItemListener(eraserItemListner);
		pickPanel.setEraserState(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnLoadingImageClip) {
			if (pickPanel != null) {
				SwingUtilities.invokeLater(() -> {
					pickPanel.loadImage();
				});
			}
		}else if (source == btn_clearAll) {
			pickPanel.clearAll();
		}
	}

}
