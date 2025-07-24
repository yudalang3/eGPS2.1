package module.skeletonscatter;

import org.apache.commons.lang3.BooleanUtils;

import egps2.EGPSProperties;
import module.skeletonscatter.io.TSVImportInfoBean;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

public class VOICM4General2dPlot extends AbstractGuiBaseVoiceFeaturedPanel {

	private MainFace mainFace;
	
	
	public VOICM4General2dPlot(MainFace mainFace) {
		this.mainFace = mainFace;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("import.file.path",
				EGPSProperties.PROPERTIES_DIR + "/bioData/example/test.file.txt",
				"The import data content, with tsv format.");
		mapProducer.addKeyValueEntryBean("has.header", "T", "Whether has the header line.");
		mapProducer.addKeyValueEntryBean("y.axis.column", "1", "The target data in column, as y axis.");
		mapProducer.addKeyValueEntryBean("x.axis.column", "",
				"Optional, default is none, means the order as the x axis.");
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		TSVImportInfoBean ret = new TSVImportInfoBean();
		ret.setFilePath(o.getSimplifiedString("import.file.path"));

		ret.setHasHeader(BooleanUtils.toBoolean(o.getSimplifiedBool("has.header")));

		ret.setTargetColumn(o.getSimplifiedInt("y.axis.column"));
		if (o.isSimplifiedValueExist("x.axis.column")) {
			int xAxisIndex = o.getSimplifiedInt("x.axis.column");
			ret.setXAxisIndex(xAxisIndex);
		}

		TSVImportInfoBean importBean = ret;
		mainFace.paintingPanel.setImportBean(importBean);
		mainFace.invokeTheFeatureMethod(0);
	}



}
