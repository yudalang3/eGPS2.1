package module.multiseq.aligner.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import egps2.frame.MainFrameProperties;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.aligner.MultipleSeqAlignerMain;
import module.multiseq.aligner.cli.Mafft2AlignmentCli;
import module.multiseq.alignment.view.Launcher4ModuleLoader;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import egps2.modulei.RunningTask;

/**
 * 
 * @ClassName Mafft2Alignment
 * 
 * @author mhl
 * 
 * @Date Created on:2019-06-05 13:23
 * 
 */
public class Mafft2AlignmentGui extends Mafft2AlignmentCli implements RunningTask {

	private MultipleSeqAlignerMain alignmentMain;

	public Mafft2AlignmentGui(List<String> parameters, MultipleSeqAlignerMain alignmentMain) {
		this(parameters);
		this.alignmentMain = alignmentMain;
	}

	public Mafft2AlignmentGui(List<String> parameters) {
		super(parameters);
	}

	@Override
	public int processNext() throws Exception {
		processNextDefaultMAFFT(null);
		return PROGRESS_FINSHED;
	}

	@Override
	public void actionsBeforeStart() throws Exception {
		seqs = new ArrayList<String>();
		seqNames = new ArrayList<String>();
	}

	@Override
	public void actionsAfterFinished() throws Exception {
		if (seqs == null) {
			throw new InputMismatchException("Sorry, can not get the aligned sequence, please tell the developers.");
		}
		// 导入数据
		List<SequenceI> vector = new ArrayList<>();
		for (int i = 0; i < seqs.size(); i++) {
			Sequence sequence = new Sequence(seqNames.get(i), seqs.get(i));
			vector.add(sequence);
		}
		// 生成一个封装类
		final SequenceDataForAViewer sequenceData = new SequenceDataForAViewer(vector);
		// New出来一个Loader实例
		Launcher4ModuleLoader launcher4ModuleLoader = new Launcher4ModuleLoader();
		// 设置模块是怎么打开的，数据是哪里来的提示符
		launcher4ModuleLoader.setModuleLaunchWay("This remnant is launched by the MAFFT to alignment remnant");
		launcher4ModuleLoader.setWhatDataInvoked("The data source is from the file: ".concat(getFileName()));
		// 把包含数据的封装类实例导入
		launcher4ModuleLoader.setSequenceDataForAViewer(sequenceData);
		// 让主框架来加载这个类
		MainFrameProperties.loadTheModuleFromIModuleLoader(launcher4ModuleLoader);
	}

	private String getFileName() {
		String tmp = parameters.get(parameters.size() - 1);
		File file = new File(tmp);
		return file.getName();
	}

	@Override
	public boolean isTimeCanEstimate() {
		return false;
	}

}
