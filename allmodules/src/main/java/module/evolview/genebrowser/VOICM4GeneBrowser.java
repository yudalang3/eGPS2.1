package module.evolview.genebrowser;

import egps2.EGPSProperties;
import module.evolview.genebrowser.io.MapperOfGeneBrowser;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

public class VOICM4GeneBrowser extends AbstractGuiBaseVoiceFeaturedPanel {
	protected GeneBrowserMainFace geneBrowserMainFace;

	private MapperOfGeneBrowser requiredDataBuilder = new MapperOfGeneBrowser();

	private RequiredGeneData requiredGeneData;

	public VOICM4GeneBrowser(GeneBrowserMainFace geneBrowserMainFace) {
		this.geneBrowserMainFace = geneBrowserMainFace;
	}

	public String getPath4storage() {
		final String fileName4store = EGPSProperties.JSON_DIR.concat("/geneBrowserPersistantStorage.ser.gz");
		return fileName4store;
	}

	/**
	 * 输入的信息有可能导出 需要
	 * 
	 * @return
	 */
	public RequiredGeneData getRequiredInputInfor() {
		return requiredGeneData;
	}

	@Override
	public String getExampleText() {
		return requiredDataBuilder.getExampleString();
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		// do not need

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		requiredGeneData = requiredDataBuilder.parseImportedData(o);
		geneBrowserMainFace.initializeTheModule(requiredGeneData);
	}
}
