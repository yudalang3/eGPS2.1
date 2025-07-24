package module.multiseq.alignment.view;

import java.io.File;
import java.util.List;

import egps2.frame.MainFrameProperties;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

public class ModuleLauncher4MSA2AlignmentView {

	public void msaData2AlignmentView(List<File> inputFiles) {

		for (File file : inputFiles) {
			MS2AlignmentUtil ms2AlignmentUtil = new MS2AlignmentUtil(file);
			SequenceDataForAViewer sequenceData = ms2AlignmentUtil.parseData();
			if (sequenceData == null) {
				return;
			}

			Launcher4ModuleLoader moduleLoader21 = new Launcher4ModuleLoader();
			moduleLoader21.moduleLaunchWay = "Module launched by remnant calling from others.";
			moduleLoader21.whatDataInvoked = "Data directly imported form file ".concat(file.getName());
			moduleLoader21.setSequenceDataForAViewer(sequenceData);
			MainFrameProperties.loadTheModuleFromIModuleLoader(moduleLoader21);
		}
	}

}
