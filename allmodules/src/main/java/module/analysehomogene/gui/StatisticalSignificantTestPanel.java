/**
 *
 */
package module.analysehomogene.gui;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import phylo.gsefea.DoGSEFEA;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

import java.util.List;

public class StatisticalSignificantTestPanel extends DockableTabModuleFaceOfVoice {

    public StatisticalSignificantTestPanel(ComputationalModuleFace cmf) {
        super(cmf);
    }

    @Override
    protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
        mapProducer.addKeyValueEntryBean("phylo.tree.path", "", "Path to the phylogenetic tree file. Necessary");
        mapProducer.addKeyValueEntryBean("result.summary.byNodes", "", "The output path of the result by nodes. Necessary");
        mapProducer.addKeyValueEntryBean("output.file", "", "THe output results with the tsv format. Necessary");
    }

    @Override
    protected void execute(OrganizedParameterGetter o) throws Exception {
        String treeFile = o.getSimplifiedString("phylo.tree.path");
        String resultSummaryByNodesPath = o.getSimplifiedString("result.summary.byNodes");
        String outputFilePath = o.getSimplifiedString("output.file");

        List<String> outputs = Lists.newArrayList();
        Stopwatch stopwatch = Stopwatch.createStarted();

        DoGSEFEA doGSEFEA = new DoGSEFEA();
        List<String> perform = doGSEFEA.perform(treeFile, resultSummaryByNodesPath, outputFilePath);
        outputs.addAll(perform);

        stopwatch.stop();
        long millis = stopwatch.elapsed().toMillis();
        outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
        setText4Console(outputs);
    }



    @Override
    public String getTabName() {
        return "4. Statistical Significant Test";
    }

    @Override
    public String getShortDescription() {
        return null;
    }
}
