package module.evoldist.msa2distview;

import egps2.EGPSProperties;
import msaoperator.io.seqFormat.MSA_DATA_FORMAT;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

public class VOICM4MSA2EvolDist extends AbstractGuiBaseVoiceFeaturedPanel {

	MSA2DistanceMatrixViewerMain main;

	public VOICM4MSA2EvolDist(MSA2DistanceMatrixViewerMain main) {
		this.main = main;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.file.path",
				EGPSProperties.PROPERTIES_DIR + "/bioData/genomicMuts/example.longAlignedFasta_2.fas",
				"Input MSA file path");
		String[] allSupportedNames = MSA_DATA_FORMAT.getAllSupportedNames();

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("Input MSA format, support: ");

		for (String string : allSupportedNames) {
			sBuilder.append(string).append(", ");
		}
		mapProducer.addKeyValueEntryBean("msa.file.format", "Aligned fasta", sBuilder.toString());
		mapProducer.addKeyValueEntryBean("whether.output.to.file", "F",
				"True, the results will be exported to file. False, the results will be viewed on this panel.");
		mapProducer.addKeyValueEntryBean("output.file.path", "",
				"If $whether.output.to.file= T, the results will output to path.");

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		MSA2DistImportBean object = new MSA2DistImportBean();
		object.setFileFormat(o.getSimplifiedString("msa.file.format"));
		object.setFilePath(o.getSimplifiedString("input.file.path"));
		object.setOutputToFile(o.getSimplifiedBool("whether.output.to.file"));
		object.setOutputPath(o.getSimplifiedStringWithDefault("output.file.path"));

		PLMSAFile2DistMatrix plmsaFile2DistMatrix = new PLMSAFile2DistMatrix(object);

		if (!object.isOutputToFile()) {
			plmsaFile2DistMatrix.setMainFace(main);
		}

		main.registerRunningTask(plmsaFile2DistMatrix);

	}
	

}
