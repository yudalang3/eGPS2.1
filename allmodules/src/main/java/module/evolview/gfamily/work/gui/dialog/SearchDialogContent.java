package module.evolview.gfamily.work.gui.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import egps2.UnifiedAccessPoint;
import evoltree.struct.util.EvolNodeUtil;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public class SearchDialogContent extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTextField inputTextField;
    private JTextField consoleTextField;
    private JButton findNextButton;
    private JButton findPrevButton;
    private JButton highlightAllButton;
    private ArrayList<GraphicsNode> findRets = Lists.newArrayList();
    ;
    private JButton closeButton;
    private JButton clearHighlightsButton;

    private GeneFamilyController controller;

	private FindSearchRecord findSearchRecord = new FindSearchRecord();

    /**
     * Create the panel.
     *
     * @param controller
     */
    public SearchDialogContent(GeneFamilyController controller) {
        this.controller = controller;

        Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
        setBorder(new EmptyBorder(6, 12, 6, 6));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 8, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JLabel lblNewLabel = new JLabel("Find: ");
        lblNewLabel.setFont(defaultFont);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        add(lblNewLabel, gbc_lblNewLabel);

        inputTextField = new JTextField();
        inputTextField.setFont(defaultFont);
        GridBagConstraints gbc_inputTextField = new GridBagConstraints();
        gbc_inputTextField.gridwidth = 2;
        gbc_inputTextField.insets = new Insets(0, 0, 5, 5);
        gbc_inputTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_inputTextField.gridx = 1;
        gbc_inputTextField.gridy = 0;
        add(inputTextField, gbc_inputTextField);
        inputTextField.setColumns(10);

        findNextButton = new JButton("Find");
        findNextButton.addActionListener(this);
        findNextButton.setFont(defaultFont);
        GridBagConstraints gbc_findNextButton = new GridBagConstraints();
        gbc_findNextButton.anchor = GridBagConstraints.WEST;
        gbc_findNextButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_findNextButton.insets = new Insets(0, 0, 5, 0);
        gbc_findNextButton.gridx = 3;
        gbc_findNextButton.gridy = 0;
        add(findNextButton, gbc_findNextButton);

        findPrevButton = new JButton("Find Prev");
        findPrevButton.addActionListener(this);
        findPrevButton.setFont(defaultFont);
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.anchor = GridBagConstraints.EAST;
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 1;
        gbc_btnNewButton.gridy = 1;
        add(findPrevButton, gbc_btnNewButton);

        highlightAllButton = new JButton("Highlight All");
        highlightAllButton.addActionListener(this);
        highlightAllButton.setFont(defaultFont);
        GridBagConstraints gbc_highlightAllButton = new GridBagConstraints();
        gbc_highlightAllButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_highlightAllButton.anchor = GridBagConstraints.WEST;
        gbc_highlightAllButton.insets = new Insets(0, 0, 5, 5);
        gbc_highlightAllButton.gridx = 2;
        gbc_highlightAllButton.gridy = 1;
        add(highlightAllButton, gbc_highlightAllButton);

        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        closeButton.setFont(defaultFont);
        GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
        gbc_btnNewButton_2.anchor = GridBagConstraints.WEST;
        gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton_2.gridx = 3;
        gbc_btnNewButton_2.gridy = 1;
        add(closeButton, gbc_btnNewButton_2);

        clearHighlightsButton = new JButton("Clear Highlights");
        clearHighlightsButton.setFont(defaultFont);
        clearHighlightsButton.addActionListener(this);
        GridBagConstraints gbc_clearHighlightsButton = new GridBagConstraints();
        gbc_clearHighlightsButton.anchor = GridBagConstraints.WEST;
        gbc_clearHighlightsButton.insets = new Insets(0, 0, 5, 5);
        gbc_clearHighlightsButton.gridx = 2;
        gbc_clearHighlightsButton.gridy = 2;
        add(clearHighlightsButton, gbc_clearHighlightsButton);

        JLabel lblNewLabel_1 = new JLabel("Console: ");
        lblNewLabel_1.setFont(defaultFont);
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 3;
        add(lblNewLabel_1, gbc_lblNewLabel_1);

        consoleTextField = new JTextField("Please input string");
        consoleTextField.setFont(defaultFont);
        consoleTextField.setForeground(Color.lightGray);
        GridBagConstraints gbc_consoleTextField = new GridBagConstraints();
        gbc_consoleTextField.gridwidth = 3;
        gbc_consoleTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_consoleTextField.gridx = 1;
        gbc_consoleTextField.gridy = 3;
        add(consoleTextField, gbc_consoleTextField);
        consoleTextField.setColumns(10);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
		String text = inputTextField.getText();
		if (findNextButton == e.getSource()) {
			if (Objects.equals(findSearchRecord.getStringToFind(), text)){
                findSearchRecord.incrementCurrentHightNodeIndex();
				if (findSearchRecord.getCurrentHightNodeIndex() >= findRets.size()) {
                    findSearchRecord.resetToZero();
				}
                if (findRets.isEmpty()){
                    return;
                }
				for (GraphicsNode graphicsNode : findRets) {
					graphicsNode.getDrawUnit().setNodeSelected(false);
				}
				findRets.get(findSearchRecord.getCurrentHightNodeIndex()).getDrawUnit().setNodeSelected(true);
				controller.refreshPhylogeneticTree();
			}else{
                findSearchRecord.resetToZero();
				boolean targets = findTargets();
				if (targets) {
					return;
				}
				int size = findRets.size();
				String concat = String.valueOf(size).concat("  nodes found.");
				consoleTextField.setText(concat);
				findRets.get(findSearchRecord.getCurrentHightNodeIndex()).getDrawUnit().setNodeSelected(true);
				controller.refreshPhylogeneticTree();
			}
        } else if (highlightAllButton == e.getSource()) {
            boolean targets = findTargets();
            if (targets) {
                return;
            }

            for (GraphicsNode graphicsNode : findRets) {
                graphicsNode.getDrawUnit().setNodeSelected(true);
            }

            disposeThis();

            controller.refreshPhylogeneticTree();
        } else if (clearHighlightsButton == e.getSource()) {
            TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
            GraphicsNode rootNode = treeLayoutProperties.getOriginalRootNode();
            EvolNodeUtil.recursiveIterateTreeIF(rootNode, node -> {
                node.getDrawUnit().setNodeSelected(false);
            });
            controller.refreshPhylogeneticTree();

        } else if (findPrevButton == e.getSource()) {
            if (findRets.isEmpty()) {
                consoleTextField.setText("Current no nodes found.");
            } else {
                findSearchRecord.decreaseCurrentHightNodeIndex();
                if (findSearchRecord.getCurrentHightNodeIndex() < 0) {
                    short currentHightNodeIndex = (short) (findRets.size() - 1);
                    findSearchRecord.setCurrentHightNodeIndex(currentHightNodeIndex);
                }
                for (GraphicsNode graphicsNode : findRets) {
                    graphicsNode.getDrawUnit().setNodeSelected(false);
                }
                findRets.get(findSearchRecord.getCurrentHightNodeIndex()).getDrawUnit().setNodeSelected(true);
                controller.refreshPhylogeneticTree();
            }
        } else if (closeButton == e.getSource()) {
            disposeThis();
        }

		findSearchRecord.setStringToFind(text);
    }

    /**
     * @return true if NOT find  any target
     */
    public boolean findTargets() {
        findRets.clear();
        String text = inputTextField.getText();
        if (text.isEmpty()) {
            consoleTextField.setText("Please input at least a character.");
            return true;
        }

        TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
        if (treeLayoutProperties == null) {
            consoleTextField.setText("Please input the tree first.");
            return true;
        }
        GraphicsNode rootNode = treeLayoutProperties.getOriginalRootNode();

        //不一致才要重新搜索
        EvolNodeUtil.recursiveIterateTreeIF(rootNode, node -> {
            String name = node.getName();
            if (Strings.isNullOrEmpty(name)) {
                return;
            }
            if (searchCriterial(name, text)) {
                findRets.add(node);
            }

        });

        return false;
    }

    private void disposeThis() {
        findRets.clear();
        JDialog root = (JDialog) SwingUtilities.getRoot(this);
        root.dispose();
    }

    private boolean searchCriterial(String nodeName, String searchChars) {
        boolean ret = nodeName.contains(searchChars);
        return ret;
    }

}
