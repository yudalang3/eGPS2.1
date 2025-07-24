package module.vennplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import egps2.utils.common.math.CheckedNumber;
import egps2.utils.common.model.filefilter.FileFilterTxt;
import egps2.utils.common.model.filefilter.OpenFilterCsv;
import egps2.utils.common.model.filefilter.OpenFilterTsv;
import egps2.UnifiedAccessPoint;
import module.vennplot.VennPlotMain;
import module.vennplot.model.Element;
import module.vennplot.model.SetItem;

public class VennPlotDataInputView extends JPanel {
	private static final long serialVersionUID = 3224358094163390349L;
	
	private JTextField textField;
	private VennPlotMain vennPlotMain;
	
	Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
	
	final int itemWidth = 120;
	final private int heightForTextField = 20;
	
	private JPanel jPanelForItems;
	private JButton btnRemoveAllSet;
	private JButton btn_RemoveSetItem;
	private JButton btn_addSetItem;
	private int numOfIterms = 0;
	private JScrollPane scrollPane;
	
	private int displaySampleIndex = 0;
	private JRadioButton rdbtnRawString;
	
	protected final int leftColumnSize = 130;
	
	private String errorString;

	/**
	 * Create the panel.
	 * @param vennPlotMain 
	 */
	public VennPlotDataInputView() {
		initializaContent();

	}
	public VennPlotDataInputView(VennPlotMain vennPlotMain) {
		this.vennPlotMain = vennPlotMain;
		initializaContent();
		
	}

	private void initializaContent() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		
		JPanel top = new JPanel();
		top.setBackground(Color.WHITE);
		top.setBorder(new EmptyBorder(5, 0, 5, 0));
		add(top, BorderLayout.NORTH);
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		
		JLabel lblPleaseInputThe = new JLabel("Input file path: ");
		lblPleaseInputThe.setFont(defaultFont);
		top.add(lblPleaseInputThe);
		Dimension preferredSize = lblPleaseInputThe.getPreferredSize();
		lblPleaseInputThe.setPreferredSize(new Dimension(leftColumnSize, preferredSize.height));
		
		Dimension dimension = new Dimension(40, 0);
		
		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setEditable(false);
		textField.setFont(defaultFont);
		top.add(textField);
		
		top.add(Box.createRigidArea(dimension));
		
