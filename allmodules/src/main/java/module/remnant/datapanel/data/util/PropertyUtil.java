package module.remnant.datapanel.data.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class PropertyUtil {
	
	/**
	 * 用来持久化存储!
	 * @param targetKey
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getPropertyString(String targetKey,File file) throws IOException {
			List<String> readLines = FileUtils.readLines(file,StandardCharsets.UTF_8);
			for (String string : readLines) {
				int index = string.indexOf(targetKey);
				if ( index != -1) {
					return string.substring(index);
				}
			}
		
		return "";
	}
	public static void savePropertyString(String targetKey,String targetValue,File file)  {
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			 out.write(targetKey); 
			 out.write(targetValue); 
		     out.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}  
       
	}

}
