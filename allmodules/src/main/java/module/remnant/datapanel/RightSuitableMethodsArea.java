package module.remnant.datapanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.SwingDialog;
import module.remnant.datapanel.data.DataType;
import egps2.UnifiedAccessPoint;
import egps2.modulei.IModuleLoader;

public class RightSuitableMethodsArea extends JPanel {

	private static final long serialVersionUID = 7210495571011693758L;

	private Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private List<RightMethodButton> methodButtons = new ArrayList<RightMethodButton>();
	private final JPanel shell;

	private MyDataEventManager myDataEventManager;

	public RightSuitableMethodsArea() {
		setPreferredSize(new Dimension(210, 300));
		setLayout(new BorderLayout());
		setBackground(Color.white);

		shell = new JPanel();
		shell.setBackground(Color.white);
		shell.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		shell.setAlignmentY(JLabel.CENTER_ALIGNMENT);

		JLabel jLabel = new JLabel("Suitable methods");
		jLabel.setHorizontalAlignment(SwingConstants.CENTER);
		// jLabel.setFont(TOOLTIP_FONT.deriveFont(Font.BOLD));
		jLabel.setFont(defaultFont);
		jLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		jLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		jLabel.setPreferredSize(new Dimension(200, 100));
		add(jLabel, BorderLayout.NORTH);
		JScrollPane jScrollPane = new JScrollPane(shell);
		jScrollPane.setBorder(null);
		add(jScrollPane, BorderLayout.CENTER);
		// add(shell, BorderLayout.CENTER);
		// shell.setLayout(new BoxLayout(shell, BoxLayout.Y_AXIS));
		shell.setLayout(new BoxLayout(shell, BoxLayout.PAGE_AXIS));
		// shell.setLayout(new JideBoxLayout(shell,JideBoxLayout.Y_AXIS, 10));
	}

	/**
	 * Add a button to the right pane in Default panel. Its title is @param tet, and
	 * its states is
	 * 
	 * @param isDisable set the state of button.
	 * @param text      tooltip content of the button.
	 * 
	 * @author ydl, yfq , mhl
	 * @date 2018-7-30
	 * @modify 2024-04-02
	 */
	private void addButton(String text, boolean isDisable, IModuleLoader moduleCommon) {
		GraphicMehtodButton jButton = new GraphicMehtodButton(text, () -> {
			revalidate();
		});
		jButton.setToolTipText(text);
		jButton.setFont(defaultFont);
		jButton.setEnabled(!isDisable);
		jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		jButton.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		jButton.setOpaque(true);
		jButton.setBackground(new Color(233, 233, 233));
		jButton.setBorder(new EmptyBorder(20, 10, 20, 10));

		Dimension dimension = new Dimension(180, 70);

		JPanel jPanel = new JPanel(new GridLayout());
		jPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		jPanel.add(jButton);
		jPanel.setPreferredSize(dimension);
		jPanel.setMaximumSize(dimension);

		shell.add(jPanel);
		shell.add(Box.createVerticalStrut(10));

//		if (moduleCommon instanceof AbstractVCFMethod) {
//			AbstractVCFMethod abstractVCFMethod = (AbstractVCFMethod) moduleCommon;
//			RightMethodButton methodButton = new RightMethodButton(jButton, abstractVCFMethod, myDataEventManager.getDefaultPanel());
//			methodButtons.add(methodButton);
//		}else if (moduleCommon instanceof MethodsForMAF2TreeViewer) {
//			MethodsForMAF2TreeViewer abstractMAFMethod = (MethodsForMAF2TreeViewer) moduleCommon;
//			RightMethodButton methodButton = new RightMethodButton(jButton, abstractMAFMethod, myDataEventManager.getDefaultPanel());
//			methodButtons.add(methodButton);
//		}else if(moduleCommon instanceof MethodsForMAF2Distance) {
//			MethodsForMAF2Distance abstractMAFMethod = (MethodsForMAF2Distance) moduleCommon;
//			RightMethodButton methodButton = new RightMethodButton(jButton, abstractMAFMethod, myDataEventManager.getDefaultPanel());
//			methodButtons.add(methodButton);
//		}else {
//			RightMethodButton methodButton = new RightMethodButton(jButton, moduleCommon);
//			methodButtons.add(methodButton);
//		}

	}

	public void clearButtons() {
		methodButtons.clear();
		// Can not use this way, because the first element is needed!
		shell.removeAll();
		repaint();
	}

