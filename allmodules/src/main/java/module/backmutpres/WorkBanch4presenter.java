package module.backmutpres;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import egps2.frame.gui.comp.SimplestWorkBench;
import egps2.EGPSProperties;

public class WorkBanch4presenter extends SimplestWorkBench {
	
	private GuiMain guiMain;

	public WorkBanch4presenter(GuiMain guiMain) {
		this.guiMain = guiMain;
	}

	@Override
	protected Pair<String, String> giveExample() {
		StringBuilder sBuilder = EGPSProperties.getSpecificationHeader();
		sBuilder.append("# Format: Mutation should split with ,\n");
		sBuilder.append("# This is first mutation string, corresponding to ####result1####\n");
		sBuilder.append(
				"C3037T,G28881A,G28882A,G28883C,C28883G,A28882G,A28881G,A28282T,T28281A,G28883C,-28271A");
		sBuilder.append("\n# This is second mutation string, corresponding to ####result2####\n");
		sBuilder.append("C3037T,C8874T,T3037C");

		String left = sBuilder.toString();
		sBuilder.setLength(0);
		sBuilder.append("## The mutations will appear with the sequential order concord as input\n");
		sBuilder.append("## Each row correspondings to one mutation, mutations happen on the same position will on different columns\n");
		sBuilder.append("#################################### Result: 1 ####################################\n");
		sBuilder.append("C3037T\n");
		sBuilder.append("G28881A\n");
		sBuilder.append("G28882A\n");
		sBuilder.append("G28883C\n");
		sBuilder.append("                  C28883G\n");
		sBuilder.append("                  A28882G\n");
		sBuilder.append("                  A28881G\n");
		sBuilder.append("A28282T\n");
		sBuilder.append("T28281A\n");
		sBuilder.append("                                    G28883C\n");
		sBuilder.append("-28271A\n");
		sBuilder.append("#################################### Result: 2 ####################################\n");
		sBuilder.append("C3037T\n");
		sBuilder.append("C8874T\n");
		sBuilder.append("                  T3037C");

		return Pair.of(left, sBuilder.toString());
	}

	@Override
	protected void handle(List<String>  inputStrings) {
		BackMutsPresenter backMutsPresenter = new BackMutsPresenter();
		backMutsPresenter.setFullInformation(true);

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("## The mutations will appear with the sequential order concord as input\n");
		sBuilder.append("## Each row correspondings to one mutation, mutations happen on the same position will on different columns\n");
		
		int index = 1;
		for (String string : inputStrings) {
			String presentWithComma = backMutsPresenter.presentWithComma(string);
			sBuilder.append("#################################### Result: " + String.valueOf(index)
					+ " ####################################\n");
			sBuilder.append(presentWithComma);

			index++;
		}

		sBuilder.toString();

		bottomTextarea.setText(sBuilder.toString());
		guiMain.invokeTheFeatureMethod(0);
	}

}
