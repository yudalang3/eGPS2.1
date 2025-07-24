package module.tablelikeview.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import utils.EGPSFileUtil;
import egps2.UnifiedAccessPoint;
import module.tablelikeview.MainGUI;
import module.tablelikeview.io.WorkImportInfoBean;

public class NavigationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldCurrentLine;
	private JTextField textFieldTotalLine;

	private JTextField textFieldPageSize;

	/**
	 * Create the panel.
	 * 
	 * @param mainGUI
	 */
	public NavigationPanel(MainGUI mainGUI) {
		mainGUI.setNavigationPanel(this);
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		setBorder(new EmptyBorder(15, 15, 15, 15));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Current line :");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		textFieldCurrentLine = new JTextField();
		textFieldCurrentLine.setHorizontalAlignment(JTextField.CENTER);
		textFieldCurrentLine.setFont(globalFont);
		GridBagConstraints gbc_textFieldCurrentLine = new GridBagConstraints();
		gbc_textFieldCurrentLine.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldCurrentLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCurrentLine.gridx = 1;
		gbc_textFieldCurrentLine.gridy = 0;
		add(textFieldCurrentLine, gbc_textFieldCurrentLine);
		textFieldCurrentLine.setColumns(10);

		JButton btnGoTo = new JButton("Goto");
		btnGoTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textFieldCurrentLine.getText();
				int int1 = Integer.parseInt(text);
				mainGUI.gotoLineOf(int1);
			}
		});
		btnGoTo.setFont(globalFont);
		GridBagConstraints gbc_btnGoTo = new GridBagConstraints();
		gbc_btnGoTo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGoTo.insets = new Insets(0, 0, 5, 0);
		gbc_btnGoTo.gridx = 1;
		gbc_btnGoTo.gridy = 1;
		add(btnGoTo, gbc_btnGoTo);

		JLabel lblNewLabel_1 = new JLabel("Total line :");
		lblNewLabel_1.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		textFieldTotalLine = new JTextField();
		textFieldTotalLine.setHorizontalAlignment(JTextField.CENTER);
		textFieldTotalLine.setColumns(10);
		textFieldTotalLine.setEditable(false);
		textFieldTotalLine.setFont(globalFont);
		GridBagConstraints gbc_textFieldTotalLine = new GridBagConstraints();
		gbc_textFieldTotalLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTotalLine.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldTotalLine.gridx = 1;
		gbc_textFieldTotalLine.gridy = 2;
		add(textFieldTotalLine, gbc_textFieldTotalLine);
		
		JLabel lblNewLabel_3 = new JLabel("Page size :");
		lblNewLabel_3.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		add(lblNewLabel_3, gbc_lblNewLabel_3);

		textFieldPageSize = new JTextField();
		textFieldPageSize.setEditable(false);
		textFieldPageSize.setHorizontalAlignment(JTextField.CENTER);
		textFieldPageSize.setFont(globalFont);
		GridBagConstraints gbc_textFieldPageSize = new GridBagConstraints();
		gbc_textFieldPageSize.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldPageSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPageSize.gridx = 1;
		gbc_textFieldPageSize.gridy = 3;
		add(textFieldPageSize, gbc_textFieldPageSize);
		textFieldPageSize.setColumns(10);

		JButton btnNextPage = new JButton("Next page");
		btnNextPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainGUI.nextPage();
			}
		});
		btnNextPage.setFont(globalFont);
		GridBagConstraints gbc_btnNextPage = new GridBagConstraints();
		gbc_btnNextPage.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNextPage.insets = new Insets(0, 0, 5, 0);
		gbc_btnNextPage.gridx = 1;
		gbc_btnNextPage.gridy = 4;
		add(btnNextPage, gbc_btnNextPage);

		JButton btnPreviousPage = new JButton("Previous page");
		btnPreviousPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainGUI.previousPage();
			}
		});
		btnPreviousPage.setFont(globalFont);
		GridBagConstraints gbc_btnPreviousPage = new GridBagConstraints();
		gbc_btnPreviousPage.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPreviousPage.insets = new Insets(0, 6, 5, 6);
		gbc_btnPreviousPage.gridx = 0;
		gbc_btnPreviousPage.gridy = 4;
		add(btnPreviousPage, gbc_btnPreviousPage);



	}

	public void initializePanel(WorkImportInfoBean importBean) {
		SwingUtilities.invokeLater(() -> {
			int currentLineNumber = importBean.getCurrentLineNumber();
			textFieldCurrentLine.setText(String.valueOf(currentLineNumber));
			int tableContentLineNumber = importBean.getTableContentLineNumber();
			textFieldPageSize.setText(String.valueOf(tableContentLineNumber));
		});
	}

	public void initializeTotalLineNumber(WorkImportInfoBean importBean) {

		new Thread() {
			@Override
			public void run() {
				try {
					String filePath = importBean.getFilePath();
					boolean skipFirst = importBean.isHasHeader();
					InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(filePath);

					int lineNumber = 0;
					int sz = 1 << 30;
					try (BufferedReader br = new BufferedReader(
							new InputStreamReader(inputStream, StandardCharsets.UTF_8), sz)) {
						String readLine = null;
						while ((readLine = br.readLine()) != null) {
							if (skipFirst) {
								skipFirst = false;
								continue;
							}

							lineNumber++;

						}
					}

					setTotalLineNumber(lineNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}


			};
		}.start();

	}

	private void setTotalLineNumber(int lineNumber) {
		SwingUtilities.invokeLater(() -> {
			String valueOf = String.valueOf(lineNumber);
			textFieldTotalLine.setText(valueOf);
		});
	}

}