	/**
	 * Create a button for each remnant based on the current file type
	 * 
	 * @Title: suitableMethods
	 * 
	 * @Date Created on: 2018-12-28 14:49
	 * 
	 */
	public void suitableMethods(int currentDataFormat) {

		int currentDataType = myDataEventManager.getDataCenter().getCurrentDataType();

		// YDL:For general text file format, we can add a editor button!
		createSuitableButtonsForGeneralText(currentDataFormat);

		if (currentDataType == DataType.VCF) {
//			createVCFMethodButtons(currentDataFormat);
//		} else if (currentDataType == DataType.TREE) {
//			createTREEMethodButtons(currentDataFormat);
//		} else if (currentDataType == DataType.CIRCUSRNA) {
//			createLISTMethodButtons(currentDataFormat);
//		} else if (currentDataType == DataType.MULTIPLE_SEQS) {
//			createMSMethodButtons(currentDataFormat);
//		} else if (currentDataType == DataType.SIMULATOR) {
//			createSIMUMethodButtons(currentDataFormat);
		} else if (currentDataType == DataType.MAF) {
			createMAFMethodButtons(currentDataFormat);
//		} else if (currentDataType == DataType.GENETIC_DIST) {
//			createDISTMethodButtons(currentDataFormat);
//		} else if (currentDataType == DataType.NEXUS) {
//			createSuitableButtonsForNEXUS(currentDataFormat);
//		} else if (currentDataType == DataType.EHEATMAP) {
//			createSuitableButtonsForEHEATMAP(currentDataFormat);
//		} else if (currentDataType == DataType.MATRIX_TABLE) {
//			createSuitableButtonsForGeneralMatrixTable(currentDataFormat);
//		} else if (currentDataType == DataType.GENERAL_TEXT) {
			// DON'T NEED
		} else {
			System.err.println("The warning dialog in the class : " + getClass());
			SwingDialog.showErrorMSGDialog("Suitable method error",
					"Current data type is "
							+ myDataEventManager.getDataCenter().getDataTypeNameFromDataType(currentDataType)
							+ ".\n While eGPS don't have suitable methode yet!");
		}

	}

//	private void createSuitableButtonsForGeneralMatrixTable(int currentDataFormat) {
//		MethodsForMatrix2Heatmap methodsForMatrix2Heatmap = new MethodsForMatrix2Heatmap();
//
//		if (methodsForMatrix2Heatmap.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.eHEATMAP2EHEATMAPString, false, methodsForMatrix2Heatmap);
//		}
//		
//	}
//
//	private void createSuitableButtonsForEHEATMAP(int currentDataFormat) {
//		MethodsForMatrix2Heatmap methodsForMatrix2Heatmap = new MethodsForMatrix2Heatmap();
//
//		if (methodsForMatrix2Heatmap.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.eHEATMAP2EHEATMAPString, false, methodsForMatrix2Heatmap);
//		}
//	}
//
	/**
	 * 
	 * Add button for text editor.
	 * 
	 * @title createSuitableButtonsForGeneralText
	 * @createdDate 2020-10-15 20:35
	 * @lastModifiedDate 2020-10-15 20:35
	 * @author "yudalang"
	 * @since 1.7
	 * 
	 * @param currentDataFormat
	 * @return void
	 */
	private void createSuitableButtonsForGeneralText(int currentDataFormat) {
//		MethodsForText2Editor methodsForText2Editor = new MethodsForText2Editor();
//		methodsForText2Editor.setDefaultPanel(myDataEventManager.getDefaultPanel());
//		if (methodsForText2Editor.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.TEXT2EIDITOR, false, methodsForText2Editor);
//		}
	}
//
//	private void createSuitableButtonsForNEXUS(int currentDataFormat) {
//		MethodsForTREE2TreeViewer phylogeneticTree = new MethodsForTREE2TreeViewer();
//		if (phylogeneticTree.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.TREEFORMAT2TREENAME, false, phylogeneticTree);
//		}
//
//		MethodsForMSA2TreeViewer fasta2Tree = new MethodsForMSA2TreeViewer();
//		MethodsForMSA2AlignmentView alignmentView = new MethodsForMSA2AlignmentView();
//		MethodsForMSA2DistanceViewer methodsForFasta2DistanceView = new MethodsForMSA2DistanceViewer();
//		MethodsForMS2MultipleSequenceAligner alignment = new MethodsForMS2MultipleSequenceAligner();
//
//		if (fasta2Tree.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.FASTA2TREE, false, fasta2Tree);
//		}
//		if (alignmentView.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.FASTA2ALIGNMENTVIEWNAME, false, alignmentView);
//		}
//		if (methodsForFasta2DistanceView.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.FASTA2DISTVIEWNAME, false, methodsForFasta2DistanceView);
//		}
//		if (alignment.suitable(currentDataFormat) == 1) {
//
//			addButton(ITurnToModuleNameCenter.FASTA2ALIGNMENTNAME, false, alignment);
//		}
//
//	}

