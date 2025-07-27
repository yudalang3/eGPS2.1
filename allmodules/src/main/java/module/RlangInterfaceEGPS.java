package module;

import java.io.File;

import javax.swing.SwingUtilities;

import egps2.EGPSProperties;
import egps2.Launcher;
import egps2.Launcher4Dev;
import egps2.panels.dialog.SwingDialog;
import egps2.frame.MainFrameProperties;
import egps2.frame.ModuleFace;
import module.correlation4wnt.DataModel;
import module.correlation4wnt.GuiMain;
import module.correlation4wnt.PaintingPanel;
import module.correlation4wnt.ModuleLoader;
import module.evolview.moderntreeviewer.IndependentModuleLoader;

/**
 * 这是一个沟通R的JAVA类
 * 
 * @author yudal
 *
 */
public class RlangInterfaceEGPS {

	/**
	 * 启动
	 * 
	 * @return
	 * @throws Exception
	 */
	public String launch() throws Exception {
		Launcher4Dev.main(new String[]{});
		Launcher.isLaunchFromR = true;
		return "Hello this is eGPS desktop, version: ".concat(EGPSProperties.EGPS_VERSION);
	}

	/**
	 * 用来进行测试
	 * 
	 * @param jsonStr
	 * @return
	 */
	public String callTest(String jsonStr) {
		SwingUtilities.invokeLater(() -> {
			SwingDialog.showInfoMSGDialog("Info", jsonStr);
		});
		return String.valueOf(jsonStr.length());
	}

	/**
	 * 载入进化树模块
	 * 
	 * @param jsonStr 通过JSON把R的变量/对象 传进来
	 */
	public void modernTreeView(String jsonStr) {
		/**
		 * 这里可以写 jsonStr字符串的处理方法，生成JAVA对象
		 */
		SwingUtilities.invokeLater(() -> {
			IndependentModuleLoader loader = new IndependentModuleLoader();
			MainFrameProperties.loadTheModuleFromIModuleLoader(loader);
		});
	}

	/**
	 * 载入热图模块
	 * 
	 * @param jsonStr
	 */
	public void eheatmap(String jsonStr) {
		SwingUtilities.invokeLater(() -> {
			module.heatmap.IndependentModuleLoader loader = new module.heatmap.IndependentModuleLoader();
			MainFrameProperties.loadTheModuleFromIModuleLoader(loader);
		});
	}

	public void correlationVis(String path, String jsonStr) {
		Runnable doRun = () -> {
			try {
				ModuleLoader loader = new ModuleLoader();
				ModuleFace face = MainFrameProperties.loadTheModuleFromIModuleLoader(loader);
				GuiMain theFace = (GuiMain) face;
				PaintingPanel paintingPanel = theFace.getPaintingPanel();
				DataModel dataModel = new DataModel(path, jsonStr);
				paintingPanel.setDataModel(dataModel);
				paintingPanel.repaint();
				face.requestFocus();
				face.requestFocusInWindow();

				new File(path).delete();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("error:\t" + e.getMessage());
			}
		};
		SwingUtilities.invokeLater(doRun);
	}
}
