package module.localblast.gui.dbtools;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import egps2.frame.gui.handler.EGPSTextTransferHandler;
import egps2.UnifiedAccessPoint;
import module.localblast.gui.util.JTextFieldHintListener;

@SuppressWarnings("serial")
public class BasicModePanel extends JPanel {
	private JTextField txtFinField;
	private JTextField txtEnterTitleHere;
	private JTextField txtFouTextField;
	private JComboBox<String> comboBox_inputType;
	private JCheckBox chckbx_parse_seqids;
	private JCheckBox chckbx_hash_index;
	private JComboBox<String> comboBox_dbType;

	/**
	 * Create the panel.
	 */
	public BasicModePanel() {
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		setBorder(new EmptyBorder(0, 15, 0, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("in");
		lblNewLabel.setFont(defaultFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		txtFinField = new JTextField();
		txtFinField.setFont(defaultFont);
		txtFinField.addFocusListener(new JTextFieldHintListener(txtFinField, "Input fasta file here, mandatory"));

		txtFinField.setTransferHandler(new EGPSTextTransferHandler());
		GridBagConstraints gbc_txtFyudalangworkblastdatancovsequencefasta = new GridBagConstraints();
		gbc_txtFyudalangworkblastdatancovsequencefasta.insets = new Insets(0, 0, 5, 0);
		gbc_txtFyudalangworkblastdatancovsequencefasta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFyudalangworkblastdatancovsequencefasta.gridx = 1;
		gbc_txtFyudalangworkblastdatancovsequencefasta.gridy = 0;
		add(txtFinField, gbc_txtFyudalangworkblastdatancovsequencefasta);
		txtFinField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("input_type");
		lblNewLabel_1.setFont(defaultFont);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		comboBox_inputType = new JComboBox<>();
		comboBox_inputType.setFont(defaultFont);
		comboBox_inputType.setModel(
				new DefaultComboBoxModel<String>(new String[] { "fasta", "asn1_bin", "asn1_txt", "blastdb" }));
		GridBagConstraints gbc_comboBox_inputType = new GridBagConstraints();
		gbc_comboBox_inputType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_inputType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_inputType.gridx = 1;
		gbc_comboBox_inputType.gridy = 1;
		add(comboBox_inputType, gbc_comboBox_inputType);

		JLabel lblNewLabel_3 = new JLabel("dbtype");
		lblNewLabel_3.setFont(defaultFont);
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 2;
		add(lblNewLabel_3, gbc_lblNewLabel_3);

		comboBox_dbType = new JComboBox<>();
		comboBox_dbType.setFont(defaultFont);
		comboBox_dbType.setModel(new DefaultComboBoxModel<String>(new String[] { "nucl", "prot" }));
		GridBagConstraints gbc_comboBox_dbType = new GridBagConstraints();
		gbc_comboBox_dbType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_dbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_dbType.gridx = 1;
		gbc_comboBox_dbType.gridy = 2;
		add(comboBox_dbType, gbc_comboBox_dbType);

		JLabel lblNewLabel_2 = new JLabel("title");
		lblNewLabel_2.setFont(defaultFont);
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 3;
		add(lblNewLabel_2, gbc_lblNewLabel_2);

		txtEnterTitleHere = new JTextField();
		txtEnterTitleHere.setFont(defaultFont);
		txtEnterTitleHere
				.addFocusListener(new JTextFieldHintListener(txtEnterTitleHere, "\"Enter title here\", example: myDB"));
		GridBagConstraints gbc_txtEnterTitleHere = new GridBagConstraints();
		gbc_txtEnterTitleHere.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterTitleHere.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterTitleHere.gridx = 1;
		gbc_txtEnterTitleHere.gridy = 3;
		add(txtEnterTitleHere, gbc_txtEnterTitleHere);
		txtEnterTitleHere.setColumns(10);

		chckbx_parse_seqids = new JCheckBox("parse_seqids");
		String text = "<html>Option to parse seqid for FASTA input if set, for all other input types seqids are parsed automatically.<br>Specific sequence retrieval requires the database be created with -parse_seqids option.";

		chckbx_parse_seqids.setToolTipText(text);
		chckbx_parse_seqids.setFont(defaultFont);
		chckbx_parse_seqids.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_chckbx_parse_seqids = new GridBagConstraints();
		gbc_chckbx_parse_seqids.anchor = GridBagConstraints.WEST;
		gbc_chckbx_parse_seqids.insets = new Insets(0, 0, 5, 0);
		gbc_chckbx_parse_seqids.gridx = 1;
		gbc_chckbx_parse_seqids.gridy = 4;
		add(chckbx_parse_seqids, gbc_chckbx_parse_seqids);

		chckbx_hash_index = new JCheckBox("hash_index");
		chckbx_hash_index.setFont(defaultFont);
		chckbx_hash_index.setHorizontalAlignment(SwingConstants.LEFT);
		chckbx_hash_index.setToolTipText(
				"<html>Create index of sequence hash values. <br> Take more time and space while more effecient for further analysis.");
		GridBagConstraints gbc_chckbx_hash_index = new GridBagConstraints();
		gbc_chckbx_hash_index.anchor = GridBagConstraints.WEST;
		gbc_chckbx_hash_index.insets = new Insets(0, 0, 5, 0);
		gbc_chckbx_hash_index.gridx = 1;
		gbc_chckbx_hash_index.gridy = 5;
		add(chckbx_hash_index, gbc_chckbx_hash_index);

		JLabel lblNewLabel_4 = new JLabel("out");
		lblNewLabel_4.setFont(defaultFont);
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 6;
		add(lblNewLabel_4, gbc_lblNewLabel_4);

		txtFouTextField = new JTextField();
		txtFouTextField.setFont(defaultFont);
		txtFouTextField.addFocusListener(new JTextFieldHintListener(txtFouTextField,
				"Output db path and name, mandatory. Example: a/b/c/d/myDB"));
		txtFouTextField.setTransferHandler(new EGPSTextTransferHandler());
		GridBagConstraints gbc_txtFyudalangworkblastdatancovexamdb = new GridBagConstraints();
		gbc_txtFyudalangworkblastdatancovexamdb.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFyudalangworkblastdatancovexamdb.gridx = 1;
		gbc_txtFyudalangworkblastdatancovexamdb.gridy = 6;
		add(txtFouTextField, gbc_txtFyudalangworkblastdatancovexamdb);
		txtFouTextField.setColumns(10);

	}

	public BasicModeParameter getAllParameters() {

		BasicModeParameter ret = new BasicModeParameter();
		ret.setDbtypeParameterString(comboBox_dbType.getSelectedItem().toString());
		ret.setInParameterString(txtFinField.getText());
		ret.setTitleParameterString(txtEnterTitleHere.getText());
		ret.setOutParameterString(txtFouTextField.getText());
		ret.setInput_typeParameterString(comboBox_inputType.getSelectedItem().toString());
		ret.setIfHash_index(chckbx_hash_index.isSelected());
		ret.setIfParse_seqids(chckbx_parse_seqids.isSelected());

		return ret;
	}

}
