package module.evoltrepipline;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

import org.dom4j.DocumentException;

import module.evoltre.pipline.TreeParameterHandler;

/**
 * 
 * @Package: egps.core.preferences
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-01-11 10:51
 * @author ydl
 * 
 * @Date 2024-04-27
 */
public class Panel4GenomeBrowser extends AbstractPrefShowContent {

	private static final long serialVersionUID = -3955234193499848072L;
	private Map<String, String> parameterMap;
	private List<String> ensembCommonlList = new ArrayList<String>(200);
	private List<String> UCSCCommonList = new ArrayList<String>(200);
	private List<String> ensembScientificlList = new ArrayList<String>(200);
	private List<String> UCSCScientificList = new ArrayList<String>(200);
	private JPanel genomeBrowser;
	private JComboBox<String> genomeBrowserComboBox;
	private JComboBox<String> speciesComboBox;
	
	private ConstantNameClass_GenomeBrowser cc = new ConstantNameClass_GenomeBrowser();

	public Panel4GenomeBrowser(Map<String, String> parameterMap2, String genomeBrowserName) {
		super.userObject = genomeBrowserName;
		this.parameterMap = parameterMap2;

		if (ensembCommonlList.size() == 0 || UCSCCommonList.size() == 0) {
			try {
				findBrowserInfo();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public JPanel getViewJPanel() {
		if (genomeBrowser == null) {
			genomeBrowser = createGenomeBrowserJPanel();
		}
		return genomeBrowser;
	}

	private JPanel createGenomeBrowserJPanel() {

		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		JPanel browserPanel = new JPanel(new GridBagLayout());
		browserPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				cc.category1_genomeBrowser, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));

		JLabel favouriteLabel = new JLabel(cc.label1_genomeBrowser);
		favouriteLabel.setFont(defaultFont);
		browserPanel.add(favouriteLabel,gridBagConstraints);

		speciesComboBox = new JComboBox<String>();
		speciesComboBox.setFocusable(false);
		for (int i = 0; i < ensembCommonlList.size(); i++) {
			String name = ensembCommonlList.get(i);
			speciesComboBox.addItem(name);
		}
		speciesComboBox.setFont(defaultFont);

		genomeBrowserComboBox = new JComboBox<String>();
		genomeBrowserComboBox.setFocusable(false);
		genomeBrowserComboBox.addItem(cc.genomeBrowser_value1_ensMain);
		genomeBrowserComboBox.addItem(cc.genomeBrowser_value2_ensAsia);
		genomeBrowserComboBox.addItem(cc.genomeBrowser_value3_ensUSW);
		genomeBrowserComboBox.addItem(cc.genomeBrowser_value4_ensUSE);
		genomeBrowserComboBox.addItem(cc.genomeBrowser_value5_ucscMain);
		genomeBrowserComboBox.addItem(cc.genomeBrowser_value6_ucscEu);
		genomeBrowserComboBox.addItem(cc.genomeBrowser_value7_ucscAsia);
		genomeBrowserComboBox.setFont(defaultFont);

		genomeBrowserComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = genomeBrowserComboBox.getSelectedItem().toString();
				speciesComboBox.removeAllItems();

				if (name.indexOf("ensembl") >= 0) {
					for (int i = 0; i < ensembCommonlList.size(); i++) {
						speciesComboBox.addItem(ensembCommonlList.get(i));
					}
				} else {
					for (int i = 0; i < UCSCCommonList.size(); i++) {
						speciesComboBox.addItem(UCSCCommonList.get(i));
					}
				}
			}
		});
		
		gridBagConstraints.gridx = 1;
		browserPanel.add(genomeBrowserComboBox,gridBagConstraints);

		JLabel speciesLabel = new JLabel(cc.label2_selectSpecies);
		speciesLabel.setFont(defaultFont);

		gridBagConstraints.gridy = 1;
		browserPanel.add(speciesComboBox,gridBagConstraints);
		gridBagConstraints.gridx = 0;
		browserPanel.add(speciesLabel,gridBagConstraints);

		if (parameterMap.size() > 0 && parameterMap != null) {

			String genomeBrowserWebSiteIndexValue = parameterMap.get(cc.genomeBrowser_index);

			if (genomeBrowserWebSiteIndexValue != null && !genomeBrowserWebSiteIndexValue.equals("")) {
				genomeBrowserComboBox.setSelectedIndex(Integer.valueOf(genomeBrowserWebSiteIndexValue));
			} else {
				genomeBrowserComboBox.setSelectedIndex(0);
			}

			String genomeBrowserSpeciesIndexValue = parameterMap.get(cc.selectSpecies_index);

			if (genomeBrowserSpeciesIndexValue != null && !genomeBrowserSpeciesIndexValue.equals("")) {
				speciesComboBox.setSelectedIndex(Integer.valueOf(genomeBrowserSpeciesIndexValue));
			} else {
				speciesComboBox.setSelectedIndex(0);
			}

		} else {
			genomeBrowserComboBox.setSelectedIndex(0);
			speciesComboBox.setSelectedIndex(0);
		}
		result.add(browserPanel);

		return result;
	}

	/*
	 * Rearrange the acquired species names.
	 */
	private void findBrowserInfo() throws DocumentException {
		TreeParameterHandler treeParameterHandler = new TreeParameterHandler();
		
		Map<String, String> ensembelSpeciesPropertiesMap = treeParameterHandler.getEnsembelSpeciesPropertiesMap();
		for (Entry<String, String> entry : ensembelSpeciesPropertiesMap.entrySet()) {
			String commonName = entry.getKey();
			ensembCommonlList.add(commonName);
			String scientificName = entry.getValue();
			ensembScientificlList.add(scientificName);
		}

		for (int i = 0, len = ensembCommonlList.size(); i < len; i++) {
			String name = ensembCommonlList.get(i);
			if ("Chimpanzee".equals(name) || "C. elegans".equals(name) || "Human".equals(name) || "Mouse".equals(name)
					|| "Orangutan".equals(name) || "Rat".equals(name) || "Zebrafish".equals(name)) {
				ensembCommonlList.remove(i);
				len--;
				i--;
			}
		}

		ensembCommonlList.add(0, "Zebrafish");
		ensembCommonlList.add(0, "C. elegans");
		ensembCommonlList.add(0, "Rat");
		ensembCommonlList.add(0, "Mouse");
		ensembCommonlList.add(0, "Orangutan");
		ensembCommonlList.add(0, "Chimpanzee");
		ensembCommonlList.add(0, "Human");

		for (int i = 0, len = ensembScientificlList.size(); i < len; i++) {
			String name = ensembScientificlList.get(i);
			if ("Danio_rerio".equals(name) || "Caenorhabditis_elegans".equals(name) || "Rattus_norvegicus".equals(name)
					|| "Mus_musculus".equals(name) || "Pongo_abelii".equals(name) || "Pan_troglodytes".equals(name)
					|| "Homo_sapiens".equals(name)) {
				ensembScientificlList.remove(i);
				len--;
				i--;
			}
		}

		ensembScientificlList.add(0, "Danio_rerio");
		ensembScientificlList.add(0, "Caenorhabditis_elegans");
		ensembScientificlList.add(0, "Rattus_norvegicus");
		ensembScientificlList.add(0, "Mus_musculus");
		ensembScientificlList.add(0, "Pongo_abelii");
		ensembScientificlList.add(0, "Pan_troglodytes");
		ensembScientificlList.add(0, "Homo_sapiens");


		Map<String, String> ucscSpeciesPropertiesMap = treeParameterHandler.getUCSCSpeciesPropertiesMap();
		for (Entry<String, String> entrySet : ucscSpeciesPropertiesMap.entrySet()) {
			String commonName = entrySet.getKey();
			UCSCCommonList.add(commonName);
			String scientificName = entrySet.getValue();
			UCSCScientificList.add(scientificName);
		}

		for (int i = 0, len = UCSCCommonList.size(); i < len; i++) {
			String name = UCSCCommonList.get(i);
			if ("Chimpanzee".equals(name) || "C. elegans".equals(name) || "Human".equals(name) || "Mouse".equals(name)
					|| "Orangutan".equals(name) || "Rat".equals(name) || "Zebrafish".equals(name)) {
				UCSCCommonList.remove(i);
				len--;
				i--;
			}
		}

		UCSCCommonList.add(0, "Zebrafish");
		UCSCCommonList.add(0, "C. elegans");
		UCSCCommonList.add(0, "Rat");
		UCSCCommonList.add(0, "Mouse");
		UCSCCommonList.add(0, "Orangutan");
		UCSCCommonList.add(0, "Chimpanzee");
		UCSCCommonList.add(0, "Human");

		for (int i = 0, len = UCSCScientificList.size(); i < len; i++) {
			String name = UCSCScientificList.get(i);
			if ("danRer11".equals(name) || "ce11".equals(name) || "rn6".equals(name) || "mm10".equals(name)
					|| "ponAbe2".equals(name) || "panTro3".equals(name) || "hg38".equals(name)) {
				UCSCScientificList.remove(i);
				len--;
				i--;
			}
		}

		UCSCScientificList.add(0, "danRer11");
		UCSCScientificList.add(0, "ce11");
		UCSCScientificList.add(0, "rn6");
		UCSCScientificList.add(0, "mm10");
		UCSCScientificList.add(0, "ponAbe2");
		UCSCScientificList.add(0, "panTro3");
		UCSCScientificList.add(0, "hg38");
	}

	@Override
	public void saveParameter(Map<String, String> parameters) {
		int browserComBoxIndex = genomeBrowserComboBox.getSelectedIndex();
		int speciesComboBoxIndex = speciesComboBox.getSelectedIndex();
		
		parameters.put(cc.label1_genomeBrowser, String.valueOf(genomeBrowserComboBox.getSelectedItem()));
		parameters.put(cc.genomeBrowser_index, String.valueOf(browserComBoxIndex));
		parameters.put(cc.label2_selectSpecies, String.valueOf(speciesComboBox.getSelectedItem()) );
		String scientificName = null;
		if (genomeBrowserComboBox.getSelectedItem().toString().indexOf("ensembl") >= 0) {
			scientificName = ensembScientificlList.get(speciesComboBoxIndex);
		} else {
			scientificName = UCSCScientificList.get(speciesComboBoxIndex);
		}
		parameters.put("GenomeBrowser_Scientific", scientificName);
		parameters.put(cc.selectSpecies_index, String.valueOf(speciesComboBoxIndex));
	}

	@Override
	public boolean checkParameters(JTree jTree) {
		return true;
	}

}
