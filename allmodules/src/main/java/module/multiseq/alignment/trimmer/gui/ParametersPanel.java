package module.multiseq.alignment.trimmer.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.multiseq.alignment.trimmer.SimpleModuleController;
import fasta.io.FastaReader;

public class ParametersPanel extends JPanel {
	

	private JComboBox<String> refNameList;
	private JSpinner spinner_numOfSites;
	private JSpinner spinner_endPos;
	private JSpinner spinner_startPos;

	/**
	 * Create the panel.
	 */
	public ParametersPanel(SimpleModuleController controller) {
		controller.setParametersPanel(this);
		
		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Reference sequence");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		refNameList = new JComboBox<>();
		refNameList.setEditable(true);
		GridBagConstraints gbc_textField_refName = new GridBagConstraints();
		gbc_textField_refName.insets = new Insets(0, 0, 5, 5);
		gbc_textField_refName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_refName.gridx = 1;
		gbc_textField_refName.gridy = 0;
		add(refNameList, gbc_textField_refName);
		
		JButton btnNewButton = new JButton("Initialize");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(() -> {

					String path = controller.getInputFilePath();
					if (path.isEmpty()) {
						SwingDialog.showErrorMSGDialog("Input error", "You need to import file first!");
					} else {
						LinkedHashMap<String, String> map = null;
						try {
							map = FastaReader.readFastaDNASequence(new File(path));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						String refSequenceName = controller.getRefSequenceName();
						if (refSequenceName.isEmpty()) {
							//When th einput JTextfield
							
							Collection<String> values = map.values();
							Iterator<String> iterator = values.iterator();
							String next = iterator.next();
							spinner_numOfSites.setValue(next.replaceAll("-", "").length());
							
							refNameList.removeAllItems();
							Set<String> keySet = map.keySet();
							for (String string : keySet) {
								refNameList.addItem(string);
							}
						}else {
							String seq = map.get(refSequenceName);

							if (seq == null) {
								SwingDialog.showErrorMSGDialog("Input error",
										"Can not find your input reference sequence!");
							} else {
								spinner_numOfSites.setValue(seq.replaceAll("-", "").length());
								
								if (refNameList.getItemCount() > 0) {
									refNameList.removeAllItems();
								}
								Set<String> keySet = map.keySet();
								for (String string : keySet) {
									refNameList.addItem(string);
								}
								refNameList.setSelectedItem(refSequenceName);
							}
						}
						
					}

				}).start();
			}
		});
		btnNewButton.setToolTipText("Clear the value in the editor to detect availiable sequences !");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("Total num. of sites");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		spinner_numOfSites = new JSpinner();
		spinner_numOfSites.setEnabled(false);
		spinner_numOfSites.setModel(new SpinnerNumberModel(1, 1, null, 1));
		GridBagConstraints gbc_spinner_numOfSites = new GridBagConstraints();
		gbc_spinner_numOfSites.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_numOfSites.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_numOfSites.gridx = 1;
		gbc_spinner_numOfSites.gridy = 1;
		add(spinner_numOfSites, gbc_spinner_numOfSites);
		
		JLabel lblNewLabel_1 = new JLabel("Start position");
		lblNewLabel_1.setToolTipText("One-based start position");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		spinner_startPos = new JSpinner();
		spinner_startPos.setModel(new SpinnerNumberModel(1, 1, null, 1));
		GridBagConstraints gbc_spinner_startPos = new GridBagConstraints();
		gbc_spinner_startPos.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_startPos.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_startPos.gridx = 1;
		gbc_spinner_startPos.gridy = 2;
		add(spinner_startPos, gbc_spinner_startPos);
		
		JLabel lblNewLabel_2 = new JLabel("End position");
		lblNewLabel_2.setToolTipText("One-based end position");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 3;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		spinner_endPos = new JSpinner();
		spinner_endPos.setModel(new SpinnerNumberModel(100, 1, null, 1));
		GridBagConstraints gbc_spinner_endPos = new GridBagConstraints();
		gbc_spinner_endPos.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_endPos.insets = new Insets(0, 0, 0, 5);
		gbc_spinner_endPos.gridx = 1;
		gbc_spinner_endPos.gridy = 3;
		add(spinner_endPos, gbc_spinner_endPos);
		
		Component[] components = getComponents();
		for (Component component : components) {
			component.setFont(UnifiedAccessPoint.getLaunchProperty().getDefaultFont());
		}

	}

	public String getRefSequenceName() {
		Object selectedItem = refNameList.getSelectedItem();
		
		if (selectedItem == null) {
			return "";
		}
		return selectedItem.toString();
	}

	public int getRefSequenceStartPos() {
		return (int) spinner_startPos.getValue();
	}

	public int getRefSequenceEndPos() {
		return (int) spinner_endPos.getValue();
	}
	public JComboBox<String> getRefNameList() {
		return refNameList;
	}

}
