package module.evolview.gfamily.work.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;
import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.colorscheme.DialogFrame;
import module.evolview.gfamily.work.model.tree.GraphicsNode;

/**
 * @ClassName LeftDataFilterPanel
 * @Date Created on:2020-04-11 14:25
 */
@SuppressWarnings("serial")
public class CtrlDataOperationPanel extends BaseCtrlPanel {
	protected Font defaultFont;
	protected JTextField textFieldSearchNodes;

	protected DialogFrame countryDialog;

	protected JCheckBox chckbxMale;
	protected JCheckBox chckbxFemale;
	protected JCheckBox chckbxUnknow;
	protected JButton btnSexClear;

	/**
	 * 存放所有过滤相关的状态值
	 */
//	protected FilterState filterState = new FilterState();

	public CtrlDataOperationPanel() {

		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		gridBagLayout.columnWidths = new int[] { 60, 60, 18, 40, 4, 4, 40 };

		setLayout(gridBagLayout);
		Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		JLabel lblSearchNodes = new JLabel("Search virus/mutation/node");
		lblSearchNodes.setFont(titleFont);
		GridBagConstraints gbc_lblSearchNodes = new GridBagConstraints();
		gbc_lblSearchNodes.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSearchNodes.gridwidth = 7;
		gbc_lblSearchNodes.insets = new Insets(0, 0, 5, 0);
		gbc_lblSearchNodes.gridx = 0;
		gbc_lblSearchNodes.gridy = 0;
		add(lblSearchNodes, gbc_lblSearchNodes);

		textFieldSearchNodes = new JTextField();
		textFieldSearchNodes.setForeground(Color.lightGray);
		String promoteText = "AccMut=A23403G+C3037T;EPI_ISL_596489;CGB123";
		textFieldSearchNodes.setText(promoteText);
		GridBagConstraints gbc_textFieldSearchNodes = new GridBagConstraints();
		gbc_textFieldSearchNodes.gridwidth = 7;
		gbc_textFieldSearchNodes.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSearchNodes.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldSearchNodes.gridx = 0;
		gbc_textFieldSearchNodes.gridy = 1;
		add(textFieldSearchNodes, gbc_textFieldSearchNodes);
		textFieldSearchNodes.setColumns(10);

		textFieldSearchNodes.addFocusListener(new MyFocusListener(promoteText, textFieldSearchNodes));

		textFieldSearchNodes.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String text = textFieldSearchNodes.getText();
					if (!text.isEmpty()) {
						// 这个里面自己会用线程池启动。
//						controller.searchForNode(text);
					}
				}
			}
		});

		JButton btnSearch = new JButton("");
		btnSearch.setFocusable(false);
		btnSearch.setToolTipText("<html><body>" + "Search nodes by viral strain, accession number, mutation, CGB ID."
				+ " <br> " + "Use ';' to represent the relation of 'OR'. Use '+' to represent the relation of 'AND'."
				+ "<br>" + "Use \"\"  for exact search, no \"\" for fuzzy search. " + "<br>"
				+ "Use the prefix 'AccMut=' for a search of cumulative mutations." + "</body></html>");
		btnSearch.setFont(defaultFont);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 这个里面自己会用线程池启动。
