package module.evolview.gfamily.work.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.model.enums.ColorScheme;

/**
 * 
 * @title LeftColorSchemePanel
 * @createdDate 2020-11-25 10:40
 * @lastModifiedDate 2020-11-25 10:40
 * @author yudalang, mhl, yjn
 * @since 1.7
 *
 */
@SuppressWarnings("serial")
public class CtrlColorSchemePanel extends BaseCtrlPanel implements ItemListener{
	private Font defaultFont;
	private GeneFamilyController controller;

	private JRadioButton rdbtnCountriesRegions;
	private JRadioButton rdbtnGender;
	private JRadioButton rdbtnNoColorRendering;
	private JRadioButton rdbtnPatientAge;
	private JRadioButton rdbtnCollectionDate;
	private JRadioButton rdbtnCustomized;

	private JButton buttonAgeAdj;
	private JButton buttonGenderAdj;
	private JButton btnCountryAdj;
	private JButton buttonDateAdj;

	public CtrlColorSchemePanel() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0 };
		gridBagLayout.columnWidths = new int[] { 0, 0 };

		setLayout(gridBagLayout);
		defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		ButtonGroup buttonGroup = new ButtonGroup();

		rdbtnNoColorRendering = new JRadioButton("No color rendering");
		rdbtnNoColorRendering.setFocusable(false);
		rdbtnNoColorRendering.setToolTipText("");
		rdbtnNoColorRendering.setFont(defaultFont);
		GridBagConstraints gbc_rdbtnNoColorRendering = new GridBagConstraints();
		gbc_rdbtnNoColorRendering.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNoColorRendering.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNoColorRendering.gridx = 0;
		gbc_rdbtnNoColorRendering.gridy = 0;
		add(rdbtnNoColorRendering, gbc_rdbtnNoColorRendering);
		buttonGroup.add(rdbtnNoColorRendering);

		JSeparator separator_4 = new JSeparator();
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_4.gridwidth = 2;
		gbc_separator_4.insets = new Insets(0, 0, 5, 5);
		gbc_separator_4.gridx = 0;
		gbc_separator_4.gridy = 1;
		add(separator_4, gbc_separator_4);

		rdbtnCountriesRegions = new JRadioButton("Countries & regions");
		rdbtnCountriesRegions.setFocusable(false);
		rdbtnCountriesRegions.setSelected(true);
		rdbtnCountriesRegions.setToolTipText("Warning: Please take care to interpret the results!");
		rdbtnCountriesRegions.setFont(defaultFont);
		GridBagConstraints gbc_rdbtnCountriesRegions = new GridBagConstraints();
		gbc_rdbtnCountriesRegions.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCountriesRegions.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnCountriesRegions.gridx = 0;
		gbc_rdbtnCountriesRegions.gridy = 2;
		add(rdbtnCountriesRegions, gbc_rdbtnCountriesRegions);
		buttonGroup.add(rdbtnCountriesRegions);

		btnCountryAdj = new JButton();
		btnCountryAdj.setIcon(IconObtainer.get("color.png"));
		btnCountryAdj.setFocusable(false);
		btnCountryAdj.setFont(defaultFont);
		btnCountryAdj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		GridBagConstraints gbc_btnCountryAdj = new GridBagConstraints();
		gbc_btnCountryAdj.insets = new Insets(0, 0, 5, 0);
		gbc_btnCountryAdj.gridx = 1;
		gbc_btnCountryAdj.gridy = 2;
		add(btnCountryAdj, gbc_btnCountryAdj);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 3;
		add(separator, gbc_separator);

		JLabel lblGender = new JLabel("");
		lblGender.setFont(defaultFont);
		GridBagConstraints gbc_lblGender = new GridBagConstraints();
		gbc_lblGender.anchor = GridBagConstraints.WEST;
		gbc_lblGender.insets = new Insets(0, 0, 5, 5);
		gbc_lblGender.gridx = 0;
		gbc_lblGender.gridy = 4;
		add(lblGender, gbc_lblGender);

		rdbtnGender = new JRadioButton("Gender");
		rdbtnGender.setFocusable(false);
		rdbtnGender.setFont(defaultFont);
		GridBagConstraints gbc_rdbtnGender = new GridBagConstraints();
		gbc_rdbtnGender.anchor = GridBagConstraints.WEST;
		gbc_rdbtnGender.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnGender.gridx = 0;
		gbc_rdbtnGender.gridy = 4;
		add(rdbtnGender, gbc_rdbtnGender);
		buttonGroup.add(rdbtnGender);

		buttonGenderAdj = new JButton();
		buttonGenderAdj.setIcon(IconObtainer.get("color.png"));
		buttonGenderAdj.setFocusable(false);
		buttonGenderAdj.setEnabled(false);
		buttonGenderAdj.setFont(defaultFont);
		GridBagConstraints gbc_buttonGenderAdj = new GridBagConstraints();
		gbc_buttonGenderAdj.insets = new Insets(0, 0, 5, 0);
		gbc_buttonGenderAdj.gridx = 1;
		gbc_buttonGenderAdj.gridy = 4;
		add(buttonGenderAdj, gbc_buttonGenderAdj);
		buttonGenderAdj.addActionListener(e -> {
			
		});

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 2;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 5;
		add(separator_1, gbc_separator_1);

		rdbtnPatientAge = new JRadioButton("Patient age");
		rdbtnPatientAge.setFocusable(false);
		rdbtnPatientAge.setFont(defaultFont);
		GridBagConstraints gbc_rdbtnPatientAge = new GridBagConstraints();
		gbc_rdbtnPatientAge.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPatientAge.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPatientAge.gridx = 0;
		gbc_rdbtnPatientAge.gridy = 6;
		add(rdbtnPatientAge, gbc_rdbtnPatientAge);
		buttonGroup.add(rdbtnPatientAge);

		buttonAgeAdj = new JButton();
		buttonAgeAdj.setIcon(IconObtainer.get("color.png"));
		buttonAgeAdj.setFocusable(false);
		buttonAgeAdj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonAgeAdj.setEnabled(false);
		buttonAgeAdj.setFont(defaultFont);
		GridBagConstraints gbc_buttonAgeAdj = new GridBagConstraints();
		gbc_buttonAgeAdj.insets = new Insets(0, 0, 5, 0);
		gbc_buttonAgeAdj.gridx = 1;
		gbc_buttonAgeAdj.gridy = 6;
		add(buttonAgeAdj, gbc_buttonAgeAdj);

		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.gridwidth = 2;
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 7;
		add(separator_2, gbc_separator_2);

		rdbtnCollectionDate = new JRadioButton("Collection date");
		rdbtnCollectionDate.setFocusable(false);
		rdbtnCollectionDate.setFont(defaultFont);
		GridBagConstraints gbc_rdbtnCollectionDate = new GridBagConstraints();
		gbc_rdbtnCollectionDate.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCollectionDate.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnCollectionDate.gridx = 0;
		gbc_rdbtnCollectionDate.gridy = 8;
		add(rdbtnCollectionDate, gbc_rdbtnCollectionDate);
		buttonGroup.add(rdbtnCollectionDate);

		buttonDateAdj = new JButton();
		buttonDateAdj.setIcon(IconObtainer.get("color.png"));
		buttonDateAdj.setFocusable(false);
		buttonDateAdj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonDateAdj.setEnabled(false);
		buttonDateAdj.setFont(defaultFont);
		GridBagConstraints gbc_buttonDateAdj = new GridBagConstraints();
		gbc_buttonDateAdj.insets = new Insets(0, 0, 5, 0);
		gbc_buttonDateAdj.gridx = 1;
		gbc_buttonDateAdj.gridy = 8;
		add(buttonDateAdj, gbc_buttonDateAdj);

		JSeparator separator_3 = new JSeparator();
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_3.gridwidth = 2;
		gbc_separator_3.insets = new Insets(0, 0, 5, 0);
		gbc_separator_3.gridx = 0;
		gbc_separator_3.gridy = 9;
		add(separator_3, gbc_separator_3);

		rdbtnCustomized = new JRadioButton("Customization");
		rdbtnCustomized.setFocusable(false);
		rdbtnCustomized.setEnabled(false);
		rdbtnCustomized.setFont(defaultFont);
		GridBagConstraints gbc_rdbtnCustomized = new GridBagConstraints();
		gbc_rdbtnCustomized.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCustomized.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnCustomized.gridx = 0;
		gbc_rdbtnCustomized.gridy = 10;
		add(rdbtnCustomized, gbc_rdbtnCustomized);
		buttonGroup.add(rdbtnCustomized);

		JButton btnAddCustomedScheme = new JButton("Add customized scheme");
		btnAddCustomedScheme.setFocusable(false);
		btnAddCustomedScheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnAddCustomedScheme.setFont(defaultFont);
		GridBagConstraints gbc_btnAddCustomedScheme = new GridBagConstraints();
		gbc_btnAddCustomedScheme.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddCustomedScheme.gridwidth = 2;
		gbc_btnAddCustomedScheme.gridx = 0;
		gbc_btnAddCustomedScheme.gridy = 11;
		add(btnAddCustomedScheme, gbc_btnAddCustomedScheme);

		rdbtnNoColorRendering.addItemListener(this);
		rdbtnCountriesRegions.addItemListener(this);
		rdbtnGender.addItemListener(this);
		rdbtnPatientAge.addItemListener(this);
		rdbtnCollectionDate.addItemListener(this);
		rdbtnCustomized.addItemListener(this);

