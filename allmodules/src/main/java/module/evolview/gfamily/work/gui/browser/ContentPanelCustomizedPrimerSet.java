package module.evolview.gfamily.work.gui.browser;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.tree.AbstractContentPanelCustomized;

/**
 * 自定义引物 可以输入一个文件或者编辑文本框进行更换绘制引物的数据
 *
 * @Author: mhl
 * @Date Created on: 2020-07-24 14:45
 */
@SuppressWarnings("serial")
public class ContentPanelCustomizedPrimerSet extends AbstractContentPanelCustomized {

	public ContentPanelCustomizedPrimerSet(GeneFamilyController controller, String context) {
		super(controller, context);
	}

	@Override
	public void executeRenderProcess() {
//		String text = textArea.getText();
//		BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
//		
//		if (selectedBrowserPanel == null) {
//			throw new InputMismatchException("The control selected browser panel not exists.");
//		}
//		
//		TrackPrimers genomePrimers = selectedBrowserPanel.getGenomePrimers();
//		CalculatorPrimers calculatorPrimers = genomePrimers.getCalculatorPrimers();
//		
//		
//		String[] split = text.split("\n");
//
//		ReaderPrimer readerPrimer = new ReaderPrimer();
//		DrawingPropertyPrimerSet primerSet = readerPrimer
//				.generateNcov2019GenomePaintPrimerSet(selectedBrowserPanel.getGeneCalculatedDerivedStatistics(), Arrays.asList(split));
//		calculatorPrimers.setGenomePrimerSet(primerSet);
//		
//		selectedBrowserPanel.getGeneDrawingLengthCursor().setReCalculator(true);
//		
//		controller.refreshViewGenomeBrowser();
	}
}
