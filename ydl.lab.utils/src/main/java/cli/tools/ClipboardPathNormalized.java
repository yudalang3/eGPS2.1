package cli.tools;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Utility class to normalize file paths in the clipboard.
 *
 * This class reads text from the system clipboard, normalizes file paths by replacing
 * backslashes with forward slashes, and writes the result back to the clipboard.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class ClipboardPathNormalized {
    /**
     * Main method to execute the clipboard path normalization utility.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new ClipboardPathNormalized().run();
    }

    /**
     * Runs the clipboard normalization process.
     * Reads from clipboard, normalizes paths, and writes back to clipboard.
     */
    public void run() {
        // 从剪贴板读取字符串
        String clipboardText = getClipboardText();
        if (clipboardText != null) {
            System.out.println("Original string:");
            System.out.println(clipboardText);

            // 将 \\ 和 \ 替换为 /
            String processedText = clipboardText.replaceAll("\\\\+", "/");

            System.out.println("Processed string:");
            System.out.println(processedText);

            // 将处理后的文本写回剪贴板
            setClipboardText(processedText);
            System.out.println("Already paste to clipboard...");
        } else {
            System.out.println("No text in the clipboard...");
        }
    }

    /**
     * Gets text content from the system clipboard.
     * 
     * @return the text content of the clipboard, or null if no text is available
     */
    private String getClipboardText() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Transferable contents = clipboard.getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets text content to the system clipboard.
     * 
     * @param text the text to set in the clipboard
     */
    private void setClipboardText(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clipboard.setContents(stringSelection, null);
    }
}
