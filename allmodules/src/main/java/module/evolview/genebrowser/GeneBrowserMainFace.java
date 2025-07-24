package module.evolview.genebrowser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import com.google.common.base.Joiner;
import com.jidesoft.swing.JideTabbedPane;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.model.filefilter.FileFilterPPTX;
import utils.EGPSObjectsUtil;
import egps2.utils.common.util.EGPSPrintUtilities;
import egps2.utils.common.util.SaveUtil;
import egps2.EGPSProperties;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.GeneFamilyMainFace;
import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.beans.GeneFamilyMainFaceBean;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.gfamily.work.gui.browser.AbstractTrackPanel;
import module.evolview.gfamily.work.gui.browser.BrowserPanel;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * |---------------------------------------------------------------|
 * |mainSplitPanel              |                                  |
 * |                            |                                  |
 * |LeftControlPanelContainner  |  rightSplitPane                  |
 * |                            |                                  |
 * |                            |  tabbedPhylogeneticTreePane      | 
 * |                            |                                  |
 * |                            | ---------------------------------|
 * |                            |                                  |
 * |                            |  tabbedAnalysisPanel             | 
 * |---------------------------------------------------------------|
 *
 * </pre>
 *
 * @author yudalang
 *
 */
public class GeneBrowserMainFace extends GeneFamilyMainFace {

    private static final Logger log = LoggerFactory.getLogger(GeneBrowserMainFace.class);
    private final String PERSISTENT_STORE_PATH = EGPSProperties.JSON_DIR.concat("/geneBrowserFace.json");

    private VOICM4GeneBrowser importHandler = new VOICM4GeneBrowser(this);

    GeneBrowserMainFace(IModuleLoader moduleLoader) {
        super(moduleLoader);
    }

