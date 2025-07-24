package module.mutationpre;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.util.SaveUtil;
import module.mutationpre.gui.DrawProperties;
import module.mutationpre.model.InputDataModel;
import module.mutationpre.model.PaintingCalculator;

public class SimplestModuleController  {

	private DrawProperties drawProperties = new DrawProperties();
	private MyWorkStudio myWorkStudio;

	private PaintingCalculator calculator = new PaintingCalculator();

	private InputDataModel parser = new InputDataModel();

	private int indexOfExampleData = 0;
	private GenomicMutationPresenterMain main;
	private InputDataModel inputDataModel;
	
	public SimplestModuleController(GenomicMutationPresenterMain genomicMutationPresenterMain) {
		this.main = genomicMutationPresenterMain;
	}



	public void saveViewPanelAs() {
		SaveUtil saveUtil = new SaveUtil();
		saveUtil.saveData(myWorkStudio.getPaintingPanel(), getClass());
	}


	public void refreshGraphcs(List<String> inputs) {

		try {
			// This is for test
			// drawProperties = DrawProperties.getExample();

			if (myWorkStudio.controller == null) {
				myWorkStudio.setController(this);
			}

			// 这里是计算各种坐标
			Dimension paintingPanelSize = myWorkStudio.getPaintingPanelSize();
			inputDataModel = configInputData(inputs);
			// 需要把计算的东西赋值到 drawProperties 实例中
			calculator.calculate(inputDataModel, drawProperties, paintingPanelSize);
			myWorkStudio.weakRefreshPaintingPanel();

		} catch (Exception e) {
			e.printStackTrace();
			SwingDialog.showErrorMSGDialog("Input error", "Please check your data format.");
		}

	}
	public void recalculateAndRefresh() {
		if (inputDataModel == null) {
			return;
		}
		
		Dimension paintingPanelSize = myWorkStudio.getPaintingPanelSize();
		calculator.calculate(inputDataModel, drawProperties, paintingPanelSize);
		myWorkStudio.weakRefreshPaintingPanel();
		main.invokeTheFeatureMethod(1);
		
	}

	private InputDataModel configInputData(List<String> inputs) {
		String[] inputStrings = inputs.toArray(new String[0]);
		InputDataModel ret = null;
		try {
			ret = parser.configInputDataFromStringArray(inputStrings);
		} catch (IOException e) {
			SwingDialog.showErrorMSGDialog("Input String error", e.getMessage());
			ret = new InputDataModel();
		}
		return ret;

	}

	public DrawProperties getDrawProperties() {
		return drawProperties;
	}

	public void setWorkStudio(MyWorkStudio myWorkStudio) {
		this.myWorkStudio = myWorkStudio;

	}

	public boolean closeTab() {
		myWorkStudio.action4closeTab();
		return true;
	}

	public void heavyRefresh() {
		myWorkStudio.heavyRefresh();
		
	}
	
	public GenomicMutationPresenterMain getMain() {
		return main;
	}

}
