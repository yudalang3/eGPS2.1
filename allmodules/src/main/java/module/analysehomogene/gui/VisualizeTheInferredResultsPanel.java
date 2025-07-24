/**
 * 
 */
package module.analysehomogene.gui;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import egps2.frame.ComputationalModuleFace;
import module.parsimonytre.WatchNodeStatesWithChange;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.bean.VoiceValueParameterBean;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

@SuppressWarnings("serial")
public class VisualizeTheInferredResultsPanel extends SummaryNodeStatesResultsPanel {


	public VisualizeTheInferredResultsPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		super.setParameter(mapProducer);
		Map<String, VoiceValueParameterBean> requiredParams = mapProducer.getRequiredParams();
		requiredParams.remove("result.summary.byPos");
		requiredParams.remove("result.summary.byNodes");

		mapProducer.addKeyValueEntryBean("%", "The remnant specific parameters", "");
		// add more parameters
		mapProducer.addKeyValueEntryBean("output.figure.data.path", "",
				"Choose $position.to.watch parameter for interactive watch or this parameters. The two parameters are mutually exclusive. ");
		mapProducer.addKeyValueEntryBean("ancestral.states.path", "",
				"If you set the output.one.figure.path, please set this parameter to assign the ancestral states. Normally it is same as the $preferStateOfRoot");
		mapProducer.addKeyValueEntryBean("position.to.watch", "0", "0 for watch all positions, 1 for first position.");
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		List<String> outputs = Lists.newArrayList();
		Stopwatch stopwatch = Stopwatch.createStarted();


		WatchNodeStatesWithChange runner = new WatchNodeStatesWithChange();

		runner.setPathOfPhylogeneticTree(o.getSimplifiedString("phylo.tree.path"));
		runner.setPathOfInferredSates(o.getSimplifiedString("inferred.node.states.path"));
		runner.setPathOfLeafSates(o.getSimplifiedString("leafStates.path"));

		String leafIDColumnName = o.getSimplifiedString("leaf.identifier");
		runner.setLeafIDColumnName(leafIDColumnName);
		List<String> excludeColNames = o.getEntriesAfterEqualChar("exclude.states.file.columns");
		HashSet<String> excludeStatesColumns = new HashSet<>(excludeColNames);
		runner.setExcludeStatesColumns(excludeStatesColumns);

		runner.setInteractiveWatch(true);

		if (o.isSimplifiedValueExist("output.figure.data.path")) {
			String outputOneFigurePath = o.getSimplifiedString("output.figure.data.path");
			runner.setOutputOneFigurePath(outputOneFigurePath);
			String ancestralStatesPath = o.getSimplifiedString("ancestral.states.path");
			runner.setAncestralStatesPath(ancestralStatesPath);
		} else {
			int position4watch = o.getSimplifiedInt("position.to.watch");
			runner.setIndexOfPositionOneBased(position4watch);
		}

		runner.run();

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);

	}

	@Override
	public String getTabName() {
		return "3. Visualize the Inferred Results";
	}
}