    @Override
    public boolean closeTab() {
        controlPanelContainner.actionForSaveFile();
        try {
            GeneFamilyMainFaceBean bean = new GeneFamilyMainFaceBean();
            bean.setSavedLocationOfMainSplitPanel(mainSplitPane.getDividerLocation());
            EGPSObjectsUtil.persistentSaveJavaBeanByFastaJSON(bean, new File(PERSISTENT_STORE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void changeToThisTab() {

    }

    @Override
    public boolean canImport() {
        return true;
    }

    @Override
    public void importData() {
        importHandler.doUserImportAction();
    }

    @Override
    public boolean canExport() {
        return true;
    }

    @Override
    public void exportData() {
        BrowserPanel selectedBrowserPanel = getSelectedBrowserPanel();
        if (selectedBrowserPanel == null) {
            SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(), "Export error",
                    "There are no data in the remnant,\nPlease input data first.");
            return;
        }

        Consumer<String> txtAction = txt -> {
            exportPptxByPart(selectedBrowserPanel,txt);
        };

        FileFilterPPTX fileFilterPPTX = new FileFilterPPTX() {
            @Override
            public String getDescription() {
                return "Multipart powerPoint slides: PPTX (*.pptx)";
            }
        };


        try {
            new SaveUtil().saveData(selectedBrowserPanel.getContextPanel(), this.getClass() ,Pair.of(fileFilterPPTX, txtAction));
        } catch (Exception e) {
            log.error("Running error", e);
        }

    }

    private void exportPptxByPart(BrowserPanel selectedBrowserPanel,String pathHeader) {
        SwingWorker<Boolean, Void> swingWorker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {

                    List<AbstractTrackPanel> tracks = selectedBrowserPanel.getTracks();

                    int index = 0;
                    for (AbstractTrackPanel abstractTrackPanel : tracks) {
                        index++;
                        String join = Joiner.on('.').join(pathHeader, "gene.browser.part", String.valueOf(index),
                                "pptx");
                        File file = new File(join);
                        log.info(file.toString());
                        EGPSPrintUtilities.saveAsPptx(abstractTrackPanel, file);

                    }
                } catch (Exception e2) {
                    log.error("Running error", e2);
                    SwingDialog.showErrorMSGDialog("Running error", e2.getMessage());
                    return Boolean.FALSE;
                }

                return Boolean.TRUE;
            }

            @Override
            protected void done() {
            }
        };

        swingWorker.execute();
    }

    @Override
    public void initializeGraphics() {
        initializeTheModule();

        importHandler.doUserImportAction();
    }

    @Override
    public String[] getFeatureNames() {
        return new String[]{"Single gene structure display", "Browser interactive features"};
    }

    public GeneFamilyController getController() {
        return controller;
    }

    @Override
    protected void initializeTheModule() {
        invokeTheFeatureMethod(0);
        controller = new GeneFamilyController(this);

        mainSplitPane = getMainSplitPane();
        mainSplitPane.setBorder(null);

        controlPanelContainner = new GBControlPanelContainner(controller);
        mainSplitPane.setLeftComponent(controlPanelContainner);

        // ==== for persistent storage
        GeneFamilyMainFaceBean bean = null;
        File jsonFile = new File(PERSISTENT_STORE_PATH);
        if (jsonFile.exists()) {
            try {
                bean = EGPSObjectsUtil.obtainJavaBeanByFastaJSON(GeneFamilyMainFaceBean.class, jsonFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bean == null) {
            bean = new GeneFamilyMainFaceBean();
        }
        mainSplitPane.setDividerLocation(bean.getSavedLocationOfMainSplitPanel());
        // ====

        JideTabbedPane tabbedAnalysisPanel2 = getTabbedAnalysisPanel();
        tabbedAnalysisPanel2.setShowCloseButton(true);
        tabbedAnalysisPanel2.setShowCloseButtonOnTab(true);
        tabbedAnalysisPanel2.setTabEditingAllowed(true);


        mainSplitPane.setRightComponent(tabbedAnalysisPanel2);
        add(mainSplitPane, BorderLayout.CENTER);


    }

    /**
     *
     * The main frame consist of following elements:
     *
     * <pre>
     *
     * 首先是垂直上的构造
     *
     *
     * Shell Panel
     *
     * ControlPanel
     *     |---------------------------------------|
     *     |                         |             | TreePanel
     *     |                         |             |
     *     |                         |             |
     *     |                         |             |
     *     |                         | ----------- |
     *     |                         |             | BrowserPanel
     *     |                         |             |
     *     |                         |             |
     *     |---------------------------------------|
     *
     *
     *
     * </pre>
     */
    public void initializeTheModule(RequiredGeneData geneData) {

        GBBrowserPanel ncov2019GenomeMain = new GBBrowserPanel(controller, geneData);

        JideTabbedPane tabbedAnalysisPanel = getTabbedAnalysisPanel();
        tabbedAnalysisPanel.addTab("Gene browser", IconObtainer.get("dna.png"), ncov2019GenomeMain,
                "Track-view of Single structure, including protein, DNA, mRNA, et al.");

        tabbedAnalysisPanel.setSelectedComponent(ncov2019GenomeMain);
        // ==========================================================================================================

    }

    public BrowserPanel getSelectedBrowserPanel() {
        JideTabbedPane tabbedAnalysisPanel = getTabbedAnalysisPanel();
        if (tabbedAnalysisPanel == null) {
            return null;
        }

        Component selectedComponent = tabbedAnalysisPanel.getSelectedComponent();

        if (selectedComponent == null) {
            return null;
        }
        BrowserPanel phylogeneticTreePanel = (BrowserPanel) selectedComponent;
        return phylogeneticTreePanel;
    }

    @Override
    public IInformation getModuleInfo() {
        IInformation iInformation = new IInformation() {

            @Override
            public String getWhatDataInvoked() {
                return "The data is loading from the import dialog.";
            }

            @Override
            public String getSummaryOfResults() {
                return "The single sequence structure is generated by the eGPS software.";
            }
        };
        return iInformation;
    }

}
