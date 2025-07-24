package module.localblast.gui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.JTextArea;

public class Util {

	public static void printMessage(final InputStream input, boolean isError, JTextArea textArea_normal,
			JTextArea textArea_error) {
		new Thread(() -> {
			Reader reader = new InputStreamReader(input);
			BufferedReader bf = new BufferedReader(reader);
			String line = null;
			try {
				while ((line = bf.readLine()) != null) {
					if (isError) {
						textArea_error.append(line + "\n");
						// System.err.println(line);
					} else {
						textArea_normal.append(line + "\n");
						// System.out.println(line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void printMessage(final InputStream input, JTextArea textArea_normal) {
//		new Thread(() -> {
			Reader reader = new InputStreamReader(input);
			BufferedReader bf = new BufferedReader(reader);
			String line = null;
			try {
				while ((line = bf.readLine()) != null) {
					textArea_normal.append(line);
					textArea_normal.append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
//		}).start();
	}

}