		JButton btnNewButton = new JButton("Import file");
		btnNewButton.setToolTipText(
				"<html><body>Click this button to import data<br>If you choose \"Simple string\",<br>you should import a file like:<br>a\tb\tc<br>d\te\tf<br>If you choose \"Genomic region\",<br>the file should like<br>chr1\t1\t100\tchr2\t3\t300<br>Separator of \",\" and \"\t\" are both allowed!");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeAllIterms();
				loadingDataOperation();
			}
			
		});
		btnNewButton.setFont(defaultFont);
		top.add(btnNewButton);
		
		JPanel bottom = new JPanel();
		bottom.setBackground(Color.WHITE);
		bottom.setBorder(new EmptyBorder(4, 0, 3, 0));
		bottom.setAlignmentX(Component.LEFT_ALIGNMENT);
		bottom.setAlignmentY(Component.TOP_ALIGNMENT);
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		add(bottom, BorderLayout.SOUTH);
		
		bottom.add(Box.createRigidArea(new Dimension(leftColumnSize,0)));
		btn_addSetItem = new JButton("Add set item");
		btn_addSetItem.setFont(defaultFont);
		bottom.add(btn_addSetItem);
		
		btn_RemoveSetItem = new JButton("Remove last item");
		btn_RemoveSetItem.setFont(defaultFont);
		bottom.add(btn_RemoveSetItem);
		
		btnRemoveAllSet = new JButton("Remove all sets");
		btnRemoveAllSet.setFont(defaultFont);
		bottom.add(btnRemoveAllSet);
		
		bottom.add(Box.createHorizontalGlue());
		JButton btnExample = new JButton("Display next example");
		btnExample.addActionListener(e ->{
			displayExamples();
		});
		btnExample.setFont(defaultFont);
		bottom.add(btnExample);
		
		btn_addSetItem.addActionListener( e ->{
			addIterm();
		});
		btnRemoveAllSet.addActionListener( e ->{
			SwingUtilities.invokeLater(() ->{
				removeAllIterms();
			});
		});
		btn_RemoveSetItem.addActionListener( e ->{
			removeLastItem();
		});
		
		JPanel left = new JPanel();
		left.setBackground(Color.WHITE);
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		
		left.add(Box.createRigidArea(new Dimension(0, 20)));
		JLabel jLabelName = new JLabel("Name:");
		jLabelName.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelName.setFont(defaultFont);
		left.add(jLabelName);
		
		left.add(Box.createRigidArea(new Dimension(0, 16)));
		
		JLabel lblCount = new JLabel("Count:");
		lblCount.setHorizontalAlignment(SwingConstants.LEFT);
		lblCount.setFont(defaultFont);
		left.add(lblCount);
		
		left.add(Box.createRigidArea(new Dimension(0, 150)));
		
		JLabel jLabelContent = new JLabel("Content : ");
		jLabelContent.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelContent.setFont(defaultFont);
		
		left.add(jLabelContent);
		add(left, BorderLayout.WEST);
		
		left.add(Box.createRigidArea(new Dimension(0, 10)));
		rdbtnRawString = new JRadioButton("Simple string");
		rdbtnRawString.setFont(defaultFont);
		rdbtnRawString.setBackground(Color.WHITE);
		rdbtnRawString.setToolTipText("Just a string!");
		left.add(rdbtnRawString);
		
		JRadioButton rdbtnGenomicRegion = new JRadioButton("Genomic region");
		rdbtnGenomicRegion.setFont(defaultFont);
		rdbtnGenomicRegion.setBackground(Color.WHITE);
		rdbtnGenomicRegion.setToolTipText("<html><body>String like this:<br>chr1\t100\t200<br>chr2\t1\t100<br>. . .<br>The bed format!");
		left.add(rdbtnGenomicRegion);
		
		left.setPreferredSize(new Dimension(leftColumnSize, left.getPreferredSize().height));
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnRawString);
		bg.add(rdbtnGenomicRegion);
		rdbtnRawString.setSelected(true);
		
		jPanelForItems = new JPanel();
		jPanelForItems.setBackground(Color.white);
		jPanelForItems.setLayout(new BoxLayout(jPanelForItems, BoxLayout.X_AXIS));
		
		//jPanelForItems.setPreferredSize(new Dimension(1000, 1000));
		
		scrollPane = new JScrollPane(jPanelForItems,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		add(scrollPane, BorderLayout.CENTER);
		
		Font deriveFont = titleFont.deriveFont(16f);
		TitledBorder topBorder = new TitledBorder("Data import part : input data from file or string content!");
		topBorder.setTitlePosition(TitledBorder.TOP);
	    topBorder.setTitleFont(deriveFont);
	    
	    TitledBorder doubleBorder = new TitledBorder(topBorder, "Clik Venn plot to see the diagram", TitledBorder.RIGHT,
	        TitledBorder.BOTTOM);
	    doubleBorder.setTitleFont(deriveFont);
	    
		setBorder(null);
		
		
	}
	
	public boolean isInputStringIsSimpleString() {
		return rdbtnRawString.isSelected();
	}
	
	private void displayExamples() {
		
		if (displaySampleIndex == 3) {
			displaySampleIndex = 0;
		}
		removeAllIterms();
		
		String text = "";
		
		boolean inputStringIsSimpleString = isInputStringIsSimpleString();
		
		/**
		 * YDL: for input string is genomic region ,we only show the basic usage!
		 */
		if (!inputStringIsSimpleString) {
			addIterm();addIterm();addIterm();
			
			JPanelForInputData first = (JPanelForInputData) jPanelForItems.getComponent(0);

			first.getTopTextField().setText("Set1 Name");
			text = "chr1	100	201\nchr1	200	301\nchr2	301	401\nchr2	501	600";
			
			first.getjTextArea().setText(text);

			JPanelForInputData second = (JPanelForInputData) jPanelForItems.getComponent(1);

			second.getTopTextField().setText("Set2 Name");
			text = "chr2	301	401\nchr2	501	600\nchr3	300	401\nchr4	450	500";
			
			second.getjTextArea().setText(text);

			JPanelForInputData third = (JPanelForInputData) jPanelForItems.getComponent(2);

			third.getTopTextField().setText("Set3 Name");
			text = "chr2	301	401\nchr4	450	500";
			
			third.getjTextArea().setText(text);
			return;
		}
		
		if (displaySampleIndex == 0) {
			addIterm();addIterm();addIterm();

			JPanelForInputData first = (JPanelForInputData) jPanelForItems.getComponent(0);

			first.getTopTextField().setText("Set1 Name");
			text = "a\nb\nc\nd\ne\nf\ng\n";

			first.getjTextArea().setText(text);

			JPanelForInputData second = (JPanelForInputData) jPanelForItems.getComponent(1);

			second.getTopTextField().setText("Set2 Name");
			text = "b\nd\nf\ng\n";

			second.getjTextArea().setText(text);

			JPanelForInputData third = (JPanelForInputData) jPanelForItems.getComponent(2);

			third.getTopTextField().setText("Set3 Name");
			text = "g\nh\ni\nj\n";

			third.getjTextArea().setText(text);
		}else if(displaySampleIndex == 1){
			
			addIterm();addIterm();addIterm();addIterm();addIterm();
			
			JPanelForInputData first = (JPanelForInputData) jPanelForItems.getComponent(0);

			first.getTopTextField().setText("Set1 Name");
			StringBuilder stringBuilder = new StringBuilder(2000);
			for (int i = 0; i < 2000; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			first.getjTextArea().setText(stringBuilder.toString());

			JPanelForInputData second = (JPanelForInputData) jPanelForItems.getComponent(1);

			second.getTopTextField().setText("Set2 Name");
			stringBuilder.setLength(0);
			for (int i = 1001; i < 3000; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			second.getjTextArea().setText(stringBuilder.toString());

			JPanelForInputData third = (JPanelForInputData) jPanelForItems.getComponent(2);

			third.getTopTextField().setText("Set3 Name");
			stringBuilder.setLength(0);
			for (int i = 1501; i < 2500; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			third.getjTextArea().setText(stringBuilder.toString());
			
			
			JPanelForInputData fourth = (JPanelForInputData) jPanelForItems.getComponent(3);
			
			fourth.getTopTextField().setText("Set4 Name");
			stringBuilder.setLength(0);
			for (int i = 1601; i < 3000; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			fourth.getjTextArea().setText(stringBuilder.toString());
			
			JPanelForInputData fivth = (JPanelForInputData) jPanelForItems.getComponent(4);
			
			fivth.getTopTextField().setText("Set5 Name");
			stringBuilder.setLength(0);
			for (int i = 1; i < 500; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			fivth.getjTextArea().setText(stringBuilder.toString());
		}else {
			
			addIterm();addIterm();addIterm();addIterm();addIterm();addIterm();addIterm();
			
			JPanelForInputData first = (JPanelForInputData) jPanelForItems.getComponent(0);

			first.getTopTextField().setText("Set1 Name");
			StringBuilder stringBuilder = new StringBuilder(2000);
			for (int i = 3001; i < 5050; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			first.getjTextArea().setText(stringBuilder.toString());

			JPanelForInputData second = (JPanelForInputData) jPanelForItems.getComponent(1);

			second.getTopTextField().setText("Set2 Name");
			stringBuilder.setLength(0);
			for (int i = 4001; i < 5090; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			second.getjTextArea().setText(stringBuilder.toString());

			JPanelForInputData third = (JPanelForInputData) jPanelForItems.getComponent(2);

			third.getTopTextField().setText("Set3 Name");
			stringBuilder.setLength(0);
			for (int i = 4501; i < 5500; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			third.getjTextArea().setText(stringBuilder.toString());
			
			
			JPanelForInputData fourth = (JPanelForInputData) jPanelForItems.getComponent(3);
			
			fourth.getTopTextField().setText("Set4 Name");
			stringBuilder.setLength(0);
			for (int i = 5001; i < 6000; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			fourth.getjTextArea().setText(stringBuilder.toString());
			
			JPanelForInputData fivth = (JPanelForInputData) jPanelForItems.getComponent(4);
			
			fivth.getTopTextField().setText("Set5 Name");
			stringBuilder.setLength(0);
			for (int i = 5501; i < 6000; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			fivth.getjTextArea().setText(stringBuilder.toString());
			
			JPanelForInputData sixth = (JPanelForInputData) jPanelForItems.getComponent(5);
			
			sixth.getTopTextField().setText("Set6 Name");
			stringBuilder.setLength(0);
			for (int i = 7001; i < 8000; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			sixth.getjTextArea().setText(stringBuilder.toString());
			
			JPanelForInputData sveenth = (JPanelForInputData) jPanelForItems.getComponent(6);
			
			sveenth.getTopTextField().setText("Set7 Name");
			stringBuilder.setLength(0);
			for (int i = 300; i < 400; i++) {
				stringBuilder.append(i);
				stringBuilder.append("\n");
			}
			sveenth.getjTextArea().setText(stringBuilder.toString());
		}
		
		
		displaySampleIndex++;
	}
	private void removeLastItem() {
		
		numOfIterms --;
		SwingUtilities.invokeLater(() ->{
			jPanelForItems.remove(jPanelForItems.getComponentCount() - 1);
			final Dimension maximumSize = new Dimension(itemWidth * numOfIterms, jPanelForItems.getHeight());
			jPanelForItems.setPreferredSize(maximumSize);
			scrollPane.updateUI();
			changeButtonStatus();
		});
		
	}
	private void changeButtonStatus() {
		if (numOfIterms > 0) {
			btn_RemoveSetItem.setEnabled(true);
			btnRemoveAllSet.setEnabled(true);
		}else {
			btn_RemoveSetItem.setEnabled(false);
			btnRemoveAllSet.setEnabled(false);
		}
		
	}

	private void removeAllIterms() {

		numOfIterms = 0;

		jPanelForItems.removeAll();
		final Dimension maximumSize = new Dimension(itemWidth * numOfIterms, jPanelForItems.getHeight());
		jPanelForItems.setPreferredSize(maximumSize);
		scrollPane.updateUI();
		changeButtonStatus();

	}

	public void addIterm() {
		numOfIterms++;
		JPanelForInputData jPanel = new JPanelForInputData();

		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		JTextArea topTextField = jPanel.getTopTextField();
		topTextField.setAlignmentX(0.5f);
		jPanel.add(topTextField);
		jPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		JLabel bottomTextField = jPanel.getBottomTextField();
		bottomTextField.setAlignmentX(0.5f);
		jPanel.add(bottomTextField);
		jPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		JScrollPane getjTextArea = jPanel.getjTextAreaShell();
		getjTextArea.setAlignmentX(0.5f);
		jPanel.add(getjTextArea);

		final Dimension preferredSize = new Dimension(itemWidth + 20, jPanelForItems.getHeight() - 10);
		jPanel.setPreferredSize(preferredSize);
		jPanel.setMaximumSize(preferredSize);
		jPanel.setBorder(new TitledBorder(new LineBorder(new Color(105, 105, 105), 1), "Set " + numOfIterms));
		jPanelForItems.add(jPanel);
		final Dimension maximumSize = new Dimension(itemWidth * numOfIterms, jPanelForItems.getHeight());
		jPanelForItems.setPreferredSize(maximumSize);

		changeButtonStatus();
//		scrollPane.updateUI();
		jPanelForItems.revalidate();

	}
	
	/**
	 * YDL: 
	 * This is the core function for poster analysis!
	 * @return null to reject analysis!
	 */
	public SetItem[] getSetItems() {
		
		errorString = "You do not import the data yet! Please import data!";
		
		int length = jPanelForItems.getComponentCount();
		if (length == 0 ) {
			return null;
		}
		
		List<String> names = new ArrayList<String>();
		List<String> contents = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			JPanelForInputData jData = (JPanelForInputData) jPanelForItems.getComponent(i);
			String setName = jData.getTopTextField().getText();

			String text = jData.getjTextArea().getText();
			
			if (text.trim().length() > 0) {
				contents.add(text);
				names.add(setName);
			}
			
		}
		
		length = names.size();
		if (length == 0) {
			return null;
		}
		
		boolean ifGenomicRegion = !isInputStringIsSimpleString();
		SetItem[] setItems = new SetItem[length];

		for (int i = 0; i < length; i++) {
			SetItem setItem = new SetItem();
			String setName = names.get(i);
			setItem.setName(setName);

			String[] elementNames = contents.get(i).split("\n");
			List<Element> temp = new ArrayList<>(elementNames.length);
			for (String string : elementNames) {
				string = string.trim();
				if (string.length() > 0) {
					Element element = new Element(string);
					element.setIfGenomicRegion(ifGenomicRegion);
					if (ifGenomicRegion) {
						String[] split = string.split("\t");
						
						if (split.length != 3) {
							errorString ="Sorry! you input element: "+string+ " in \""+setName+"\" set is not correct!";
							return null;
						}
						
						boolean p1 = CheckedNumber.isPositiveInteger(split[1], false) ;
						boolean p2 = CheckedNumber.isPositiveInteger(split[2], false);
						if (!p1 || !p2) {
							errorString ="Sorry! you input element: "+string+ " in \""+setName+"\" set is not correct!";
							return null;
						}
						
						element.setRawStr(split[0]);
						element.setStart(Integer.parseInt(split[1]));
						element.setEnd(Integer.parseInt(split[2]));
					}
					temp.add(element);
				}
			}
			
			
			
			setItem.setSetElements(temp.toArray(new Element[0]));

			setItems[i] = setItem;
		}

		return setItems;

	}
	
	public String getErrorString() {
		return errorString;
	}

	
	private void loadingDataOperation() {
		Preferences pref = Preferences.userNodeForPackage(this.getClass());
		String lastPath = pref.get("lastPath", "");
			
		JFileChooser jfc = null;
		if (lastPath.length() > 0) {
		    jfc = new JFileChooser(lastPath);
		} else{
		    jfc = new JFileChooser();
		}
		
		jfc.setDialogTitle("Open File");
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);

		jfc.addChoosableFileFilter(new OpenFilterCsv());
		jfc.addChoosableFileFilter(new OpenFilterTsv());
		jfc.addChoosableFileFilter(new FileFilterTxt());

		int result = jfc.showOpenDialog(UnifiedAccessPoint.getInstanceFrame());
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();

			if (file != null) {
				SwingUtilities.invokeLater(()->{
					textField.setText(file.getAbsolutePath());
				} );
				
				parserFileIntoModule(file);
				
				jfc.setCurrentDirectory(file.getParentFile());
				pref.put("lastPath",file.getParent());
			}
		}
	}
	private void parserFileIntoModule(File file) {
		int numOfEle = getNumOfElementsForEachLine(file);
		
		if (isInputStringIsSimpleString()) {
			loading(numOfEle,file);					
		}else {
			numOfEle /= 3;
			loading(numOfEle,file);	
		}
		
	}
	
	private void loading(int numOfEle,File file) {
		StringBuilder[] sBuilders = getStringBuilders(numOfEle);
		
		try (BufferedReader br = new BufferedReader(new FileReader(file));){
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0 || line.startsWith("#")) {
					continue;
				}
				
				String[] split = line.split(",|\\s",-1);
				for (int i = 0; i < numOfEle; i++) {
					sBuilders[i].append(split[i]);
					sBuilders[i].append("\n");
				}
			}
			
			for (int i = 0; i < numOfEle; i++) {
				sBuilders[i].deleteCharAt(sBuilders[i].length()-1);;
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < numOfEle; i++) {
			addIterm();
		}
		
		for (int i = 0; i < numOfEle; i++) {
			JPanelForInputData first = (JPanelForInputData) jPanelForItems.getComponent(i);
			first.getjTextArea().setText(sBuilders[i].toString());	
		}
	}
	private StringBuilder[] getStringBuilders(int numOfEle) {
		StringBuilder[] bReaders = new StringBuilder[numOfEle];
		for (int i = 0; i < numOfEle; i++) {
			bReaders[i] = new StringBuilder();
		}
		return bReaders;
	}
	private int getNumOfElementsForEachLine(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file));){
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0 || line.startsWith("#")) {
					continue;
				}
				
				String[] split = line.split(",|\\s",-1);
				return split.length;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	class JPanelForInputData extends JPanel{
		private static final long serialVersionUID = -7685146971670631647L;
		
		JTextArea jTextArea = new JTextArea();
		JTextArea topTextField = new JTextArea();
		JLabel bottomTextField = new JLabel("       0");

		public JScrollPane getjTextAreaShell() {
			jTextArea.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED) );
			JScrollPane jScrollPane = new JScrollPane(jTextArea);
			
			jTextArea.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					updateCount(jTextArea.getText());
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					updateCount(jTextArea.getText());
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					
				}
			});
			
			return jScrollPane;
		}
		
		protected void updateCount(String text) {
			String[] split = text.split("\\s+");
			int num = 0;
			for (String string : split) {
				if (string.length() > 0) {
					num ++;
				}
			}
			bottomTextField.setText("       "+num);
		}

		public JTextArea getjTextArea() {
			return jTextArea;
		}
		public JTextArea getTopTextField() {
			topTextField.setMaximumSize(new Dimension(itemWidth, heightForTextField));
			topTextField.setBorder(new BevelBorder(BevelBorder.LOWERED) );
			return topTextField;
		}
		public JLabel getBottomTextField() {
			bottomTextField.setBorder(new LineBorder(Color.gray) );
			bottomTextField.setMaximumSize(new Dimension(itemWidth, heightForTextField));
			bottomTextField.setEnabled(false);
			return bottomTextField;
		}
		
	}
	

}

