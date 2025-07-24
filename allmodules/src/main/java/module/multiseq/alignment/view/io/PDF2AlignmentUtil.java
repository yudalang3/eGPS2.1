package module.multiseq.alignment.view.io;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JPanel;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.gui.AlignmentViewContinuousRightPanel;
import module.multiseq.alignment.view.gui.AlignmentViewInterLeavedPanel;
import module.multiseq.alignment.view.gui.VisulizationDataProperty;
import egps2.modulei.RunningTask;

public class PDF2AlignmentUtil {

	private AlignmentViewMain alignmentViewMain;

	public void setAlignmentViewMain(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewMain = alignmentViewMain;
	}

	public void exportDataWithSelectedFormat(String path, String defaultSuffix, SaveFilterPDFDescription description)
			throws Exception {
		File outputFile = new File(path + "." + defaultSuffix);

		switch (description) {
		case CONTINUOUSALL:
			exportDataWithInterleavedAllCopy(outputFile);
			break;
		case INTERLEAVEDALL:
			exportDataWithInterleavedAll(outputFile);
			break;
		case LOCALVIEW:
			exportDataWithLocalView(outputFile);
			break;

		default:
			break;
		}

	}

	private void exportDataWithContinuousAll(File outputFile) throws Exception {

	}

	private void exportDataWithLocalView(File outputFile) throws Exception {
		OutputStream stream = new FileOutputStream(outputFile);

		Rectangle2D bounds = component.getBounds();

		if (component instanceof AlignmentViewContinuousRightPanel) {

			AlignmentViewContinuousRightPanel alignmentViewInnerRightPanel = (AlignmentViewContinuousRightPanel) component;

			alignmentViewInnerRightPanel.getBottomJScrollBar().setVisible(false);

			alignmentViewInnerRightPanel.getRightJScrollBar().setVisible(false);

		} else if (component instanceof AlignmentViewInterLeavedPanel) {

			AlignmentViewInterLeavedPanel alignmentViewInnerRightPanel = (AlignmentViewInterLeavedPanel) component;

			alignmentViewInnerRightPanel.getRightJScrollBar().setVisible(false);

		}
		Document document = new Document(new Rectangle((float) bounds.getWidth(), (float) bounds.getHeight() + 5));
		PdfWriter writer = PdfWriter.getInstance(document, stream);

		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate tp = cb.createTemplate((float) bounds.getWidth(), (float) bounds.getHeight() + 5);
		Graphics2D g2d = tp.createGraphics((float) bounds.getWidth(), (float) bounds.getHeight() + 5,
				new DefaultFontMapper());
		component.print(g2d);
		g2d.dispose();
		cb.addTemplate(tp, 0, 0);
		document.close();
		if (component instanceof AlignmentViewContinuousRightPanel) {

			AlignmentViewContinuousRightPanel alignmentViewInnerRightPanel = (AlignmentViewContinuousRightPanel) component;

			alignmentViewInnerRightPanel.getBottomJScrollBar().setVisible(true);

			alignmentViewInnerRightPanel.getRightJScrollBar().setVisible(true);

		} else if (component instanceof AlignmentViewInterLeavedPanel) {

			AlignmentViewInterLeavedPanel alignmentViewInnerRightPanel = (AlignmentViewInterLeavedPanel) component;

			alignmentViewInnerRightPanel.getRightJScrollBar().setVisible(true);

		}

	}

