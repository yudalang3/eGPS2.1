package module.multiseq.alignment.view.io;

public class SaveFilterLocalViewPDF extends SaveFilterPDF {

	@Override
	public String getDescription() {
		return "Local view (*.pdf)";
	}

	@Override
	public SaveFilterPDFDescription getDefaultFormatDescription() {
		return SaveFilterPDFDescription.LOCALVIEW;
	}

}
