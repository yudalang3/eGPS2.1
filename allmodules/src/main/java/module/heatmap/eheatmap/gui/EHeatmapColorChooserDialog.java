package module.heatmap.eheatmap.gui;

import javax.swing.JButton;
import javax.swing.JDialog;

import egps2.panels.dialog.EGPSColorChooser;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import egps2.utils.common.model.datatransfer.CallBackBehavior;

public class EHeatmapColorChooserDialog extends JDialog{
	
	private static final long serialVersionUID = 5471651631834922433L;
	final private JButton jButton;
	private EGPSColorChooser egpsColorChooser;
	
	public EHeatmapColorChooserDialog(JButton jButton) {
		super(UnifiedAccessPoint.getInstanceFrame(), "EHeatmap color chooser", false);
		MyFrame instance = UnifiedAccessPoint.getInstanceFrame();
		this.jButton = jButton;
		
		setSize(600, 450);
		egpsColorChooser = new EGPSColorChooser(this);
		egpsColorChooser.setJbutton(jButton);
		setContentPane(egpsColorChooser);
		setLocationRelativeTo(instance);
		setIconImage(EGPSShellIcons.get("eGPS_logo16x16.png").getImage());
	}
	
	
	public static void main(String[] args) {
		// create our jframe as usual
		EHeatmapColorChooserDialog eHeatmapColorChooser = new EHeatmapColorChooserDialog(null);
		eHeatmapColorChooser.setVisible(true);
	}
	
	public void setCallBackEvent(CallBackBehavior cal) {
		egpsColorChooser.setCallBackInstance(cal);
	}

}

