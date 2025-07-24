package module.evolview.gfamily.work.gui.colorscheme;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.browser.BrowserPanel;
import module.evolview.gfamily.work.gui.browser.CalculatorKeyDomains;
import module.evolview.gfamily.work.gui.browser.CalculatorPrimers;
import module.evolview.gfamily.work.gui.browser.ContentPanelCustomizedKeyDomains;
import module.evolview.gfamily.work.gui.browser.ContentPanelCustomizedPrimerSet;
import module.evolview.gfamily.work.gui.browser.TrackKeyDomains;
import module.evolview.gfamily.work.gui.browser.TrackPrimers;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimers;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;
import module.evolview.gfamily.work.gui.render.CustomizedRenderUtil;
import module.evolview.gfamily.work.gui.tree.JColorChooser4Node;
/**
 * 一个自定义dialog的类，汇集了各个地方用到的自定义的dialog
 */
@SuppressWarnings("serial")
public class DialogFrame extends JDialog {

    public DialogFrame(BaseContentPanel contentPanel) {

        super(UnifiedAccessPoint.getInstanceFrame(), true);

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(contentPanel, BorderLayout.CENTER);

        Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

        JPanel buttonPane = new JPanel();

        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);


        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
        	try {
        		contentPanel.executeRenderProcess();
        		this.dispose();
			} catch (Exception e2) {
				e2.printStackTrace();
				SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(),"Running error","Some thing wrong with your input!");
			}
            
        });


        okButton.setFont(globalFont);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            this.dispose();
        });
        cancelButton.setFont(globalFont);
        buttonPane.add(cancelButton);

    }

 



    public static DialogFrame obetainCustomizedTreeDialog(GeneFamilyController controller, Runnable callBack) {
        ContentPanelCustomizedTreeColor contentPanel = new ContentPanelCustomizedTreeColor(controller, CustomizedRenderUtil.getDefaultCustomizedString(controller), callBack);
        DialogFrame dialogFrame = new DialogFrame(contentPanel);
        dialogFrame.setTitle("Customized renderer");
        dialogFrame.setSize(600, 500);
        dialogFrame.setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
        return dialogFrame;
    }

    public static DialogFrame obetainCustomizedKeyDoMainsDialog(GeneFamilyController controller) {
        StringBuilder stringBuilder = new StringBuilder();
        BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
        TrackKeyDomains elementStructure = selectedBrowserPanel.getElementStructure();
        CalculatorKeyDomains calculatorElementStructure = elementStructure
                .getCalculatorElementStructure();
        List<DrawingPropertySequenceModel> genomeElementModelItems = calculatorElementStructure.getGenomeElementModelItems();
        if (!genomeElementModelItems.isEmpty()) {
            DrawingPropertySequenceModel ncov2019GenomePaintElementModelItem = genomeElementModelItems.get(0);
            String header = ncov2019GenomePaintElementModelItem.getHeader();
            stringBuilder.append(header).append("\n");
            List<DrawingPropertyElement> genomeElementModels = ncov2019GenomePaintElementModelItem.getGenomeElementModels();
            for (DrawingPropertyElement genomeElementModel : genomeElementModels) {
                stringBuilder.append(genomeElementModel.getName()).append("\t");
                stringBuilder.append(genomeElementModel.getGene()).append("\t");
//                stringBuilder.append(genomeElementModel.getAa_start()).append("\t");
//                stringBuilder.append(genomeElementModel.getAa_end()).append("\t");
//                stringBuilder.append(genomeElementModel.getNt_start()).append("\t");
//                stringBuilder.append(genomeElementModel.getNt_end()).append("\t");
//                stringBuilder.append(genomeElementModel.getPfam_IdOrCDD_Id()).append("\t");
//                stringBuilder.append(genomeElementModel.getNote()).append("\t");
//                stringBuilder.append(genomeElementModel.getWebsite()).append("\t").append("\n");
            }
        }

        ContentPanelCustomizedKeyDomains contentPanel = new ContentPanelCustomizedKeyDomains(controller, stringBuilder.toString());
        DialogFrame dialogFrame = new DialogFrame(contentPanel);
        dialogFrame.setTitle("Customized key domain track");
        dialogFrame.setSize(600, 500);
        dialogFrame.setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
        return dialogFrame;
    }

    public static DialogFrame obetainCustomizedPrimerDialog(GeneFamilyController controller) {
        StringBuilder stringBuilder = new StringBuilder();
        
        BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
        TrackPrimers genomePrimers = selectedBrowserPanel.getGenomePrimers();
        CalculatorPrimers calculatorPrimers = genomePrimers.getCalculatorPrimers();
        DrawingPropertyPrimerSet paintPrimerSet = calculatorPrimers.getPaintPrimerSet();
        List<DrawingPropertyPrimers> primers = paintPrimerSet.getPrimers();
        if (!primers.isEmpty()) {
            String header = paintPrimerSet.getHeader();
            stringBuilder.append(header).append("\n");
            for (DrawingPropertyPrimers primer : primers) {
                stringBuilder.append(primer.getInstitution()).append("\t");
                stringBuilder.append(primer.getGene()).append("\t");
                stringBuilder.append(primer.getIndex()).append("\t");
                stringBuilder.append(primer.getFStart()).append("\t");
                stringBuilder.append(primer.getFEnd()).append("\t");
                stringBuilder.append(primer.getRStart()).append("\t");
                stringBuilder.append(primer.getREnd()).append("\t").append("\n");
            }
        }
        ContentPanelCustomizedPrimerSet contentPanel = new ContentPanelCustomizedPrimerSet(controller, stringBuilder.toString());
        DialogFrame dialogFrame = new DialogFrame(contentPanel);
        dialogFrame.setTitle("Customized primer track");
        dialogFrame.setSize(600, 500);
        dialogFrame.setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
        return dialogFrame;
    }

//    /**
//     * country filter点击choose后调用的dialog
//     *因为CGB2.0和CGB1.0的这个dialog所需传入参数不同以及LeftDataOperationPanel不同，所以dialog移至了各自的方法，不再放在此处。
//     *
//     * @param controller
//     * @param filterState
//     * @return
//     */
//    public static DialogFrame obetainCountryDialogForFilter(SarsCov2ViewerController controller, FilterState filterState) {
//        CountryJsonOperator countryJsonOperator = new CountryJsonOperator();
//        CountriesByContinent countriesByContinent = countryJsonOperator.getCountriesByContinent();
//        FilterPanelCountry2 contentPanel = new FilterPanelCountry2(controller.getGloablCountry2numberMap(),countriesByContinent, controller, filterState);
//        DialogFrame dialogFrame = new DialogFrame(contentPanel);
//        dialogFrame.setTitle("Country & region chooser");
//        dialogFrame.setSize(1500, 800);
//        dialogFrame.setLocationRelativeTo(UniSoftInstance.getInstanceFrame());
//
//        
//        return dialogFrame;
//    }
    
    /**
     * 在树上选中了节点后，点击recolor后跳出的颜色选择对话框
     * @param controller
     * @return
     */
    public static DialogFrame nodeRecolorDialogFrame(GeneFamilyController controller) {
    	JColorChooser4Node jColorChoo=new JColorChooser4Node(controller);
       
        DialogFrame dialogFrame = new DialogFrame(jColorChoo);
        dialogFrame.setTitle("Recolor");
        dialogFrame.setSize(730, 550);
        dialogFrame.setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());

        return dialogFrame;
    }


}