//				controller.searchForNode(textFieldSearchNodes.getText());
			}
		});

		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 2;
		add(btnSearch, gbc_btnSearch);
		btnSearch.setIcon(IconObtainer.get("search16x16.png"));

		JButton clearMarkerbtn = new JButton("");
		clearMarkerbtn.setFocusable(false);
		clearMarkerbtn.setToolTipText("Clear the markers of search results.");
		clearMarkerbtn.setFont(defaultFont);
		clearMarkerbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				delegateTheAction2threadPool(() -> {
//					controller.clearSearchedNode();
//				});
			}
		});
		clearMarkerbtn.setIcon(IconObtainer.get("no_select.png"));
		GridBagConstraints gbc_clearMarker = new GridBagConstraints();
		gbc_clearMarker.fill = GridBagConstraints.HORIZONTAL;
		gbc_clearMarker.insets = new Insets(0, 0, 5, 5);
		gbc_clearMarker.gridx = 1;
		gbc_clearMarker.gridy = 2;
		add(clearMarkerbtn, gbc_clearMarker);

		JButton btnSearchFilterButton = new JButton("");
		btnSearchFilterButton.setFocusable(false);
		btnSearchFilterButton.setFont(defaultFont);

		btnSearchFilterButton.setIcon(IconObtainer.get("filter.png"));
		btnSearchFilterButton.setToolTipText("Only show the selected lineages.");
		GridBagConstraints gbc_btnSearchFilterButton = new GridBagConstraints();
		gbc_btnSearchFilterButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSearchFilterButton.gridwidth = 2;
		gbc_btnSearchFilterButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearchFilterButton.gridx = 2;
		gbc_btnSearchFilterButton.gridy = 2;
		add(btnSearchFilterButton, gbc_btnSearchFilterButton);
		btnSearchFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				delegateTheAction2threadPool(() -> {
//					SarsCov2ViewerController.isSelectedFilter = true;
//					controller.processAllFilter(filterState);
				});
			}
		});

		JButton btnSearchClear = new JButton("");
		btnSearchClear.setFocusable(false);
		btnSearchClear.setToolTipText("Undo the search filter (to show all nodes).");
		btnSearchClear.setFont(defaultFont);
		btnSearchClear.setIcon(IconObtainer.get("clear.png"));
		GridBagConstraints gbc_btnSearchClear = new GridBagConstraints();
		gbc_btnSearchClear.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSearchClear.gridwidth = 3;
		gbc_btnSearchClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchClear.gridx = 4;
		gbc_btnSearchClear.gridy = 2;
		add(btnSearchClear, gbc_btnSearchClear);
		btnSearchClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delegateTheAction2threadPool(() -> {
//					SarsCov2ViewerController.isSelectedFilter = false;
//					controller.processAllFilter(filterState);
				});
			}
		});

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 7;
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 3;
		add(separator_1, gbc_separator_1);

		JLabel lblCountriesRegions = new JLabel("Countries & regions chooser");
		lblCountriesRegions.setFont(titleFont);
		GridBagConstraints gbc_lblCountriesRegions = new GridBagConstraints();
		gbc_lblCountriesRegions.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCountriesRegions.gridwidth = 7;
		gbc_lblCountriesRegions.insets = new Insets(0, 0, 5, 0);
		gbc_lblCountriesRegions.gridx = 0;
		gbc_lblCountriesRegions.gridy = 4;
		add(lblCountriesRegions, gbc_lblCountriesRegions);

		JButton btnChooseRegion = new JButton("Choose");
		btnChooseRegion.setFocusable(false);
		btnChooseRegion.setIcon(IconObtainer.get("region.png"));
		btnChooseRegion.setToolTipText("Choose the region you want to filter.");
		btnChooseRegion.setFont(defaultFont);

		btnChooseRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delegateTheAction2threadPool(() -> {
//					dialog = DialogFrame.obetainCountryDialogForFilter(controller, filterState);
//
//					if (!filterState.getCountryFilterState()) {
//						FilterCountryPanel2 contentPanel = new FilterCountryPanel2(
//								controller.getGloablCountry2numberMap(), controller, filterState);
//						countryDialog = new DialogFrame(contentPanel);
//					}
//					countryDialog.setTitle("Country & region chooser");
//					countryDialog.setSize(1500, 800);
//					countryDialog.setLocationRelativeTo(SarsCov2MainFrame.getInstance());
//					countryDialog.setVisible(true);

				});
			}
		});

		GridBagConstraints gbc_btnChooseRegion = new GridBagConstraints();
		gbc_btnChooseRegion.gridwidth = 2;
		gbc_btnChooseRegion.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChooseRegion.insets = new Insets(0, 0, 5, 5);
		gbc_btnChooseRegion.gridx = 0;
		gbc_btnChooseRegion.gridy = 5;
		add(btnChooseRegion, gbc_btnChooseRegion);

		JButton btnCountryClear = new JButton("Clear");
		btnCountryClear.setFocusable(false);
		btnCountryClear.setIcon(IconObtainer.get("clear.png"));
		btnCountryClear.setToolTipText("Undo the 'countries and regions' filter.");
		btnCountryClear.setFont(defaultFont);
		GridBagConstraints gbc_btnCountryClear = new GridBagConstraints();
		gbc_btnCountryClear.gridwidth = 5;
		gbc_btnCountryClear.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCountryClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnCountryClear.gridx = 2;
		gbc_btnCountryClear.gridy = 5;
		add(btnCountryClear, gbc_btnCountryClear);
		btnCountryClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				delegateTheAction2threadPool(() -> {
//					filterState.setCountryFilterState(false);
//					controller.processAllFilter(filterState);
//					try {
//						filterState.getFilterCountryRecordor().allCountryName();
//					} catch (IOException eg) {
//						eg.printStackTrace();
//					}
//				});

			}
		});

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 7;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 6;

		add(separator, gbc_separator);

		JLabel lblGender = new JLabel("Gender");
		lblGender.setFont(titleFont);
		GridBagConstraints gbc_lblGender = new GridBagConstraints();
		gbc_lblGender.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblGender.insets = new Insets(0, 0, 5, 5);
		gbc_lblGender.gridx = 0;
		gbc_lblGender.gridy = 7;
		add(lblGender, gbc_lblGender);

		JButton btnSexClear = new JButton("Clear");
		btnSexClear.setFocusable(false);
		btnSexClear.setIcon(IconObtainer.get("clear.png"));
		btnSexClear.setToolTipText("Undo the gender filter.");
		btnSexClear.setEnabled(false);
		btnSexClear.setFont(defaultFont);
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.gridwidth = 4;
		gbc_btnClear.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 3;
		gbc_btnClear.gridy = 7;
		add(btnSexClear, gbc_btnClear);
		btnSexClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				chckbxMale.setSelected(true);
				chckbxFemale.setSelected(true);
				chckbxUnknow.setSelected(true);
