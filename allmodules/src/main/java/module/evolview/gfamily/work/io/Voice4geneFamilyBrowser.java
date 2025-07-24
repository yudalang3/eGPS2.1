package module.evolview.gfamily.work.io;

import java.util.Optional;

import module.evolview.gfamily.GeneFamilyMainFace;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.moderntreeviewer.io.MTVImportInforBean;
import module.evolview.moderntreeviewer.io.TreeParser4MTV;
import module.evolview.moderntreeviewer.io.TreePropertiesAssigner;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

/**
 * 这个类的移植还是有点麻烦，需要花时间让其更加通用
 */
public class Voice4geneFamilyBrowser extends AbstractGuiBaseVoiceFeaturedPanel {

    private final GeneFamilyMainFace main;
    private final ParamsAssignerAndParser4GeneFamilyBrowser evolutionTreeParserMap4VOICM = new ParamsAssignerAndParser4GeneFamilyBrowser();

    public Voice4geneFamilyBrowser(GeneFamilyMainFace main) {
        this.main = main;
    }

    @Override
    public String getExampleText() {
        return evolutionTreeParserMap4VOICM.getExampleString();
    }


	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		// no need to implement as the getExampleText() method is already implement

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		evolutionTreeParserMap4VOICM.obtainData(o);
        RequiredGeneData requiredGeneData = evolutionTreeParserMap4VOICM.getRequiredGeneData();
        MTVImportInforBean mtvImportInfo = evolutionTreeParserMap4VOICM.getMTVImportInfo();
        Optional<GraphicsNode> optRoot = new TreeParser4MTV().parseTree(mtvImportInfo);
        if (!optRoot.isPresent()) {
            throw new IllegalArgumentException("The phylogenetic tree has error");
        }

        GraphicsNode convertNode = optRoot.get();
        TreeLayoutProperties treeLayoutProperties = new TreeLayoutProperties(convertNode);

        TreePropertiesAssigner treePropertiesAssigner = new TreePropertiesAssigner();
        treePropertiesAssigner.assign(treeLayoutProperties, mtvImportInfo);

        main.initializeTheModule(requiredGeneData, treeLayoutProperties);

	}

}
