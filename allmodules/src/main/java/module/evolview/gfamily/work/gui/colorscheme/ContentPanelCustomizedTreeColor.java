package module.evolview.gfamily.work.gui.colorscheme;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.render.CustomizedRender;
import module.evolview.gfamily.work.gui.tree.AbstractContentPanelCustomized;

public class ContentPanelCustomizedTreeColor extends AbstractContentPanelCustomized {

    public ContentPanelCustomizedTreeColor(GeneFamilyController controller, String context, Runnable callBack) {
        super(controller, context, callBack);
    }

    @Override
    public void executeRenderProcess() {
        String text = textArea.getText();
        CustomizedRender render = new CustomizedRender(text, controller);
        render.renderNodes(controller.getTreeLayoutProperties().getOriginalRootNode());
        controller.refreshPhylogeneticTree();
        callBack.run();
    }
}