//				filterState.setSexState(judgeSex());
//				filterState.setSexFilterState(false);

//				delegateTheAction2threadPool(() -> {
//					SarsCov2MainFrame.getInstance().glassPaneStart();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(false);
//
////					controller.processAllFilter(filterState);
//
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(true);
//					SarsCov2MainFrame.getInstance().glassPaneStop();
//				});
			}

		});

		chckbxMale = new JCheckBox(" Male ");
		chckbxMale.setFocusable(false);
		chckbxMale.setSelected(true);
		chckbxMale.setFont(defaultFont);
		GridBagConstraints gbc_chckbxMale = new GridBagConstraints();
		gbc_chckbxMale.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxMale.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxMale.gridx = 0;
		gbc_chckbxMale.gridy = 8;
		add(chckbxMale, gbc_chckbxMale);

		chckbxMale.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				filterState.setSexState(judgeSex());
				if (judgeSex() == "MFU") {
					btnSexClear.setEnabled(false);
				} else {
					btnSexClear.setEnabled(true);
				}

//				delegateTheAction2threadPool(() -> {
//
//					SarsCov2MainFrame.getInstance().glassPaneStart();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(false);
//
//					filterState.setSexFilterState(true);
//
////					controller.processAllFilter(filterState);
//
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(true);
//
//					SarsCov2MainFrame.getInstance().glassPaneStop();
//
//				});
			}
		});

		chckbxFemale = new JCheckBox("Female");
		chckbxFemale.setFocusable(false);
		chckbxFemale.setSelected(true);
		chckbxFemale.setFont(defaultFont);
		GridBagConstraints gbc_chckbxFemale = new GridBagConstraints();
		gbc_chckbxFemale.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxFemale.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxFemale.gridx = 1;
		gbc_chckbxFemale.gridy = 8;
		add(chckbxFemale, gbc_chckbxFemale);
		chckbxFemale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				delegateTheAction2threadPool(() -> {
//					SarsCov2MainFrame.getInstance().glassPaneStart();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(false);
//					filterState.setSexState(judgeSex());
//					if (judgeSex() == "MFU") {
//						btnSexClear.setEnabled(false);
//					} else {
//						btnSexClear.setEnabled(true);
//					}
//
//					filterState.setSexFilterState(true);
//
////					controller.processAllFilter(filterState);
//
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(true);
//					SarsCov2MainFrame.getInstance().glassPaneStop();
//				});
			}
		});

		chckbxUnknow = new JCheckBox("Unknown");
		chckbxUnknow.setFocusable(false);
		chckbxUnknow.setSelected(true);
		chckbxUnknow.setFont(defaultFont);
		GridBagConstraints gbc_chckbxUnknow = new GridBagConstraints();
		gbc_chckbxUnknow.gridwidth = 5;
		gbc_chckbxUnknow.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxUnknow.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxUnknow.gridx = 2;
		gbc_chckbxUnknow.gridy = 8;
		add(chckbxUnknow, gbc_chckbxUnknow);
		chckbxUnknow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				delegateTheAction2threadPool(() -> {
//					SarsCov2MainFrame.getInstance().glassPaneStart();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(false);
//					filterState.setSexState(judgeSex());
//
//					if (judgeSex() == "MFU") {
//						btnSexClear.setEnabled(false);
//					} else {
//						btnSexClear.setEnabled(true);
//					}
//
//					filterState.setSexFilterState(true);
//
////					controller.processAllFilter(filterState);
//
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(true);
//					SarsCov2MainFrame.getInstance().glassPaneStop();
//				});
			}
		});

		JSeparator separatorAfterSearchFilter = new JSeparator();
		GridBagConstraints gbc_separator_afterSearchFilter = new GridBagConstraints();
		gbc_separator_afterSearchFilter.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_afterSearchFilter.gridwidth = 7;
		gbc_separator_afterSearchFilter.insets = new Insets(0, 0, 5, 0);
		gbc_separator_afterSearchFilter.gridx = 0;
		gbc_separator_afterSearchFilter.gridy = 9;
		add(separatorAfterSearchFilter, gbc_separator_afterSearchFilter);

		JLabel lblAge = new JLabel("Patient age");
		lblAge.setToolTipText("If the age value is -1, it indicates that the information of patient age is missing.");
		lblAge.setFont(titleFont);
		GridBagConstraints gbc_lblAge = new GridBagConstraints();
		gbc_lblAge.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblAge.gridwidth = 2;
		gbc_lblAge.insets = new Insets(0, 0, 5, 5);
		gbc_lblAge.gridx = 0;
		gbc_lblAge.gridy = 10;
		add(lblAge, gbc_lblAge);

		JButton minusAgeButton = new JButton("");
		minusAgeButton.setIcon(IconObtainer.get("minus.png"));
		minusAgeButton.setToolTipText("Reduce the lower bound of age range by 1.");
		GridBagConstraints gbc_minusAgeButton = new GridBagConstraints();
		gbc_minusAgeButton.gridwidth = 2;
		gbc_minusAgeButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_minusAgeButton.insets = new Insets(0, 0, 5, 5);
		gbc_minusAgeButton.gridx = 3;
		gbc_minusAgeButton.gridy = 10;
		add(minusAgeButton, gbc_minusAgeButton);
		minusAgeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		JButton addAgeButton = new JButton("");
		addAgeButton.setIcon(IconObtainer.get("add.png"));
		addAgeButton.setToolTipText("Increase the upper bound of age range by 1.");
		GridBagConstraints gbc_addAgeButton = new GridBagConstraints();
		gbc_addAgeButton.gridwidth = 2;
		gbc_addAgeButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_addAgeButton.insets = new Insets(0, 0, 5, 0);
		gbc_addAgeButton.gridx = 5;
		gbc_addAgeButton.gridy = 10;
		add(addAgeButton, gbc_addAgeButton);
		addAgeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		GridBagConstraints gbc_btnAgePanel = new GridBagConstraints();
		gbc_btnAgePanel.gridwidth = 7;
		gbc_btnAgePanel.insets = new Insets(0, 0, 5, 0);
		gbc_btnAgePanel.gridx = 0;
		gbc_btnAgePanel.gridy = 12;

