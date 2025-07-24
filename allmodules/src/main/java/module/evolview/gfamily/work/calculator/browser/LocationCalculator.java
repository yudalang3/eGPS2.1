package module.evolview.gfamily.work.calculator.browser;

/**
 * 通过计算每个Track的位置信息,进行绘制
 *
 * @param <T> 绘制界面返回的具体模块的类型
 * @author mhl
 */
public interface LocationCalculator<T> {
    int BLINK_LEFT_SPACE_LENGTH = 150;//左侧空白区宽度
    //int BLINKRIGHTSPACELENGTH = BLINKLEFTSPACELENGTH / 2;
    int BLINK_RIGHT_SPACE_LENGTH = 30;
    int BLINKTOPSPACELENGTH = 20;
    String GENOMESTRUCTURE = "Show/Hide genome structure";
    String KEYDOMAINS = "Show/Hide key domains";
    String SNPFREQUENCY = "Show/Hide SNP frequency";
    String COVSEQIDENTITY = "Show/Hide CoV seq identity";
    String COVALIGNMENT = "Show/Hide CoV alignment";
    String PRIMERSETS = "Show/Hide primer sets";
    String GENOMESTRUCTURETRACK = "Gene struct";
    String SEQENCE_ELEMENT_TRACK_NAME = "Sequence element";
    String SNPFREQUENCYTRACK = "Allele freq";
    String COVSEQIDENTITYTRACK = "Seq similarity";
    String COVALIGNMENTTRACK = "Gene alignment";
    String PRIMERSETSTRACK = "Primer sets";


}
