package module.evolview.gfamily.work.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.evolview.genebrowser.GeneBrowserMainFace;
import module.evolview.gfamily.GeneFamilyMainFace;
import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.browser.BrowserPanel;
import module.evolview.gfamily.work.gui.browser.GeneDrawingLengthCursor;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;

@SuppressWarnings("serial")
public class CtrlGenomeBrowserPanel extends BaseCtrlPanel {
	private final JSpinner spinnerEndPosition;
	private final JSpinner spinnerStartPosition;
	private JRadioButton rdbtnProtein;
	private JRadioButton rdbtnNeclotide;
	private final JButton moveOneLeft;
	private final JButton moveTwoLeft;
	private final JButton moveThreeLeft;
	private final JButton moveOneRight;
	private final JButton moveTwoRight;
	private final JButton moveThreeRight;
	private final int moveUnitLength = 1000;

	private JSpinner spinnerWindowStep;
	private JSpinner spinnerWindowSize;

	/**
	 * Create the panel.
	 */
	public CtrlGenomeBrowserPanel() {
		Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblExpensionArea = new JLabel("Genomic region");
		lblExpensionArea.setFont(titleFont);
		GridBagConstraints gbc_lblExpensionArea = new GridBagConstraints();
		gbc_lblExpensionArea.insets = new Insets(0, 0, 5, 5);
		gbc_lblExpensionArea.anchor = GridBagConstraints.WEST;
		gbc_lblExpensionArea.gridwidth = 4;
		gbc_lblExpensionArea.gridx = 0;
		gbc_lblExpensionArea.gridy = 0;
		add(lblExpensionArea, gbc_lblExpensionArea);

		JLabel lblStartLocation = new JLabel(" Start position");
		lblStartLocation.setFont(globalFont);
		GridBagConstraints gbc_lblStartLocation = new GridBagConstraints();
		gbc_lblStartLocation.gridwidth = 3;
		gbc_lblStartLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartLocation.gridx = 0;
		gbc_lblStartLocation.gridy = 1;
		add(lblStartLocation, gbc_lblStartLocation);

		spinnerStartPosition = new JSpinner();
		spinnerStartPosition.setFont(globalFont);

		int drawStart = 1;
		int drawEnd = 100000;
		spinnerStartPosition.setModel(new SpinnerNumberModel(drawStart, 1, drawEnd, 1));
		GridBagConstraints gbc_spinnerStartPosition = new GridBagConstraints();
		gbc_spinnerStartPosition.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStartPosition.gridwidth = 2;
		gbc_spinnerStartPosition.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerStartPosition.gridx = 4;
		gbc_spinnerStartPosition.gridy = 1;
		add(spinnerStartPosition, gbc_spinnerStartPosition);

		JLabel lblEndLocation = new JLabel("End position");
		lblEndLocation.setFont(globalFont);
		GridBagConstraints gbc_lblEndLocation = new GridBagConstraints();
		gbc_lblEndLocation.gridwidth = 3;
		gbc_lblEndLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndLocation.gridx = 0;
		gbc_lblEndLocation.gridy = 2;
		add(lblEndLocation, gbc_lblEndLocation);

		spinnerEndPosition = new JSpinner();
		spinnerEndPosition.setFont(globalFont);
		spinnerEndPosition.setModel(new SpinnerNumberModel(drawEnd - 1, 1, drawEnd - 1, 1));
		GridBagConstraints gbc_spinnerEnd = new GridBagConstraints();
		gbc_spinnerEnd.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerEnd.gridwidth = 2;
		gbc_spinnerEnd.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerEnd.gridx = 4;
		gbc_spinnerEnd.gridy = 2;
		add(spinnerEndPosition, gbc_spinnerEnd);

		JLabel lblMove = new JLabel("Move");
		lblMove.setFont(globalFont);
		GridBagConstraints gbc_lblMove = new GridBagConstraints();
		gbc_lblMove.insets = new Insets(0, 0, 5, 5);
		gbc_lblMove.gridx = 0;
		gbc_lblMove.gridy = 3;
		add(lblMove, gbc_lblMove);

		JButton btnNewButton = new JButton("Global view");
		btnNewButton.setIcon(IconObtainer.get("global_view.png"));
		btnNewButton.setToolTipText("Turn to global view, also initialize the start and end positions.");
		btnNewButton.setFont(globalFont);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridwidth = 3;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 3;
		add(btnNewButton, gbc_btnNewButton);
		btnNewButton.addActionListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			GeneStructureInfo geneStructureInfo = selectedBrowserPanel.getGeneStructureInfo();

			drawProperties.setDrawStart(1);
			drawProperties.setDrawEnd(geneStructureInfo.geneLength);
			controller.refreshViewGenomeBrowser();
			reSetValue();

			GeneFamilyMainFace main = controller.getMain();
			if (main instanceof GeneBrowserMainFace) {
				GeneBrowserMainFace face = (GeneBrowserMainFace) main;
				face.invokeTheFeatureMethod(1);
			}
		});
		this.rdbtnProtein = new JRadioButton();
