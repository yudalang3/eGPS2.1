package module.batchexcom;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import tsv.io.TSVReader;
import tsv.io.TSVWriter;

public class WindowPath2WSL {

	public static String convertPath(String windowsPath) {

		int length = windowsPath.length();
		if (windowsPath.charAt(0) == '"' && windowsPath.charAt(length - 1) == '"') {
			windowsPath = windowsPath.substring(1, length - 1);
		}

		// 将Windows路径中的反斜杠替换为正斜杠
		String linuxPath = windowsPath.replace("\\", "/");

		// 将盘符部分转换为Linux子系统路径
		Pattern pattern = Pattern.compile("^([a-zA-Z]):");
		Matcher matcher = pattern.matcher(linuxPath);

		if (matcher.find()) {
			String driveLetter = matcher.group(1).toLowerCase();
			linuxPath = linuxPath.replaceAll("^[a-zA-Z]:", String.format("/mnt/%s", driveLetter));
		}

		return linuxPath;
	}

	public static void main(String[] args) throws IOException {
		int length = args.length;

		if (length == 1 && Objects.equals("-h", args[0])) {
			System.err.println(
					"Usage: 1 args direct translate, 0 from clipborad, 2 first is the tsv file second is the colunmn name to translate.");
			return;
		}

		if (length == 0) {
			String clipboardContent = null;
			try {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				clipboardContent = (String) toolkit.getSystemClipboard().getData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException e) {
				System.out.println("Can not get clipboard content: " + e.getMessage());
			}


			String linuxPath = convertPath(clipboardContent);
			System.out.println("Linux subsystem path: ");
			System.out.println(linuxPath);
		} else if (length == 1) {
			String windowsPath = args[0];
			String linuxPath = convertPath(windowsPath);
			System.out.println("Linux subsystem path: ");
			System.out.println(linuxPath);
		} else {
			String key = args[1];
			String tsvFile = args[0];
			Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(tsvFile);
			List<String> list = asKey2ListMap.get(key);
			if (list == null) {
				throw new IllegalArgumentException("Your input key not found in the tsv file.");
			}

			List<String> output = new ArrayList<>();

			for (String string : list) {
				String convertPath = convertPath(string);
				output.add(convertPath);
			}

			asKey2ListMap.put(key, output);

			TSVWriter.write(asKey2ListMap, addCopySuffix(tsvFile));

		}
	}

	public static String addCopySuffix(String filePath) {
		// 获取路径和文件名（不含扩展名）
		String baseName = FilenameUtils.getBaseName(filePath);
		// 获取路径和扩展名
		String extension = FilenameUtils.getExtension(filePath);
		// 获取完整路径（不含文件名）
		String fullPathNoEndSeparator = FilenameUtils.getFullPathNoEndSeparator(filePath);

		// 组合新的文件名
		String newFileName = baseName + ".copy." + extension;
		// 组合新的完整路径
		String newFilePath = fullPathNoEndSeparator + "/" + newFileName;

		return newFilePath;
	}
}
