package utils.datetime;

import java.util.HashMap;
import java.util.Map;

public class LocalDateHandler {
	
	private Map<String, String> mouthAbbreviationMap;
	
	public LocalDateHandler() {
		mouthAbbreviationMap = new HashMap<>(16, 1f);
		mouthAbbreviationMap.put("January", "Jan.");
		mouthAbbreviationMap.put("February", "Feb.");
		mouthAbbreviationMap.put("March", "Mar.");
		mouthAbbreviationMap.put("April", "Apr.");
		mouthAbbreviationMap.put("May", "May.");
		mouthAbbreviationMap.put("June", "Jun.");
		mouthAbbreviationMap.put("July", "Jul.");
		mouthAbbreviationMap.put("August", "Aug.");
		mouthAbbreviationMap.put("September", "Sept.");
		mouthAbbreviationMap.put("October", "Oct.");
		mouthAbbreviationMap.put("November", "Nov.");
		mouthAbbreviationMap.put("December", "Dec.");
	}

	public Map<String, String> getMouthAbbreviationMap() {
		return mouthAbbreviationMap;
	}

	public String getMonthNameAbbr(String name) {
		String string = mouthAbbreviationMap.get(name);
		if (string == null && !name.equalsIgnoreCase("no")) {
			System.err.println(getClass() + "  " + name);
		}

		return string;
	}
	
	
	public String getMonthNameAbbrAndYearMonthDay(String str) {
		int indexOf = str.indexOf(" ");
		String substring = str.substring(0, indexOf);

		String mouthNameAbbr = getMonthNameAbbr(substring);
		return mouthNameAbbr + str.substring(indexOf);
	}

	public String getMonthNameAbbrAndYearMonth(String str) {
		int indexOf = str.indexOf(" ");
		String substring = str.substring(0, indexOf);

		String mouthNameAbbr = getMonthNameAbbr(substring);

		int indexOfComma = str.indexOf(",");
		return mouthNameAbbr + str.substring(indexOfComma + 1);
	}

}