//		this.rdbtnNeclotide = rdbtnNeclotide;

		moveTwoLeft = new JButton();
		moveTwoLeft.setToolTipText("Move 50% to the left!");
		moveTwoLeft.setIcon(IconObtainer.get("left2.png"));
		moveTwoLeft.setFocusPainted(false);
		// moveTwoLeft.setFont(globalFont);
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_1.insets = new Insets(0, 0, 5, 5);
		gbc_button_1.gridx = 1;
		gbc_button_1.gridy = 4;
		add(moveTwoLeft, gbc_button_1);
		moveTwoLeft.addActionListener(e -> {

			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			int start = drawProperties.getDrawStart();
			int end = drawProperties.getDrawEnd();
			int tempMoveLength = (int) ((end - start + 1) * 0.5);
			int moveLength = start > tempMoveLength ? tempMoveLength : start - 1;
			drawProperties.setDrawStart(start - moveLength);
			drawProperties.setDrawEnd(end - moveLength);
			controller.refreshViewGenomeBrowser();
			reSetValue();
		});
		moveOneLeft = new JButton();
		moveOneLeft.setToolTipText("Move 20% to the left!");
		moveOneLeft.setIcon(IconObtainer.get("left.png"));
		moveOneLeft.setFocusPainted(false);
		// moveOneLeft.setFont(globalFont);
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_2.insets = new Insets(0, 0, 5, 5);
		gbc_button_2.gridx = 2;
		gbc_button_2.gridy = 4;
		add(moveOneLeft, gbc_button_2);
		moveOneLeft.addActionListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			int start = drawProperties.getDrawStart();
			int end = drawProperties.getDrawEnd();
			int tempMoveLength = (int) ((end - start + 1) * 0.2);
			int moveLength = start > tempMoveLength ? tempMoveLength : start - 1;
			drawProperties.setDrawStart(start - moveLength);
			drawProperties.setDrawEnd(end - moveLength);
			controller.refreshViewGenomeBrowser();
			reSetValue();
		});
		moveOneRight = new JButton();
		moveOneRight.setToolTipText("Move 20% to the right!");
		moveOneRight.setIcon(IconObtainer.get("right.png"));
		moveOneRight.setFocusPainted(false);
		// moveOneRight.setFont(globalFont);
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.insets = new Insets(0, 0, 5, 5);
		gbc_button_3.gridx = 3;
		gbc_button_3.gridy = 4;
		add(moveOneRight, gbc_button_3);
		moveOneRight.addActionListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			int start = drawProperties.getDrawStart();
			int end = drawProperties.getDrawEnd();
			int maxValue = drawProperties.getMaxValue();
			int moveMaxInterval = maxValue - end;
			int tempMoveLength = (int) ((end - start + 1) * 0.2);
			int moveLength = moveMaxInterval > tempMoveLength ? tempMoveLength : moveMaxInterval;
			drawProperties.setDrawStart(start + moveLength);
			drawProperties.setDrawEnd(end + moveLength);
			controller.refreshViewGenomeBrowser();
			reSetValue();
		});
		moveTwoRight = new JButton();
		moveTwoRight.setFocusPainted(false);
		moveTwoRight.setToolTipText("Move 50% to the right!");
		moveTwoRight.setIcon(IconObtainer.get("right2.png"));
		// moveTwoRight.setFont(globalFont);
		GridBagConstraints gbc_button_4 = new GridBagConstraints();
		gbc_button_4.insets = new Insets(0, 0, 5, 5);
		gbc_button_4.gridx = 4;
		gbc_button_4.gridy = 4;
		add(moveTwoRight, gbc_button_4);
		moveTwoRight.addActionListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			int start = drawProperties.getDrawStart();
			int end = drawProperties.getDrawEnd();
			int maxValue = drawProperties.getMaxValue();
			int moveMaxInterval = maxValue - end;
			int tempMoveLength = (int) ((end - start + 1) * 0.5);
			int moveLength = moveMaxInterval > tempMoveLength ? tempMoveLength : moveMaxInterval;
			drawProperties.setDrawStart(start + moveLength);
			drawProperties.setDrawEnd(end + moveLength);
			controller.refreshViewGenomeBrowser();
			reSetValue();
		});

		moveThreeLeft = new JButton();
		moveThreeLeft.setToolTipText("Move 95% to the left!");
		moveThreeLeft.setIcon(IconObtainer.get("left3.png"));
		moveThreeLeft.setFocusPainted(false);
		// moveThreeLeft.setFont(globalFont);
		GridBagConstraints gbc_btnNewButton11 = new GridBagConstraints();
		gbc_btnNewButton11.anchor = GridBagConstraints.EAST;
		// gbc_button.gridwidth = 2;
		gbc_btnNewButton11.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton11.gridx = 0;
		gbc_btnNewButton11.gridy = 4;
		add(moveThreeLeft, gbc_btnNewButton11);
		moveThreeLeft.addActionListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			int start = drawProperties.getDrawStart();
			int end = drawProperties.getDrawEnd();
			int tempMoveLength = (int) ((end - start + 1) * 0.95);
			int moveLength = start > tempMoveLength ? tempMoveLength : start - 1;
			drawProperties.setDrawStart(start - moveLength);
			drawProperties.setDrawEnd(end - moveLength);
			controller.refreshViewGenomeBrowser();
			reSetValue();
		});
		moveThreeRight = new JButton();
		moveThreeRight.setToolTipText("Move 95% to the right!");
		moveThreeRight.setIcon(IconObtainer.get("right3.png"));
		// moveThreeRight.setFont(globalFont);
		moveThreeRight.setFocusPainted(false);
		GridBagConstraints gbc_button_5 = new GridBagConstraints();
		gbc_button_5.anchor = GridBagConstraints.WEST;
		// gbc_button_5.gridwidth = 2;
		gbc_button_5.insets = new Insets(0, 0, 5, 0);
		gbc_button_5.gridx = 5;
		gbc_button_5.gridy = 4;
		add(moveThreeRight, gbc_button_5);
		moveThreeRight.addActionListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();

			int start = drawProperties.getDrawStart();
			int end = drawProperties.getDrawEnd();
			int maxValue = drawProperties.getMaxValue();
			int moveMaxInterval = maxValue - end;
			int tempMoveLength = (int) ((end - start + 1) * 0.95);
			int moveLength = moveMaxInterval > tempMoveLength ? tempMoveLength : moveMaxInterval;
			drawProperties.setDrawStart(start + moveLength);
			drawProperties.setDrawEnd(end + moveLength);
			controller.refreshViewGenomeBrowser();
			reSetValue();
		});

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 6;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 5;
		add(separator, gbc_separator);

		ButtonGroup buttonGroup = new ButtonGroup();

		JLabel lblNewLabel_10 = new JLabel("Similarity plot");
		lblNewLabel_10.setFont(titleFont);
		GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
		gbc_lblNewLabel_10.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_10.gridwidth = 3;
		gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_10.gridx = 0;
		gbc_lblNewLabel_10.gridy = 7;
		add(lblNewLabel_10, gbc_lblNewLabel_10);
