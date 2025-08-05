package module;

import egps2.frame.MainFrameProperties;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.model.tree.TreeLayoutProperties;
import module.evolview.moderntreeviewer.IndependentModuleLoader;

import javax.swing.*;

public class PythonInterfaceEGPS {
    public void modernTreeView(GraphicsNode root, TreeLayoutProperties treeLayoutProperties) {

        SwingUtilities.invokeLater(() -> {
            IndependentModuleLoader loader = new IndependentModuleLoader();
            loader.setModuleData(root, treeLayoutProperties);
            MainFrameProperties.loadTheModuleFromIModuleLoader(loader);
        });
    }
}
