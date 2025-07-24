package primary.struct.display;

import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;
import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.utils.common.util.SaveUtil;
import primary.struct.display.data.PairwiseHomologyData;
import primary.struct.display.drawer.PrimaryStructDrawer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class MyModuleFace extends ModuleFace {

    PrimaryStructDrawer primaryStructDrawer = new PrimaryStructDrawer();
    ParametersImporter voicm4General2dPlot;

    protected MyModuleFace(IModuleLoader moduleLoader) {
        super(moduleLoader);
        setLayout(new GridLayout(0, 1, 0, 10));

        LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();
        Font defaultFont = launchProperty.getDefaultFont();
        Font defaultTitleFont = launchProperty.getDefaultTitleFont();


        try {
            JComponent jComponent = myAnalyis();
            add(jComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private JComponent myAnalyis() throws IOException {
        JScrollPane jScrollPane = new JScrollPane(primaryStructDrawer);
        jScrollPane.setBorder(null);
        return jScrollPane;
    }

    public void resetData(PairwiseHomologyData pairwiseHomologyData) throws IOException {
		primaryStructDrawer.configData(pairwiseHomologyData);
		primaryStructDrawer.repaint();
    }


    @Override
    public boolean canImport() {
        return true;
    }

    @Override
    public void importData() {
        ParametersImporter voicm4General2dPlot2 = getVoicm4General2dPlot();
        voicm4General2dPlot2.doUserImportAction();
    }

    public ParametersImporter getVoicm4General2dPlot() {
        if (voicm4General2dPlot == null) {
            voicm4General2dPlot = new ParametersImporter(this);
        }
        return voicm4General2dPlot;
    }

    @Override
    public boolean canExport() {
        return true;
    }

    @Override
    public void exportData() {
        new SaveUtil().saveData(primaryStructDrawer);
    }

    @Override
    protected void initializeGraphics() {

    }

}
