package module.multiseq.alignment.view.io;

public class SaveFilterInterleavedPDF extends SaveFilterPDF {

	@Override
	public SaveFilterPDFDescription getDefaultFormatDescription() {
		return SaveFilterPDFDescription.INTERLEAVEDALL;
	}

	@Override
	public String getDescription() {
		return "Interleaved all (*.pdf)";
	}

}
