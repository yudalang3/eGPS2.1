package primary.struct.display.drawer;

import fasta.io.FastaReader;
import graphic.engine.colors.EGPSColors;
import pfam.parse.PfamScanRecord;
import primary.struct.display.data.DomainV2;
import primary.struct.display.data.GeneDataV2;
import primary.struct.display.data.GeneV2;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LauncherDomainVis {

    public static void main(String[] args) throws IOException {
        String fastaFile = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Wnt_QuerySequences\\compoentSet4\\set4\\wntPathway_querySeq_compoentSet4.fa";
        String domainFile = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Wnt_QuerySequences\\compoentSet4\\set4\\pfam_scan_wntComp_domain_rawOut.txt";
        if (args.length != 0) {
            fastaFile = args[0];
            domainFile = args[1];
        }

        LauncherDomainVis launcherDomainVis = new LauncherDomainVis();
        GeneDataV2 run = launcherDomainVis.run(fastaFile, domainFile);

        JFrame frame = new JFrame("Draggable Rectangle Drawer");
        PrimaryStructDrawer panel = new PrimaryStructDrawer(run);
        panel.setPreferredSize(new Dimension(2000, 5000));
        JScrollPane jScrollPane = new JScrollPane(panel);
        frame.add(jScrollPane);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public GeneDataV2 run(String fastaFile, String domainFile) throws IOException {
        LinkedHashMap<String, String> map1 = FastaReader.readFastaSequence(fastaFile);

        Map<String, List<PfamScanRecord>> seqId2PfamDBEntriesMap = PfamScanRecord.parseHmmerScanOut(domainFile);

        GeneDataV2 geneDataV2 = new GeneDataV2();
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String seqId = entry.getKey();
            int seqLength = entry.getValue().length();

            GeneV2 geneV2 = new GeneV2();
            geneV2.setGeneName(seqId);
            geneV2.setLength(seqLength);

            List<PfamScanRecord> pfamDBEntries = seqId2PfamDBEntriesMap.get(seqId);
            if (pfamDBEntries == null) {

            } else {
                for (PfamScanRecord pfamDBEntry : pfamDBEntries) {
                    DomainV2 domainV2 = new DomainV2();
                    domainV2.setName(pfamDBEntry.getHmmName());
                    domainV2.setStart(pfamDBEntry.getAlignmentStart());
                    domainV2.setEnd(pfamDBEntry.getAlignmentEnd());
                    Color color = EGPSColors.getEyeFriendlyColor();
                    domainV2.setColor(color);
                    geneV2.getDomains().add(domainV2);
                }
            }

            geneDataV2.getGenes().put(seqId, geneV2);

        }
        return geneDataV2;

    }
}
