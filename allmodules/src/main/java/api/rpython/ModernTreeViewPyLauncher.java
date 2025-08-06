package api.rpython;

import egps2.Launcher;
import egps2.UnifiedAccessPoint;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;
import egps2.frame.DefaultParamsAssignerAndParserHandler4VOICE;
import egps2.frame.MainFrameProperties;
import module.evolview.moderntreeviewer.IndependentModuleLoader;
import module.evolview.moderntreeviewer.MTreeViewMainFace;
import module.evolview.moderntreeviewer.VOICM4MTV;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModernTreeViewPyLauncher {

    public static void main(String[] args) throws Exception {
        Launcher.main(new String[]{});

        DefaultParamsAssignerAndParserHandler4VOICE defaultVoiceInputParamHandler = new DefaultParamsAssignerAndParserHandler4VOICE();
        VoiceParameterParser parser = defaultVoiceInputParamHandler.getParameterParser();
        String configFilePath = args[0];
//        String configFilePath = "C:\\Users\\yudal\\Documents\\project\\eGPS2\\CLI\\modernTreeView\\input_config.txt";
        String inputs = FileUtils.readFileToString(new File(configFilePath), StandardCharsets.UTF_8);
        OrganizedParameterGetter organizedParameterGetter = parser.getOrganizedParameterGetter(inputs);

        UnifiedAccessPoint.registerActionAfterMainFrame(() -> {
            IndependentModuleLoader loader = new IndependentModuleLoader();
            MTreeViewMainFace moduleFace = (MTreeViewMainFace) MainFrameProperties.loadTheModuleFromIModuleLoader(loader);
            VOICM4MTV importHandler = moduleFace.getImportHandler();
            try {
                Thread.sleep(1500);
                importHandler.execute(organizedParameterGetter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
