package module.evolview.moderntreeviewer.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import egps2.frame.gui.EGPSSwingUtil;
import egps2.panels.dialog.SwingDialog;
import egps2.frame.gui.comp.toggle.toggle.ToggleAdapter;
import egps2.frame.gui.comp.toggle.toggle.ToggleButton;
import egps2.UnifiedAccessPoint;
import evoltree.struct.util.EvolNodeUtil;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator.LongestRoot2leafBean;
import module.evolview.moderntreeviewer.MTreeViewMainFace;
import module.pill.images.ImageUtils;

public class CreativeModeTaskPanel extends JPanel {

    private TreeLayoutProperties treeLayoutProperties;

    private MTreeViewMainFace main;

    /**
     * Create the panel.
     */
    public CreativeModeTaskPanel() {

        Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
        Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

        setBorder(new EmptyBorder(8, 15, 8, 15));
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        JLabel lblNewLabel_1 = new JLabel("Mode Switch Button");
        lblNewLabel_1.setFont(titleFont);
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 0;
        add(lblNewLabel_1, gbc_lblNewLabel_1);

        ToggleButton toggleButtonCreative = getToggleButtonCreative(defaultFont);
        GridBagConstraints gbc_toggleButtonCreative = new GridBagConstraints();
        gbc_toggleButtonCreative.fill = GridBagConstraints.BOTH;
        gbc_toggleButtonCreative.insets = new Insets(0, 0, 5, 0);
        gbc_toggleButtonCreative.gridx = 0;
        gbc_toggleButtonCreative.gridy = 1;
        add(toggleButtonCreative, gbc_toggleButtonCreative);


        toggleButtonCreative.addEventToggleSelected(new ToggleAdapter() {
            @Override
            public void onSelected(boolean selected) {
                if (treeLayoutProperties == null) {
                    return;
                }
                treeLayoutProperties.setCreativeMode(selected);
                if (selected) {
                    main.invokeTheFeatureMethod(4);
                }
            }
        });

        JButton buttonAlign2right = getButtonAlign2right(defaultFont);

        JLabel lblNewLabel = new JLabel("Quick operation: ");
        lblNewLabel.setFont(titleFont);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 3;
        add(lblNewLabel, gbc_lblNewLabel);
        GridBagConstraints gbc_buttonAlign2right = new GridBagConstraints();
        gbc_buttonAlign2right.fill = GridBagConstraints.BOTH;
        gbc_buttonAlign2right.insets = new Insets(0, 0, 5, 0);
        gbc_buttonAlign2right.gridx = 0;
        gbc_buttonAlign2right.gridy = 4;
        add(buttonAlign2right, gbc_buttonAlign2right);

        JButton buttonSetAllBranches = getButtonSetAllBranches(defaultFont);
        GridBagConstraints gbc_buttonSetAllBranches = new GridBagConstraints();
        gbc_buttonSetAllBranches.fill = GridBagConstraints.BOTH;
        gbc_buttonSetAllBranches.insets = new Insets(0, 0, 5, 0);
        gbc_buttonSetAllBranches.gridx = 0;
        gbc_buttonSetAllBranches.gridy = 5;
        add(buttonSetAllBranches, gbc_buttonSetAllBranches);

        JButton btn_load = new JButton("Load image from clipboard");
        btn_load.setFont(defaultFont);
        btn_load.setToolTipText("Load image from clipboard, remove is already existed.");
        btn_load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    loadImageFrom();
                });
            }
        });
        Icon icon = ImageUtils.getIcon("clipboard-solid.png");
        btn_load.setIcon(icon);
        btn_load.setRequestFocusEnabled(false);

        // GridBagLayout 这里帮我添加一个separator
        // 添加一个分隔符
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // 分隔符所在的列
        gbc.gridy = 6; // 分隔符所在的行
        gbc.gridwidth = GridBagConstraints.REMAINDER; // 分隔符跨越所有剩余列
        gbc.fill = GridBagConstraints.HORIZONTAL; // 分隔符填充水平空间
        gbc.insets = new Insets(5, 0, 5, 0); // 上下间距
        add(separator, gbc);


        GridBagConstraints gbc_buttonLoad = new GridBagConstraints();
        gbc_buttonLoad.fill = GridBagConstraints.BOTH;
        gbc_buttonLoad.gridx = 0;
        gbc_buttonLoad.gridy = 7;
        gbc_buttonLoad.insets = new Insets(0, 0, 5, 0);
        add(btn_load, gbc_buttonLoad);
    }

    private void loadImageFrom() {
        if (treeLayoutProperties == null) {
            EGPSSwingUtil.promote(5, "Missing Tree", "Please load the phylogenetic tree first.", main);
            return;
        }
        if (!treeLayoutProperties.isCreativeMode()) {
            JOptionPane.showMessageDialog(main, "You are not in creative mode, you can't load image from clipboard.");
            return;
        }
        try {
            Image backgroundImage4CreativeMode = treeLayoutProperties.getBackgroundImage4CreativeMode();
            if (backgroundImage4CreativeMode == null) {
                Image image = EGPSSwingUtil.getImageFromClipboard();
                if (image == null) {
                    JOptionPane.showMessageDialog(main, "You need to copy a image.");
                } else {
                    treeLayoutProperties.setBackgroundImage4CreativeMode(image);
                }
            } else {
                treeLayoutProperties.setBackgroundImage4CreativeMode(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        main.getSelectedPhylogenticTreePanel().refreshViewPort();
    }

    private static ToggleButton getToggleButtonCreative(Font defaultFont) {
        ToggleButton toggleButtonCreative = new ToggleButton();
        toggleButtonCreative.setForeground(new Color(40, 139, 236));
        toggleButtonCreative.setText("Toggle creative mode");
        toggleButtonCreative.setFocusable(false);
        toggleButtonCreative.setFont(defaultFont);
        toggleButtonCreative.setMinimumSize(new Dimension(80, 35));
        toggleButtonCreative.setToolTipText(
                "Click to toggle free-create mode, enable users to free drag, create and manipulate the tree nodes.");
        return toggleButtonCreative;
    }

    private JButton getButtonSetAllBranches(Font defaultFont) {
        JButton buttonSetAllBranches = new JButton("Set all branches to equal length");
        buttonSetAllBranches.setHorizontalAlignment(SwingConstants.LEFT);
        buttonSetAllBranches.setFont(defaultFont);
        buttonSetAllBranches.addActionListener(e -> {
            if (main == null) {
                return;
            }

            PhylogeneticTreePanel tree = main.getSelectedPhylogenticTreePanel();

            SwingWorker<Void, Void> swingWorker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        GraphicsNode rootNode = tree.getRootNode();
                        EvolNodeUtil.recursiveIterateTreeIF(rootNode, node -> node.setRealBranchLength(1));
                    } catch (Exception e2) {
                        SwingDialog.showErrorMSGDialog("Running error", e2.getMessage());
                    }
                    return null;
                }

                @Override
                protected void done() {
                    tree.updatePhylogeneticTreePanel4fitFrame();
                }
            };

            swingWorker.execute();

        });
        return buttonSetAllBranches;
    }

    private JButton getButtonAlign2right(Font defaultFont) {
        JButton buttonAlign2right = new JButton("Align all leaves to right side");
        buttonAlign2right.setHorizontalAlignment(SwingConstants.LEFT);
        buttonAlign2right.setFont(defaultFont);
        buttonAlign2right.addActionListener(e -> {
            if (main == null) {
                return;
            }

            PhylogeneticTreePanel tree = main.getSelectedPhylogenticTreePanel();

            SwingWorker<Void, Void> swingWorker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    GraphicsNode rootNode = tree.getRootNode();
                    LongestRoot2leafBean maxLengthOfRoot2Leaf = GraphicTreePropertyCalculator
                            .getMaxLengthOfRoot2Leaf(rootNode);
                    List<GraphicsNode> leaves = EvolNodeUtil.getLeaves(rootNode);

                    for (GraphicsNode leaf : leaves) {
                        if (leaf != maxLengthOfRoot2Leaf.getLeaf()) {
                            double lengthFromNode2root = EvolNodeUtil.getLengthFromNode2root(leaf, rootNode);
                            double plus = maxLengthOfRoot2Leaf.getLength() - lengthFromNode2root;
                            leaf.setRealBranchLength(leaf.getRealBranchLength() + plus);
                        }
                    }

                    return null;
                }

                @Override
                protected void done() {
                    tree.updatePhylogeneticTreePanel4fitFrame();
                }
            };

            swingWorker.execute();
        });
        return buttonAlign2right;
    }

    public void setTreeLayoutProperties(TreeLayoutProperties treeLayoutProperties) {
        this.treeLayoutProperties = treeLayoutProperties;
    }

    public void setMain(MTreeViewMainFace mainFace) {
        this.main = mainFace;
    }

}
