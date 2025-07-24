package module.mutationpre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FileUtils;

import egps2.panels.dialog.EGPSFileChooser;
import utils.EGPSFileUtil;
import egps2.EGPSProperties;
import egps2.UnifiedAccessPoint;
import module.mutationpre.gui.PaintingPanel;

@SuppressWarnings("serial")
public class MyWorkStudio extends JPanel {

	private JScrollPane jScrollPane;
	private PaintingPanel paintingPanel;

	private Runnable action4refresh;
	SimplestModuleController controller;

	List<File> inputFileList = new ArrayList<>();

	final String JSON_PATH = "/previous.imported.json";
	
	private int indexOfExampleData = 1;

	public MyWorkStudio() {
		setLayout(new BorderLayout(0, 0));

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

//		JSplitPane splitPane = new JSplitPane();
//		splitPane.setDividerLocation(340);
//		splitPane.setDividerSize(7);
//		splitPane.setOneTouchExpandable(true);
//		add(splitPane, BorderLayout.CENTER);

		JPanel leftPanel = new JPanel();
//		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new BorderLayout(0, 0));

		JPanel leftButtomButtonPanel = new JPanel();
		leftPanel.add(leftButtomButtonPanel, BorderLayout.SOUTH);
		leftButtomButtonPanel.setLayout(new BorderLayout(0, 0));

		leftPanel.add(getTopButtonPanel(), BorderLayout.NORTH);

		JButton exampleBut = new JButton("Example: 1");
		exampleBut.setFont(globalFont);
		exampleBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exampleBut.setText("Example: ".concat(String.valueOf(indexOfExampleData)));
			}
		});
		exampleBut.setToolTipText("Click the button to display the example input. Multiple clicks to see more.");
		leftButtomButtonPanel.add(exampleBut, BorderLayout.WEST);

//		JButton refreshBut = new JButton("Execute");
//		refreshBut.setFont(globalFont);
//		refreshBut.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (action4refresh != null) {
//					action4refresh.run();
//				}
//			}
//		});
//		refreshBut.setToolTipText("Click to see the instance results.");
//		leftButtomButtonPanel.add(refreshBut, BorderLayout.EAST);

//		textArea = new JTextArea();
//		textArea.setFont(globalFont);
//		leftPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

		JPanel rightPanel = new JPanel();
//		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new BorderLayout(0, 0));

		paintingPanel = new PaintingPanel();
		jScrollPane = new JScrollPane(paintingPanel);

		JPanel paintingPanelContainer = new JPanel();
		paintingPanelContainer.setBackground(Color.WHITE);
		paintingPanelContainer.setBorder(new TitledBorder(new EmptyBorder(15, 15, 15, 15), "Plots",
				TitledBorder.LEADING, TitledBorder.TOP, globalFont.deriveFont(Font.BOLD), null));
		rightPanel.add(jScrollPane, BorderLayout.CENTER);
		
		
		add(rightPanel, BorderLayout.CENTER);
	}

	private JPanel getTopButtonPanel() {
		File file = new File(EGPSProperties.JSON_DIR.concat(JSON_PATH));
		Vector<String> vector = new Vector<>();
		if (file.exists()) {
			try {
				List<String> readLines = FileUtils.readLines(file);
				for (String string : readLines) {
					File e = new File(string);
					inputFileList.add(e);
					vector.add(e.getName());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		JComboBox<String> jComboBox = new JComboBox<>(vector);

		JPanel importDataPanel = new JPanel(new BorderLayout(0, 0));

//		JButton jButton = new JButton("Import");

		ActionListener importListener = e -> {

			int selectedIndex = jComboBox.getSelectedIndex();
			File file2 = inputFileList.get(selectedIndex);
//			try {
//				textArea.setText(FileUtils.readFileToString(file2));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}

			// 设置最大的存储量
			final int MAX_STORED_NUMBER = 10;
			if (inputFileList.size() > MAX_STORED_NUMBER) {
				inputFileList.remove(0);
				vector.remove(0);
			}
//			System.out.println("Selected index is: " + selectedIndex);
		};
//		jButton.addActionListener(importListener);

//		ImageIcon iconpng = new ImageIcon(getClass().getResource("images/open.png"));
		JButton icon = new JButton();
		icon.addActionListener(e -> {
			EGPSFileChooser egpsFileChooser = new EGPSFileChooser(this.getClass());
			int showOpenDialog = egpsFileChooser.showOpenDialog(UnifiedAccessPoint.getInstanceFrame());
			if (showOpenDialog == EGPSFileChooser.APPROVE_OPTION) {
				File selectedFile = egpsFileChooser.getSelectedFile();
				String name = selectedFile.getName();

				boolean remove = vector.remove(name);
				if (remove) {
					inputFileList.remove(selectedFile);
				}

				inputFileList.add(selectedFile);
				vector.add(name);
				jComboBox.setSelectedItem(name);

				importListener.actionPerformed(e);
			}
		});

		importDataPanel.add(jComboBox, BorderLayout.CENTER);
//		importDataPanel.add(jButton, BorderLayout.EAST);
		importDataPanel.add(icon, BorderLayout.WEST);

		jComboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int selectedIndex = jComboBox.getSelectedIndex();

				int j = vector.size() - 1;
				Collections.swap(vector, selectedIndex, j);
				Collections.swap(inputFileList, selectedIndex, j);

				importListener.actionPerformed(null);
			}
		});

		return importDataPanel;
	}

	public void setController(SimplestModuleController controller) {
		this.controller = controller;
		paintingPanel.setController(controller);
	}

	public void weakRefreshPaintingPanel() {
		paintingPanel.repaint();
	}