	private void exportDataWithInterleavedAll(File outputFile) throws Exception {

		DataWithInterleavedAllWriteFile task = new DataWithInterleavedAllWriteFile(outputFile);
		alignmentViewMain.registerRunningTask(task);

//		EGPSThreadUtil.quicklyRunThread(alignmentViewMain, task);

//		AlignmentViewPort alignmentViewPort = alignmentViewMain.getAlignmentViewPort();
//
//		int charHeight = alignmentViewPort.getCharHeight();
//
//		int charWidth = alignmentViewPort.getCharWidth();
//
//		JPanel jPanel = new JPanel();
//
//		int yOf = jPanel.getFontMetrics(alignmentViewPort.getFont()).getDescent();
//
//		int scaleHeight = yOf * 2 + charHeight - yOf;
//
//		int totalSequenceCount = alignmentViewPort.getTotalSequenceCount();
//
//		int sequenceHeight = totalSequenceCount * charHeight;
//
//		int sequenceAnnotationHeighty = 50;
//
//		int blockDistance = 20;
//
//		int blockHeight = scaleHeight + sequenceHeight + charHeight + sequenceAnnotationHeighty + charHeight
//				+ blockDistance;
//
//		Rectangle a4 = PageSize.A4;
//
//		float pageHeight = a4.getHeight();
//
//		int pageShowBlockCount = (int) Math.ceil(pageHeight / blockHeight);
//
//		int width = ((int) a4.getWidth()) - alignmentViewPort.getBaseNameLenght() - 50;
//
//		int showDataLength = (int) Math.floor(width * 1.0 / charWidth);
//
//		if (showDataLength > 10 && showDataLength % 10 != 0) {
//
//			showDataLength = showDataLength / 10 * 10;
//		}
//		int totalSequenceLength = alignmentViewPort.getTotalSequenceLength();
//
//		int blockCount = (int) Math.ceil(totalSequenceLength * 1.0 / showDataLength);
//
//		int pageCount = (int) Math.ceil(blockCount * 1.0 / pageShowBlockCount);
//
//		OutputStream stream = new FileOutputStream(outputFile);
//
//		Document document = new Document(new Rectangle(a4.getWidth(), pageShowBlockCount * blockHeight));
//
//		PdfWriter writer = PdfWriter.getInstance(document, stream);
//
//		document.open();
//
//		for (int i = 0; i < pageCount; i++) {
//
//			document.newPage();
//
//			for (int j = 0; j < pageShowBlockCount; j++) {
//				PdfContentByte cb = writer.getDirectContent();
//
//				PdfTemplate tp = cb.createTemplate(a4.getWidth(), blockHeight);
//
//				int currentShowBlock = i * pageShowBlockCount + j;
//
//				int currentStartRes = currentShowBlock * showDataLength;
//
//				int startRes = currentStartRes > totalSequenceLength ? totalSequenceLength : currentStartRes;
//
//				int currentEndRes = startRes + showDataLength;
//
//				int endRes = currentEndRes > totalSequenceLength ? totalSequenceLength : currentEndRes;
//
//				if (startRes == endRes) {
//					break;
//				}
//
//				PrintAlignmentPanel printAlignmentPanel = new PrintAlignmentPanel(alignmentViewMain, alignmentViewPort);
//
//				printAlignmentPanel.setCharWidth(charWidth);
//
//				printAlignmentPanel.setCharHeight(charHeight);
//
//				printAlignmentPanel.setStartRes(startRes);
//
//				printAlignmentPanel.setEndRes(endRes);
//
//				printAlignmentPanel.setPreferredSize(new Dimension((int) a4.getWidth(), blockHeight));
//
//				Graphics2D g2d = tp.createGraphics(a4.getWidth(), (float) blockHeight, new DefaultFontMapper());
//
//				printAlignmentPanel.print(g2d);
//
//				float index = (pageShowBlockCount - 1 - j) * blockHeight;
//
//				cb.addTemplate(tp, 0, index);
//
//				g2d.dispose();
//			}
//		}
//
//		document.close();
	}

	private void exportDataWithInterleavedAllCopy(File outputFile) throws Exception {

		VisulizationDataProperty alignmentViewPort = alignmentViewMain.getAlignmentViewPort();

		int charHeight = alignmentViewPort.getCharHeight();

		int charWidth = alignmentViewPort.getCharWidth();

		JPanel jPanel = new JPanel();

		int yOf = jPanel.getFontMetrics(alignmentViewPort.getFont()).getDescent();

		int scaleHeight = yOf * 2 + charHeight - yOf;

		int totalSequenceCount = alignmentViewPort.getTotalSequenceCount();

		int sequenceHeight = totalSequenceCount * charHeight;

		int sequenceAnnotationHeighty = 50;

		int blockDistance = 20;

		int blockHeight = scaleHeight + sequenceHeight + charHeight + sequenceAnnotationHeighty + charHeight
				+ blockDistance;

		Rectangle a4 = PageSize.A4;

		float pageHeight = a4.getHeight();

		int pageShowBlockCount = (int) Math.ceil(pageHeight / blockHeight);

		int width = ((int) a4.getWidth()) - alignmentViewPort.getBaseNameLenght() - 50;

		int showDataLength = (int) Math.floor(width * 1.0 / charWidth);

		if (showDataLength > 10 && showDataLength % 10 != 0) {

			showDataLength = showDataLength / 10 * 10;
		}
		int totalSequenceLength = alignmentViewPort.getTotalSequenceLength();

		int blockCount = (int) Math.ceil(totalSequenceLength * 1.0 / showDataLength);

		int pageCount = (int) Math.ceil(blockCount * 1.0 / pageShowBlockCount);

		OutputStream stream = new FileOutputStream(outputFile);

		Rectangle2D bounds = component.getBounds();

		Document document = new Document(new Rectangle((float) bounds.getWidth(), (float) component.getHeight() * 5));

		PdfWriter writer = PdfWriter.getInstance(document, stream);

		document.open();

		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate tp = cb.createTemplate((float) bounds.getWidth(), (float) component.getHeight() * 5);

		for (int i = 0; i < pageCount; i++) {
			// boolean newPage = document.newPage();
			Graphics2D g2d = tp.createGraphics((float) bounds.getWidth(), (float) component.getHeight() * i,
					new DefaultFontMapper());
			// if (newPage) {
			component.print(g2d);
			float index = i * component.getHeight();
			cb.addTemplate(tp, 0, index);
			// }
			g2d.dispose();
		}

		document.close();
	}

