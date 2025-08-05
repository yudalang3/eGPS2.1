package module.evolview.pathwaybrowser.io;

import java.util.LinkedHashMap;
import java.util.Optional;

import module.evolview.model.tree.GraphicsNode;
import module.evolview.model.tree.TreeLayoutProperties;
import module.evolview.moderntreeviewer.io.TreeParser4MTV;
import module.evolview.moderntreeviewer.io.TreePropertiesAssigner;
import module.evolview.pathwaybrowser.PathwayFamilyMainFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.bean.VoiceValueParameterBean;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

public class Voice4pathwayFamilyBrowser extends AbstractGuiBaseVoiceFeaturedPanel {
	private final PathwayFamilyMainFace pathwayFamilyMainFace;
	private final ParamsAssignerAndParser4pathwayFamBrowser paramsAssignerAndParser4pathwayFamBrowser = new ParamsAssignerAndParser4pathwayFamBrowser();

	public Voice4pathwayFamilyBrowser(PathwayFamilyMainFace pathwayFamilyMainFace) {
		super();
		this.pathwayFamilyMainFace = pathwayFamilyMainFace;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		LinkedHashMap<String, VoiceValueParameterBean> requiredParams1 = paramsAssignerAndParser4pathwayFamBrowser
				.getRequiredParams();
		mapProducer.addKeyValueEntryBean(requiredParams1);
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		PathwayFamilyBrowserImportInfoBean importBeanInfo = paramsAssignerAndParser4pathwayFamBrowser
				.getImportBeanInfo(o);
		pathwayFamilyMainFace.invokeTheFeatureMethod(0);

		Optional<GraphicsNode> optRoot = new TreeParser4MTV().parseTree(importBeanInfo);
		if (!optRoot.isPresent()) {
			throw new IllegalArgumentException("The phylogenetic tree has error");
		}

		GraphicsNode convertNode = optRoot.get();
		TreeLayoutProperties treeLayoutProperties = new TreeLayoutProperties(convertNode);

		TreePropertiesAssigner treePropertiesAssigner = new TreePropertiesAssigner();
		treePropertiesAssigner.assign(treeLayoutProperties, importBeanInfo);
		pathwayFamilyMainFace.initializeTheModule(importBeanInfo, treeLayoutProperties);
	}

}