//        SimPlotData simPlotData = controller.getDataModel().getSimPlotData();
//        List<ComparedSequenceResult> comparedSequenceResults = simPlotData.getComparedSequenceResults();

		JLabel lblWindow = new JLabel("");
		lblWindow.setFont(globalFont);
		GridBagConstraints gbc_lblWindow = new GridBagConstraints();
		gbc_lblWindow.gridwidth = 3;
		gbc_lblWindow.anchor = GridBagConstraints.WEST;
		gbc_lblWindow.insets = new Insets(0, 0, 5, 5);
		gbc_lblWindow.gridx = 0;
		gbc_lblWindow.gridy = 8;
		add(lblWindow, gbc_lblWindow);

		JLabel lblNewLabel_3 = new JLabel(" Window Step");
		lblNewLabel_3.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.gridwidth = 3;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 9;
		add(lblNewLabel_3, gbc_lblNewLabel_3);

		spinnerWindowStep = new JSpinner();
		spinnerWindowStep.setModel(new SpinnerNumberModel(50, 1, 1000, 1));
		spinnerWindowStep.setFont(globalFont);
		GridBagConstraints gbc_spinnerWindowSize = new GridBagConstraints();
		gbc_spinnerWindowSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerWindowSize.gridwidth = 2;
		gbc_spinnerWindowSize.anchor = GridBagConstraints.WEST;
		gbc_spinnerWindowSize.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerWindowSize.gridx = 4;
		gbc_spinnerWindowSize.gridy = 9;
		add(spinnerWindowStep, gbc_spinnerWindowSize);

		spinnerWindowStep.addChangeListener(e -> {

			reCalculateSimplotDataAndRefresh();
		});

		JLabel lblStep = new JLabel("Window Size");
		lblStep.setFont(globalFont);
		GridBagConstraints gbc_lblStep = new GridBagConstraints();
		gbc_lblStep.gridwidth = 3;
		gbc_lblStep.insets = new Insets(0, 0, 5, 5);
		gbc_lblStep.gridx = 0;
		gbc_lblStep.gridy = 10;
		add(lblStep, gbc_lblStep);

		spinnerWindowSize = new JSpinner();
		spinnerWindowSize.setModel(new SpinnerNumberModel(200, 1, 10000, 1));
		spinnerWindowSize.setFont(globalFont);
		GridBagConstraints gbc_spinnerWindowStep = new GridBagConstraints();
		gbc_spinnerWindowStep.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerWindowStep.gridwidth = 2;
		gbc_spinnerWindowStep.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerWindowStep.gridx = 4;
		gbc_spinnerWindowStep.gridy = 10;
		add(spinnerWindowSize, gbc_spinnerWindowStep);

		spinnerWindowSize.addChangeListener(e -> {

			reCalculateSimplotDataAndRefresh();
		});

