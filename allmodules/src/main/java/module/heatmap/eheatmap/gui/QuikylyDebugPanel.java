package module.heatmap.eheatmap.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import module.heatmap.EheatmapMain;

public class QuikylyDebugPanel extends JPanel{
	
	private final EheatmapMain eheatmapMain;

	public QuikylyDebugPanel(EheatmapMain main) {
		this.eheatmapMain = main;
		setLayout(new GridBagLayout());
		initializa();
	}

	private void initializa() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		
		JLabel matrixJLabel = new JLabel();
		matrixJLabel.setText("Regions");
		matrixJLabel.setFont(eheatmapMain.defaultFont);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.add(matrixJLabel, gridBagConstraints);
		
		String[] listData = new String[] { "only heatmap", "map, left tree",
				"map and tree","map and name","map tree and name","map/T/name/legend"
				,"all"};
		JComboBox<String> comboBox = new JComboBox<String>(listData);
		comboBox.setFont(eheatmapMain.defaultFont);
		comboBox.setSelectedIndex(0);
		
		comboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				eheatmapMain.quicklyResetData(comboBox.getSelectedIndex());
			}
		});
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		this.add(comboBox, gridBagConstraints);
		
	}
}
