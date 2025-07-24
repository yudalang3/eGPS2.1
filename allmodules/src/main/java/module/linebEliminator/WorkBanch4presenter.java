package module.linebEliminator;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import egps2.frame.gui.comp.SimplestWorkBench;
import egps2.EGPSProperties;

@SuppressWarnings("serial")
public class WorkBanch4presenter extends SimplestWorkBench {

	
	private GuiMain guiMain;

	public WorkBanch4presenter(GuiMain guiMain) {
		bottomTextarea.setLineWrap(true);
		this.guiMain = guiMain;
	}

	@Override
	protected Pair<String, String> giveExample() {
		StringBuilder sBuilder = EGPSProperties.getSpecificationHeader();
		sBuilder.append("Collectively, our results imply that tumorigenesis can proceed from \n");
		sBuilder.append("multiple welldifferentiated or progenitor-like states, andthat their neoplastic\n");
		sBuilder.append("progression is not dictated solely by cell-intrinsic determinants (Krasgene\n");
		sBuilder.append("mutation), but rather is affected by inflammatory signals that epigenetically\n");
		sBuilder.append("prime them toward diverse fates that can be predicted early\n");
		sBuilder.append("in disease progression.\n");

		String left = sBuilder.toString();
		sBuilder.setLength(0);
		sBuilder.append("Collectively, our results imply that tumorigenesis can proceed from multiple welldifferentiated or progenitor-like states, and that their neoplastic progression is not dictated solely by cell-intrinsic determinants (Kras gene mutation), but rather is affected by inflammatory signals that epigenetically prime them toward diverse fates that can be predicted early in disease progression.");

		return Pair.of(left, sBuilder.toString());
	}

	@Override
	protected void handle(List<String>  inputStrings) {
		StringBuilder sBuilder = new StringBuilder();
		
		final char writeSpace = ' ';
		final char dotChar = '.';

		for (String string : inputStrings) {

			char lastChar = string.charAt(string.length() - 1);
			if (lastChar == dotChar) {
				sBuilder.append(string).append('\n');
			}else if (lastChar == writeSpace){
				sBuilder.append(string);
			}else {
				sBuilder.append(string).append(writeSpace);
			}
		}

		bottomTextarea.setText(sBuilder.toString());
		guiMain.invokeTheFeatureMethod(0);
	}


}
