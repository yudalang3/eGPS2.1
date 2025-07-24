package module.maplot.maplot.gui;

import java.util.Arrays;
import java.util.List;

import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.model.datatransfer.Result;
import utils.string.EGPSStringUtil;
import module.maplot.MainFace;
import module.maplot.maplot.CONSTANT;
import module.maplot.maplot.io.MAReadFile;
import module.maplot.maplot.model.OriginalData;

public class VOICM4MAPlot extends VersatileOpenInputClickAbstractGuiBase {

	
	private MainFace mainFace;

	public VOICM4MAPlot(MainFace mainFace) {
		this.mainFace = mainFace;
	}
	@Override
	public String getExampleText() {
		return getText();
	}
	
	String getText() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("## Line start with # is annotation").append("\n");
		stringBuilder.append("# This is the annotaiton").append("\n");
		stringBuilder.append("# Each column should be separated with tab key.").append("\n");
		stringBuilder.append("# Use ").append(CONSTANT.UID).append(" to specify the unique id\n");
		stringBuilder.append("# Use ").append(CONSTANT.COND1).append(" to specify the condition 1 columns\n");
		stringBuilder.append("# Use ").append(CONSTANT.COND2).append(" to specify the condition 2 columns id\n");
		stringBuilder.append("# Use ").append(CONSTANT.SIGNIFICANT).append(" to specify the significant column\n");
		stringBuilder.append("# No significant value is allowed. Assign it -1, default is -1\n");
		stringBuilder.append("# !!The first line most be the file header. See example for what is header line.\n");
		stringBuilder.append("## example here").append("\n");
		stringBuilder.append("$uniqueColumn_index=1").append("\n");
		stringBuilder.append("$condition1_index=2").append("\n");
		stringBuilder.append("$condition2_index=3").append("\n");
		stringBuilder.append("$significant_index=4").append("\n");
		stringBuilder.append("id	Ctrl	Trat	FDR").append("\n");
		stringBuilder.append("OS01T0100100-01	2.9	2.66	0.524535856").append("\n");
		stringBuilder.append("OS01T0100200-01	0.8	1.28	0.222003795").append("\n");
		stringBuilder.append("OS01T0100300-00	0.06	0.001	1").append("\n");
		stringBuilder.append("OS01T0100400-01	0.98	2.31	2.25E-07").append("\n");
		stringBuilder.append("OS01T0100466-00	0.001	0.09	0.653216704").append("\n");
		stringBuilder.append("OS01T0100500-01	5.46	7.5	0.00014463").append("\n");
		stringBuilder.append("OS01T0100600-01	1.85	1.75	0.849088714").append("\n");
		
		return stringBuilder.toString();
	}

	@Override
	public void execute(String inputs) {
		inputs = inputs.trim();
		String[] split = EGPSStringUtil.split(inputs, '\n');
		List<String> asList = Arrays.asList(split);
		Result<List<String>> ret = new Result<>(asList, true);

		if (ret.isSuccess()) {
			OriginalData dataModel = MAReadFile.importData(ret.getResult());

			MA_PlotDrawProperties drawProperties = new MA_PlotDrawProperties(dataModel);
			mainFace.initialize(drawProperties);
			mainFace.revalidate();
		}else {
			SwingDialog.showErrorMSGDialog("Import error", "Please check your import data.");
		}
		
	}

}
