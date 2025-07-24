package module.vennplot.util;

import java.util.ArrayList;
import java.util.List;

import egps2.utils.common.math.CheckedNumber;
import module.vennplot.model.Element;
import module.vennplot.model.SetItem;

public class InputDataParser {
	
	String errorString = "You do not import the data yet! Please import data!";

	public SetItem[] parseData(List<String> inputStrings, boolean isInputStringIsSimpleString) {

		
		int size = inputStrings.size();
		List<String> names = new ArrayList<>();
		List<String[]> contents = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			String text = inputStrings.get(i);

			text = text.trim();

			if (!text.isEmpty()) {
				String[] split = text.split("\t", -1);
				String[] dest = new String[split.length - 1];
				System.arraycopy(split, 1, dest, 0, dest.length);
				contents.add(dest);
				names.add(split[0]);
			}

		}

		int length = names.size();
		if (length == 0) {
			return null;
		}

		boolean ifGenomicRegion = !isInputStringIsSimpleString;
		SetItem[] setItems = new SetItem[length];

		for (int i = 0; i < length; i++) {
			SetItem setItem = new SetItem();
			String setName = names.get(i);
			setItem.setName(setName);

			String[] elementNames = contents.get(i);
			List<Element> temp = new ArrayList<>(elementNames.length);
			for (String string : elementNames) {
				string = string.trim();
				if (string.length() > 0) {
					Element element = new Element(string);
					element.setIfGenomicRegion(ifGenomicRegion);
					if (ifGenomicRegion) {
						String[] split = string.split(",");

						if (split.length != 3) {
							errorString = "Sorry! you input element: " + string + " in \"" + setName
									+ "\" set is not correct!";
							return null;
						}

						boolean p1 = CheckedNumber.isPositiveInteger(split[1], false);
						boolean p2 = CheckedNumber.isPositiveInteger(split[2], false);
						if (!p1 || !p2) {
							errorString = "Sorry! you input element: " + string + " in \"" + setName
									+ "\" set is not correct!";
							return null;
						}

						element.setRawStr(split[0]);
						element.setStart(Integer.parseInt(split[1]));
						element.setEnd(Integer.parseInt(split[2]));
					}
					temp.add(element);
				}
			}

			setItem.setSetElements(temp.toArray(new Element[0]));

			setItems[i] = setItem;
		}

		return setItems;

	}
	
	
	public String getErrorString() {
		return errorString;
	}

}
