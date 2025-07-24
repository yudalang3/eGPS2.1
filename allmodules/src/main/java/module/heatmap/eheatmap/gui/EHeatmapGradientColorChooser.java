package module.heatmap.eheatmap.gui;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JFrame;

import egps2.utils.common.model.datatransfer.CallBackBehavior;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import module.heatmap.eheatmap.HeatmapController;

@SuppressWarnings("serial")
public class EHeatmapGradientColorChooser extends JDialog {

	private EHeatmapGradientColorChooser(HeatmapController heamapCont, float[] dist, Color[] colors) {
		super(UnifiedAccessPoint.getInstanceFrame(), "EHeatmap color chooser", false);
		JFrame instance = UnifiedAccessPoint.getInstanceFrame();

		setSize(450, 700);
		GradientColorChooserWorkPanel contentPane = new GradientColorChooserWorkPanel(this, heamapCont, dist, colors);

		contentPane.setCallBackInstance(null);
		setContentPane(contentPane);
		setLocationRelativeTo(instance);
		setIconImage(EGPSShellIcons.get("eGPS_logo16x16.png").getImage());
	}

	private EHeatmapGradientColorChooser(HeatmapController heamapCont, float[] dist, Color[] colors,
			CallBackBehavior callBack) {
		super(UnifiedAccessPoint.getInstanceFrame(), "EHeatmap color chooser", false);
		JFrame instance = UnifiedAccessPoint.getInstanceFrame();

		setSize(450, 700);
		GradientColorChooserWorkPanel contentPane = new GradientColorChooserWorkPanel(this, heamapCont, dist, colors);

		contentPane.setCallBackInstance(callBack);
		setContentPane(contentPane);
		setLocationRelativeTo(instance);
		setIconImage(EGPSShellIcons.get("eGPS_logo16x16.png").getImage());
	}

	public static EHeatmapGradientColorChooser createTheDialog(HeatmapController heamapCont, float[] dist,
			Color[] colors) {
		EHeatmapGradientColorChooser instance = new EHeatmapGradientColorChooser(heamapCont, dist, colors);

		return instance;
	}

	public static EHeatmapGradientColorChooser createTheDialog(HeatmapController heamapCont, float[] dist,
			Color[] colors, CallBackBehavior callBack) {
		EHeatmapGradientColorChooser instance = new EHeatmapGradientColorChooser(heamapCont, dist, colors, callBack);

		return instance;
	}

}
