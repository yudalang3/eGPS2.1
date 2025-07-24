package module.evolview.gfamily.work.gui.tree;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import org.apache.commons.io.FileUtils;

import egps2.panels.dialog.EGPSFileChooser;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.colorscheme.BaseContentPanel;


@SuppressWarnings("serial")
public abstract class AbstractContentPanelCustomized extends BaseContentPanel {
    public JTextField textField;
    public JTextPane textArea;
    public GeneFamilyController controller;
    public Runnable callBack;
	private MouseAdapter mouseListener;

    public AbstractContentPanelCustomized(GeneFamilyController controller, String context, Runnable callBack) {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
        setLayout(new BorderLayout(0, 0));
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panel, BorderLayout.NORTH);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{72, 66, 57, 0};
        gbl_panel.rowHeights = new int[]{23, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        JLabel lblInputFile = new JLabel("Input file: ");
        lblInputFile.setFont(globalFont);
        GridBagConstraints gbc_lblInputFile = new GridBagConstraints();
        gbc_lblInputFile.anchor = GridBagConstraints.WEST;
        gbc_lblInputFile.insets = new Insets(0, 0, 0, 5);
        gbc_lblInputFile.gridx = 0;
        gbc_lblInputFile.gridy = 0;
        panel.add(lblInputFile, gbc_lblInputFile);

        textField = new JTextField();
        textField.setFont(globalFont);
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.anchor = GridBagConstraints.WEST;
        gbc_textField.insets = new Insets(0, 0, 0, 5);
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        panel.add(textField, gbc_textField);
        textField.setColumns(10);

        JButton btnLoad = new JButton("Load");
        btnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                EGPSFileChooser jFileChooser = new EGPSFileChooser(this.getClass());
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                SwingUtilities.invokeLater(() -> {
                    int showOpenDialog = jFileChooser.showOpenDialog(UnifiedAccessPoint.getInstanceFrame());
                    if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = jFileChooser.getSelectedFile();
                        try {
                            textArea.setForeground(Color.black);
                            textArea.setText("");
                            List<String> readLines = FileUtils.readLines(selectedFile);
                            StringBuilder stringBuilder = new StringBuilder();
                            readLines.forEach(string -> {
                                stringBuilder.append(string + "\n");
                            });
                            textArea.setText(stringBuilder.toString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        btnLoad.setFont(globalFont);
        GridBagConstraints gbc_btnLoad = new GridBagConstraints();
        gbc_btnLoad.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnLoad.gridx = 2;
        gbc_btnLoad.gridy = 0;
        panel.add(btnLoad, gbc_btnLoad);


        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextPane();
        //禁止自动换行
        textArea.setEditorKit(new ExtendedStyledEditorKit());
        textArea.setForeground(Color.lightGray);

        textArea.setText(context);

        scrollPane.setViewportView(textArea);

        MyListener myListener = new MyListener(() -> {
            textArea.setForeground(Color.black);
        });
        textArea.getDocument().addDocumentListener(myListener);
        
        mouseListener = new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		textArea.setForeground(Color.black);
        		textArea.removeMouseListener(mouseListener);
        		mouseListener = null;
        	}
		};
		textArea.addMouseListener(mouseListener);

        this.controller = controller;
        this.callBack = callBack;

    }

    public AbstractContentPanelCustomized(GeneFamilyController controller, String context) {
        this(controller, context, null);
    }
}


 class MyListener implements DocumentListener {

    private Runnable callBackFuntion;

    public MyListener(Runnable callBackFuntion) {
        this.callBackFuntion = callBackFuntion;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        usrEdited(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        usrEdited(e);

    }


    private void usrEdited(DocumentEvent e) {
        Document document = e.getDocument();
        document.removeDocumentListener(this);
        callBackFuntion.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

}

class ExtendedStyledEditorKit extends StyledEditorKit {
    private static final long serialVersionUID = 1L;

    private static final ViewFactory styledEditorKitFactory = (new StyledEditorKit()).getViewFactory();

    private static final ViewFactory defaultFactory = new ExtendedStyledViewFactory();

    public Object clone() {
        return new ExtendedStyledEditorKit();
    }

    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    /* The extended view factory */
    static class ExtendedStyledViewFactory implements ViewFactory {
        public View create(Element elem) {
            String elementName = elem.getName();
            if (elementName != null) {
                if (elementName.equals(AbstractDocument.ParagraphElementName)) {
                    return new ExtendedParagraphView(elem);
                }
            }

            // Delegate others to StyledEditorKit
            return styledEditorKitFactory.create(elem);
        }
    }

}

class ExtendedParagraphView extends ParagraphView {
    public ExtendedParagraphView(Element elem) {
        super(elem);
    }

    @Override
    public float getMinimumSpan(int axis) {
        return super.getPreferredSpan(axis);
    }
}


