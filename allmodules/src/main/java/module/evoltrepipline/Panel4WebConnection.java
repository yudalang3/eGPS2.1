package module.evoltrepipline;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

import egps2.panels.dialog.EGPSJSpinner;
import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.math.CheckedNumber;

/**
 * 
 * @Package: egps.core.preferences
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-01-11 10:51
 * 
 * @author ydl
 * 
 * @Date 2024-04-27
 */
public class Panel4WebConnection extends AbstractPrefShowContent {

	private static final long serialVersionUID = 5562371735380833299L;
	private Map<String, String> parameterMap;
	private JPanel connectionPanel;
	private JSpinner timeSpinner;
	private ConstantNameClass_WebConnection cc = new ConstantNameClass_WebConnection();

	public Panel4WebConnection(Map<String, String> parameterMap, String connectionName) {
		super.userObject = connectionName;
		this.parameterMap = parameterMap;
	}

	@Override
	public JPanel getViewJPanel() {
		if (connectionPanel == null) {
			connectionPanel = createConnectionJPanel();
		}
		return connectionPanel;
	}

	private JPanel createConnectionJPanel() {

		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel timePanel = new JPanel(new GridBagLayout());
		timePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1),
				cc.category1_connection, TitledBorder.LEFT, TitledBorder.TOP, defaultFont));

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		JLabel timeLabel = new JLabel(cc.label1_timeOut);
		timeLabel.setFont(defaultFont);
		timePanel.add(timeLabel, gridBagConstraints);

		JPanel littlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		int minValue = 3;
		int maxValue = 60;
		int currentValue = 30;
		int steps = 1;
		timeSpinner = new EGPSJSpinner(currentValue, minValue, maxValue, steps);
		timeSpinner.setToolTipText("The value must be between 3 and 60.");
		timeSpinner.setFont(defaultFont);
		gridBagConstraints.gridx = 3;
		littlePanel.add(timeSpinner);

		JLabel secondLabel = new JLabel("second");
		secondLabel.setFont(defaultFont);
		gridBagConstraints.gridx = 4;
		littlePanel.add(secondLabel);

		int timeOutValue = Integer.valueOf(parameterMap.get(cc.label1_timeOut).trim());

		if (timeOutValue >= 3 && timeOutValue <= 60) {
			timeSpinner.setValue(Integer.valueOf(timeOutValue));
		} else {
			timeSpinner.setValue(30);
		}

		gridBagConstraints.gridx = 1;
		gridBagConstraints.insets = new Insets(5, 35, 5, 5);
		timePanel.add(littlePanel, gridBagConstraints);

		result.add(timePanel);

		return result;
	}

	@Override
	public void saveParameter(Map<String, String> parameters) {
		String timeOutText = timeSpinner.getValue().toString();
		parameters.put(cc.label1_timeOut, timeOutText);

	}

	@Override
	public boolean checkParameters(JTree jTree) {
		boolean flagTime = false;
		try {
			String timeValue = timeSpinner.getValue().toString();
			boolean timeFlag = CheckedNumber.isPositiveInteger(timeValue, false);
			if (timeFlag) {
				int cutNumber = Integer.valueOf(timeValue);
				if (cutNumber >= 3 && cutNumber <= 60) {
					flagTime = true;
				} else {
					jTree.setSelectionRow(1);
					timeSpinner.requestFocus();
					SwingDialog.showMSGDialog("Error", "The value must be between 3 and 60!", 1);
				}
			} else {
				jTree.setSelectionRow(1);
				timeSpinner.requestFocus();
				SwingDialog.showMSGDialog("Error", "The value must be between 3 and 60!", 1);
			}
		} catch (Exception e) {
			jTree.setSelectionRow(1);
			timeSpinner.requestFocus();
			SwingDialog.showMSGDialog("Error", "The value must be between 3 and 60!", 1);
		}

		return flagTime;
	}
}
