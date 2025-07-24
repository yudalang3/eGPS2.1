package hts.format.trans;

import htsjdk.samtools.reference.FastaSequenceIndexCreator;
import htsjdk.samtools.SAMException;
import htsjdk.samtools.util.GZIIndex;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 一个使用 htsjdk 为 FASTA 文件（.fa 或 .fa.gz）创建索引的示例程序。
 */
public class FastaIndexer {

    public static void main(String[] args) {
        // 1. 检查是否提供了输入文件路径作为参数
//        if (args.length != 1) {
//            System.err.println("程序用法: java -jar FastaIndexer.jar <path/to/your/genome.fa>");
//            System.err.println("输入文件可以是普通的FASTA (.fa) 或 bgzip压缩的FASTA (.fa.gz)");
//            System.exit(1);
//        }

        // 2. 获取文件路径并创建一个File对象
//        String fastaPath = args[0];
        String fastaPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\带学生\\韩珊珊\\GenomeData\\hs1.fa.gz";
        File fastaFile = new File(fastaPath);


        // 3. 验证文件是否存在且可读
        if (!fastaFile.exists() || !fastaFile.isFile() || !fastaFile.canRead()) {
            System.err.println("错误: 无法读取输入文件，请检查文件是否存在或权限是否正确: " + fastaPath);
            System.exit(1);
        }
        
        // 重要提示：对于压缩文件，htsjdk 要求它必须是 "bgzip" (Block GZIP) 格式，
        // 而不是标准的 gzip。Bgzp 格式允许对文件进行随机访问，这是创建索引所必需的。
        // 普通的 gzip 文件无法被索引。

        System.out.println("正在为文件创建索引: " + fastaFile.getAbsolutePath());

        try {
            // 4. 使用 FastaSequenceIndexCreator 创建索引
            // 这是 htsjdk 的核心功能。它是一个静态方法，非常易于使用。
            // 它会自动处理普通文本的 .fa 文件和 bgzip 压缩的 .fa.gz 文件。

            FastaSequenceIndexCreator.create(fastaFile.toPath(),true);

            // 5. 报告成功信息
            System.out.println("索引创建成功!");

        } catch (FileNotFoundException e) {
            System.err.println("错误: 输入文件未找到。");
            e.printStackTrace();
            System.exit(1);
        } catch (SAMException e) {
            System.err.println("处理FASTA文件时发生错误。请确保文件格式正确。");
            System.err.println("如果文件是压缩的，请确保它是 bgzip 格式，而不是常规的 gzip。");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("创建索引时发生未知错误: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}