package module.vennplot.gui;

import java.awt.Color;

import javax.swing.JColorChooser;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import module.vennplot.VennPlotController;
import egps2.panels.reusablecom.InstantStatusPanel;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class MyInstantStatusPanel extends InstantStatusPanel implements ParameterInitialized {

	private VennPlotController controller;

	public MyInstantStatusPanel() {
		super(false, true);
	}

	@Override
	public void initializeParameters() {
		colorIcon.setColor(Color.gray);

	}

	@Override
	public void addListeners() {

		buttonCurrentColor.addActionListener(e -> {
			
			if (!controller.canSetColor()) {
				SwingDialog.showInfoMSGDialog("Selection error", "You need to select the element first.");
				return;
			}
			Color iniColor = colorIcon.getColor();
			MyFrame parent = UnifiedAccessPoint.getInstanceFrame();
			Color showDialog = JColorChooser.showDialog(parent, "Choose new color", iniColor);
			if (showDialog != null) {
				colorIcon.setColor(showDialog);
				controller.setColor(showDialog);
			}

		});
	}

	@Override
	public void removeListeners() {

	}

	public void setController(VennPlotController controller) {
		this.controller = controller;
	}

	public void refreshInstantStatus() {
		if (controller.canSetColor()) {
			buttonCurrentColor.setEnabled(true);
			Color currentColorStatus = controller.getCurrentColorStatus();
			colorIcon.setColor(currentColorStatus);
			
			buttonCurrentColor.repaint();
		}else {
			setNoSelection();
		}
		
	}

}
