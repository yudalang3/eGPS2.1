package module.multiseq.alignment.view.io;

import java.awt.Dialog;
import java.io.File;

import javax.swing.SwingUtilities;

import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import msaoperator.io.seqFormat.MSA_DATA_FORMAT;
import module.multiseq.alignment.view.VOICM4AlignmentView;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

public class VOICE4AlignmentView extends VersatileOpenInputClickAbstractGuiBase {

	private VOICM4AlignmentView voicmAlignViewEGPS2;
	
	private AligViewAbstractParamsAssignerAndParser mapProducer = new AligViewAbstractParamsAssignerAndParser();

	public VOICE4AlignmentView() {

	}

	public VOICE4AlignmentView(VOICM4AlignmentView voicmAlignViewEGPS2) {
		this.voicmAlignViewEGPS2 = voicmAlignViewEGPS2;
	}

	@Override
	public String getExampleText() {
		return mapProducer.getExampleString();
	}

	@Override
	public void execute(String inputs) {
		AlignmentImportBean object = mapProducer.getImportBean(inputs);

		String upperCase = object.getFileFormat();
		MSA_DATA_FORMAT dataFormat = MSA_DATA_FORMAT.getFormatAccording2name(upperCase);

		File tempFile = new File(object.getFilePath());
		SequenceDataForAViewer parseData = voicmAlignViewEGPS2.parseData(tempFile, dataFormat);
		voicmAlignViewEGPS2.loadOneAlignmentViewTab(parseData, "Import data from the voicm content in file: ".concat(tempFile.getName()));
		
		
		Dialog root = (Dialog) SwingUtilities.getRoot(voicmAlignViewEGPS2);
		root.dispose();
	}

}
