/**
 *
 */
package module.analysehomogene.gui;

import java.util.HashSet;
import java.util.List;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import egps2.frame.ComputationalModuleFace;
import module.parsimonytre.ResultsSummary;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;

public class SummaryNodeStatesResultsPanel extends DockableTabModuleFaceOfVoice {

    public SummaryNodeStatesResultsPanel(ComputationalModuleFace cmf) {
        super(cmf);
    }

    @Override
    protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("@", "Analyse homogeneous with species tree: SummaryNodeStatesResultsPanel",
				"");

		mapProducer.addKeyValueEntryBean("phylo.tree.path", "", "Path to the phylogenetic tree file. Necessary");
        mapProducer.addKeyValueEntryBean("leafStates.path", "", "Path to the leaf states TSV file. Necessary");
		mapProducer.addKeyValueEntryBean("leaf.identifier", "leafName",
				"The ID of the leaf name/specie name, i.e. the column name of the $leafStates.path, Default is leafName");
        mapProducer.addKeyValueEntryBean("exclude.states.file.columns", "", "Example: a;b;c . The columns to excludes for inference, default none: assume beside the leafID, these are positions.");
        mapProducer.addKeyValueEntryBean("%", "Above parameters are same with sub tab 1", "");

		mapProducer.addKeyValueEntryBean("inferred.node.states.path", "",
				"Path to the inferred node states path, the results of \"the first sub tab\". Necessary");
        mapProducer.addKeyValueEntryBean("result.summary.byNodes", "", "The output path of the result by nodes. Necessary\n# The output file Has header line.");
        mapProducer.addKeyValueEntryBean("result.summary.byPos", "", "The output path of the result by position. Necessary\n# The output file NO header line.");
    }

    @Override
    protected void execute(OrganizedParameterGetter o) throws Exception {
        ResultsSummary runner = new ResultsSummary();

        List<String> outputs = Lists.newArrayList();
        Stopwatch stopwatch = Stopwatch.createStarted();

        runner.setPathOfInferredSates(o.getSimplifiedString("inferred.node.states.path"));
        runner.setPathOfLeafSates(o.getSimplifiedString("leafStates.path"));

        runner.setPathOfPhylogeneticTree(o.getSimplifiedString("phylo.tree.path"));
        runner.setOutputSummaryPaths(o.getSimplifiedString("result.summary.byNodes"), o.getSimplifiedString("result.summary.byPos"));

        String leafIDColumnName = o.getSimplifiedString("leaf.identifier");
        runner.setLeafIDColumnName(leafIDColumnName);
        List<String> excludeColNames = o.getEntriesAfterEqualChar("exclude.states.file.columns");
        HashSet<String> excludeStatesColumns = new HashSet<>(excludeColNames);
        runner.setExcludeStatesColumns(excludeStatesColumns);

        runner.run();

        stopwatch.stop();
        long millis = stopwatch.elapsed().toMillis();
        outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
        setText4Console(outputs);

    }


    @Override
    public String getShortDescription() {
        return null;
    }

    @Override
    public String getTabName() {
        return "2. Summary Node States With Species and Node";
    }
}
