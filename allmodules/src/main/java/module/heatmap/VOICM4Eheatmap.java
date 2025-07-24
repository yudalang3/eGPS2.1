package module.heatmap;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import org.apache.commons.compress.utils.Lists;

import com.google.common.base.Joiner;

import utils.string.EGPSStringUtil;
import module.heatmap.dataset.ExampleObtainer;
import egps2.modulei.IModuleLoader;

public class VOICM4Eheatmap extends VersatileOpenInputClickAbstractGuiBase {
	
	private int exampleIndex = 0;
	
	private ExampleObtainer exampleObtainer = new ExampleObtainer();

	private EheatmapMain main;

	public VOICM4Eheatmap(EheatmapMain eheatmapMain) {
		this.main = eheatmapMain;
	}

	@Override
	public String getExampleText() {
		List<String> exampleContent = exampleObtainer.getExampleContent(exampleIndex);
		String join = Joiner.on('\n').join(exampleContent);
		
		exampleIndex ++;
		return join;
	}
	
	@Override
	protected int getNumberOfExamples() {
		return exampleObtainer.getExampleCount();
	}

	@Override
	public void execute(String inputs) throws Exception {
		//可惜这里不需要去掉#之后的而是原来的值
//		EgpsParameterParser egpsParameterParser = new EgpsParameterParser();
//		List<String> stringsFromLongSingleLine = egpsParameterParser.getStringsFromLongSingleLine(inputs);

		Optional<IModuleLoader> moduleLoaderOpt = main.getModuleLoader();
		if (!moduleLoaderOpt.isPresent()) {
			throw new InputMismatchException("No remnant loader, please check by the developer.");
		}

		IndependentModuleLoader moduleLoader = (IndependentModuleLoader) moduleLoaderOpt.get();
		
		String[] splits = EGPSStringUtil.split(inputs, '\n');
		List<String> inputList = Lists.newArrayList();
		for (String string : splits) {
			inputList.add(string.trim());
		}
		moduleLoader.setInputContents(inputList);

		main.initializeGraphics();
		// 重新设置回 index = 0
		exampleIndex = 0;
	}

}