	private Component component;

	public void setSequenceDataPanel(Component component) {
		this.component = component;
	}

	class DataWithInterleavedAllWriteFile implements RunningTask {
		private int blockIndex = 0;

		File outputFile;

		private Document document;

		private PdfWriter writer;

		private int pageCount;

		private int totalSequenceCount;

		private int charHeight;

		private int charWidth;

		private int pageShowBlockCount;

		private int blockHeight;

		private int totalSequenceLength;

		private VisulizationDataProperty alignmentViewPort;

		private Rectangle a4;

		private int showDataLength;

		public DataWithInterleavedAllWriteFile(File outputFile) throws Exception {
			this.outputFile = outputFile;

			alignmentViewPort = alignmentViewMain.getAlignmentViewPort();

			charHeight = alignmentViewPort.getCharHeight();

			charWidth = alignmentViewPort.getCharWidth();

			JPanel jPanel = new JPanel();

			int yOf = jPanel.getFontMetrics(alignmentViewPort.getFont()).getDescent();

			int scaleHeight = yOf * 2 + charHeight - yOf;

			totalSequenceCount = alignmentViewPort.getTotalSequenceCount();

			int sequenceHeight = totalSequenceCount * charHeight;

			int sequenceAnnotationHeighty = 50;

			int blockDistance = 20;

			blockHeight = scaleHeight + sequenceHeight + charHeight + sequenceAnnotationHeighty + charHeight
					+ blockDistance;

			a4 = PageSize.A4;

			float pageHeight = a4.getHeight();

			pageShowBlockCount = (int) Math.ceil(pageHeight / blockHeight);

			int width = ((int) a4.getWidth()) - alignmentViewPort.getBaseNameLenght() - 50;

			showDataLength = (int) Math.floor(width * 1.0 / charWidth);

			if (showDataLength > 10 && showDataLength % 10 != 0) {

				showDataLength = showDataLength / 10 * 10;
			}
			totalSequenceLength = alignmentViewPort.getTotalSequenceLength();

			int blockCount = (int) Math.ceil(totalSequenceLength * 1.0 / showDataLength);

			pageCount = (int) Math.ceil(blockCount * 1.0 / pageShowBlockCount);

			OutputStream stream = new FileOutputStream(outputFile);

			document = new Document(new Rectangle(a4.getWidth(), pageShowBlockCount * blockHeight));

			writer = PdfWriter.getInstance(document, stream);

			document.open();

		}

		@Override
		public int processNext() throws Exception {

			while (blockIndex < pageCount) {

				document.newPage();

				for (int j = 0; j < pageShowBlockCount; j++) {
					PdfContentByte cb = writer.getDirectContent();

					PdfTemplate tp = cb.createTemplate(a4.getWidth(), blockHeight);

					int currentShowBlock = blockIndex * pageShowBlockCount + j;

					int currentStartRes = currentShowBlock * showDataLength;

					int startRes = currentStartRes > totalSequenceLength ? totalSequenceLength : currentStartRes;

					int currentEndRes = startRes + showDataLength;

					int endRes = currentEndRes > totalSequenceLength ? totalSequenceLength : currentEndRes;

					if (startRes == endRes) {
						break;
					}

					PrintAlignmentPanel printAlignmentPanel = new PrintAlignmentPanel(alignmentViewMain,
							alignmentViewPort);

					printAlignmentPanel.setCharWidth(charWidth);

					printAlignmentPanel.setCharHeight(charHeight);

					printAlignmentPanel.setStartRes(startRes);

					printAlignmentPanel.setEndRes(endRes);

					printAlignmentPanel.setPreferredSize(new Dimension((int) a4.getWidth(), blockHeight));

					Graphics2D g2d = tp.createGraphics(a4.getWidth(), (float) blockHeight, new DefaultFontMapper());

					printAlignmentPanel.print(g2d);

					float index = (pageShowBlockCount - 1 - j) * blockHeight;

					cb.addTemplate(tp, 0, index);

					g2d.dispose();
				}

				blockIndex++;

				return blockIndex;

			}

			return PROGRESS_FINSHED;
		}

		@Override
		public void actionsAfterFinished() throws Exception {
			document.close();
		}

		@Override
		public boolean isTimeCanEstimate() {
			return true;
		}

		@Override
		public int getTotalSteps() {
			return pageCount + 1;
		}

	}
}
