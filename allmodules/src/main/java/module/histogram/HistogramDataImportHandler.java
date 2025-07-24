package module.histogram;

import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;
import org.apache.commons.lang3.BooleanUtils;

import egps2.EGPSProperties;
import module.skeletonscatter.io.TSVImportInfoBean;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class HistogramDataImportHandler extends AbstractGuiBaseVoiceFeaturedPanel {

	private MainFace mainFace;

	public HistogramDataImportHandler(MainFace mainFace) {
		this.mainFace = mainFace;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("import.file.path",
				EGPSProperties.PROPERTIES_DIR + "/bioData/example/test.file.txt",
				"The import data content, with tsv format.");
		mapProducer.addKeyValueEntryBean("has.header", "T", "Whether has the header line.");
		mapProducer.addKeyValueEntryBean("target.column", "1", "The target data in column, as y axis.");
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		TSVImportInfoBean ret = new TSVImportInfoBean();
		ret.setFilePath(o.getSimplifiedString("import.file.path"));

		ret.setHasHeader(BooleanUtils.toBoolean(o.getSimplifiedBool("has.header")));
		ret.setTargetColumn(o.getSimplifiedInt("target.column"));

		TSVImportInfoBean importBean = ret;
		mainFace.loadingData(importBean);
		mainFace.invokeTheFeatureMethod(0);
	}


}