//		ageSlider = setAgeSliderPanel();

		JButton btnAgeClear = new JButton("Clear");
		btnAgeClear.setFocusable(false);
		btnAgeClear.setIcon(IconObtainer.get("clear.png"));
		btnAgeClear.setToolTipText("Undo the age filter.");
		btnAgeClear.setEnabled(true);
		btnAgeClear.setFont(defaultFont);
		GridBagConstraints gbc_btnNewButton_2_1_1 = new GridBagConstraints();
		gbc_btnNewButton_2_1_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_2_1_1.gridwidth = 4;
		gbc_btnNewButton_2_1_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2_1_1.gridx = 3;
		gbc_btnNewButton_2_1_1.gridy = 11;
		add(btnAgeClear, gbc_btnNewButton_2_1_1);
//		add(ageSlider, gbc_btnAgePanel);
		btnAgeClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				delegateTheAction2threadPool(() -> {
//
//					SarsCov2MainFrame.getInstance().glassPaneStart();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(false);
//
//					filterState.setAgeFilterState(false);
////					controller.processAllFilter(filterState);
//					filterState.setAgeStart(Integer.parseInt(ageSlider.getMin4SlideRangeInString()));
//					filterState.setAgeEnd(Integer.parseInt(ageSlider.getMax4SlideRangeInString()));
//
//					ageSlider.setCurrentStartValue(ageSlider.getBoundRangeModel().getMinimum());
//					ageSlider.setCurrentEndValue(ageSlider.getBoundRangeModel().getMaximum());
//
//					ageSlider.repaint();
//
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(true);
//					SarsCov2MainFrame.getInstance().glassPaneStop();
//				});
			}
		});

		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.gridwidth = 7;
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 13;
		add(separator_2, gbc_separator_2);

		JLabel lblCollectionDate = new JLabel("Collection/Inferred date");
		lblCollectionDate.setFont(titleFont);
		GridBagConstraints gbc_lblCollectionDate = new GridBagConstraints();
		gbc_lblCollectionDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCollectionDate.gridwidth = 3;
		gbc_lblCollectionDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblCollectionDate.gridx = 0;
		gbc_lblCollectionDate.gridy = 14;
		add(lblCollectionDate, gbc_lblCollectionDate);

		GridBagConstraints gbc_btnDatePanel = new GridBagConstraints();
		gbc_btnDatePanel.gridwidth = 7;
		gbc_btnDatePanel.insets = new Insets(0, 0, 5, 5);
		gbc_btnDatePanel.gridx = 0;
		gbc_btnDatePanel.gridy = 16;
