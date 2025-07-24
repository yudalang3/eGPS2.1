package module.sequencelogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.apache.commons.lang3.tuple.Triple;

import com.jidesoft.swing.JideTabbedPane;

import egps2.frame.gui.VectorGraphicsEncoder;
import egps2.utils.common.util.EGPSPrintUtilities;
import egps2.utils.common.util.SaveUtil;
import egps2.Authors;
import egps2.UnifiedAccessPoint;
import module.sequencelogo.extractMEMEinformation.ProcessMEMEChipOutfile;
import module.sequencelogo.makesequencelogo.CalculateFrequencyOfPerBase;
import module.sequencelogo.makesequencelogo.MakeSequenceLogo;
import module.sequencelogo.makesequencelogo.MakeSequenceLogoParameter;
import module.sequencelogo.makesequencelogo.ReadFrequencyFile;

/**
 * 
 * @ClassName: MakeSeqLogoController
 * @Description: 绘制sequence logo的控制类
 * @author zjw
 * @date 2024-04-28 03:19:33
 */
public class MakeSeqLogoController {

	private final Main_makeSeqLogo main_makeSeqLogo;
	private boolean savable = false;
	private boolean isFromMEME = false;
	private Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	public MakeSeqLogoController(Main_makeSeqLogo main) {
		main_makeSeqLogo = main;
	}

	public String[] getTeamAndAuthors() {
		String[] info = new String[3];
		info[0] = "EvolGen";
		info[1] = Authors.ZHANGJIANWEI + "," + Authors.LIHAIPENG;
		info[2] = "http://www.picb.ac.cn/evolgen/";
		return info;
	}

	public Main_makeSeqLogo getMain() {
		return main_makeSeqLogo;
	}

	/**
	 * 
	 * @MethodName: loadTab
	 * @Description: 直接根据比对好的序列文件，fasta文件，或者频率矩阵文件生成sequence logo时调用
	 * @author zjw
	 * @param parameter void
	 * @date 2024-05-03 03:48:41
	 */
	public void loadTab(MakeSequenceLogoParameter parameter) {
				
		JPanel panel = loadOnlyOneTab(parameter);		
		main_makeSeqLogo.setRightJPanel(panel);	
		getMain().invokeTheFeatureMethod(0);
	}

