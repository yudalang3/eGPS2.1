package module.multiseq.alignment.view.io;

public class SaveFilterContinuousPDF extends SaveFilterPDF {

	@Override
	public String getDescription() {
		return "Continuous all (*.pdf)";
	}

	@Override
	public SaveFilterPDFDescription getDefaultFormatDescription() {
		return SaveFilterPDFDescription.CONTINUOUSALL;
	}

}
