package module.sankeyplot.gui;

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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;

import egps2.utils.common.math.CheckedNumber;
import egps2.utils.common.model.filefilter.FileFilterTxt;
import egps2.utils.common.model.filefilter.OpenFilterCsv;
import egps2.utils.common.model.filefilter.OpenFilterTsv;
import egps2.UnifiedAccessPoint;
import module.vennplot.model.Element;
import module.vennplot.model.SetItem;

@SuppressWarnings("serial")
public class SankeyPlotDataInputPanel extends JPanel {

    private JTextField textField;

	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

    final int itemWidth = 120;
    final private int heightForTextField = 20;

    private JPanel jPanelForItems;
    private JButton btnRemoveAllSet;
    private JButton btn_RemoveSetItem;
    private JButton btn_addSetItem;
    private int numOfIterms = 0;
    private JScrollPane scrollPane;

    private int displaySampleIndex = 0;

    protected final int leftColumnSize = 150;

    private String errorString;
    private JButton btnVisualize;

    /**
     * Create the panel.
     */
    public SankeyPlotDataInputPanel() {
        initializaContent();

    }

    private void initializaContent() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel top = new JPanel();
        top.setBackground(Color.white);
        top.setBorder(new EmptyBorder(5, 0, 5, 0));
        add(top, BorderLayout.NORTH);
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        JLabel labelInputFilePath = new JLabel("  Input file path: ");
        labelInputFilePath.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelInputFilePath.setFont(defaultFont);
        top.add(labelInputFilePath);
        Dimension preferredSize = labelInputFilePath.getPreferredSize();
        labelInputFilePath.setPreferredSize(new Dimension(leftColumnSize, preferredSize.height));

        Dimension dimension = new Dimension(40, 0);

        textField = new JTextField();
        textField.setEditable(false);
        textField.setFont(defaultFont);
        top.add(textField);

        top.add(Box.createRigidArea(dimension));

