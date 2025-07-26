package demo.floating;

import egps2.EGPSProperties;
import demo.floating.work.BioinformaticsApp;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

/**
 * Implementation of the floating voice panel for bioinformatics visualization.
 * This class handles the parameter configuration and execution logic for the floating
 * bioinformatics visualization modules, including multiple sequence alignment,
 * expression data visualization, and molecular mechanism diagrams.
 */
public class FloatingVoiceImp extends AbstractGuiBaseVoiceFeaturedPanel {
    private final GuiMain guiMain;

    /**
     * Constructor for FloatingVoiceImp
     * @param guiMain The main GUI component to which this implementation belongs
     */
    public FloatingVoiceImp(GuiMain guiMain) {
        this.guiMain = guiMain;
    }

    /**
     * Define parameters for the floating voice panel
     * @param mapProducer The parameter designer used to define input parameters
     */
    @Override
    protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
        mapProducer.addKeyValueEntryBean("%1","Alignment file for evolution","");
        mapProducer.addKeyValueEntryBean("input.msa.fasta",
                EGPSProperties.PROPERTIES_DIR + "/bioData/example/floatingVoice/test.fa",
                "The multiple sequence alignment file");

        mapProducer.addKeyValueEntryBean("%2","Expression files","");
        mapProducer.addKeyValueEntryBean("input.expression.tpm",
                EGPSProperties.PROPERTIES_DIR + "/bioData/example/floatingVoice/exp.tpm",
                "The normalized tmp fasta file");

        mapProducer.addKeyValueEntryBean("%3","Molecular interaction files","");
        mapProducer.addKeyValueEntryBean("input.mol.picture",
                EGPSProperties.PROPERTIES_DIR + "/bioData/example/floatingVoice/mol.mechanism.pptx",
                "The power point file of the molecular mechanism");
    }

    /**
     * Execute the floating voice panel functionality
     * @param o The organized parameter getter containing user inputs
     * @throws Exception If execution fails
     */
    @Override
    protected void execute(OrganizedParameterGetter o) throws Exception {

        BioinformaticsApp bioinformaticsApp = new BioinformaticsApp();
        guiMain.addContent(bioinformaticsApp);

        bioinformaticsApp.displayData();
    }

}