//        JSeparator separator_4 = new JSeparator();
//        GridBagConstraints gbc_separator_4 = new GridBagConstraints();
//        gbc_separator_4.fill = GridBagConstraints.HORIZONTAL;
//        gbc_separator_4.gridwidth = 6;
//        gbc_separator_4.insets = new Insets(0, 0, 5, 0);
//        gbc_separator_4.gridx = 0;
//        gbc_separator_4.gridy = 11;
//        add(separator_4, gbc_separator_4);

//        JLabel lblNewLabel_1 = new JLabel("Alignment view");
//        lblNewLabel_1.setFont(titleFont);
//        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
//        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
//        gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
//        gbc_lblNewLabel_1.gridwidth = 3;
//        gbc_lblNewLabel_1.gridx = 0;
//        gbc_lblNewLabel_1.gridy = 12;
//        add(lblNewLabel_1, gbc_lblNewLabel_1);
//
//        rdbtnNeclotide = new JRadioButton("Necleotide");
//        rdbtnNeclotide.setFont(globalFont);
//        GridBagConstraints gbc_rdbtnNeclotide = new GridBagConstraints();
//        gbc_rdbtnNeclotide.gridwidth = 2;
//        gbc_rdbtnNeclotide.anchor = GridBagConstraints.WEST;
//        gbc_rdbtnNeclotide.insets = new Insets(0, 0, 5, 5);
//        gbc_rdbtnNeclotide.gridx = 0;
//        gbc_rdbtnNeclotide.gridy = 13;
//        add(rdbtnNeclotide, gbc_rdbtnNeclotide);
//        rdbtnNeclotide.setFocusPainted(false);
//        buttonGroup.add(rdbtnNeclotide);
//        rdbtnNeclotide.setSelected(true);
//        rdbtnNeclotide.addItemListener(e -> {
//        	BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
//        	GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
//            drawProperties.setReCalculator(true);
//            controller.refreshViewGenomeBrowser();
//        });
//
//        rdbtnProtein = new JRadioButton("Amino acid");
//        rdbtnProtein.setFont(globalFont);
//        GridBagConstraints gbc_rdbtnProtein = new GridBagConstraints();
//        gbc_rdbtnProtein.insets = new Insets(0, 0, 0, 5);
//        gbc_rdbtnProtein.gridwidth = 2;
//        gbc_rdbtnProtein.anchor = GridBagConstraints.WEST;
//        gbc_rdbtnProtein.gridx = 0;
//        gbc_rdbtnProtein.gridy = 14;
//        add(rdbtnProtein, gbc_rdbtnProtein);
//        rdbtnProtein.setFocusPainted(false);
//        buttonGroup.add(rdbtnProtein);
//        rdbtnProtein.addItemListener(e -> {
//        	BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
//        	GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
//            drawProperties.setReCalculator(true);
//            controller.refreshViewGenomeBrowser();
//        });

		spinnerStartPosition.addChangeListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			Integer startValue = (Integer) spinnerStartPosition.getValue();
			Integer endValue = (Integer) spinnerEndPosition.getValue();

			if (startValue > endValue) {
				SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(), "Input error",
						"The start location should less than end location !");
				spinnerStartPosition.setValue(drawProperties.getDrawStart());
				return;
			}
			drawProperties.setDrawStart(startValue);
			controller.refreshViewGenomeBrowser();
		});

		spinnerEndPosition.addChangeListener(e -> {
			BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
			GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();
			Integer startValue = (Integer) spinnerStartPosition.getValue();
			Integer endValue = (Integer) spinnerEndPosition.getValue() + 1;
			if (startValue >= endValue) {
				SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(), "Input error",
						"The start location should less than end location !");
				spinnerEndPosition.setValue(drawProperties.getDrawEnd() - 1);
				return;
			}

			drawProperties.setDrawEnd(endValue);
			controller.refreshViewGenomeBrowser();
		});
	}

	private void reCalculateSimplotDataAndRefresh() {
		Integer size = (Integer) spinnerWindowSize.getValue();
		Integer step = (Integer) spinnerWindowStep.getValue();

		BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
		GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();

//        AlignmentWithDerivedStatistics dataModel = selectedBrowserPanel.getGeneCalculatedDerivedStatistics();
//        AlignmentGetSequence sequence = dataModel.getSequence();
//        dataModel.setSimPlotData(GeneStaticsCalculator.getSimplotData(sequence, step, size));

		controller.refreshViewGenomeBrowser();
		drawProperties.setReCalculator(true);

	}

	public void reSetValue() {
		BrowserPanel selectedBrowserPanel = controller.getMain().getSelectedBrowserPanel();
		GeneDrawingLengthCursor drawProperties = selectedBrowserPanel.getGeneDrawingLengthCursor();

		int drawEnd = drawProperties.getDrawEnd() - 1;
		int drawStart = drawProperties.getDrawStart();
		Integer endValue = (Integer) spinnerEndPosition.getValue();
		if (drawStart > endValue) {
			spinnerEndPosition.setValue(drawEnd);
			spinnerStartPosition.setValue(drawStart);
		} else {
			spinnerStartPosition.setValue(drawStart);
			spinnerEndPosition.setValue(drawEnd);
		}

	}

	public JRadioButton getRdbtnNeclotide() {
		return rdbtnNeclotide;
	}

	public JRadioButton getRdbtnProtein() {
		return rdbtnProtein;
	}

}