//		dateSlider = setDataSliderPanel();

		JButton minusDateButton = new JButton("");
		minusDateButton.setIcon(IconObtainer.get("minus.png"));
		minusDateButton.setToolTipText("Reduce the lower bound of date range by 1.");
		GridBagConstraints gbc_minusDateButton = new GridBagConstraints();
		gbc_minusDateButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_minusDateButton.gridwidth = 2;
		gbc_minusDateButton.insets = new Insets(0, 0, 5, 5);
		gbc_minusDateButton.gridx = 3;
		gbc_minusDateButton.gridy = 14;
		add(minusDateButton, gbc_minusDateButton);
		minusDateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		JButton addDateButton = new JButton("");
		addDateButton.setIcon(IconObtainer.get("add.png"));
		addDateButton.setToolTipText("Increase the upper bound of date range by 1.");
		GridBagConstraints gbc_addDateButton = new GridBagConstraints();
		gbc_addDateButton.gridwidth = 2;
		gbc_addDateButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_addDateButton.insets = new Insets(0, 0, 5, 0);
		gbc_addDateButton.gridx = 5;
		gbc_addDateButton.gridy = 14;
		add(addDateButton, gbc_addDateButton);

		addDateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		JCheckBox leafDateFilterCheckBox = new JCheckBox("Only the leaves");
		leafDateFilterCheckBox.setFont(defaultFont);
		GridBagConstraints gbc_leafDateCheckBox = new GridBagConstraints();
		gbc_leafDateCheckBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_leafDateCheckBox.gridwidth = 3;
		gbc_leafDateCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_leafDateCheckBox.gridx = 0;
		gbc_leafDateCheckBox.gridy = 15;
		add(leafDateFilterCheckBox, gbc_leafDateCheckBox);
		leafDateFilterCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				if (leafDateFilterCheckBox.isSelected()) {
//					filterState.setDateLeafFilter(true);
//				} else {
//					filterState.setDateLeafFilter(false);
//				}

			}
		});

		JButton btnDateClear = new JButton("Clear");
		btnDateClear.setFocusable(false);
		btnDateClear.setIcon(IconObtainer.get("clear.png"));
		btnDateClear.setToolTipText("Undo the date filter.");
		btnDateClear.setEnabled(true);
		btnDateClear.setFont(defaultFont);
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.gridwidth = 4;
		gbc_button_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_1.insets = new Insets(0, 0, 5, 0);
		gbc_button_1.gridx = 3;
		gbc_button_1.gridy = 15;
		add(btnDateClear, gbc_button_1);
