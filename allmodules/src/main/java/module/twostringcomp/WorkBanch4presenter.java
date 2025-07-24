package module.twostringcomp;

import java.awt.Font;
import java.util.List;

import egps2.panels.graphicpro.CustomizeFontEnum;
import org.apache.commons.lang3.tuple.Pair;

import egps2.frame.gui.comp.SimplestWorkBench;
import egps2.EGPSProperties;

public class WorkBanch4presenter extends SimplestWorkBench {
	
	private GuiMain guiMain;

	public WorkBanch4presenter(GuiMain guiMain) {
		this.guiMain = guiMain;
		
	}
	
	
	@Override
	protected Font getDefaultFont() {
		Font cousineDefinedFont = CustomizeFontEnum.COUSINEREGULARFONTFAMILY.getCousineDefinedFont(Font.PLAIN, 14);
		return cousineDefinedFont;
	}

	@Override
	protected Pair<String, String> giveExample() {
		StringBuilder sBuilder = EGPSProperties.getSpecificationHeader();
		sBuilder.append("I am string1: abcde\n");
		sBuilder.append("I am string2: abgde");

		String left = sBuilder.toString();
		sBuilder.setLength(0);
		sBuilder.append("## The mutations will appear with the sequential order concord as input\n");
		sBuilder.append("I am string1: abcde\n");
		sBuilder.append("||||||||||| |||| ||\n");
		sBuilder.append("I am string2: abgde");

		return Pair.of(left, sBuilder.toString());
	}

	@Override
	protected void handle(List<String>  inputStrings) {
		//
		String ret = null;
		if (inputStrings.size() < 2) {
			ret = "Please input two strings to compare.";
		}else {
			StringBuilder sbBuilder = new StringBuilder();
			
			char[] str1Arr = inputStrings.get(0).toCharArray();
			char[] str2Arr = inputStrings.get(1).toCharArray();
			
			sbBuilder.append(str1Arr);
			sbBuilder.append("\n");
			
			int size = str1Arr.length < str2Arr.length ? str1Arr.length : str2Arr.length;
			
			for (int i = 0; i < size; i++) {
				if (str1Arr[i] == str2Arr[i]) {
					sbBuilder.append('|');
				}else {
					sbBuilder.append(' ');
				}
			}
			
			sbBuilder.append("\n");
			sbBuilder.append(str2Arr);
			ret = sbBuilder.toString();
		}

		bottomTextarea.setText( ret );
		guiMain.invokeTheFeatureMethod(0);
	}

}