        JButton buttonImportFile = new JButton("Import file");
        buttonImportFile.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonImportFile.setToolTipText(
            "<html><body>Click this button to import data.<br><br>You should import a file like:<br>a\tb\tc<br>d\te\tf<br>");
        buttonImportFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeAllIterms();
                loadingDataOperation();
            }

        });
        buttonImportFile.setFont(defaultFont);
        top.add(buttonImportFile);

        JPanel bottom = new JPanel();
        bottom.setBorder(new EmptyBorder(5, 0, 5, 0));
        bottom.setBackground(Color.white);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        add(bottom, BorderLayout.SOUTH);

        bottom.add(Box.createRigidArea(new Dimension(leftColumnSize, 0)));
        btn_addSetItem = new JButton("Add set item");
        btn_addSetItem.setFont(defaultFont);
        bottom.add(btn_addSetItem);

        Dimension gap = new Dimension(7, 0);
		bottom.add(Box.createRigidArea(gap));
        btn_RemoveSetItem = new JButton("Remove last item");
        btn_RemoveSetItem.setFont(defaultFont);
        bottom.add(btn_RemoveSetItem);

        bottom.add(Box.createRigidArea(gap));
        btnRemoveAllSet = new JButton("Remove all sets");
        btnRemoveAllSet.setFont(defaultFont);
        bottom.add(btnRemoveAllSet);

        bottom.add(Box.createHorizontalGlue());
        JButton btnExample = new JButton("Display example");
        btnExample.addActionListener(e -> {
            displayExamples();
        });
        btnExample.setFont(defaultFont);
        bottom.add(btnExample);
        
        bottom.add(Box.createRigidArea(gap));
        btnVisualize = new JButton("Visualize");
        bottom.add(btnVisualize);
        btnVisualize.setFont(defaultFont);

        btn_addSetItem.addActionListener(e -> {
            addIterm();
        });
        btnRemoveAllSet.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                removeAllIterms();
            });
        });
        btn_RemoveSetItem.addActionListener(e -> {
            removeLastItem();
        });

        JPanel left = new JPanel();
        left.setBorder(new EmptyBorder(5, 10, 0, 0));
        left.setBackground(Color.WHITE);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        left.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel jLabelName = new JLabel("Name:");
        jLabelName.setHorizontalAlignment(SwingConstants.LEFT);
        jLabelName.setFont(defaultFont);
        left.add(jLabelName);

        left.add(Box.createRigidArea(new Dimension(0, 16)));

        JLabel lblCount = new JLabel("Count:");
        lblCount.setBackground(Color.WHITE);
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

        left.setPreferredSize(new Dimension(leftColumnSize, left.getPreferredSize().height));

        jPanelForItems = new JPanel();
        jPanelForItems.setLayout(new BoxLayout(jPanelForItems, BoxLayout.X_AXIS));

        scrollPane = new JScrollPane(jPanelForItems, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            addIterm();
            addIterm();
            addIterm();
            jPanelForItems.revalidate();
        });
    }

    protected void displayExamples() {

        if (displaySampleIndex == 3) {
            displaySampleIndex = 0;
        }
        removeAllIterms();

        String text = "";

        if (displaySampleIndex == 0) {
            addIterm();
            addIterm();
            addIterm();

            JPanelForInputData first = (JPanelForInputData)jPanelForItems.getComponent(0);

            first.getTopTextField().setText("LncRNA");
            text = "1\n1\n1\n1\n1\n2\n2\n3\n3\n3\n3\n3\n4\n4\n4\n4\n4";

            first.getjTextArea().setText(text);

            JPanelForInputData second = (JPanelForInputData)jPanelForItems.getComponent(1);

            second.getTopTextField().setText("miRNA");
            text = "a\na\nb\nd\nd\nc\nc\nc\nc\nb\nb\nb\na\na\na\ne\ne";

            second.getjTextArea().setText(text);

            JPanelForInputData third = (JPanelForInputData)jPanelForItems.getComponent(2);

            third.getTopTextField().setText("Gene");
            text = "3q\n3q\n5q\n5q\n4q\n4q\n4q\n1q\n1q\n1q\n2q\n2q\n2q\n2q\n2q\n3q\n3q";

            third.getjTextArea().setText(text);
        } else if (displaySampleIndex == 1) {

            addIterm();
            addIterm();
            addIterm();
            addIterm();

            JPanelForInputData first = (JPanelForInputData)jPanelForItems.getComponent(0);

            first.getTopTextField().setText("Set1 Name");
            text = "1\n1\n1\n1\n1\n2\n2\n3\n3\n3\n3\n3\n4\n4\n4\n4\n4";
            first.getjTextArea().setText(text);

            JPanelForInputData second = (JPanelForInputData)jPanelForItems.getComponent(1);

            second.getTopTextField().setText("Set2 Name");
            text = "a\na\nb\nd\nd\nc\nc\nc\nc\nb\nb\nb\na\na\na\ne\ne";
            second.getjTextArea().setText(text);

            JPanelForInputData third = (JPanelForInputData)jPanelForItems.getComponent(2);

            third.getTopTextField().setText("Set3 Name");
            text = "3q\n3q\n5q\n5q\n4q\n4q\n4q\n1q\n1q\n1q\n2q\n2q\n2q\n2q\n2q\n3q\n3q";
            third.getjTextArea().setText(text);

            JPanelForInputData fourth = (JPanelForInputData)jPanelForItems.getComponent(3);

            fourth.getTopTextField().setText("Set4 Name");
            text = "a2\na2\nb2\nd2\nd2\nc2\nc2\nc2\nc2\nb2\nb2\nb2\na2\na2\na2\ne2\ne2";
            fourth.getjTextArea().setText(text);

        } else {

            addIterm();
            addIterm();
            addIterm();
            addIterm();
            addIterm();

            JPanelForInputData first = (JPanelForInputData)jPanelForItems.getComponent(0);

            first.getTopTextField().setText("Set1 Name");
            text = "1\n1\n1\n1\n1\n2\n2\n3\n3\n3\n3\n3\n4\n4\n4\n4\n4";
            first.getjTextArea().setText(text);

            JPanelForInputData second = (JPanelForInputData)jPanelForItems.getComponent(1);

            second.getTopTextField().setText("Set2 Name");
            text = "a\na\nb\nd\nd\nc\nc\nc\nc\nb\nb\nb\na\na\na\ne\ne";
            second.getjTextArea().setText(text);

            JPanelForInputData third = (JPanelForInputData)jPanelForItems.getComponent(2);

            third.getTopTextField().setText("Set3 Name");
            text = "3q\n3q\n5q\n5q\n4q\n4q\n4q\n1q\n1q\n1q\n2q\n2q\n2q\n2q\n2q\n3q\n3q";
            third.getjTextArea().setText(text);

            JPanelForInputData fourth = (JPanelForInputData)jPanelForItems.getComponent(3);

            fourth.getTopTextField().setText("Set4 Name");
            text = "a2\na2\nb2\nd2\nd2\nc2\nc2\nc2\nc2\nb2\nb2\nb2\na2\na2\na2\ne2\ne2";
            fourth.getjTextArea().setText(text);

            JPanelForInputData fifth = (JPanelForInputData)jPanelForItems.getComponent(4);

            fifth.getTopTextField().setText("Set4 Name");
            text = "1t\n1t\n1t\n1t\n1t\n2t\n2t\n3t\n3t\n3t\n3t\n3t\n4t\n4t\n4t\n4t\n4";
            fifth.getjTextArea().setText(text);
        }

        displaySampleIndex++;
    }

    private void removeLastItem() {

        numOfIterms--;
        SwingUtilities.invokeLater(() -> {
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
        } else {
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
        // scrollPane.updateUI();
        jPanelForItems.revalidate();

    }

    /**
     * YDL: This is the core function for poster analysis!
     * 
     * @return null to reject analysis!
     */
    public SetItem[] getSetItems() {

        errorString = "You do not import the data yet! Please import data!";

        int length = jPanelForItems.getComponentCount();
        if (length == 0) {
            return null;
        }

        List<String> names = new ArrayList<String>();
        List<String> contents = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            JPanelForInputData jData = (JPanelForInputData)jPanelForItems.getComponent(i);
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

        boolean ifGenomicRegion = false;
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
                            errorString =
                                "Sorry! you input element: " + string + " in \"" + setName + "\" set is not correct!";
                            return null;
                        }

                        boolean p1 = CheckedNumber.isPositiveInteger(split[1], false);
                        boolean p2 = CheckedNumber.isPositiveInteger(split[2], false);
                        if (!p1 || !p2) {
                            errorString =
                                "Sorry! you input element: " + string + " in \"" + setName + "\" set is not correct!";
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
    
    public void setAction4VisualizeButton(Runnable run) {
    	btnVisualize.addActionListener(e->{
    		run.run();
    		
    		Component root = SwingUtilities.getRoot(SankeyPlotDataInputPanel.this);
    		if (root instanceof JDialog) {
				((JDialog) root).dispose();
			}
    	});
	}

    private void loadingDataOperation() {
        Preferences pref = Preferences.userNodeForPackage(this.getClass());
        String lastPath = pref.get("lastPath", "");

        JFileChooser jfc = null;
        if (lastPath.length() > 0) {
            jfc = new JFileChooser(lastPath);
        } else {
            jfc = new JFileChooser();
        }

        jfc.setDialogTitle("Open File");
        jfc.setMultiSelectionEnabled(false);
        jfc.setAcceptAllFileFilterUsed(false);

		OpenFilterCsv filterOfCSV = new OpenFilterCsv();
		jfc.addChoosableFileFilter(filterOfCSV);
		OpenFilterTsv filterOfTsv = new OpenFilterTsv();
		jfc.addChoosableFileFilter(filterOfTsv);
		FileFilterTxt filterOfTxt = new FileFilterTxt();
		jfc.addChoosableFileFilter(filterOfTxt);

        int result = jfc.showOpenDialog(UnifiedAccessPoint.getInstanceFrame());

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
			FileFilter fileFilter = jfc.getFileFilter();
			String splitter = null;
			if (Objects.equal(filterOfCSV.getDescription(), fileFilter.getDescription())) {
				splitter = ",";
			} else if (Objects.equal(filterOfTsv.getDescription(), fileFilter.getDescription())) {
				splitter = "\t";
			} else {
				splitter = ",|\t";
			}
            if (file != null) {
                SwingUtilities.invokeLater(() -> {
                    textField.setText(file.getAbsolutePath());
                });

				parserFileIntoModule(file, splitter);

                jfc.setCurrentDirectory(file.getParentFile());
                pref.put("lastPath", file.getParent());
            }
        }
    }

	private void parserFileIntoModule(File file, String splitter) {
		int numOfEle = getNumOfElementsForEachLine(file, splitter);

		loading(numOfEle, file, splitter);

    }

	private void loading(int numOfEle, File file, String splitter) {
        StringBuilder[] sBuilders = getStringBuilders(numOfEle);

		boolean isFirstLine = true;
		String[] headerLineStrs = new String[numOfEle];
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            String line = null;
            while ((line = br.readLine()) != null) {
				line = StringUtils.stripEnd(line, "\r");
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }

				String[] split = line.split(splitter, -2);
				if (isFirstLine) {
					headerLineStrs = split;
					isFirstLine = false;
					continue;
				}


                for (int i = 0; i < numOfEle; i++) {
                    String str = split[i];
					if (str.isEmpty()) {
						continue;
					}
					sBuilders[i].append(str);
                    sBuilders[i].append("\n");
                }
            }

            for (int i = 0; i < numOfEle; i++) {
				sBuilders[i].deleteCharAt(sBuilders[i].length() - 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numOfEle; i++) {
            addIterm();
        }

        for (int i = 0; i < numOfEle; i++) {
            JPanelForInputData first = (JPanelForInputData)jPanelForItems.getComponent(i);
			first.getTopTextField().setText(headerLineStrs[i]);
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

	private int getNumOfElementsForEachLine(File file, String splitter) {
		int ret = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }

				String[] split = line.split(splitter, -1);
				ret = split.length;
				break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return ret;
    }

    class JPanelForInputData extends JPanel {
        private static final long serialVersionUID = -7685146971670631647L;

        JTextArea jTextArea = new JTextArea();
        JTextArea topTextField = new JTextArea();
        JLabel bottomTextField = new JLabel("       0");

        public JScrollPane getjTextAreaShell() {
            jTextArea.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
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
                    num++;
                }
            }
            bottomTextField.setText("       " + num);
        }

        public JTextArea getjTextArea() {
            return jTextArea;
        }

        public JTextArea getTopTextField() {
            topTextField.setMaximumSize(new Dimension(itemWidth, heightForTextField));
            topTextField.setBorder(new BevelBorder(BevelBorder.LOWERED));
            return topTextField;
        }

        public JLabel getBottomTextField() {
            bottomTextField.setBorder(new LineBorder(Color.gray));
            bottomTextField.setMaximumSize(new Dimension(itemWidth, heightForTextField));
            bottomTextField.setEnabled(false);
            return bottomTextField;
        }

    }

}
