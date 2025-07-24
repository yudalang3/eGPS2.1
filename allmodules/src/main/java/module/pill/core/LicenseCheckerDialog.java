package module.pill.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class LicenseCheckerDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JCheckBox chckbx_showAgain;
	

//	public static void main(String[] args) {
//		try {
//			LicenseCheckerDialog dialog = new LicenseCheckerDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void diposeThis() {
		this.dispose();
	}
	/**
	 * Create the dialog.
	 */
	public LicenseCheckerDialog(Frame owner,  boolean modal) {
		super(owner, "End user agreement license", modal);
		
		getContentPane().setBackground(Color.WHITE);
		setBounds(100, 100, 602, 585);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			String version = "Current version : ";
			version = version.concat(ConfigFileMaintainer.VERSION_INFORMATION);
			contentPanel.setLayout(new BorderLayout(0, 15));
			
			JLabel lblNewLabel = new JLabel(version);
			lblNewLabel.setBackground(Color.LIGHT_GRAY);
			contentPanel.add(lblNewLabel, BorderLayout.NORTH);
		}
		{
			JTextArea textArea = new JTextArea();
			textArea.setLineWrap(true);
			textArea.setEditable(false);
			
			InputStream inputStream = getClass().getResourceAsStream("PathwayIlluminator_licenseTerms.txt");
			
			String text = new BufferedReader(
				      new InputStreamReader(inputStream, StandardCharsets.UTF_8))
				        .lines()
				        .collect(Collectors.joining("\n"));
			textArea.setText(text);
			
			textArea.setCaretPosition(0);
			
			contentPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				chckbx_showAgain = new JCheckBox("Do not show again in this version");
				chckbx_showAgain.setBackground(Color.WHITE);
				chckbx_showAgain.setSelected(true);
				buttonPane.add(chckbx_showAgain);
			}
			{
				JButton okButton = new JButton("I agree");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new Thread(() ->{
							// Note: the value is whether we need to open the license dialog
							String val = Boolean.toString(!chckbx_showAgain.isSelected());
							new ConfigFileMaintainer().putAndSaveValue(CONSTANTS.LICENSE_NAME, val);
							LicenseCheckerDialog.this.diposeThis();
						}).start();
						
					}

				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Reject");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	
	
}
