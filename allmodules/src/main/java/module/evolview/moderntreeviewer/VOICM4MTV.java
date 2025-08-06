package module.evolview.moderntreeviewer;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.google.common.base.Strings;

import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;
import egps2.panels.dialog.SwingDialog;
import egps2.EGPSProperties;
import module.evoltreio.EvolTreeImportInfoBean;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.model.tree.TreeLayoutProperties;
import module.evolview.moderntreeviewer.io.MTVImportInforBean;
import module.evolview.moderntreeviewer.io.ParamsAssignerAndParser4ModernTreeView;
import module.evolview.moderntreeviewer.io.TreeParser4MTV;
import module.evolview.moderntreeviewer.io.TreePropertiesAssigner;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个类的移植还是有点麻烦，需要花时间让其更加通用
 */
public class VOICM4MTV extends AbstractGuiBaseVoiceFeaturedPanel {

    private static final Logger log = LoggerFactory.getLogger(VOICM4MTV.class);
    private final MTreeViewMainFace mTreeViewMainFace;

    private EvolTreeImportInfoBean current;

    private ParamsAssignerAndParser4ModernTreeView evolTreeParserMap4VOICM;

    private byte exampleIndex = 0;

    public VOICM4MTV(MTreeViewMainFace mTreeViewMainFace) {
        this.mTreeViewMainFace = mTreeViewMainFace;
    }

    @Override
    protected void actionBeforeVoiceGuiAppear() {
        exampleIndex = 0;
    }

    @Override
    protected int getNumberOfExamples() {
        return 3;
    }

    private boolean whetherSameImportInfoBean(EvolTreeImportInfoBean newBean) {
        if (current == null) {
            return false;
        }
        if (!Objects.equals(current.getInput_nwk_string(), newBean.getInput_nwk_string())) {
            return false;
        }

        if (!Objects.equals(current.getInput_nwk_path(), newBean.getInput_nwk_path())) {
            return false;
        }

        if (!Objects.equals(current.getInput_tableLike_path(), newBean.getInput_tableLike_path())) {
            return false;
        }

        return true;
    }

    @Override
    public String getExampleText() {

        evolTreeParserMap4VOICM = new ParamsAssignerAndParser4ModernTreeView();

        if (exampleIndex == 0) {
            String nwkStr = "(((a:8.5,b:8.5):2.5,e:11.0):5.5,(c:14.0,d:14.0):2.5):0.0;";
            evolTreeParserMap4VOICM.addKeyValueEntryBean("input.nwk.string", nwkStr,
                    "Direct input the nwk string content. Hight priority.");
        } else if (exampleIndex == 1) {
            String speciesTree = EGPSProperties.PROPERTIES_DIR + "/bioData/example/treeBarplot/01.speci.nwk";
            evolTreeParserMap4VOICM.addKeyValueEntryBean("input.nwk.path", speciesTree, "Input the nwk file path.");
        } else if (exampleIndex == 2) {
            evolTreeParserMap4VOICM.addKeyValueEntryBean("input.nwk.path", "", "Input the nwk file path.");
            evolTreeParserMap4VOICM.addKeyValueEntryBean("input.tableLike.path",
                    EGPSProperties.PROPERTIES_DIR + "/bioData/example/myTree.tpv",
                    "Input the table-like tree file path.");
            exampleIndex = -1;
        }
        exampleIndex++;

        return evolTreeParserMap4VOICM.getExampleString();
    }

    @Override
    protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
        // do not need to set the parameter, because the getExampleText is already
        // re-writed
    }

    @Override
    public void execute(OrganizedParameterGetter o) throws Exception {
        if (evolTreeParserMap4VOICM == null) {
            getExampleText();
        }

        // 这个导入过程，如果更改需要注意三处：VOICM4MTV类的execute()；GeneFamilyMainFace类的initialize和MTreeViewMainFace的initializeGraphics()

        try {
            MTVImportInforBean object = evolTreeParserMap4VOICM.generateTreeFromKeyValue(o);

            boolean whetherSameImportInfoBean = whetherSameImportInfoBean(object);
            if (whetherSameImportInfoBean) {
                return;
            }

            Optional<GraphicsNode> optRoot = new TreeParser4MTV().parseTree(object);
            if (!optRoot.isPresent()) {
                return;
            }

            GraphicsNode convertNode = optRoot.get();
            TreeLayoutProperties treeLayoutProperties = new TreeLayoutProperties(convertNode);

            TreePropertiesAssigner treePropertiesAssigner = new TreePropertiesAssigner();
            treePropertiesAssigner.assign(treeLayoutProperties, object);
            treePropertiesAssigner.assignGraphicsNodeEffects(convertNode, object);

            String dataSource = null;
            if (!Strings.isNullOrEmpty(object.getInput_nwk_string())) {
                dataSource = "Data is loading from nwk string.";
            } else if (!Strings.isNullOrEmpty(object.getInput_nwk_path())) {
                File file = new File(object.getInput_nwk_path());
                dataSource = "Data is loading from file: ".concat(file.getName());
            } else {
                dataSource = "Data is loading from eGPS-defined table like string ";
            }

            mTreeViewMainFace.whatDataInvoked = dataSource;

            mTreeViewMainFace.phylogeneticTreePanel = new PhylogeneticTreePanel(treeLayoutProperties, convertNode, null,
                    null);
            mTreeViewMainFace.creativeModeTaskPanel.setTreeLayoutProperties(treeLayoutProperties);
            mTreeViewMainFace.controller.setTreeLayoutProperties(treeLayoutProperties);
        } catch (Exception e2) {
            SwingDialog.showErrorMSGDialog("Running error", e2.getMessage());
            e2.printStackTrace();
            return;
        }
        log.trace("Finish loading tree.");
        SwingUtilities.invokeLater(() -> {
            // 要把原来的viewport移除，再新增
            JViewport viewport = mTreeViewMainFace.scrollPane.getViewport();
            viewport.removeAll();
            log.trace("Count is: {}", viewport.getComponentCount());
            viewport.add(mTreeViewMainFace.phylogeneticTreePanel);
            log.trace("Count is: {}, parent is {}", viewport.getComponentCount(), mTreeViewMainFace.phylogeneticTreePanel.getParent());
            log.trace("phylogeneticTreePanel, parent is {}", mTreeViewMainFace.phylogeneticTreePanel.getParent());

            mTreeViewMainFace.phylogeneticTreePanel.initializeLeftPanel();
            log.trace("mTreeViewMainFace.phylogeneticTreePanel.initializeLeftPanel();");
            mTreeViewMainFace.reInitializeGUIAccording2Properties();
            log.trace("mTreeViewMainFace.reInitializeGUIAccording2Properties;");
        });
		/**
		 * 教训：这里有个巨大的坑要注意：
		 *
		 * MainFace有个功能是 initialize，它也会放入EDT里面，所以很多时候GUI的线程如果不是一个代码块的话，不可预测啊
		 */

    }

}