//		add(dateSlider, gbc_btnDatePanel);
		btnDateClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				delegateTheAction2threadPool(() -> {
//
//					SarsCov2MainFrame.getInstance().glassPaneStart();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(false);
//
//					filterState.setDateFilterState(false);
////					controller.processAllFilter(filterState);
//
//					filterState.setDateStart(dateSlider.getMin4SlideRangeInString());
//					filterState.setDateEnd(dateSlider.getMax4SlideRangeInString());
//					dateSlider.setCurrentStartValue(dateSlider.getBoundRangeModel().getMinimum());
//					dateSlider.setCurrentEndValue(dateSlider.getBoundRangeModel().getMaximum());
//					dateSlider.repaint();
//
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(true);
//					SarsCov2MainFrame.getInstance().glassPaneStop();
//				});
			}
		});

		JButton btnShowAllSamples = new JButton("Show all samples");
		btnShowAllSamples.setFocusable(false);
		btnShowAllSamples.setIcon(IconObtainer.get("show_all.png"));
		btnShowAllSamples.setFont(defaultFont);
		GridBagConstraints gbc_btnShowAllSamples = new GridBagConstraints();
		gbc_btnShowAllSamples.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnShowAllSamples.gridwidth = 7;
		gbc_btnShowAllSamples.gridx = 0;
		gbc_btnShowAllSamples.gridy = 18;
		add(btnShowAllSamples, gbc_btnShowAllSamples);
		btnShowAllSamples.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				delegateTheAction2threadPool(() -> {
//					SarsCov2MainFrame.getInstance().glassPaneStart();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(false);
//					controller.showAllSamples();
//
//					try {
//						filterState.getFilterCountryRecordor().allCountryName();
//
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					chckbxMale.setSelected(true);
//					chckbxFemale.setSelected(true);
//					chckbxUnknow.setSelected(true);
//					btnSexClear.setEnabled(false);
//					dateSlider.setCurrentStartValue(dateSlider.getBoundRangeModel().getMinimum());
//					dateSlider.setCurrentEndValue(dateSlider.getBoundRangeModel().getMaximum());
//					dateSlider.repaint();
//					ageSlider.setCurrentStartValue(ageSlider.getBoundRangeModel().getMinimum());
//					ageSlider.setCurrentEndValue(ageSlider.getBoundRangeModel().getMaximum());
//					ageSlider.repaint();
//					filterState.setSexFilterState(false);
//					filterState.setAgeFilterState(false);
//					filterState.setDateFilterState(false);
//					filterState.setCountryFilterState(false);
//					controller.doLastColorSchemeRendering();
//					controller.doLastBranchLengthSetting();
//					for (java.awt.Component c : getComponents())
//						c.setEnabled(true);
//					SarsCov2MainFrame.getInstance().glassPaneStop();
//				});
			}
		});

	}

	protected String judgeSex() {
		String sexSelect = "";
		if (chckbxMale.isSelected() & chckbxFemale.isSelected() & chckbxUnknow.isSelected()) {
			sexSelect = "MFU";
		}
		if (chckbxMale.isSelected() & chckbxFemale.isSelected() & !chckbxUnknow.isSelected()) {
			sexSelect = "MF";
		}
		if (chckbxMale.isSelected() & !chckbxFemale.isSelected() & chckbxUnknow.isSelected()) {
			sexSelect = "MU";
		}
		if (!chckbxMale.isSelected() & chckbxFemale.isSelected() & chckbxUnknow.isSelected()) {
			sexSelect = "FU";
		}
		if (chckbxMale.isSelected() & !chckbxFemale.isSelected() & !chckbxUnknow.isSelected()) {
			sexSelect = "M";
		}
		if (!chckbxMale.isSelected() & chckbxFemale.isSelected() & !chckbxUnknow.isSelected()) {
			sexSelect = "F";
		}
		if (!chckbxMale.isSelected() & !chckbxFemale.isSelected() & chckbxUnknow.isSelected()) {
			sexSelect = "U";
		}
		return sexSelect;

	}

	/**
	 * 
	 * 设置和初始化年龄的slider
	 * 
	 * @title setAgeSliderPanel
	 * @createdDate
	 * @lastModifiedDate 2020-11-30 16:54
	 * @author yjn
	 * @since 1.7
	 * 
	 * @return
	 * @return NcovLeftSlider
	 */
