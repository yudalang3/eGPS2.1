package module.regexExtract;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import egps2.panels.dialog.SwingDialog;
import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import egps2.builtin.modules.voice.bean.VoiceExampleGenerator;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class VOICM4RegexExtract extends VersatileOpenInputClickAbstractGuiBase {

	private GuiMain guiMain;

	private VoiceExampleGenerator generator ;
	private ImportAbstractMap4VOICE parameterMap = new ImportAbstractMap4VOICE();

	public VOICM4RegexExtract(GuiMain guiMain) {
		this.guiMain = guiMain;
		
		generator = parameterMap.getExampleGenerator();
	}

	@Override
	public String getExampleText() {
		String example = generator.generateExample(parameterMap.getRequiredParams());
		return example;
	}

	@Override
	public void execute(String inputs) {

		List<String> output = new ArrayList<>();

		VoiceParameterParser parser = new VoiceParameterParser();

		Map<String, String> map = parser.parseLongSingleString4simplifiedMap(inputs);

		output.add("Start to read the contents from input ...");
		guiMain.setText4Console(output);

		List<String> outputContent = new LinkedList<>();

		File importFile = parameterMap.getImportFile(map);

		String regex = parameterMap.getRegxExpressionString(map);
		String seperator = parameterMap.getSeperator(map);

		StringBuilder sb = new StringBuilder();
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regex);
		try {
			List<String> lines = FileUtils.readLines(importFile, StandardCharsets.UTF_8);
			for (String text : lines) {

				// 创建matcher来查找字符串中的匹配项
				Matcher matcher = pattern.matcher(text);
				// 遍历所有匹配项
				if (matcher.matches()) {
					int groupCount = matcher.groupCount(); // 获取总的捕获组数

					if (groupCount == 1) {
						outputContent.add(matcher.group(1));
					} else {
						sb.setLength(0);
						sb.append(matcher.group(1));
						for (int i = 2; i <= groupCount; i++) { // 访问每一个捕获组
							sb.append(seperator).append(matcher.group(i));
						}
						outputContent.add(sb.toString());
					}

				}else {
					outputContent.add("");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		guiMain.setText4Console(output);
		

		if (parameterMap.ifOnlyConsole(map)) {
			guiMain.setText4Console(outputContent);
		} else {
			Optional<File> exportFileOpt = parameterMap.getExportFile(map);
			if (!exportFileOpt.isPresent()) {
				SwingDialog.showErrorMSGDialog("Import error",
						"You set the parameter to be export to file ($only.console=F).\nWhile the export file path is empty.");
				return;
			}
			try {
				File exportFile = exportFileOpt.get();
				FileUtils.forceMkdirParent(exportFile);
				FileUtils.writeLines(exportFile, outputContent);
			} catch (IOException e) {
				e.printStackTrace();
			}
			output.add("Successfully executed.");
			guiMain.setText4Console(output);
		}
		
		guiMain.recordTheFunctionInvoke();
		

		SwingDialog.showInfoMSGDialog("Running Successfully", "The program successfully executed.");
	}

}
