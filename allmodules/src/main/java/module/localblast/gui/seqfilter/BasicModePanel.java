package module.localblast.gui.seqfilter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import egps2.frame.gui.handler.EGPSTextTransferHandler;

public class BasicModePanel extends JPanel {
	private JTextField txtFinField_db;
	private JTextField txt_entry;
	private JTextField textField_out;
	private JComboBox comboBox_dbType;
	private JTextField textField_outfmt;
	private JTextField textField_range;
	private JTextField textField_entryBatch;
	private JSeparator separator;
	private JComboBox comboBox_strand;

	/**
	 * Create the panel.
	 */
	public BasicModePanel() {
		setBorder(new EmptyBorder(0, 15, 0, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("db");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		txtFinField_db = new JTextField();
		txtFinField_db.setText("F:/yudalangWork/blastData/2019nCov/hash_ids/ncov");
		txtFinField_db.setTransferHandler(new EGPSTextTransferHandler());
		GridBagConstraints gbc_txtFinField_db = new GridBagConstraints();
		gbc_txtFinField_db.insets = new Insets(0, 0, 5, 0);
		gbc_txtFinField_db.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFinField_db.gridx = 1;
		gbc_txtFinField_db.gridy = 1;
		add(txtFinField_db, gbc_txtFinField_db);
		txtFinField_db.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("dbtype");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 2;
		add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		comboBox_dbType = new JComboBox();
		comboBox_dbType.setModel(new DefaultComboBoxModel(new String[] {"guess", "nucl", "prot"}));
		GridBagConstraints gbc_comboBox_dbType = new GridBagConstraints();
		gbc_comboBox_dbType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_dbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_dbType.gridx = 1;
		gbc_comboBox_dbType.gridy = 2;
		add(comboBox_dbType, gbc_comboBox_dbType);
		
		JLabel lblNewLabel_2 = new JLabel("entry");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 3;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		txt_entry = new JTextField();
		txt_entry.setToolTipText("Comma-delimited search string(s) of sequence identifiers:\r\n   \te.g.: 555, AC147927, 'gnl|dbname|tag', or 'all' to select all\r\n   \tsequences in the database\r\n    * Incompatible with:  entry_batch, ipg, ipg_batch, taxids, taxidlist,\r\n   info, tax_info, list, recursive, remove_redundant_dbs, list_outfmt,\r\n   show_blastdb_search_path");
		txt_entry.setText("all");
		GridBagConstraints gbc_txt_entry = new GridBagConstraints();
		gbc_txt_entry.insets = new Insets(0, 0, 5, 0);
		gbc_txt_entry.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_entry.gridx = 1;
		gbc_txt_entry.gridy = 3;
		add(txt_entry, gbc_txt_entry);
		txt_entry.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("range");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 4;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField_range = new JTextField();
		textField_range.setText("1-100");
		GridBagConstraints gbc_textField_range = new GridBagConstraints();
		gbc_textField_range.insets = new Insets(0, 0, 5, 0);
		gbc_textField_range.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_range.gridx = 1;
		gbc_textField_range.gridy = 4;
		add(textField_range, gbc_textField_range);
		textField_range.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("strand");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 5;
		add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		comboBox_strand = new JComboBox();
		comboBox_strand.setModel(new DefaultComboBoxModel(new String[] {"plus", "minus"}));
		GridBagConstraints gbc_comboBox_strand = new GridBagConstraints();
		gbc_comboBox_strand.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_strand.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_strand.gridx = 1;
		gbc_comboBox_strand.gridy = 5;
		add(comboBox_strand, gbc_comboBox_strand);
		
		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 6;
		add(separator, gbc_separator);
		
		JLabel lblNewLabel_5 = new JLabel("entry_batch");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 7;
		add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		textField_entryBatch = new JTextField();
		GridBagConstraints gbc_textField_entryBatch = new GridBagConstraints();
		gbc_textField_entryBatch.insets = new Insets(0, 0, 5, 0);
		gbc_textField_entryBatch.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_entryBatch.gridx = 1;
		gbc_textField_entryBatch.gridy = 7;
		add(textField_entryBatch, gbc_textField_entryBatch);
		textField_entryBatch.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("out");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 8;
		add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		textField_out = new JTextField();
		textField_out.setText("F:/yudalangWork/blastData/2019nCov/examDB");
		textField_out.setTransferHandler(new EGPSTextTransferHandler());
		GridBagConstraints gbc_textField_out = new GridBagConstraints();
		gbc_textField_out.insets = new Insets(0, 0, 5, 0);
		gbc_textField_out.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_out.gridx = 1;
		gbc_textField_out.gridy = 8;
		add(textField_out, gbc_textField_out);
		textField_out.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("outfmt");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 0;
		gbc_lblNewLabel_7.gridy = 9;
		add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		textField_outfmt = new JTextField();
		textField_outfmt.setText("%f");
		GridBagConstraints gbc_textField_outfmt = new GridBagConstraints();
		gbc_textField_outfmt.insets = new Insets(0, 0, 5, 0);
		gbc_textField_outfmt.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_outfmt.gridx = 1;
		gbc_textField_outfmt.gridy = 9;
		add(textField_outfmt, gbc_textField_outfmt);
		textField_outfmt.setColumns(10);

	}
	
	public BasicModeParameter getAllParameters() {
		
		BasicModeParameter ret = new BasicModeParameter();
		ret.setDbParameterString(txtFinField_db.getText());
		ret.setDbtypeParameterString(comboBox_dbType.getSelectedItem().toString());
		ret.setEntryParameterString(txt_entry.getText());
		ret.setRangeParameterString(textField_range.getText());
		ret.setStrandParameterString(comboBox_strand.getSelectedItem().toString());
		ret.setEntry_batchParameterString(textField_entryBatch.getText());
		
		ret.setOutParameterString(textField_out.getText());
		ret.setOutfmtParameterString(textField_outfmt.getText());
		
		return ret;
	}

}
