package module.evolview.gfamily.work.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.model.tree.AnnotationsProperties;
import module.evolview.model.tree.GraphicsNode;

/**
 *
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 *
 * @ClassName NodeAnnotationDialog.java
 *
 * @Package egps.remnant.phylogenetictree.util
 *
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-04-25 11:29
 * 
 */
public class NodeAnnotationDialogContainer extends JDialog {

	private static final long serialVersionUID = -8208821326033273500L;

	private JSplitPane splitPane;
	private JPanel jContentPane = null;
	private JPanel southJPanel = null;
	private JTree jTree;
	private JScrollPane rightView;

	private DefaultMutableTreeNode treeRoot;

//	private GeneFamilyController controller;
	private PhylogeneticTreePanel phylogeneticTreePanel;
	private PhylogeneticTreePanel mainPhylogeneticTreePanel;
	private GraphicsNode curreNode;

	AnnotationsProperties currentAnnotationsProperties = new AnnotationsProperties();

	private GraphicsNode virtureRoot;
	
	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	public NodeAnnotationDialogContainer(PhylogeneticTreePanel phylogeneticTreePanel2, GraphicsNode curreNode) {
		super(UnifiedAccessPoint.getInstanceFrame(), "Internal node Annotation", true);
		this.mainPhylogeneticTreePanel = phylogeneticTreePanel2;
		setSize(900, 450);
		setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
		this.curreNode = curreNode;
		setContentPane(getJContentPane());
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				actionsForExitWinodow();
			}
		});

	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getCenterJPanel(), BorderLayout.CENTER);
			jContentPane.add(getSouthJPanel(), BorderLayout.SOUTH);

		}
		return jContentPane;
	}

	private JSplitPane getCenterJPanel() {
		if (splitPane == null) {
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setOneTouchExpandable(false);
			splitPane.setDividerSize(7);
			splitPane.setDividerLocation(250);

			treeRoot = new DefaultMutableTreeNode("");

			jTree = new JTree(treeRoot);
			jTree.setCellRenderer(new PreferenceTree());
			JScrollPane leftView = new JScrollPane(jTree);

			leftView.setBorder(BorderFactory.createLineBorder(Color.lightGray));

			DialogSidewardAnnotation peripheryAnnotation = new DialogSidewardAnnotation(this,
					"Sideward clade annotation", mainPhylogeneticTreePanel);
			treeRoot.add(peripheryAnnotation);

			DialogInternalNodeInsituAnnotation internalNodeAnnotation = new DialogInternalNodeInsituAnnotation(this,
					"Internal node in-situ annotation", mainPhylogeneticTreePanel);
			treeRoot.add(internalNodeAnnotation);

			DialogInternalNode2LeafAnnotation internalNode2leafAnnotation = new DialogInternalNode2LeafAnnotation(this,
					"Internal node to leaf annotation", mainPhylogeneticTreePanel);
			treeRoot.add(internalNode2leafAnnotation);

            DialogLeafNameAnnotation leafNameAnnotation = new DialogLeafNameAnnotation(this, "Leaf name annotation",
					mainPhylogeneticTreePanel);
            treeRoot.add(leafNameAnnotation);

            jTree.expandRow(0);
            jTree.setSelectionRow(1);
            jTree.setRootVisible(false);
            jTree.setFont(defaultFont);
            jTree.setRowHeight(20);
            //jTree.setBackground(new Color(240, 240, 240));

            rightView = new JScrollPane();
            rightView.setViewportView(peripheryAnnotation.getViewJPanel());
            rightView.setBorder(BorderFactory.createLineBorder(Color.lightGray));

            splitPane.add(leftView);

            splitPane.add(rightView);

            valueChangedJtree();

		}

		JSplitPane jSplitPane = new JSplitPane();
		jSplitPane.setOneTouchExpandable(false);
		jSplitPane.setDividerLocation(500);

		jSplitPane.setLeftComponent(splitPane);

		phylogeneticTreePanel = new PhylogeneticTreePanel(mainPhylogeneticTreePanel.getLayoutProperties(),
				getAVirtureRoot(), null, null);
		phylogeneticTreePanel.setMainAnnotationsProperties(currentAnnotationsProperties);
		
		TitledBorder border = new TitledBorder(null, "Preview", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null);
		
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		border.setTitleFont(defaultTitleFont);
		phylogeneticTreePanel.setBorder(border);
		
		jSplitPane.setRightComponent(phylogeneticTreePanel);

		SwingUtilities.invokeLater(() -> {
			
			AbstarctShowAnnotationDialog node = (AbstarctShowAnnotationDialog) jTree.getLastSelectedPathComponent();
			node.configurateAnnotationProperty2preview();
			phylogeneticTreePanel.setWidthAndHeight(400, 400);
			phylogeneticTreePanel.updateViewAccording2TreeLayoutForAnnotationDialog();
		
		});

		return jSplitPane;
	}

	public GraphicsNode getAVirtureRoot() {
		if (virtureRoot == null) {

			virtureRoot = new GraphicsNode("Root");
			
			GraphicsNode leaf1 = new GraphicsNode("This is leaf1");
			GraphicsNode leaf2 = new GraphicsNode("This is leaf2");
			GraphicsNode leaf3 = new GraphicsNode("This is leaf3");
			GraphicsNode leaf4 = new GraphicsNode("This is leaf4");
			GraphicsNode iNode1 = new GraphicsNode("This is iNode1");
			GraphicsNode iNode2 = new GraphicsNode("This is iNode2");
			
			iNode1.addChild(leaf1);
			iNode1.addChild(leaf2);
			iNode2.addChild(leaf3);
			iNode2.addChild(leaf4);
			virtureRoot.addChild(iNode1);
			virtureRoot.addChild(iNode2);

		}

		return virtureRoot;
	}

	private void valueChangedJtree() {
		jTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				switchingOptions(jTree);
			}

			private void switchingOptions(JTree jTree) {
				AbstarctShowAnnotationDialog node = (AbstarctShowAnnotationDialog) jTree.getLastSelectedPathComponent();

				if (node == null) {
					return;
				}
				rightView.setViewportView(node.getViewJPanel());
				node.configurateAnnotationProperty2preview();
				phylogeneticTreePanel.updatePhylogeneticTreePanel();
			}
		});
	}

	private JPanel getSouthJPanel() {
		if (southJPanel == null) {
			southJPanel = new JPanel();
			southJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			JButton applyAnnotation = new JButton("OK");
			JButton cancelAnnotation = new JButton("Cancel");

			applyAnnotation.addActionListener(e -> {
				AbstarctShowAnnotationDialog node = (AbstarctShowAnnotationDialog) jTree.getLastSelectedPathComponent();
				if (node == null) {
					return;
				}
				
				PhylogeneticTreePanel selectedPhylogeneticTreePanel = mainPhylogeneticTreePanel;
				AnnotationsProperties mainAnnotationsProperties = selectedPhylogeneticTreePanel.getMainAnnotationsProperties();
				mainAnnotationsProperties.addAnnotationProperties(currentAnnotationsProperties,curreNode);
				selectedPhylogeneticTreePanel.setIsShowAnnotation(true);
				selectedPhylogeneticTreePanel.updatePhylogeneticTreePanel();
				dispose();
			});

			cancelAnnotation.addActionListener(e -> {
				actionsForExitWinodow();
			});

			applyAnnotation.setFont(defaultFont);
			cancelAnnotation.setFont(defaultFont);
			southJPanel.add(applyAnnotation);
			southJPanel.add(cancelAnnotation);
		}

		return southJPanel;
	}

	private void actionsForExitWinodow() {
		splitPane = null;
		jContentPane = null;
		southJPanel = null;
		jTree = null;
		rightView = null;
		treeRoot = null;
		defaultFont = null;
		phylogeneticTreePanel = null;
		curreNode = null;
		currentAnnotationsProperties = null;
		virtureRoot = null;
		mainPhylogeneticTreePanel.setIsShowAnnotation(true);
		mainPhylogeneticTreePanel.updatePhylogeneticTreePanel();
		dispose();
	}

	public GraphicsNode getCurreNode() {
		return curreNode;
	}

	public AnnotationsProperties getCurrentAnnotationsProperties() {
		return currentAnnotationsProperties;
	}
	
	public void updatePhylogeneticTreePanel() {
		phylogeneticTreePanel.updatePhylogeneticTreePanel();
	}

}