//		new Thread(() -> {
//			renderAccording2Country();
//		}).start();
	}

	/**
	 * 
	 */
	public void letCustomizedRadioButtonBeSelected() {
		rdbtnCustomized.setSelected(true);

	}

	/**
	 * 
	 * 当撤销自定义染色的时候，需要调用此方法重新设置别的染色方案的选中
	 * 
	 * @title setButtonSelected
	 * @createdDate 2020-11-11 15:38
	 * @lastModifiedDate 2020-11-11 15:38
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param colorScheme
	 * @return void
	 */
	public void setButtonSelected(ColorScheme colorScheme) {
		switch (colorScheme) {
		case NO_COLOR:
			rdbtnNoColorRendering.setSelected(true);
			break;
		case COUNTRIESREGIONS:
			rdbtnCountriesRegions.setSelected(true);
			break;
		case GENDER:
			rdbtnGender.setSelected(true);
			break;
		case PATIENTAGE:
			rdbtnPatientAge.setSelected(true);
			break;
		case COLLECTIONDATE:
			rdbtnCollectionDate.setSelected(true);
			break;
		case CUSTOMIZED:
			rdbtnCustomized.setSelected(true);
			break;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		boolean isCustomized = false;

		if (e.getStateChange() == ItemEvent.DESELECTED) {
			return;
		}

		ColorScheme colorScheme = null;
		Object source = e.getSource();

		if (source == rdbtnNoColorRendering) {
			buttonAgeAdj.setEnabled(false);
			buttonGenderAdj.setEnabled(false);
			btnCountryAdj.setEnabled(false);
			buttonDateAdj.setEnabled(false);
			colorScheme = ColorScheme.NO_COLOR;
		} else if (source == rdbtnCountriesRegions) {
			buttonAgeAdj.setEnabled(false);
			buttonGenderAdj.setEnabled(false);
			btnCountryAdj.setEnabled(true);
			buttonDateAdj.setEnabled(false);
			controller.refreshPhylogeneticTree();
			colorScheme = ColorScheme.COUNTRIESREGIONS;
		} else if (source == rdbtnGender) {
//				controller.refreshPhylogeneticTree();
			buttonAgeAdj.setEnabled(false);
			buttonGenderAdj.setEnabled(true);
			btnCountryAdj.setEnabled(false);
			buttonDateAdj.setEnabled(false);
			colorScheme = ColorScheme.GENDER;
		} else if (source == rdbtnPatientAge) {
//				controller.refreshPhylogeneticTree();
			buttonAgeAdj.setEnabled(true);
			buttonGenderAdj.setEnabled(false);
			btnCountryAdj.setEnabled(false);
			buttonDateAdj.setEnabled(false);
			colorScheme = ColorScheme.PATIENTAGE;
		} else if (source == rdbtnCollectionDate) {
			buttonAgeAdj.setEnabled(false);
			buttonGenderAdj.setEnabled(false);
			btnCountryAdj.setEnabled(false);
			buttonDateAdj.setEnabled(true);
			colorScheme = ColorScheme.COLLECTIONDATE;
		} else {
			// rdbtnCustomized
			buttonAgeAdj.setEnabled(false);
			buttonGenderAdj.setEnabled(false);
			btnCountryAdj.setEnabled(false);
			buttonDateAdj.setEnabled(false);
			colorScheme = ColorScheme.CUSTOMIZED;
			isCustomized = true;
		}

		if (!isCustomized) {
			final ColorScheme tt = colorScheme;
			new Thread(() -> {
				controller.changeColorScheme(tt);
			}).start();
		}
	}


}