//	public JTextArea getTextArea() {
//		return textArea;
//	}

	public Dimension getPaintingPanelSize() {
		return jScrollPane.getSize();
	}

	public PaintingPanel getPaintingPanel() {
		return paintingPanel;
	}

	public void setAction4Refresh(Runnable run) {
		this.action4refresh = run;
	}

	public void action4closeTab() {

		File file2output = new File(EGPSProperties.JSON_DIR.concat(JSON_PATH));
		List<String> output = new ArrayList<>();
		for (File file : inputFileList) {
			output.add(file.getAbsolutePath());
		}

		try {
			FileUtils.writeLines(file2output, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void heavyRefresh() {
		jScrollPane.revalidate();
	}

	public String getExampleStr() {
		StringBuilder sBuilder = EGPSProperties.getSpecificationHeader();
		try {
			List<String> contentList = null;
			if (indexOfExampleData == 1) {
				contentList = EGPSFileUtil
						.getContentFromInputStreamAsLines(getClass().getResourceAsStream("example1.txt"));
				indexOfExampleData++;
			} else if (indexOfExampleData == 2) {
				contentList = EGPSFileUtil
						.getContentFromInputStreamAsLines(getClass().getResourceAsStream("example2.txt"));
				indexOfExampleData++;
			}else if (indexOfExampleData == 3) {
				contentList = EGPSFileUtil
						.getContentFromInputStreamAsLines(getClass().getResourceAsStream("example3.txt"));
				indexOfExampleData++;
			}else if (indexOfExampleData == 4) {
				contentList = EGPSFileUtil
						.getContentFromInputStreamAsLines(getClass().getResourceAsStream("input.merged.all.VOC.inputFasta.txt"));
				String lastLine = "alignmnetFile="
						+ EGPSProperties.PROPERTIES_DIR
						.concat("/bioData/genomicMuts/example.longAlignedFasta_2.fas");
				contentList.add(lastLine);
				indexOfExampleData++;
			}else if (indexOfExampleData == 5) {
				contentList = EGPSFileUtil
						.getContentFromInputStreamAsLines(getClass().getResourceAsStream("input.variant_visulization.example.txt"));
				String lastLine2 = "mutationsFile=" + EGPSProperties.PROPERTIES_DIR
						+ "/bioData/genomicMuts/variant_of_cumulatedMuts_forVisulize.txt";
				contentList.add(lastLine2);
				indexOfExampleData++;
			} else {
				contentList = EGPSFileUtil
						.getContentFromInputStreamAsLines(getClass().getResourceAsStream("input.merged.all.VOC.directInput.txt"));
				indexOfExampleData = 1;
			}
			for (String string : contentList) {
				sBuilder.append(string).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sBuilder.toString();
	}

}