	/**
	 * 
	 * @param tt 0 rna; 1 pro; 2 matrix
	 */
//	public void createRnaExpMethodButtons(int tt) {
//		clearButtons();
//		
//		createSuitableButtonsForGeneralText(DataFormat.MATRIX_TABLE);
//		createSuitableButtonsForGeneralMatrixTable(DataFormat.MATRIX_TABLE);
//		
//		switch (tt) {
//		case 0:
//			MethodsForRnaExp2DEGRnaExp methodsForRnaExp = new MethodsForRnaExp2DEGRnaExp();
//			addButton(ITurnToModuleNameCenter.RNAEXP2DEGRNASEQNAME, false, methodsForRnaExp);
//			break;
//		case 1:
//			MethodsForPRO2DEGMassSpectrum htmlUtil = new MethodsForPRO2DEGMassSpectrum();
//			addButton(ITurnToModuleNameCenter.PRO2DEGMASSSPECTRUMNAME, false, htmlUtil);
//			break;
//
//		default:
//			break;
//		}
//		
//		revalidate();
//		
//	}

	/**
	 * Create a button that conforms to the phlogentic tree remnant
	 * 
	 * @author yudalang
	 * @date 2019-2-25
	 */
//	private void createDISTMethodButtons(int currentDataFormat) {
//		MethodsForDIST2TreeViewer dis = new MethodsForDIST2TreeViewer();
//		MethodsForDIST2DistanceViewer geneToDeneticDist = new MethodsForDIST2DistanceViewer();
//		if (dis.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.DIST2TREE, false, dis);
//		}
//		if (geneToDeneticDist.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.DIST2DISTVIEWNAME, false, geneToDeneticDist);
//		}
//
//	}

	/**
	 * 
	 * Create a button that suitable for the MAF related remnant!
	 * 
	 * @title createMAFMethodButtons
	 * @createdDate 2020-10-15 20:47
	 * @lastModifiedDate 2020-10-15 20:47
	 * @author "yudalang"
	 * @since 1.7
	 * 
	 * @param currentDataFormat
	 * @return void
	 */
	private void createMAFMethodButtons(int currentDataFormat) {
//		MethodsForMAF2TreeViewer maf = new MethodsForMAF2TreeViewer();
//		MethodsForMAF2Distance maf2Dist = new MethodsForMAF2Distance();
//		if (maf.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.MAF2TREE, false, maf);
//			addButton(ITurnToModuleNameCenter.mAF2DISt, false, maf2Dist);
//		}
	}

	/**
	 * Create a button that conforms to the Simulator remnant
	 * 
	 * @Title: createSimulatorModuleButton
	 * @Description: Create a button that conforms to the Simulator remnant
	 * @author mhl
	 * @Date Created on: 2018-12-29 14:52
	 * 
	 */
//	private void createSIMUMethodButtons(int currentDataFormat) {
//		MethodsForSIMU2Simulator simulator = new MethodsForSIMU2Simulator();
//		if (simulator.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.SIMU2SIMULATORNAME, false, simulator);
//		}
//	}

	/**
	 * 
	 * Create a button that conforms to the Fasta2Tree remnant
	 * 
	 * @Title: createFasta2TreeModuleButton
	 * @Description: Create a button that conforms to the Fasta2Tree remnant
	 * @author mhl
	 * @Date Created on: 2018-12-29 14:52
	 * 
	 */

//	private void createMSMethodButtons(int currentDataFormat) {
//		MethodsForMSA2TreeViewer fasta2Tree = new MethodsForMSA2TreeViewer();
//		MethodsForMSA2AlignmentView alignmentView = new MethodsForMSA2AlignmentView();
//		MethodsForMSA2DistanceViewer methodsForFasta2DistanceView = new MethodsForMSA2DistanceViewer();
//		MethodsForMS2MultipleSequenceAligner alignment = new MethodsForMS2MultipleSequenceAligner();
//
//		if (fasta2Tree.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.FASTA2TREE, false, fasta2Tree);
//		}
//		if (alignmentView.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.FASTA2ALIGNMENTVIEWNAME, false, alignmentView);
//		}
//		if (methodsForFasta2DistanceView.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.FASTA2DISTVIEWNAME, false, methodsForFasta2DistanceView);
//		}
//		if (alignment.suitable(currentDataFormat) == 1) {
//
//			addButton(ITurnToModuleNameCenter.FASTA2ALIGNMENTNAME, false, alignment);
//		}
//
//	}

