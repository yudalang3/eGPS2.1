/**
 * 
 */
package module.heatmap.eheatmap.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import module.heatmap.eheatmap.HeatmapController;

public class EheatmapInputTreeAllDialog extends JDialog {

	private static final long serialVersionUID = 7520499775750910756L;

	private JPanel jContentPane = null;
	private JButton jButtonReplace;
	private Font font = new Font(Font.DIALOG, Font.PLAIN, 12);

	private File inputFile;

	private JButton cancel;

	public EheatmapInputTreeAllDialog(HeatmapController hController) {
		super(UnifiedAccessPoint.getInstanceFrame(), "Input phylogenetc tree located in left", true);
		setSize(350, 150);
		setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
		setContentPane(getJContentPane(hController));
		setResizable(false);
		setIconImage(EGPSShellIcons.get("eGPS_logo16x16.png").getImage());
	}

	private JPanel getJContentPane(HeatmapController hController) {

		jContentPane = new JPanel(null);

		JLabel jLabelInput = new JLabel("Input tree file(nwk/tre/nhx/nex) :");
		jLabelInput.setFont(font);
		jLabelInput.setBounds(25, 10, 330, 25);
		jContentPane.add(jLabelInput);

		JTextField jTextField = new JTextField();
		jTextField.setBounds(25, 42, 200, 25);
		jTextField.setFont(font);
		jContentPane.add(jTextField);

		JButton jButtonOpen = new JButton("Open");
		jButtonOpen.setBounds(240, 42, 95, 25);
		jButtonOpen.setFont(font);
		jButtonOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Preferences pref = Preferences.userNodeForPackage(this.getClass());
				String lastPath = pref.get("lastPath", "");

				JFileChooser jfc = null;
				if (lastPath.length() > 0) {
					jfc = new JFileChooser(lastPath);
				} else {
					jfc = new JFileChooser();
				}

				jfc.setDialogTitle("Open File");
				jfc.setMultiSelectionEnabled(false);
				jfc.setAcceptAllFileFilterUsed(false);

				jfc.addChoosableFileFilter(new OpenFilterTree());

				int result = jfc.showOpenDialog(UnifiedAccessPoint.getInstanceFrame());

				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();

					if (file != null) {
						SwingUtilities.invokeLater(() -> {
							jTextField.setText(file.getName());
							jButtonReplace.setEnabled(true);
						});
						inputFile = file;

						jfc.setCurrentDirectory(file.getParentFile());
						pref.put("lastPath", file.getParent());
					} else {
						if (jTextField.getText().length() == 0) {
							SwingUtilities.invokeLater(() -> {
								jButtonReplace.setEnabled(false);
							});
						}
					}
				}
			}
		});
		jContentPane.add(jButtonOpen);

		cancel = new JButton("Cancel");

		cancel.setFont(font);

		cancel.addActionListener(e -> {
			dispose();
		});

		cancel.setBounds(240, 75, 95, 25);

		jContentPane.add(cancel);

		jButtonReplace = new JButton("OK");
		jButtonReplace.setBounds(140, 75, 95, 25);
		jButtonReplace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (inputFile == null) {
					return;
				}

				hController.incorporateTreeInformationOnleft(inputFile);

				dispose();

			}
		});
		jButtonReplace.setFont(font);
		jButtonReplace.setEnabled(false);
		jContentPane.add(jButtonReplace);

		return jContentPane;
	}

}
