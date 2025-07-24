package module.evolview.gfamily.work.gui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.BiConsumer;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import graphic.engine.guibean.ColorIcon;
import egps2.UnifiedAccessPoint;

public class CollapseDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private BiConsumer<Color, Integer> callBackConsumer;
	private JButton btnCol;
	private JSpinner spinner;

	final private String triangleSize = "Triangle size";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CollapseDialog dialog = new CollapseDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CollapseDialog(Color initialColor) {
		super(UnifiedAccessPoint.getInstanceFrame(), true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblTraiangleSize = new JLabel(triangleSize);
			lblTraiangleSize.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_lblTraiangleSize = new GridBagConstraints();
			gbc_lblTraiangleSize.insets = new Insets(0, 0, 5, 5);
			gbc_lblTraiangleSize.gridx = 0;
			gbc_lblTraiangleSize.gridy = 0;
			contentPanel.add(lblTraiangleSize, gbc_lblTraiangleSize);
		}
		{

			Preferences userNodeForPackage = Preferences.userNodeForPackage(this.getClass());
			int previousValue = userNodeForPackage.getInt(triangleSize, 15);

			spinner = new JSpinner();
			spinner.setModel(new SpinnerNumberModel(15, 1, 30, 1));
			spinner.setFont(new Font("Arial", Font.PLAIN, 16));
			spinner.setValue(previousValue);
			GridBagConstraints gbc_spinner = new GridBagConstraints();
			gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_spinner.insets = new Insets(0, 0, 5, 0);
			gbc_spinner.gridx = 2;
			gbc_spinner.gridy = 0;
			contentPanel.add(spinner, gbc_spinner);
		}
		{
			JLabel Color = new JLabel("Color");
			Color.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_Color = new GridBagConstraints();
			gbc_Color.insets = new Insets(0, 0, 0, 5);
			gbc_Color.gridx = 0;
			gbc_Color.gridy = 1;
			contentPanel.add(Color, gbc_Color);
		}
		{
			btnCol = new JButton("Col");
			btnCol.setIcon(new ColorIcon(initialColor));
			btnCol.setForeground(initialColor);
			btnCol.setFont(new Font("Arial", Font.PLAIN, 16));
			GridBagConstraints gbc_btnCol = new GridBagConstraints();
			gbc_btnCol.gridx = 2;
			gbc_btnCol.gridy = 1;
			contentPanel.add(btnCol, gbc_btnCol);
			btnCol.addActionListener(e -> {
				Color newColor = JColorChooser.showDialog(this, "Choose color", initialColor);
				btnCol.setForeground(newColor);
				btnCol.setIcon(new ColorIcon(newColor));
			});
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(new Font("Arial", Font.PLAIN, 16));
				okButton.addActionListener(e -> {
					if (callBackConsumer != null) {
						Color background = btnCol.getForeground();
						Integer value = (Integer) spinner.getValue();
						callBackConsumer.accept(background, value);
					}

					Preferences userNodeForPackage = Preferences.userNodeForPackage(this.getClass());
					userNodeForPackage.putInt(triangleSize, (int) spinner.getValue());
					this.dispose();
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Arial", Font.PLAIN, 16));
				cancelButton.addActionListener(e -> {
					this.dispose();
				});
				buttonPane.add(cancelButton);
			}
		}

		this.setLocationRelativeTo(UnifiedAccessPoint.getInstanceFrame());
		this.setSize(270, 200);
	}

	/**
	 * Create the dialog.
	 */
	public CollapseDialog() {
		this(Color.red);
	}

	public void setCallBackConsumer(BiConsumer<Color, Integer> callBackConsumer) {
		this.callBackConsumer = callBackConsumer;
	}
}
