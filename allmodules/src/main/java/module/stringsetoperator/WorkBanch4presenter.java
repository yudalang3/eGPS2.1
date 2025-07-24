package module.stringsetoperator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import egps2.panels.dialog.SwingDialog;
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
		sBuilder.append("# 1 for element to include; 0 for element to exclude\n");
		sBuilder.append("$parameter:operation:101\n");
		sBuilder.append("# For example: set1={1,2}; set2={2,3}\n");
		sBuilder.append("# If the operation is 11, the result set should be {2}. It means keeping the element included in set1, also included in set2.\n");
		sBuilder.append("# If the operation is 01, the result set should be {3}. It means keeping the element excluded in set1, but included in set2.\n");
		sBuilder.append("$setName=set1\n");
		sBuilder.append("a\n");
		sBuilder.append("e\n");
		sBuilder.append("c\n");
		sBuilder.append("f\n");
		sBuilder.append("$setName=set2\n");
		sBuilder.append("a\n");
		sBuilder.append("b\n");
		sBuilder.append("d\n");
		sBuilder.append("$setName=set3\n");
		sBuilder.append("a\n");
		sBuilder.append("e\n");
		sBuilder.append("f\n");
		sBuilder.append("d");

		String left = sBuilder.toString();
		sBuilder.setLength(0);
		sBuilder.append("e\n");
		sBuilder.append("f\n");

		return Pair.of(left, sBuilder.toString());
	}

	@Override
	protected void handle(List<String>  inputStrings) {
		final String operationString = "$parameter:operation:";
		final String setNameString = "$setName=";
		boolean[] operationArray = null;

		List<List<String>> listOfAllSet = new ArrayList<>();
		List<String> oneSetList = new LinkedList<>();

		for (String string : inputStrings) {

			if (string.startsWith(operationString)) {
				String substring = string.substring(operationString.length());
				int length = substring.length();
				operationArray = new boolean[length];
				for (int i = 0; i < length; i++) {
					operationArray[i] = ('0' != substring.charAt(i));
				}
				continue;
			}

			if (string.startsWith(setNameString)) {
				if (!oneSetList.isEmpty()) {
					listOfAllSet.add(oneSetList);
					oneSetList = new LinkedList<>();
				}
			} else {
				oneSetList.add(string);
			}
		}
		if (!oneSetList.isEmpty()) {
			listOfAllSet.add(oneSetList);
		}

		if (operationArray == null) {
			SwingDialog.showErrorMSGDialog("Input error",
					"The parameter string start with " + operationString + " should include.");
			return;
		}

		if (listOfAllSet.size() != operationArray.length) {
			SwingDialog.showErrorMSGDialog("Input error",
					"Please check your input parameter length and the number of sets.");
			return;
		}

		// 取交集
		Set<String> lastSet = new HashSet<>();
		for (int i = 0; i < operationArray.length; i++) {
			boolean op = operationArray[i];
			if (op) {
				if (lastSet.isEmpty()) {
					lastSet.addAll(listOfAllSet.get(i));
				} else {
					lastSet.retainAll(listOfAllSet.get(i));
				}
			}
		}

		for (int i = 0; i < operationArray.length; i++) {
			boolean op = operationArray[i];
			if (!op) {
				lastSet.removeAll(listOfAllSet.get(i));
			}
		}
		
		
		// present the results
		StringBuilder sBuilder = new StringBuilder();
		for (String string2 : lastSet) {
			sBuilder.append(string2).append("\n");
		}
		
		bottomTextarea.setText(sBuilder.toString());
		guiMain.invokeTheFeatureMethod(0);

	}

}
