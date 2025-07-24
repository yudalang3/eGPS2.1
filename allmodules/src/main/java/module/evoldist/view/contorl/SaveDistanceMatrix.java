package module.evoldist.view.contorl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import egps2.frame.gui.VectorGraphicsEncoder;
import egps2.panels.dialog.EGPSOutputFileChooser200907;
import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.model.filefilter.FileFilterPdf;
import egps2.utils.common.model.filefilter.FileFilterSvg;
import egps2.utils.common.model.filefilter.SaveFilterDist;
import egps2.utils.common.model.filefilter.SaveFilterExel;
import egps2.utils.common.model.filefilter.SaveFilterJpg;
import egps2.utils.common.util.EGPSPrintUtilities;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;

public class SaveDistanceMatrix {

	private JPanel jPanelToTackle;

	public SaveDistanceMatrix(JPanel holder) {
		jPanelToTackle = holder;
	}

	public SaveDistanceMatrix() {
	}

	public void SaveData(String[] realColumnNames, double[][] realValueMatrix, double[][] displayedValueMatrix) {

		SwingUtilities.invokeLater(() -> {
			// swing thread
			JFileChooser jfc = EGPSOutputFileChooser200907.getFileChooser();

			jfc.setDialogTitle("Save the results as ... ");

			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);

			jfc.addChoosableFileFilter(SaveFilterDist.getInstance());
			jfc.addChoosableFileFilter(SaveFilterExel.getInstance());
			jfc.addChoosableFileFilter(new SaveFilterJpg());
			jfc.addChoosableFileFilter(new FileFilterSvg());
			jfc.addChoosableFileFilter(new FileFilterPdf());

			MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();

			if (jfc.showSaveDialog(instanceFrame) == JFileChooser.APPROVE_OPTION) {
				String ext = "dist";
				File selectedF = jfc.getSelectedFile();
				String extension = jfc.getFileFilter().getDescription();
				String descriptionDist = "DIST (*.dist)";
				String descriptionExcel = "Excel (*.xls)";
				if (descriptionDist.equals(extension)) {
					ext = "dist";
				} else if (descriptionExcel.equals(extension)) {
					ext = "xls";
				} else if ("JPEG (*.jpg)".equals(extension)) {
					ext = "jpg";
				} else if (extension.equals("SVG (*.svg)")) {
					ext = "svg";
				} else if (extension.equals("PDF (*.pdf)")) {
					ext = "pdf";
				}

				if (selectedF.exists()) {
					int res = JOptionPane.showConfirmDialog(instanceFrame, "File exists, confirm to overlap?",
							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (res != JOptionPane.OK_OPTION) {
						return;
					}
				} else {
					selectedF = new File(jfc.getSelectedFile().getPath() + "." + ext);
				}

				try {
					String suffixDistName = "dist";
					String suffixExcelName = "xls";
					if (suffixDistName.equals(ext)) {
						saveAsJTableToDist(selectedF, realColumnNames, realValueMatrix);
					} else if (suffixExcelName.equals(ext)) {
						saveAsJTableToExel(selectedF, realColumnNames, displayedValueMatrix);
					} else if (ext.equals("jpg") || ext.equals("png") || ext.equals("bmp") || ext.equals("gif")) {
						EGPSPrintUtilities.saveAsBitGraphics(ext, jPanelToTackle, selectedF);
					} else if (ext.equals("svg")) {
						VectorGraphicsEncoder.saveVectorGraphic(jPanelToTackle, selectedF.getAbsolutePath(),
								VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
					} else if (ext.equals("nwk")) {
					} else if (ext.equals("pdf")) {
						EGPSPrintUtilities.saveAsPDF(jPanelToTackle, selectedF);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				EGPSOutputFileChooser200907.setEGPSLastPath(selectedF.getPath());

				SwingDialog.showInfoMSGDialog("Information", "Output successfully !");
			}
			// swing thread
		});

	}

	public void saveAsJTableToDist(File file, String[] realColumnNames, double[][] realValueMatrix) throws IOException {
		FileWriter out = new FileWriter(file);

		int columnCount = realColumnNames.length;
		// output compitiable with phylip's distance format
		// columnCount = OTUs + 1
		out.write("    " + columnCount);
		out.write("\n");

		for (int i = 0; i < columnCount; i++) {
			out.write(realColumnNames[i] + "\t");
			for (double d : realValueMatrix[i]) {
				out.write(d + "\t");
			}
			out.write("\n");
		}
		out.close();
	}

	/**
	 * 
	 * Export the JTable as a excel file
	 * 
	 * @author mhl
	 * @param realColumnNames
	 * @param displayedValueMatrix
	 * 
	 * @Date Created on: 2019-03-20 14:22
	 * 
	 */
	private void saveAsJTableToExel(File file, String[] realColumnNames, double[][] displayedValueMatrix)
			throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(file);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet hs = wb.createSheet();

		int row = displayedValueMatrix.length;
		int cloumn = row + 1;

		HSSFCellStyle style = wb.createCellStyle();
//		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
//		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 11);
		style.setFont(font);
		HSSFCellStyle style1 = wb.createCellStyle();
//		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style1.setFillForegroundColor(HSSFColor.ORANGE.index);
//		style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 15);
//		font1.setBoldweight((short) 700);
		style1.setFont(font);
		for (int i = 0; i < row + 1; i++) {
			HSSFRow hr = hs.createRow(i);
			int maxHeadLeng = Integer.MIN_VALUE;
			for (int j = 0; j < cloumn; j++) {
				if (i == 0) {
					if (j == 0) {
						HSSFCell hc = hr.createCell((short) j);
						hc.setCellStyle(style1);
						hc.setCellValue("     ");
					} else {

						String value = realColumnNames[j - 1];
						int len = value.length();
						hs.setColumnWidth((short) j, (short) (len * 400));
						// Set the width of the zeroth column to the maximum length of all headers
						if (len > maxHeadLeng) {
							maxHeadLeng = len;
							hs.setColumnWidth((short) 0, (short) (len * 400));
						}
						HSSFRichTextString srts = new HSSFRichTextString(value);
						HSSFCell hc = hr.createCell((short) j);
						hc.setCellStyle(style1);
						hc.setCellValue(srts);
					}
				} else {
					String value = null;
					if (j == 0) {
						value = realColumnNames[i - 1];
					} else {
						value = displayedValueMatrix[i - 1][j - 1] + "";
					}

					HSSFRichTextString srts = new HSSFRichTextString(value);
					HSSFCell hc = hr.createCell((short) j);
					hc.setCellStyle(style);
					if (value.equals("") || value == null) {
						hc.setCellValue(new HSSFRichTextString(""));
					} else {
						hc.setCellValue(srts);
					}
				}
			}
		}

		try {
			wb.write(fos);
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
