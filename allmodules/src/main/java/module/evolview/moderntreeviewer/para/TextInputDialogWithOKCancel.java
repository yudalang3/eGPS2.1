package module.evolview.moderntreeviewer.para;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class TextInputDialogWithOKCancel extends JDialog {

	private final JTextArea contentPanel = new JTextArea();

	/**
	 * Create the dialog.
	 */
	public TextInputDialogWithOKCancel(String str, Consumer<String> callbackFunction) {
		super(UnifiedAccessPoint.getInstanceFrame(), "Parameter input dialog", true);

		LaunchProperty launchProperty = UnifiedAccessPoint.getLaunchProperty();
		Font defaultFont = launchProperty.getDefaultFont();
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setText(str);
		contentPanel.setFont(defaultFont);

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane jScrollPanel = new JScrollPane(contentPanel);
		getContentPane().add(jScrollPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			{
				JButton cancelButton = new JButton(UnifiedAccessPoint.getResourceString("general.button.cancel"));
				cancelButton.addActionListener(e -> {
					this.dispose();
				});
				cancelButton.setFont(defaultFont);
				buttonPane.add(cancelButton);
			}

			{
				JButton okButton = new JButton(UnifiedAccessPoint.getResourceString("general.button.applyAndClose"));
				okButton.addActionListener(e -> {
					String text = contentPanel.getText();

					this.dispose();
					callbackFunction.accept(text);
				});
				okButton.setFont(defaultFont);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}

		contentPanel.setCaretPosition(0);
	}

}