//	private NcovLeftSlider setAgeSliderPanel() {
//
//		NcovLeftSlider ageSlider = new NcovLeftSlider("Age range", controller, "age", filterState);
//
//		AgeRangeValueCalculator ageRangeValue = new AgeRangeValueCalculator();
//
//		int rangeMaxValue = 0;
//		try {
//			rangeMaxValue = ageRangeValue.calculatorRangeMaxValue(getRootNode());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		NcovLeftBoundedRangeModel ageBoundedRangeModel = new NcovLeftDataDefaultBoundedRangeModel(0, rangeMaxValue, 0,
//				rangeMaxValue);
//		ageSlider.setBoundRangeModel(ageBoundedRangeModel);
//		ageSlider.setNcovLeftSliderCalculatorRangeValue(ageRangeValue);
//		ageSlider.setPreferredSize(new Dimension(300, 70));
//		ageSlider.setFont(defaultFont);
//		return ageSlider;
//
//	}

	/**
	 * 
	 * 设置和初始日期的slider
	 * 
	 * @title setDataSliderPanel
	 * @createdDate
	 * @lastModifiedDate 2020-11-30 16:55
	 * @author yjn
	 * @since 1.7
	 * 
	 * @return
	 * @return NcovLeftSlider
	 */
//	private NcovLeftSlider setDataSliderPanel() {
//		NcovLeftSlider dateSlider = new NcovLeftSlider("Date range", controller, "date", filterState);
//		DateRangeValueCalculator dateRangeValue = new DateRangeValueCalculator();
//
//		int rangeMaxValue = 0;
//		try {
//			rangeMaxValue = dateRangeValue.calculatorRangeMaxValue(getRootNode());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		NcovLeftDataDefaultBoundedRangeModel dateBoundedRangeModel = new NcovLeftDataDefaultBoundedRangeModel(0,
//				rangeMaxValue, 0, rangeMaxValue);
//		dateSlider.setBoundRangeModel(dateBoundedRangeModel);
//		dateSlider.setNcovLeftSliderCalculatorRangeValue(dateRangeValue);
//		dateSlider.setPreferredSize(new Dimension(300, 80));
//		dateSlider.setFont(defaultFont);
//		return dateSlider;
//
//	}

	public GraphicsNode getRootNode() {
		return null;
//		return treeLayoutProperties.getOriginalRootNode();
	}

	protected void delegateTheAction2threadPool(Runnable run) {
		new Thread(() -> {
			run.run();
		}).start();
	}

}

/**
 * 
 * 供搜索查询的默认提示例子增加的类
 * 
 * @title MyFocusListener
 * @createdDate
 * @lastModifiedDate 2020-11-30 15:41
 * @author yjn
 * @since 1.7
 *
 */
class MyFocusListener implements FocusListener {

	String info;
	JTextField jtf;

	public MyFocusListener(String info, JTextField jtf) {
		this.info = info;
		this.jtf = jtf;
	}

	@Override
	public void focusGained(FocusEvent e) {
		jtf.setForeground(Color.black);
	}

	@Override
	public void focusLost(FocusEvent e) {
//		String temp=jtf.getText();
//		if(temp.equals("")) {
//			jtf.setText(info);
//			jtf.setForeground(Color.lightGray);
//		}
	}

}
