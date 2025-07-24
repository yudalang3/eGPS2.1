package module.regexExtract;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import egps2.EGPSProperties;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;

public class ImportAbstractMap4VOICE extends AbstractParamsAssignerAndParser4VOICE {
	
	public ImportAbstractMap4VOICE() {
		addKeyValueEntryBean("import.file", EGPSProperties.PROPERTIES_DIR + "/bioData/example/translated.species.txt",
				"String contents in the file.");
		addKeyValueEntryBean("regx.expression", "(.*?)\\\\s*-\\\\s*(.*?)\\\\s*\\\\((.*?)\\\\)", "Input the regular expression.");
		addKeyValueEntryBean("capture.group.sep", "\\t",
				"The seperator of the captured group. That is the output results will be seperate by which symbol.\nFor example: the captered string are: a,b,c if seperated by ,");
		addKeyValueEntryBean("only.console", "T", "whether only export results in console.");
		addKeyValueEntryBean("export.file", "", "Export the results to file");
	}
	
	
	
	public File getImportFile(Map<String, String> inputMap) {
		String file = inputMap.get("$import.file");
		return new File(file);
	}
	
	public String getRegxExpressionString(Map<String, String> inputMap) {
		String file = inputMap.get("$regx.expression");
		// 去除字符串中多余的反斜杠，假设都是成对出现的
		// 这是一个重要的转换过程
		String regexText = file.replace("\\\\", "\\");
		return regexText;
	}
	
	public String getSeperator(Map<String, String> inputMap) {
		String file = inputMap.get("$capture.group.sep");
		return file.replace("\\t", "\t");
	}
	
	public boolean ifOnlyConsole(Map<String, String> inputMap) {
		String file = inputMap.get("$only.console");
		return BooleanUtils.toBoolean(file);
	}
	
	public Optional<File> getExportFile(Map<String, String> inputMap) {
		String file = inputMap.get("$export.file");
		
		if (Strings.isNullOrEmpty(file)) {
			return Optional.empty();
		}
		return Optional.of(new File(file));
	}

}
