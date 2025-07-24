package module.multiseq.alignment.view.io;

import java.awt.Component;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import egps2.frame.gui.VectorGraphicsEncoder;
import egps2.utils.common.model.filefilter.FileFilterEPS;
import egps2.utils.common.model.filefilter.FileFilterPPTX;
import egps2.utils.common.model.filefilter.FileFilterPng;
import egps2.utils.common.model.filefilter.FileFilterSvg;
import egps2.utils.common.model.filefilter.SaveFilterJpg;
import egps2.utils.common.util.EGPSPrintUtilities;
import egps2.utils.common.util.SaveUtil;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import msaoperator.io.seqFormat.MSAFormatSuffixName;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.MS2AlignmentUtil;
import module.multiseq.alignment.view.gui.AlignmentViewContinuousRightPanel;
import module.multiseq.alignment.view.gui.AlignmentViewInterLeavedPanel;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

public class AlignmentSaver extends SaveUtil {

	private AlignmentViewMain main;

	private final String PDF_SUFFIX = "pdf";

	public AlignmentSaver(AlignmentViewMain alignmentViewMain) {
		this.main = alignmentViewMain;
	}

	@Override
	protected Optional<File> process(JComponent jpanel, Consumer<String> txtAction, JFileChooser jfc) throws Exception {

		jfc.setDialogTitle("Save the results as ... ");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		jfc.addChoosableFileFilter(new SaveFilterFasta());
		jfc.addChoosableFileFilter(new SaveFilterClustalW());
		jfc.addChoosableFileFilter(new SaveFilterGCGMSF());
		jfc.addChoosableFileFilter(new SaveFilterMega());
		jfc.addChoosableFileFilter(new SaveFilterPAML());
		jfc.addChoosableFileFilter(new SaveFilterPhylip());
		jfc.addChoosableFileFilter(new SaveFilterNEXUS());
		jfc.addChoosableFileFilter(new SaveFilterContinuousPDF());
		jfc.addChoosableFileFilter(new SaveFilterInterleavedPDF());
		jfc.addChoosableFileFilter(new SaveFilterLocalViewPDF());

		// 新增
		SaveFilterJpg filterJPG = new SaveFilterJpg();
		jfc.addChoosableFileFilter(filterJPG);
		FileFilterPng filterPNG = new FileFilterPng();
		jfc.addChoosableFileFilter(filterPNG);
		FileFilterSvg filterSVG = new FileFilterSvg();
		jfc.addChoosableFileFilter(filterSVG);
		FileFilterEPS filterEPS = new FileFilterEPS();
		jfc.addChoosableFileFilter(filterEPS);
		FileFilterPPTX filterPPTX = new FileFilterPPTX();
		jfc.addChoosableFileFilter(filterPPTX);

		MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();

		if (jfc.showSaveDialog(instanceFrame) != JFileChooser.APPROVE_OPTION) {
			return Optional.empty();
		}
		File selectedFile = jfc.getSelectedFile();
		if (selectedFile.exists()) {
			int res = JOptionPane.showConfirmDialog(instanceFrame, "File exists, confirm to overlap?", "Warning",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (res != JOptionPane.OK_OPTION) {
				return Optional.empty();
			}
		}

		if (main.getRightTabbedPanel().getComponentCount() == 0) {
			throw new InputMismatchException("Please import alignment data first.");
		}

		FileFilter selectedFileFilter = jfc.getFileFilter();

		Component component = main.getRightTabbedPanel().getComponent(0);
		// 表示选中的数据格式是 MSA alignment的格式
		if (selectedFileFilter instanceof MSAFormatSuffixName) {
			MSAFormatSuffixName fileFilter = (MSAFormatSuffixName) selectedFileFilter;
			String defaultFormatName = fileFilter.getDefaultFormatName();

			if (defaultFormatName.equalsIgnoreCase(PDF_SUFFIX)) {

				SaveFilterPDF filterPDF = (SaveFilterPDF) fileFilter;
				PDF2AlignmentUtil pdf2AlignmentUtil = new PDF2AlignmentUtil();
				pdf2AlignmentUtil.setSequenceDataPanel(component);

				pdf2AlignmentUtil.setAlignmentViewMain(main);

				pdf2AlignmentUtil.exportDataWithSelectedFormat(selectedFile.getAbsolutePath(),
						fileFilter.getDefaultFormatName(), filterPDF.getDefaultFormatDescription());

			} else {
				MS2AlignmentUtil ms2AlignmentUtil = new MS2AlignmentUtil();
				SequenceDataForAViewer sequenceData = main.getAlignmentViewPort().getSequenceData();
				ms2AlignmentUtil.setSequenceData(sequenceData);
				ms2AlignmentUtil.exportDataWithSelectedFormat(selectedFile.getAbsolutePath(),
						fileFilter.getDefaultFormatName());

			}
		} else {
			String ext = null;
			String extension = selectedFileFilter.getDescription();

			if (filterJPG.getDescription().equals(extension)) {
				ext = "jpg";
			} else if (filterPNG.getDescription().equals(extension)) {
				ext = "png";
			} else if (filterSVG.getDescription().equals(extension)) {
				ext = "svg";
			} else if (filterEPS.getDescription().equals(extension)) {
				ext = "eps";
			} else if (filterPPTX.getDescription().equals(extension)) {
				ext = "pptx";
			}

			selectedFile = new File(jfc.getSelectedFile().getPath() + "." + ext);

			JComponent paintJPanel = (JComponent) component;
			if (ext.equals("jpg") || ext.equals("png") || ext.equals("bmp") || ext.equals("gif")) {
				EGPSPrintUtilities.saveAsBitGraphics(ext, paintJPanel, selectedFile);
			} else if (ext.equals("svg")) {
				VectorGraphicsEncoder.saveVectorGraphic(paintJPanel, selectedFile.getAbsolutePath(),
						VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
			} else if (ext.equals("eps")) {
				VectorGraphicsEncoder.saveVectorGraphic(paintJPanel, selectedFile.getAbsolutePath(),
						VectorGraphicsEncoder.VectorGraphicsFormat.EPS);
			} else if (ext.equals("pptx")) {
				
				if (component instanceof AlignmentViewContinuousRightPanel) {
					AlignmentViewContinuousRightPanel panel4paint =  (AlignmentViewContinuousRightPanel) component;
					
					EGPSPrintUtilities.saveAsPptx(panel4paint.getSequenceJPanel(), selectedFile);
				}else {
					AlignmentViewInterLeavedPanel panel4paint =  (AlignmentViewInterLeavedPanel) component;
					EGPSPrintUtilities.saveAsPptx(panel4paint.getMainSplitPane(), selectedFile);
				}
			} else if (ext.equals("pdf")) {
				// 这边不可能出现pdf，因为在上一个if block里面已经被处理了
				EGPSPrintUtilities.saveAsPDF(paintJPanel, selectedFile);
			} else {
				throw new InputMismatchException("Please tell developers error happened.");
			}
		}

		// 这里如果返回到这一步就会弹出 成功导出的提示
		return Optional.of(selectedFile);
	}
}
