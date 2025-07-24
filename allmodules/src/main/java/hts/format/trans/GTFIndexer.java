package hts.format.trans;

import htsjdk.samtools.util.BlockCompressedInputStream;
import htsjdk.samtools.util.BlockCompressedOutputStream;
import htsjdk.tribble.gff.Gff3Feature;
import htsjdk.tribble.index.Index;
import htsjdk.tribble.index.IndexFactory;
import htsjdk.tribble.gff.Gff3Codec;
import htsjdk.tribble.index.tabix.TabixFormat;
import htsjdk.tribble.index.tabix.TabixIndexCreator;
import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.LineIteratorImpl;

import java.io.*;

public class GTFIndexer {
    public static void main(String[] args) throws IOException {
//        generateGTFgz(args);
        generateIndex(args);
    }

    public static void generateGTFgz(String[] args) throws IOException {
        File inputFile = new File("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\带学生\\韩珊珊\\GenomeData\\hs1.ncbiRefSeq.sorted.gtf");
        File outputBgzFile = new File("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\带学生\\韩珊珊\\GenomeData\\hs1.ncbiRefSeq.sorted.gtf.gz");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BlockCompressedOutputStream bgzfOut = new BlockCompressedOutputStream(outputBgzFile)
        ) {
            String line;
            byte[] lineFeedBytes = "\n".getBytes();
            while ((line = reader.readLine()) != null) {
                bgzfOut.write(line.getBytes());
                bgzfOut.write(lineFeedBytes);
            }
        }

        System.out.println("BGZF-compressed file written to: " + outputBgzFile.getAbsolutePath());
    }

    public static void generateIndex(String[] args) throws IOException {
        File bgzfGtfFile = new File("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\带学生\\韩珊珊\\GenomeData\\hs1.ncbiRefSeq.sorted.gtf.gz");
        File tbiIndexFile = new File("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\带学生\\韩珊珊\\GenomeData\\hs1.ncbiRefSeq.sorted.gtf.gz.tai");

        // Initialize the GFF3 codec for parsing GTF lines
        Gff3Codec gff3Codec = new Gff3Codec();

        try (
                BlockCompressedInputStream bcis = new BlockCompressedInputStream(bgzfGtfFile);
                AsciiLineReader lineReader = new AsciiLineReader(bcis)
        ) {
            LineIteratorImpl lineIterator = new LineIteratorImpl(lineReader);
            TabixIndexCreator creator = new TabixIndexCreator(TabixFormat.GFF); // GTF 用 GFF 格式

            while (lineIterator.hasNext()) {
                long filePointer = bcis.getFilePointer();

                // Decode the GTF line into a Gff3Feature
                Gff3Feature feature = gff3Codec.decode(lineIterator);
                // Add the feature to the index
                creator.addFeature(feature, filePointer);
            }

            Index index = creator.finalizeIndex(bcis.getFilePointer());
            index.write(tbiIndexFile);

            System.out.println("Tabix index created: " + tbiIndexFile.getAbsolutePath());
        }
    }
}
