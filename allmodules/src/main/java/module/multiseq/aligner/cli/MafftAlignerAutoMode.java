package module.multiseq.aligner.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * <h1>目的</h1>
 * <p>
 *  
 * Invoke MAFFT software to align multiple sequence
 *  </p>
 *  
 * <h1>输入/输出</h1>
 * 
 * <p>
 * 输入有两种模式，如果提供了MAFFT软件的地址，那么就用这个地址。否则用默认的地址，默认地址适合像eGPS一样直接包软件。
 * 再输入未必对的fasta文件。
 * 输出为比对过的文件，默认fasta格式。
 * </p>
 * 
 * <h1>使用方法</h1>
 * 
 * <blockquote>
 * String softwarePath = "C:/Users/yudal/Documents/software/mafft-win/mafft.bat";
 * MafftAlignerAutoMode mode = new MafftAlignerAutoMode(softwarePath);
 * mode.getAlignment("/Users/yudalang/workDir/test/beforeAlign.fas.align.fas");
 * List<String> seqNames = mode.getSeqNames();
 * List<String> sequences = mode.getSequences();
 * 
 * </blockquote>
 * 
 * <h1>注意点</h1>
 * <ol>
 * <li>
 * 在调用主要方法 <code>getAlignment()</code>时不要忘记设置一些属性
 * </li>
 * </ol>
 * 
 * @implSpec
 * 就是将MAFFT包了一下，通过外部调用来运行，执行结果没有放到文件中。而是直接传到内存里面了。
 * 
 * @author yudal
 *
 */
public class MafftAlignerAutoMode {
	List<String> sequences;
	List<String> seqNames;
	List<String> parameters = new ArrayList<String>(Arrays.asList("--auto"));
	Mafft2AlignmentCli mafft2Alignment = new Mafft2AlignmentCli(parameters);

	String mafftAlignerPath;
	final boolean ifAlignerPathProvided;
	boolean showErrorMessage = false;

	public MafftAlignerAutoMode() {
		ifAlignerPathProvided = false;
	}

	public MafftAlignerAutoMode(String mafftAlignerPath) {
		ifAlignerPathProvided = true;
		this.mafftAlignerPath = mafftAlignerPath;
	}

	/**
	 * Run by provide the software path
	 * 
	 * @param inputPath
	 * @throws Exception
	 */
	public void getAlignment(String inputPath) throws Exception {
		mafft2Alignment.actionsBeforeStart();
		mafft2Alignment.setShowErrorMessage(showErrorMessage);

		if (ifAlignerPathProvided) {
			mafft2Alignment.processNextWithMAFFTPath(mafftAlignerPath, inputPath);
		} else {
			mafft2Alignment.processNextDefaultMAFFT(inputPath);
		}

		seqNames = mafft2Alignment.getSeqNames();
		sequences = mafft2Alignment.getSeqs();
	}

	public List<String> getSeqNames() {
		return seqNames;
	}

	public List<String> getSequences() {
		return sequences;
	}

	public static void main(String[] args) throws Exception {
		
		String softwarePath = "C:/Users/yudal/Documents/software/mafft-win/mafft.bat";
		
		MafftAlignerAutoMode mode = new MafftAlignerAutoMode(softwarePath);

		mode.getAlignment("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\CK1\\protein\\all.CK1.prot.fas");
//		List<String> seqNames = mode.getSeqNames();
//		System.out.println(seqNames.size());
//		List<String> sequences = mode.getSequences();
//		for (String string : sequences) {
//			System.out.println(string);
//		}
//		System.out.println(sequences.size());
		
	}

}