	/**
	 * Create a button that conforms to the CirRNA remnant
	 * 
	 * @Title: createCirRNAModuleButton
	 * @Description: Create a button that conforms to the CirRNA remnant
	 * @author mhl
	 * @Date Created on: 2018-12-29 14:52
	 * 
	 */
//	private void createLISTMethodButtons(int currentDataFormat) {
//		MethodsForLIST2CirRNAViewer cirRNA = new MethodsForLIST2CirRNAViewer();
//		if (cirRNA.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.LIST2CIRCRNANAME, false, cirRNA);
//		}
//	}

	/**
	 * Create a button that conforms to the Tree remnant
	 * 
	 * @Title: createTreeModuleButton
	 * @Description: Create a button that conforms to the Tree remnant
	 * @author mhl
	 * @Date Created on: 2018-12-29 14:52
	 * 
	 */

	/**
	 * Create a button that conforms to the vcf remnant
	 * 
	 * @Title: createVCFModuleButton
	 * @Description: Create a button that conforms to the vcf remnant
	 * @author mhl
	 * @Date Created on: 2018-12-29 14:51
	 * 
	 */
//	private void createVCFMethodButtons(int currentDataFormat) {
//		MethodsForVCFSnapShot snapShot = new MethodsForVCFSnapShot();
//		MethodsForVCF2VCFTools vcfTools = new MethodsForVCF2VCFTools();
//		MethodsForVCF2SNPFasta vcf2snpFasta = new MethodsForVCF2SNPFasta();
//		MethodsForNeutralityTest calculatePositiveSelection = new MethodsForNeutralityTest();
//		MethodsForGeneticDiversity geneticDiversity = new MethodsForGeneticDiversity();
//		MethodsForHKA hkAscore = new MethodsForHKA();
//		MethodsForPBS pbsStatistic = new MethodsForPBS();
//		MethodsForFST fixationIndex = new MethodsForFST();
//		MethodsForVCF2TreeViewer buildTree = new MethodsForVCF2TreeViewer();
//
//		snapShot.setDataPanel(myDataEventManager.getDefaultPanel());
//		buildTree.setDataPanel(myDataEventManager.getDefaultPanel());
//		vcf2snpFasta.setDataPanel(myDataEventManager.getDefaultPanel());
//
//		if (snapShot.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2VCFSNAPSHOTNAME, false, snapShot);
//		} else if (snapShot.suitable(currentDataFormat) == 0) {
//			addButton(ITurnToModuleNameCenter.VCF2VCFSNAPSHOTNAME, true, snapShot);
//		}
//
//		if (vcfTools.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2VCFTOOLSNAME, false, vcfTools);
//		}
//		if (vcf2snpFasta.suitable(currentDataFormat) == 1) {
//			addButton("To SNP fasta", false, vcf2snpFasta);
//		}
//		if (calculatePositiveSelection.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2POSITIVESELECTIONNAME, false, calculatePositiveSelection);
//		}
//		if (geneticDiversity.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2GENETICDIVERSITYNAME, false, geneticDiversity);
//		}
//		if (hkAscore.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2HKANAME, false, hkAscore);
//		}
//		if (pbsStatistic.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2PBSVIEWNAME, false, pbsStatistic);
//		}
//		if (fixationIndex.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2FSTSTATISTICNAME, false, fixationIndex);
//		}
//		if (buildTree.suitable(currentDataFormat) == 1) {
//			addButton(ITurnToModuleNameCenter.VCF2TREENAME, false, buildTree);
//		}
//
//	}

	public RightMethodButton getMethoButton(Class<?> clz) throws ClassNotFoundException {
		for (RightMethodButton mm : methodButtons) {
			if (clz.isAssignableFrom(mm.getITurnToModuleCommon().getClass())) {
				return mm;
			}
		}
		System.err.println("This is: " + getClass());
		System.err.println("There is no sutiable method element found here!");
		throw new ClassNotFoundException();
	}

	public void setDataEventManager(MyDataEventManager myDataEventManager) {
		this.myDataEventManager = myDataEventManager;

	}

}

class GraphicMehtodButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 6727653597292552102L;
	final Runnable callBack;

	public GraphicMehtodButton(String text, Runnable callBack) {
		super(text);
		this.callBack = callBack;
		addMouseListener(this);

	}

	@Override
	public void mouseEntered(MouseEvent evt) {
		setBackground(Color.cyan);
		callBack.run();
	}

	@Override
	public void mouseExited(MouseEvent evt) {
		setBackground(new Color(233, 233, 233));
		callBack.run();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
