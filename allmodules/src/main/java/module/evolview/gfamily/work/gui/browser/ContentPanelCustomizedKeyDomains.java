package module.evolview.gfamily.work.gui.browser;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.tree.AbstractContentPanelCustomized;

@SuppressWarnings("serial")
public class ContentPanelCustomizedKeyDomains extends AbstractContentPanelCustomized {

	public ContentPanelCustomizedKeyDomains(GeneFamilyController controller, String context) {
		super(controller, context);
	}

	@Override
	public void executeRenderProcess() throws Exception {
		 List<String> list=new ArrayList<String>();
        for(int i=0;i<=10;i++){
            list.add(i+"x");
        }
        System.out.println("list："+list);

        //截取集合部分元素
        ImmutableList<String> imInfolist=ImmutableList.copyOf(list);
        System.out.println("imInfolist："+imInfolist);
        
		throw new UnsupportedOperationException();
//		String text = textArea.getText();
//		BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
//
//		if (selectedBrowserPanel == null) {
//			throw new InputMismatchException("The control selected browser panel not exists.");
//		}
//
//		TrackKeyDomains elementStructure = selectedBrowserPanel.getElementStructure();
//		CalculatorKeyDomains calculatorElementStructure = elementStructure.getCalculatorElementStructure();
//		ReaderDoMain readerDoMain = new ReaderDoMain();
//		DrawingPropertySeqElementModel ncov2019GenomePaintElementModelItem = readerDoMain
//				.generateNcov2019GenomeElementModels(text);
//		calculatorElementStructure.setGenomeElementModelItems(ncov2019GenomePaintElementModelItem);
//		
//		selectedBrowserPanel.getGeneDrawingLengthCursor().setReCalculator(true);
//		controller.refreshViewGenomeBrowser();
	}
}
