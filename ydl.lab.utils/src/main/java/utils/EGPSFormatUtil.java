package utils;

import java.text.DecimalFormat;

public class EGPSFormatUtil {
	
	private static DecimalFormat df = new DecimalFormat(",###,###");
	
	
	private EGPSFormatUtil(){
		
	}
	
	
	public static String addThousandSeparatorForInteger(int number) {
		return df.format(number);
	}

	public static String html32Concat(int fontsize, String content) {
		String htmlStr = """
				<html><font size="%d">%s</font></html>
				""";

		return htmlStr.formatted(fontsize, content);
	}
}