	/**
	 * 
	 * @MethodName: loadTab4MEMEChipOut
	 * @Description: 根据MEMEChip得到的文件批量生成sequence logo
	 * @author zjw
	 * @param files
	 * @param tempOutMotifHeader
	 * @param outputFloderString
	 * @param tempOutFolder
	 * @param outImageFolderPath
	 * @param outputFormat
	 * @param initialParameter void
	 * @date 2024-05-03 03:48:01
	 */
	public void loadTab4MEMEChipOut(String[] files, String tempOutMotifHeader, String outputFloderString,
			String tempOutFolder, String outImageFolderPath, String outputFormat, MakeSequenceLogoParameter initialParameter) {

		isFromMEME = true;
		String systemSeparator = System.getProperty("file.separator");
		Boolean generateSucessfully = true;
		int numOfLabelsDisplayed = initialParameter.numOfLabelsDisplayed4MEMEChipOut;

		JPanel jPanel = new JPanel(new BorderLayout());
		JideTabbedPane tabbedPane = new JideTabbedPane();
		tabbedPane.setTabShape(JideTabbedPane.SHAPE_OFFICE2003);
		tabbedPane.setTabPlacement(JideTabbedPane.TOP);
		tabbedPane.setTabColorProvider(JideTabbedPane.ONENOTE_COLOR_PROVIDER);
		tabbedPane.setTabEditingAllowed(true);
		tabbedPane.setSelectedTabFont(globalFont);
		tabbedPane.setShowCloseButtonOnTab(true);
		tabbedPane.setBoldActiveTab(true);

		List<JPanel> panels = new ArrayList<>(); 
		// 根据每个MEMEChip中有的motif的每个碱基在每个位置的比例文件，依次生成sequence logo图
		try {
			int numberOfFiles = 0;
			for (String fileName : files) {
				if (fileName.startsWith(tempOutMotifHeader)) {
					numberOfFiles++;
					String inputOneFilename = outputFloderString + tempOutFolder + systemSeparator + fileName;
					File inputOneFile = new File(inputOneFilename);
					LinkedHashMap<Integer, LinkedHashMap<Character, Double>> frequenceOfBasesPerPosition = null;
					try {
						frequenceOfBasesPerPosition = ReadFrequencyFile.readFrequencyFile(inputOneFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> NFB = CalculateFrequencyOfPerBase
							.calculateFrequencyAndBitsOfPerBaseUsedFrequencyFile(frequenceOfBasesPerPosition);
					
					MakeSequenceLogoParameter parameter = new MakeSequenceLogoParameter();
					parameter.setAbscissaAngle(initialParameter.getAbscissaAngle());
					parameter.setdrawingBoardHeight(initialParameter.getdrawingBoardHeight());
					parameter.setDrawingBoardWidth(initialParameter.getDrawingBoardWidth());
					parameter.setInitialCoordinate();
					parameter.setIsSeqLogoFontSizeDefault(initialParameter.getIsSeqLogoFontSizeDefault());
					parameter.setNFB(NFB);
					parameter.setSeqLogoFontSize(initialParameter.getSeqLogoFontSize());
					parameter.setSeqLogoType(initialParameter.getSeqLogoType());
					parameter.setSequenceType(initialParameter.getSequenceType());
					
					JPanel panel = loadOnlyOneTab(parameter);

					String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
					String outputFilename = outImageFolderPath + systemSeparator + nameWithoutExtension + '.'
							+ outputFormat;
					saveViewPanelAs(panel, outputFilename, outputFormat);

					if (numberOfFiles <= numOfLabelsDisplayed) {
						tabbedPane.addTab(nameWithoutExtension, panel);
						panels.add(panel);
					}

				}
			}
		} catch (Exception e) {
			generateSucessfully = false;
			e.printStackTrace();
		}

		jPanel.add(tabbedPane, BorderLayout.CENTER);
		main_makeSeqLogo.setRightJPanel(jPanel);

		if (generateSucessfully) {
			// 如果有临时生成的这个目录（将.meme文件处理后生成的所有的motif的每个碱基在每个位置的比例文件所在的目录），就删除
			ProcessMEMEChipOutfile.deleteFolder(new File(outputFloderString + tempOutFolder));
			UIManager.put("OptionPane.messageFont", globalFont);
			JTextArea textArea = new JTextArea(2, 30); // 设置文本区域的行数和列数
			textArea.setFont(globalFont);
			Color lightGray = new Color(250, 250, 250);
			textArea.setBackground(lightGray);
			textArea.setLineWrap(true);
	        textArea.setWrapStyleWord(true);
			textArea.setPreferredSize(new Dimension(100, 100));
			StringBuilder sBuilder = new StringBuilder();
			if (files.length == 1) {
				sBuilder.append("Generate sequence logo successfully! The result was saved in ");
				sBuilder.append(outImageFolderPath);
				sBuilder.append(".");
			} else if (files.length <= numOfLabelsDisplayed) {				
				sBuilder.append("Generate sequence logos successfully! The results were saved in ");
				sBuilder.append(outImageFolderPath);
				sBuilder.append(".");
			} else if (files.length > numOfLabelsDisplayed) {
				sBuilder.append("Generate sequence logos successfully! The results were saved in ");
				sBuilder.append(outImageFolderPath);
				sBuilder.append(". ");
				sBuilder.append("Since there are more than five sequence logos, only display five sequence logos.");
			}
			textArea.setText(sBuilder.toString());
	        textArea.setEditable(false); 
	        JOptionPane.showMessageDialog(null, textArea, "Notice", JOptionPane.PLAIN_MESSAGE);
		}

	}

	/**
	 * 
	 * @MethodName: loadOnlyOneTab
	 * @Description: 当只需要画一张sequence logo时进行调用
	 * @author zjw
	 * @param parameter
	 * @return JPanel
	 * @date 2024-05-03 03:48:22
	 */
	private JPanel loadOnlyOneTab(MakeSequenceLogoParameter parameter) {

		MakeSequenceLogo logo = new MakeSequenceLogo(parameter);
		logo.setSize(main_makeSeqLogo.getRightJPanel().getSize());
		savable = true;
		return logo;
	}

	/**
	 * 
	 * @MethodName: saveViewPanelAs
	 * @Description: 通过MEMEChip得到的文件生成sequence logo时，需要批量保存的时候调用
	 * @author zjw
	 * @param panel
	 * @param outputFilename
	 * @param outputFormat void
	 * @date 2024-04-26 05:15:37
	 */
	private void saveViewPanelAs(JPanel panel, String outputFilename, String outputFormat) {

		File file = new File(outputFilename);
		try {
			if (outputFormat.equals("pdf")) {
				EGPSPrintUtilities.saveAsPDF(panel, file);
			} else if (outputFormat.equals("jpg") || outputFormat.equals("png")) {
				EGPSPrintUtilities.saveAsBitGraphics(outputFormat, panel, file);
			} else if (outputFormat.equals("svg")) {
				VectorGraphicsEncoder.saveVectorGraphic(panel, file.getAbsolutePath(),
						VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
			} else if (outputFormat.equals("eps")) {
				VectorGraphicsEncoder.saveVectorGraphic(panel, file.getAbsolutePath(),
						VectorGraphicsEncoder.VectorGraphicsFormat.EPS);
			} else if (outputFormat.equals("pptx")) {
				EGPSPrintUtilities.saveAsPptx(panel, file);
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public boolean isSaveable() {

		return savable;
	}

	public void saveViewPanelAs() {
		if (isFromMEME) {
			JPanel rightPanel = getMain().getRightJPanel();
			JideTabbedPane tabbedPane = (JideTabbedPane) rightPanel.getComponent(0);
			JComponent jComponent = (JComponent) tabbedPane.getSelectedComponent();
			new SaveUtil().saveData(jComponent);
		} else {
			new SaveUtil().saveData(getMain().getRightJPanel());
		}
	}

}
