package hts.format.trans;

import com.google.common.base.Stopwatch;
import htsjdk.samtools.util.BlockCompressedOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class AutoDetectAndConvert {

    public static void convertToBGZip(String inputPath, String outputPath) throws IOException {

        Path input = Paths.get(inputPath);
        InputStream inputStream;

        int bufferSize = 100 * 1024 * 1024;
        // 自动检测是否为 gzip 文件
        if (inputPath.toLowerCase().endsWith(".gz")) {
            System.out.println("Detected GZIP file.");
            // 使用 GZIP 解压流读取
            inputStream = new BufferedInputStream(
                    new GZIPInputStream(new FileInputStream(input.toFile()),
                            bufferSize), bufferSize);
        } else {
            System.out.println("Detected plain text file.");
            // 普通文本文件
            inputStream = new BufferedInputStream(Files.newInputStream(input), bufferSize);
        }

        // 写入 BGZIP 文件

        try (OutputStream out = new BlockCompressedOutputStream(new File(outputPath))) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("BGZIP file saved to: " + outputPath);
    }

    public static void main(String[] args) {
        args = new String[]{
                "C:/Users/yudal/Documents/BaiduSyncdisk/博士后工作开展/带学生/韩珊珊/GenomeData/hs1.fa",
                "C:/Users/yudal/Documents/BaiduSyncdisk/博士后工作开展/带学生/韩珊珊/GenomeData/hs1.2.fa.bgz"
        };
        args = new String[]{
                "C:/Users/yudal/Documents/BaiduSyncdisk/博士后工作开展/带学生/韩珊珊/GenomeData/hs1.fa.gz",
                "C:/Users/yudal/Documents/BaiduSyncdisk/博士后工作开展/带学生/韩珊珊/GenomeData/hs1.fa.bgz"
        };
//        if (args.length != 2) {
//            System.out.println("Usage: java AutoDetectAndConvert <input_file> <output_bgzip_file>");
//            return;
//        }

        String inputFile = args[0];
        String outputFile = args[1];

        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            convertToBGZip(inputFile, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error during conversion: " + e.getMessage());
        }

        stopwatch.stop();

        System.out.println("Conversion completed in " + stopwatch);
    }
}