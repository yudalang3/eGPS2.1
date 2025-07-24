package module.remnant.datapanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import egps2.frame.MainFrameProperties;
import egps2.modulei.IModuleLoader;


public class RightMethodButton implements ActionListener{
	JButton button;
	
	IModuleLoader iToModule;
	
	public RightMethodButton(JButton bb,IModuleLoader ii) {
		this.button = bb;
		this.iToModule = ii;
		button.addActionListener(this);
	}
//	public RightMethodButton(JButton bb,AbstractVCFMethod ii, DefaultPanel defaultPanel) {
//		this.button = bb;
//		this.iToModule = ii;
//		button.addActionListener(  e ->{
//			ii.setDataPanel(defaultPanel);
//			iToModule.turnToModule();
//		});
//	}
//	public RightMethodButton(JButton bb,MethodsForMAF2TreeViewer ii, DefaultPanel defaultPanel) {
//		this.button = bb;
//		this.iToModule = ii;
//		button.addActionListener(  e ->{
//			ii.setDataPanel(defaultPanel);
//			iToModule.turnToModule();
//		});
//	}
//	
//	public RightMethodButton(JButton bb,MethodsForMAF2Distance ii, DefaultPanel defaultPanel) {
//		this.button = bb;
//		this.iToModule = ii;
//		button.addActionListener(  e ->{
//			ii.setDataPanel(defaultPanel);
//			iToModule.turnToModule();
//		});
//	}
	
	
	public void setEnable(boolean b){
		if (button != null) {
			button.setEnabled(b);
		}
	}
	
	public IModuleLoader getITurnToModuleCommon() {
		return iToModule;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		MainFrameProperties.loadTheModuleFromIModuleLoader(iToModule);
	}
}
