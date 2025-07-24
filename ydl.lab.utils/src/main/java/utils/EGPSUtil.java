package utils;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.google.common.base.Stopwatch;


public class EGPSUtil {

    /**
     * Open the local browser, if it fails, copy to the system clipboard and show a dialog!
     *
     * @param url: the URL to open
     */
    public static void openNativeBrowser(String url) throws URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(url);
                SwingUtilities.invokeLater(() -> {
                    try {
                        desktop.browse(uri);
                    } catch (Exception e) {
                        operationsIfFailed(url);
                    }
                });

            } else {
                operationsIfFailed(url);
            }
        } else {
            operationsIfFailed(url);
        }
    }

    public static void operationsIfFailed(String homeUrl) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(homeUrl);
        clipboard.setContents(tText, null);

        String title = "Network error";
        String msg = "URL redirection is failed. Network link has been copied to your clipboard."
                + " Please paste the link to your website browser. \n" + homeUrl;
        System.err.println(title + "\n" + msg);
    }

    /**
     *
     * @return unit : MB
     */
    public static long getAlreadyUsedJVMMemory() {
        // 1048576 = 1024 * 1024
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
    }

    public static void printUsedJVMMemory() {
        // Get the Runtime instance
        Runtime runtime = Runtime.getRuntime();

        // Get the maximum memory of the JVM
        long maxMemory = runtime.maxMemory();
        // Get the allocated memory of the JVM
        long allocatedMemory = runtime.totalMemory();
        // Get the free memory of the JVM
        long freeMemory = runtime.freeMemory();

        // Convert bytes to MB
        double maxMemoryMB = bytesToMB(maxMemory);
        double allocatedMemoryMB = bytesToMB(allocatedMemory);
        double freeMemoryMB = bytesToMB(freeMemory);
        double usedMemoryMB = bytesToMB(allocatedMemory - freeMemory);

        System.out.println("------------------------------------------------------------");
        System.out.println("Max memory (MB): " + maxMemoryMB);
        System.out.println("Allocated memory (MB): " + allocatedMemoryMB);
        System.out.println("Free memory (MB): " + freeMemoryMB);
        System.out.println("Used memory (MB): " + usedMemoryMB);
    }

    // 字节转换为MB的辅助方法
    private static double bytesToMB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }

    public static void obtainRunningTimeNewThread(Runnable run) {
        new Thread() {
            @Override
            public void run() {

                Stopwatch stopwatch = Stopwatch.createStarted();
                run.run();
                stopwatch.stop();

                long elapsedTimeInMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                System.out.printf("Code execution time: %d milliseconds\n", elapsedTimeInMillis);
            }
        }.start();
    }

    public static void obtainRunningTimeSecondBlocked(Runnable run) {

        // 创建一个 Stopwatch 实例
        Stopwatch stopwatch = Stopwatch.createStarted();
        run.run();
        stopwatch.stop();
        long elapsedTimeInMillis = stopwatch.elapsed(TimeUnit.SECONDS);

        System.out.printf("Code execution time: %d seconds\n", elapsedTimeInMillis);
    }


    public static long printTimeSinceStartAndPrintUsedMemory(long startTime) {
        long endTime = System.currentTimeMillis();

        long timeWithMs = endTime - startTime;

        long memory2 = Runtime.getRuntime().totalMemory() / 1024 / 1024
                - Runtime.getRuntime().freeMemory() / 1024 / 1024;
        System.out.println("Time since start is: " + timeWithMs + " ms, ( " + timeWithMs / 1000 + " s )! And " + memory2
                + " MB memory has used!");

        return endTime;
    }

    /**
     *
     * @title getTheMemoryUsageSize
     * @createdDate 2020-11-16 09:23
     * @lastModifiedDate 2020-11-16 09:23
     * @author yudalang
     * @since 1.7
     *
     * @param runningCode: the code to run
     * @return long : measured in bytes.
     */
    public static long getTheMemoryUsageSize(Runnable runningCode) {
        Runtime r = Runtime.getRuntime();
        r.gc();
        long startMem = r.freeMemory();
        runningCode.run();

        return Math.abs(startMem - r.freeMemory());
    }

    /**
     * Provide help documentation for CLI classes
     *
     * @return The usage header line
     */
    public static String getCLIUtilityName(Class<?> clz) {
        return "java -cp \"/path/to/eGPS_lib/*\" ".concat(clz.getName());
    }

}
